/*
 * ServiceController.groovy
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
 * The class {@code ServiceController} contains actions which manage services.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class ServiceController {

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
			int num = Service.countByNameLessThan(params.letter)
			params.sort = 'name'
			params.offset = Math.floor(num / params.max) * params.max
		}
        return [serviceInstanceList: Service.list(params), serviceInstanceTotal: Service.count()]
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
        return [serviceInstanceList: list, serviceInstanceTotal: count]
	}

    def create() {
        def serviceInstance = new Service()
        serviceInstance.properties = params
        return [serviceInstance: serviceInstance]
    }

	def copy() {
		def serviceInstance = Service.get(params.id)
		if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])
            redirect(action: 'list')
            return
        }

		serviceInstance = new Service(serviceInstance)
		render(view: 'create', model: [serviceInstance: serviceInstance])
	}

    def save() {
        def serviceInstance = new Service(params)
        if (!serviceInstance.save(flush: true)) {
            render(view: 'create', model: [serviceInstance: serviceInstance])
            return
        }
        params.id = serviceInstance.ident()

		serviceInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: serviceInstance.id)
		}
    }

    def show() {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])
            redirect(action: 'list')
            return
        }

        return [serviceInstance: serviceInstance]
    }

    def edit() {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])
            redirect(action: 'list')
            return
        }

        return [serviceInstance: serviceInstance]
    }

    def update() {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (serviceInstance.version > version) {
                serviceInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'service.label', default: 'Service')] as Object[], 'Another user has updated this Service while you were editing')
                render(view: 'edit', model: [serviceInstance: serviceInstance])
                return
            }
        }

        /*
         * Implementation notes:
         *
         * There are a lot of problems when we try to simply use data binding
         * and 1:n relation storage in GORM.  It simply doesn't work, at least
         * in Grails 2.1.1.
         *
         * I change the way data binding works.  The client submits the items
         * as array (items[...]) with subsequent indices.  The client must
         * ensure that no gaps (i. e. items[0], items[1], items[3], ...) are
         * submitted.  The following implementation simply clears the items
         * list and adds all submitted items, which itself undergo data
         * binding, to the list.  A disadvantage of this way is that GORM
         * deletes all previous items and then adds back all new items which is
         * somewhat inefficient and permanently allocates new IDs.
         */
        def pricing = serviceInstance.pricing
        if (pricing == null) {
            pricing = new SalesItemPricing()
            serviceInstance.pricing = pricing
        }
        if (pricing.items == null) {
            pricing.items = []
        } else {
            pricing.items.clear()
        }
        for (int i = 0; params.pricing?."items[${i}]"; i++) {
            pricing.addToItems(params.pricing."items[${i}]")
        }

		if (params.autoNumber) {
			params.number = serviceInstance.number
		}
        serviceInstance.properties = params.findAll { !it.key.startsWith('pricing.') && it.key != 'pricing' }

        if (!serviceInstance.save(flush: true)) {
            render(view: 'edit', model: [serviceInstance: serviceInstance])
            return
        }

		serviceInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'service.label', default: 'Service'), serviceInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: serviceInstance.id)
		}
    }

    def delete() {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'service.label', default: 'Service'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

        try {
            serviceInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'service.label', default: 'Service')])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'service.label', default: 'Service')])
            redirect(action: 'show', id: params.id)
        }
    }

	def get() {
        def serviceInstance = Service.get(params.id)
        if (!serviceInstance) {
			render(status: 404)
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
