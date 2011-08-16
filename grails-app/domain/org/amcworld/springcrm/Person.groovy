package org.amcworld.springcrm

import java.util.Date;

class Person {

    static constraints = {
        number(unique:true)
        organization()
        salutation(nullable:true)
        firstName(blank:false)
        lastName(blank:false)
        mailingAddrStreet(widget:'textarea', nullable:true)
        mailingAddrPoBox(nullable:true)
        mailingAddrPostalCode(nullable:true)
        mailingAddrLocation(nullable:true)
        mailingAddrState(nullable:true)
        mailingAddrCountry(nullable:true)
        otherAddrStreet(widget:'textarea', nullable:true)
        otherAddrPoBox(nullable:true)
        otherAddrPostalCode(nullable:true)
        otherAddrLocation(nullable:true)
        otherAddrState(nullable:true)
        otherAddrCountry(nullable:true)
        phone(maxSize:40, nullable:true)
        phoneHome(maxSize:40, nullable:true)
        mobile(maxSize:40, nullable:true)
        fax(maxSize:40, nullable:true)
        phoneAssistant(maxSize:40, nullable:true)
        phoneOther(maxSize:40, nullable:true)
        email1(email:true, nullable:true)
        email2(email:true, nullable:true)
        jobTitle(nullable:true)
        department(nullable:true)
        assistant(nullable:true)
        birthday(nullable:true)
        notes(widget:'textarea', nullable:true)
		picture(nullable:true, maxSize:1048576)
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [organization:Organization]
    static hasMany = [
		calls:Call, noteEntries:Note, quotes:Quote, salesOrders:SalesOrder,
		invoices:Invoice
	]
	static mapping = {
		calls column:'Person'
		sort 'lastName'
	}
	static searchable = true
	static transients = ['fullNumber', 'fullName', 'mailingAddr', 'otherAddr']
	
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
	
	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}
	
	String getFullName() {
		return "${firstName} ${lastName}"
	}
	
	String getMailingAddr() {
		String s = mailingAddrStreet ?: ''
		if (mailingAddrLocation) {
			if (s) {
				s += ','
			}
			if (mailingAddrPostalCode) {
				if (s) {
					s += ' '
				}
				s += mailingAddrPostalCode ?: ''
			}
			if (s) {
				s += ' '
			}
			s += mailingAddrLocation ?: ''
		}
		return s
	}
	
	String getOtherAddr() {
		String s = otherAddrStreet ?: ''
		if (otherAddrLocation) {
			if (s) {
				s += ','
			}
			if (otherAddrPostalCode) {
				if (s) {
					s += ' '
				}
				s += otherAddrPostalCode ?: ''
			}
			if (s) {
				s += ' '
			}
			s += otherAddrLocation ?: ''
		}
		return s
	}

    String toString() {
		String s = ''
		if (lastName) {
			s += lastName ?: ''
		}
		if (lastName && firstName) {
			s += ', '
		}
		if (firstName) {
			s += firstName ?: ''
		}
        return s
    }
}
