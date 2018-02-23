/*
 * QuoteControllerSpec.groovy
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
import grails.validation.ValidationErrors
import grails.validation.ValidationException
import grails.web.servlet.mvc.GrailsParameterMap
import org.bson.types.ObjectId
import spock.lang.Specification


class QuoteControllerSpec extends Specification
    implements ControllerUnitTest<QuoteController>
{

    //-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        Quote quote = instance

        when: 'the action is executed'
        controller.copy quote

        then: 'the model is correctly created'
        null != model.quote
        quote.subject == model.quote.subject
        0i == model.quote.number
        quote.headerText == model.quote.headerText

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.quote

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.quote
        params.subject == model.quote.subject
        params.headerText == model.quote.headerText
        params.validUntil == model.quote.validUntil
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        Quote quote = instance
        quote.id = new ObjectId()

        and: 'a service instance'
        QuoteService service = Mock()
        service.delete(quote.id) >> quote
        controller.quoteService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.delete(_)
        '/quote/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(quote.id)
        '/quote/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete quote.id.toString()

        then: 'the instance is deleted'
        //noinspection GroovyAssignabilityCheck
        1 * service.delete(quote.id) >> quote
        '/quote/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        Quote quote = instance
        quote.id = new ObjectId()

        and: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

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
        0 * service.get(quote.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit quote.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(quote.id) >> quote
        quote == model.quote
    }

    void 'The find action without type returns the correct model'() {
        given: 'a list of quotes'
        def list = [
            new Quote(number: 10000i, subject: 'Quote 1'),
            new Quote(number: 10020i, subject: 'Quote 2'),
            new Quote(number: 10021i, subject: 'Quote 3'),
        ]

        and: 'an organization'
        def org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        when: 'the action is executed with a null value'
        controller.find null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.find(_, _, _)
        404 == response.status

        when: 'the action is executed with a numeric name'
        response.reset()
        controller.find '10020'

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.find(10020i, '10020', null) >> list.subList(1, 2)
        1 == model.quoteList.size()
        list[1] == model.quoteList.first()

        when: 'the action is executed with a name'
        response.reset()
        controller.find 'ote'

        then: 'the model is correct'
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.find(null, 'ote', null) >> list
        3 == model.quoteList.size()
        //noinspection GroovyAssignabilityCheck
        list == model.quoteList

        when: 'the action is executed with a numeric name and an organization'
        response.reset()
        params.organization = org.id
        controller.find '10020'

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        //noinspection GroovyAssignabilityCheck
        1 * service.find(10020i, '10020', org) >> list.subList(1, 2)
        1 == model.quoteList.size()
        list[1] == model.quoteList.first()

        when: 'the action is executed with a name'
        response.reset()
        controller.find 'ote'

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        //noinspection GroovyAssignabilityCheck
        1 * service.find(null, 'ote', org) >> list
        3 == model.quoteList.size()
        //noinspection GroovyAssignabilityCheck
        list == model.quoteList
    }

    void 'The index action returns the correct model'() {
        given: 'a list of quotes'
        def list = [
            new Quote(subject: 'Quote 1'),
            new Quote(subject: 'Quote 2'),
            new Quote(subject: 'Quote 3'),
        ]

        and: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

        when: 'the action is executed'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        1 * service.count() >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        list.size() == model.quoteList.size()
        list == (List) model.quoteList
        list.size() == model.quoteCount

        when: 'the action is executed with a search term'
        response.reset()
        params.max = 10
        params.offset = 20
        params.search = 'ote'
        controller.index()

        then: 'the model is correct'
        1 * service.countBySubjectLike('%ote%') >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllBySubjectLike(
            '%ote%', getParameterMap(max: 10, offset: 20, search: 'ote')
        ) >> list
        list.size() == model.quoteList.size()
        list == (List) model.quoteList
        list.size() == model.quoteCount
    }

    void 'The listEmbedded action returns the correct model'() {
        given: 'a list of quotes'
        def list = [
            new Quote(subject: 'Quote 1'),
            new Quote(subject: 'Quote 2'),
            new Quote(subject: 'Quote 3'),
        ]

        and: 'an organization'
        Organization org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a person'
        Person person = new Person(firstName: 'John', lastName: 'Doe')
        person.id = new ObjectId()

        and: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        and: 'a person service instance'
        PersonService personService = Mock()
        controller.personService = personService

        when: 'the action is called for a null instance'
        controller.listEmbedded null, null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * organizationService.get(_)
        0 * personService.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for a non-existing organization'
        response.reset()
        controller.listEmbedded new ObjectId().toString(), null

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
        controller.listEmbedded org.id.toString(), null

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        list == (List) model.quoteList
        list.size() == model.quoteCount
        [organization: org.id.toString()] == (Map) model.linkParams

        when: 'the action is called for a non-existing person'
        response.reset()
        controller.listEmbedded null, new ObjectId().toString()

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
        controller.listEmbedded null, person.id.toString()

        then: 'the model is correct'
        0 * organizationService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * personService.get(person.id) >> person
        //noinspection GroovyAssignabilityCheck
        0 * service.findAllByOrganization(_)
        0 * service.countByOrganization(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByPerson(
            person, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByPerson(person) >> list.size()
        list == (List) model.quoteList
        list.size() == model.quoteCount
        [person: person.id.toString()] == (Map) model.linkParams

        when: 'the action is called for a non-existing organization and a person'
        response.reset()
        controller.listEmbedded new ObjectId().toString(), person.id.toString()

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
        controller.listEmbedded org.id.toString(), person.id.toString()

        then: 'the model is correct'
        //noinspection GroovyAssignabilityCheck
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        list == (List) model.quoteList
        list.size() == model.quoteCount
        [organization: org.id.toString()] == (Map) model.linkParams
    }

    void 'The print action correctly produces PDF values'() {
        given: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

        and: 'an invoicing transaction service instance'
        InvoicingTransactionService invoicingTransactionService = Mock()
        controller.invoicingTransactionService = invoicingTransactionService

        and: 'a sequence number service instance'
        SeqNumberService seqNumberService = Mock()
        controller.seqNumberService = seqNumberService

        and: 'an instance'
        Quote quote = instance
        quote.id = new ObjectId()

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
        0 * service.get(quote.id)
        0 * seqNumberService.getFullNumber(_)
        404 == response.status

        when: 'the action is executed with the ID of an existing instance'
        response.reset()
        controller.print quote.id.toString(), 'default'

        then: 'the PDF is written to the response'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(quote.id) >> quote
        //noinspection GroovyAssignabilityCheck
        1 * invoicingTransactionService.print(quote, 'default', false) >> pdf
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(quote) >> 'Q-45000'
        'application/pdf;charset=utf-8' == response.contentType
        pdf.length == response.contentLength
        pdf == response.contentAsByteArray
        'attachment;filename="quote.label Q-45000.pdf"' ==
            response.getHeader('Content-Disposition')

        when: 'the action is executed with the another template'
        response.reset()
        controller.print quote.id.toString(), 'foo'

        then: 'the PDF is written to the response'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(quote.id) >> quote
        //noinspection GroovyAssignabilityCheck
        1 * invoicingTransactionService.print(quote, 'foo', false) >> pdf
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(quote) >> 'Q-45000'
        'application/pdf;charset=utf-8' == response.contentType
        pdf.length == response.contentLength
        pdf == response.contentAsByteArray
        'attachment;filename="quote.label Q-45000.pdf"' ==
            response.getHeader('Content-Disposition')

        when: 'the action is executed with a duplicate'
        response.reset()
        params.duplicate = 1
        controller.print quote.id.toString(), 'default'

        then: 'the PDF is written to the response'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(quote.id) >> quote
        //noinspection GroovyAssignabilityCheck
        1 * invoicingTransactionService.print(quote, 'default', true) >> pdf
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(quote) >> 'Q-45000'
        'application/pdf;charset=utf-8' == response.contentType
        pdf.length == response.contentLength
        pdf == response.contentAsByteArray
        'attachment;filename="quote.label Q-45000 invoicingTransaction.duplicate.pdf"' ==
            response.getHeader('Content-Disposition')
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        Quote quote = instance
        quote.id = new ObjectId()

        and: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/quote/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save quote

        then: 'the create view is rendered again with the correct model'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(quote) >> {
            throw new ValidationException('', new ValidationErrors(quote))
        }
        quote == model.quote
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save quote

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(quote) >> quote
        '/quote/show/' + quote.id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The show action returns the correct model'() {
        given: 'an instance'
        Quote quote = instance
        quote.id = new ObjectId()

        and: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

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
        0 * service.get(quote.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show quote.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(quote.id) >> quote
        quote == model.quote
    }

    void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        Quote quote = instance
        quote.id = new ObjectId()

        and: 'a service instance'
        QuoteService service = Mock()
        controller.quoteService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/quote/index' == response.redirectedUrl
        null != flash.message

        when: 'an invalid domain instance is passed to the action'
        response.reset()
        controller.update quote

        then: 'the edit view is rendered again with the invalid instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(quote) >> {
            throw new ValidationException('', new ValidationErrors(quote))
        }
        quote == model.quote
        'edit' == view

        when: 'a valid domain instance is passed to the action'
        response.reset()
        controller.update quote

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(quote) >> quote
        '/quote/show/' + quote.id == response.redirectedUrl
        null != controller.flash.message
    }


    //-- Non-public methods -------------------------

    private static Quote getInstance() {
        Map properties = [: ]
        populateValidParams properties, 45000i

        new Quote(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params, Integer number = null) {
        assert params != null

        params.headerText = 'We offer you the following services:'
        params.number = number
        params.subject = 'Groovy programming'
        params.validUntil = new Date() + 14
    }
}
