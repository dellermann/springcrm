/*
 * CallControllerSpec.groovy
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

import grails.test.mixin.TestFor
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification


@TestFor(CallController)
class CallControllerSpec extends Specification {

    //-- Fields -------------------------------------

    CallService callService


    //-- Fixture methods ------------------------

    def setup() {
        callService = Mock(CallService)
        controller.callService = callService
    }


    //-- Feature methods ------------------------

    def 'Copy shows a copy in edit view'() {
        when: 'I call the action method'
        controller.copy(5L)

        then: 'the service method is called'
        1 * callService.get(5L) >> fixtureCall(5L)

        and: 'the create view is rendered'
        '/call/create' == view

        and: 'the copied instance is in the model'
        verifyCall model.callInstance
    }

    def 'Copy non-existing instance redirect to index view'() {
        when: 'I call the action method'
        controller.copy(5L)

        then: 'the service method is called'
        1 * callService.get(5L) >> null

        and: 'the user is redirected to index view'
        '/call/index' == response.redirectedUrl

        and: 'a message in the flash scope is set'
        'default.not.found.message' == flash.message
    }

    def 'Create without presets'() {
        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains an empty instance'
        null != model.callInstance
        null == model.callInstance.id
        null == model.callInstance.subject
    }

    def 'Create with presets'() {
        given: 'some presets'
        params.subject = 'My phone call'
        params.phone = '+1 5739 948377'

        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains a pre-filled instance'
        null != model.callInstance
        null == model.callInstance.id
        'My phone call' == model.callInstance.subject
        null == model.callInstance.notes
        '+1 5739 948377' == model.callInstance.phone
    }

    def 'Create with person'() {
        given: 'an organization'
        def org = new Organization(
            name: 'MyOrganization Ltd.', phone: '+1 5739 948000'
        )

        and: 'a person'
        def p = new Person(
            firstName: 'John', lastName: 'Smith', organization: org,
            phone: '+1 5739 948377'
        )

        and: 'some presets'
        params.person = p

        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains a pre-filled instance'
        null != model.callInstance
        null == model.callInstance.id
        null == model.callInstance.subject
        '+1 5739 948377' == model.callInstance.phone
        model.callInstance.organization.is org
    }

    def 'Create with organization'() {
        given: 'an organization'
        def org = new Organization(
            name: 'MyOrganization Ltd.', phone: '+1 5739 948000'
        )

        and: 'some presets'
        params.organization = org

        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains a pre-filled instance'
        null != model.callInstance
        null == model.callInstance.id
        null == model.callInstance.subject
        null == model.callInstance.person
        '+1 5739 948000' == model.callInstance.phone
    }

    def 'Delete existing instance'() {
        given: 'an instance'
        def call = fixtureCall(5L)

        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> call

        and: 'the deletion method is called'
        1 * callService.delete(5L)

        and: 'a flash message has been set'
        'default.deleted.message' == flash.message

        and: 'the instance has been stored in request context'
        request.callInstance.is call

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Delete existing instance with versioning conflict'() {
        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> fixtureCall(5L)

        and: 'the deletion method is called'
        1 * callService.delete(5L) >> {
            throw new DataIntegrityViolationException('')
        }

        and: 'a flash message has been set'
        'default.not.deleted.message' == flash.message

        and: 'the user is redirected to show view'
        '/call/show/5' == response.redirectedUrl
    }

    def 'Delete non-existing instance'() {
        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> null

        and: 'the deletion method is not called'
        0 * callService.delete(_)

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the instance has not been stored in request context'
        null == request.callInstance

        and: 'the user is redirected to index view'
        '/call/index' == response.redirectedUrl
    }

    def 'Edit existing instance'() {
        when: 'I call the action method'
        Map model = controller.edit(5L)

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> fixtureCall(5L)

        and: 'the model contains an empty instance'
        verifyCall model.callInstance, 5L
    }

    def 'Edit non-existing instance'() {
        when: 'I call the action method'
        controller.edit(5L)

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/call/index' == response.redirectedUrl
    }

    def 'Index with empty instance list'() {
        given: 'some method stubs'
        1 * callService.list(_ as Map) >> []
        1 * callService.count() >> 0

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get an empty list'
        model.callInstanceList.isEmpty()
        0 == model.callInstanceTotal
    }

    def 'Index with non-empty instance list'() {
        given: 'some method stubs'
        1 * callService.list(_ as Map) >> fixtureCalls()
        1 * callService.count() >> 3

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.callInstanceList.size()
        3 == model.callInstanceTotal

        and: 'the items are correct'
        verifyCalls model
    }

    def 'Index with non-empty instance list and params'() {
        given: 'some method stubs'
        1 * callService.list({
            it.offset == 60 && it.max == 20 && it.sort == 'phone' &&
                it.order == 'desc'
        }) >> fixtureCalls()
        1 * callService.count() >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'phone'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.callInstanceList.size()
        3 == model.callInstanceTotal

        and: 'the items are correct'
        verifyCalls model
    }

    def 'Index with empty list and letter'() {
        given: 'some method stubs'
        1 * callService.list(_ as Map) >> []
        1 * callService.count() >> 0

        and: 'a given letter'
        params.letter = 'M'

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get an empty list'
        model.callInstanceList.isEmpty()
        0 == model.callInstanceTotal
    }

    def 'Index with non-empty list and letter'(int n, int max, int offset) {
        given: 'some method stubs'
        1 * callService.countBySubjectLessThan('M') >> n
        1 * callService.list({
            it.offset == offset && it.max == max && it.search == null
        }) >> fixtureCalls()
        1 * callService.count() >> 3

        and: 'a given letter and maximum value'
        params.letter = 'M'
        params.max = max

        and: 'an offset which will be overwritten'
        params.offset = 1234

        and: 'a search pattern which will be reset'
        params.search = 'phone'

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.callInstanceList.size()
        3 == model.callInstanceTotal

        and: 'the items are correct'
        verifyCalls model

        where:
        n       | max       || offset
        0       | 10        || 0
        1       | 10        || 0
        5       | 10        || 0
        9       | 10        || 0
        10      | 10        || 10
        99      | 10        || 90
        100     | 10        || 100
        0       | 20        || 0
        19      | 20        || 0
        20      | 20        || 20
        99      | 100       || 0
        100     | 100       || 100
        101     | 100       || 100
        999     | 100       || 900
        1000    | 100       || 1000
    }

    def 'Index with search string'() {
        given: 'some method stubs'
        1 * callService.findAllBySubjectLike(
            '%phone%', { it.offset == 200 && it.max == 50}
        ) >> fixtureCalls()
        1 * callService.countBySubjectLike('%phone%') >> 3

        and: 'a given search pattern and other parameters'
        params.search = 'phone'
        params.offset = 200
        params.max = 50

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.callInstanceList.size()
        3 == model.callInstanceTotal

        and: 'the items are correct'
        verifyCalls model
    }

    def 'Embedded list without parameters'() {
        when: 'I call the action method'
        Map model = controller.listEmbedded(null, null)

        then: 'I get an empty list'
        model.callInstanceList.isEmpty()
        0 == model.callInstanceTotal
        model.linkParams.isEmpty()
    }

    def 'Embedded list with non-existing organization'() {
        given: 'an organization service'
        OrganizationService organizationService = Mock(OrganizationService)
        controller.organizationService = organizationService

        when: 'I call the action method'
        Map model = controller.listEmbedded(5L, null)

        then: 'the organization retrieval method is called'
        1 * organizationService.get(5L) >> null

        and: 'I get an empty list'
        model.callInstanceList.isEmpty()
        0 == model.callInstanceTotal
        model.linkParams.isEmpty()
    }

    def 'Embedded list with existing organization'() {
        given: 'an organization service'
        OrganizationService organizationService = Mock(OrganizationService)
        controller.organizationService = organizationService

        and: 'an organization'
        def org = new Organization(name: 'MyCompany Ltd.')
        org.id = 5L

        and: 'some method stubs'
        1 * callService.findAllByOrganization(
            org,
            {it.offset == 60 && it.max == 20 && it.sort == 'phone' &&
                it.order == 'desc'}
        ) >> fixtureCalls()
        1 * callService.countByOrganization(org) >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'phone'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.listEmbedded(5L, null)

        then: 'the organization retrieval method is called'
        1 * organizationService.get(5L) >> org

        then: 'I get a non-empty list'
        3 == model.callInstanceList.size()
        3 == model.callInstanceTotal
        1 == model.linkParams.size()
        5L == model.linkParams.organization

        and: 'the items are correct'
        verifyCalls model
    }

    def 'Embedded list with non-existing person'() {
        given: 'an person service'
        PersonService personService = Mock(PersonService)
        controller.personService = personService

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, 35L)

        then: 'the person retrieval method is called'
        1 * personService.get(35L) >> null

        and: 'I get an empty list'
        model.callInstanceList.isEmpty()
        0 == model.callInstanceTotal
        model.linkParams.isEmpty()
    }

    def 'Embedded list with existing person'() {
        given: 'an person service'
        PersonService personService = Mock(PersonService)
        controller.personService = personService

        and: 'a person'
        def p = new Person(firstName: 'John', lastName: 'Doe')
        p.id = 35L

        and: 'some method stubs'
        1 * callService.findAllByPerson(
            p,
            {it.offset == 60 && it.max == 20 && it.sort == 'phone' &&
                it.order == 'desc'}
        ) >> fixtureCalls()
        1 * callService.countByPerson(p) >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'phone'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.listEmbedded(null, 35L)

        then: 'the person retrieval method is called'
        1 * personService.get(35L) >> p

        then: 'I get a non-empty list'
        3 == model.callInstanceList.size()
        3 == model.callInstanceTotal
        1 == model.linkParams.size()
        35L == model.linkParams.person

        and: 'the items are correct'
        verifyCalls model
    }

    def 'Embedded list with existing organization and person'() {
        given: 'an organization service'
        OrganizationService organizationService = Mock(OrganizationService)
        controller.organizationService = organizationService

        and: 'an person service'
        PersonService personService = Mock(PersonService)
        controller.personService = personService

        and: 'an organization'
        def org = new Organization(name: 'MyCompany Ltd.')
        org.id = 5L

        and: 'a person'
        def p = new Person(firstName: 'John', lastName: 'Doe')
        p.id = 35L

        and: 'some method stubs'
        1 * callService.findAllByOrganization(
            org,
            {it.offset == 60 && it.max == 20 && it.sort == 'phone' &&
                it.order == 'desc'}
        ) >> fixtureCalls()
        1 * callService.countByOrganization(org) >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'phone'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.listEmbedded(5L, 35L)

        then: 'the organization retrieval method is called'
        1 * organizationService.get(5L) >> org

        then: 'I get a non-empty list'
        3 == model.callInstanceList.size()
        3 == model.callInstanceTotal
        1 == model.linkParams.size()
        5L == model.linkParams.organization

        and: 'the items are correct'
        verifyCalls model
    }

    def 'Save successful'() {
        given: 'some method stubs'
        1 * callService.save(_ as Call) >> { Call call ->
            assert 'My phone call' == call.subject
            assert 'A client called me' == call.notes
            assert '+1 4759 4859743' == call.phone

            call.id = 47L

            call
        }

        and: 'some form data'
        params.subject = 'My phone call'
        params.notes = 'A client called me'
        params.phone = '+1 4759 4859743'

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the instance has been stored in request context'
        47L == request.callInstance.id
        'My phone call' == request.callInstance.subject
        'A client called me' == request.callInstance.notes
        '+1 4759 4859743' == request.callInstance.phone

        and: 'a message has been set in flash context'
        'default.created.message' == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Save not successful'() {
        given: 'some method stubs'
        1 * callService.save(_ as Call) >> { Call call ->
            assert 'My phone call' == call.subject
            assert 'A client called me' == call.notes
            assert '+1 4759 4859743' == call.phone

            null
        }

        and: 'some form data'
        params.subject = 'My phone call'
        params.notes = 'A client called me'
        params.phone = '+1 4759 4859743'

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the create view is rendered'
        '/call/create' == view

        and: 'the model contains the instance'
        null != model.callInstance
        null == model.callInstance.id
        'My phone call' == model.callInstance.subject
        'A client called me' == model.callInstance.notes
        '+1 4759 4859743' == model.callInstance.phone

        and: 'the no instance has been stored in request context'
        null == request.callInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Show existing instance'() {
        when: 'I call the action method'
        Map model = controller.show(5L)

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> fixtureCall(5L)

        and: 'the model contains an instance'
        null != model.callInstance
        5L == model.callInstance.id
        'My phone call' == model.callInstance.subject
        '+1 5739 948377' == model.callInstance.phone
    }

    def 'Show non-existing instance'() {
        when: 'I call the action method'
        controller.show(5L)

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/call/index' == response.redirectedUrl
    }

    def 'Update successful'() {
        given: 'some method stubs'
        1 * callService.get(5L) >> fixtureCall(5L, 14L)
        1 * callService.save(_ as Call) >> { Call c ->
            assert 5L == c.id
            assert 14L == c.version
            assert 'My other phone call' == c.subject
            assert 'A client called me' == c.notes
            assert '+1 4759 4859000' == c.phone

            c
        }

        and: 'some form data'
        params.version = 14L
        params.subject = 'My other phone call'
        params.notes = 'A client called me'
        params.phone = '+1 4759 4859000'

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the instance has been stored in request context'
        null != request.callInstance
        5L == request.callInstance.id
        14L == request.callInstance.version
        'My other phone call' == request.callInstance.subject
        'A client called me' == request.callInstance.notes
        '+1 4759 4859000' == request.callInstance.phone

        and: 'a message has been set in flash context'
        'default.updated.message' == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update not successful'() {
        given: 'some method stubs'
        1 * callService.get(5L) >> fixtureCall(5L, 14L)
        1 * callService.save(_ as Call) >> { Call c ->
            assert 'My other phone call' == c.subject
            assert 'A client called me' == c.notes
            assert '+1 4759 4859000' == c.phone

            null
        }

        and: 'some form data'
        params.version = 14L
        params.subject = 'My other phone call'
        params.notes = 'A client called me'
        params.phone = '+1 4759 4859000'

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the edit view is rendered'
        '/call/edit' == view

        and: 'the model contains the instance'
        null != model.callInstance
        5L == model.callInstance.id
        'My other phone call' == model.callInstance.subject
        'A client called me' == model.callInstance.notes
        '+1 4759 4859000' == model.callInstance.phone

        and: 'no instance has been stored in request context'
        null == request.callInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update existing instance with version conflict'() {
        given: 'some method stubs'
        1 * callService.get(5L) >> fixtureCall(5L, 14L)

        and: 'some form data'
        params.version = '13'
        params.subject = 'My other phone call'
        params.notes = 'A client called me'
        params.phone = '+1 4759 4859000'

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the edit view is rendered'
        '/call/edit' == view

        and: 'the model contains the instance unmodified'
        verifyCall model.callInstance, 5L, 14L

        and: 'a field error has been set'
        'default.optimistic.locking.failure' == model.callInstance.errors['version'].code

        and: 'the save method has not been called'
        0 * callService.save(_)

        and: 'no redirect has been set'
        null == response.redirectedUrl
    }

    def 'Update non-existing instance'() {
        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the retrieval method is called'
        1 * callService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/call/index' == response.redirectedUrl
    }


    //-- Non-public methods -------------------------

    private static Call fixtureCall(Long id = null, Long version = null) {
        def call = new Call(
            subject: 'My phone call', phone: '+1 5739 948377'
        )
        if (id != null) call.id = id
        if (version != null) call.version = version

        call
    }

    private static List<Call> fixtureCalls(int n = 3) {
        List<Call> res = []
        for (int i = 0; i < n; i++) {
            Call c = new Call(subject: 'My phone call ' + (i + 1))
            res << c
        }

        res
    }

    private static void verifyCall(Call instance, Long id = null,
                                   Long version = null)
    {
        assert null != instance
        assert id == instance.id
        assert version == instance.version
        assert 'My phone call' == instance.subject
        assert '+1 5739 948377' == instance.phone
    }

    private static void verifyCalls(Map model, int n = 3) {
        for (int i = 0; i < n; i++) {
            String subject = 'My phone call ' + (i + 1)
            assert subject == model.callInstanceList[i].subject
        }
    }
}
