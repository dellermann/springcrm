package org.amcworld.springcrm

class NoteController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def seqNumberService

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.letter) {
			int num = Note.countByTitleLessThan(params.letter)
			params.sort = 'title'
			params.offset = Math.floor(num / params.max) * params.max
		}
        [noteInstanceList: Note.list(params), noteInstanceTotal: Note.count()]
    }

	def listEmbedded = {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = Note.findAllByOrganization(organizationInstance, params)
			count = Note.countByOrganization(organizationInstance)
			linkParams = [organization:organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = Note.findAllByPerson(personInstance, params)
			count = Note.countByPerson(personInstance)
			linkParams = [person:personInstance.id]
		}
		[noteInstanceList:l, noteInstanceTotal:count, linkParams:linkParams]
	}

    def create = {
        def noteInstance = new Note()
        noteInstance.properties = params
        return [noteInstance: noteInstance]
    }

	def copy = {
        def noteInstance = Note.get(params.id)
        if (noteInstance) {
			noteInstance = new Note(noteInstance)
			render(view:'create', model:[noteInstance:noteInstance])
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])}"
			redirect(action: 'show', id: noteInstance.id)
        }
	}

    def save = {
        def noteInstance = new Note(params)
        if (noteInstance.save(flush: true)) {
			noteInstance.index()
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'note.label', default: 'Note'), noteInstance.toString()])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'show', id: noteInstance.id)
			}
        } else {
            render(view: 'create', model: [noteInstance: noteInstance])
        }
    }

    def show = {
        def noteInstance = Note.get(params.id)
        if (!noteInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])}"
            redirect(action: 'list')
        } else {
            [noteInstance: noteInstance]
        }
    }

    def edit = {
        def noteInstance = Note.get(params.id)
        if (!noteInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])}"
            redirect(action: 'list')
        } else {
            return [noteInstance: noteInstance]
        }
    }

    def update = {
        def noteInstance = Note.get(params.id)
        if (noteInstance) {
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
            if (!noteInstance.hasErrors() && noteInstance.save(flush: true)) {
				noteInstance.reindex()
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'note.label', default: 'Note'), noteInstance.toString()])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'show', id: noteInstance.id)
				}
            } else {
                render(view: 'edit', model: [noteInstance: noteInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def noteInstance = Note.get(params.id)
        if (noteInstance && params.confirmed) {
            try {
                noteInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'note.label', default: 'Note')])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'note.label', default: 'Note')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'note.label', default: 'Note'), params.id])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }
}
