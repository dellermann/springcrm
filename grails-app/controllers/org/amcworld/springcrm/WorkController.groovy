/*
 * WorkController.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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
 * The class {@code WorkController} contains actions which manage works
 * (services).
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class WorkController {

    //-- Class fiels ----------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    SalesItemService salesItemService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (params.letter) {
            int num = Work.countByNameLessThan(params.letter)
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }

        [workInstanceList: Work.list(params), workInstanceTotal: Work.count()]
    }

    def selectorList() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        String searchFilter =
            params.search ? "%${params.search}%".toString() : ''
        if (params.letter) {
            int num
            if (params.search) {
                num = Work.countByNameLessThanAndNameLike(
                    params.letter, searchFilter
                )
            } else {
                num = Work.countByNameLessThan(params.letter)
            }
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }

        List<Work> list
        int count
        if (params.search) {
            list = Work.findAllByNameLike(searchFilter, params)
            count = Work.countByNameLike(searchFilter)
        } else {
            list = Work.list(params)
            count = Work.count()
        }

        [workInstanceList: list, workInstanceTotal: count]
    }

    def create() {
        [workInstance: new Work(params)]
    }

    def copy(Long id) {
        def workInstance = Work.get(id)
        if (!workInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'work.label'), id]
            )
            redirect action: 'index'
            return
        }

        workInstance = new Work(workInstance)
        render view: 'create', model: [workInstance: workInstance]
    }

    def save() {
        def workInstance = new Work(params)
        if (!salesItemService.saveSalesItemPricing(workInstance, params)) {
            render view: 'create', model: [workInstance: workInstance]
            return
        }

        request.workInstance = workInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'work.label'), workInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: workInstance.id
        }
    }

    def show(Long id) {
        def workInstance = Work.get(id)
        if (!workInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'work.label'), id]
            )
            redirect action: 'index'
            return
        }

        [workInstance: workInstance]
    }

    def edit(Long id) {
        def workInstance = Work.get(id)
        if (!workInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'work.label'), id]
            )
            redirect action: 'index'
            return
        }

        [workInstance: workInstance]
    }

    def update(Long id) {
        def workInstance = Work.get(id)
        if (!workInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'work.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (workInstance.version > version) {
                workInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'work.label')] as Object[],
                    'Another user has updated this Work while you were editing'
                )
                render view: 'edit', model: [workInstance: workInstance]
                return
            }
        }

        if (!salesItemService.saveSalesItemPricing(workInstance, params)) {
            render view: 'edit', model: [workInstance: workInstance]
            return
        }

        request.workInstance = workInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'work.label'), workInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: workInstance.id
        }
    }

    def delete(Long id) {
        def workInstance = Work.get(id)
        if (!workInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'work.label'), id]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        try {
            if (workInstance.pricing) {
                workInstance.pricing.delete flush: true
            }
            workInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'work.label')]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'work.label')]
            )
            redirect action: 'show', id: id
        }
    }

    def get(Long id) {
        def workInstance = Work.read(id)
        if (!workInstance) {
            render status: SC_NOT_FOUND
            return
        }

        JSON.use('deep') {
            render(contentType: 'text/json') {
                fullNumber = workInstance.fullNumber
                inventoryItem = workInstance
            }
        }
    }

    def find(String name) {
        Integer number = null
        try {
            number = name as Integer
        } catch (NumberFormatException ignored) { /* ignored */ }

        def c = Work.createCriteria()
        List<Work> list = c.list {
            or {
                eq 'number', number
                ilike 'name', "%${name}%"
            }
            order 'number', 'asc'
        }

        render(contentType: 'text/json') {
            array {
                for (Work w in list) {
                    work id: w.id, name: w.name
                }
            }
        }
    }
}
