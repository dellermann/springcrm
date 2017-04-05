/*
 * StaffController.groovy
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


/**
 * The class {@code StaffController} contains actions which manage personnel
 * of a company.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class StaffController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        int max = params.max =
            Math.min(params.max ? params.int('max') : 10, 100)

        String letter = params.letter?.toString()
        if (letter) {
            int num = Staff.countByLastNameLessThan(letter)
            params.sort = 'name'
            params.offset = (Math.floor(num / max) * max) as int
            params.search = null
        }

        List<Staff> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Staff.findAllByLastNameLike(searchFilter, params)
            count = Staff.countByLastNameLike(searchFilter)
        } else {
            list = Staff.list(params)
            count = Staff.count()
        }

        [staffInstanceList: list, staffInstanceTotal: count]
    }

    def create() {
        [staffInstance: new Staff(params)]
    }

    def copy(Long id) {
        Staff staffInstance = Staff.get(id)
        if (!staffInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'staff.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        staffInstance = new Staff(staffInstance)
        render view: 'create', model: [staffInstance: staffInstance]
    }

    def save() {
        Staff staffInstance = new Staff(params)
        if (!staffInstance.save(flush: true)) {
            render view: 'create', model: [staffInstance: staffInstance]
            return
        }

        request.staffInstance = staffInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'staff.label'), staffInstance.toString()]
        ) as Object

        redirect action: 'show', id: staffInstance.id
    }

    def show(Long id) {
        Staff staffInstance = Staff.get(id)
        if (!staffInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'staff.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [staffInstance: staffInstance]
    }

    def edit(Long id) {
        Staff staffInstance = Staff.get(id)
        if (!staffInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'staff.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [staffInstance: staffInstance]
    }

    def update(Long id) {
        Staff staffInstance = Staff.get(id)
        if (!staffInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'staff.label'), id]
            ) as Object
            redirect action: 'index', id: id
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (staffInstance.version > version) {
                staffInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'staff.label', default: 'Staff')]
                        as Object[],
                    'Another user has updated this staff while you were editing'
                )
                render view: 'edit', model: [staffInstance: staffInstance]
                return
            }
        }
        staffInstance.properties = params
        println "time: ${params.weeklyWorkingTime} // ${staffInstance.weeklyWorkingTime}"
        if (!staffInstance.save(flush: true)) {
            render view: 'edit', model: [staffInstance: staffInstance]
            return
        }

        request.staffInstance = staffInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'staff.label'), staffInstance.toString()]
        ) as Object

        redirect action: 'show', id: staffInstance.id
    }

    def delete(Long id) {
        Staff staffInstance = Staff.get(id)
        if (!staffInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'staff.label'), id]
            ) as Object

            redirect action: 'index'
            return
        }

        request.staffInstance = staffInstance
        try {
            staffInstance.delete(flush: true)
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'staff.label')]
            ) as Object

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'staff.label')]
            ) as Object
            redirect action: 'show', id: id
        }
    }
}
