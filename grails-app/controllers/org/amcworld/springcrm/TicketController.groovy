/*
 * TicketController.groovy
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

import javax.servlet.http.HttpServletResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code TicketController} handles actions concerning tickets in a
 * helpdesk.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.4
 */
class TicketController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    MailSystemService mailSystemService
    TicketService ticketService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<Ticket> ticketInstanceList
        int ticketInstanceTotal
        User user = session.credential.loadUser()

        if (params.helpdesk) {
            Helpdesk helpdesk = Helpdesk.get(params.helpdesk as Long)
            ticketInstanceList = Ticket.findAllByHelpdesk(helpdesk)
            ticketInstanceTotal = Ticket.countByHelpdesk(helpdesk)
        } else if (user.admin) {
            ticketInstanceList = Ticket.list(params)
            ticketInstanceTotal = Ticket.count()
        } else {
            List<HelpdeskUser> helpdeskUsers = HelpdeskUser.findAllByUser(user)
            if (!helpdeskUsers) {
                render view: 'noHelpdesks'
                return
            }

            List<Helpdesk> helpdesks = helpdeskUsers*.helpdesk
            ticketInstanceList =
                Ticket.findAllByHelpdeskInList(helpdesks, params)
            ticketInstanceTotal = Ticket.countByHelpdeskInList(helpdesks)
        }

        [
            ticketInstanceList: ticketInstanceList,
            ticketInstanceTotal: ticketInstanceTotal,
            mailSystemConfigured: mailSystemService.configured
        ]
    }

    def listEmbedded(Long organization) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        Organization organizationInstance = Organization.get(organization)
        User user = session.credential.loadUser()

        List<Ticket> ticketList = Ticket.executeQuery(
            'select t from Ticket as t inner join t.helpdesk as h, ' +
                'HelpdeskUser as hu where hu.helpdesk = h and hu.user = :u ' +
                'and h.organization = :o',
            [u: user, o: organizationInstance],
            params
        )
        int count = Ticket.executeQuery(
            'select count(*) from Ticket as t inner join t.helpdesk as h, ' +
                'HelpdeskUser as hu where hu.helpdesk = h and hu.user = :u ' +
                'and h.organization = :o',
            [u: user, o: organizationInstance],
        )[0] as int

        [
            ticketInstanceList: ticketList,
            ticketInstanceTotal: count,
            linkParams: [organization: organizationInstance.id]
        ]
    }

    def create() {
        [ticketInstance: new Ticket(params), helpdeskInstanceList: helpdesks]
    }

    def copy(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        ticketInstance = new Ticket(ticketInstance)
        render view: 'create', model: [
            ticketInstance: ticketInstance, helpdeskInstanceList: helpdesks
        ]
    }

    def save() {
        Ticket ticketInstance = new Ticket(params)
        String messageText = ticketInstance.messageText = params.messageText

        if (!ticketInstance.validate() || !messageText) {
            if (!messageText) {
                ticketInstance.errors.rejectValue(
                    'messageText', 'default.blank.message'
                )
            }
            render view: 'create', model: [
                ticketInstance: ticketInstance,
                helpdeskInstanceList: helpdesks
            ]
            return
        }

        ticketInstance = ticketService.createTicket(
            ticketInstance, messageText, (MultipartFile) params.attachment
        )
        if (!ticketInstance) {
            render view: 'create', model: [
                ticketInstance: ticketInstance,
                helpdeskInstanceList: helpdesks
            ]
            return
        }

        request.ticketInstance = ticketInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'ticket.label'), ticketInstance.toString()]
        ) as Object

        redirect action: 'show', id: ticketInstance.id
    }

    def show(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [ticketInstance: ticketInstance]
    }

    def edit(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        [ticketInstance: ticketInstance, helpdeskInstanceList: helpdesks]
    }

    def update(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        if (params.version) {
            long version = params.version.toLong()
            if (ticketInstance.version > version) {
                ticketInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'ticket.label')] as Object[],
                    'Another user has updated this Ticket while you were editing'
                )
                render view: 'edit', model: [ticketInstance: ticketInstance]
                return
            }
        }

        ticketInstance.properties = params
        if (!ticketInstance.save(flush: true)) {
            render view: 'edit', model: [
                ticketInstance: ticketInstance, helpdeskInstanceList: helpdesks
            ]
            return
        }

        request.ticketInstance = ticketInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'ticket.label'), ticketInstance.toString()]
        ) as Object

        redirect action: 'show', id: ticketInstance.id
    }

    def delete(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object

            redirect action: 'index'
            return
        }

        request.ticketInstance = ticketInstance
        try {
            ticketInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'ticket.label')]
            ) as Object

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'ticket.label')]
            ) as Object

            redirect action: 'show', id: id
        }
    }

    def takeOn(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        User user = session.credential.loadUser()
        if ((user.admin || user in ticketInstance.helpdesk.users) &&
            ticketInstance.stage in [TicketStage.created, TicketStage.resubmitted])
        {
            ticketInstance.stage = TicketStage.assigned
            ticketService.assignUser ticketInstance, user, user
        }

        redirect action: 'show', id: id
    }

    def sendMessage(Long id) {
        String msg = params.message
        if (msg) {
            Ticket ticketInstance = Ticket.get(id)
            if (!ticketInstance) {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'ticket.label'), id]
                ) as Object
                redirect action: 'index'
                return
            }

            User creator = ((Credential) session.credential).loadUser()
            User recipient =
                params.recipient ? User.get(params.long('recipient')) : null
            if (recipient || creator.admin ||
                (creator == ticketInstance.assignedUser &&
                ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess]))
            {
                ticketService.sendMessage(
                    ticketInstance, msg, (MultipartFile) params.attachment,
                    creator, recipient
                )
            }
        }

        redirect action: 'show', id: id
    }

    def createNote(Long id) {
        String message = params.messageText
        if (message) {
            Ticket ticketInstance = Ticket.get(id)
            if (!ticketInstance) {
                flash.message = g.message(
                    code: 'default.not.found.message',
                    args: [g.message(code: 'ticket.label'), id]
                ) as Object
                redirect action: 'index'
                return
            }

            ticketService.createNote(
                ticketInstance, message, (MultipartFile) params.attachment,
                ((Credential) session.credential).loadUser()
            )
        }

        redirect action: 'show', id: id
    }

    def changeStage(Long id, String stage) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        User user = session.credential.loadUser()
        if (user.admin || user == ticketInstance.assignedUser) {
            EnumSet<TicketStage> allowedStages
            switch (ticketInstance.stage) {
            case TicketStage.assigned:
                allowedStages = EnumSet.of(
                    TicketStage.inProcess, TicketStage.closed
                )
                break
            case TicketStage.inProcess:
                allowedStages = EnumSet.of(TicketStage.closed)
                break
            case TicketStage.closed:
                allowedStages = EnumSet.of(TicketStage.resubmitted)
                break
            default:
                allowedStages = EnumSet.noneOf(TicketStage)
                break
            }

            TicketStage requestedStage = TicketStage.valueOf(stage)
            if (requestedStage in allowedStages) {
                ticketService.changeStage ticketInstance, requestedStage, user
            }
        }

        redirect action: 'show', id: id
    }

    def assignToUser(Long id, Long user) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            ) as Object
            redirect action: 'index'
            return
        }

        User creator = session.credential.loadUser()
        User recipient = User.get(user)
        if (recipient &&
            (recipient in ticketInstance.helpdesk.users) &&
            creator != recipient &&
            (creator.admin || creator == ticketInstance.assignedUser) &&
            ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess])
        {
            ticketService.assignUser ticketInstance, creator, recipient
        }

        redirect action: 'show', id: id
    }

    def frontendCreate() {
        Helpdesk helpdeskInstance = Helpdesk.get(params.long('helpdesk'))
        if (!helpdeskInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        Organization organization = helpdeskInstance.organization
        Ticket ticketInstance = new Ticket(
            address: organization?.shippingAddr, phone: organization?.phone,
            fax: organization?.fax, email1: organization?.email1,
            email2: organization?.email2
        )

        [helpdeskInstance: helpdeskInstance, ticketInstance: ticketInstance]
    }

    def frontendSave() {
        Helpdesk helpdeskInstance = Helpdesk.get(params.long('helpdesk'))
        if (!helpdeskInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }
        params.helpdesk = helpdeskInstance

        Ticket ticketInstance = new Ticket(params)
        ticketInstance.stage = TicketStage.created
        ticketInstance.address = new Address()
        String messageText = ticketInstance.messageText = params.messageText

        if (!ticketInstance.validate() || !messageText) {
            if (log.debugEnabled) {
                log.debug "Ticket validation failed: ${ticketInstance.errors}"
            }
            if (!messageText) {
                ticketInstance.errors.rejectValue(
                    'messageText', 'default.blank.message'
                )
            }

            render view: '/ticket/frontendCreate', model: [
                ticketInstance: ticketInstance,
                helpdeskInstance: helpdeskInstance
            ]
            return
        }

        ticketInstance = ticketService.createTicket(
            ticketInstance, messageText, (MultipartFile) params.attachment
        )
        if (!ticketInstance) {
            log.debug 'No ticket created.'
            render view: '/helpdesk/frontendIndex', model: [
                ticketInstance: ticketInstance,
                helpdeskInstance: helpdeskInstance
            ]
            return
        }

        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'ticket.label'), ticketInstance.toString()]
        ) as Object
        if (helpdeskInstance.forEndUsers) {
            def params = [
                accessCode: helpdeskInstance.accessCode,
                helpdesk: helpdeskInstance.id,
                id: ticketInstance.id
            ]
            redirect mapping: 'ticketFrontendShow', params: params
        } else {
            redirectToHelpdeskFrontend helpdeskInstance
        }
    }

    def frontendShow(Long id) {
        Ticket ticketInstance = Ticket.read(id)

        [
            ticketInstance: ticketInstance,
            helpdeskInstance: ticketInstance.helpdesk
        ]
    }

    def frontendSendMessage(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            redirectToHelpdeskFrontend Helpdesk.read(params.long('helpdesk'))
            return
        }

        String msg = params.message
        if (!msg) {
            redirectToFrontendPage ticketInstance.helpdesk
            return
        }

        ticketService.sendMessage(
            ticketInstance, msg, (MultipartFile) params.attachment
        )

        flash.message = message(code: 'ticket.sendMessage.flash') as Object
        redirectToFrontendPage ticketInstance.helpdesk
    }

    def frontendCloseTicket(Long id) {
        frontendChangeStage id, TicketStage.closed
    }

    def frontendResubmitTicket(Long id) {
        frontendChangeStage id, TicketStage.resubmitted
    }


    //-- Non-public methods ---------------------

    /**
     * Changes the stage of the ticket with the given ID as requested from
     * frontend.
     *
     * @param id    the ID of the ticket
     * @param stage the stage that should be changed to
     */
    private frontendChangeStage(Long id, TicketStage stage) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            redirectToHelpdeskFrontend Helpdesk.read(params.long('helpdesk'))
            return
        }

        ticketService.changeStage ticketInstance, stage
        flash.message = message(code: "ticket.${stage}.flash") as Object
        redirectToFrontendPage ticketInstance.helpdesk
    }

    /**
     * Gets the helpdesks the current user may access.
     *
     * @return  the list of helpdesks
     */
    private List<Helpdesk> getHelpdesks() {
        User user = ((Credential) session.credential).loadUser()

        user.admin ? Helpdesk.list() : user.helpdesks as List
    }

    /**
     * Redirects the to origin helpdesk frontend page, either to the overview
     * page or the show view of a ticket.
     *
     * @param helpdesk  the given helpdesk
     */
    private void redirectToFrontendPage(Helpdesk helpdesk) {
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirectToHelpdeskFrontend helpdesk
        }
    }

    /**
     * Redirects to the frontend index page of the given helpdesk.
     *
     * @param helpdesk  the given helpdesk
     */
    private void redirectToHelpdeskFrontend(Helpdesk helpdesk) {
        def params = [
            accessCode: helpdesk.accessCode,
            urlName: helpdesk.urlName
        ]
        redirect mapping: 'helpdeskFrontend', params: params
    }
}
