/*
 * RoleGroupControllerSpec.groovy
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


class RoleGroupControllerSpec extends Specification
    implements ControllerUnitTest<RoleGroupController>,
        DomainUnitTest<RoleGroup>
{

    //-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        RoleGroup group = instance

        when: 'the action is executed'
        controller.copy group

        then: 'the model is correctly created'
        null != model.roleGroup
        group.name == model.roleGroup.name
        //noinspection GroovyAssignabilityCheck
        group.authorities == model.roleGroup.authorities

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.roleGroup

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.roleGroup
        params.name == model.roleGroup.name
        //noinspection GroovyAssignabilityCheck
        params.authorities == model.roleGroup.authorities
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        RoleGroup roleGroup = instance
        roleGroup.id = new ObjectId()

        and: 'a service instance'
        RoleGroupService service = Mock()
        service.delete(roleGroup.id) >> roleGroup
        controller.roleGroupService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        webRequest.actionName = 'delete'
        controller.delete null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.delete(_)
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(roleGroup.id)
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete roleGroup.id.toString()

        then: 'the instance is deleted'
        //noinspection GroovyAssignabilityCheck
        1 * service.delete(roleGroup.id) >> roleGroup
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        RoleGroup roleGroup = instance
        roleGroup.id = new ObjectId()

        and: 'a service instance'
        RoleGroupService service = Mock()
        controller.roleGroupService = service

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
        0 * service.get(roleGroup.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit roleGroup.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(roleGroup.id) >> roleGroup
        roleGroup == model.roleGroup
    }

    void 'The index action returns the correct model'() {
        given: 'a list of groups'
        def list = [
            new RoleGroup(name: 'Group 1'),
            new RoleGroup(name: 'Group 2'),
            new RoleGroup(name: 'Group 3'),
        ]

        and: 'a service instance'
        RoleGroupService service = Mock()
        1 * service.count() >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        controller.roleGroupService = service

        when: 'the action is executed without list type'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.roleGroupList.size()
        list == (List) model.roleGroupList
        list.size() == model.roleGroupCount
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        RoleGroup roleGroup = instance
        roleGroup.id = new ObjectId()

        and: 'a service instance'
        RoleGroupService service = Mock()
        controller.roleGroupService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        webRequest.actionName = 'save'
        controller.save null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save roleGroup

        then: 'the create view is rendered again with the correct model'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> {
            throw new ValidationException('', new ValidationErrors(roleGroup))
        }
        roleGroup == model.roleGroup
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save roleGroup

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        '/roleGroup/edit/' + roleGroup.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        controller.save roleGroup

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        //noinspection SpellCheckingInspection
        '/roleGroup/edit/' + roleGroup.id +
            '?returnUrl=%2Finvoice%2Fshow%2F12345' ==
                response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        params.remove 'returnUrl'
        params.close = 1
        controller.save roleGroup

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        '/roleGroup/show/' + roleGroup.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.save roleGroup

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The show action returns the correct model'() {
        given: 'an instance'
        RoleGroup roleGroup = instance
        roleGroup.id = new ObjectId()

        and: 'a service instance'
        RoleGroupService service = Mock()
        controller.roleGroupService = service

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
        0 * service.get(roleGroup.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show roleGroup.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(roleGroup.id) >> roleGroup
        roleGroup == model.roleGroup
    }

    void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        RoleGroup roleGroup = instance
        roleGroup.id = new ObjectId()

        and: 'a service instance'
        RoleGroupService service = Mock()
        controller.roleGroupService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        webRequest.actionName = 'update'
        controller.update null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message

        when: 'an invalid domain instance is passed to the action'
        response.reset()
        controller.update roleGroup

        then: 'the edit view is rendered again with the invalid instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> {
            throw new ValidationException('', new ValidationErrors(roleGroup))
        }
        roleGroup == model.roleGroup
        'edit' == view

        when: 'a valid domain instance is passed to the action'
        response.reset()
        controller.update roleGroup

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        '/roleGroup/edit/' + roleGroup.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        controller.update roleGroup

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        //noinspection SpellCheckingInspection
        '/roleGroup/edit/' + roleGroup.id +
            '?returnUrl=%2Finvoice%2Fshow%2F12345' ==
                response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        params.remove 'returnUrl'
        params.close = 1
        controller.update roleGroup

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        '/roleGroup/show/' + roleGroup.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.update roleGroup

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.save(roleGroup) >> roleGroup
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
    }


    //-- Non-public methods -------------------------

    private static RoleGroup getInstance() {
        Map properties = [: ]
        populateValidParams properties

        new RoleGroup(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params) {
        assert params != null

        params.authorities = [new Role(authority: 'ROLE_ADMIN')] as Set
        params.name = 'Administrators'
    }
}






