/*
 * GoogleSyncFactorySpec.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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


package org.amcworld.springcrm.google

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.context.ApplicationContext
import spock.lang.Specification


@TestMixin(GrailsUnitTestMixin)
class GoogleSyncFactorySpec extends Specification {

    //-- Feature methods ------------------------

    def 'Google sync factory is singleton'() {
        when: 'I obtain an instance of the Google sync factory'
        def gsf1 = GoogleSyncFactory.defaultInstance

        then: 'I get a valid instance'
        null != gsf1

        when: 'I obtain another instance of this Google sync factory'
        def gsf2 = GoogleSyncFactory.defaultInstance

        then: 'I get the same instance'
        gsf2.is gsf1
    }

    def 'Obtain a contact sync instance'() {
        given: 'a factory'
        def factory = GoogleSyncFactory.defaultInstance

        and: 'a mocked application context'
        def context = Mock(ApplicationContext)
        context.getBean(_) >> new GoogleContactSync()

        and: 'a mocked Grails application'
        factory.grailsApplication = Mock(GrailsApplication)
        factory.grailsApplication.getMainContext() >> context

        when: 'I obtain a sync instance'
        def sync = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')

        then: 'I get the correct instance'
        sync instanceof GoogleContactSync
        'jsmith' == sync.userName
    }

    def 'Obtain a calendar sync instance'() {
        given: 'a factory'
        def factory = GoogleSyncFactory.defaultInstance

        and: 'a mocked application context'
        def context = Mock(ApplicationContext)
        context.getBean(_) >> new GoogleCalendarSync()

        and: 'a mocked Grails application'
        factory.grailsApplication = Mock(GrailsApplication)
        factory.grailsApplication.getMainContext() >> context

        when: 'I obtain a sync instance'
        def sync = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')

        then: 'I get the correct instance'
        sync instanceof GoogleCalendarSync
        'jsmith' == sync.userName
    }

    def 'There is only one contact sync instance per user'() {
        given: 'a factory'
        def factory = GoogleSyncFactory.defaultInstance

        and: 'a mocked application context'
        def context = Mock(ApplicationContext)
        context.getBean(_) >> new GoogleContactSync()

        and: 'a mocked Grails application'
        factory.grailsApplication = Mock(GrailsApplication)
        factory.grailsApplication.getMainContext() >> context

        when: 'I obtain two sync instances'
        def sync1 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')

        then: 'I get the same instance'
        sync1.is sync2
    }

    def 'There is only one calendar sync instance per user'() {
        given: 'a factory'
        def factory = GoogleSyncFactory.defaultInstance

        and: 'a mocked application context'
        def context = Mock(ApplicationContext)
        context.getBean(_) >> new GoogleCalendarSync()

        and: 'a mocked Grails application'
        factory.grailsApplication = Mock(GrailsApplication)
        factory.grailsApplication.getMainContext() >> context

        when: 'I obtain two sync instances'
        def sync1 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')

        then: 'I get the same instance'
        sync1.is sync2
    }

    def 'There are different contact sync instances for different users'() {
        given: 'a factory'
        def factory = GoogleSyncFactory.defaultInstance

        and: 'a mocked application context'
        def context = Mock(ApplicationContext)
        context.getBean(_) >> new GoogleContactSync()

        and: 'a mocked Grails application'
        factory.grailsApplication = Mock(GrailsApplication)
        factory.grailsApplication.getMainContext() >> context

        when: 'I obtain two sync instances'
        def sync1 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jdoe')

        then: 'I get different instances'
        !sync1.is(sync2)
        'jsmith' == sync1.userName
        'jdoe' == sync2.userName
    }

    def 'There are different calendar sync instances for different users'() {
        given: 'a factory'
        def factory = GoogleSyncFactory.defaultInstance

        and: 'a mocked application context'
        def context = Mock(ApplicationContext)
        context.getBean(_) >> new GoogleCalendarSync()

        and: 'a mocked Grails application'
        factory.grailsApplication = Mock(GrailsApplication)
        factory.grailsApplication.getMainContext() >> context

        when: 'I obtain two sync instances'
        def sync1 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jdoe')

        then: 'I get different instances'
        !sync1.is(sync2)
        'jsmith' == sync1.userName
        'jdoe' == sync2.userName
    }

    def 'There are different sync instances for different data types'() {
        given: 'a factory'
        def factory = GoogleSyncFactory.defaultInstance

        and: 'a mocked application context'
        def context = Mock(ApplicationContext)
        context.getBean(GoogleSyncType.CONTACT.beanName) >> new GoogleContactSync()
        context.getBean(GoogleSyncType.CALENDAR.beanName) >> new GoogleCalendarSync()

        and: 'a mocked Grails application'
        factory.grailsApplication = Mock(GrailsApplication)
        factory.grailsApplication.getMainContext() >> context

        when: 'I obtain two different types of sync instances'
        def sync1 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')

        then: 'I get different instances'
        !sync1.is(sync2)
        sync1 instanceof GoogleContactSync
        sync2 instanceof GoogleCalendarSync
        'jsmith' == sync1.userName
        'jsmith' == sync2.userName
    }
}
