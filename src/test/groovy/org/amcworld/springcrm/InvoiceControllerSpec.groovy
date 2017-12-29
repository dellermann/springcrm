/*
 * InvoiceControllerSpec.groovy
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

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification


class InvoiceControllerSpec extends Specification
    implements ControllerUnitTest<InvoiceController>
{

    //-- Feature methods ------------------------

    def 'Index action without parameters'() {
        when:
        def model = controller.index()

        then:
        matchEmptyList model
    }

    def 'List unpaid bills'() {
        given: 'some invoices'
        makeInvoicesFixture()

        when: 'I call the listUnpaidBills action'
        def model = controller.listUnpaidBills()
        def l = model.invoiceInstanceList

        then: 'I get two unpaid invoice'
        null != l
        2 == l.size()

        and: 'the first invoice has the following properties'
        def i1 = l.find { it.number == 40000 }
        null != i1
        107.0762 == i1.total
        -107.08 == i1.balance

        and: 'the second invoice has the following properties'
        def i2 = l.find { it.number == 40001 }
        null != i2
        9.99915 == i2.total
        -0.50 == i2.balance
    }

    // TODO test the remaining actions


    //-- Non-public methods ---------------------

    protected void makeInvoicesFixture(Organization org = null,
                                       Person p = null)
    {
        makeInvoiceStageFixture()
        if (!org) {
            makeOrganizationFixture()
            org = Organization.get(1)
        }
        if (!p) {
            makePersonFixture()
            p = Person.get(1)
        }
        def d = new Date() - 5      // 5 days before today

        UserService userService = Mock()
        userService.numFractionDigitsExt >> 2

        def i1 = new Invoice(
            billingAddr: new Address(),
            dueDatePayment: d,
            footerText: 'my footer text',
            headerText: 'my header text',
            items: [],
            number: 39999,
            organization: org,
            paymentAmount: 214.1524,
            person: p,
            shippingAddr: new Address(),
            stage: InvoiceStage.get(903),
            subject: 'Test invoice 1',
            termsAndConditions: [],
            total: 214.1524
        )
        i1.userService = userService
        i1.items << new InvoicingItem(
            quantity: 4, unit: 'pcs.', name: 'books',
            unitPrice: 44.99, tax: 19
        )
        def i2 = new Invoice(
            billingAddr: new Address(),
            dueDatePayment: d,
            footerText: 'my footer text',
            headerText: 'my header text',
            items: [],
            number: 40000,
            organization: org,
            person: p,
            shippingAddr: new Address(),
            stage: InvoiceStage.get(902),
            subject: 'Test invoice 2',
            termsAndConditions: [],
            total: 107.0762
        )
        i2.userService = userService
        i2.items << new InvoicingItem(
            quantity: 2, unit: 'pcs.', name: 'books',
            unitPrice: 44.99, tax: 19
        )

        def i3 = new Invoice(
            billingAddr: new Address(),
            dueDatePayment: d,
            footerText: 'my footer text',
            headerText: 'my header text',
            items: [],
            number: 40001,
            organization: org,
            paymentAmount: 9.5,
            person: p,
            shippingAddr: new Address(),
            stage: InvoiceStage.get(903),
            subject: 'Test invoice 3',
            termsAndConditions: [],
            total: 9.99915
        )
        i3.userService = userService
        i3.items << new InvoicingItem(
            quantity: 10.5, unit: 'm', name: 'tape',
            unitPrice: 0.89, tax: 7
        )

        mockDomain Invoice, [i1, i2, i3]
    }

    protected void makeInvoiceStageFixture() {
        def names = ['sent', 'paid', 'reminded', 'collection']
        for (int i = 0; i < names.size(); i++) {
            def is = new InvoiceStage(name: names.get(i))
            is.id = 902 + i     // setting the ID in constructor doesn't work
            is.save failOnError: true
        }
    }

    protected void makeOrganizationFixture() {
        mockDomain Organization, [
            [
                id: 1, number: 10000, recType: 1,
                name: 'AMC World Technologies GmbH', legalForm: 'GmbH',
                billingAddr: new Address(), shippingAddr: new Address(),
                phone: '987654321'
            ]
        ]
    }

    protected void makePersonFixture(Organization org) {
        mockDomain Person, [
            [
                id: 1, number: 10000, organization: org, firstName: 'Daniel',
                lastName: 'Ellermann', mailingAddr: new Address(),
                otherAddr: new Address(), phone: '123456789'
            ]
        ]
    }

    protected void matchEmptyList(Map model) {
        assert null != model.invoiceInstanceList
        assert 0 == model.invoiceInstanceList.size()
        assert 0 == model.invoiceInstanceTotal
    }


//  private static final String ERROR_MSG = 'error message'
//
//    protected void setUp() {
//        super.setUp()
//
//    controller.metaClass.message = { Map map -> return ERROR_MSG }
//    def ts = mockFor(TransactionStatus, true)
//    ts.demand.setRollbackOnly { -> }
//    Invoice.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }
//
//    def q1 = new Invoice(number:10000, subject:'Invoice 1')
//    def q2 = new Invoice(number:10001, subject:'Invoice 2')
//    mockDomain(Invoice, [q1, q2])
//
////    def seqNumber = new SeqNumber(controllerName:'invoice', nextNumber:10002, prefix:'I', suffix:'')
////    mockDomain(SeqNumber, [seqNumber])
//
//    controller.seqNumberService = new SeqNumberService()
//    }
//
//    protected void tearDown() {
//        super.tearDown()
//    }
//
//  void testList() {
//    def map = controller.list()
//    assertEquals 2, map.invoiceInstanceTotal
//    assertEquals 2, map.invoiceInstanceList.size()
//    assertEquals 'Invoice 1', map.invoiceInstanceList[0].subject
//    assertEquals 'Invoice 2', map.invoiceInstanceList[1].subject
//  }
//
//  void testCreate() {
//    def map = controller.create()
//    assertNotNull map.invoiceInstance
//    assertNull map.invoiceInstance.subject
//  }
//
//  void testSaveSuccessfully() {
//    controller.params.number = 10010
//    controller.params.subject = 'Invoice 3'
//    controller.params.organization = new Organization()
//    controller.params.docDate = new Date()
//    controller.params.dueDatePayment = new Date()
//    controller.params.headerText = 'Test'
//    controller.params.stage = new InvoiceStage()
//    controller.save()
//    assertEquals 3, Invoice.count()
//    assertEquals 'show', controller.redirectArgs['action']
//    SeqNumber seqNumber = SeqNumber.findByControllerName('invoice')
//    assertEquals 10003, seqNumber.nextNumber
//  }
//
//  void testSaveFailed() {
//    controller.params.number = 10001
//    controller.params.subject = ''
//    controller.params.organization = new Organization()
//    controller.params.docDate = new Date()
//    controller.params.headerText = 'Test'
//    controller.params.stage = new InvoiceStage()
//    def map = controller.save()
//    assertEquals 2, Invoice.count()
//    assertEquals 'unique', map.invoiceInstance.errors['number']
//    assertEquals 'blank', map.invoiceInstance.errors['subject']
//  }
//
//  void testShow() {
//    controller.params.id = 2
//    def map = controller.show()
//    assertEquals 'Invoice 2', map.invoiceInstance.subject
//
//    controller.params.id = 10
//    controller.show()
//    assertEquals 'list', controller.redirectArgs['action']
//    assertEquals ERROR_MSG, controller.flash['message']
//  }
//
//  void testEdit() {
//    controller.params.id = 1
//    def map = controller.edit()
//    assertEquals 'Invoice 1', map.invoiceInstance.subject
//
//    controller.params.id = 10
//    controller.edit()
//    assertEquals 'list', controller.redirectArgs['action']
//    assertEquals ERROR_MSG, controller.flash['message']
//  }
//
//  void testUpdate() {
//    controller.params.id = 1
//    controller.params.number = 10000
//    controller.params.subject = 'Invoice 3'
//    controller.params.organization = new Organization()
//    controller.params.docDate = new Date()
//    controller.params.dueDatePayment = new Date()
//    controller.params.headerText = 'Test'
//    controller.params.stage = new InvoiceStage()
//    controller.update()
//    assertEquals 'show', controller.redirectArgs['action']
//    assertEquals 2, Invoice.count()
//    SeqNumber seqNumber = SeqNumber.findByControllerName('invoice')
//    assertEquals 10002, seqNumber.nextNumber
//    def invoice = Invoice.get(1)
//    assertEquals 10000, invoice.number
//    assertEquals 'Invoice 3', invoice.subject
//    assertEquals 'Test', invoice.headerText
//
//    controller.params.subject = ''
//    def map = controller.update()
//    assertEquals 2, Invoice.count()
//    assertEquals 'blank', map.invoiceInstance.errors['subject']
//
//    controller.params.id = 10
//    controller.update()
//    assertEquals 'list', controller.redirectArgs['action']
//    assertEquals ERROR_MSG, controller.flash['message']
//  }
//
//  void testDelete() {
//    controller.params.id = 1
//    controller.delete()
//    assertEquals 1, Invoice.count()
//    assertNull Invoice.get(1)
//    assertNotNull Invoice.get(2)
//
//    controller.params.id = 10
//    controller.delete()
//    assertEquals 'list', controller.redirectArgs['action']
//    assertEquals ERROR_MSG, controller.flash['message']
//  }
}
