/*
 * Call.groovy
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
 * The class {@code Call} represents a phone call.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class Call {

    //-- Class variables ------------------------

    static constraints = {
		subject(blank: false)
		notes(nullable: true, widget: 'textarea')
		organization(nullable: true)
		person(nullable: true)
		phone(nullable: true, maxSize: 40)
		start()
		type()
		status()
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [ organization: Organization, person: Person ]
	static mapping = {
		sort start: 'desc'
		table 'phone_call'
		notes type: 'text'
        subject index: 'subject'
    }
	static searchable = true


    //-- Instance variables ---------------------

	String subject
	String notes
	String phone
	Date start = new Date()
	CallType type
	CallStatus status
	Date dateCreated
	Date lastUpdated


    //-- Constructors ---------------------------

	Call() {}

	Call(Call call) {
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

	String toString() {
		return subject
	}
}


/**
 * The enumeration {@code CallType} represents the types of phone calls, that
 * is, the direction of them.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
enum CallType {
    incoming, outgoing
}

/**
 * The enumeration {@code CallStatus} represents the status of a phone call.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
enum CallStatus {
    planned, completed, acknowledged, cancelled
}
