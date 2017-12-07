/*
 * TicketService.groovy
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

import grails.gorm.services.Query
import grails.gorm.services.Service
import grails.web.mapping.LinkGenerator
import org.springframework.web.multipart.MultipartFile


interface TicketService {

    //-- Public methods -------------------------

    /**
     * Assigns the given ticket to the specified user.
     *
     * @param ticket    the given ticket
     * @param creator   the user who assigns a user
     * @param assignTo  the user the ticket is assigned to
     * @return          the modified ticket
     */
    Ticket assignUser(Ticket ticket, User creator, User assignTo)

    /**
     * Changes the stage of the given ticket and uses the customer as trigger.
     *
     * @param ticket    the given ticket
     * @param stage     the stage to change to
     * @return          the modified ticket
     */
    Ticket changeStage(Ticket ticket, TicketStage stage)

    /**
     * Changes the stage of the given ticket.
     *
     * @param ticket    the given ticket
     * @param stage     the stage to change to
     * @param creator   the user who triggers the stage change; {@code null} if
     *                  the customer changes the stage
     * @return          the modified ticket
     */
    Ticket changeStage(Ticket ticket, TicketStage stage, User creator)

    /**
     * Counts all tickets.
     *
     * @return  the number of all tickets
     */
    int count()

    /**
     * Counts the tickets which belong to the given helpdesk.
     *
     * @param helpdesk  the given helpdesk
     * @return          the number of tickets
     */
    int countByHelpdesk(Helpdesk helpdesk)

    /**
     * Counts the tickets which belong to the given helpdesks.
     *
     * @param helpdesks the given helpdesks
     * @return          the number of tickets
     */
    int countByHelpdeskInList(Iterable<Helpdesk> helpdesks)

    @Query("select count(*) from $Ticket as t inner join t.helpdesk as h, HelpdeskUser as hu where hu.helpdesk = h and hu.user = $user and h.organization = $organization")
    int countByUserAndOrganization(User user, Organization organization)

    /**
     * Creates a note for the given ticket.
     *
     * @param ticket        the given ticket to store
     * @param message       the message text of the note
     * @param attachment    an optional attachment to store along the note
     * @param creator       the user who creates the note
     * @return              the modified ticket
     */
    Ticket createNote(Ticket ticket, String message, MultipartFile attachment,
                      User creator)

    /**
     * Creates the given ticket in the underlying database and stores the given
     * message and attachment along this.
     *
     * @param ticket        the given ticket to store
     * @param message       the message text to store along the ticket
     * @param attachment    an optional attachment to store along the ticket
     * @return              the stored ticket
     */
    Ticket createTicket(Ticket ticket, String message, MultipartFile attachment)

    /**
     * Deletes the ticket with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the tickets which belong to the given helpdesk.
     *
     * @param helpdesk  the given helpdesk
     * @return          a list of tickets
     */
    List<Ticket> findAllByHelpdesk(Helpdesk helpdesk)

    @Query("select t from $Ticket as t inner join t.helpdesk as h, HelpdeskUser as hu where hu.helpdesk = h and hu.user = $user and h.organization = $organization")
    List<Ticket> findAllByUserAndOrganization(User user,
                                              Organization organization,
                                              Map args)

    /**
     * Finds the tickets which belong to the given helpdesks.
     *
     * @param helpdesks the given helpdesks
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of tickets
     */
    List<Ticket> findAllByHelpdeskInList(Iterable<Helpdesk> helpdesks, Map args)

    /**
     * Gets the ticket with the given ID.
     *
     * @param id    the given ID
     * @return      the ticket or {@code null} if no such ticket with the given
     *              ID exists
     */
    Ticket get(Serializable id)

    /**
     * Gets a list of all tickets.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of tickets
     */
    List<Ticket> list(Map args)

    /**
     * Saves the given ticket.
     *
     * @param instance  the given ticket
     * @return          the saved ticket
     */
    Ticket save(Ticket instance)

    /**
     * Records sending a message concerning the given ticket from the customer
     * to the opposite site.
     *
     * @param ticket        the ticket the given message belongs to
     * @param message       the message to send
     * @param attachment    an optional attachment to send along the message
     * @return              the modified ticket
     */
    Ticket sendMessage(Ticket ticket, String message, MultipartFile attachment)

    /**
     * Records sending a message concerning the given ticket from the specified
     * sender to the opposite site.
     *
     * @param ticket        the ticket the given message belongs to
     * @param message       the message to send
     * @param attachment    an optional attachment to send along the message
     * @param sender        the sending user; if {@code null} the customer
     *                      sends the message
     * @return              the modified ticket
     */
    Ticket sendMessage(Ticket ticket, String message, MultipartFile attachment,
                       User sender)

    /**
     * Records sending a message concerning the given ticket from the specified
     * sender to the specified recipient.
     *
     * @param ticket        the ticket the given message belongs to
     * @param message       the message to send
     * @param attachment    an optional attachment to send along the message
     * @param sender        the sending user; if {@code null} the customer
     *                      sends the message
     * @param recipient     the receiving user; if {@code null} to opposite
     *                      site is the recipient
     * @return              the modified ticket
     */
    Ticket sendMessage(Ticket ticket, String message, MultipartFile attachment,
                       User sender, User recipient)
}


