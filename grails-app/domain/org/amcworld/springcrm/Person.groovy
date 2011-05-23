package org.amcworld.springcrm

import java.util.Date;

class Person {

    static constraints = {
        number(unique:true)
        organization()
        salutation(nullable:true)
        firstName(blank:false)
        lastName(blank:false)
        mailingAddrStreet(widget:"textarea")
        mailingAddrPoBox()
        mailingAddrPostalCode()
        mailingAddrLocation()
        mailingAddrState()
        mailingAddrCountry()
        otherAddrStreet(widget:"textarea")
        otherAddrPoBox()
        otherAddrPostalCode()
        otherAddrLocation()
        otherAddrState()
        otherAddrCountry()
        phone(maxSize:40)
        phoneHome(maxSize:40)
        mobile(maxSize:40)
        fax(maxSize:40)
        phoneAssistant(maxSize:40)
        phoneOther(maxSize:40)
        email1(email:true)
        email2(email:true)
        jobTitle()
        department()
        assistant()
        birthday(nullable:true)
        notes(widget:"textarea")
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [organization:Organization]
    static hasMany = [calls:Call]
	static mapping = {
		calls column:"Person"
	}
	static transients = ["fullNumber", "mailingAddr", "otherAddr"]
	
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
    Calendar birthday
    // TODO image
    String notes
	Date dateCreated
	Date lastUpdated
	
	String getFullNumber() {
		return "PER-" + number
	}
	
	String getMailingAddr() {
		String s = mailingAddrStreet ?: ""
		if (mailingAddrLocation) {
			if (s) {
				s += ","
			}
			if (mailingAddrPostalCode) {
				if (s) {
					s += " "
				}
				s += mailingAddrPostalCode ?: ""
			}
			if (s) {
				s += " "
			}
			s += mailingAddrLocation ?: ""
		}
		return s
	}
	
	String getOtherAddr() {
		String s = otherAddrStreet ?: ""
		if (otherAddrLocation) {
			if (s) {
				s += ","
			}
			if (otherAddrPostalCode) {
				if (s) {
					s += " "
				}
				s += otherAddrPostalCode ?: ""
			}
			if (s) {
				s += " "
			}
			s += otherAddrLocation ?: ""
		}
		return s
	}

    String toString() {
        return lastName + ", " + firstName
    }
}
