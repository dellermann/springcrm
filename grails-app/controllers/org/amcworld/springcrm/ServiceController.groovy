/*
 * ServiceController.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

import grails.converters.JSON


/**
 * The class {@code ServiceController} contains actions which manage services.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class ServiceController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    SalesItemService salesItemService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (params.letter) {
            int num = Service.countByNameLessThan(params.letter)
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }

        [
            serviceInstanceList: Service.list(params),
            serviceInstanceTotal: Service.count()
        ]
    }

    def selectorList() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        String searchFilter =
            params.search ? "%${params.search}%".toString() : ''
        if (params.letter) {
            int num
            if (params.search) {
                num = Service.countByNameLessThanAndNameLike(
                    params.letter, searchFilter
                )
            } else {
                num = Service.countByNameLessThan(params.letter)
            }
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }

        List<Service> list
        int count
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
        [serviceInstance: new Service(params)]
    }

    def copy(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'service.label'), id]
            )
            redirect action: 'index'
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

        request.serviceInstance = serviceInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'service.label'), serviceInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: serviceInstance.id
        }
    }

    def show(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'service.label'), id]
            )
            redirect action: 'index'
            return
        }

        [serviceInstance: serviceInstance]
    }

    def edit(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'service.label'), id]
            )
            redirect action: 'index'
            return
        }

        [serviceInstance: serviceInstance]
    }

    def update(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'service.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (serviceInstance.version > version) {
                serviceInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'service.label')] as Object[],
                    'Another user has updated this Service while you were editing'
                )
                render view: 'edit', model: [serviceInstance: serviceInstance]
                return
            }
        }

        if (!salesItemService.saveSalesItemPricing(serviceInstance, params)) {
            render view: 'edit', model: [serviceInstance: serviceInstance]
            return
        }

        request.serviceInstance = serviceInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'service.label'), serviceInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: serviceInstance.id
        }
    }

    def delete(Long id) {
        def serviceInstance = Service.get(id)
        if (!serviceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'service.label'), id]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        try {
            if (serviceInstance.pricing) {
                serviceInstance.pricing.delete flush: true
            }
            serviceInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'service.label')]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'service.label')]
            )
            redirect action: 'show', id: id
        }
    }

    def get(Long id) {
        def serviceInstance = Service.read(id)
        if (!serviceInstance) {
            render status: SC_NOT_FOUND
            return
        }

        JSON.use('deep') {
            render(contentType: 'text/json') {
                fullNumber = serviceInstance.fullNumber
                inventoryItem = serviceInstance
            }
        }
    }

    def find(String name) {
        Integer number = null
        try {
            number = name as Integer
        } catch (NumberFormatException ignored) { /* ignored */ }

        def c = Service.createCriteria()
        List<Service> list = c.list {
            or {
                eq 'number', number
                ilike 'name', "%${name}%"
            }
            order 'number', 'asc'
        }

        render(contentType: 'text/json') {
            array {
                for (Service s in list) {
                    service id: s.id, name: s.name
                }
            }
        }
    }
}
