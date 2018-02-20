/*
 * PhoneCall.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId


/**
 * The class {@code PhoneCall} represents a phone call.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@EqualsAndHashCode(includes = ['id'])
class PhoneCall {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS =
        ['subject', 'notes', 'phone'].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
        subject blank: false
        notes nullable: true, widget: 'textarea'
        organization nullable: true
        person nullable: true
        phone nullable: true, maxSize: 40
    }
    static belongsTo = [organization: Organization, person: Person]
    static mapping = {
        sort start: 'desc'
        notes type: 'text'
        subject index: true
    }


    //-- Fields ---------------------------------

    /**
     * The timestamp of the creation of the record.
     */
    Date dateCreated

    /**
     * The ID of the phone call.
     */
    ObjectId id

    /**
     * The timestamp of the last update of the record.
     */
    Date lastUpdated

    /**
     * Any notes about the phone call.
     */
    String notes

    /**
     * The organization associated to the phone call.
     */
    Organization organization

    /**
     * The person associated to the phone call.
     */
    Person person

    /**
     * The phone number of the call.
     */
    String phone

    /**
     * The timestamp of the start of the phone call.
     */
    Date start = new Date()

    /**
     * The status of the phone call.
     */
    PhoneCallStatus status

    /**
     * The subject of the phone call.
     */
    String subject

    /**
     * The type of phone call.
     */
    PhoneCallType type


    //-- Constructors ---------------------------

    PhoneCall() {}

    PhoneCall(PhoneCall call) {
        subject = call.subject
        notes = call.notes
        organization = call.organization
        person = call.person
        phone = call.phone
        start = call.start
        type = call.type
        status = call.status
    }


    //-- Public methods -------------------------

    @Override
    String toString() {
        subject ?: ''
    }
}


/**
 * The enumeration {@code PhoneCallStatus} represents the status of a phone
 * call.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
enum PhoneCallStatus {

    //-- Values ---------------------------------

    PLANNED, COMPLETED, ACKNOWLEDGED, CANCELLED
}


/**
 * The enumeration {@code PhoneCallType} represents the types of phone calls,
 * that is, the direction of them.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
enum PhoneCallType {

    //-- Values ---------------------------------

    INCOMING, OUTGOING
}
