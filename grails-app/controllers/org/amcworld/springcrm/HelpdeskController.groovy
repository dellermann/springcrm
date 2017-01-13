/*
 * HelpdeskController.groovy
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

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code HelpdeskController} handles helpdesks.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
class HelpdeskController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    HelpdeskService helpdeskService
    MailSystemService mailSystemService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        [
            helpdeskInstanceList: Helpdesk.list(params),
            helpdeskInstanceTotal: Helpdesk.count(),
            mailSystemConfigured: mailSystemService.configured
        ]
    }

    def listEmbedded(Long organization) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        Organization organizationInstance = Organization.get(organization)
        List<Helpdesk> l =
            Helpdesk.findAllByOrganization(organizationInstance, params)
        int count = Helpdesk.countByOrganization(organizationInstance)

        [
            helpdeskInstanceList: l, helpdeskInstanceTotal: count,
            linkParams: [organization: organizationInstance.id]
        ]
    }

    def create() {
        [helpdeskInstance: new Helpdesk(params)]
    }

    def copy(Long id) {
        Helpdesk helpdeskInstance = Helpdesk.get(id)
        if (!helpdeskInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'helpdesk.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        helpdeskInstance = new Helpdesk(helpdeskInstance)
        render view: 'create', model: [helpdeskInstance: helpdeskInstance]
    }

    def save() {
        Helpdesk helpdeskInstance = new Helpdesk()
        if (!helpdeskService.saveHelpdesk(helpdeskInstance, params)) {
            render view: 'create', model: [helpdeskInstance: helpdeskInstance]
            return
        }

        request.helpdeskInstance = helpdeskInstance
        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'helpdesk.label'), helpdeskInstance.toString()
            ]
        ) as Object

        redirect action: 'show', id: helpdeskInstance.id
    }

    def show(Long id) {
        Helpdesk helpdeskInstance = Helpdesk.get(id)
        if (!helpdeskInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'helpdesk.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [helpdeskInstance: helpdeskInstance]
    }

    def edit(Long id) {
        Helpdesk helpdeskInstance = Helpdesk.get(id)
        if (!helpdeskInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'helpdesk.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [helpdeskInstance: helpdeskInstance]
    }

    def update(Long id) {
        Helpdesk helpdeskInstance = Helpdesk.get(id)
        if (!helpdeskInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'helpdesk.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        if (params.version) {
            long version = params.version.toLong()
            if (helpdeskInstance.version > version) {
                helpdeskInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'helpdesk.label')] as Object[],
                    'Another user has updated this Helpdesk while you were editing'
                )
                render view: 'edit', model: [helpdeskInstance: helpdeskInstance]
                return
            }
        }

        if (!helpdeskService.saveHelpdesk(helpdeskInstance, params)) {
            render view: 'edit', model: [helpdeskInstance: helpdeskInstance]
            return
        }

        request.helpdeskInstance = helpdeskInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'helpdesk.label'), helpdeskInstance.toString()
            ]
        ) as Object

        redirect action: 'show', id: helpdeskInstance.id
    }

    def delete(Long id) {
        Helpdesk helpdeskInstance = Helpdesk.get(id)
        if (!helpdeskInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'helpdesk.label'), id]
            ) as Object

            redirect action: 'index'
            return
        }

        request.helpdeskInstance = helpdeskInstance
        try {
            helpdeskInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'helpdesk.label')]
            ) as Object

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'helpdesk.label')]
            ) as Object
            redirect action: 'show', id: id
        }
    }

    def frontendIndex(String urlName) {
        Helpdesk helpdeskInstance = Helpdesk.findByUrlName(urlName)
        if (!helpdeskInstance) {
            render status: SC_NOT_FOUND
            return
        }

        if (helpdeskInstance.forEndUsers) {
            redirect(
                controller: 'ticket', action: 'frontendCreate',
                params: [
                    helpdesk: helpdeskInstance.id,
                    accessCode: helpdeskInstance.accessCode
                ]
            )
            return
        }

        List<Ticket> ticketInstanceList =
            Ticket.findAllByHelpdesk(helpdeskInstance, params)

        [
            helpdeskInstance: helpdeskInstance,
            ticketInstanceList: ticketInstanceList
        ]
    }
}