/**
 * The class {@code TicketService} contains auxiliary methods to work with
 * tickets in a helpdesk.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   1.4
 */
@Service(value = Ticket, name = 'ticketService')
abstract class TicketServiceImpl implements TicketService {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.ticketMessage


    //-- Fields ---------------------------------

    DataFileService dataFileService
    LinkGenerator grailsLinkGenerator
    MailSystemService mailSystemService


    //-- Public methods -------------------------

    Ticket assignUser(Ticket ticket, User creator, User assignTo) {
        ticket.assignedUser = assignTo
        ticket.addToLogEntries(new TicketLogEntry(
            action: TicketLogAction.assign,
            creator: creator,
            recipient: assignTo
        ))
        save ticket

        if (creator != assignTo) {
            mailSystemService.sendMail(
                to: assignTo.email,
                subject: [key: 'email.ticket.assignedUser.subject'],
                message: [
                    controller: 'ticket',
                    view: 'assignedUser',
                    model: [ticketInstance: ticket, creator: creator]
                ]
            )
        }

        ticket
    }

    Ticket changeStage(Ticket ticket, TicketStage stage) {
        changeStage ticket, stage, null
    }

    Ticket changeStage(Ticket ticket, TicketStage stage, User creator) {
        ticket.stage = stage
        ticket.addToLogEntries(new TicketLogEntry(
            action: TicketLogAction.changeStage,
            creator: creator,
            stage: stage
        ))
        save ticket

        if (stage == TicketStage.assigned && (ticket.email1 || ticket.email2))
        {
            mailSystemService.sendMail(
                to: ticket.email1 ?: ticket.email2,
                subject: [key: 'email.ticket.assigned.subject'],
                message: [
                    controller: 'ticket',
                    view: 'assigned',
                    model: [ticketInstance: ticket]
                ]
            )
        } else if (stage == TicketStage.resubmitted) {
            mailSystemService.sendMail(
                to: ticket.helpdesk.users*.email,
                subject: [key: 'email.ticket.resubmitted.subject'],
                message: [
                    controller: 'ticket',
                    view: 'resubmittedUser',
                    model: [ticketInstance: ticket]
                ]
            )
        } else if (stage == TicketStage.closed) {
            if (ticket.email1 || ticket.email2) {
                mailSystemService.sendMail(
                    to: ticket.email1 ?: ticket.email2,
                    subject: [key: 'email.ticket.closed.subject'],
                    message: [
                        controller: 'ticket',
                        view: 'closedCustomer',
                        model: [ticketInstance: ticket]
                    ]
                )
            }
            if (creator != null) {
                mailSystemService.sendMail(
                    to: ticket.assignedUser?.email ?: ticket.helpdesk.users*.email,
                    subject: [key: 'email.ticket.closed.subject'],
                    message: [
                        controller: 'ticket',
                        view: 'closedUser',
                        model: [ticketInstance: ticket]
                    ]
                )
            }
        }

        ticket
    }

    Ticket createNote(Ticket ticket, String message, MultipartFile attachment,
                      User creator)
    {
        DataFile dataFile = dataFileService.storeFile(FILE_TYPE, attachment)
        ticket.addToLogEntries(new TicketLogEntry(
            action: TicketLogAction.note,
            creator: creator,
            message: message,
            attachment: dataFile
        ))
        save ticket
    }

