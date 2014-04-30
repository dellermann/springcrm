/*
 * TicketService.groovy
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

import com.naleid.grails.MarkdownService
import grails.gsp.PageRenderer
import grails.plugin.mail.MailService
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code TicketService} contains auxiliary methods to work with
 * tickets in a helpdesk.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class TicketService {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.ticketMessage


    //-- Instance variables ---------------------

    DataFileService dataFileService
    GrailsApplication grailsApplication
    LinkGenerator grailsLinkGenerator
    PageRenderer groovyPageRenderer
    MailService mailService
    MarkdownService markdownService
    MessageSource messageSource


    //-- Public methods -------------------------

    /**
     * Assigns the given ticket to the specified user.
     *
     * @param ticket    the given ticket
     * @param creator   the user who assigns a user
     * @param assignTo  the user the ticket is assigned to
     * @return          the modified ticket
     */
    Ticket assignUser(Ticket ticket, User creator, User assignTo) {
        ticket.assignedUser = assignTo
        ticket.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.assign,
                creator: creator,
                recipient: assignTo
            ))
            .save()

        if (creator != assignTo) {
            String msgText = getTextMessage(
                'assignedUser', [ticketInstance: ticket, creator: creator]
            )
            sendMail {
                multipart true
                from fromAddr
                to assignTo.email
                subject messageSource.getMessage(
                    'email.ticket.assignedUser.subject', null, '', LCH.locale
                )
                text msgText
                html getHtmlMessage(msgText)
            }
        }

        ticket
    }

    /**
     * Changes the stage of the given ticket.
     *
     * @param ticket    the given ticket
     * @param stage     the stage to change to
     * @param creator   the user who triggers the stage change; {@code null} if
     *                  the customer changes the stage
     * @return          the modified ticket
     */
    Ticket changeStage(Ticket ticket, TicketStage stage, User creator = null) {
        ticket.stage = stage
        ticket.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.changeStage,
                creator: creator,
                stage: stage
            ))
            .save()

        if (stage == TicketStage.assigned && (ticket.email1 || ticket.email2))
        {
            String msgText =
                getTextMessage('assigned', [ticketInstance: ticket])
            sendMail {
                multipart true
                from fromAddr
                to ticket.email1 ?: ticket.email2
                subject messageSource.getMessage(
                    'email.ticket.assigned.subject', null, '', LCH.locale
                )
                text msgText
                html getHtmlMessage(msgText)
            }
        } else if (stage == TicketStage.closed) {
            if (ticket.email1 || ticket.email2) {
                String msgText =
                    getTextMessage('closedCustomer', [ticketInstance: ticket])
                sendMail {
                    multipart true
                    from fromAddr
                    to ticket.email1 ?: ticket.email2
                    subject messageSource.getMessage(
                        'email.ticket.closed.subject', null, '', LCH.locale
                    )
                    text msgText
                    html getHtmlMessage(msgText)
                }
            }
            if (!creator) {
                String msgText =
                    getTextMessage('closedUser', [ticketInstance: ticket])
                sendMail {
                    multipart true
                    from fromAddr
                    to ticket.assignedUser.email ?: ticket.helpdesk.users*.email
                    subject messageSource.getMessage(
                        'email.ticket.closed.subject', null, '', LCH.locale
                    )
                    text msgText
                    html getHtmlMessage(msgText)
                }
            }
        }

        ticket
    }

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
    {
        DataFile dataFile = dataFileService.storeFile(FILE_TYPE, attachment)
        ticket.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.note,
                creator: creator,
                message: message,
                attachment: dataFile
            ))
            .save()
    }

    /**
     * Creates the given ticket in the underlying database and stores the given
     * message and attachment along this.
     *
     * @param ticket        the given ticket to store
     * @param message       the message text to store along the ticket
     * @param attachment    an optional attachment to store along the ticket
     * @return              the stored ticket
     */
    Ticket createTicket(Ticket ticket, String message,
                        MultipartFile attachment)
    {
        DataFile dataFile = dataFileService.storeFile(FILE_TYPE, attachment)
        ticket.stage = TicketStage.created
        ticket.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.create
            ))
            .addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.sendMessage,
                message: message,
                attachment: dataFile
            ))
            .save()

        /* send mail to helpdesk team */
        String msgText = getTextMessage(
            'createdUsers', [ticketInstance: ticket, messageText: message]
        )
        sendMail {
            multipart true
            from fromAddr
            to ticket.helpdesk.users*.email
            subject messageSource.getMessage(
                'email.ticket.create.subject', null, '', LCH.locale
            )
            text msgText
            html getHtmlMessage(msgText)
        }

        /* send mail to customer */
        if (ticket.email1 || ticket.email2) {
            Map model = getCustomerEmailLinks(ticket)
            model.ticketInstance = ticket
            model.messageText = message
            msgText = getTextMessage('createdCustomer', model)
            sendMail {
                multipart true
                from fromAddr
                to ticket.email1 ?: ticket.email2
                subject messageSource.getMessage(
                    'email.ticket.create.subject', null, '', LCH.locale
                )
                text msgText
                html getHtmlMessage(msgText)
            }
        }

        ticket
    }

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
                       User sender = null, User recipient = null)
    {
        DataFile dataFile = dataFileService.storeFile(FILE_TYPE, attachment)
        ticket.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.sendMessage,
                creator: sender,
                recipient: recipient,
                message: message,
                attachment: dataFile
            ))
            .save()

        def toAddr = null
        String msgText
        if (sender == null) {               // customer -> engineer/team
            toAddr = ticket.assignedUser?.email ?: ticket.helpdesk.users*.email
            msgText = getTextMessage(
                'sendMessageUser',
                [ticketInstance: ticket, messageText: message]
            )
        } else if (recipient == null) {     // engineer -> customer
            toAddr = ticket.email1 ?: ticket.email2
            if (toAddr) {
                Map model = getCustomerEmailLinks(ticket)
                model.ticketInstance = ticket
                model.messageText = message
                model.sender = sender
                msgText = getTextMessage('sendMessageCustomer', model)
            }
        } else {                            // engineer -> engineer
            toAddr = recipient.email
            msgText = getTextMessage(
                'sendMessageUser',
                [ticketInstance: ticket, messageText: message, sender: sender]
            )
        }

        if (toAddr) {
            sendMail {
                multipart true
                from fromAddr
                to toAddr
                subject messageSource.getMessage(
                    'email.ticket.sendMessage.subject', null, '', LCH.locale
                )
                text msgText
                html getHtmlMessage(msgText)
            }
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
    protected Map getCustomerEmailLinks(Ticket ticket) {

        /*
         * IMPLEMENTATION NOTES:
         *
         * Rendering templates this way works out of the request scope.
         * Thus, generating absolute links is not possible because the
         * <g:link> or <g:createLink> tags need to know the schema (HTTP,
         * HTTPS etc.) to render an absolute link.  So, if you try to use
         * these tags in the .gsp files you'll get an
         * UnsupportedOperationExeption with the message "You cannot read
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

    /**
     * Gets the sender address for e-mails from ticket system.
     *
     * @return  the sender address
     */
    protected String getFromAddr() {
        grailsApplication.config.springcrm.mail.from
    }

    /**
     * Converts the given plain text to HTML using Markdown.
     *
     * @param text  the given text
     * @return      the code converted to HTML
     */
    protected String getHtmlMessage(String text) {
        markdownService.markdown text
    }

    /**
     * Retrieves the text in the given view and converts it to HTML using
     * Markdown.
     *
     * @param view  the view that should be rendered relative to
     *              "/email/ticket/"
     * @param model a data the view uses
     * @return      the rendered and converted HTML code
     */
    protected String getHtmlMessage(String view, Map model) {
        getHtmlMessage getTextMessage(view, model)
    }

    /**
     * Retrieves the text in the given view.
     *
     * @param view  the view that should be rendered
     * @param model a data the view uses
     * @return      the rendered plain text code
     */
    protected String getTextMessage(String view, Map model) {
        groovyPageRenderer.render view: "/email/ticket/${view}", model: model
    }

    /**
     * Really sends a mail using the given data.
     *
     * @param mail  the mail data
     */
    protected void sendMail(Closure mail) {
        mailService.sendMail mail
    }
}
