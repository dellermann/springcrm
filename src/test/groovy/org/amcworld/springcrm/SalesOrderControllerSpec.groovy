/*
 * SalesOrderControllerSpec.groovy
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
 *
 */


package org.amcworld.springcrm

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationErrors
import grails.validation.ValidationException
import grails.web.servlet.mvc.GrailsParameterMap
import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification


class SalesOrderControllerSpec extends Specification
    implements ControllerUnitTest<SalesOrderController>,
        DomainUnitTest<SalesOrder>
{

    //-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        SalesOrder salesOrder = instance

        when: 'the action is executed'
        controller.copy salesOrder

        then: 'the model is correctly created'
        null != model.salesOrder
        salesOrder.subject == model.salesOrder.subject
        0i == model.salesOrder.number
        salesOrder.headerText == model.salesOrder.headerText

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        given: 'a quote'
        Quote quote = new Quote(
            subject: 'My first quote',
            headerText: 'We offer you the following services:'
        )
        quote.id = new ObjectId()

        and: 'a quote service'
        QuoteService quoteService = Mock()
        controller.quoteService = quoteService

        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.salesOrder
        null == model.salesOrder.quote

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create quote.id.toString()

        then: 'the quote has been obtained'
        //noinspection GroovyAssignabilityCheck
        1 * quoteService.get(quote.id) >> quote

        and: 'the model is correctly created'
        null != model.salesOrder
        quote.subject == model.salesOrder.subject
        quote.headerText == model.salesOrder.headerText
        quote.is model.salesOrder.quote
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        service.delete(salesOrder.id) >> salesOrder
        controller.salesOrderService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        webRequest.actionName = 'delete'
        controller.delete null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.delete(_)
        '/salesOrder/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(salesOrder.id)
        '/salesOrder/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete salesOrder.id.toString()

        then: 'the instance is deleted'
        //noinspection GroovyAssignabilityCheck
        1 * service.delete(salesOrder.id) >> salesOrder
        '/salesOrder/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The delete action deletes associated data files'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()
        salesOrder.orderDocument = new DataFile()

        and: 'a service instance'
        SalesOrderService service = Mock()
        service.delete(salesOrder.id) >> salesOrder
        controller.salesOrderService = service

        and: 'a data file service instance'
        DataFileService dataFileService = Mock()
        controller.dataFileService = dataFileService

        when: 'the action is called for an existing instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        webRequest.actionName = 'delete'
        controller.delete salesOrder.id.toString()

        then: 'the instance is deleted'
        //noinspection GroovyAssignabilityCheck
        1 * service.delete(salesOrder.id) >> salesOrder
        1 * dataFileService.removeFile(
            SalesOrderController.FILE_TYPE, salesOrder.orderDocument
        )
        '/salesOrder/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        when: 'the action is executed with a null domain'
        controller.edit null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.edit new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(salesOrder.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit salesOrder.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(salesOrder.id) >> salesOrder
        salesOrder == model.salesOrder
    }

    void 'The find action without type returns the correct model'() {
        given: 'a list of sales orders'
        def list = [
            new SalesOrder(number: 10000i, subject: 'Sales order 1'),
            new SalesOrder(number: 10020i, subject: 'Sales order 2'),
            new SalesOrder(number: 10021i, subject: 'Sales order 3'),
        ]

        and: 'an organization'
        def org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        when: 'the action is executed with a null value'
        def model = controller.find(null, null)

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.find(_, _, _)
        //noinspection GroovyAssignabilityCheck
        1 * service.list([: ]) >> list.subList(1, 2)
        1 == model.salesOrderList.size()
        list[1] == model.salesOrderList.first()

        when: 'the action is executed with an empty value'
        model = controller.find('', null)

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.find(_, _, _)
        //noinspection GroovyAssignabilityCheck
        1 * service.list([: ]) >> list.subList(1, 2)
        1 == model.salesOrderList.size()
        list[1] == model.salesOrderList.first()

        when: 'the action is executed with a numeric name'
        response.reset()
        model = controller.find('10020', null)

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.find(10020i, '10020', null) >> list.subList(1, 2)
        1 == model.salesOrderList.size()
        list[1] == model.salesOrderList.first()

        when: 'the action is executed with a name'
        response.reset()
        model = controller.find('ote', null)

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.find(null, 'ote', null) >> list
        3 == model.salesOrderList.size()
        //noinspection GroovyAssignabilityCheck
        list == model.salesOrderList

        when: 'the action is executed with a name and an empty organization'
        response.reset()
        model = controller.find('ote', '')

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.find(null, 'ote', null) >> list
        3 == model.salesOrderList.size()
        //noinspection GroovyAssignabilityCheck
        list == model.salesOrderList

        when: 'the action is executed with no name and an organization'
        response.reset()
        model = controller.find(null, org.id.toString())

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        //noinspection GroovyAssignabilityCheck
        0 * service.find(_, _, _)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByOrganization(org, [: ]) >> list.subList(1, 2)
        1 == model.salesOrderList.size()
        list[1] == model.salesOrderList.first()

        when: 'the action is executed with an empty name and an organization'
        response.reset()
        model = controller.find('', org.id.toString())

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        //noinspection GroovyAssignabilityCheck
        0 * service.find(_, _, _)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByOrganization(org, [: ]) >> list.subList(1, 2)
        1 == model.salesOrderList.size()
        list[1] == model.salesOrderList.first()

        when: 'the action is executed with a numeric name and an organization'
        response.reset()
        model = controller.find('10020', org.id.toString())

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        //noinspection GroovyAssignabilityCheck
        1 * service.find(10020i, '10020', org) >> list.subList(1, 2)
        1 == model.salesOrderList.size()
        list[1] == model.salesOrderList.first()

        when: 'the action is executed with a name and an organization'
        response.reset()
        model = controller.find('ote', org.id.toString())

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        //noinspection GroovyAssignabilityCheck
        1 * service.find(null, 'ote', org) >> list
        3 == model.salesOrderList.size()
        //noinspection GroovyAssignabilityCheck
        list == model.salesOrderList
    }

    void 'The index action returns the correct model'() {
        given: 'a list of sales orders'
        def list = [
            new SalesOrder(subject: 'Sales order 1'),
            new SalesOrder(subject: 'Sales order 2'),
            new SalesOrder(subject: 'Sales order 3'),
        ]

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        when: 'the action is executed'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        1 * service.count() >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        list.size() == model.salesOrderList.size()
        list == (List) model.salesOrderList
        list.size() == model.salesOrderCount

        when: 'the action is executed with a search term'
        response.reset()
        params.max = 10
        params.offset = 20
        params.search = 'rde'
        controller.index()

        then: 'the model is correct'
        1 * service.countBySubjectLike('%rde%') >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllBySubjectLike(
            '%rde%', getParameterMap(max: 10, offset: 20, search: 'rde')
        ) >> list
        list.size() == model.salesOrderList.size()
        list == (List) model.salesOrderList
        list.size() == model.salesOrderCount
    }

    void 'The listEmbedded action returns the correct model'() {
        given: 'a list of sales orders'
        def list = [
            new SalesOrder(subject: 'Sales order 1'),
            new SalesOrder(subject: 'Sales order 2'),
            new SalesOrder(subject: 'Sales order 3'),
        ]

        and: 'an organization'
        Organization org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a person'
        Person person = new Person(firstName: 'John', lastName: 'Doe')
        person.id = new ObjectId()

        and: 'a quote'
        Quote quote = new Quote(
            subject: 'My first quote',
            headerText: 'We offer you the following services:'
        )
        quote.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        and: 'a person service instance'
        PersonService personService = Mock()
        controller.personService = personService

        and: 'a quote service instance'
        QuoteService quoteService = Mock()
        controller.quoteService = quoteService

        when: 'the action is called for a null instance'
        controller.listEmbedded null, null, null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        0 * personService.get(_)
        0 * quoteService.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByQuote(_, _)
        0 * service.countByQuote(_)
        404 == response.status

        when: 'the action is called for a non-existing organization'
        response.reset()
        controller.listEmbedded new ObjectId().toString(), null, null

        then: 'a 404 error is returned'
        0 * organizationService.get(org.id)
        0 * personService.get(_)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for an existing organization'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded org.id.toString(), null, null

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        0 * quoteService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByQuote(_, _)
        0 * service.countByQuote(_)
        list == (List) model.salesOrderList
        list.size() == model.salesOrderCount
        [organization: org.id.toString()] == (Map) model.linkParams

        when: 'the action is called for a non-existing person'
        response.reset()
        controller.listEmbedded null, new ObjectId().toString(), null

        then: 'a 404 error is returned'
        0 * organizationService.get(_)
        0 * personService.get(person.id)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for an existing person'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded null, person.id.toString(), null

        then: 'the model is correct'
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * personService.get(person.id) >> person
        0 * quoteService.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByOrganization(_)
        0 * service.countByOrganization(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByPerson(
            person, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByPerson(person) >> list.size()
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByQuote(_, _)
        0 * service.countByQuote(_)
        list == (List) model.salesOrderList
        list.size() == model.salesOrderCount
        [person: person.id.toString()] == (Map) model.linkParams

        when: 'the action is called for a non-existing organization and a person'
        response.reset()
        controller.listEmbedded(
            new ObjectId().toString(), person.id.toString(), null
        )

        then: 'a 404 error is returned'
        0 * organizationService.get(org.id)
        0 * personService.get(_)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for an existing organization and person'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded org.id.toString(), person.id.toString(), null

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        0 * quoteService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByQuote(_, _)
        0 * service.countByQuote(_)
        list == (List) model.salesOrderList
        list.size() == model.salesOrderCount
        [organization: org.id.toString()] == (Map) model.linkParams

        when: 'the action is called for an existing quote'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded null, null, quote.id.toString()

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * quoteService.get(quote.id) >> quote
        0 * personService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByQuote(
            quote, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByQuote(quote) >> list.size()
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        list == (List) model.salesOrderList
        list.size() == model.salesOrderCount
        [quote: quote.id.toString()] == (Map) model.linkParams
    }

    void 'The print action correctly produces PDF values'() {
        given: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'an invoicing transaction service instance'
        InvoicingTransactionService invoicingTransactionService = Mock()
        controller.invoicingTransactionService = invoicingTransactionService

        and: 'a sequence number service instance'
        SeqNumberService seqNumberService = Mock()
        controller.seqNumberService = seqNumberService

        and: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'some PDF data'
        byte [] pdf = 'PDF/1.1 data example'.bytes

        when: 'the action is executed without an ID'
        controller.print null, null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * seqNumberService.getFullNumber(_)
        404 == response.status

        when: 'the action is executed with the ID of a non-existing instance'
        response.reset()
        controller.print new ObjectId().toString(), null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(salesOrder.id)
        0 * seqNumberService.getFullNumber(_)
        404 == response.status

        when: 'the action is executed with the ID of an existing instance'
        response.reset()
        controller.print salesOrder.id.toString(), 'default'

        then: 'the PDF is written to the response'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(salesOrder.id) >> salesOrder
        //noinspection GroovyAssignabilityCheck
        1 * invoicingTransactionService.print(salesOrder, 'default', false) >>
            pdf
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(salesOrder) >> 'S-45000'
        'application/pdf;charset=utf-8' == response.contentType
        pdf.length == response.contentLength
        pdf == response.contentAsByteArray
        'attachment;filename="salesOrder.label S-45000.pdf"' ==
            response.getHeader('Content-Disposition')

        when: 'the action is executed with the another template'
        response.reset()
        controller.print salesOrder.id.toString(), 'foo'

        then: 'the PDF is written to the response'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(salesOrder.id) >> salesOrder
        //noinspection GroovyAssignabilityCheck
        1 * invoicingTransactionService.print(salesOrder, 'foo', false) >> pdf
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(salesOrder) >> 'S-45000'
        'application/pdf;charset=utf-8' == response.contentType
        pdf.length == response.contentLength
        pdf == response.contentAsByteArray
        'attachment;filename="salesOrder.label S-45000.pdf"' ==
            response.getHeader('Content-Disposition')

        when: 'the action is executed with a duplicate'
        response.reset()
        params.duplicate = 1
        controller.print salesOrder.id.toString(), 'default'

        then: 'the PDF is written to the response'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(salesOrder.id) >> salesOrder
        //noinspection GroovyAssignabilityCheck
        1 * invoicingTransactionService.print(salesOrder, 'default', true) >> pdf
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(salesOrder) >> 'S-45000'
        'application/pdf;charset=utf-8' == response.contentType
        pdf.length == response.contentLength
        pdf == response.contentAsByteArray
        'attachment;filename="salesOrder.label S-45000 invoicingTransaction.duplicate.pdf"' ==
            response.getHeader('Content-Disposition')
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        SalesOrder salesOrder = instance

        and: 'an ID'
        ObjectId id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'a data service'
        DataFileService dataFileService = Mock()
        controller.dataFileService = dataFileService

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        webRequest.actionName = 'save'
        controller.save null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/salesOrder/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save salesOrder

        then: 'the create view is rendered again with the correct model'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> {
            throw new ValidationException('', new ValidationErrors(salesOrder))
        }
        salesOrder == model.salesOrder
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save salesOrder

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        '/salesOrder/edit/' + id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        controller.save salesOrder

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + id + '?returnUrl=%2Finvoice%2Fshow%2F12345' ==
            response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        params.remove 'returnUrl'
        params.close = 1
        controller.save salesOrder

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        '/salesOrder/show/' + id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.save salesOrder

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The save action correctly handles documents'() {
        given: 'an instance'
        SalesOrder salesOrder = instance

        and: 'an ID'
        ObjectId id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'a data service'
        DataFileService dataFileService = Mock()
        controller.dataFileService = dataFileService

        and: 'a multipart and a data file'
        MultipartFile file = Mock()
        DataFile dataFile = new DataFile()

        when: 'the action is executed without a file'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        webRequest.actionName = 'save'
        controller.save salesOrder

        then: 'no file has been stored'
        //noinspection GroovyAssignabilityCheck
        1 * dataFileService.storeFile(SalesOrderController.FILE_TYPE, null)
        null == salesOrder.orderDocument
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        '/salesOrder/edit/' + id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a file'
        response.reset()
        params.file = file
        controller.save salesOrder

        then: 'the file has been stored'
        //noinspection GroovyAssignabilityCheck
        1 * dataFileService.storeFile(SalesOrderController.FILE_TYPE, file) >>>
            dataFile
        dataFile.is salesOrder.orderDocument
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The save action correctly handles quotes'() {
        given: 'an instance'
        SalesOrder salesOrder = instance

        and: 'an ID'
        ObjectId id = new ObjectId()

        and: 'a quote'
        Quote quote = new Quote(stage: new QuoteStage())
        quote.id = new ObjectId()

        and: 'a quote stage'
        QuoteStage newStage = new QuoteStage()
        newStage.id = 603L

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'a quote service'
        QuoteService quoteService = Mock()
        quoteService.get(quote.id) >> quote
        controller.quoteService = quoteService

        and: 'a data service'
        DataFileService dataFileService = Mock()
        controller.dataFileService = dataFileService

        and: 'a selection value service'
        SelValueService selValueService = Mock()
        selValueService.get(603L) >> newStage
        controller.selValueService = selValueService

        when: 'the action is executed without an associated quote'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        webRequest.actionName = 'save'
        controller.save salesOrder

        then: 'no quote has been changed'
        //noinspection GroovyAssignabilityCheck
        0 * quoteService.save(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with an associated quote'
        response.reset()
        salesOrder.quote = quote
        controller.save salesOrder

        then: 'the quote has been changed'
        1 * quoteService.save(quote)
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> { salesOrder.id = id; salesOrder }
        newStage.is quote.stage
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The setSignature action sets the signature data'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        when: 'the action is called for a null instance'
        params.signature = 'foobar'
        controller.setSignature null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        404 == response.status

        when: 'the action is called for a non-existing instance'
        response.reset()
        params.signature = 'foobar'
        controller.setSignature new ObjectId().toString()

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(salesOrder.id)
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        404 == response.status

        when: 'the action is called for an existing instance'
        response.reset()
        params.signature = 'foobar'
        controller.setSignature salesOrder.id.toString()

        then: 'the signature has been set'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(salesOrder.id) >> salesOrder
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            it instanceof SalesOrder && it.signature == params.signature
        })
        '/salesOrder/show/' + salesOrder.id == response.redirectedUrl
    }

    void 'The show action returns the correct model'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        when: 'the action is executed with a null domain'
        controller.show null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.show new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(salesOrder.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show salesOrder.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(salesOrder.id) >> salesOrder
        salesOrder == model.salesOrder
    }

    void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'a data service'
        DataFileService dataFileService = Mock()
        controller.dataFileService = dataFileService

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        webRequest.actionName = 'update'
        controller.update null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/salesOrder/index' == response.redirectedUrl
        null != flash.message

        when: 'an invalid domain instance is passed to the action'
        response.reset()
        controller.update salesOrder

        then: 'the edit view is rendered again with the invalid instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> {
            throw new ValidationException('', new ValidationErrors(salesOrder))
        }
        salesOrder == model.salesOrder
        'edit' == view

        when: 'a valid domain instance is passed to the action'
        response.reset()
        controller.update salesOrder

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        controller.save salesOrder

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + salesOrder.id +
            '?returnUrl=%2Finvoice%2Fshow%2F12345' == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        params.remove 'returnUrl'
        params.close = 1
        controller.save salesOrder

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        '/salesOrder/show/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.save salesOrder

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The update action correctly handles documents'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'a data service'
        DataFileService dataFileService = Mock()
        controller.dataFileService = dataFileService

        and: 'a multipart and a data file'
        MultipartFile file = Mock()
        DataFile dataFile = new DataFile()

        when: 'the action is executed without a file'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        webRequest.actionName = 'update'
        controller.update salesOrder

        then: 'no file has been stored'
        //noinspection GroovyAssignabilityCheck
        0 * dataFileService.updateFile(_, _, _)
        null == salesOrder.orderDocument
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a file'
        response.reset()
        params.file = file
        controller.update salesOrder

        then: 'the file has been stored'
        //noinspection GroovyAssignabilityCheck
        1 * dataFileService.updateFile(
            SalesOrderController.FILE_TYPE, null, file
        ) >>> dataFile
        dataFile.is salesOrder.orderDocument
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed to overwrite a file'
        response.reset()
        salesOrder.orderDocument = new DataFile()
        params.file = file
        controller.update salesOrder

        then: 'the file has been stored'
        //noinspection GroovyAssignabilityCheck
        1 * dataFileService.updateFile(
            SalesOrderController.FILE_TYPE, salesOrder.orderDocument, file
        ) >>> dataFile
        dataFile.is salesOrder.orderDocument
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed to delete a non-existing file'
        response.reset()
        salesOrder.orderDocument = null
        params.fileRemove = '1'
        controller.update salesOrder

        then: 'the file has not been removed'
        //noinspection GroovyAssignabilityCheck
        0 * dataFileService.updateFile(_, _, _)
        0 * dataFileService.removeFile(_, _)
        null == salesOrder.orderDocument
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed to delete an existing file'
        response.reset()
        salesOrder.orderDocument = dataFile
        params.fileRemove = '1'
        controller.update salesOrder

        then: 'the file has been removed'
        //noinspection GroovyAssignabilityCheck
        0 * dataFileService.updateFile(_, _, _)
        1 * dataFileService.removeFile(SalesOrderController.FILE_TYPE, dataFile)
        null == salesOrder.orderDocument
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The update action correctly handles quotes'() {
        given: 'an instance'
        SalesOrder salesOrder = instance
        salesOrder.id = new ObjectId()

        and: 'a quote'
        Quote quote = new Quote(stage: new QuoteStage())
        quote.id = new ObjectId()

        and: 'a quote stage'
        QuoteStage newStage = new QuoteStage()
        newStage.id = 603L

        and: 'a service instance'
        SalesOrderService service = Mock()
        controller.salesOrderService = service

        and: 'a quote service'
        QuoteService quoteService = Mock()
        quoteService.get(quote.id) >> quote
        controller.quoteService = quoteService

        and: 'a data service'
        DataFileService dataFileService = Mock()
        controller.dataFileService = dataFileService

        and: 'a selection value service'
        SelValueService selValueService = Mock()
        selValueService.get(603L) >> newStage
        controller.selValueService = selValueService

        when: 'the action is executed without an associated quote'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        webRequest.actionName = 'update'
        controller.update salesOrder

        then: 'no quote has been changed'
        //noinspection GroovyAssignabilityCheck
        0 * quoteService.save(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with an associated quote'
        response.reset()
        salesOrder.quote = quote
        controller.update salesOrder

        then: 'the quote has been changed'
        1 * quoteService.save(quote)
        //noinspection GroovyAssignabilityCheck
        1 * service.save(salesOrder) >> salesOrder
        newStage.is quote.stage
        //noinspection SpellCheckingInspection
        '/salesOrder/edit/' + salesOrder.id == response.redirectedUrl
        null != controller.flash.message
    }


    //-- Non-public methods -------------------------

    private static SalesOrder getInstance() {
        Map properties = [: ]
        populateValidParams properties, 45000i

        new SalesOrder(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params, Integer number = null) {
        assert params != null

        params.headerText = 'Thank you for ordering the following services:'
        params.number = number
        params.subject = 'Groovy programming'
        params.validUntil = new Date() + 14
    }
}
