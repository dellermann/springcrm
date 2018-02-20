/*
 * NoteController.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm

import static org.springframework.http.HttpStatus.*

import grails.validation.ValidationException
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.RequestAttribute


/**
 * The class {@code NoteController} contains actions which manage notes.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class NoteController {

    //-- Fields -------------------------------------

    NoteService noteService
    OrganizationService organizationService
    PersonService personService


    //-- Public methods -------------------------

    def copy(Note note) {
        respond new Note(note), view: 'create'
    }

    def create() {
        respond new Note(params)
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        Note note = noteService.delete new ObjectId(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.deleted.message',
                    args: [message(code: 'note.label'), note]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    def edit(String id) {
        respond id == null ? null : noteService.get(new ObjectId(id))
    }

    def index() {
        String letter = params.letter?.toString()
        if (letter) {
            int max = params.int('max')
            int num = noteService.countByTitleLessThan(letter)
            params.sort = 'title'
            params.offset = (int) (Math.floor(num.toDouble() / max) * max)
            params.remove 'search'
        }

        List<Note> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = noteService.findAllByTitleLike(searchFilter, params)
            count = noteService.countByTitleLike(searchFilter)
        } else {
            list = noteService.list(params)
            count = noteService.count()
        }

        respond list, model: [noteCount: count]
    }

    def listEmbedded(@RequestAttribute('organization') String organizationId,
                     @RequestAttribute('person') String personId) {
        List<Note> list = null
        Map model = null

        if (organizationId) {
            Organization organization =
                organizationService.get(new ObjectId(organizationId))
            if (organization != null) {
                list = noteService.findAllByOrganization(organization, params)
                model = [
                    noteCount: noteService.countByOrganization(organization),
                    linkParams: [organization: organization.id.toString()]
                ]
            }
        } else if (personId) {
            Person person = personService.get(new ObjectId(personId))
            if (person != null) {
                list = noteService.findAllByPerson(person, params)
                model = [
                    noteCount: noteService.countByPerson(person),
                    linkParams: [person: person.id.toString()]
                ]
            }
        }

        respond list, model: model
    }

    def save(Note note) {
        if (note == null) {
            notFound()
            return
        }

        try {
            noteService.save note
        } catch (ValidationException ignored) {
            respond note.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.created.message',
                    args: [message(code: 'note.label'), note]
                ) as Object
                redirect note
            }
            '*' { respond note, [status: CREATED] }
        }
    }

    def show(String id) {
        respond id == null ? null : noteService.get(new ObjectId(id))
    }

    def update(Note note) {
        if (note == null) {
            notFound()
            return
        }

        try {
            noteService.save note
        } catch (ValidationException ignored) {
            respond note.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.updated.message',
                    args: [message(code: 'note.label'), note]
                ) as Object
                redirect note
            }
            '*' { respond note, [status: OK] }
        }
    }


    //-- Non-public methods ---------------------

    private void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'note.label'), params.id]
                ) as Object
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
