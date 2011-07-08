package org.amcworld.springcrm

import grails.converters.JSON

class ServiceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "GET"]

	def seqNumberService
	
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [serviceInstanceList: Service.list(params), serviceInstanceTotal: Service.count()]
    }
	
	def selectorList = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [serviceInstanceList: Service.list(params), serviceInstanceTotal: Service.count()]
	}

    def create = {
		def seqNumber = seqNumberService.loadSeqNumber(Service.class)
        def serviceInstance = new Service(number:seqNumber.nextNumber)
        serviceInstance.properties = params
        return [serviceInstance: serviceInstance, seqNumberPrefix: seqNumber.prefix]
    }

    def save = {
        def serviceInstance = new Service(params)
		Service.withTransaction { status ->
			try {
				seqNumberService.stepFurther(Service.class)
				serviceInstance.save(failOnError:true)
			} catch (Exception) {
				status.setRollbackOnly()
			}
		}
        if (serviceInstance.hasErrors()) {
            render(view: "create", model: [serviceInstance: serviceInstance])
        } else {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.toString()])}"
            redirect(action: "show", id: serviceInstance.id)
        }
    }

    def show = {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        } else {
            [serviceInstance: serviceInstance]
        }
    }

    def edit = {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        } else {
            return [serviceInstance: serviceInstance]
        }
    }

    def update = {
        def serviceInstance = Service.get(params.id)
        if (serviceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (serviceInstance.version > version) {
                    
                    serviceInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'service.label', default: 'Service')] as Object[], "Another user has updated this Service while you were editing")
                    render(view: "edit", model: [serviceInstance: serviceInstance])
                    return
                }
            }
            serviceInstance.properties = params
            if (!serviceInstance.hasErrors() && serviceInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.toString()])}"
                redirect(action: "show", id: serviceInstance.id)
            } else {
                render(view: "edit", model: [serviceInstance: serviceInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def serviceInstance = Service.get(params.id)
        if (serviceInstance) {
            try {
                serviceInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'service.label', default: 'Service')])}"
                redirect(action: "list")
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'service.label', default: 'Service')])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def get = {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
			render(status: 404)
        } else {
			JSON.use("deep") {
				render(contentType:"text/json") {
					fullNumber = serviceInstance.fullNumber
					inventoryItem = serviceInstance
				}
			}
        }
	}
}
