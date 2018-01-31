/*
 * OrganizationControllerSpec.groovy
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

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationErrors
import grails.validation.ValidationException
import grails.web.servlet.mvc.GrailsParameterMap
import org.bson.types.ObjectId
import spock.lang.Specification


class OrganizationControllerSpec extends Specification
	implements ControllerUnitTest<OrganizationController>, DataTest
{

    //-- Fixture methods ------------------------

    void setup() {
        mockDomains Config, Organization
    }


	//-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        Organization org = instance

        when: 'the action is executed'
        controller.copy org

        then: 'the model is correctly created'
        null != model.organization
        ((byte) 1) == model.organization.recType
        'My Organization ltd.' == model.organization.name
        0i == model.organization.number
        '+1 47 304503033' == model.organization.phone

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.organization

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.organization
        ((byte) 1) == model.organization.recType
        'My Organization ltd.' == model.organization.name
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()

        and: 'a service instance'
        OrganizationService service = Mock()
        service.delete(org.id) >> org
        controller.organizationService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete null

        then: 'a 404 error is returned'
        0 * service.delete(_)
        '/organization/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(org.id)
        '/organization/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete org.id.toString()

        then: 'the instance is deleted'
        1 * service.delete(org.id) >> org
        '/organization/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        when: 'the action is executed with a null domain'
        controller.edit null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.edit new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(org.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit org.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(org.id) >> org
        org == model.organization
    }

    void 'The find action without type returns the correct model'() {
        given: 'a list of organizations'
        def list = [
            new Organization(name: 'Organization 1'),
            new Organization(name: 'Organization 2'),
            new Organization(name: 'Organization 3'),
        ]

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        when: 'the action is executed with a null value and an invalid name'
        params.name = 'xxx'
        controller.find((Byte) null)

        then: 'the model is correct'
        1 * service.findAllByNameLike(
            '%xxx%', getParameterMap(sort: 'name')
        ) >> []
        null == model.organizationList
        model.emptyCollection.empty

        when: 'the action is executed with a value of 0 and an invalid name'
        response.reset()
        params.name = 'xxx'
        controller.find((Byte) 0)

        then: 'the model is correct'
        1 * service.findAllByNameLike(
            '%xxx%', getParameterMap(sort: 'name')
        ) >> []
        null == model.organizationList
        model.emptyCollection.empty

        when: 'the action is executed with a null value'
        response.reset()
        params.name = 'niza'
        controller.find((Byte) null)

        then: 'the model is correct'
        1 * service.findAllByNameLike(
            '%niza%', getParameterMap(sort: 'name')
        ) >> list
        list.size() == model.organizationList.size()
        list == (List) model.organizationList

        when: 'the action is executed with a value of 0'
        response.reset()
        params.name = 'niza'
        controller.find((Byte) 0)

        then: 'the model is correct'
        1 * service.findAllByNameLike(
            '%niza%', getParameterMap(sort: 'name')
        ) >> list
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
    }

    void 'The get action returns the correct model'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        when: 'the action is executed with a null domain'
        controller.get null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.get new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(org.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.get org.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(org.id) >> org
        org == model.organization
    }

    void 'The getPhoneNumbers action returns the correct models'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()
        org.phoneOther = '+1 47 304503034'
        org.fax = '+1 47 304503039'

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        when: 'the action is executed with a null domain'
        controller.getPhoneNumbers null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.getPhoneNumbers new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(org.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.getPhoneNumbers org.id.toString()

        then: 'the model is correct'
        1 * service.get(org.id) >> org
        ['+1 47 304503033', '+1 47 304503034', '+1 47 304503039'] == model.phoneNumbers

        when: 'some phone numbers are unset'
        org.phoneOther = null
        response.reset()
        controller.getPhoneNumbers org.id.toString()

        then: 'the model is correct'
        1 * service.get(org.id) >> org
        ['+1 47 304503033', '+1 47 304503039'] == model.phoneNumbers

        when: 'some phone numbers are set to an empty string'
        org.phoneOther = ''
        response.reset()
        controller.getPhoneNumbers org.id.toString()

        then: 'the model is correct'
        1 * service.get(org.id) >> org
        ['+1 47 304503033', '+1 47 304503039'] == model.phoneNumbers

        when: 'some phone numbers are set to a duplicate'
        org.phoneOther = org.phone
        response.reset()
        controller.getPhoneNumbers org.id.toString()

        then: 'the model is correct'
        1 * service.get(org.id) >> org
        ['+1 47 304503033', '+1 47 304503039'] == model.phoneNumbers
    }

    void 'The getTermOfPayment action returns the correct model'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        controller.configService = configService

        when: 'the action is executed without a configuration'
        controller.getTermOfPayment null

        then: 'the model is correct'
        1 * configService.getInteger('termOfPayment') >> null
        0 * service.get(_)
        14i == model.termOfPayment

        when: 'the action is executed with a null domain'
        controller.getTermOfPayment null

        then: 'the model is correct'
        1 * configService.getInteger('termOfPayment') >> 28i
        0 * service.get(_)
        28i == model.termOfPayment

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.getTermOfPayment new ObjectId().toString()

        then: 'the model is correct'
        1 * configService.getInteger('termOfPayment') >> 28i
        0 * service.get(org.id)
        28i == model.termOfPayment

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.getTermOfPayment org.id.toString()

        then: 'the model is correct'
        1 * configService.getInteger('termOfPayment') >> 28i
        1 * service.get(org.id) >> org
        28i == model.termOfPayment

        when: 'the action is executed with a term of payment value'
        org.termOfPayment = 21i
        response.reset()
        controller.getTermOfPayment org.id.toString()

        then: 'the model is correct'
        1 * configService.getInteger('termOfPayment') >> 28i
        1 * service.get(org.id) >> org
        21i == model.termOfPayment
    }

    void 'The index action returns the correct model'() {
		given: 'a list of organizations'
        def list = [
            new Organization(name: 'Organization 1'),
            new Organization(name: 'Organization 2'),
            new Organization(name: 'Organization 3'),
        ]

        and: 'a service instance'
        OrganizationService service = Mock()
        2 * service.count() >> list.size()
        2 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        controller.organizationService = service

		when: 'the action is executed without list type'
        params.max = 10
        params.offset = 20
		controller.index()

		then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount

		when: 'the action is executed with general list type'
        params.max = 10
        params.offset = 20
		controller.index((byte) 0)

		then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount
	}

    void 'The index action with type 1 returns the correct model'() {
		given: 'a list of organizations'
        def list = [
            new Organization(name: 'Organization 1'),
            new Organization(name: 'Organization 2'),
            new Organization(name: 'Organization 3'),
        ]

        and: 'a service instance'
        def types = [(byte) 1, (byte) 3] as Set<Byte>
        OrganizationService service = Mock()
        1 * service.countByRecTypeInList(types) >> list.size()
        1 * service.findAllByRecTypeInList(
            types, getParameterMap(max: 15, offset: 40)
        ) >> list
        controller.organizationService = service

		when: 'the action is executed with list type 1'
        params.max = 15
        params.offset = 40
		controller.index((byte) 1)

		then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount
	}

    void 'The index action with type 2 returns the correct model'() {
		given: 'a list of organizations'
        def list = [
            new Organization(name: 'Organization 1'),
            new Organization(name: 'Organization 2'),
            new Organization(name: 'Organization 3'),
        ]

        and: 'a service instance'
        def types = [(byte) 2, (byte) 3] as Set<Byte>
        OrganizationService service = Mock()
        1 * service.countByRecTypeInList(types) >> list.size()
        1 * service.findAllByRecTypeInList(
            types, getParameterMap(max: 15, offset: 40)
        ) >> list
        controller.organizationService = service

		when: 'the action is executed with list type 2'
        params.max = 15
        params.offset = 40
		controller.index((byte) 2)

		then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount
	}

    void 'The index action with letter returns the correct model'() {
        given: 'a list of organizations'
        def list = [
            new Organization(name: 'Organization 1'),
            new Organization(name: 'Organization 2'),
            new Organization(name: 'Organization 3'),
        ]

        and: 'a service instance'
        OrganizationService service = Mock()
        2 * service.countByNameLessThan('E') >>> [45, 40]
        2 * service.count() >> list.size()
        2 * service.list(
            getParameterMap(letter: 'E', max: 10, offset: 40, sort: 'name')
        ) >> list
        controller.organizationService = service

        when: 'the action is executed without list type'
        params.letter = 'E'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount

        when: 'the action is executed with general list type'
        params.max = 10
        params.offset = 20
        controller.index((byte) 0)

        then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount
    }

    void 'The index action with letter and type 1 returns the correct model'() {
        given: 'a list of organizations'
        def list = [
            new Organization(name: 'Organization 1'),
            new Organization(name: 'Organization 2'),
            new Organization(name: 'Organization 3'),
        ]

        and: 'a service instance'
        def types = [(byte) 1, (byte) 3] as Set<Byte>
        OrganizationService service = Mock()
        2 * service.countByNameLessThanAndRecTypeInList('E', types) >>> [45, 40]
        2 * service.countByRecTypeInList(types) >> list.size()
        2 * service.findAllByRecTypeInList(
            types,
            getParameterMap(letter: 'E', max: 10, offset: 40, sort: 'name')
        ) >> list
        controller.organizationService = service

        when: 'the action is executed with list type 1'
        params.letter = 'E'
        params.max = 10
        params.offset = 60
        controller.index((byte) 1)

        then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount

        when: 'the action is executed again with list type 1'
        params.max = 10
        params.offset = 60
        controller.index((byte) 1)

        then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount
    }

    void 'The index action with letter and type 2 returns the correct model'() {
        given: 'a list of organizations'
        def list = [
            new Organization(name: 'Organization 1'),
            new Organization(name: 'Organization 2'),
            new Organization(name: 'Organization 3'),
        ]

        and: 'a service instance'
        def types = [(byte) 2, (byte) 3] as Set<Byte>
        OrganizationService service = Mock()
        2 * service.countByNameLessThanAndRecTypeInList('E', types) >>> [45, 40]
        2 * service.countByRecTypeInList(types) >> list.size()
        2 * service.findAllByRecTypeInList(
            types,
            getParameterMap(letter: 'E', max: 10, offset: 40, sort: 'name')
        ) >> list
        controller.organizationService = service

        when: 'the action is executed with list type 2'
        params.letter = 'E'
        params.max = 10
        params.offset = 60
        controller.index((byte) 2)

        then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount

        when: 'the action is executed again with list type 2'
        params.max = 10
        params.offset = 60
        controller.index((byte) 2)

        then: 'the model is correct'
        list.size() == model.organizationList.size()
        list == (List) model.organizationList
        list.size() == model.organizationCount
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save null

        then: 'a 404 error is returned'
        0 * service.save(_)
        '/organization/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save org

        then: 'the create view is rendered again with the correct model'
        1 * service.save(org) >> {
            throw new ValidationException('', new ValidationErrors(org))
        }
        org == model.organization
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save org

        then: 'a redirect is issued to the show action'
        1 * service.save(org) >> org
        '/organization/show/' + org.id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The show action returns the correct model'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        when: 'the action is executed with a null domain'
        controller.show null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.show new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(org.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show org.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(org.id) >> org
        org == model.organization
    }

	void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        Organization org = instance
        org.id = new ObjectId()

        and: 'a service instance'
        OrganizationService service = Mock()
        controller.organizationService = service

        when: 'the action is called for a null instance'
		request.contentType = FORM_CONTENT_TYPE
		request.method = 'PUT'
		controller.update null

		then: 'a 404 error is returned'
        0 * service.save(_)
        '/organization/index' == response.redirectedUrl
        null != flash.message

		when: 'an invalid domain instance is passed to the update action'
		response.reset()
		controller.update org

		then: 'the edit view is rendered again with the invalid instance'
        1 * service.save(org) >> {
            throw new ValidationException('', new ValidationErrors(org))
        }
        org == model.organization
        'edit' == view

		when: 'a valid domain instance is passed to the update action'
		response.reset()
		controller.update org

		then: 'a redirect is issued to the show action'
        1 * service.save(org) >> org
        '/organization/show/' + org.id == response.redirectedUrl
        null != controller.flash.message
	}


    //-- Non-public methods -------------------------

    private static Organization getInstance() {
        Map properties = [: ]
        populateValidParams properties, 45000i

        new Organization(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params, Integer number = null) {
        assert params != null

        params.name = 'My Organization ltd.'
        params.number = number
        params.phone = '+1 47 304503033'
        params.recType = (byte) 1
    }
}
