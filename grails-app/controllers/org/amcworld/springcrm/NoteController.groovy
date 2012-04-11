/*
 * NoteController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code NoteController} contains actions which manage notes.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class NoteController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

	def seqNumberService


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.letter) {
			int num = Note.countByTitleLessThan(params.letter)
			params.sort = 'title'
			params.offset = Math.floor(num / params.max) * params.max
		}
        return [noteInstanceList: Note.list(params), noteInstanceTotal: Note.count()]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = Note.findAllByOrganization(organizationInstance, params)
			count = Note.countByOrganization(organizationInstance)
			linkParams = [organization: organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = Note.findAllByPerson(personInstance, params)
			count = Note.countByPerson(personInstance)
			linkParams = [person: personInstance.id]
		}
		return [noteInstanceList: l, noteInstanceTotal: count, linkParams: linkParams]
	}

    def create() {
        def noteInstance = new Note()
        noteInstance.properties = params
        return [noteInstance: noteInstance]
    }

	def copy() {
        def noteInstance = Note.get(params.id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])
            redirect(action: 'list')
            return
        }

		noteInstance = new Note(noteInstance)
		render(view: 'create', model: [noteInstance: noteInstance])
	}

    def save() {
        def noteInstance = new Note(params)
        if (!noteInstance.save(flush: true)) {
            render(view: 'create', model: [noteInstance: noteInstance])
            return
        }
        params.id = noteInstance.ident()

		noteInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'note.label', default: 'Note'), noteInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: noteInstance.id)
		}
    }

    def show() {
        def noteInstance = Note.get(params.id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])
            redirect(action: 'list')
            return
        }

        return [noteInstance: noteInstance]
    }

    def edit() {
        def noteInstance = Note.get(params.id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])
            redirect(action: 'list')
            return
        }

        return [noteInstance: noteInstance]
    }

    def update() {
        def noteInstance = Note.get(params.id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (noteInstance.version > version) {
                noteInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'note.label', default: 'Note')] as Object[], "Another user has updated this Note while you were editing")
                render(view: 'edit', model: [noteInstance: noteInstance])
                return
            }
        }
		if (params.autoNumber) {
			params.number = noteInstance.number
		}
        noteInstance.properties = params
        if (!noteInstance.save(flush: true)) {
            render(view: 'edit', model: [noteInstance: noteInstance])
            return
        }

		noteInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'note.label', default: 'Note'), noteInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: noteInstance.id)
		}
    }

    def delete() {
        def noteInstance = Note.get(params.id)
        if (!noteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

        try {
            noteInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'note.label', default: 'Note')])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'note.label', default: 'Note')])
            redirect(action: 'show', id: params.id)
        }
    }
}
