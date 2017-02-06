/*
 * DepartmentController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.validation.BindingResult


/**
 * The class {@code DepartmentController} contains actions which manage
 * departments of a company.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class DepartmentController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        int max = params.max =
            Math.min(params.max ? params.int('max') : 10, 100)

        String letter = params.letter?.toString()
        if (letter) {
            int num = Department.countByNameLessThan(letter)
            params.sort = 'name'
            params.offset = (Math.floor(num / max) * max) as int
            params.search = null
        }

        List<Department> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Department.findAllByNameLike(searchFilter, params)
            count = Department.countByNameLike(searchFilter)
        } else {
            list = Department.list(params)
            count = Department.count()
        }

        [departmentInstanceList: list, departmentInstanceTotal: count]
    }

    def create() {
        [departmentInstance: new Department(params)]
    }

    def copy(Long id) {
        Department departmentInstance = Department.get(id)
        if (!departmentInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'department.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        departmentInstance = new Department(departmentInstance)
        render view: 'create', model: [departmentInstance: departmentInstance]
    }

    def save() {
        Department departmentInstance = new Department(params)
        if (!departmentInstance.save(flush: true)) {
            render(
                view: 'create', model: [departmentInstance: departmentInstance]
            )
            return
        }

        request.departmentInstance = departmentInstance
        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'department.label'),
                departmentInstance.toString()
            ]
        ) as Object

        redirect action: 'show', id: departmentInstance.id
    }

    def show(Long id) {
        Department departmentInstance = Department.get(id)
        if (!departmentInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'department.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [departmentInstance: departmentInstance]
    }

    def edit(Long id) {
        Department departmentInstance = Department.get(id)
        if (!departmentInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'department.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [departmentInstance: departmentInstance]
    }

    def update(Long id) {
        Department departmentInstance = Department.get(id)
        if (!departmentInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'department.label'), id]
            ) as Object
            redirect action: 'index', id: id
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (departmentInstance.version > version) {
                departmentInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [
                        message(code: 'department.label', default: 'Call')
                    ] as Object[],
                    'Another user has updated this department while you were ' +
                        'editing'
                )
                render(
                    view: 'edit',
                    model: [departmentInstance: departmentInstance]
                )
                return
            }
        }
        departmentInstance.properties = params as BindingResult
        if (!departmentInstance.save(flush: true)) {
            render view: 'edit', model: [departmentInstance: departmentInstance]
            return
        }

        request.departmentInstance = departmentInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'department.label'),
                departmentInstance.toString()
            ]
        ) as Object

        redirect action: 'show', id: departmentInstance.id
    }

    def delete(Long id) {
        def departmentInstance = Department.get(id)
        if (!departmentInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'department.label'), id]
            ) as Object

            redirect action: 'index'
            return
        }

        request.departmentInstance = departmentInstance
        try {
            departmentInstance.delete(flush: true)
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'department.label')]
            ) as Object

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'department.label')]
            ) as Object
            redirect action: 'show', id: id
        }
    }
}
