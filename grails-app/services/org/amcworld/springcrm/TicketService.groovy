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
                       User sender = null, User recipient = null) {
        DataFile dataFile = dataFileService.storeFile(FILE_TYPE, attachment)
        ticket.addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.sendMessage,
                creator: sender,
                recipient: recipient,
                message: message,
                attachment: dataFile
            ))
            .save()
    }
}
