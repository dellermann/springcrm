package org.amcworld.springcrm

class CallController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [callInstanceList: Call.list(params), callInstanceTotal: Call.count()]
    }

    def create = {
        def callInstance = new Call()
        callInstance.properties = params
		if (callInstance.person) {
			callInstance.phone = callInstance.person.phone
		} else if (callInstance.organization) {
			callInstance.phone = callInstance.organization.phone
		}
        return [callInstance: callInstance]
    }

    def save = {
        def callInstance = new Call(params)
        if (callInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'call.label', default: 'Call'), callInstance.toString()])}"
            redirect(action: 'show', id: callInstance.id)
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
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'call.label', default: 'Call'), callInstance.toString()])}"
                redirect(action: 'show', id: callInstance.id)
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
                redirect(action: 'list')
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'call.label', default: 'Call')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])}"
            redirect(action: 'list')
        }
    }
}
