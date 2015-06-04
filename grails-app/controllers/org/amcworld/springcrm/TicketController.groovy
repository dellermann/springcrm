/*
 * TicketController.groovy
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

import javax.servlet.http.HttpServletResponse
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code TicketController} handles actions concerning tickets in a
 * helpdesk.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.4
 */
class TicketController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    DataFileService dataFileService
    MailSystemService mailSystemService
    TicketService ticketService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<Ticket> ticketInstanceList = null
        int ticketInstanceTotal = 0
        User user = session.user

        if (params.helpdesk) {
            Helpdesk helpdesk = Helpdesk.get(params.helpdesk as Long)
            ticketInstanceList = Ticket.findAllByHelpdesk(helpdesk)
            ticketInstanceTotal = Ticket.countByHelpdesk(helpdesk)
        } else if (user.admin) {
            ticketInstanceList = Ticket.list(params)
            ticketInstanceTotal = Ticket.count()
        } else {
            List<Helpdesk> helpdesks = user.helpdesks
            if (!helpdesks) {
                render view: 'noHelpdesks'
                return
            }

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

    def create() {
        [ticketInstance: new Ticket(params), helpdeskInstanceList: helpdesks]
    }

    def copy(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label', id)]
            )
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
                ticketInstance.errors.rejectValue 'messageText', 'default.blank.message'
            }
            render view: 'create', model: [
                ticketInstance: ticketInstance,
                helpdeskInstanceList: helpdesks
            ]
            return
        }

        ticketInstance = ticketService.createTicket(
            ticketInstance, messageText, params.attachment
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
            args: [message(code: 'ticket.label', ticketInstance.toString())]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: ticketInstance.id
        }
    }

    def show(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            )
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
            )
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
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
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
            args: [message(code: 'ticket.label', ticketInstance.toString())]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: ticketInstance.id
        }
    }

    def delete(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        try {
            ticketInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'ticket.label')]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (DataIntegrityViolationException e) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'ticket.label')]
            )
            redirect action: 'show', id: id
        }
    }

    def takeOn(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'ticket.label'), id]
            )
            redirect action: 'index'
            return
        }

        User user = session.user
        if ((user.admin || user in ticketInstance.helpdesk.users) &&
            ticketInstance.stage in [TicketStage.created, TicketStage.resubmitted])
        {
            ticketInstance.stage = TicketStage.assigned
            ticketService.assignUser ticketInstance, user, user
        }

        redirect action: 'show', id: id
    }

    def sendMessage(Long id) {
        String message = params.messageText
        if (message) {
            Ticket ticketInstance = Ticket.get(id)
            if (!ticketInstance) {
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'ticket.label'), id]
                )
                redirect action: 'index'
                return
            }

            User creator = session.user
            User recipient =
                params.recipient ? User.get(params.recipient) : null
            if (recipient || creator.admin ||
                (creator == ticketInstance.assignedUser &&
                ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess]))
            {
                ticketService.sendMessage(
                    ticketInstance, message, params.attachment, creator,
                    recipient
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
                flash.message = message(
                    code: 'default.not.found.message',
                    args: [message(code: 'ticket.label'), id]
                )
                redirect action: 'index'
                return
            }

            ticketService.createNote(
                ticketInstance, message, params.attachment, session.user
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
            )
            redirect action: 'index'
            return
        }

        User user = session.user
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
            )
            redirect action: 'index'
            return
        }

        User creator = session.user
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

    def frontendSave() {
        Helpdesk helpdeskInstance = Helpdesk.get(params.helpdesk)
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

            render view: '/helpdesk/frontendIndex', model: [
                ticketInstance: ticketInstance,
                helpdeskInstance: helpdeskInstance
            ]
            return
        }

        ticketInstance = ticketService.createTicket(
            ticketInstance, messageText, params.attachment
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
            args: [message(code: 'ticket.label', ticketInstance.toString())]
        )
        redirectToHelpdeskFrontend helpdeskInstance
    }

    def frontendShow(Long id) {
        [ticketInstance: Ticket.read(id)]
    }

    def frontendSendMessage(Long id) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            redirectToHelpdeskFrontend Helpdesk.read(params.helpdesk)
            return
        }

        String message = params.messageText
        if (!message) {
            redirectToFrontendPage ticketInstance.helpdesk
            return
        }

        ticketService.sendMessage ticketInstance, message, params.attachment

        flash.message = g.message(code: 'ticket.sendMessage.flash')
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
    protected def frontendChangeStage(Long id, TicketStage stage) {
        Ticket ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            redirectToHelpdeskFrontend Helpdesk.read(params.helpdesk)
            return
        }

        ticketService.changeStage ticketInstance, stage
        flash.message = message(code: "ticket.${stage}.flash")
        redirectToFrontendPage ticketInstance.helpdesk
    }

    /**
     * Gets the helpdesks the current user may access.
     *
     * @return  the list of helpdesks
     */
    protected List<Helpdesk> getHelpdesks() {
        User user = session.user
        user.admin ? Helpdesk.list() : user.helpdesks as List
    }

    /**
     * Redirects the to origin helpdesk frontend page, either to the overview
     * page or the show view of a ticket.
     *
     * @param helpdesk  the given helpdesk
     */
    protected void redirectToFrontendPage(Helpdesk helpdesk) {
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
    protected void redirectToHelpdeskFrontend(Helpdesk helpdesk) {
        def params = [
            accessCode: helpdesk.accessCode,
            urlName: helpdesk.urlName
        ]
        redirect mapping: 'helpdeskFrontend', params: params
    }
}
