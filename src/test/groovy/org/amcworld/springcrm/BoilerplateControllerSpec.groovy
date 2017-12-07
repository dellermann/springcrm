/*
 * BoilerplateControllerSpec.groovy
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
import javax.servlet.http.HttpServletResponse
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification


@TestFor(BoilerplateController)
class BoilerplateControllerSpec extends Specification {

    //-- Fields -------------------------------------

    BoilerplateService boilerplateService


    //-- Fixture methods ------------------------

    def setup() {
        boilerplateService = Mock(BoilerplateService)
        controller.boilerplateService = boilerplateService
    }


    //-- Feature methods ------------------------

    def 'Copy shows a copy in edit view'() {
        when: 'I call the action method'
        controller.copy(5L)

        then: 'the service method is called'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate(5L)

        and: 'the create view is rendered'
        '/boilerplate/create' == view

        and: 'the copied instance is in the model'
        verifyBoilerplate model.boilerplateInstance
    }

    def 'Copy non-existing instance redirect to index view'() {
        when: 'I call the action method'
        controller.copy(5L)

        then: 'the service method is called'
        1 * boilerplateService.get(5L) >> null

        and: 'the user is redirected to index view'
        '/boilerplate/index' == response.redirectedUrl

        and: 'a message in the flash scope is set'
        'default.not.found.message' == flash.message
    }

    def 'Create without presets'() {
        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains an empty instance'
        null != model.boilerplateInstance
        null == model.boilerplateInstance.id
        null == model.boilerplateInstance.name
    }

    def 'Create with presets'() {
        given: 'some presets'
        params.name = 'My first boilerplate'
        params.content = 'For [[description of service]] we ...'

        when: 'I call the action method'
        Map model = controller.create()

        then: 'the model contains a pre-filled instance'
        verifyBoilerplate model.boilerplateInstance
    }

    def 'Delete existing instance'() {
        given: 'an instance'
        def boilerplate = fixtureBoilerplate(5L)

        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> boilerplate

        and: 'the deletion method is called'
        1 * boilerplateService.delete(5L)

        and: 'a flash message has been set'
        'default.deleted.message' == flash.message

        and: 'the instance has been stored in request context'
        request.boilerplateInstance.is boilerplate

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Delete existing instance with versioning conflict'() {
        when: 'I boilerplate the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate(5L)

        and: 'the deletion method is called'
        1 * boilerplateService.delete(5L) >> {
            throw new DataIntegrityViolationException('')
        }

        and: 'a flash message has been set'
        'default.not.deleted.message' == flash.message

        and: 'the user is redirected to show view'
        '/boilerplate/show/5' == response.redirectedUrl
    }

    def 'Delete non-existing instance'() {
        when: 'I call the action method'
        controller.delete(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> null

        and: 'the deletion method is not called'
        0 * boilerplateService.delete(_)

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the instance has not been stored in request context'
        null == request.boilerplateInstance

        and: 'the user is redirected to index view'
        '/boilerplate/index' == response.redirectedUrl
    }

    def 'Edit existing instance'() {
        when: 'I call the action method'
        Map model = controller.edit(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate()

        and: 'the model contains an empty instance'
        verifyBoilerplate model.boilerplateInstance
    }

    def 'Edit non-existing instance'() {
        when: 'I call the action method'
        controller.edit(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/boilerplate/index' == response.redirectedUrl
    }

    def 'Find instances successfully'() {
        given: 'some method stubs'
        1 * boilerplateService.findAllByNameIlike('%first%') >>
            fixtureBoilerplates()

        when: 'I call the action method'
        Map model = controller.find('first')

        then: 'I get a non-empty list'
        3 == model.boilerplateInstanceList.size()

        and: 'the items are valid'
        verifyBoilerplates model
    }

    def 'Find instances not successfully'() {
        given: 'some method stubs'
        1 * boilerplateService.findAllByNameIlike('%company%') >> []

        when: 'I call the action method'
        Map model = controller.find('company')

        then: 'I get an empty list'
        model.boilerplateInstanceList.isEmpty()
    }

    def 'Get existing instance'() {
        when: 'I call the action method'
        Map model = controller.get(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate()

        and: 'the model contains an empty instance'
        verifyBoilerplate model.boilerplateInstance
    }

    def 'Get non-existing instance'() {
        when: 'I call the action method'
        controller.get(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> null

        and: 'an HTTP error has been set'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Index with empty instance list'() {
        given: 'some method stubs'
        1 * boilerplateService.list(_ as Map) >> []
        1 * boilerplateService.count() >> 0

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get an empty list'
        model.boilerplateInstanceList.isEmpty()
        0 == model.boilerplateInstanceTotal
    }

    def 'Index with non-empty instance list'() {
        given: 'some method stubs'
        1 * boilerplateService.list(_ as Map) >> fixtureBoilerplates()
        1 * boilerplateService.count() >> 3

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.boilerplateInstanceList.size()
        3 == model.boilerplateInstanceTotal

        and: 'the items are correct'
        verifyBoilerplates model
    }

    def 'Index with non-empty instance list and params'() {
        given: 'some method stubs'
        1 * boilerplateService.list({
            it.offset == 60 && it.max == 20 && it.sort == 'name' &&
                it.order == 'desc'
        }) >> fixtureBoilerplates()
        1 * boilerplateService.count() >> 3

        and: 'some parameters'
        params.offset = 60
        params.max = 20
        params.sort = 'name'
        params.order = 'desc'

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.boilerplateInstanceList.size()
        3 == model.boilerplateInstanceTotal

        and: 'the items are correct'
        verifyBoilerplates model
    }

    def 'Index with empty list and letter'() {
        given: 'some method stubs'
        1 * boilerplateService.list(_ as Map) >> []
        1 * boilerplateService.count() >> 0

        and: 'a given letter'
        params.letter = 'M'

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get an empty list'
        model.boilerplateInstanceList.isEmpty()
        0 == model.boilerplateInstanceTotal
    }

    def 'Index with non-empty list and letter'(int n, int max, int offset) {
        given: 'some method stubs'
        1 * boilerplateService.countByNameLessThan('M') >> n
        1 * boilerplateService.list({ it.offset == offset && it.max == max}) >>
            fixtureBoilerplates()
        1 * boilerplateService.count() >> 3

        and: 'a given letter and maximum value'
        params.letter = 'M'
        params.max = max

        and: 'an offset which will be overwritten'
        params.offset = 1234

        when: 'I call the action method'
        Map model = controller.index()

        then: 'I get a non-empty list'
        3 == model.boilerplateInstanceList.size()
        3 == model.boilerplateInstanceTotal

        and: 'the items are correct'
        verifyBoilerplates model

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

    def 'Save successful'() {
        given: 'some method stubs'
        1 * boilerplateService.save(_ as Boilerplate) >> { Boilerplate b ->
            assert 'Another boilerplate' == b.name
            assert 'You can write this...' == b.content

            b.id = 47L

            b
        }

        and: 'some form data'
        params.name = 'Another boilerplate'
        params.content = 'You can write this...'

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the instance has been stored in request context'
        47L == request.boilerplateInstance.id
        'Another boilerplate' == request.boilerplateInstance.name
        'You can write this...' == request.boilerplateInstance.content

        and: 'a message has been set in flash context'
        'default.created.message' == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Save not successful'() {
        given: 'some method stubs'
        1 * boilerplateService.save(_ as Boilerplate) >> { Boilerplate b ->
            assert 'Another boilerplate' == b.name
            assert 'You can write this...' == b.content

            null
        }

        and: 'some form data'
        params.name = 'Another boilerplate'
        params.content = 'You can write this...'

        when: 'I call the action method'
        controller.testSave()   // XXX temporary hack, see testSave()

        then: 'the create view is rendered'
        '/boilerplate/create' == view

        and: 'the model contains the instance'
        null != model.boilerplateInstance
        null == model.boilerplateInstance.id
        'Another boilerplate' == model.boilerplateInstance.name
        'You can write this...' == model.boilerplateInstance.content

        and: 'the no instance has been stored in request context'
        null == request.boilerplateInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Show existing instance'() {
        when: 'I call the action method'
        Map model = controller.show(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate(5L)

        and: 'the model contains an instance'
        verifyBoilerplate model.boilerplateInstance, 5L
    }

    def 'Show non-existing instance'() {
        when: 'I call the action method'
        controller.show(5L)

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/boilerplate/index' == response.redirectedUrl
    }

    def 'Update successful'() {
        given: 'some method stubs'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate(5L, 14L)
        1 * boilerplateService.save(_ as Boilerplate) >> { Boilerplate b ->
            assert 5L == b.id
            assert 14L == b.version
            assert 'Another important boilerplate' == b.name
            assert 'You can write this...' == b.content

            b
        }

        and: 'some form data'
        params.version = 14L
        params.name = 'Another important boilerplate'
        params.content = 'You can write this...'

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the instance has been stored in request context'
        null != request.boilerplateInstance
        5L == request.boilerplateInstance.id
        14L == request.boilerplateInstance.version
        'Another important boilerplate' == request.boilerplateInstance.name
        'You can write this...' == request.boilerplateInstance.content

        and: 'a message has been set in flash context'
        'default.updated.message' == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update not successful'() {
        given: 'some method stubs'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate(5L, 14L)
        1 * boilerplateService.save(_ as Boilerplate) >> { Boilerplate b ->
            assert 'Another important boilerplate' == b.name
            assert 'You can write this...' == b.content

            null
        }

        and: 'some form data'
        params.version = 14L
        params.name = 'Another important boilerplate'
        params.content = 'You can write this...'

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the edit view is rendered'
        '/boilerplate/edit' == view

        and: 'the model contains the instance'
        assert null != model.boilerplateInstance
        assert 5L == model.boilerplateInstance.id
        assert 14L == model.boilerplateInstance.version
        assert 'Another important boilerplate' == model.boilerplateInstance.name
        assert 'You can write this...' == model.boilerplateInstance.content

        and: 'no instance has been stored in request context'
        null == request.boilerplateInstance

        and: 'no message has been set in flash context'
        null == flash.message

        and: 'no redirection has been set'
        null == response.redirectedUrl
    }

    def 'Update existing instance with version conflict'() {
        given: 'some method stubs'
        1 * boilerplateService.get(5L) >> fixtureBoilerplate(5L, 14L)

        and: 'some form data'
        params.version = '13'
        params.name = 'Another important boilerplate'
        params.content = 'You can write this...'

        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the edit view is rendered'
        '/boilerplate/edit' == view

        and: 'the model contains the instance unmodified'
        verifyBoilerplate model.boilerplateInstance, 5L, 14L

        and: 'a field error has been set'
        'default.optimistic.locking.failure' == model.boilerplateInstance.errors['version'].code

        and: 'the save method has not been called'
        0 * boilerplateService.save(_)

        and: 'no redirect has been set'
        null == response.redirectedUrl
    }

    def 'Update non-existing instance'() {
        when: 'I call the action method'
        controller.testUpdate(5L)   // XXX temporary hack, see testUpdate()

        then: 'the retrieval method is called'
        1 * boilerplateService.get(5L) >> null

        and: 'a flash message has been set'
        'default.not.found.message' == flash.message

        and: 'the user is redirected to index view'
        '/boilerplate/index' == response.redirectedUrl
    }


    //-- Non-public methods -------------------------

    private static Boilerplate fixtureBoilerplate(Long id = null,
                                                  Long version = null)
    {
        def boilerplate = new Boilerplate(
            name: 'My first boilerplate',
            content: 'For [[description of service]] we ...'
        )
        if (id != null) boilerplate.id = id
        if (version != null) boilerplate.version = version

        boilerplate
    }

    private static List<Boilerplate> fixtureBoilerplates(int n = 3) {
        List<Boilerplate> res = []
        for (int i = 0; i < n; i++) {
            Boilerplate c = new Boilerplate(name: 'My boilerplate ' + (i + 1))
            res << c
        }

        res
    }

    private static void verifyBoilerplate(Boilerplate instance, Long id = null,
                                          Long version = null)
    {
        assert null != instance
        assert id == instance.id
        assert version == instance.version
        assert 'My first boilerplate' == instance.name
        assert 'For [[description of service]] we ...' == instance.content
    }

    private static void verifyBoilerplates(Map model, int n = 3) {
        for (int i = 0; i < n; i++) {
            String subject = 'My boilerplate ' + (i + 1)
            assert subject == model.boilerplateInstanceList[i].name
        }
    }
}
