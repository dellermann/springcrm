/*
 * NoteController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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


/**
 * The class {@code NoteController} contains actions which manage notes.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class NoteController extends GeneralController<Note> {

    //-- Constructors ---------------------------

    NoteController() {
        super(Note)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def index() {
        if (params.letter) {
            int max = params.int('max')
            int num = Note.countByTitleLessThan(params.letter.toString())
            params.sort = 'title'
            params.offset = Math.floor(num / max) * max
            params.search = null
        }

        List<Note> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Note.findAllByTitleLike(searchFilter, params)
            count = Note.countByTitleLike(searchFilter)
        } else {
            list = Note.list(params)
            count = Note.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person) {
        List<Note> list = null
        int count = 0
        Map<String, Object> linkParams = null

        if (organization) {
            Organization organizationInstance = Organization.get(organization)
            list = Note.findAllByOrganization(organizationInstance, params)
            count = Note.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            Person personInstance = Person.get(person)
            list = Note.findAllByPerson(personInstance, params)
            count = Note.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }

        getListEmbeddedModel list, count, linkParams
    }

    def save() {
        super.save()
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }
}
