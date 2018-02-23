/*
 * WorkControllerSpec.groovy
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

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationErrors
import grails.validation.ValidationException
import grails.web.servlet.mvc.GrailsParameterMap
import org.bson.types.ObjectId
import spock.lang.Specification


class WorkControllerSpec extends Specification
    implements ControllerUnitTest<WorkController>, DomainUnitTest<Work>
{

    //-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        Work work = instance

        when: 'the action is executed'
        controller.copy work

        then: 'the model is correctly created'
        null != model.work
        work.name == model.work.name
        0i == model.work.number
        work.quantity == model.work.quantity
        work.unitPrice == model.work.unitPrice

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.work

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.work
        params.name == model.work.name
        params.quantity == model.work.quantity
        params.unitPrice == model.work.unitPrice
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        Work work = instance
        work.id = new ObjectId()

        and: 'a service instance'
        WorkService service = Mock()
        service.delete(work.id) >> work
        controller.workService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        webRequest.actionName = 'delete'
        controller.delete null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.delete(_)
        '/work/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(work.id)
        '/work/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete work.id.toString()

        then: 'the instance is deleted'
        //noinspection GroovyAssignabilityCheck
        1 * service.delete(work.id) >> work
        '/work/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        Work work = instance
        work.id = new ObjectId()

        and: 'a service instance'
        WorkService service = Mock()
        controller.workService = service

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
        0 * service.get(work.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit work.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(work.id) >> work
        work == model.work
    }

    // TODO find action

//    void 'The find action without type returns the correct model'() {
//        given: 'a list of services'
//        def list = [
//            new Person(lastName: 'Person 1'),
//            new Person(lastName: 'Person 2'),
//            new Person(lastName: 'Person 3'),
//        ]
//
//        and: 'an organization'
//        def org = new Organization(name: 'My organization ltd.')
//        org.id = new ObjectId()
//
//        and: 'a service instance'
//        PersonService service = Mock()
//        controller.personService = service
//
//        and: 'an organization service instance'
//        OrganizationService organizationService = Mock()
//        controller.organizationService = organizationService
//
//        when: 'the action is executed with a null value'
//        params.name = 'erso'
//        controller.find null
//
//        then: 'a 404 error is returned'
//        0 * organizationService.get(_)
//        1 * service.search(null, 'erso') >> null
//        404 == response.status
//
//        when: 'the action is executed with a non-existing ID'
//        response.reset()
//        params.name = 'erso'
//        controller.find new ObjectId().toString()
//
//        then: 'a 404 error is returned'
//        0 * organizationService.get(org.id)
//        1 * service.search(null, 'erso') >> null
//        404 == response.status
//
//        when: 'the action is executed with a null value and a null name'
//        response.reset()
//        params.name = null
//        controller.find null
//
//        then: 'a 404 error is returned'
//        0 * organizationService.get(_)
//        1 * service.search(null, null) >> null
//        404 == response.status
//
//        when: 'the action is executed with an existing ID and an invalid name'
//        response.reset()
//        params.name = 'xxx'
//        controller.find org.id.toString()
//
//        then: 'the model is correct'
//        1 * organizationService.get(_) >> org
//        1 * service.search(org, 'xxx') >> []
//        null == model.personList
//        model.emptyCollection.empty
//
//        when: 'the action is executed with an existing ID and a name'
//        response.reset()
//        params.name = 'erso'
//        controller.find org.id.toString()
//
//        then: 'the model is correct'
//        1 * organizationService.get(_) >> org
//        1 * service.search(org, 'erso') >> list
//        list.size() == model.personList.size()
//        list == (List) model.personList
//    }

    void 'The get action returns the correct model'() {
        given: 'an instance'
        Work work = instance
        work.id = new ObjectId()

        and: 'a service instance'
        WorkService service = Mock()
        controller.workService = service

        when: 'the action is executed with a null domain'
        controller.get null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.get new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(work.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.get work.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(work.id) >> work
        work == model.work
    }

    void 'The index action returns the correct model'() {
        given: 'a list of services'
        def list = [
            new Work(name: 'Service 1'),
            new Work(name: 'Service 2'),
            new Work(name: 'Service 3'),
        ]

        and: 'a service instance'
        WorkService service = Mock()
        1 * service.count() >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        controller.workService = service

        when: 'the action is executed'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.workList.size()
        list == (List) model.workList
        list.size() == model.workCount
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        Work work = instance
        work.id = new ObjectId()

        and: 'a service instance'
        WorkService service = Mock()
        controller.workService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        webRequest.actionName = 'save'
        controller.save null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/work/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save work

        then: 'the create view is rendered again with the correct model'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> {
            throw new ValidationException('', new ValidationErrors(work))
        }
        work == model.work
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save work

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        '/work/edit/' + work.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        controller.save work

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        //noinspection SpellCheckingInspection
        '/work/edit/' + work.id + '?returnUrl=%2Finvoice%2Fshow%2F12345' ==
            response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        params.remove 'returnUrl'
        params.close = 1
        controller.save work

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        '/work/show/' + work.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.save work

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
    }

    // TODO selectorList action

    void 'The show action returns the correct model'() {
        given: 'an instance'
        Work work = instance
        work.id = new ObjectId()

        and: 'a service instance'
        WorkService service = Mock()
        controller.workService = service

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
        0 * service.get(work.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show work.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(work.id) >> work
        work == model.work
    }

    void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        Work work = instance
        work.id = new ObjectId()

        and: 'a service instance'
        WorkService service = Mock()
        controller.workService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        webRequest.actionName = 'update'
        controller.update null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/work/index' == response.redirectedUrl
        null != flash.message

        when: 'an invalid domain instance is passed to the action'
        response.reset()
        controller.update work

        then: 'the edit view is rendered again with the invalid instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> {
            throw new ValidationException('', new ValidationErrors(work))
        }
        work == model.work
        'edit' == view

        when: 'a valid domain instance is passed to the action'
        response.reset()
        controller.update work

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        '/work/edit/' + work.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        controller.update work

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        //noinspection SpellCheckingInspection
        '/work/edit/' + work.id + '?returnUrl=%2Finvoice%2Fshow%2F12345' ==
            response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        params.remove 'returnUrl'
        params.close = 1
        controller.update work

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        '/work/show/' + work.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.update work

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(work) >> work
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
    }


    //-- Non-public methods -------------------------

    private static Work getInstance() {
        Map properties = [: ]
        populateValidParams properties, 45000i

        new Work(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params, Integer number = null) {
        assert params != null

        params.name = 'Groovy programming'
        params.number = number
        params.quantity = 1
        params.unitPrice = 130.0
    }
}
