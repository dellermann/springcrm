/*
 * ServiceController.groovy
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

import grails.converters.JSON
import javax.servlet.http.HttpServletResponse


/**
 * The class {@code ServiceController} contains actions which manage services.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class ServiceController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    LruService lruService
    SalesItemService salesItemService


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (params.letter) {
            int num = Service.countByNameLessThan(params.letter)
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }
        [serviceInstanceList: Service.list(params), serviceInstanceTotal: Service.count()]
    }

    def selectorList() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        String searchFilter = params.search ? "%${params.search}%".toString() : ''
        if (params.letter) {
            int num
            if (params.search) {
                num = Service.countByNameLessThanAndNameLike(params.letter, searchFilter)
            } else {
                num = Service.countByNameLessThan(params.letter)
            }
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }
        def list, count
        if (params.search) {
            list = Service.findAllByNameLike(searchFilter, params)
            count = Service.countByNameLike(searchFilter)
        } else {
            list = Service.list(params)
            count = Service.count()
        }
        [serviceInstanceList: list, serviceInstanceTotal: count]
    }

    def create() {
        def serviceInstance = new Service()
        serviceInstance.properties = params
        [serviceInstance: serviceInstance]
    }

    def copy(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), id])
            redirect action: 'list'
            return
        }

        serviceInstance = new Service(serviceInstance)
        render view: 'create', model: [serviceInstance: serviceInstance]
    }

    def save() {
        def serviceInstance = new Service(params)
        if (!salesItemService.saveSalesItemPricing(serviceInstance, params)) {
            render view: 'create', model: [serviceInstance: serviceInstance]
            return
        }

        lruService.recordItem controllerName, serviceInstance
        serviceInstance.index()

        flash.message = message(code: 'default.created.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: serviceInstance.id
        }
    }

    def show(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), id])
            redirect action: 'list'
            return
        }

        [serviceInstance: serviceInstance]
    }

    def edit(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), id])
            redirect action: 'list'
            return
        }

        [serviceInstance: serviceInstance]
    }

    def update(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (serviceInstance.version > version) {
                serviceInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'service.label', default: 'Service')] as Object[], 'Another user has updated this Service while you were editing')
                render view: 'edit', model: [serviceInstance: serviceInstance]
                return
            }
        }

        if (!salesItemService.saveSalesItemPricing(serviceInstance, params)) {
            render view: 'edit', model: [serviceInstance: serviceInstance]
            return
        }

        lruService.recordItem controllerName, serviceInstance
        serviceInstance.reindex()

        flash.message = message(code: 'default.updated.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: serviceInstance.id
        }
    }

    def delete(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
            return
        }

        try {
            serviceInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'service.label', default: 'Service')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'service.label', default: 'Service')])
            redirect action: 'show', id: id
        }
    }

    def get(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        JSON.use('deep') {
            render(contentType: 'text/json') {
                fullNumber = serviceInstance.fullNumber
                inventoryItem = serviceInstance
            }
        }
    }
}
