/*
 * TicketController.groovy
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

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.MultipartFile


class TicketController {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.ticketMessage


    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    DataFileService dataFileService


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<Ticket> ticketInstanceList = null
        int ticketInstanceTotal = 0
        User user = session.user
        if (user.admin) {
            ticketInstanceList = Ticket.list(params)
            ticketInstanceTotal = Ticket.count()
        } else {
            def helpdesks = user.helpdesks
            if (!helpdesks) {
                render view: 'noHelpdesks'
                return
            }

            ticketInstanceList = Ticket.findAllByHelpdeskInList(helpdesks, params)
            ticketInstanceTotal = Ticket.countByHelpdeskInList(helpdesks)
        }

        [ticketInstanceList: ticketInstanceList, ticketInstanceTotal: ticketInstanceTotal]
    }

    def create() {
        def ticketInstance = new Ticket(params)
        [ticketInstance: ticketInstance, helpdeskInstanceList: helpdesks]
    }

    def copy(Long id) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'show', id: id
            return
        }

        ticketInstance = new Ticket(ticketInstance)
        render view: 'create', model: [ticketInstance: ticketInstance, helpdeskInstanceList: helpdesks]
    }

    def save() {
        def ticketInstance = new Ticket(params)
        ticketInstance.stage = TicketStage.created
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

        DataFile dataFile =
            dataFileService.storeFile(FILE_TYPE, params.attachment)
        ticketInstance.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.create
            ))
            .addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.sendMessage,
                message: messageText,
                attachment: dataFile
            ))
        if (!ticketInstance.save()) {
            render view: 'create', model: [
                ticketInstance: ticketInstance,
                helpdeskInstanceList: helpdesks
            ]
            return
        }

        request.ticketInstance = ticketInstance
        flash.message = message(code: 'default.created.message', args: [message(code: 'ticket.label', default: 'Ticket'), ticketInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: ticketInstance.id
        }
    }

    def show(Long id) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'list'
            return
        }

        [ticketInstance: ticketInstance]
    }

    def edit(Long id) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'list'
            return
        }

        [ticketInstance: ticketInstance, helpdeskInstanceList: helpdesks]
    }

    def update(Long id) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (ticketInstance.version > version) {
                ticketInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'ticket.label', default: 'Ticket')] as Object[], 'Another user has updated this Ticket while you were editing')
                render view: 'edit', model: [ticketInstance: ticketInstance]
                return
            }
        }

        ticketInstance.properties = params

        if (!ticketInstance.save(flush: true)) {
            render view: 'edit', model: [ticketInstance: ticketInstance, helpdeskInstanceList: helpdesks]
            return
        }

        request.ticketInstance = ticketInstance
        flash.message = message(code: 'default.updated.message', args: [message(code: 'ticket.label', default: 'Ticket'), ticketInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: ticketInstance.id
        }
    }

    def delete(Long id) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
            return
        }

        try {
            ticketInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'ticket.label', default: 'Ticket')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'ticket.label', default: 'Ticket')])
            redirect action: 'show', id: id
        }
    }

    def takeOn(Long id) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'list'
            return
        }
        User user = session.user
        if (!((user.admin || user in ticketInstance.helpdesk.users) &&
            ticketInstance.stage in [TicketStage.created, TicketStage.resubmitted]))
        {
            redirect action: 'show', id: id
            return
        }

        ticketInstance.stage = TicketStage.assigned
        ticketInstance.assignedUser = user
        ticketInstance.addToLogEntries(new TicketLogEntry(
            action: TicketLogAction.assign,
            creator: user,
            recipient: user
        ))
        ticketInstance.save()

        redirect action: 'show', id: id
    }

    def sendMessage(Long id) {
        String message = params.messageText
        if (!message) {
            redirect action: 'show', id: id
            return
        }

        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'list'
            return
        }
        User creator = session.user
        if (!(creator.admin || (creator == ticketInstance.assignedUser &&
            ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess])))
        {
            redirect action: 'show', id: id
            return
        }

        DataFile dataFile =
            dataFileService.storeFile(FILE_TYPE, params.attachment)
        User recipient = params.recipient ? User.get(params.recipient) : null
        ticketInstance.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.sendMessage,
                creator: creator,
                recipient: recipient,
                message: message,
                attachment: dataFile
            ))
            .save()

        redirect action: 'show', id: id
    }

    def changeStage(Long id, String stage) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'list'
            return
        }

        User user = session.user
        if (!user.admin && user != ticketInstance.assignedUser) {
            redirect action: 'show', id: id
            return
        }

        EnumSet<TicketStage> allowedStages
        switch (ticketInstance.stage) {
        case TicketStage.assigned:
            allowedStages = EnumSet.of(TicketStage.inProcess, TicketStage.closed)
            break
        case TicketStage.inProcess:
            allowedStages = EnumSet.of(TicketStage.closed)
            break
        }
        TicketStage requiredStage = TicketStage.valueOf(stage)
        if (!(requiredStage in allowedStages)) {
            redirect action: 'show', id: id
            return
        }

        ticketInstance.stage = requiredStage
        ticketInstance.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.changeStage,
                creator: user,
                stage: requiredStage
            ))
            .save()

        redirect action: 'show', id: id
    }

    def assignToUser(Long id, Long user) {
        def ticketInstance = Ticket.get(id)
        if (!ticketInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'ticket.label', default: 'Ticket'), id])
            redirect action: 'list'
            return
        }

        User creator = session.user
        User recipient = User.get(user)
        if (!recipient || !(recipient in ticketInstance.helpdesk.users)) {
            redirect action: 'show', id: id
            return
        }
        if (!((creator.admin || creator == ticketInstance.assignedUser) &&
            ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess]))
        {
            redirect action: 'show', id: id
            return
        }

        ticketInstance.assignedUser = recipient
        ticketInstance.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.assign,
                creator: creator,
                recipient: recipient
            ))
            .save()

        redirect action: 'show', id: id
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the helpdesks the current user may access.
     *
     * @return  the list of helpdesks
     */
    List<Helpdesk> getHelpdesks() {
        User user = session.user
        user.admin ? Helpdesk.list() : user.helpdesks as List
    }
}
