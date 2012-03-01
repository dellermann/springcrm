/*
 * CallController.groovy
 *
 * Copyright (c) 2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


package org.amcworld.springcrm

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code CallController} contains actions which manage phone calls
 * associated to an organization or person.
 *
 * @author	Daniel Ellermann
 * @version 0.9
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
			params.offset = Math.floor(num / params.max) * params.max
		}
        return [callInstanceList: Call.list(params), callInstanceTotal: Call.count()]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = Call.findAllByOrganization(organizationInstance, params)
			count = Call.countByOrganization(organizationInstance)
			linkParams = [organization: organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = Call.findAllByPerson(personInstance, params)
			count = Call.countByPerson(personInstance)
			linkParams = [person: personInstance.id]
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
            redirect(action: 'list')
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
        if (callInstance && params.confirmed) {
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
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'call.label', default: 'Call'), params.id])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }
}
