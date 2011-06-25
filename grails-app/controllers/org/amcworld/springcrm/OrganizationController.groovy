package org.amcworld.springcrm

import grails.converters.JSON

class OrganizationController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [organizationInstanceList: Organization.list(params), organizationInstanceTotal: Organization.count()]
    }

    def create = {
		def res = Organization.executeQuery('select max(o.number)+1 from Organization o')
        def organizationInstance = new Organization(number:res[0] ?: 10000)
        organizationInstance.properties = params
        return [organizationInstance: organizationInstance]
    }

    def save = {
        def organizationInstance = new Organization(params)
        if (organizationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'organization.label', default: 'Organization'), organizationInstance.toString()])}"
            redirect(action: 'show', id: organizationInstance.id)
        } else {
            render(view: 'create', model: [organizationInstance: organizationInstance])
        }
    }

    def show = {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])}"
            redirect(action: 'list')
        } else {
            [organizationInstance: organizationInstance]
        }
    }

    def edit = {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])}"
            redirect(action: 'list')
        } else {
            return [organizationInstance: organizationInstance]
        }
    }

    def update = {
        def organizationInstance = Organization.get(params.id)
        if (organizationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (organizationInstance.version > version) {
                    
                    organizationInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'organization.label', default: 'Organization')] as Object[], 'Another user has updated this Organization while you were editing')
                    render(view: 'edit', model: [organizationInstance: organizationInstance])
                    return
                }
            }
            organizationInstance.properties = params
            if (!organizationInstance.hasErrors() && organizationInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'organization.label', default: 'Organization'), organizationInstance.toString()])}"
                redirect(action: 'show', id: organizationInstance.id)
            } else {
                render(view: 'edit', model: [organizationInstance: organizationInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def organizationInstance = Organization.get(params.id)
        if (organizationInstance) {
            try {
                organizationInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'organization.label', default: 'Organization')])}"
                redirect(action: 'list')
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'organization.label', default: 'Organization')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'organization.label', default: 'Organization'), params.id])}"
            redirect(action: 'list')
        }
    }
	
	def get = {
        def organizationInstance = Organization.get(params.id)
        if (!organizationInstance) {
			render(status: 404)
        } else {
            render organizationInstance as JSON
        }
	}
}
