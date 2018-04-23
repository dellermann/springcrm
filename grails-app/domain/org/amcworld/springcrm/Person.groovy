/*
 * Person.groovy
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

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code Person} represents a person of an organization.
 *
 * @author  Daniel Ellermann
 * @author 	Philip Drozd
 * @version 2.1
 */
class Person implements GormEntity<Person>, NumberedDomain {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'title', 'firstName', 'lastName', 'mailingAddr.street',
        'mailingAddr.poBox', 'mailingAddr.postalCode', 'mailingAddr.location',
        'mailingAddr.state', 'mailingAddr.country', 'otherAddr.street',
        'otherAddr.poBox', 'otherAddr.postalCode', 'otherAddr.location',
        'otherAddr.state', 'otherAddr.country', 'phone', 'phoneHome', 'mobile',
        'fax', 'phoneAssistant', 'phoneOther', 'email1', 'email2', 'jobTitle',
        'department', 'assistant', 'notes', 'assessmentPositive',
        'assessmentNegative'
    ].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
        number unique: true, widget: 'autonumber'
        salutation nullable: true
        title nullable: true
        firstName blank: false
        lastName blank: false
        phone nullable: true, maxSize: 40
        phoneHome nullable: true, maxSize: 40
        mobile nullable: true, maxSize: 40
        fax nullable: true, maxSize: 40
        phoneAssistant nullable: true, maxSize: 40
        phoneOther nullable: true, maxSize: 40
        email1 nullable: true, email: true
        email2 nullable: true, email: true
        jobTitle nullable: true
        department nullable: true
        assistant nullable: true
        birthday nullable: true
        notes nullable: true, widget: 'textarea'
        picture nullable: true, maxSize: 1048576
        assessmentPositive nullable: true, widget: 'textarea'
        assessmentNegative nullable: true, widget: 'textarea'
    }
    static belongsTo = [organization: Organization]
    static embedded = ['mailingAddr', 'otherAddr']
    static hasMany = [
        calls: Call, noteEntries: Note, quotes: Quote, salesOrders: SalesOrder,
        invoices: Invoice, creditMemos: CreditMemo, dunnings: Dunning,
        projects: Project
    ]
    static mapping = {
        calls column: 'Person'
        firstName index: 'first_name'
        lastName index: 'last_name'
        notes type: 'text'
        sort 'lastName'
    }
    static transients = ['fullName']


    //-- Fields ---------------------------------

    Organization organization
    Salutation salutation
    String title
    String firstName
    String lastName
    Address mailingAddr
    Address otherAddr
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
    String assessmentPositive
    String assessmentNegative
    Date dateCreated
    Date lastUpdated


    //-- Constructors ---------------------------

    Person() {}

    Person(Person p) {
        organization = p.organization
        salutation = p.salutation
        title = p.title
        firstName = p.firstName
        lastName = p.lastName
        mailingAddr = new Address(p.mailingAddr)
        otherAddr = new Address(p.otherAddr)
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
        assessmentPositive = p.assessmentPositive
        assessmentNegative = p.assessmentNegative
    }


    //-- Properties -----------------------------

    String getFullName() {
        String firstName = this.firstName?.trim()?:''
        String lastName = this.lastName?.trim()?:''

        StringBuilder buf = new StringBuilder (firstName)
        if(firstName && lastName) {
            buf << ' '
        }
        buf << lastName
        buf.toString()
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Person && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        StringBuilder s = new StringBuilder()
        if (lastName) s << lastName ?: ''
        if (lastName && firstName) s << ', '
        if (firstName) s << firstName ?: ''
        s.toString()
    }
}
