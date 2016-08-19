/*
 * Organization.groovy
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

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code Organization} represents an organization, either a
 * customer or a vendor.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class Organization implements GormEntity<Organization>, NumberedDomain {

    //-- Class fields ---------------------------

    static constraints = {
        number unique: 'recType', widget: 'autonumber'
        recType range: 1..3
        name blank: false, unique: true
        phone nullable: true, maxSize: 40, widget: 'tel'
        fax nullable: true, maxSize: 40, widget: 'tel'
        phoneOther nullable: true, maxSize: 40, widget: 'tel'
        email1 nullable: true, email: true, widget: 'email'
        email2 nullable: true, email: true, widget: 'email'
        website nullable: true, widget: 'url'
        legalForm nullable: true
        type nullable: true
        industry nullable: true
        owner nullable: true
        numEmployees nullable: true
        rating nullable: true
        notes nullable: true, widget: 'textarea'
        termOfPayment nullable: true
        docPlaceholderValue nullable: true
        assessmentPositive nullable: true, widget: 'textarea'
        assessmentNegative nullable: true, widget: 'textarea'
    }
    static embedded = ['billingAddr', 'shippingAddr']
    static hasMany = [
        persons: Person, calls: Call, noteEntries: Note, quotes: Quote,
        salesOrders: SalesOrder, invoices: Invoice, creditMemos: CreditMemo,
        dunnings: Dunning, purchaseInvoices: PurchaseInvoice,
        projects: Project, calendarEvents: CalendarEvent, helpdesks: Helpdesk
    ]
    static mapping = {
        calls column: 'Organization'
        sort 'name'
        name index: 'name'
        notes type: 'text'
        recType index: 'rec_type'
    }
    static transients = ['fullNumber', 'shortName', 'customer', 'vendor']


    //-- Fields ---------------------------------

    byte recType
    String name
    Address billingAddr
    Address shippingAddr
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
    Integer termOfPayment
    String docPlaceholderValue
    String assessmentPositive
    String assessmentNegative
    Date dateCreated
    Date lastUpdated


    //-- Constructors ---------------------------

    Organization() {}

    Organization(Organization org) {
        recType = org.recType
        name = org.name
        billingAddr = new Address(org.billingAddr)
        shippingAddr = new Address(org.shippingAddr)
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
        docPlaceholderValue = org.docPlaceholderValue
        assessmentPositive = org.assessmentPositive
        assessmentNegative = org.assessmentNegative
    }


    //-- Properties -----------------------------

    String getShortName() {
        String res = name ?: ''
        if (res.length() > 40) {
            res = name.substring(0, 40) + '...'
        }
        res
    }

    boolean isCustomer() {
        (recType & 1) != 0
    }

    boolean isVendor() {
        (recType & 2) != 0
    }

    void setWebsite(String website) {
        website = website ?: ''
        if (website && !(website ==~ '^https?://.*')) {
            website = "http://${website}"
        }
        this.website = website
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Organization && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        name ?: ''
    }
}
