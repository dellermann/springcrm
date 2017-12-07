/*
 * CreditMemoControllerSpec.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import javax.servlet.http.HttpServletResponse
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.IgnoreRest
import spock.lang.Specification


@TestFor(CreditMemoController)
@Mock([CreditMemo, InvoicingItem, User])
class CreditMemoControllerSpec extends Specification {

    //-- Fields -------------------------------------

    CreditMemoService creditMemoService


    //-- Fixture methods ------------------------

    def setup() {
        creditMemoService = Mock(CreditMemoService)
        controller.creditMemoService = creditMemoService
    }


    //-- Feature methods ------------------------

    def 'Copy shows a copy in edit view'() {
        when: 'I call the action method'
        controller.copy(5L)

        then: 'the service method is called'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L)

        and: 'the create view is rendered'
        '/creditMemo/create' == view

        and: 'the copied instance is in the model'
        verifyCreditMemo model.creditMemoInstance
    }

    def 'Copy non-existing instance redirect to index view'() {
        when: 'I call the action method'
        controller.copy(5L)

        then: 'the service method is called'
        1 * creditMemoService.get(5L) >> null

        and: 'the user is redirected to index view'
        '/creditMemo/index' == response.redirectedUrl

        and: 'a message in the flash scope is set'
        'default.not.found.message' == flash.message
    }

    def 'Create without presets'() {
        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains an empty instance'
        null != model.creditMemoInstance
        null == model.creditMemoInstance.id
        null == model.creditMemoInstance.subject
    }

    def 'Create with presets'() {
        given: 'some presets'
        params.subject = 'My credit memo'
        params.headerText = 'For ... you get the following credit memo.'

        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains a pre-filled instance'
        null != model.creditMemoInstance
        null == model.creditMemoInstance.id
        'My credit memo' == model.creditMemoInstance.subject
        'For ... you get the following credit memo.' == model.creditMemoInstance.headerText
    }

    def 'Create with invoice'() {
        given: 'an invoice'
        def invoice = new Invoice(
            subject: 'My first project',
            headerText: 'For ... we send you the following invoice.'
        )
        invoice.id = 56L

        and: 'an invoice service'
        InvoiceService invoiceService = Mock(InvoiceService)
        controller.invoiceService = invoiceService
        1 * invoiceService.get(56L) >> invoice

        and: 'some presets'
        params.invoice = '56'

        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains a pre-filled instance'
        null != model.creditMemoInstance
        null == model.creditMemoInstance.id
        'My first project' == model.creditMemoInstance.subject
        '' == model.creditMemoInstance.headerText
        model.creditMemoInstance.invoice.is invoice
    }

    def 'Create with dunning'() {
        given: 'a dunning'
        def dunning = new Dunning(
            subject: 'My first project',
            headerText: 'For ... we send you the following dunning.'
        )
        dunning.id = 63L

        and: 'a dunning service'
        DunningService dunningService = Mock(DunningService)
        controller.dunningService = dunningService
        1 * dunningService.get(63L) >> dunning

        and: 'some presets'
        params.dunning = '63'

        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains a pre-filled instance'
        null != model.creditMemoInstance
        null == model.creditMemoInstance.id
        'My first project' == model.creditMemoInstance.subject
        '' == model.creditMemoInstance.headerText
        model.creditMemoInstance.dunning.is dunning
    }

    def 'Delete existing instance'() {
        given: 'a credential for an administrator'
        fixtureCredential()

        and: 'an instance'
        def creditMemo = fixtureCreditMemo(5L)

        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> creditMemo

        and: 'the deletion method is called'
        1 * creditMemoService.delete(5L)

        and: 'a flash message has been set'
        'default.deleted.message' == flash.message

        and: 'the instance has been stored in request context'
        request.creditMemoInstance.is creditMemo

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Delete existing instance with versioning conflict'() {
        given: 'a credential for an administrator'
        fixtureCredential()

        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L)

        and: 'the deletion method is called'
        1 * creditMemoService.delete(5L) >> {
            throw new DataIntegrityViolationException('')
        }

        and: 'a flash message has been set'
        'default.not.deleted.message' == flash.message

        and: 'the user is redirected to show view'
        '/creditMemo/show/5' == response.redirectedUrl
    }

    def 'Delete existing instance without permission'() {
        given: 'a stage'
        CreditMemoStage stage = new CreditMemoStage()
        stage.id = 2502

        and: 'an instance'
        def creditMemo = fixtureCreditMemo(5L)
        creditMemo.stage = stage

        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> creditMemo

        and: 'the deletion method is not called'
        0 * creditMemoService.delete(_)

        and: 'no flash message has been set'
        null == flash.message

        and: 'the user is redirected to index view'
        '/creditMemo/index' == response.redirectedUrl
    }

    def 'Delete existing instance with permission'() {
        given: 'a stage'
        CreditMemoStage stage = new CreditMemoStage()
        stage.id = 2501

        and: 'an instance'
        def creditMemo = fixtureCreditMemo(5L)
        creditMemo.stage = stage

        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> creditMemo

        and: 'the deletion method is called'
        1 * creditMemoService.delete(5L)

        and: 'a flash message has been set'
        'default.deleted.message' == flash.message

        and: 'the instance has been stored in request context'
        request.creditMemoInstance.is creditMemo

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Delete non-existing instance'() {
        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> null

        and: 'the deletion method is not called'
        0 * creditMemoService.delete(_)

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the instance has not been stored in request context'
        null == request.creditMemoInstance

        and: 'the user is redirected to index view'
        '/creditMemo/index' == response.redirectedUrl
    }

    def 'Edit existing instance'() {
        given: 'a credential for an administrator'
        fixtureCredential()

        when: 'I call the action method'
        Map model = controller.edit(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L)

        and: 'the model contains an instance'
        verifyCreditMemo model.creditMemoInstance, 5L
    }

    def 'Edit non-existing instance'() {
        given: 'a credential for an administrator'
        fixtureCredential()

        when: 'I call the action method'
        controller.edit(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/creditMemo/index' == response.redirectedUrl
    }

    def 'Edit payment of existing instance'() {
        given: 'a credential for an administrator'
        fixtureCredential()

        when: 'I call the action method'
        Map model = controller.editPayment(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L)

        and: 'the model contains an instance'
        verifyCreditMemo model.creditMemoInstance, 5L
    }

    def 'Edit payment of non-existing instance'() {
        given: 'a credential for an administrator'
        fixtureCredential()

        when: 'I call the action method'
        controller.editPayment(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/creditMemo/index' == response.redirectedUrl
    }

    def 'Index with empty instance list'() {
        given: 'some method stubs'
        1 * creditMemoService.list(_ as Map) >> []
        1 * creditMemoService.count() >> 0

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get an empty list'
        model.creditMemoInstanceList.isEmpty()
        0 == model.creditMemoInstanceTotal
    }

    def 'Index with non-empty instance list'() {
        given: 'some method stubs'
        1 * creditMemoService.list(_ as Map) >> fixtureCreditMemos()
        1 * creditMemoService.count() >> 3

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.creditMemoInstanceList.size()
        3 == model.creditMemoInstanceTotal

        and: 'the items are correct'
        verifyCreditMemos model
    }

    def 'Index with non-empty instance list and params'() {
        given: 'some method stubs'
        1 * creditMemoService.list({
            it.offset == 60 && it.max == 20 && it.sort == 'headerText' &&
                it.order == 'desc'
        }) >> fixtureCreditMemos()
        1 * creditMemoService.count() >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'headerText'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.creditMemoInstanceList.size()
        3 == model.creditMemoInstanceTotal

        and: 'the items are correct'
        verifyCreditMemos model
    }

    def 'Index with search string'() {
        given: 'some method stubs'
        1 * creditMemoService.findAllBySubjectLike(
            '%service%', { it.offset == 200 && it.max == 50}
        ) >> fixtureCreditMemos()
        1 * creditMemoService.countBySubjectLike('%service%') >> 3

        and: 'a given search pattern and other parameters'
        params.search = 'service'
        params.offset = 200
        params.max = 50

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.creditMemoInstanceList.size()
        3 == model.creditMemoInstanceTotal

        and: 'the items are correct'
        verifyCreditMemos model
    }

    def 'Embedded list without parameters'() {
        when: 'I call the action method'
        Map model = controller.listEmbedded(null, null, null, null)

        then: 'I get an undefined list'
        null == model.creditMemoInstanceList
        0 == model.creditMemoInstanceTotal
        null == model.linkParams
    }

    def 'Embedded list with non-existing organization'() {
        given: 'an organization service'
        OrganizationService organizationService = Mock(OrganizationService)
        controller.organizationService = organizationService

        when: 'I call the action method'
        Map model = controller.listEmbedded(5L, null, null, null)

        then: 'the organization retrieval method is called'
        1 * organizationService.get(5L) >> null

        and: 'I get an empty list'
        null == model.creditMemoInstanceList
        0 == model.creditMemoInstanceTotal
        null == model.linkParams
    }

    def 'Embedded list with existing organization'() {
        given: 'an organization service'
        OrganizationService organizationService = Mock(OrganizationService)
        controller.organizationService = organizationService

        and: 'an organization'
        def org = new Organization(name: 'MyCompany Ltd.')
        org.id = 5L

        and: 'some method stubs'
        1 * creditMemoService.findAllByOrganization(
            org,
            {it.offset == 60 && it.max == 20 && it.sort == 'headerText' &&
                it.order == 'desc'}
        ) >> fixtureCreditMemos()
        1 * creditMemoService.countByOrganization(org) >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'headerText'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.listEmbedded(5L, null, null,  null)

        then: 'the organization retrieval method is called'
        1 * organizationService.get(5L) >> org

        then: 'I get a non-empty list'
        3 == model.creditMemoInstanceList.size()
        3 == model.creditMemoInstanceTotal
        1 == model.linkParams.size()
        5L == model.linkParams.organization

        and: 'the items are correct'
        verifyCreditMemos model
    }

    def 'Embedded list with non-existing person'() {
        given: 'an person service'
        PersonService personService = Mock(PersonService)
        controller.personService = personService

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, 35L, null, null)

        then: 'the person retrieval method is called'
        1 * personService.get(35L) >> null

        and: 'I get an empty list'
        null == model.creditMemoInstanceList
        0 == model.creditMemoInstanceTotal
        null == model.linkParams
    }

    def 'Embedded list with existing person'() {
        given: 'an person service'
        PersonService personService = Mock(PersonService)
        controller.personService = personService

        and: 'a person'
        def p = new Person(firstName: 'John', lastName: 'Doe')
        p.id = 35L

        and: 'some method stubs'
        1 * creditMemoService.findAllByPerson(
            p,
            {it.offset == 60 && it.max == 20 && it.sort == 'headerText' &&
                it.order == 'desc'}
        ) >> fixtureCreditMemos()
        1 * creditMemoService.countByPerson(p) >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'headerText'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, 35L, null, null)

        then: 'the person retrieval method is called'
        1 * personService.get(35L) >> p

        then: 'I get a non-empty list'
        3 == model.creditMemoInstanceList.size()
        3 == model.creditMemoInstanceTotal
        1 == model.linkParams.size()
        35L == model.linkParams.person

        and: 'the items are correct'
        verifyCreditMemos model
    }

    def 'Embedded list with non-existing invoice'() {
        given: 'an person service'
        InvoiceService invoiceService = Mock(InvoiceService)
        controller.invoiceService = invoiceService

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, null, 47L, null)

        then: 'the invoice retrieval method is called'
        1 * invoiceService.get(47L) >> null

        and: 'I get an empty list'
        null == model.creditMemoInstanceList
        0 == model.creditMemoInstanceTotal
        null == model.linkParams
    }

    def 'Embedded list with existing invoice'() {
        given: 'an person service'
        InvoiceService invoiceService = Mock(InvoiceService)
        controller.invoiceService = invoiceService

        and: 'an invoice'
        def invoice = new Invoice(subject: 'My first invoice')
        invoice.id = 47L

        and: 'some method stubs'
        1 * creditMemoService.findAllByInvoice(
            invoice,
            {it.offset == 60 && it.max == 20 && it.sort == 'headerText' &&
                it.order == 'desc'}
        ) >> fixtureCreditMemos()
        1 * creditMemoService.countByInvoice(invoice) >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'headerText'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, null, 47L, null)

        then: 'the invoice retrieval method is called'
        1 * invoiceService.get(47L) >> invoice

        then: 'I get a non-empty list'
        3 == model.creditMemoInstanceList.size()
        3 == model.creditMemoInstanceTotal
        1 == model.linkParams.size()
        47L == model.linkParams.invoice

        and: 'the items are correct'
        verifyCreditMemos model
    }

    def 'Embedded list with non-existing dunning'() {
        given: 'an person service'
        DunningService dunningService = Mock(DunningService)
        controller.dunningService = dunningService

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, null, null, 81L)

        then: 'the dunning retrieval method is called'
        1 * dunningService.get(81L) >> null

        and: 'I get an empty list'
        null == model.creditMemoInstanceList
        0 == model.creditMemoInstanceTotal
        null == model.linkParams
    }

    def 'Embedded list with existing dunning'() {
        given: 'an person service'
        DunningService dunningService = Mock(DunningService)
        controller.dunningService = dunningService

        and: 'a dunning'
        def dunning = new Dunning(subject: 'My first invoice')
        dunning.id = 81L

        and: 'some method stubs'
        1 * creditMemoService.findAllByDunning(
            dunning,
            {it.offset == 60 && it.max == 20 && it.sort == 'headerText' &&
                it.order == 'desc'}
        ) >> fixtureCreditMemos()
        1 * creditMemoService.countByDunning(dunning) >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'headerText'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, null, null, 81L)

        then: 'the dunning retrieval method is called'
        1 * dunningService.get(81L) >> dunning

        then: 'I get a non-empty list'
        3 == model.creditMemoInstanceList.size()
        3 == model.creditMemoInstanceTotal
        1 == model.linkParams.size()
        81L == model.linkParams.dunning

        and: 'the items are correct'
        verifyCreditMemos model
    }

    def 'Print existing instance'() {
        given: 'a user'
        User user = fixtureUser()

        and: 'a sequence number service'
        def seqNumberService = Mock(SeqNumberService)
        seqNumberService.format(Invoice, 14005) >> 'I-14005-20040'

        and: 'some instances'
        Invoice invoice = new Invoice(number: 14005, subject: 'My invoice', seqNumberService: seqNumberService)
//        invoice.seqNumberService = seqNumberService
        CreditMemo instance = fixtureCreditMemo(5L)
        instance.number = 14010
        instance.invoice = invoice
        instance.createUser = user

        and: 'a service for invoicing transactions'
        def its = Mock(InvoicingTransactionService)
        controller.invoicingTransactionService = its

        and: 'a FOP service'
        def fop = Mock(FopService)
        controller.fopService = fop

        when: 'I call the action method'
        controller.print(5L, 'foo-template')

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> instance

        and: 'the XML is generated'
        1 * its.generateXML(
            _ as CreditMemo, _ as User, false, _ as Map) >>
        {
            CreditMemo cm, User u, boolean duplicate, Map addData ->
                verifyCreditMemo cm, 5L

                assert 475L == u.id

                assert addData.invoice.is(invoice)
                assert 'I-14010-20040' == addData.invoiceFullNumber

                '<?xml version="1.0"?> <data/>'
        }

        and: 'converted to PDF'
        1 * fop.outputPdf(
            '<?xml version="1.0"?> <data/>', 'credit-memo', 'foo-template',
            _ as HttpServletResponse, 'creditMemo.label .pdf'
        )
    }

    def 'Print non-existing instance'() {
        when: 'I call the action method'
        controller.print(5L, 'default')

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> null

        and: 'a not found error code has been set'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Save successful'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some method stubs'
        1 * creditMemoService.save(_ as CreditMemo) >> { CreditMemo cm ->
            assert 14000 == cm.number
            assert 'My credit memo' == cm.subject
            assert cm.organization.is(org)
            assert 2500L == cm.stage.id
            assert cm.billingAddr.is(addr)
            assert cm.shippingAddr.is(addr)
            assert 'For ... you get the following credit memo.' == cm.headerText
            assert 'pcs' == cm.items[0].unit
            assert 'Item 1' == cm.items[0].name
            assert 'm' == cm.items[1].unit
            assert 'Item 2' == cm.items[1].name

            cm.id = 47L

            cm
        }

        and: 'some form data'
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = org
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(unit: 'pcs', name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the instance has been stored in request context'
        verifyCreditMemo request.creditMemoInstance, 47L

        and: 'a message has been set in flash context'
        'default.created.message' == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Save not successful in items'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some form data'
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = org
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the instance is not saved'
        0 * creditMemoService.save(_)

        and: 'the create view is rendered'
        '/creditMemo/create' == view

        and: 'the model contains the instance'
        verifyCreditMemo model.creditMemoInstance

        and: 'no instance has been stored in request context'
        null == request.creditMemoInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Save not successful in instance'() {
        given: 'some fixtures'
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some form data'
        params.number = '14000'
        params.subject = 'My credit memo'
        // no organization -> validation error
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(unit: 'pcs', name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the instance is not saved'
        0 * creditMemoService.save(_)

        and: 'the create view is rendered'
        '/creditMemo/create' == view

        and: 'the model contains the instance'
        verifyCreditMemo model.creditMemoInstance

        and: 'no instance has been stored in request context'
        null == request.creditMemoInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Save not successful in lowLevelSave'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some method stubs'
        1 * creditMemoService.save(_ as CreditMemo) >> { CreditMemo cm ->
            assert 14000 == cm.number
            assert 'My credit memo' == cm.subject
            assert cm.organization.is(org)
            assert 2500L == cm.stage.id
            assert cm.billingAddr.is(addr)
            assert cm.shippingAddr.is(addr)
            assert 'For ... you get the following credit memo.' == cm.headerText
            assert 'pcs' == cm.items[0].unit
            assert 'Item 1' == cm.items[0].name
            assert 'm' == cm.items[1].unit
            assert 'Item 2' == cm.items[1].name

            null
        }

        and: 'some form data'
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = org
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(unit: 'pcs', name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the create view is rendered'
        '/creditMemo/create' == view

        and: 'the model contains the instance'
        verifyCreditMemo model.creditMemoInstance

        and: 'no instance has been stored in request context'
        null == request.creditMemoInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Show existing instance'() {
        when: 'I call the action method'
        Map model = controller.show(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L)

        and: 'the model contains an instance'
        verifyCreditMemo model.creditMemoInstance, 5L
    }

    def 'Show non-existing instance'() {
        when: 'I call the action method'
        controller.show(5L)

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/creditMemo/index' == response.redirectedUrl
    }

    def 'Update successful'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some method stubs'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L, 14L)
        1 * creditMemoService.save(_ as CreditMemo) >> { CreditMemo cm ->
            assert 5L == cm.id
            assert 14L == cm.version
            assert 14000 == cm.number
            assert 'My credit memo' == cm.subject
            assert cm.organization.is(org)
            assert 2500L == cm.stage.id
            assert cm.billingAddr.is(addr)
            assert cm.shippingAddr.is(addr)
            assert 'For ... you get the following credit memo.' == cm.headerText
            assert 'pcs' == cm.items[0].unit
            assert 'Item 1' == cm.items[0].name
            assert 'm' == cm.items[1].unit
            assert 'Item 2' == cm.items[1].name

            cm
        }

        and: 'some form data'
        params.version = 14L
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = org
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(unit: 'pcs', name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the instance has been stored in request context'
        verifyCreditMemo request.creditMemoInstance, 5L, 14L

        and: 'a message has been set in flash context'
        'default.updated.message' == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update not successful in items'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some method stubs'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L, 14L)

        and: 'some form data'
        params.version = 14L
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = org
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the instance is not saved'
        0 * creditMemoService.save(_)

        and: 'the edit view is rendered'
        '/creditMemo/edit' == view

        and: 'the model contains the instance'
        verifyCreditMemo model.creditMemoInstance, 5L, 14L

        and: 'no instance has been stored in request context'
        null == request.creditMemoInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update not successful in instance'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some method stubs'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L, 14L)

        and: 'some form data'
        params.version = 14L
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = null
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(unit: 'pcs', name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the instance is not saved'
        0 * creditMemoService.save(_)

        and: 'the edit view is rendered'
        '/creditMemo/edit' == view

        and: 'the model contains the instance'
        verifyCreditMemo model.creditMemoInstance, 5L, 14L

        and: 'no instance has been stored in request context'
        null == request.creditMemoInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update not successful in lowLevelSave'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some method stubs'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L, 14L)
        1 * creditMemoService.save(_ as CreditMemo) >> { CreditMemo cm ->
            assert 5L == cm.id
            assert 14L == cm.version
            assert 14000 == cm.number
            assert 'My credit memo' == cm.subject
            assert cm.organization.is(org)
            assert 2500L == cm.stage.id
            assert cm.billingAddr.is(addr)
            assert cm.shippingAddr.is(addr)
            assert 'For ... you get the following credit memo.' == cm.headerText
            assert 'pcs' == cm.items[0].unit
            assert 'Item 1' == cm.items[0].name
            assert 'm' == cm.items[1].unit
            assert 'Item 2' == cm.items[1].name

            null
        }

        and: 'some form data'
        params.version = 14L
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = org
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(unit: 'pcs', name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the edit view is rendered'
        '/creditMemo/edit' == view

        and: 'the model contains the instance'
        verifyCreditMemo model.creditMemoInstance, 5L, 14L

        and: 'no instance has been stored in request context'
        null == request.creditMemoInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update existing instance with version conflict'() {
        given: 'some fixtures'
        def org = new Organization(
            name: 'My company, ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        def stage = new CreditMemoStage()
        stage.id = 2500L
        def addr = new Address()

        and: 'some method stubs'
        1 * creditMemoService.get(5L) >> fixtureCreditMemo(5L, 14L)

        and: 'some form data'
        params.version = '13'
        params.number = '14000'
        params.subject = 'My credit memo'
        params.organization = org
        params.stage = stage
        params.billingAddr = addr
        params.shippingAddr = addr
        params.headerText = 'For ... you get the following credit memo.'
        params.'items[0]' = new InvoicingItem(unit: 'pcs', name: 'Item 1')
        params.'items[1]' = new InvoicingItem(unit: 'm', name: 'Item 2')

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the edit view is rendered'
        '/creditMemo/edit' == view

        and: 'the model contains the instance unmodified'
        verifyCreditMemo model.creditMemoInstance, 5L, 14L

        and: 'a field error has been set'
        'default.optimistic.locking.failure' == model.creditMemoInstance.errors['version'].code

        and: 'the save method has not been called'
        0 * creditMemoService.save(_)

        and: 'no redirect has been set'
        null == response.redirectedUrl
    }

    def 'Update non-existing instance'() {
        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the retrieval method is called'
        1 * creditMemoService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/creditMemo/index' == response.redirectedUrl
    }

    // TODO updatePayment


    //-- Non-public methods -------------------------

    private void fixtureCredential() {
        session.credential = new Credential(fixtureUser())
    }

    private static CreditMemo fixtureCreditMemo(Long id = null,
                                                Long version = null)
    {
        def stage = new CreditMemoStage()
        stage.id = 2500L

        def creditMemo = new CreditMemo(
            subject: 'My credit memo',
            headerText: 'For ... you get the following credit memo.',
            stage: stage,
            items: [
                new InvoicingItem(unit: 'units', name: 'Item X1'),
                new InvoicingItem(unit: 'pcs', name: 'Item X2')
            ]
        )
        if (id != null) creditMemo.id = id
        if (version != null) creditMemo.version = version

        creditMemo
    }

    private static List<CreditMemo> fixtureCreditMemos(int n = 3) {
        List<CreditMemo> res = []
        for (int i = 0; i < n; i++) {
            CreditMemo c = new CreditMemo(subject: 'My credit memo ' + (i + 1))
            res << c
        }

        res
    }

    private static User fixtureUser() {
        User user = new User(admin: true)
        user.id = 475L

        user
    }

    private static void verifyCreditMemo(CreditMemo instance, Long id = null,
                                         Long version = null)
    {
        assert null != instance
        assert id == instance.id
        assert version == instance.version
        assert 'My credit memo' == instance.subject
        assert 'For ... you get the following credit memo.' == instance.headerText
    }

    private static void verifyCreditMemos(Map model, int n = 3) {
        for (int i = 0; i < n; i++) {
            String subject = 'My credit memo ' + (i + 1)
            assert subject == model.creditMemoInstanceList[i].subject
        }
    }
}
