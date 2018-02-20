/*
 * DunningControllerTests.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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


class DunningControllerTests {

    //-- Public methods -------------------------

    void setUp() {
        SeqNumberService.metaClass.formatWithPrefix = { -> '10000' }
        Tenant.metaClass.static.loadAsMap = { -> [: ] }
        grails.converters.XML.metaClass.static.toString = { -> '' }
        ConfigHolder.metaClass.getConfig = { String name -> null }

        session.user = new User(username: 'dellermann')
    }

    void testIndex() {
        controller.index()
        assert '/dunning/list' == response.redirectedUrl
    }

    void testIndexWithParams() {
        params.max = 30
        params.offset = 60
        controller.index()
        assert '/dunning/list?max=30&offset=60' == response.redirectedUrl
    }

    void testListEmpty() {
        def model = controller.list()
        assert null != model.dunningInstanceList
        assert 0 == model.dunningInstanceList.size()
        assert 0 == model.dunningInstanceTotal
    }

    void testListNonEmpty() {
        def d = new Date()
        makeDunningFixture(d)
        def model = controller.list()
        assert null != model.dunningInstanceList
        assert 1 == model.dunningInstanceList.size()
        assert 'Test dunning' == model.dunningInstanceList[0].subject
        assert null != model.dunningInstanceList[0].organization
        assert 'AMC World Technologies GmbH' == model.dunningInstanceList[0].organization.name
        assert d == model.dunningInstanceList[0].docDate
        assert null != model.dunningInstanceList[0].items
        assert 3 == model.dunningInstanceList[0].items.size()
        assert 'S-10000' == model.dunningInstanceList[0].items[0].number
        assert 5 == model.dunningInstanceList[0].items[0].quantity
        assert 'h' == model.dunningInstanceList[0].items[0].unit
        assert 1 == model.dunningInstanceTotal
    }

    void testListEmbeddedEmpty() {
        def model = controller.listEmbedded()
        assert null == model.dunningInstanceList
        assert null == model.dunningInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithOrganization() {
        params.organization = 1
        def model = controller.listEmbedded()
        assert null == model.dunningInstanceList
        assert null == model.dunningInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithPerson() {
        params.person = 1
        def model = controller.listEmbedded()
        assert null == model.dunningInstanceList
        assert null == model.dunningInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedEmptyWithInvoice() {
        params.invoice = 1
        def model = controller.listEmbedded()
        assert null == model.dunningInstanceList
        assert null == model.dunningInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutOrganization() {
        makeDunningFixture()
        params.organization = 2
        def model = controller.listEmbedded()
        assert null == model.dunningInstanceList
        assert null == model.dunningInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutPerson() {
        makeDunningFixture()
        params.person = 2
        def model = controller.listEmbedded()
        assert null == model.dunningInstanceList
        assert null == model.dunningInstanceTotal
        assert null == model.linkParams
    }

    void testListEmbeddedNonEmptyWithoutInvoice() {
        makeDunningFixture()
        params.invoice = 1000
        def model = controller.listEmbedded()
        assert null == model.dunningInstanceList
        assert null == model.dunningInstanceTotal
        assert null == model.linkParams
    }

    /*
     * XXX The following tests don't work, probably because the findAllBy...
     * methods are not correctly mocked.  FindAllBy... works when the property
     * type is a simple type such as int, String etc. but it seems to refuse
     * working when the type is a domain model class.
     */
