/*
 * Person.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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
 * The class {@code Person} represents a person of an organization.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class Person {

    //-- Class variables ------------------------

    static constraints = {
        number(unique: true, widget: 'autonumber')
        organization()
        salutation(nullable: true)
        firstName(blank: false)
        lastName(blank: false)
        mailingAddrStreet(nullable: true, widget: 'textarea')
        mailingAddrPoBox(nullable: true)
        mailingAddrPostalCode(nullable: true)
        mailingAddrLocation(nullable: true)
        mailingAddrState(nullable: true)
        mailingAddrCountry(nullable: true)
        otherAddrStreet(nullable: true, widget: 'textarea')
        otherAddrPoBox(nullable: true)
        otherAddrPostalCode(nullable: true)
        otherAddrLocation(nullable: true)
        otherAddrState(nullable: true)
        otherAddrCountry(nullable: true)
        phone(nullable: true, maxSize: 40)
        phoneHome(nullable: true, maxSize: 40)
        mobile(nullable: true, maxSize: 40)
        fax(nullable: true, maxSize: 40)
        phoneAssistant(nullable: true, maxSize: 40)
        phoneOther(nullable: true, maxSize: 40)
        email1(nullable: true, email: true)
        email2(nullable: true, email: true)
        jobTitle(nullable: true)
        department(nullable: true)
        assistant(nullable: true)
        birthday(nullable: true)
        notes(nullable: true, widget: 'textarea')
		picture(nullable: true, maxSize: 1048576)
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [organization: Organization]
    static hasMany = [
		calls: Call, noteEntries: Note, quotes: Quote, salesOrders: SalesOrder,
		invoices: Invoice, creditMemos: CreditMemo, dunnings: Dunning
	]
	static mapping = {
		calls column: 'Person'
        firstName index: 'first_name'
        lastName index: 'last_name'
		notes type: 'text'
        sort 'lastName'
	}
	static searchable = true
	static transients = ['fullNumber', 'fullName', 'mailingAddr', 'otherAddr']


    //-- Instance variables ---------------------

	def seqNumberService

    int number
    Organization organization
    Salutation salutation
    String firstName
    String lastName
    String mailingAddrStreet
    String mailingAddrPoBox
    String mailingAddrPostalCode
    String mailingAddrLocation
    String mailingAddrState
    String mailingAddrCountry
    String otherAddrStreet
    String otherAddrPoBox
    String otherAddrPostalCode
    String otherAddrLocation
    String otherAddrState
    String otherAddrCountry
    String phone
    String phoneHome
    String mobile
    String fax
    String phoneAssistant
    String phoneOther
    String email1
    String email2
    String jobTitle
    String department
    String assistant
    Date birthday
    byte [] picture
    String notes
	Date dateCreated
	Date lastUpdated


    //-- Constructors ---------------------------

	Person() {}

	Person(Person p) {
		organization = p.organization
		salutation = p.salutation
		firstName = p.firstName
		lastName = p.lastName
		mailingAddrStreet = p.mailingAddrStreet
		mailingAddrPoBox = p.mailingAddrPoBox
		mailingAddrPostalCode = p.mailingAddrPostalCode
		mailingAddrLocation = p.mailingAddrLocation
		mailingAddrState = p.mailingAddrState
		mailingAddrCountry = p.mailingAddrCountry
		otherAddrStreet = p.otherAddrStreet
		otherAddrPoBox = p.otherAddrPoBox
		otherAddrPostalCode = p.otherAddrPostalCode
		otherAddrLocation = p.otherAddrLocation
		otherAddrState = p.otherAddrState
		otherAddrCountry = p.otherAddrCountry
		phone = p.phone
		phoneHome = p.phoneHome
		mobile = p.mobile
		fax = p.fax
		phoneAssistant = p.phoneAssistant
		phoneOther = p.phoneOther
		email1 = p.email1
		email2 = p.email2
		jobTitle = p.jobTitle
		department = p.department
		assistant = p.assistant
		birthday = p.birthday
		picture = p.picture
		notes = p.notes
	}


    //-- Public methods -------------------------

	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}

	String getFullName() {
		return "${firstName} ${lastName}"
	}

	String getMailingAddr() {
		StringBuilder s = new StringBuilder(mailingAddrStreet ?: '')
		if (mailingAddrLocation) {
			if (s) {
				s << ','
			}
			if (mailingAddrPostalCode) {
				if (s) {
					s << ' '
				}
				s << mailingAddrPostalCode ?: ''
			}
			if (s) {
				s << ' '
			}
			s << mailingAddrLocation ?: ''
		}
		return s.toString()
	}

	String getOtherAddr() {
		StringBuilder s = new StringBuilder(otherAddrStreet ?: '')
		if (otherAddrLocation) {
			if (s) {
				s << ','
			}
			if (otherAddrPostalCode) {
				if (s) {
					s << ' '
				}
				s << otherAddrPostalCode ?: ''
			}
			if (s) {
				s << ' '
			}
			s << otherAddrLocation ?: ''
		}
		return s.toString()
	}

    String toString() {
		StringBuilder s = new StringBuilder()
		if (lastName) {
			s << lastName ?: ''
		}
		if (lastName && firstName) {
			s << ', '
		}
		if (firstName) {
			s << firstName ?: ''
		}
        return s.toString()
    }

	def beforeInsert() {
		if (number == 0) {
			number = seqNumberService.nextNumber(getClass())
		}
	}
}
