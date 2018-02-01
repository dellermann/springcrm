/*
 * PhoneCallControllerSpec.groovy
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
import org.amcworld.springcrm.google.GoogleContactSync
import org.bson.types.ObjectId
import org.grails.web.util.GrailsApplicationAttributes
import spock.lang.Ignore
import spock.lang.Specification


class PhoneCallControllerSpec extends Specification
    implements ControllerUnitTest<PhoneCallController>
{

    //-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        PhoneCall call = instance

        when: 'the action is executed'
        controller.copy call

        then: 'the model is correctly created'
        null != model.phoneCall
        call.phone == model.phoneCall.phone
        call.status == model.phoneCall.status
        call.subject == model.phoneCall.subject
        call.type == model.phoneCall.type

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.phoneCall

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.phoneCall
        params.phone == model.phoneCall.phone
        params.status == model.phoneCall.status
        params.subject == model.phoneCall.subject
        params.type == model.phoneCall.type
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        PhoneCall phoneCall = instance
        phoneCall.id = new ObjectId()

        and: 'a service instance'
        PhoneCallService service = Mock()
        service.delete(phoneCall.id) >> phoneCall
        controller.phoneCallService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete null

        then: 'a 404 error is returned'
        0 * service.delete(_)
        '/phoneCall/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(phoneCall.id)
        '/phoneCall/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete phoneCall.id.toString()

        then: 'the instance is deleted'
        1 * service.delete(phoneCall.id) >> phoneCall
        '/phoneCall/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an LDAP service'
        response.reset()
        controller.delete phoneCall.id.toString()

        then: 'the instance is deleted in LDAP, too'
        1 * service.delete(phoneCall.id) >> phoneCall
        '/phoneCall/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        PhoneCall phoneCall = instance
        phoneCall.id = new ObjectId()

        and: 'a service instance'
        PhoneCallService service = Mock()
        controller.phoneCallService = service

        when: 'the action is executed with a null domain'
        controller.edit null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.edit new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(phoneCall.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit phoneCall.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(phoneCall.id) >> phoneCall
        phoneCall == model.phoneCall
    }

    void 'The index action returns the correct model'() {
        given: 'a list of phone calls'
        def list = [
            new PhoneCall(subject: 'Call 1'),
            new PhoneCall(subject: 'Call 2'),
            new PhoneCall(subject: 'Call 3'),
        ]

        and: 'a service instance'
        PhoneCallService service = Mock()
        1 * service.count() >> list.size()
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        controller.phoneCallService = service

        when: 'the action is executed without list type'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.phoneCallList.size()
        list == (List) model.phoneCallList
        list.size() == model.phoneCallCount
    }

    void 'The index action with letter returns the correct model'() {
        given: 'a list of phone calls'
        def list = [
            new PhoneCall(subject: 'Call 1'),
            new PhoneCall(subject: 'Call 2'),
            new PhoneCall(subject: 'Call 3'),
        ]

        and: 'a service instance'
        PhoneCallService service = Mock()
        2 * service.countBySubjectLessThan('E') >>> [45, 40]
        2 * service.count() >> list.size()
        2 * service.list(getParameterMap(
            letter: 'E', max: 10, offset: 40, sort: 'subject'
        )) >> list
        controller.phoneCallService = service

        when: 'the action is executed without search parameter'
        params.letter = 'E'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.phoneCallList.size()
        list == (List) model.phoneCallList
        list.size() == model.phoneCallCount

        when: 'the action is executed with search parameter'
        response.reset()
        params.letter = 'E'
        params.max = 10
        params.offset = 20
        params.search = 'foobar'
        controller.index()

        then: 'the model is correct'
        list.size() == model.phoneCallList.size()
        list == (List) model.phoneCallList
        list.size() == model.phoneCallCount
    }

    void 'The index action with search term returns the correct model'() {
        given: 'a list of phone calls'
        def list = [
            new PhoneCall(subject: 'Call 1'),
            new PhoneCall(subject: 'Call 2'),
            new PhoneCall(subject: 'Call 3'),
        ]

        and: 'a service instance'
        PhoneCallService service = Mock()
        1 * service.countBySubjectLike('%all%') >> list.size()
        1 * service.findAllBySubjectLike(
            '%all%', getParameterMap(max: 10, offset: 20, search: 'all')
        ) >> list
        controller.phoneCallService = service

        when: 'the action is executed without list type'
        params.max = 10
        params.offset = 20
        params.search = 'all'
        controller.index()

        then: 'the model is correct'
        list.size() == model.phoneCallList.size()
        list == (List) model.phoneCallList
        list.size() == model.phoneCallCount
    }

    void 'The listEmbedded action returns the correct model'() {
        given: 'a list of phone calls'
        def list = [
            new PhoneCall(subject: 'Call 1'),
            new PhoneCall(subject: 'Call 2'),
            new PhoneCall(subject: 'Call 3'),
        ]

        and: 'an organization'
        Organization org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a person'
        Person person = new Person(firstName: 'John', lastName: 'Doe')
        person.id = new ObjectId()

        and: 'a service instance'
        PhoneCallService service = Mock()
        controller.phoneCallService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        and: 'a person service instance'
        PersonService personService = Mock()
        controller.personService = personService

        when: 'the action is called for a null instance'
        controller.listEmbedded null, null

        then: 'a 404 error is returned'
        0 * organizationService.get(_)
        0 * personService.get(_)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
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
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        list == (List) model.phoneCallList
        list.size() == model.phoneCallCount
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
        1 * personService.get(person.id) >> person
        0 * service.findAllByOrganization(_)
        0 * service.countByOrganization(_)
        1 * service.findAllByPerson(
            person, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByPerson(person) >> list.size()
        list == (List) model.phoneCallList
        list.size() == model.phoneCallCount
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
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        list == (List) model.phoneCallList
        list.size() == model.phoneCallCount
        [organization: org.id.toString()] == (Map) model.linkParams
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        PhoneCall phoneCall = instance
        phoneCall.id = new ObjectId()

        and: 'a service instance'
        PhoneCallService service = Mock()
        controller.phoneCallService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save null

        then: 'a 404 error is returned'
        0 * service.save(_)
        '/phoneCall/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save phoneCall

        then: 'the create view is rendered again with the correct model'
        1 * service.save(phoneCall) >> {
            throw new ValidationException('', new ValidationErrors(phoneCall))
        }
        phoneCall == model.phoneCall
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save phoneCall

        then: 'a redirect is issued to the show action'
        1 * service.save(phoneCall) >> phoneCall
        '/phoneCall/show/' + phoneCall.id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The show action returns the correct model'() {
        given: 'an instance'
        PhoneCall phoneCall = instance
        phoneCall.id = new ObjectId()

        and: 'a service instance'
        PhoneCallService service = Mock()
        controller.phoneCallService = service

        when: 'the action is executed with a null domain'
        controller.show null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.show new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(phoneCall.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show phoneCall.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(phoneCall.id) >> phoneCall
        phoneCall == model.phoneCall
    }

    void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        PhoneCall phoneCall = instance
        phoneCall.id = new ObjectId()

        and: 'a service instance'
        PhoneCallService service = Mock()
        controller.phoneCallService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update null

        then: 'a 404 error is returned'
        0 * service.save(_)
        '/phoneCall/index' == response.redirectedUrl
        null != flash.message

        when: 'an invalid domain instance is passed to the action'
        response.reset()
        controller.update phoneCall

        then: 'the edit view is rendered again with the invalid instance'
        1 * service.save(phoneCall) >> {
            throw new ValidationException('', new ValidationErrors(phoneCall))
        }
        phoneCall == model.phoneCall
        'edit' == view

        when: 'a valid domain instance is passed to the action'
        response.reset()
        controller.update phoneCall

        then: 'a redirect is issued to the show action'
        1 * service.save(phoneCall) >> phoneCall
        '/phoneCall/show/' + phoneCall.id == response.redirectedUrl
        null != controller.flash.message
    }


    //-- Non-public methods -------------------------

    private static PhoneCall getInstance() {
        Map properties = [: ]
        populateValidParams properties

        new PhoneCall(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params) {
        assert params != null

        params.phone = '+1 47 304503033'
        params.status = PhoneCallStatus.COMPLETED
        params.subject = 'My last phone call'
        params.type = PhoneCallType.OUTGOING
    }
}
