/*
 * OrganizationController.groovy
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

import grails.converters.JSON
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code OrganizationController} contains actions which manage
 * organizations.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class OrganizationController {

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
			int num = Organization.countByNameLessThan(params.letter)
			params.sort = 'name'
			params.offset = Math.floor(num / params.max) * params.max
		}
		def list, count
		def type = params.type
		if (type) {
			List<Byte> types = [new Byte(type), 3 as byte]
			list = Organization.findAllByRecTypeInList(types, params)
			count = Organization.countByRecTypeInList(types)
		} else {
			list = Organization.list(params)
			count = Organization.count()
		}
        return [organizationInstanceList: list, organizationInstanceTotal: count]
    }

    def create() {
        def organizationInstance = new Organization()
        organizationInstance.properties = params
        return [organizationInstance: organizationInstance]
    }

	def copy() {
		def organizationInstance = Organization.get(params.id)
		if (!organizationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])
            redirect(action: 'list')
            return
        }

		organizationInstance = new Organization(organizationInstance)
		render(view: 'create', model: [organizationInstance: organizationInstance])
	}

    def save() {
        def organizationInstance = new Organization(params)
        if (!organizationInstance.save(flush: true)) {
            render(view: 'create', model: [organizationInstance: organizationInstance])
            return
        }
        params.id = organizationInstance.ident()

		organizationInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'organization.label', default: 'Organization'), organizationInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: organizationInstance.id)
		}
    }

    def show() {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])
            redirect(action: 'list')
            return
        }

        return [organizationInstance: organizationInstance]
    }

    def edit() {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])
            redirect(action: 'list')
            return
        }

        return [organizationInstance: organizationInstance]
    }

    def update() {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (organizationInstance.version > version) {
                organizationInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'organization.label', default: 'Organization')] as Object[], 'Another user has updated this Organization while you were editing')
                render(view: 'edit', model: [organizationInstance: organizationInstance])
                return
            }
        }
		if (params.autoNumber) {
			params.number = organizationInstance.number
		}
        organizationInstance.properties = params
        if (!organizationInstance.save(flush: true)) {
            render(view: 'edit', model: [organizationInstance: organizationInstance])
            return
        }

		organizationInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'organization.label', default: 'Organization'), organizationInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: organizationInstance.id, params: [type: params.listType])
		}
    }

    def delete() {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list', params: [type: params.type])
            }
            return
        }

        try {
            organizationInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'organization.label', default: 'Organization')])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list', params: [type: params.type])
			}
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'organization.label', default: 'Organization')])
            redirect(action: 'show', id: params.id, params: [type: params.type])
        }
    }

	def find() {
		def list
		def type = params.type
		if (type) {
			List<Byte> types = [new Byte(type), 3 as byte]
			list = Organization.findAllByRecTypeInListAndNameLike(types, "%${params.name}%", [sort: 'name'])
		} else {
			list = Organization.findAllByNameLike("%${params.name}%", [sort: 'name'])
		}
		render(contentType: "text/json") {
			array {
				for (org in list) {
					organization id: org.id, name: org.name
				}
			}
		}
	}

	def get() {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            render(status: 404)
            return
        }

        render organizationInstance as JSON
	}

	def getPhoneNumbers() {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            render(status: 404)
            return
        }

		def phoneNumbers = [
			organizationInstance.phone,
			organizationInstance.phoneOther,
			organizationInstance.fax
		]
		render phoneNumbers as JSON
	}
}
