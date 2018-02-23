/*
 * UserControllerSpec.groovy
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


class UserControllerSpec extends Specification
	implements ControllerUnitTest<UserController>, DomainUnitTest<User>
{

	//-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an organization'
        User user = instance

        when: 'the action is executed'
        controller.copy user

        then: 'the model is correctly created'
        null != model.user
        //noinspection SpellCheckingInspection
        'jdoe' == model.user.username
        'John' == model.user.firstName
        'Doe' == model.user.lastName
        'j.doe@example.org' == model.user.email

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.user

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.user
        //noinspection SpellCheckingInspection
        'jdoe' == model.user.username
        'John' == model.user.firstName
        'Doe' == model.user.lastName
        'j.doe@example.org' == model.user.email
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        User user = instance
        user.id = new ObjectId()

        and: 'a service instance'
        UserService service = Mock()
        service.delete(user.id) >> user
        controller.userService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        webRequest.actionName = 'delete'
        controller.delete null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.delete(_)
        '/user/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(user.id)
        '/user/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete user.id.toString()

        then: 'the instance is deleted'
        //noinspection GroovyAssignabilityCheck
        1 * service.delete(user.id) >> user
        '/user/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        User user = instance
        user.id = new ObjectId()

        and: 'a service instance'
        UserService service = Mock()
        controller.userService = service

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
        0 * service.get(user.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit user.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        user == model.user
    }

    void 'The index action returns the correct model'() {
		given: 'a list of users'
        def list = [
            new User(username: 'user1'),
            new User(username: 'user2'),
            new User(username: 'user3'),
        ]

        and: 'a service instance'
        UserService service = Mock()
        1 * service.count() >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        controller.userService = service

		when: 'the action is executed'
        params.max = 10
        params.offset = 20
		controller.index()

		then: 'the model is correct'
        list.size() == model.userList.size()
        list == (List) model.userList
        list.size() == model.userCount
	}

    void 'The index action with letter returns the correct model'() {
        given: 'a list of users'
        def list = [
            new User(username: 'user1'),
            new User(username: 'user2'),
            new User(username: 'user3'),
        ]

        and: 'a service instance'
        UserService service = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * service.countByUsernameLessThan('E') >>> [45, 40]
        1 * service.count() >> list.size()
        //noinspection GroovyAssignabilityCheck
        1 * service.list(
            getParameterMap(letter: 'E', max: 10, offset: 40, sort: 'username')
        ) >> list
        controller.userService = service

        when: 'the action is executed'
        params.letter = 'E'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.userList.size()
        list == (List) model.userList
        list.size() == model.userCount
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        User user = instance
        user.id = new ObjectId()

        and: 'a service instance'
        UserService service = Mock()
        controller.userService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        webRequest.actionName = 'save'
        controller.save null

        then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.encodePassword(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/user/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed without a password'
        response.reset()
        controller.save user

        then: 'the create view is rendered again with the correct model'
        0 * service.encodePassword(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save({ it.errors.hasFieldErrors('password') }) >> {
            throw new ValidationException('', new ValidationErrors(user))
        }
        user == model.user
        'create' == view

        when: 'the action is executed with a blank password'
        response.reset()
        user.password = ''
        controller.save user

        then: 'the create view is rendered again with the correct model'
        0 * service.encodePassword(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save({ it.errors.hasFieldErrors('password') }) >> {
            throw new ValidationException('', new ValidationErrors(user))
        }
        user == model.user
        'create' == view

        when: 'the action is executed with a wrong repeated password'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdee'
        controller.save user

        then: 'the create view is rendered again with the correct model'
        0 * service.encodePassword(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save({ it.errors.hasFieldErrors('password') }) >> {
            throw new ValidationException('', new ValidationErrors(user))
        }
        user == model.user
        'create' == view

        when: 'the action is executed with an invalid instance'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        controller.save user

        then: 'the create view is rendered again with the correct model'
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'jdoe' && it.password == 'encrypted' &&
                it.firstName == 'John' && it.lastName == 'Doe'
        }) >> {
            throw new ValidationException('', new ValidationErrors(user))
        }
        user == model.user
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        controller.save user

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'jdoe' && it.password == 'encrypted' &&
                it.firstName == 'John' && it.lastName == 'Doe'
        }) >> user
        '/user/edit/' + user.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        params.returnUrl = '/invoice/show/12345'
        controller.save user

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'jdoe' && it.password == 'encrypted' &&
                it.firstName == 'John' && it.lastName == 'Doe'
        }) >> user
        //noinspection SpellCheckingInspection
        '/user/edit/' + user.id + '?returnUrl=%2Finvoice%2Fshow%2F12345' ==
            response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        params.remove 'returnUrl'
        params.close = 1
        controller.save user

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'jdoe' && it.password == 'encrypted' &&
                it.firstName == 'John' && it.lastName == 'Doe'
        }) >> user
        '/user/show/' + user.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.save user

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'jdoe' && it.password == 'encrypted' &&
                it.firstName == 'John' && it.lastName == 'Doe'
        }) >> user
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
    }

    // TODO settingsControl
    // TODO settingsControlSave
    // TODO settingsGoogleAuth
    // TODO settingsGoogleAuthRequest
    // TODO settingsGoogleAuthResponse
    // TODO settingsGoogleAuthRevoke
    // TODO settingsLanguage
    // TODO settingsLanguageSave
    // TODO settingsSync
    // TODO settingsSyncSave

    void 'The show action returns the correct model'() {
        given: 'an instance'
        User user = instance
        user.id = new ObjectId()

        and: 'a service instance'
        UserService service = Mock()
        controller.userService = service

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
        0 * service.get(user.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show user.id.toString()

        then: 'a model is populated containing the domain instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        user == model.user
    }

    // TODO storeSetting

	void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        User user = instance
        user.id = new ObjectId()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'

        and: 'a service instance'
        UserService service = Mock()
        controller.userService = service

        when: 'the action is called for a null instance'
		request.contentType = FORM_CONTENT_TYPE
		request.method = 'PUT'
        webRequest.actionName = 'update'
		controller.update null

		then: 'a 404 error is returned'
        //noinspection GroovyAssignabilityCheck
        0 * service.get(_)
        //noinspection GroovyAssignabilityCheck
        0 * service.save(_)
        '/user/index' == response.redirectedUrl
        null != flash.message

        when: 'update is called for a non-existing instance'
        response.reset()
		controller.update new ObjectId().toString()

		then: 'a 404 error is returned'
        0 * service.get(user.id)
        0 * service.save(_)
        '/user/index' == response.redirectedUrl
        null != flash.message

        when: 'no password is passed to the update action'
        response.reset()
        //noinspection SpellCheckingInspection
        params.username = 'bboo'
        params.firstName = 'Betty'
        params.lastName = 'Boo'
        controller.update user.id.toString()

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        //noinspection GroovyAssignabilityCheck
        0 * service.encodePassword(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'bboo' && it.password == 'abcdef' &&
                it.firstName == 'Betty' && it.lastName == 'Boo'
        }) >> user
        '/user/edit/' + user.id == response.redirectedUrl
        null != controller.flash.message

		when: 'an invalid domain instance is passed to the update action'
		response.reset()
		controller.update user.id.toString()

		then: 'the edit view is rendered again with the invalid instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        0 * service.encodePassword(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save(user) >> {
            throw new ValidationException('', new ValidationErrors(user))
        }
        user == model.user
        'edit' == view

		when: 'an wrong repeated password is passed to the update action'
		response.reset()
        //noinspection SpellCheckingInspection
        params.username = 'bboo'
        params.password = '123456'
        params.passwordRepeat = '123455'
        params.firstName = 'Betty'
        params.lastName = 'Boo'
		controller.update user.id.toString()

		then: 'the edit view is rendered again with the invalid instance'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        0 * service.encodePassword(_)
        //noinspection GroovyAssignabilityCheck
        1 * service.save({ it.errors.hasFieldErrors('password') }) >> {
            throw new ValidationException('', new ValidationErrors(user))
        }
        user == model.user
        'edit' == view

		when: 'a valid domain instance is passed to the update action'
		response.reset()
        //noinspection SpellCheckingInspection
        params.username = 'bboo'
        params.password = '123456'
        params.passwordRepeat = '123456'
        params.firstName = 'Betty'
        params.lastName = 'Boo'
		controller.update user.id.toString()

		then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        //noinspection GroovyAssignabilityCheck
        1 * service.encodePassword('123456') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'bboo' && it.password == 'encrypted' &&
            it.firstName == 'Betty' && it.lastName == 'Boo'
        }) >> user
        '/user/edit/' + user.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with a return URL'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        params.returnUrl = '/invoice/show/12345'
        controller.update user.id.toString()

        then: 'a redirect is issued to the edit action'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'bboo' && it.password == 'encrypted' &&
                it.firstName == 'Betty' && it.lastName == 'Boo'
        }) >> user
        //noinspection SpellCheckingInspection
        '/user/edit/' + user.id + '?returnUrl=%2Finvoice%2Fshow%2F12345' ==
            response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        params.remove 'returnUrl'
        params.close = 1
        controller.update user.id.toString()

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'bboo' && it.password == 'encrypted' &&
                it.firstName == 'Betty' && it.lastName == 'Boo'
        }) >> user
        '/user/show/' + user.id == response.redirectedUrl
        null != controller.flash.message

        when: 'the action is executed with the close flag and return URL'
        response.reset()
        //noinspection SpellCheckingInspection
        user.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.password = 'abcdef'
        //noinspection SpellCheckingInspection
        params.passwordRepeat = 'abcdef'
        params.returnUrl = '/invoice/show/12345'
        params.close = 1
        controller.update user.id.toString()

        then: 'a redirect is issued to the show action'
        //noinspection GroovyAssignabilityCheck
        1 * service.get(user.id) >> user
        //noinspection GroovyAssignabilityCheck,SpellCheckingInspection
        1 * service.encodePassword('abcdef') >> 'encrypted'
        //noinspection GroovyAssignabilityCheck
        1 * service.save({
            //noinspection SpellCheckingInspection
            it.username == 'bboo' && it.password == 'encrypted' &&
                it.firstName == 'Betty' && it.lastName == 'Boo'
        }) >> user
        '/invoice/show/12345' == response.redirectedUrl
        null != controller.flash.message
	}


    //-- Non-public methods -------------------------

    private static User getInstance() {
        Map properties = [: ]
        populateValidParams properties

        new User(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params) {
        assert params != null

        //noinspection SpellCheckingInspection
        params.username = 'jdoe'
        params.firstName = 'John'
        params.lastName = 'Doe'
        params.email = 'j.doe@example.org'
    }
}
