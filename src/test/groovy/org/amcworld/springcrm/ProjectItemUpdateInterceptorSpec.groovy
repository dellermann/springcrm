/*
 * ProjectItemUpdateInterceptorSpec.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(ProjectItemUpdateInterceptor)
@Mock([Project, ProjectItem, Call])
class ProjectItemUpdateInterceptorSpec extends Specification {

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
        'call'              | 'update'              || true
        'organization'      | 'update'              || true
        'user'              | 'update'              || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No instance does not update project item'() {
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

        then: 'no project item has been updated'
        0 == ProjectItem.count()
    }

    def 'No entity does not update project item'() {
        given: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'a request attribute which is no entity'
        request.callInstance = 'bar'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project item has been updated'
        0 == ProjectItem.count()
    }

    def 'An existing instance changes all referring project items'() {
        given: 'a project'
        def p = makeProject()

        and: 'a controller name'
        webRequest.controllerName = 'call'

        and: 'a request attribute which is no entity'
        def call = new Call(subject: 'Phone call')
        call.id = 456
        request.callInstance = call

        when: 'I call the interceptor'
        interceptor.after()

        then: 'the number of project items remains unchanged'
        def pNew = Project.get(p.id)
        p.items.size() == pNew.items.size()

        and: 'two project items have been altered'
        ProjectItem.findAllByItemId(456).every {
            it.controller == 'call' && it.title == 'Phone call'
        }

        and: 'one project item remained unchanged'
        ProjectItem.findAllByItemId(4730).every {
            it.controller == 'salesOrder' && it.title == 'Order #3'
        }
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
        p.items = [
            new ProjectItem(
                phase: ProjectPhase.planning, controller: 'call',
                itemId: 456, title: 'Call to client'
            ),
            new ProjectItem(
                phase: ProjectPhase.ordering, controller: 'salesOrder',
                itemId: 4730, title: 'Order #3'
            ),
            new ProjectItem(
                phase: ProjectPhase.implementation, controller: 'call',
                itemId: 456, title: 'Call to client'
            )
        ]
        p.save failOnError: true
    }
}