    Ticket createTicket(Ticket ticket, String message, MultipartFile attachment)
    {
        DataFile dataFile = dataFileService.storeFile(FILE_TYPE, attachment)
        ticket.stage = TicketStage.created
        ticket.addToLogEntries(new TicketLogEntry(
            action: TicketLogAction.create
        )).addToLogEntries(new TicketLogEntry(
            action: TicketLogAction.sendMessage,
            message: message,
            attachment: dataFile
        ))
        save ticket

        /* send mail to helpdesk team */
        mailSystemService.sendMail(
            to: ticket.helpdesk.users*.email,
            subject: [key: 'email.ticket.create.subject'],
            message: [
                controller: 'ticket',
                view: 'createdUsers',
                model: [ticketInstance: ticket, messageText: message]
            ]
        )

        /* send mail to customer */
        if (ticket.email1 || ticket.email2) {
            Map model = getCustomerEmailLinks(ticket)
            model.ticketInstance = ticket
            model.messageText = message
            mailSystemService.sendMail(
                to: ticket.email1 ?: ticket.email2,
                subject: [key: 'email.ticket.create.subject'],
                message: [
                    controller: 'ticket', view: 'createdCustomer', model: model
                ]
            )
        }

        ticket
    }

    Ticket sendMessage(Ticket ticket, String message, MultipartFile attachment)
    {
        sendMessage ticket, message, attachment, null
    }

    Ticket sendMessage(Ticket ticket, String message, MultipartFile attachment,
                       User sender)
    {
        sendMessage ticket, message, attachment, sender, null
    }

    Ticket sendMessage(Ticket ticket, String message, MultipartFile attachment,
                       User sender, User recipient)
    {
        DataFile dataFile = dataFileService.storeFile(FILE_TYPE, attachment)
        ticket.addToLogEntries(new TicketLogEntry(
            action: TicketLogAction.sendMessage,
            creator: sender,
            recipient: recipient,
            message: message,
            attachment: dataFile
        ))
        save ticket

        def toAddr
        String msgView = 'sendMessageUser'
        Map msgModel = null
        if (sender == null) {               // customer -> engineer/team
            toAddr = ticket.assignedUser?.email ?: ticket.helpdesk.users*.email
            msgModel = [ticketInstance: ticket, messageText: message]
        } else if (recipient == null) {     // engineer -> customer
            toAddr = ticket.email1 ?: ticket.email2
            if (toAddr) {
                msgView = 'sendMessageCustomer'
                msgModel = getCustomerEmailLinks(ticket)
                msgModel.ticketInstance = ticket
                msgModel.messageText = message
                msgModel.sender = sender
            }
        } else {                            // engineer -> engineer
            toAddr = recipient.email
            msgModel = [
                ticketInstance: ticket, messageText: message, sender: sender
            ]
        }

        if (toAddr) {
            mailSystemService.sendMail(
                to: toAddr,
                subject: [key: 'email.ticket.sendMessage.subject'],
                message: [controller: 'ticket', view: msgView, model: msgModel]
            )
        }

        ticket
    }


    //-- Non-public methods ---------------------

    /**
     * Computes a map which can be used as model when rendering GSPs which
     * contains links to view a ticket and to show all tickets in frontend.
     *
     * @param ticket    the ticket referred to in the links
     * @return          a map containing these links
     */
    private Map getCustomerEmailLinks(Ticket ticket) {

        /*
         * IMPLEMENTATION NOTES:
         *
         * Rendering templates this way works out of the request scope.
         * Thus, generating absolute links is not possible because the
         * <g:link> or <g:createLink> tags need to know the schema (HTTP,
         * HTTPS etc.) to render an absolute link.  So, if you try to use
         * these tags in the .gsp files you'll get an
         * UnsupportedOperationException with the message "You cannot read
         * the server port in non-request rendering operations."
         */
        String showLink = grailsLinkGenerator.link(
            controller: 'ticket', action: 'frontendShow', id: ticket.id,
            params: [
                helpdesk: ticket.helpdesk.id,
                accessCode: ticket.helpdesk.accessCode
            ],
            absolute: true
        )
        String overviewLink = grailsLinkGenerator.link(
            mapping: 'helpdeskFrontend', params: [
                urlName: ticket.helpdesk.urlName,
                accessCode: ticket.helpdesk.accessCode
            ],
            absolute: true
        )

        [showLink: showLink, overviewLink: overviewLink]
    }
}