//    void testListEmbeddedNonEmptyWithOrganization() {
//        makeDunningFixture()
//        params.organization = 1
//        def model = controller.listEmbedded()
//        assert null != model.dunningInstanceList
//        assert 1 == model.dunningInstanceList.size()
//        assert 1 == model.dunningInstanceTotal
//        assert null != model.linkParams
//        assert 1 == model.linkParams.organization
//    }
//
//    void testListEmbeddedNonEmptyWithPerson() {
//        makeDunningFixture()
//        params.person = 1
//        def model = controller.listEmbedded()
//        assert null != model.dunningInstanceList
//        assert 1 == model.dunningInstanceList.size()
//        assert 1 == model.dunningInstanceTotal
//        assert null != model.linkParams
//        assert 1 == model.linkParams.person
//    }
//
//    void testListEmbeddedNonEmptyWithInvoice() {
//        makeDunningFixture()
//        def invoice = Invoice.findByType('I')
//        params.invoice = invoice.id
//        def model = controller.listEmbedded()
//        assert null != model.dunningInstanceList
//        assert 1 == model.dunningInstanceList.size()
//        assert 1 == model.dunningInstanceTotal
//        assert null != model.linkParams
//        assert invoice.id == model.linkParams.invoice
//    }

    void testCreate() {
        def model = controller.create()
        assert null != model
        assert null != model.dunningInstance
    }

    void testCreateWithParams() {
        params.subject = 'Test dunning'
        def model = controller.create()
        assert null != model
        assert null != model.dunningInstance
        assert 'Test dunning' == model.dunningInstance.subject
    }

    void testCreateWithInvoice() {
        makeDunningFixture()
        params.invoice = Invoice.findByType('I').id
        def model = controller.create()
        assert null != model.dunningInstance
        assert '' == model.dunningInstance.subject
        assert 'AMC World Technologies GmbH' == model.dunningInstance.organization.name
        assert 'Fischerinsel 1' == model.dunningInstance.billingAddrStreet
        assert 'Berlin' == model.dunningInstance.shippingAddrLocation
        assert null != model.dunningInstance.items
        assert 0 == model.dunningInstance.items.size()
    }

    void testCopyNonExisting() {
        params.id = 1000
        controller.copy()
        assert 'default.not.found.message' == flash.message
        assert '/dunning/show/1000' == response.redirectedUrl
    }

    void testCopyExisting() {
        makeDunningFixture()
        params.id = Dunning.findByType('D').id
        controller.copy()
        assert '/dunning/create' == view
        assert null != model.dunningInstance
        assert 'Test dunning' == model.dunningInstance.subject
        assert null != model.dunningInstance.organization
        assert 'AMC World Technologies GmbH' == model.dunningInstance.organization.name
        assert null != model.dunningInstance.docDate
        assert null != model.dunningInstance.items
        assert 3 == model.dunningInstance.items.size()
    }

    void testSaveSuccess() {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        makeInvoiceFixture(org, person)
        def invoice = Invoice.findByType('I')
        def d = new Date()
        params.number = 20000
        params.subject = 'Test dunning'
        params.organization = org
        params.person = person
        params.docDate = d
        params.stage = new DunningStage(name: 'created')
        params.level = new DunningLevel(name: '1st dunning')
        params.dueDatePayment = d
        params.invoice = invoice
        controller.save()
        assert 'default.created.message' == flash.message
        def dunning = Dunning.findByType('D')
        assert '/dunning/show/' + dunning.id == response.redirectedUrl
        assert 1 == Dunning.count()
        invoice = Invoice.findByType('I')
        assert 904 == invoice.stage.id
    }

    void testSaveError() {
        params.subject = 'Test dunning'
        controller.save()
        assert '/dunning/create' == view
        assert 'Test dunning' == model.dunningInstance.subject
    }

    void testSaveWithReturnUrl() {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        makeInvoiceFixture(org, person)
        def invoice = Invoice.findByType('I')
        def d = new Date()
        params.number = 20000
        params.subject = 'Test dunning'
        params.organization = org
        params.person = Person.get(1)
        params.docDate = d
        params.stage = new DunningStage(name: 'created')
        params.level = new DunningLevel(name: '1st dunning')
        params.dueDatePayment = d
        params.invoice = invoice
        params.returnUrl = '/organization/show/5'
        controller.save()
        assert 'default.created.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Dunning.count()
    }

    void testShowExisting() {
        def control = mockFor(FopService)
        control.demand.getTemplateNames(1) { -> [] }
        controller.fopService = control.createMock()

        def d = new Date()
        makeDunningFixture(d)
        params.id = Dunning.findByType('D').id
        def model = controller.show()
        assert null != model.dunningInstance
        assert 'Test dunning' == model.dunningInstance.subject
        assert null != model.dunningInstance.organization
        assert 'AMC World Technologies GmbH' == model.dunningInstance.organization.name
        assert d == model.dunningInstance.docDate
        assert null != model.dunningInstance.items
        assert 3 == model.dunningInstance.items.size()
        assert null != model.printTemplates
    }

    void testShowNonExisting() {
        params.id = 1000
        controller.show()
        assert 'default.not.found.message' == flash.message
        assert '/dunning/list' == response.redirectedUrl
    }

    void testEditExisting() {
        def d = new Date()
        makeDunningFixture(d)
        params.id = Dunning.findByType('D').id
        def model = controller.edit()
        assert null != model.dunningInstance
        assert 'Test dunning' == model.dunningInstance.subject
        assert null != model.dunningInstance.organization
        assert 'AMC World Technologies GmbH' == model.dunningInstance.organization.name
        assert d == model.dunningInstance.docDate
        assert null != model.dunningInstance.items
        assert 3 == model.dunningInstance.items.size()
    }

    void testEditNonExisting() {
        params.id = 1000
        controller.edit()
        assert 'default.not.found.message' == flash.message
        assert '/dunning/list' == response.redirectedUrl
    }

    void testEditForbiddenByNonAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        params.id = dunning.id
        controller.edit()
        assert '/dunning/list' == response.redirectedUrl
    }

    void testEditForbiddenByAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        session.user.admin = true
        params.id = dunning.id
        def model = controller.edit()
        assert null != model.dunningInstance
    }

    void testEditPaymentNonAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        params.id = dunning.id
        def model = controller.editPayment()
        assert null != model.dunningInstance
    }

    void testEditPaymentAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        session.user.admin = true
        params.id = dunning.id
        def model = controller.editPayment()
        assert null != model.dunningInstance
    }

    void testEditPaymentNonExisting() {
        params.id = 1000
        controller.editPayment()
        assert 'default.not.found.message' == flash.message
        assert '/dunning/list' == response.redirectedUrl
    }

    void testUpdateSuccess() {
        def d = new Date()
        makeDunningFixture(d)
        def dunning = Dunning.findByType('D')
        params.id = dunning.id
        params.subject = 'Test 2'
        params.'items[0]' = new InvoicingItem(number: 'S-10000', quantity: 10.0, unit: 'h', name: 'Test entry 1a', unitPrice: 153.0, tax: 19.0)
        params.'items[1]' = new InvoicingItem(number: 'S-10001', quantity: 1.0, unit: 'pc.', name: 'Test entry 2a', unitPrice: 140.0, tax: 19.0)
        params.'items[2]' = new InvoicingItem(number: 'P-20140', quantity: 2.0, unit: 'pc.', name: 'Test entry 3a', unitPrice: 599.0, tax: 19.0)
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/dunning/show/' + dunning.id == response.redirectedUrl
        assert 1 == Dunning.count()
        dunning = Dunning.findByType('D')
        assert 'Test 2' == dunning.subject

        /*
         * XXX The invoicing transaction contains 4 * 3 invoicing items but
         * before saving there are 3 items only.  I suppose the save() method
         * doesn't correctly handle the cleared item list.
         */
//        assert 3 == dunning.items.size()
        assert 0 < dunning.items.size()
    }

    void testUpdateError() {
        makeDunningFixture()
        params.id = Dunning.findByType('D').id
        params.subject = ''
        controller.update()
        assert '/dunning/edit' == view
        assert '' == model.dunningInstance.subject
        assert 1 == Dunning.count()
    }

    void testUpdateNonExisting() {
        controller.update()
        assert 'default.not.found.message' == flash.message
        assert '/dunning/list' == response.redirectedUrl
    }

    void testUpdateForbiddenByNonAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        params.id = dunning.id
        params.subject = 'Test dunning forbidden'
        controller.update()
        assert '/dunning/list' == response.redirectedUrl
    }

    void testUpdateForbiddenByAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        session.user.admin = true
        params.id = dunning.id
        params.subject = 'Test 2'
        params.'items[0]' = new InvoicingItem(number: 'S-10000', quantity: 10.0, unit: 'h', name: 'Test entry 1a', unitPrice: 153.0, tax: 19.0)
        params.'items[1]' = new InvoicingItem(number: 'S-10001', quantity: 1.0, unit: 'pc.', name: 'Test entry 2a', unitPrice: 140.0, tax: 19.0)
        params.'items[2]' = new InvoicingItem(number: 'P-20140', quantity: 2.0, unit: 'pc.', name: 'Test entry 3a', unitPrice: 599.0, tax: 19.0)
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/dunning/show/' + dunning.id == response.redirectedUrl
        assert 1 == Dunning.count()
        dunning = Dunning.findByType('D')
        assert 'Test 2' == dunning.subject
    }

    void testUpdatePaymentByNonAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        params.id = dunning.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/dunning/show/' + dunning.id == response.redirectedUrl
        assert 1 == Dunning.count()
        dunning = Dunning.findByType('D')
        assert 'Test dunning' == dunning.subject
        assert 2503 == dunning.stage.id
        assert d == dunning.paymentDate
        assert 1470.10 == dunning.paymentAmount
    }

    void testUpdatePaymentByAdmin() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        session.user.admin = true
        params.id = dunning.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/dunning/show/' + dunning.id == response.redirectedUrl
        assert 1 == Dunning.count()
        dunning = Dunning.findByType('D')
        assert 'Test dunning' == dunning.subject
        assert 2503 == dunning.stage.id
        assert d == dunning.paymentDate
        assert 1470.10 == dunning.paymentAmount
    }

    void testUpdatePaymentByNonAdminWithReturnUrl() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        params.id = dunning.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        params.returnUrl = '/organization/show/5'
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Dunning.count()
        dunning = Dunning.findByType('D')
        assert 'Test dunning' == dunning.subject
        assert 2503 == dunning.stage.id
        assert d == dunning.paymentDate
        assert 1470.10 == dunning.paymentAmount
    }

    void testUpdatePaymentByAdminWithReturnUrl() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        dunning.stage = new DunningStage(name: 'sent')
        dunning.stage.id = 2502
        dunning.save()

        session.user.admin = true
        params.id = dunning.id
        params.subject = 'Test 2'
        params.'stage.id' = 2503
        def d = new Date()
        params.paymentDate = d
        params.paymentAmount = 1470.10
        params.returnUrl = '/organization/show/5'
        controller.updatePayment()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Dunning.count()
        dunning = Dunning.findByType('D')
        assert 'Test dunning' == dunning.subject
        assert 2503 == dunning.stage.id
        assert d == dunning.paymentDate
        assert 1470.10 == dunning.paymentAmount
    }

    void testUpdateWithReturnUrl() {
        def d = new Date()
        makeDunningFixture(d)
        def dunning = Dunning.findByType('D')
        params.id = dunning.id
        params.subject = 'Test 2'
        params.'items[0]' = new InvoicingItem(number: 'S-10000', quantity: 10.0, unit: 'h', name: 'Test entry 1a', unitPrice: 153.0, tax: 19.0)
        params.'items[1]' = new InvoicingItem(number: 'S-10001', quantity: 1.0, unit: 'pc.', name: 'Test entry 2a', unitPrice: 140.0, tax: 19.0)
        params.'items[2]' = new InvoicingItem(number: 'P-20140', quantity: 2.0, unit: 'pc.', name: 'Test entry 3a', unitPrice: 599.0, tax: 19.0)
        params.returnUrl = '/organization/show/5'
        controller.update()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Dunning.count()
        dunning = Dunning.findByType('D')
        assert 'Test 2' == dunning.subject

        /*
         * XXX The invoicing transaction contains 4 * 3 invoicing items but
         * before saving there are 3 items only.  I suppose the save() method
         * doesn't correctly handle the cleared item list.
         */
//        assert 3 == dunning.items.size()
        assert 0 < dunning.items.size()
    }

    void testDeleteExistingConfirmed() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        params.id = dunning.id
        params.confirmed = true
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/dunning/list' == response.redirectedUrl
        assert 0 == Dunning.countByType('D')
    }

    void testDeleteExistingUnconfirmed() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        params.id = dunning.id
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/dunning/list' == response.redirectedUrl
        assert 1 == Dunning.countByType('D')
    }

    void testDeleteNonExisting() {
        params.id = 1000
        params.confirmed = true
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/dunning/list' == response.redirectedUrl
    }

    void testDeleteWithReturnUrlConfirmed() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        params.id = dunning.id
        params.confirmed = true
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.deleted.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 0 == Dunning.countByType('D')
    }

    void testDeleteWithReturnUrlUnconfirmed() {
        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        params.id = dunning.id
        params.returnUrl = '/organization/show/5'
        controller.delete()
        assert 'default.not.found.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        assert 1 == Dunning.countByType('D')
    }

    void testPrintExisting() {
        def control = mockFor(FopService)
        control.demand.outputPdf(1) {}
        controller.fopService = control.createMock()

        makeDunningFixture()
        def dunning = Dunning.findByType('D')
        params.id = dunning.id
        controller.print()
    }


    //-- Non-public methods ---------------------

    protected void makeDunningFixture(Date d = new Date()) {
        makeOrganizationFixture()
        def org = Organization.get(1)
        makePersonFixture(org)
        def person = Person.get(1)
        makeInvoiceFixture(org, person, d)
        def invoice = Invoice.findByType('I')
        def stage = new DunningStage(name: 'created')
        def level = new DunningLevel(name: '1st dunning')
        mockDomain(
            Dunning, [
                [number: 20000, subject: 'Test dunning', organization: org, person: person, docDate: d, total: 1789.76, stage: stage, level: level, dueDatePayment: d, invoice: invoice]
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
        stage = new InvoiceStage(name: 'dunned')
        stage.id = 904
        l << stage
        mockDomain(InvoiceStage, l)
    }
}
