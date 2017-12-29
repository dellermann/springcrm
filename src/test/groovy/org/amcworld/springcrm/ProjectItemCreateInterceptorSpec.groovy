/*
 * ProjectItemCreateInterceptorSpec.groovy
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

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification


class ProjectItemCreateInterceptorSpec extends Specification
    implements InterceptorUnitTest<ProjectItemCreateInterceptor>
{

    //-- Feature methods ------------------------

    def 'Interceptor matches the correct controller/action pairs'(
        String c, String a, boolean b
    ) {
        when: 'I use a particular request'
        withRequest controller: c, action: a

        then: 'the interceptor does match or not'
        b == interceptor.doesMatch()

        where:
        c                   | a                     || b
        'call'              | null                  || false
        'organization'      | null                  || false
        'user'              | null                  || false
        'call'              | 'index'               || false
        'organization'      | 'index'               || false
        'user'              | 'index'               || false
        'call'              | 'save'                || true
        'organization'      | 'save'                || true
        'user'              | 'save'                || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No instance does not create project item'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project item has been created'
        0 == ProjectItem.count()

        when: 'I set another request attribute'
        request.foo = 'bar'

        and: 'call the interceptor'
        interceptor.after()

        then: 'no project item has been created'
        0 == ProjectItem.count()
    }

    def 'No entity does not create project item'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'a request attribute which is no entity'
        request.callInstance = 'bar'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project item has been created'
        0 == ProjectItem.count()
    }

    def 'No project does not create project item'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'a request attribute which is no entity'
        request.callInstance = new Call()

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project item has been created'
        0 == ProjectItem.count()
    }

    def 'No project phase does not create project item'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'a request attribute which is no entity'
        request.callInstance = new Call()

        and: 'some parameters'
        params.project = 5

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project item has been created'
        0 == ProjectItem.count()
    }

    def 'Not existing project does not create project item'() {
        given: 'a project'
        def p = makeProject()

        and: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'a request attribute which is no entity'
        request.callInstance = new Call()

        and: 'some parameters'
        params.project = p.id + 1
        params.projectPhase = 'planning'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project item has been created'
        0 == ProjectItem.count()
    }

    def 'An existing project creates a project item'() {
        given: 'a project'
        def p = makeProject()

        and: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'a request attribute which is no entity'
        def call = new Call(subject: 'Phone call')
        call.id = 453
        request.callInstance = call

        and: 'some parameters'
        params.project = p.id
        params.projectPhase = 'quote'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the project phase has been stored'
        def pNew = Project.get(p.id)
        ProjectPhase.quote == pNew.phase

        and: 'a project item has been created and assigned to the project'
        1 == pNew.items.size()

        and: 'the project item data are correct'
        ProjectItem item = pNew.items.first()
        ProjectPhase.quote == item.phase
        'call' == item.controller
        453L == item.itemId
        'Phone call' == item.title
    }


    //-- Non-public methods ---------------------

    private Project makeProject() {
        def p = new Project(
			number: 1993,
			title: 'Website',
			description: 'Develope a website for my best client.',
			organization: new Organization(name: 'AMC World'),
			person: new Person(firstName: 'Daniel', lastName: 'Ellermann'),
			phase: ProjectPhase.planning,
            status: new ProjectStatus()
		)
        p.save failOnError: true
    }
}
