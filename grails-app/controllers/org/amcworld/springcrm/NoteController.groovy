/*
 * NoteController.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * @author	Daniel Ellermann
 * @version 1.3
 */
class NoteController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        if (params.letter) {
            int num = Note.countByTitleLessThan(params.letter)
            params.sort = 'title'
            params.offset = Math.floor(num / params.max) * params.max
            params.search = null
        }

        def list, count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Note.findAllByTitleLike(searchFilter, params)
            count = Note.countByTitleLike(searchFilter)
        } else {
            list = Note.list(params)
            count = Note.count()
        }

        [noteInstanceList: list, noteInstanceTotal: count]
    }

    def listEmbedded(Long organization, Long person) {
        def l
        def count
        def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            def organizationInstance = Organization.get(organization)
            l = Note.findAllByOrganization(organizationInstance, params)
            count = Note.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            def personInstance = Person.get(person)
            l = Note.findAllByPerson(personInstance, params)
            count = Note.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }
        [noteInstanceList: l, noteInstanceTotal: count, linkParams: linkParams]
    }

    def create() {
        def noteInstance = new Note()
        noteInstance.properties = params
        [noteInstance: noteInstance]
    }

    def copy(Long id) {
        def noteInstance = Note.get(id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), id])
            redirect action: 'list'
            return
        }

        noteInstance = new Note(noteInstance)
        render view: 'create', model: [noteInstance: noteInstance]
    }

    def save() {
        def noteInstance = new Note(params)
        if (!noteInstance.save(flush: true)) {
            render view: 'create', model: [noteInstance: noteInstance]
            return
        }

        request.noteInstance = noteInstance
        flash.message = message(code: 'default.created.message', args: [message(code: 'note.label', default: 'Note'), noteInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: noteInstance.id
        }
    }

    def show(Long id) {
        def noteInstance = Note.get(id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), id])
            redirect action: 'list'
            return
        }

        [noteInstance: noteInstance]
    }

    def edit(Long id) {
        def noteInstance = Note.get(id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), id])
            redirect action: 'list'
            return
        }

        [noteInstance: noteInstance]
    }

    def update(Long id) {
        def noteInstance = Note.get(id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (noteInstance.version > version) {
                noteInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'note.label', default: 'Note')] as Object[], "Another user has updated this Note while you were editing")
                render view: 'edit', model: [noteInstance: noteInstance]
                return
            }
        }
        if (params.autoNumber) {
            params.number = noteInstance.number
        }
        noteInstance.properties = params
        if (!noteInstance.save(flush: true)) {
            render view: 'edit', model: [noteInstance: noteInstance]
            return
        }

        request.noteInstance = noteInstance
        flash.message = message(code: 'default.updated.message', args: [message(code: 'note.label', default: 'Note'), noteInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: noteInstance.id
        }
    }

    def delete(Long id) {
        def noteInstance = Note.get(id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
            return
        }

        try {
            noteInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'note.label', default: 'Note')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'note.label', default: 'Note')])
            redirect action: 'show', id: id
        }
    }
}
