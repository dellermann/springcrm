/*
 * TicketLogEntry.groovy
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

import org.springframework.context.MessageSourceResolvable


/**
 * The class {@code TicketLogEntry} represents a log entry for a particular
 * ticket.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class TicketLogEntry implements MessageSourceResolvable {

    //-- Class variables ------------------------

    static belongsTo = [ticket: Ticket]
    static constraints = {
        action()
        creator nullable: true
        recipient nullable: true
        stage nullable: true
        message nullable: true
        dateCreated()
    }
    static mapping = {
        version false
    }
    static transients = ['arguments', 'codes', 'defaultMessage']


    //-- Instance variables ---------------------

    TicketLogAction action
    User creator
    User recipient
    TicketStage stage
    TicketMessage message
    Date dateCreated


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof TicketLogEntry)) {
            return false
        }

        (id == null) ? super.equals(obj) : (id == obj.id)
    }

    @Override
    Object [] getArguments() {
        (action == TicketLogAction.assign) ? [recipient] : []
    }

    @Override
    String getDefaultMessage() {
        action.toString()
    }

    @Override
    String [] getCodes() {
        (action == TicketLogAction.changeStage) \
            ? ["ticket.log.action.changeStage.${stage}"]
            : ["ticket.log.action.${action}.label"]
    }

    @Override
    int hashCode() {
        (id == null) ? super.hashCode : (id as int)
    }

    @Override
    String toString() {
        action.toString()
    }
}


/**
 * The class {@code TicketLogAction} contains various action types for ticket
 * log entries.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
enum TicketLogAction {
    create,                 // creator
    changeStage,            // creator, stage
    assign,                 // creator, recipient
    sendMessage             // creator, recipient, message
}