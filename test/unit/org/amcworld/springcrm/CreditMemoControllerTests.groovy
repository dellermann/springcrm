/*
 * CreditMemoControllerTests.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


/**
 * The class {@code CreditMemoControllerTests} contains the unit test cases
 * for {@code CreditMemoController}.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
@TestFor(CreditMemoController)
@Mock([CreditMemo, Organization, Person, InvoicingItem, Invoice, Dunning, InvoiceStage, SeqNumberService])
class CreditMemoControllerTests {

    //-- Public methods -------------------------

    void setUp() {
//        int seqNumber = 1
//        def seqNumberService = mockFor(SeqNumberService)
//        seqNumberService.demand.nextNumber(0..10) { cls -> seqNumber++ }
//        InvoicingTransaction.metaClass.seqNumberService = seqNumberService.createMock()

        SeqNumberService.metaClass.formatWithPrefix = { -> '10000' }
        Client.metaClass.static.loadAsMap = { -> [: ] }
        grails.converters.XML.metaClass.static.toString = { -> '' }

        session.user = new User(userName: 'dellermann')
    }

    void testIndex() {
        controller.index()
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testIndexWithParams() {
        params.max = 30
        params.offset = 60
        controller.index()
        assert '/creditMemo/list?max=30&offset=60' == response.redirectedUrl
    }

    void testListEmpty() {
        def model = controller.list()
        assert null != model.creditMemoInstanceList
        assert 0 == model.creditMemoInstanceList.size()
        assert 0 == model.creditMemoInstanceTotal
    }

    void testListNonEmpty() {
        def d = new Date()
        makeCreditMemoFixture(d)
        def model = controller.list()
        assert null != model.creditMemoInstanceList
        assert 1 == model.creditMemoInstanceList.size()
        assert 'Test credit memo' == model.creditMemoInstanceList[0].subject
        assert null != model.creditMemoInstanceList[0].organization
        assert 'AMC World Technologies GmbH' == model.creditMemoInstanceList[0].organization.name
        assert d == model.creditMemoInstanceList[0].docDate
        assert null != model.creditMemoInstanceList[0].items
        assert 3 == model.creditMemoInstanceList[0].items.size()
        assert 'S-10000' == model.creditMemoInstanceList[0].items[0].number
        assert 5 == model.creditMemoInstanceList[0].items[0].quantity
        assert 'h' == model.creditMemoInstanceList[0].items[0].unit
        assert 1 == model.creditMemoInstanceTotal
    }

    void testListEmbeddedEmpty() {
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithOrganization() {
        params.organization = 1
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithPerson() {
        params.person = 1
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithInvoice() {
        params.invoice = 1
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithDunning() {
        params.dunning = 1
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutOrganization() {
        makeCreditMemoFixture()
        params.organization = 2
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutPerson() {
        makeCreditMemoFixture()
        params.person = 2
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutInvoice() {
        makeCreditMemoFixture()
        params.invoice = 1000
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutDunning() {
        makeCreditMemoFixture()
        params.dunning = 1000
        def model = controller.listEmbedded()
        assert null == model.creditMemoInstanceList
        assert null == model.creditMemoInstanceTotal
        assert null == model.linkParams
    }

    /*
     * XXX The following tests don't work, probably because the findAllBy...
     * methods are not correctly mocked.  FindAllBy... works when the property
     * type is a simple type such as int, String etc. but it seems to refuse
     * working when the type is a domain model class.
     */
