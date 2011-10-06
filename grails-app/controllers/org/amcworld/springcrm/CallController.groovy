package org.amcworld.springcrm

class CallController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.letter) {
			int num = Call.countBySubjectLessThan(params.letter)
			params.sort = 'subject'
			params.offset = Math.floor(num / params.max) * params.max
		}
        [callInstanceList: Call.list(params), callInstanceTotal: Call.count()]
    }
	
	def listEmbedded = {
		def l
		def count
		def linkParams
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = Call.findAllByOrganization(organizationInstance, params)
			count = Call.countByOrganization(organizationInstance)
			linkParams = [organization:organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = Call.findAllByPerson(personInstance, params)
			count = Call.countByPerson(personInstance)
			linkParams = [person:personInstance.id]
		}
		[callInstanceList:l, callInstanceTotal:count, linkParams:linkParams]
	}

    def create = {
        def callInstance = new Call()
        callInstance.properties = params
		if (callInstance.person) {
			callInstance.phone = callInstance.person.phone
			callInstance.organization = callInstance.person.organization
		} else if (callInstance.organization) {
			callInstance.phone = callInstance.organization.phone
		}
        return [callInstance: callInstance]
    }

	def copy = {
        def callInstance = Call.get(params.id)
        if (callInstance) {
			callInstance = new Call(callInstance)
			render(view:'create', model:[callInstance:callInstance])
        } else {
            flash.message = "${message(code:'default.not.found.message', args:[message(code:'call.label', default:'Call'), params.id])}"
			redirect(action: 'show', id: callInstance.id)
        }
	}

    def save = {
        def callInstance = new Call(params)
        if (callInstance.save(flush: true)) {
			callInstance.index()
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'call.label', default: 'Call'), callInstance.toString()])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'show', id: callInstance.id)
			}
        } else {
            render(view: 'create', model: [callInstance: callInstance])
        }
    }

    def show = {
        def callInstance = Call.get(params.id)
        if (!callInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])}"
            redirect(action: 'list')
        } else {
            [callInstance: callInstance]
        }
    }

    def edit = {
        def callInstance = Call.get(params.id)
        if (!callInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])}"
            redirect(action: 'list')
        } else {
            return [callInstance: callInstance]
        }
    }

    def update = {
        def callInstance = Call.get(params.id)
        if (callInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (callInstance.version > version) {
                    callInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'call.label', default: 'Call')] as Object[], 'Another user has updated this Call while you were editing')
                    render(view: 'edit', model: [callInstance: callInstance])
                    return
                }
            }
            callInstance.properties = params
            if (!callInstance.hasErrors() && callInstance.save(flush: true)) {
				callInstance.reindex()
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'call.label', default: 'Call'), callInstance.toString()])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'show', id: callInstance.id)
				}
            } else {
                render(view: 'edit', model: [callInstance: callInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def callInstance = Call.get(params.id)
        if (callInstance) {
            try {
                callInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'call.label', default: 'Call')])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'call.label', default: 'Call')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }
}
