/*
 * CallController.groovy
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
 * The class {@code CallController} contains actions which manage phone calls
 * associated to an organization or person.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class CallController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        if (params.letter) {
            int num = Call.countBySubjectLessThan(params.letter)
            params.sort = 'subject'
            params.offset = (Math.floor(num / params.max) * params.max) as Integer
            params.search = null
        }

        def list, count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Call.findAllBySubjectLike(searchFilter, params)
            count = Call.countBySubjectLike(searchFilter)
        } else {
            list = Call.list(params)
            count = Call.count()
        }

        return [callInstanceList: list, callInstanceTotal: count]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
            if (organizationInstance) {
    			l = Call.findAllByOrganization(organizationInstance, params)
    			count = Call.countByOrganization(organizationInstance)
    			linkParams = [organization: organizationInstance.id]
            }
		} else if (params.person) {
			def personInstance = Person.get(params.person)
            if (personInstance) {
    			l = Call.findAllByPerson(personInstance, params)
    			count = Call.countByPerson(personInstance)
    			linkParams = [person: personInstance.id]
            }
		}
		return [callInstanceList: l, callInstanceTotal: count, linkParams: linkParams]
	}

    def create() {
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

	def copy() {
        def callInstance = Call.get(params.id)
        if (!callInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])
            redirect(action: 'show', id: params.id)
            return
        }

		callInstance = new Call(callInstance)
		render(view: 'create', model: [callInstance: callInstance])
	}

    def save() {
        def callInstance = new Call(params)
        if (!callInstance.save(flush: true)) {
            render(view: 'create', model: [callInstance: callInstance])
            return
        }
        params.id = callInstance.ident()

		callInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'call.label', default: 'Call'), callInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: callInstance.id)
		}
    }

    def show() {
        def callInstance = Call.get(params.id)
        if (!callInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])
            redirect(action: 'list')
            return
        }

        return [callInstance: callInstance]
    }

    def edit() {
        def callInstance = Call.get(params.id)
        if (!callInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])
            redirect(action: 'list')
            return
        }

        return [callInstance: callInstance]
    }

    def update() {
        def callInstance = Call.get(params.id)
        if (!callInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (callInstance.version > version) {
                callInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'call.label', default: 'Call')] as Object[], 'Another user has updated this Call while you were editing')
                render(view: 'edit', model: [callInstance: callInstance])
                return
            }
        }
        callInstance.properties = params
        if (!callInstance.save(flush: true)) {
            render(view: 'edit', model: [callInstance: callInstance])
            return
        }

		callInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'call.label', default: 'Call'), callInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: callInstance.id)
		}
    }

    def delete() {
        def callInstance = Call.get(params.id)
        if (!callInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

        try {
            callInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'call.label', default: 'Call')])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'call.label', default: 'Call')])
            redirect(action: 'show', id: params.id)
        }
    }
}
