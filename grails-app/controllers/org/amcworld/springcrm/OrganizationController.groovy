/*
 * OrganizationController.groovy
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

import javax.servlet.http.HttpServletResponse
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code OrganizationController} contains actions which manage
 * organizations.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class OrganizationController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index(Byte listType) {
        int max = params.max =
            Math.min(params.max ? params.int('max') : 10, 100)

        List<Byte> types = [listType, 3 as byte]
        String letter = params.letter?.toString()
        if (letter) {
            int num
            if (listType) {
                num = Organization.countByNameLessThanAndRecTypeInList(
                    letter, types
                )
            } else {
                num = Organization.countByNameLessThan(letter)
            }
            params.sort = 'name'
            params.offset = Math.floor(num / max) * max
        }

        List<Organization> list
        int count
        if (listType) {
            list = Organization.findAllByRecTypeInList(types, params)
            count = Organization.countByRecTypeInList(types)
        } else {
            list = Organization.list(params)
            count = Organization.count()
        }

        [organizationInstanceList: list, organizationInstanceTotal: count]
    }

    def create() {
        [organizationInstance: new Organization(params)]
    }

    def copy(Long id) {
        Organization organizationInstance = Organization.get(id)
        if (!organizationInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'organization.label'), id]
            )
            redirect action: 'index'
            return
        }

        organizationInstance = new Organization(organizationInstance)
        render(
            view: 'create',
            model: [organizationInstance: organizationInstance]
        )
    }

    def save() {
        Organization organizationInstance = new Organization(params)
        if (!organizationInstance.save(flush: true)) {
            render(
                view: 'create',
                model: [organizationInstance: organizationInstance]
            )
            return
        }

        request.organizationInstance = organizationInstance
        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'organization.label'),
                organizationInstance.toString()
            ]
        )

        redirect(
            action: 'show', id: organizationInstance.id,
            params: [listType: params.listType]
        )
    }

    def show(Long id) {
        Organization organizationInstance = Organization.get(id)
        if (!organizationInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'organization.label'), id]
            )
            redirect action: 'index', params: [listType: params.listType]
            return
        }

        [organizationInstance: organizationInstance]
    }

    def edit(Long id) {
        Organization organizationInstance = Organization.get(id)
        if (!organizationInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'organization.label'), id]
            )
            redirect action: 'index', params: [listType: params.listType]
            return
        }

        [organizationInstance: organizationInstance]
    }

    def update(Long id) {
        Organization organizationInstance = Organization.get(id)
        if (!organizationInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'organization.label'), id]
            )
            redirect action: 'index', params: [listType: params.listType]
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (organizationInstance.version > version) {
                organizationInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'organization.label')] as Object[],
                    'Another user has updated this Organization while you were editing'
                )
                render(
                    view: 'edit',
                    model: [organizationInstance: organizationInstance]
                )
                return
            }
        }
        if (params.autoNumber) {
            params.number = organizationInstance.number
        }
        organizationInstance.properties = params
        if (!organizationInstance.save(flush: true)) {
            render(
                view: 'edit',
                model: [organizationInstance: organizationInstance]
            )
            return
        }

        request.organizationInstance = organizationInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'organization.label'),
                organizationInstance.toString()
            ]
        )

        redirect(
            action: 'show', id: organizationInstance.id,
            params: [listType: params.listType]
        )
    }

    def delete(Long id, Byte listType) {
        Organization organizationInstance = Organization.get(id)
        if (!organizationInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'organization.label'), id]
            )

            redirect action: 'index', params: [listType: listType]
            return
        }

        request.organizationInstance = organizationInstance
        try {
            organizationInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'organization.label')]
            )

            redirect action: 'index', params: [listType: listType]
        } catch (DataIntegrityViolationException ignored) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'organization.label')]
            )

            redirect action: 'show', id: id, params: [listType: listType]
        }
    }

    def find(Byte type) {
        List<Organization> list
        if (type) {
            List<Byte> types = [type, 3 as byte]
            list = Organization.findAllByRecTypeInListAndNameLike(
                types, "%${params.name}%", [sort: 'name']
            )
        } else {
            list = Organization.findAllByNameLike(
                "%${params.name}%", [sort: 'name']
            )
        }

        [organizationInstanceList: list]
    }

    def get(Long id) {
        Organization organizationInstance = Organization.get(id)
        if (!organizationInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        [organizationInstance: organizationInstance]
    }

    def getPhoneNumbers(Long id) {
        Organization organizationInstance = Organization.get(id)
        if (!organizationInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        List<String> phoneNumbers = [
                organizationInstance.phone,
                organizationInstance.phoneOther,
                organizationInstance.fax
            ]
            .findAll({ it != '' })
            .unique()

        [phoneNumbers: phoneNumbers]
    }

    def getTermOfPayment(Long id) {
        int termOfPayment =
            ConfigHolder.instance['termOfPayment'].toType(Integer) ?: 14

        Organization organizationInstance = Organization.get(id)
        if (organizationInstance?.termOfPayment != null) {
            termOfPayment = organizationInstance.termOfPayment
        }

        [termOfPayment: termOfPayment]
    }
}
