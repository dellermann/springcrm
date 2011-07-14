package org.amcworld.springcrm

import grails.converters.JSON

class OrganizationController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def seqNumberService

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [organizationInstanceList: Organization.list(params), organizationInstanceTotal: Organization.count()]
    }

    def create = {
		def seqNumber = seqNumberService.loadSeqNumber(Organization.class)
        def organizationInstance = new Organization(number:seqNumber.nextNumber)
        organizationInstance.properties = params
        return [organizationInstance: organizationInstance, seqNumberPrefix: seqNumber.prefix]
    }

    def save = {
        def organizationInstance = new Organization(params)
		Organization.withTransaction { status ->
			try {
				seqNumberService.stepFurther(Organization.class)
				organizationInstance.save(failOnError:true)
			} catch (Exception) {
				status.setRollbackOnly()
			}
        }
        if (organizationInstance.hasErrors()) {
            render(view: 'create', model: [organizationInstance: organizationInstance])
        } else {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'organization.label', default: 'Organization'), organizationInstance.toString()])}"
            redirect(action: 'show', id: organizationInstance.id)
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
			def seqNumber = seqNumberService.loadSeqNumber(Organization.class)
            return [organizationInstance: organizationInstance, seqNumberPrefix: seqNumber.prefix]
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
	
	def find = {
		def list = Organization.findAllByNameLike("%${params.name}%", [sort:'name'])
		render(contentType:"text/json") {
			array {
				for (org in list) {
					organization id:org.id, name:org.name
				}
			}
		}
	}
	
	def get = {
        def organizationInstance = Organization.get(params.id)
        if (organizationInstance) {
            render organizationInstance as JSON
        } else {
			render(status:404)
        }
	}
}
