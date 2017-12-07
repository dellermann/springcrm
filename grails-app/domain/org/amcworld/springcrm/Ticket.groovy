/*
 * Ticket.groovy
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


/**
 * The class {@code Ticket} represents a ticket of a particular helpdesk.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
class Ticket implements NumberedDomain {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'subject', 'salutation', 'firstName', 'lastName', 'address.street',
        'address.poBox', 'address.postalCode', 'address.location',
        'address.state', 'address.country', 'phone', 'phoneHome', 'mobile',
        'fax', 'email1', 'email2', 'logEntries.*message'
    ].asImmutable()


    //-- Class fields ---------------------------

    static belongsTo = [helpdesk: Helpdesk]
    static constraints = {
        number unique: true, widget: 'autonumber'
        subject blank: false
        salutation nullable: true
        firstName blank: false
        lastName blank: false
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
    }
    static embedded = ['address']
    static hasMany = [logEntries: TicketLogEntry]
    static mapping = {
        sort lastUpdated: 'desc'
    }
    static transients = [
        'customerName', 'fullName', 'fullNumber', 'initialMessage',
        'messageText'
    ]


    //-- Fields ---------------------------------

    Helpdesk helpdesk
    String subject
    TicketStage stage = TicketStage.created
    Salutation salutation
    String firstName
    String lastName
    Address address
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


    //-- Constructors ---------------------------

    /**
     * Creates an empty ticket.
     */
    Ticket() {}

    /**
     * Creates a new ticket by copying properties of the given one.
     *
     * @param ticket    the given ticket
     */
    Ticket(Ticket ticket) {
        subject = ticket.subject
        stage = ticket.stage
        salutation = ticket.salutation
        firstName = ticket.firstName
        lastName = ticket.lastName
        address = new Address(ticket.address)
        phone = ticket.phone
        phoneHome = ticket.phoneHome
        mobile = ticket.mobile
        fax = ticket.fax
        email1 = ticket.email1
        email2 = ticket.email2
        priority = ticket.priority
    }


    //-- Properties -----------------------------

    /**
     * Gets the name of the customer in the order last name, first name.
     *
     * @return  the name of the customer
     */
    String getCustomerName() {
        "${lastName}, ${firstName}"
    }

    /**
     * Gets the full name of the customer including salutation.
     *
     * @return  the full name of the customer
     */
    String getFullName() {
        StringBuilder s = new StringBuilder()
        if (salutation) {
            s << salutation.name << ' '
        }
        s << firstName << ' ' << lastName

        s.toString()
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
            def l = query.list(max: 1, sort: 'dateCreated', order: 'asc')
            assert !l.empty
            messageText = l.first().message
        }

        messageText
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Ticket && id == obj.id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
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
