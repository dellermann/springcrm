/*
 * Organization.groovy
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
 * The class {@code Organization} represents an organization, either a
 * customer or a vendor.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@EqualsAndHashCode(includes = ['id'])
class Organization implements NumberedDomain {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'name', 'billingAddr.street', 'billingAddr.poBox',
        'billingAddr.postalCode', 'billingAddr.location', 'billingAddr.state',
        'billingAddr.country', 'shippingAddr.street', 'shippingAddr.poBox',
        'shippingAddr.postalCode', 'shippingAddr.location',
        'shippingAddr.state', 'shippingAddr.country', 'phone', 'fax',
        'phoneOther', 'email1', 'email2', 'website', 'legalForm', 'type.name',
        'industry.name', 'owner', 'notes', 'assessmentPositive',
        'assessmentNegative'
    ].asImmutable()


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
        persons    : Person, calls: PhoneCall, noteEntries: Note, quotes: Quote,
        salesOrders: SalesOrder, invoices: Invoice, creditMemos: CreditMemo,
        dunnings   : Dunning, purchaseInvoices: PurchaseInvoice,
        projects   : Project, calendarEvents: CalendarEvent, helpdesks: Helpdesk
    ]
    static mapping = {
        sort 'name'
        name index: true
        notes type: 'text'
        recType index: true
    }
    static transients = ['shortName', 'client', 'vendor']


    //-- Fields ---------------------------------

    /**
     * Any negative assessments of the organization.
     */
    String assessmentNegative

    /**
     * Any positive assessments of the organization.
     */
    String assessmentPositive

    /**
     * The billing address of the organization.
     */
    Address billingAddr

    /**
     * The timestamp of the creation of the record.
     */
    Date dateCreated

    /**
     * The placeholder of the organization in the document management.
     */
    String docPlaceholderValue

    /**
     * The e-mail address of the organization.
     */
    String email1

    /**
     * Any other e-mail address of the organization.
     */
    String email2

    /**
     * The fax of the organization.
     */
    String fax

    /**
     * The ID of the organization.
     */
    ObjectId id

    /**
     * The industry the organization belongs to.
     */
    Industry industry

    /**
     * The timestamp of the last update of the record.
     */
    Date lastUpdated

    /**
     * The legal form of the organization, for example "ltd.".
     */
    String legalForm

    /**
     * The name of the organization.
     */
    String name

    /**
     * Any notes about the organization.
     */
    String notes

    /**
     * Information about the number of employees of the organization.
     */
    String numEmployees

    /**
     * The name of the owner of the organization.
     */
    String owner

    /**
     * The phone of the organization.
     */
    String phone

    /**
     * Any other phone number of the organization.
     */
    String phoneOther

    /**
     * The rating of the organization.
     */
    Rating rating

    /**
     * The type of record (client or vendor) of the organization.
     */
    byte recType

    /**
     * The shipping address of the organization.
     */
    Address shippingAddr

    /**
     * The term of payment of the organization in days.
     */
    Integer termOfPayment

    /**
     * The type of the organization.
     */
    OrgType type

    /**
     * The URL of the website of the organization.
     */
    String website


    //-- Constructors ---------------------------

    Organization() {
        billingAddr = new Address()
        shippingAddr = new Address()
    }

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
            res = name.substring(0, 40) + '…'
        }

        res
    }

    boolean isClient() {
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
    String toString() {
        name ?: ''
    }
}