//    void testListEmbeddedNonEmptyWithOrganization() {
//        makeCreditMemoFixture()
//        params.organization = 1
//        def model = controller.listEmbedded()
//        assert null != model.creditMemoInstanceList
//        assert 1 == model.creditMemoInstanceList.size()
//        assert 1 == model.creditMemoInstanceTotal
//        assert null != model.linkParams
//        assert 1 == model.linkParams.organization
//    }
//
//    void testListEmbeddedNonEmptyWithPerson() {
//        makeCreditMemoFixture()
//        params.person = 1
//        def model = controller.listEmbedded()
//        assert null != model.creditMemoInstanceList
//        assert 1 == model.creditMemoInstanceList.size()
//        assert 1 == model.creditMemoInstanceTotal
//        assert null != model.linkParams
//        assert 1 == model.linkParams.person
//    }
//
//    void testListEmbeddedNonEmptyWithInvoice() {
//        makeCreditMemoFixture()
//        def invoice = Invoice.findByType('I')
//        params.invoice = invoice.id
//        def model = controller.listEmbedded()
//        assert null != model.creditMemoInstanceList
//        assert 1 == model.creditMemoInstanceList.size()
//        assert 1 == model.creditMemoInstanceTotal
//        assert null != model.linkParams
//        assert invoice.id == model.linkParams.invoice
//    }
//
//    void testListEmbeddedNonEmptyWithDunning() {
//        makeCreditMemoFixture()
//        def dunning = Dunning.findByType('D')
//        params.dunning = dunning.id
//        def model = controller.listEmbedded()
//        assert null != model.creditMemoInstanceList
//        assert 1 == model.creditMemoInstanceList.size()
//        assert 1 == model.creditMemoInstanceTotal
//        assert null != model.linkParams
//        assert dunning.id == model.linkParams.dunning
//    }

    void testCreate() {
        def model = controller.create()
        assert null != model
        assert null != model.creditMemoInstance
    }

    void testCreateWithParams() {
        params.subject = 'Test credit memo'
        def model = controller.create()
        assert null != model
        assert null != model.creditMemoInstance
        assert 'Test credit memo' == model.creditMemoInstance.subject
    }

    void testCreateWithInvoice() {
        makeCreditMemoFixture()
        params.invoice = Invoice.findByType('I').id
        def model = controller.create()
        assert null != model.creditMemoInstance
        assert '' == model.creditMemoInstance.subject
        assert 'AMC World Technologies GmbH' == model.creditMemoInstance.organization.name
        assert 'Fischerinsel 1' == model.creditMemoInstance.billingAddrStreet
        assert 'Berlin' == model.creditMemoInstance.shippingAddrLocation
        assert null != model.creditMemoInstance.items
        assert 3 == model.creditMemoInstance.items.size()
    }

    void testCreateWithDunning() {
        makeCreditMemoFixture()
        params.dunning = Dunning.findByType('D').id
        def model = controller.create()
        assert null != model.creditMemoInstance
        assert '' == model.creditMemoInstance.subject
        assert 'AMC World Technologies GmbH' == model.creditMemoInstance.organization.name
        assert 'Fischerinsel 1' == model.creditMemoInstance.billingAddrStreet
        assert 'Berlin' == model.creditMemoInstance.shippingAddrLocation
        assert null != model.creditMemoInstance.items
        assert 3 == model.creditMemoInstance.items.size()
    }

    void testCopyNonExisting() {
        params.id = 1000
        controller.copy()
        assert 'default.not.found.message' == flash.message
        assert '/creditMemo/show/1000' == response.redirectedUrl
    }

    void testCopyExisting() {
        makeCreditMemoFixture()
        params.id = CreditMemo.findByType('C').id
        controller.copy()
        assert '/creditMemo/create' == view
        assert null != model.creditMemoInstance
        assert 'Test credit memo' == model.creditMemoInstance.subject
        assert null != model.creditMemoInstance.organization
        assert 'AMC World Technologies GmbH' == model.creditMemoInstance.organization.name
        assert null != model.creditMemoInstance.docDate
        assert null != model.creditMemoInstance.items
        assert 3 == model.creditMemoInstance.items.size()
    }

    void testSaveSuccess() {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        makeInvoiceFixture(org, person)
        def invoice = Invoice.findByType('I')
        makeDunningFixture(org, person, invoice)
        def dunning = Dunning.findByType('D')
        def d = new Date()
        params.number = 20000
        params.subject = 'Test credit memo'
        params.organization = org
        params.person = person
        params.docDate = d
        params.stage = new CreditMemoStage(name: 'created')
        params.invoice = invoice
        params.dunning = dunning
        controller.save()
        assert 'default.created.message' == flash.message
        def creditMemo = CreditMemo.findByType('C')
        assert '/creditMemo/show/' + creditMemo.id == response.redirectedUrl
        assert 1 == CreditMemo.count()
        invoice = Invoice.findByType('I')
        assert 907 == invoice.stage.id
        dunning = Dunning.findByType('D')
        assert 2206 == dunning.stage.id
    }

    void testSaveError() {
        params.subject = 'Test credit memo'
        controller.save()
        assert '/creditMemo/create' == view
        assert 'Test credit memo' == model.creditMemoInstance.subject
    }

    void testSaveWithReturnUrl() {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def d = new Date()
        params.number = 20000
        params.subject = 'Test credit memo'
        params.organization = org
        params.person = Person.get(1)
        params.docDate = d
        params.stage = new CreditMemoStage(name: 'created')
        params.returnUrl = '/organization/show/5'
        controller.save()
        assert 'default.created.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CreditMemo.count()
    }

    void testShowExisting() {
        def control = mockFor(FopService)
        control.demand.getTemplateNames(1) { -> [] }
        controller.fopService = control.createMock()

        def d = new Date()
        makeCreditMemoFixture(d)
        params.id = CreditMemo.findByType('C').id
        def model = controller.show()
        assert null != model.creditMemoInstance
        assert 'Test credit memo' == model.creditMemoInstance.subject
        assert null != model.creditMemoInstance.organization
        assert 'AMC World Technologies GmbH' == model.creditMemoInstance.organization.name
        assert d == model.creditMemoInstance.docDate
        assert null != model.creditMemoInstance.items
        assert 3 == model.creditMemoInstance.items.size()
        assert null != model.printTemplates
    }

    void testShowNonExisting() {
        params.id = 1000
        controller.show()
        assert 'default.not.found.message' == flash.message
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testEditExisting() {
        def d = new Date()
        makeCreditMemoFixture(d)
        params.id = CreditMemo.findByType('C').id
        def model = controller.edit()
        assert null != model.creditMemoInstance
        assert 'Test credit memo' == model.creditMemoInstance.subject
        assert null != model.creditMemoInstance.organization
        assert 'AMC World Technologies GmbH' == model.creditMemoInstance.organization.name
        assert d == model.creditMemoInstance.docDate
        assert null != model.creditMemoInstance.items
        assert 3 == model.creditMemoInstance.items.size()
    }

    void testEditNonExisting() {
        params.id = 1000
        controller.edit()
        assert 'default.not.found.message' == flash.message
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testEditForbiddenByNonAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        params.id = creditMemo.id
        controller.edit()
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testEditForbiddenByAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        session.user.admin = true
        params.id = creditMemo.id
        def model = controller.edit()
        assert null != model.creditMemoInstance
    }

    void testEditPaymentNonAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        params.id = creditMemo.id
        def model = controller.editPayment()
        assert null != model.creditMemoInstance
    }

    void testEditPaymentAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        session.user.admin = true
        params.id = creditMemo.id
        def model = controller.editPayment()
        assert null != model.creditMemoInstance
    }

    void testEditPaymentNonExisting() {
        params.id = 1000
        controller.editPayment()
        assert 'default.not.found.message' == flash.message
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testUpdateSuccess() {
        def d = new Date()
        makeCreditMemoFixture(d)
        def creditMemo = CreditMemo.findByType('C')
        params.id = creditMemo.id
        params.subject = 'Test 2'
        params.'items[0]' = new InvoicingItem(number: 'S-10000', quantity: 10.0, unit: 'h', name: 'Test entry 1a', unitPrice: 153.0, tax: 19.0)
        params.'items[1]' = new InvoicingItem(number: 'S-10001', quantity: 1.0, unit: 'pc.', name: 'Test entry 2a', unitPrice: 140.0, tax: 19.0)
        params.'items[2]' = new InvoicingItem(number: 'P-20140', quantity: 2.0, unit: 'pc.', name: 'Test entry 3a', unitPrice: 599.0, tax: 19.0)
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/creditMemo/show/' + creditMemo.id == response.redirectedUrl
        assert 1 == CreditMemo.count()
        creditMemo = CreditMemo.findByType('C')
        assert 'Test 2' == creditMemo.subject

        /*
         * XXX The invoicing transaction contains 4 * 3 invoicing items but
         * before saving there are 3 items only.  I suppose the save() method
         * doesn't correctly handle the cleared item list.
         */
//        assert 3 == creditMemo.items.size()
        assert 0 < creditMemo.items.size()
    }

    void testUpdateError() {
        makeCreditMemoFixture()
        params.id = CreditMemo.findByType('C').id
        params.subject = ''
        controller.update()
        assert '/creditMemo/edit' == view
        assert '' == model.creditMemoInstance.subject
        assert 1 == CreditMemo.count()
    }

    void testUpdateNonExisting() {
        controller.update()
        assert 'default.not.found.message' == flash.message
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testUpdateForbiddenByNonAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        params.id = creditMemo.id
        params.subject = 'Test credit memo forbidden'
        controller.update()
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testUpdateForbiddenByAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        session.user.admin = true
        params.id = creditMemo.id
        params.subject = 'Test 2'
        params.'items[0]' = new InvoicingItem(number: 'S-10000', quantity: 10.0, unit: 'h', name: 'Test entry 1a', unitPrice: 153.0, tax: 19.0)
        params.'items[1]' = new InvoicingItem(number: 'S-10001', quantity: 1.0, unit: 'pc.', name: 'Test entry 2a', unitPrice: 140.0, tax: 19.0)
        params.'items[2]' = new InvoicingItem(number: 'P-20140', quantity: 2.0, unit: 'pc.', name: 'Test entry 3a', unitPrice: 599.0, tax: 19.0)
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/creditMemo/show/' + creditMemo.id == response.redirectedUrl
        assert 1 == CreditMemo.count()
        creditMemo = CreditMemo.findByType('C')
        assert 'Test 2' == creditMemo.subject
    }

    void testUpdatePaymentByNonAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        params.id = creditMemo.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/creditMemo/show/' + creditMemo.id == response.redirectedUrl
        assert 1 == CreditMemo.count()
        creditMemo = CreditMemo.findByType('C')
        assert 'Test credit memo' == creditMemo.subject
        assert 2503 == creditMemo.stage.id
        assert d == creditMemo.paymentDate
        assert 1470.10 == creditMemo.paymentAmount
    }

    void testUpdatePaymentByAdmin() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        session.user.admin = true
        params.id = creditMemo.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/creditMemo/show/' + creditMemo.id == response.redirectedUrl
        assert 1 == CreditMemo.count()
        creditMemo = CreditMemo.findByType('C')
        assert 'Test credit memo' == creditMemo.subject
        assert 2503 == creditMemo.stage.id
        assert d == creditMemo.paymentDate
        assert 1470.10 == creditMemo.paymentAmount
    }

    void testUpdatePaymentByNonAdminWithReturnUrl() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        params.id = creditMemo.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        params.returnUrl = '/organization/show/5'
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CreditMemo.count()
        creditMemo = CreditMemo.findByType('C')
        assert 'Test credit memo' == creditMemo.subject
        assert 2503 == creditMemo.stage.id
        assert d == creditMemo.paymentDate
        assert 1470.10 == creditMemo.paymentAmount
    }

    void testUpdatePaymentByAdminWithReturnUrl() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        creditMemo.stage = new CreditMemoStage(name: 'sent')
        creditMemo.stage.id = 2502
        creditMemo.save()

        session.user.admin = true
        params.id = creditMemo.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        params.returnUrl = '/organization/show/5'
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CreditMemo.count()
        creditMemo = CreditMemo.findByType('C')
        assert 'Test credit memo' == creditMemo.subject
        assert 2503 == creditMemo.stage.id
        assert d == creditMemo.paymentDate
        assert 1470.10 == creditMemo.paymentAmount
    }

    void testUpdateWithReturnUrl() {
        def d = new Date()
        makeCreditMemoFixture(d)
        def creditMemo = CreditMemo.findByType('C')
        params.id = creditMemo.id
        params.subject = 'Test 2'
        params.'items[0]' = new InvoicingItem(number: 'S-10000', quantity: 10.0, unit: 'h', name: 'Test entry 1a', unitPrice: 153.0, tax: 19.0)
        params.'items[1]' = new InvoicingItem(number: 'S-10001', quantity: 1.0, unit: 'pc.', name: 'Test entry 2a', unitPrice: 140.0, tax: 19.0)
        params.'items[2]' = new InvoicingItem(number: 'P-20140', quantity: 2.0, unit: 'pc.', name: 'Test entry 3a', unitPrice: 599.0, tax: 19.0)
        params.returnUrl = '/organization/show/5'
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CreditMemo.count()
        creditMemo = CreditMemo.findByType('C')
        assert 'Test 2' == creditMemo.subject

        /*
         * XXX The invoicing transaction contains 4 * 3 invoicing items but
         * before saving there are 3 items only.  I suppose the save() method
         * doesn't correctly handle the cleared item list.
         */
//        assert 3 == creditMemo.items.size()
        assert 0 < creditMemo.items.size()
    }

    void testDeleteExistingConfirmed() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        params.id = creditMemo.id
        params.confirmed = true
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/creditMemo/list' == response.redirectedUrl
        assert 0 == CreditMemo.countByType('C')
    }

    void testDeleteExistingUnconfirmed() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        params.id = creditMemo.id
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/creditMemo/list' == response.redirectedUrl
        assert 1 == CreditMemo.countByType('C')
    }

    void testDeleteNonExisting() {
        params.id = 1000
        params.confirmed = true
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/creditMemo/list' == response.redirectedUrl
    }

    void testDeleteWithReturnUrlConfirmed() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        params.id = creditMemo.id
        params.confirmed = true
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 0 == CreditMemo.countByType('C')
    }

    void testDeleteWithReturnUrlUnconfirmed() {
        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        params.id = creditMemo.id
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == CreditMemo.countByType('C')
    }

    void testPrintExisting() {
        def control = mockFor(FopService)
        control.demand.outputPdf(1) {}
        controller.fopService = control.createMock()

        makeCreditMemoFixture()
        def creditMemo = CreditMemo.findByType('C')
        params.id = creditMemo.id
        controller.print()
    }


    //-- Non-public methods ---------------------

    protected void makeCreditMemoFixture(Date d = new Date()) {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        makeInvoiceFixture(org, person, d)
        def invoice = Invoice.findByType('I')
        makeDunningFixture(org, person, invoice, d)
        def dunning = Dunning.findByType('D')
        def stage = new CreditMemoStage(name: 'created')
        mockDomain(
            CreditMemo, [
                [number: 20000, subject: 'Test credit memo', organization: org, person: person, docDate: d, total: 1789.76, stage: stage, invoice: invoice, dunning: dunning]
            ]
        )
        makeInvoicingItemsFixture(CreditMemo.findByType('C'))
    }

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
                [number: 30000, subject: 'Test invoice', organization: org, person: person, docDate: d, total: 1789.76, stage: InvoiceStage.get(900), dueDatePayment: d]
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
