/*
 * ProjectRememberInterceptorSpec.groovy
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


@TestFor(ProjectRememberInterceptor)
@Mock(Project)
class ProjectRememberInterceptorSpec extends Specification {

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
        'call'              | 'create'              || true
        'organization'      | 'create'              || true
        'user'              | 'create'              || true
    }

    def 'All interceptor methods return true'() {
        expect:
        interceptor.after()
        interceptor.before()
    }

    def 'No preparations if no project has been specified'() {
        when: 'I call the interceptor'
        interceptor.before()

        then: 'no organization and person have been specified'
        null == params.organization
        null == params.person
    }

    def 'No preparations if no project has been found'() {
        given: 'a project'
        def p = makeProject()

        and: 'a request parameter'
        params.project = p.id + 1

        when: 'I call the interceptor'
        interceptor.before()

        then: 'no organization and person have been specified'
        null == params.organization
        null == params.person
    }

    def 'If project has been found set organization and person'() {
        given: 'a project'
        def p = makeProject()

        and: 'a request parameter'
        params.project = p.id

        when: 'I call the interceptor'
        interceptor.before()

        then: 'no organization and person have been specified'
        p.organization == params.organization
        p.person == params.person
    }

    def 'If no model has been specified model is unchanged'() {
        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project and phase has been set'
        null == interceptor.model?.project
        null == interceptor.model?.projectPhase
    }

    def 'If no project and phase has been specified model is unchanged'() {
        given: 'an empty model'
        interceptor.model = [: ]

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project and phase has been set'
        null == interceptor.model.project
        null == interceptor.model.projectPhase
    }

    def 'If no phase has been specified model is unchanged'() {
        given: 'an empty model'
        interceptor.model = [: ]

        and: 'a project'
        params.project = new Project()

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project and phase has been set'
        null == interceptor.model.project
        null == interceptor.model.projectPhase
    }

    def 'If no project has been specified model is unchanged'() {
        given: 'an empty model'
        interceptor.model = [: ]

        and: 'a project phase'
        params.projectPhase = 'planning'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'no project and phase has been set'
        null == interceptor.model.project
        null == interceptor.model.projectPhase
    }

    def 'If project and phase has been specified they are stored in model'() {
        given: 'an empty model'
        interceptor.model = [: ]

        and: 'a project and a project phase'
        def p = new Project()
        params.project = p
        params.projectPhase = 'planning'

        when: 'I call the interceptor'
        interceptor.after()

        then: 'project and phase has been set'
        p.is interceptor.model.project
        'planning' == interceptor.model.projectPhase
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
