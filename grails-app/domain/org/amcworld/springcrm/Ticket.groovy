/*
 * Ticket.groovy
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
 * The class {@code Ticket} represents a ticket of a particular helpdesk.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class Ticket {

    //-- Class variables ------------------------

    static belongsTo = [helpdesk: Helpdesk]
    static constraints = {
        number unique: true, widget: 'autonumber'
        subject blank: false
        stage()
        salutation nullable: true
        firstName blank: false
        lastName blank: false
        street nullable: true, widget: 'textarea'
        postalCode nullable: true
        location nullable: true
        state nullable: true
        country nullable: true
        phone nullable: true, maxSize: 40, validator: { phone, ticket ->
            !ticket.email1 && !ticket.email2 && !phone && !ticket.phoneHome && !ticket.mobile ? ['ticket.contact.validator.required'] : null
        }
        phoneHome nullable: true, maxSize: 40, validator: { phoneHome, ticket ->
            !ticket.email1 && !ticket.email2 && !ticket.phone && !phoneHome && !ticket.mobile ? ['ticket.contact.validator.required'] : null
        }
        mobile nullable: true, maxSize: 40, validator: { mobile, ticket ->
            !ticket.email1 && !ticket.email2 && !ticket.phone && !ticket.phoneHome && !mobile ? ['ticket.contact.validator.required'] : null
        }
        fax nullable: true, maxSize: 40
        email1 nullable: true, email: true, validator: { email1, ticket ->
            !email1 && !ticket.email2 && !ticket.phone && !ticket.phoneHome && !ticket.mobile ? ['ticket.contact.validator.required'] : null
        }
        email2 nullable: true, email: true, validator: { email2, ticket ->
            !ticket.email1 && !email2 && !ticket.phone && !ticket.phoneHome && !ticket.mobile ? ['ticket.contact.validator.required'] : null
        }
        creator nullable: true
        assignedUser nullable: true
        priority nullable: true
        logEntries minSize: 1
        dateCreated()
        lastUpdated()
    }
    static hasMany = [logEntries: TicketLogEntry]
    static searchable = true
    static transients = [
        'address', 'customerName', 'fullName', 'fullNumber', 'initialMessage',
        'messageText'
    ]


    //-- Instance variables ---------------------

    def seqNumberService

    int number
    String subject
    TicketStage stage
    Salutation salutation
    String firstName
    String lastName
    String street
    String postalCode
    String location
    String state
    String country
    String phone
    String phoneHome
    String mobile
    String fax
    String email1
    String email2
    User creator
    User assignedUser
    TicketPriority priority
    List<TicketLogEntry> logEntries
    Date dateCreated
    Date lastUpdated

    /* temporary value for the first message text; not persisted */
    String messageText


    //-- Properties -----------------------------

    String getAddress() {
        StringBuilder s = new StringBuilder(street ?: '')
        if (location) {
            if (s) s << ','
            if (postalCode) {
                if (s) s << ' '
                s << postalCode ?: ''
            }
            if (s) s << ' '
            s << location ?: ''
        }
        s.toString()
    }

    String getCustomerName() {
        "${lastName}, ${firstName}"
    }

    String getFullName() {
        StringBuilder s = new StringBuilder()
        if (salutation) {
            s << salutation.name << ' '
        }
        s << firstName << ' ' << lastName
        s.toString()
    }

    String getFullNumber() {
        seqNumberService.format getClass(), number
    }

    /**
     * Gets the initial message text of this ticket, that is, the message text
     * submitted when the ticket was created.
     *
     * @return  the initial message text
     */
    String getInitialMessage() {
        if (!messageText) {
            Ticket thisTicket = this
            def query = TicketLogEntry.where {
                ticket == thisTicket && action == TicketLogAction.sendMessage
            }
            def l = query.list(max: 1, sort: 'dateCreated', order: 'desc')
            assert !l.empty
            messageText = l.first().message
        }
        messageText
    }


    //-- Public methods -------------------------

    def beforeInsert() {
        if (number == 0) {
            number = seqNumberService.nextNumber(getClass())
        }
    }

    @Override
    boolean equals(Object obj) {
        if (!(obj instanceof Ticket)) {
            return false
        }

        (id == null) ? super.equals(obj) : id == obj.id
    }

    @Override
    int hashCode() {
        (id == null) ? super.hashCode() : (id as int)
    }

    @Override
    String toString() {
        subject
    }
}


/**
 * The enumeration {@code TicketStage} represents the stages of tickets.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
enum TicketStage {
    created,
    assigned,
    inProcess,
    closed,
    resubmitted
}
