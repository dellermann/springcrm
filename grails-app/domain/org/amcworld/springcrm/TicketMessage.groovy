/*
 * TicketMessage.groovy
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


/**
 * The class {@code TicketMessage} represents a message send related to a
 * ticket.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class TicketMessage {

    //-- Class variables ------------------------

    static belongsTo = [ticket: Ticket, logEntry: TicketLogEntry]
    static constraints = {
        message widget: 'textarea'
        sender nullable: true
        recipient nullable: true
        attachment nullable: true
        dateCreated()
    }
    static mapping = {
        message type: 'text'
        version false
    }
    static transients = ['internal']


    //-- Instance variables ---------------------

    String message
    User sender
    User recipient
    DataFile attachment
    Date dateCreated


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof TicketMessage)) {
            return false
        }

        (id == null) ? super.equals(obj) : id == obj.id
    }

    @Override
    int hashCode() {
        (id == null) ? super.hashCode() : (id as int)
    }

    /**
     * Returns whether or not this ticket message is for internal use only.
     * This is the case if both sender and recipient are system users (not
     * customers).
     *
     * @return  {@code true} if the ticket message is internal; {@code false}
     *          otherwise
     */
    boolean isInternal() {
        sender && recipient
    }

    @Override
    String toString() {
        message
    }
}
