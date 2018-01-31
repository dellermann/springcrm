/*
 * PersonControllerSpec.groovy
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
import org.amcworld.springcrm.google.GoogleContactSync
import org.bson.types.ObjectId
import org.grails.web.util.GrailsApplicationAttributes
import spock.lang.Specification


class PersonControllerSpec extends Specification
    implements ControllerUnitTest<PersonController>, DomainUnitTest<Person>
{

    //-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        Person person = instance

        when: 'the action is executed'
        controller.copy person

        then: 'the model is correctly created'
        null != model.person
        'John' == model.person.firstName
        'Doe' == model.person.lastName
        0i == model.person.number
        '+1 47 304503033' == model.person.phone
        'j.doe@example.com' == model.person.email1
        'info@example.com' == model.person.email2

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.person

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.person
        'John' == model.person.firstName
        'Doe' == model.person.lastName
        '+1 47 304503033' == model.person.phone
        'j.doe@example.com' == model.person.email1
        'info@example.com' == model.person.email2
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        service.delete(person.id) >> person
        controller.personService = service

        and: 'an LDAP service instance'
        LdapService ldapService = Mock()

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete null

        then: 'a 404 error is returned'
        0 * service.delete(_)
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(person.id)
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete person.id.toString()

        then: 'the instance is deleted'
        1 * service.delete(person.id) >> person
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an LDAP service'
        controller.ldapService = ldapService
        response.reset()
        controller.delete person.id.toString()

        then: 'the instance is deleted in LDAP, too'
        1 * service.delete(person.id) >> person
        1 * ldapService.delete(person)
        '/person/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        when: 'the action is executed with a null domain'
        controller.edit null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.edit new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(person.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit person.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(person.id) >> person
        person == model.person
    }

    void 'The find action without type returns the correct model'() {
        given: 'a list of persons'
        def list = [
            new Person(lastName: 'Person 1'),
            new Person(lastName: 'Person 2'),
            new Person(lastName: 'Person 3'),
        ]

        and: 'an organization'
        def org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        when: 'the action is executed with a null value'
        params.name = 'erso'
        controller.find null

        then: 'a 404 error is returned'
        0 * organizationService.get(_)
        1 * service.search(null, 'erso') >> null
        404 == response.status

        when: 'the action is executed with a non-existing ID'
        response.reset()
        params.name = 'erso'
        controller.find new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * organizationService.get(org.id)
        1 * service.search(null, 'erso') >> null
        404 == response.status

        when: 'the action is executed with a null value and a null name'
        response.reset()
        params.name = null
        controller.find null

        then: 'a 404 error is returned'
        0 * organizationService.get(_)
        1 * service.search(null, null) >> null
        404 == response.status

        when: 'the action is executed with an existing ID and an invalid name'
        response.reset()
        params.name = 'xxx'
        controller.find org.id.toString()

        then: 'the model is correct'
        1 * organizationService.get(_) >> org
        1 * service.search(org, 'xxx') >> []
        null == model.personList
        model.emptyCollection.empty

        when: 'the action is executed with an existing ID and a name'
        response.reset()
        params.name = 'erso'
        controller.find org.id.toString()

        then: 'the model is correct'
        1 * organizationService.get(_) >> org
        1 * service.search(org, 'erso') >> list
        list.size() == model.personList.size()
        list == (List) model.personList
    }

    void 'The gdatasync action correctly redirects'() {
        given: 'a Google data synchronization instance'
        GoogleContactSync googleContactSync = Mock()

        and: 'a user service instance'
        UserService userService = Mock()
        controller.userService = userService

        and: 'a mocked user instance'
        User user = new User(
            username: 'jdoe', password: 'secret', firstName: 'John',
            lastName: 'Doe', 'email': 'j.doe@example.com'
        )

        when: 'the action is called without sync'
        controller.gdatasync()

        then: 'the redirect is correctly set'
        0 * userService.getCurrentUser()
        0 * googleContactSync.sync(_)
        '/person/index' == response.redirectedUrl
        null == flash.message

        when: 'the action is called without sync and with return URL'
        response.reset()
        params.returnUrl = '/organization/show/abcdef012345'
        controller.gdatasync()

        then: 'the redirect is correctly set'
        0 * userService.getCurrentUser()
        0 * googleContactSync.sync(_)
        '/organization/show/abcdef012345' == response.redirectedUrl
        null == flash.message

        when: 'the action is called with sync'
        response.reset()
        params.returnUrl = null
        controller.googleContactSync = googleContactSync
        controller.gdatasync()

        then: 'the redirect is correctly set'
        1 * userService.getCurrentUser() >> user
        1 * googleContactSync.sync(user)
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called with sync and with return URL'
        response.reset()
        params.returnUrl = '/organization/show/abcdef012345'
        controller.gdatasync()

        then: 'the redirect is correctly set'
        1 * userService.getCurrentUser() >> user
        1 * googleContactSync.sync(user)
        '/organization/show/abcdef012345' == response.redirectedUrl
        null != flash.message
    }

    void 'The getPhoneNumbers action returns the correct models'() {
        given: 'an organization'
        Organization org = new Organization(
            name: 'My organization ltd.', phone: '+1 47 304503030',
            fax: '+1 47 304503039'
        )

        and: 'an instance'
        Person person = instance
        person.id = new ObjectId()
        person.organization = org
        person.phoneOther = '+1 47 304503034'
        person.phoneHome = '+1 47 749393711'
        person.mobile = '+1 47 584030374'
        person.fax = '+1 47 304503039'

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        when: 'the action is executed with a null domain'
        controller.getPhoneNumbers null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.getPhoneNumbers new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(person.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.getPhoneNumbers person.id.toString()

        then: 'the model is correct'
        1 * service.get(person.id) >> person
        ['+1 47 304503033', '+1 47 749393711', '+1 47 584030374',
         '+1 47 304503039', '+1 47 304503034', '+1 47 304503030'
        ] == model.phoneNumbers

        when: 'some phone numbers are unset'
        person.phoneOther = null
        response.reset()
        controller.getPhoneNumbers person.id.toString()

        then: 'the model is correct'
        1 * service.get(person.id) >> person
        ['+1 47 304503033', '+1 47 749393711', '+1 47 584030374',
         '+1 47 304503039', '+1 47 304503030'] == model.phoneNumbers

        when: 'some phone numbers are set to an empty string'
        person.phoneOther = ''
        response.reset()
        controller.getPhoneNumbers person.id.toString()

        then: 'the model is correct'
        1 * service.get(person.id) >> person
        ['+1 47 304503033', '+1 47 749393711', '+1 47 584030374',
         '+1 47 304503039', '+1 47 304503030'] == model.phoneNumbers

        when: 'some phone numbers are set to a duplicate'
        person.phoneOther = person.phone
        response.reset()
        controller.getPhoneNumbers person.id.toString()

        then: 'the model is correct'
        1 * service.get(person.id) >> person
        ['+1 47 304503033', '+1 47 749393711', '+1 47 584030374',
         '+1 47 304503039', '+1 47 304503030'] == model.phoneNumbers
    }

    void 'The getPicture action returns the correct response data'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        and: 'a 1x1 transparent GIF'
        byte [] picture = [
            0x47, 0x49, 0x46, 0x38, 0x39, 0x61, 0x01, 0x00,
            0x01, 0x00, 0x80, 0x00, 0x00, 0x00, 0x00, 0x00,
            0xff, 0xff, 0xff, 0x21, 0xf9, 0x04, 0x01, 0x00,
            0x00, 0x00, 0x00, 0x2c, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x01, 0x44,
            0x00, 0x3b
        ] as byte[]

        when: 'the action is executed with a null domain'
        controller.getPicture null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing ID'
        response.reset()
        controller.getPicture new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(person.id)
        404 == response.status

        when: 'the action is executed with an existing ID but without picture'
        response.reset()
        controller.getPicture person.id.toString()

        then: 'a 404 error is returned'
        1 * service.get(person.id) >> person
        404 == response.status

        when: 'the action is executed with an existing ID and with picture'
        response.reset()
        person.picture = picture
        controller.getPicture person.id.toString()

        then: 'the picture is returned'
        1 * service.get(person.id) >> person
        response.contentType.startsWith 'image/gif'
        42 == response.contentLength
        picture == response.contentAsByteArray
    }

    void 'The handleAuthenticationException method correctly redirects'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        when: 'the method is called'
        params.id = person.id.toString()
        request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = 'update'
        controller.handleAuthenticationException null

        then: 'the redirect is correct'
        '/error/ldapPerson?type=authentication&origAction=update&personId=' +
            person.id == response.redirectedUrl

        when: 'the method is called'
        response.reset()
        request['person'] = person
        request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = 'save'
        controller.handleAuthenticationException null

        then: 'the redirect is correct'
        '/error/ldapPerson?type=authentication&origAction=save&personId=' +
            person.id == response.redirectedUrl
    }

    void 'The handleConnectException method correctly redirects'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        when: 'the method is called'
        params.id = person.id.toString()
        request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = 'update'
        controller.handleConnectException null

        then: 'the redirect is correct'
        '/error/ldapPerson?type=communication&origAction=update&personId=' +
            person.id == response.redirectedUrl

        when: 'the method is called'
        response.reset()
        request['person'] = person
        request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = 'save'
        controller.handleConnectException null

        then: 'the redirect is correct'
        '/error/ldapPerson?type=communication&origAction=save&personId=' +
            person.id == response.redirectedUrl
    }

    void 'The handleNameNotFoundException method correctly redirects'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        when: 'the method is called'
        params.id = person.id.toString()
        request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = 'update'
        controller.handleNameNotFoundException null

        then: 'the redirect is correct'
        '/error/ldapPerson?type=nameNotFound&origAction=update&personId=' +
            person.id == response.redirectedUrl

        when: 'the method is called'
        response.reset()
        request['person'] = person
        request[GrailsApplicationAttributes.ACTION_NAME_ATTRIBUTE] = 'save'
        controller.handleNameNotFoundException null

        then: 'the redirect is correct'
        '/error/ldapPerson?type=nameNotFound&origAction=save&personId=' +
            person.id == response.redirectedUrl
    }

    void 'The index action returns the correct model'() {
        given: 'a list of persons'
        def list = [
            new Person(lastName: 'Person 1'),
            new Person(lastName: 'Person 2'),
            new Person(lastName: 'Person 3'),
        ]

        and: 'a service instance'
        PersonService service = Mock()
        1 * service.count() >> list.size()
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        controller.personService = service

        when: 'the action is executed without list type'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.personList.size()
        list == (List) model.personList
        list.size() == model.personCount
    }

    void 'The index action with letter returns the correct model'() {
        given: 'a list of persons'
        def list = [
            new Person(lastName: 'Person 1'),
            new Person(lastName: 'Person 2'),
            new Person(lastName: 'Person 3'),
        ]

        and: 'a service instance'
        PersonService service = Mock()
        1 * service.countByLastNameLessThan('E') >>> [45, 40]
        1 * service.count() >> list.size()
        1 * service.list(
            getParameterMap(letter: 'E', max: 10, offset: 40, sort: 'lastName')
        ) >> list
        controller.personService = service

        when: 'the action is executed without list type'
        params.letter = 'E'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.personList.size()
        list == (List) model.personList
        list.size() == model.personCount
    }

    void 'The ldapdelete action correctly redirects'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        and: 'an LDAP service instance'
        LdapService ldapService = Mock()

        when: 'the action is executed without the LDAP service and null value'
        controller.ldapdelete null

        then: 'only a redirect is issued'
        0 * service.get(_)
        '/person/index' == response.redirectedUrl

        when: 'the action is executed without the LDAP service and a non-existing ID'
        response.reset()
        controller.ldapdelete new ObjectId().toString()

        then: 'only a redirect is issued'
        0 * service.get(person.id)
        '/person/index' == response.redirectedUrl

        when: 'the action is executed without the LDAP service and a value'
        response.reset()
        controller.ldapdelete person.id.toString()

        then: 'only a redirect is issued'
        0 * service.get(person.id)
        '/person/index' == response.redirectedUrl

        when: 'the action is executed with the LDAP service and null value'
        response.reset()
        controller.ldapService = ldapService
        controller.ldapdelete null

        then: 'only a redirect is issued'
        0 * service.get(_)
        0 * ldapService.delete(_)
        '/person/index' == response.redirectedUrl

        when: 'the action is executed with the LDAP service and a non-existing ID'
        response.reset()
        controller.ldapdelete new ObjectId().toString()

        then: 'only a redirect is issued'
        0 * service.get(person.id)
        0 * ldapService.delete(_)
        '/person/index' == response.redirectedUrl

        when: 'the action is executed with the LDAP service and a value'
        response.reset()
        controller.ldapdelete person.id.toString()

        then: 'a correct redirect is performed'
        1 * service.get(person.id) >> person
        1 * ldapService.delete(person)
        '/person/index' == response.redirectedUrl
    }

    void 'The ldapexport action does nothing without LDAP service'() {
        when: 'the action is executed without return URL'
        controller.ldapexport null

        then: 'only a redirect is issued'
        '/person/index' == response.redirectedUrl
        null == flash.message

        when: 'the action is executed with return URL'
        response.reset()
        params.returnUrl = '/organization/show/abcdef012345'
        controller.ldapexport null

        then: 'only a redirect is issued'
        '/organization/show/abcdef012345' == response.redirectedUrl
        null == flash.message
    }

    void 'The ldapexport action correctly exports persons without excludes'() {
        given: 'an organization rating'
        Rating rating = new Rating()
        rating.id = 4510L

        and: 'an organization'
        Organization org = new Organization(
            name: 'My organization ltd.', rating: rating
        )

        and: 'a single person'
        Person person = new Person(lastName: 'Person 1', organization: org)
        person.id = new ObjectId()

        and: 'a list of persons'
        def list = [
            person,
            new Person(lastName: 'Person 2', organization: org),
            new Person(lastName: 'Person 3', organization: org),
        ]

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        and: 'an LDAP service instance'
        LdapService ldapService = Mock()
        controller.ldapService = ldapService

        and: 'a user instance'
        User user = new User(username: 'jdoe')

        and: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentUser() >> user
        controller.userService = userService

        and: 'an additional ID'
        ObjectId anotherId = new ObjectId()

        when: 'the action is called without an ID'
        controller.ldapexport null

        then: 'the person is saved in LDAP'
        1 * service.list(null) >> list
        3 * ldapService.save({ it.lastName.startsWith 'Person ' })
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called without an ID and return URL'
        response.reset()
        params.returnUrl = '/organization/show/abcdef012345'
        controller.ldapexport null

        then: 'the person is saved in LDAP'
        1 * service.list(null) >> list
        3 * ldapService.save({ it.lastName.startsWith 'Person ' })
        '/organization/show/abcdef012345' == response.redirectedUrl
        null != flash.message

        when: 'the action is called with a non-existing ID'
        response.reset()
        controller.ldapexport anotherId.toString()

        then: 'the person is saved in LDAP'
        0 * service.get(person.id)
        0 * ldapService.save(_)
        '/person/show/' + anotherId == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an existing ID'
        response.reset()
        controller.ldapexport person.id.toString()

        then: 'the person is saved in LDAP'
        1 * service.get(person.id) >> person
        1 * ldapService.save(person)
        '/person/show/' + person.id == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an ID but and return URL'
        response.reset()
        params.returnUrl = '/organization/show/abcdef012345'
        controller.ldapexport person.id.toString()

        then: 'the person is saved in LDAP'
        1 * service.get(person.id) >> person
        1 * ldapService.save(person)
        '/person/show/' + person.id == response.redirectedUrl
        null != flash.message
    }

    void 'The ldapexport action correctly exports persons with excludes'() {
        given: 'two organization ratings'
        Rating rating1 = new Rating()
        rating1.id = 4510L
        Rating rating2 = new Rating()
        rating2.id = 4520L

        and: 'two organizations'
        Organization org1 = new Organization(
            name: 'My organization ltd.', rating: rating1
        )
        Organization org2 = new Organization(
            name: 'Another organization ltd.', rating: rating2
        )

        and: 'some single person'
        Person person1 = new Person(lastName: 'Person 1', organization: org1)
        person1.id = new ObjectId()
        Person person2 = new Person(lastName: 'Person 2', organization: org2)
        person2.id = new ObjectId()
        Person person3 = new Person(lastName: 'Person 3', organization: org2)
        person3.id = new ObjectId()

        and: 'a list of persons'
        def list = [person1, person2, person3]

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        and: 'an LDAP service instance'
        LdapService ldapService = Mock()
        controller.ldapService = ldapService

        and: 'a user instance'
        User user = new User(
            username: 'jdoe', settings: [excludeFromSync: '4520']
        )

        and: 'a user service instance'
        UserService userService = Mock()
        userService.getCurrentUser() >> user
        controller.userService = userService

        and: 'an additional ID'
        ObjectId anotherId = new ObjectId()

        when: 'the action is called without an ID'
        controller.ldapexport null

        then: 'the person is saved in LDAP'
        1 * service.list(null) >> list
        1 * ldapService.save(person1)
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called without an ID and return URL'
        response.reset()
        params.returnUrl = '/organization/show/abcdef012345'
        controller.ldapexport null

        then: 'the person is saved in LDAP'
        1 * service.list(null) >> list
        1 * ldapService.save(person1)
        '/organization/show/abcdef012345' == response.redirectedUrl
        null != flash.message

        when: 'the action is called with a non-existing ID'
        response.reset()
        controller.ldapexport anotherId.toString()

        then: 'the person is saved in LDAP'
        0 * service.get(person1.id)
        0 * ldapService.save(_)
        '/person/show/' + anotherId == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an existing ID'
        response.reset()
        controller.ldapexport person1.id.toString()

        then: 'the person is saved in LDAP'
        1 * service.get(person1.id) >> person1
        1 * ldapService.save(person1)
        '/person/show/' + person1.id == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an existing but excluded ID'
        response.reset()
        controller.ldapexport person2.id.toString()

        then: 'the person is saved in LDAP'
        1 * service.get(person2.id) >> person2
        0 * ldapService.save(_)
        '/person/show/' + person2.id == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an ID but and return URL'
        response.reset()
        params.returnUrl = '/organization/show/abcdef012345'
        controller.ldapexport person1.id.toString()

        then: 'the person is saved in LDAP'
        1 * service.get(person1.id) >> person1
        1 * ldapService.save(person1)
        '/person/show/' + person1.id == response.redirectedUrl
        null != flash.message
    }

    void 'The listEmbedded action returns the correct model'() {
        given: 'a list of persons'
        def list = [
            new Person(lastName: 'Person 1'),
            new Person(lastName: 'Person 2'),
            new Person(lastName: 'Person 3'),
        ]

        and: 'an organization'
        Organization org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        when: 'the action is called for a null instance'
        controller.listEmbedded null

        then: 'a 404 error is returned'
        0 * organizationService.get(_)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        404 == response.status

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.listEmbedded new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * organizationService.get(org.id)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        404 == response.status

        when: 'the action is called for an existing instance'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded org.id.toString()

        then: 'the model is correct'
        1 * organizationService.get(org.id) >> org
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        list == (List) model.personList
        list.size() == model.personCount
        [organization: org.id.toString()] == (Map) model.linkParams
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save null

        then: 'a 404 error is returned'
        0 * service.save(_)
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save person

        then: 'the create view is rendered again with the correct model'
        1 * service.save(person) >> {
            throw new ValidationException('', new ValidationErrors(person))
        }
        person == model.person
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save person

        then: 'a redirect is issued to the show action'
        1 * service.save(person) >> person
        '/person/show/' + person.id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The show action returns the correct model'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        when: 'the action is executed with a null domain'
        controller.show null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.show new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(person.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show person.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(person.id) >> person
        person == model.person
    }

    void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        Person person = instance
        person.id = new ObjectId()

        and: 'a service instance'
        PersonService service = Mock()
        controller.personService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update null

        then: 'a 404 error is returned'
        0 * service.get(_)
        0 * service.save(_)
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called with the ID of a non-existing instance'
        response.reset()
        controller.update new ObjectId().toString()

        then: 'the edit view is rendered again with the invalid instance'
        0 * service.get(person.id)
        0 * service.save(_)
        '/person/index' == response.redirectedUrl
        null != flash.message

        when: 'an invalid domain instance is passed to the action'
        response.reset()
        controller.update person.id.toString()

        then: 'the edit view is rendered again with the invalid instance'
        1 * service.get(person.id) >> person
        1 * service.save(person) >> {
            throw new ValidationException('', new ValidationErrors(person))
        }
        person == model.person
        'edit' == view

        when: 'a valid domain instance is passed to the action'
        response.reset()
        controller.update person.id.toString()

        then: 'a redirect is issued to the show action'
        1 * service.get(person.id) >> person
        1 * service.save(person) >> person
        '/person/show/' + person.id == response.redirectedUrl
        null != controller.flash.message
    }


    //-- Non-public methods -------------------------

    private static Person getInstance() {
        Map properties = [: ]
        populateValidParams properties, 45000i

        new Person(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params, Integer number = null) {
        assert params != null

        params.firstName = 'John'
        params.lastName = 'Doe'
        params.number = number
        params.phone = '+1 47 304503033'
        params.email1 = 'j.doe@example.com'
        params.email2 = 'info@example.com'
    }
}
