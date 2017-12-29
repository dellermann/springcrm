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
import grails.validation.ValidationException
import org.bson.types.ObjectId
import spock.lang.*


class RoleGroupControllerSpec extends Specification
    implements ControllerUnitTest<RoleGroupController>,
        DomainUnitTest<RoleGroup>
{

    //-- Feature methods ------------------------

    def 'The index action returns the correct model'() {
        given:
        def rg = new RoleGroup(name: 'Administrators')

        and:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * list(_) >> [rg]
            1 * count() >> 1
        }

        when: 'the index action is executed'
        controller.index()

        then: 'the model is correct'
        null != model.roleGroupList
        1 == model.roleGroupList.size()
        rg == model.roleGroupList.first()
        1 == model.roleGroupCount
    }

    def 'The create action returns the correct model'() {
        when: 'the create action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.roleGroup
    }

    def 'The save action can handle a null instance'() {
        when: 'save is called for a domain instance that does not exist'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save null

        then: 'a 404 error is returned'
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message
    }

    def 'The save action can persist correctly'() {
        given:
        def id = new ObjectId()

        and:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * save(_ as RoleGroup)
        }

        when: 'the save action is executed with a valid instance'
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def roleGroup = new RoleGroup(params)
        roleGroup.id = id

        controller.save roleGroup

        then: 'a redirect is issued to the show action'
        '/roleGroup/show/' + id == response.redirectedUrl
        null != controller.flash.message
    }

    def 'The save action can handle an invalid instance'() {
        given:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * save(_ as RoleGroup) >> { RoleGroup roleGroup ->
                throw new ValidationException(
                    'Invalid instance', roleGroup.errors
                )
            }
        }

        when: 'the save action is executed with an invalid instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def roleGroup = new RoleGroup()
        controller.save(roleGroup)

        then: 'the create view is rendered again with the correct model'
        null != model.roleGroup
        'create' == view
    }

    def 'The show action can handle a null id'() {
        given:
        controller.roleGroupService = Mock(RoleGroupService) {
            0 * get(null)
        }

        when: 'the show action is executed with a null domain'
        controller.show null

        then: 'a 404 error is returned'
        response.status == 404
    }

    def 'The show action can handle a valid id'() {
        given:
        def id = new ObjectId()

        and:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * get(id) >> new RoleGroup()
        }

        when: 'a domain instance is passed to the show action'
        controller.show id.toString()

        then: 'a model is populated containing the domain instance'
        model.roleGroup instanceof RoleGroup
    }

    def 'The edit action can handle a null id'() {
        given:
        controller.roleGroupService = Mock(RoleGroupService) {
            0 * get(null)
        }

        when: 'the show action is executed with a null domain'
        controller.edit null

        then: 'a 404 error is returned'
        response.status == 404
    }

    def 'The edit action can handle a valid id'() {
        given:
        def id = new ObjectId()

        and:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * get(id) >> new RoleGroup()
        }

        when: 'a domain instance is passed to the show action'
        controller.edit id.toString()

        then: 'a model is populated containing the domain instance'
        model.roleGroup instanceof RoleGroup
    }

    def 'The update action can handle a null instance'() {
        when: 'save is called for a domain instance that doesn\'t exist'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update null

        then: 'a 404 error is returned'
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message
    }

    def 'The update action can persist correctly'() {
        given:
        def id = new ObjectId()

        and:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * save(_ as RoleGroup)
        }

        when: 'the save action is executed with a valid instance'
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def roleGroup = new RoleGroup(params)
        roleGroup.id = id

        controller.update roleGroup

        then: 'a redirect is issued to the show action'
        '/roleGroup/show/' + id == response.redirectedUrl
        null != controller.flash.message
    }

    def 'The update action can handle an invalid instance'() {
        given:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * save(_ as RoleGroup) >> { RoleGroup roleGroup ->
                throw new ValidationException(
                    'Invalid instance', roleGroup.errors
                )
            }
        }

        when: 'the save action is executed with an invalid instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update new RoleGroup()

        then: 'the edit view is rendered again with the correct model'
        null != model.roleGroup
        'edit' == view
    }

    def 'The delete action can handle a null instance'() {
        when: 'the delete action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete null

        then: 'a 404 is returned'
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message
    }

    def 'The delete action can handle an instance'() {
        given:
        def id = new ObjectId()

        and:
        controller.roleGroupService = Mock(RoleGroupService) {
            1 * delete(id)
        }

        when: 'the domain instance is passed to the delete action'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete id.toString()

        then: 'the user is redirected to index'
        '/roleGroup/index' == response.redirectedUrl
        null != flash.message
    }


    //-- Non-public methods ---------------------

    private static void populateValidParams(params) {
        assert params != null

        params.name = 'Administrators'
        params.authorities = [new Role(authority: 'ROLE_ADMIN')]
    }
}






