/*
 * CreditMemoTests.groovy
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

import java.util.Date
import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * The class {@code CreditMemoTests} contains the unit test cases for
 * {@code CreditMemo}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(CreditMemo)
@Mock([CreditMemo, Organization, Person, InvoicingItem, Invoice, Dunning, InvoiceStage, SeqNumberService])
class CreditMemoTests {

    //-- Public methods -------------------------

    void testConstructor() {
        def creditMemo = new CreditMemo()
        assert 'C' == creditMemo.type
    }

    /*
     * XXX These tests produce errors because the Invoice.findByType('I') and
     * similar calls return null.
     */
//    void testCopyConstructor() {
//        def d = new Date()
//        makeOrganizationFixture()
//        def org = Organization.get(1)
//        makePersonFixture(org)
//        def person = Person.get(1)
//        makeInvoiceFixture(org, person, d)
//        def invoice = Invoice.findByType('I')
//        makeDunningFixture(org, person, invoice, d)
//        def dunning = Dunning.findByType('D')
//        def stage = new CreditMemoStage(name: 'created')
//        def creditMemo = new CreditMemo(number: 20000, subject: 'Test credit memo', organization: org, person: person, docDate: d, total: 1789.76, stage: stage, invoice: invoice, dunning: dunning)
//        makeInvoicingItemsFixture(creditMemo)
//
//        def anotherCreditMemo = new CreditMemo(creditMemo)
//        assert 'Test credit memo' == anotherCreditMemo.subject
//        assert null != anotherCreditMemo.organization
//        assert 'AMC World Technologies GmbH' == anotherCreditMemo.organization.name
//        assert null != anotherCreditMemo.person
//        assert 'Daniel' == anotherCreditMemo.person.firstName
//        assert 'Ellermann' == anotherCreditMemo.person.lastName
//        assert d <= anotherCreditMemo.docDate
//        assert 1789.76 == anotherCreditMemo.total
//        assert null != anotherCreditMemo.invoice
//        assert 'Test invoice' == anotherCreditMemo.invoice.subject
//        assert null != anotherCreditMemo.dunning
//        assert 'Test dunning' == anotherCreditMemo.dunning.subject
//        assert null != anotherCreditMemo.items
//        assert 3 == anotherCreditMemo.items.size()
//    }
//
//    void testCreateFromInvoice() {
//        def d = new Date()
//        makeOrganizationFixture()
//        def org = Organization.get(1)
//        makePersonFixture(org)
//        def person = Person.get(1)
//        makeInvoiceFixture(org, person, d)
//        def invoice = Invoice.findByType('I')
//
//        def creditMemo = new CreditMemo(invoice)
//        assert '' == creditMemo.subject
//        assert null != creditMemo.organization
//        assert 'AMC World Technologies GmbH' == creditMemo.organization.name
//        assert null != creditMemo.person
//        assert 'Daniel' == creditMemo.person.firstName
//        assert 'Ellermann' == creditMemo.person.lastName
//        assert d <= creditMemo.docDate
//        assert invoice == creditMemo.invoice
//        assert null != creditMemo.items
//        assert 3 == creditMemo.items.size()
//    }
//
//    void testCreateFromDunning() {
//        def d = new Date()
//        makeOrganizationFixture()
//        def org = Organization.get(1)
//        makePersonFixture(org)
//        def person = Person.get(1)
//        makeInvoiceFixture(org, person, d)
//        def invoice = Invoice.findByType('I')
//        makeDunningFixture(org, person, invoice, d)
//        def dunning = Dunning.findByType('D')
//
//        def creditMemo = new CreditMemo(dunning)
//        assert '' == creditMemo.subject
//        assert null != creditMemo.organization
//        assert 'AMC World Technologies GmbH' == creditMemo.organization.name
//        assert null != creditMemo.person
//        assert 'Daniel' == creditMemo.person.firstName
//        assert 'Ellermann' == creditMemo.person.lastName
//        assert d <= creditMemo.docDate
//        assert dunning == creditMemo.dunning
//        assert null != creditMemo.items
//        assert 3 == creditMemo.items.size()
//    }

    void testConstraints() {
        def d = new Date()
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        def creditMemo = new CreditMemo(number: 20000, subject: 'Test credit memo', organization: org, person: person, docDate: d, total: 1789.76)
        assert !creditMemo.validate()
        assert 'nullable' == creditMemo.errors['stage']

        creditMemo = new CreditMemo(number: 20000, subject: 'Test credit memo', organization: org, person: person, docDate: d, total: 1789.76, new CreditMemoStage(name: 'created'))
        assert creditMemo.validate()
    }


    //-- Non-public methods ---------------------

    protected void makeDunningFixture(Organization org, Person person, Invoice invoice, Date d = new Date()) {
        def level = new DunningLevel(name: '1st dunning')
        mockDomain(
            Dunning, [
                [number: 30000, subject: 'Test dunning', organization: org, person: person, docDate: d, total: 1789.76, level: level, stage: DunningStage.get(2200), dueDatePayment: d, invoice: invoice]
            ]
        )
        makeInvoicingItemsFixture(Dunning.findByType('D'))
    }

    protected void makeInvoiceFixture(Organization org, Person person, Date d = new Date()) {
        makeStageFixture()
        mockDomain(
            Invoice, [
                [number: 40000, subject: 'Test invoice', organization: org, person: person, docDate: d, total: 1789.76, stage: InvoiceStage.get(900), dueDatePayment: d]
            ]
        )
        makeInvoicingItemsFixture(Invoice.findByType('I'))
    }

    protected void makeInvoicingItemsFixture(InvoicingTransaction invoicingTransaction) {
        def list = [
            new InvoicingItem(number: 'S-10000', quantity: 5.0, unit: 'h', name: 'Test entry 1', unitPrice: 153.0, tax: 19.0, invoicingTransaction: invoicingTransaction),
            new InvoicingItem(number: 'S-10001', quantity: 1.0, unit: 'pc.', name: 'Test entry 2', unitPrice: 140.0, tax: 19.0, invoicingTransaction: invoicingTransaction),
            new InvoicingItem(number: 'P-20140', quantity: 1.0, unit: 'pc.', name: 'Test entry 3', unitPrice: 599.0, tax: 19.0, invoicingTransaction: invoicingTransaction)
        ]
        mockDomain(InvoicingItem, list)
        invoicingTransaction.items = list
    }

    protected void makeOrganizationFixture() {
        mockDomain(
            Organization, [
                [id: 1, number: 10000, recType: 1, name: 'AMC World Technologies GmbH', legalForm: 'GmbH', billingAddrStreet: 'Fischerinsel 1', billingAddrLocation: 'Berlin', shippingAddrStreet: 'Fischerinsel 1', shippingAddrLocation: 'Berlin']
            ]
        )
    }

    protected void makePersonFixture(Organization org) {
        mockDomain(
            Person, [
                [id: 1, number: 10000, organization: org, firstName: 'Daniel', lastName: 'Ellermann', phone: '123456789']
            ]
        )
    }

    protected void makeStageFixture() {

        /*
         * Implementation notes: currently, we cannot call something like this:
         *   new InvoiceStage(id: 900, name: 'created')
         * In this case the ID is null. We must set it afterwards otherwise it
         * is not stored.
         */
        def l = []
        def stage = new InvoiceStage(name: 'created')
        stage.id = 900
        l << stage
        stage = new InvoiceStage(name: 'credited')
        stage.id = 907
        l << stage
        mockDomain(InvoiceStage, l)

        l = []
        stage = new DunningStage(name: 'created')
        stage.id = 2200
        l << stage
        stage = new DunningStage(name: 'credited')
        stage.id = 2206
        l << stage
        mockDomain(DunningStage, l)
    }
}
