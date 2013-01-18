/*
 * Organization.groovy
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
 * The class {@code Organization} represents an organization, either a
 * customer or a vendor.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class Organization {

    //-- Class variables ------------------------

    static constraints = {
        number(unique: 'recType', widget: 'autonumber')
		recType(range: 1..3)
        name(blank: false, unique: true)
        billingAddrStreet(nullable: true, widget: 'textarea')
        billingAddrPoBox(nullable: true)
        billingAddrPostalCode(nullable: true)
        billingAddrLocation(nullable: true)
        billingAddrState(nullable: true)
        billingAddrCountry(nullable: true)
        shippingAddrStreet(nullable: true, widget: 'textarea')
        shippingAddrPoBox(nullable: true)
        shippingAddrPostalCode(nullable: true)
        shippingAddrLocation(nullable: true)
        shippingAddrState(nullable: true)
        shippingAddrCountry(nullable: true)
        phone(nullable: true, maxSize: 40)
        fax(nullable: true, maxSize: 40)
        phoneOther(nullable: true, maxSize: 40)
        email1(nullable: true, email: true)
        email2(nullable: true, email: true)
        website(nullable: true, widget: 'url')
		legalForm(nullable: true)
        type(nullable: true)
        industry(nullable: true)
        owner(nullable: true)
        numEmployees(nullable: true)
        rating(nullable: true)
        notes(nullable: true, widget: 'textarea')
        docPlaceholderValue(nullable: true)
		dateCreated()
		lastUpdated()
    }
    static hasMany = [
		persons: Person, calls: Call, noteEntries: Note, quotes: Quote,
		salesOrders: SalesOrder, invoices: Invoice, creditMemos: CreditMemo,
        dunnings: Dunning, purchaseInvoices: PurchaseInvoice, projects: Project
	]
	static mapping = {
		calls column: 'Organization'
		sort 'name'
        name index: 'name'
		notes type: 'text'
        recType index: 'rec_type'
	}
	static searchable = true
	static transients = [
		'fullNumber', 'shortName', 'billingAddr', 'shippingAddr', 'customer',
		'vendor'
	]


    //-- Instance variables ---------------------

	def seqNumberService

    int number
	byte recType
    String name
    String billingAddrStreet
    String billingAddrPoBox
    String billingAddrPostalCode
    String billingAddrLocation
    String billingAddrState
    String billingAddrCountry
    String shippingAddrStreet
    String shippingAddrPoBox
    String shippingAddrPostalCode
    String shippingAddrLocation
    String shippingAddrState
    String shippingAddrCountry
    String phone
    String fax
    String phoneOther
    String email1
    String email2
    String website
	String legalForm
    OrgType type
    Industry industry
    String owner
    String numEmployees
    Rating rating
    String notes
    String docPlaceholderValue
	Date dateCreated
	Date lastUpdated


    //-- Constructors ---------------------------

	Organization() {}

	Organization(Organization org) {
		recType = org.recType
		name = org.name
		billingAddrStreet = org.billingAddrStreet
		billingAddrPoBox = org.billingAddrPoBox
		billingAddrPostalCode = org.billingAddrPostalCode
		billingAddrLocation = org.billingAddrLocation
		billingAddrState = org.billingAddrState
		billingAddrCountry = org.billingAddrCountry
		shippingAddrStreet = org.shippingAddrStreet
		shippingAddrPoBox = org.shippingAddrPoBox
		shippingAddrPostalCode = org.shippingAddrPostalCode
		shippingAddrLocation = org.shippingAddrLocation
		shippingAddrState = org.shippingAddrState
		shippingAddrCountry = org.shippingAddrCountry
		phone = org.phone
		fax = org.fax
		phoneOther = org.phoneOther
		email1 = org.email1
		email2 = org.email2
		website = org.website
		legalForm = org.legalForm
		type = org.type
		industry = org.industry
		owner = org.owner
		numEmployees = org.numEmployees
		rating = org.rating
		notes = org.notes
	}


    //-- Public methods -------------------------

	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}

	String getShortName() {
		String res = name ?: ''
		if (res.length() > 40) {
			res = name.substring(0, 40) + '...'
		}
		return res
	}

	String getBillingAddr() {
		StringBuilder s = new StringBuilder(billingAddrStreet ?: '')
		if (billingAddrLocation) {
			if (s) {
				s << ','
			}
			if (billingAddrPostalCode) {
				if (s) {
					s << ' '
				}
				s << billingAddrPostalCode ?: ''
			}
			if (s) {
				s << ' '
			}
			s << billingAddrLocation ?: ''
		}
		return s.toString()
	}

	String getShippingAddr() {
		StringBuilder s = new StringBuilder(shippingAddrStreet ?: '')
		if (shippingAddrLocation) {
			if (s) {
				s << ','
			}
			if (shippingAddrPostalCode) {
				if (s) {
					s << ' '
				}
				s << shippingAddrPostalCode ?: ''
			}
			if (s) {
				s << ' '
			}
			s << shippingAddrLocation ?: ''
		}
		return s.toString()
	}

    void setWebsite(String website) {
		website = website ?: ''
        if ((website.size() > 0) && !(website =~ '^https?://')) {
			website = "http://${website}"
        }
        this.website = website
    }

	boolean isCustomer() {
		return (this.recType & 1) != 0
	}

	boolean isVendor() {
		return (this.recType & 2) != 0
	}

    String toString() {
        return name ?: ''
    }

	def beforeInsert() {
		if (number == 0) {
			number = seqNumberService.nextNumber(getClass())
		}
	}
}
