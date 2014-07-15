/*
 * GoogleSyncFactorySpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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

import grails.spring.BeanBuilder
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import grails.util.Holders
import org.amcworld.springcrm.GoogleOAuthService
import spock.lang.Specification


@TestMixin(GrailsUnitTestMixin)
class GoogleSyncFactorySpec extends Specification {

    //-- Instance variables ---------------------

    def grailsApplication = Holders.grailsApplication


    //-- Fixture methods ------------------------

    def setup() {
        def builder = new BeanBuilder(grailsApplication.mainContext)
        builder.beans {
            googleOAuthService(GoogleOAuthService)
            googleSyncFactory(GoogleSyncFactory) { bean ->
                bean.factoryMethod = 'getDefaultInstance'
                grailsApplication = ref('grailsApplication')
            }
            "${GoogleSyncType.CONTACT.beanName}"(GoogleContactSync) { bean ->
                bean.singleton = false
                googleOAuthService = ref('googleOAuthService')
                messageSource = ref('messageSource')
            }
            "${GoogleSyncType.CALENDAR.beanName}"(GoogleCalendarSync) { bean ->
                bean.singleton = false
                googleOAuthService = ref('googleOAuthService')
                messageSource = ref('messageSource')
            }
        }
        grailsApplication.mainContext = builder.createApplicationContext()
    }


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
        when:
        def sync = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')

        then:
        sync instanceof GoogleContactSync
        'jsmith' == sync.userName
    }

    def 'Obtain a calendar sync instance'() {
        when:
        def sync = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')

        then:
        sync instanceof GoogleCalendarSync
        'jsmith' == sync.userName
    }

    def 'There is only one contact sync instance per user'() {
        when:
        def sync1 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')

        then:
        sync1.is sync2
    }

    def 'There is only one calendar sync instance per user'() {
        when:
        def sync1 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')

        then:
        sync1.is sync2
    }

    def 'There are different contact sync instances for different users'() {
        when:
        def sync1 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jdoe')

        then:
        !sync1.is(sync2)
        'jsmith' == sync1.userName
        'jdoe' == sync2.userName
    }

    def 'There are different calendar sync instances for different users'() {
        when:
        def sync1 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jdoe')

        then:
        !sync1.is(sync2)
        'jsmith' == sync1.userName
        'jdoe' == sync2.userName
    }

    def 'There are different sync instances for different data types'() {
        when:
        def sync1 = factory.getSyncInstance(GoogleSyncType.CONTACT, 'jsmith')
        def sync2 = factory.getSyncInstance(GoogleSyncType.CALENDAR, 'jsmith')

        then:
        !sync1.is(sync2)
        sync1 instanceof GoogleContactSync
        sync2 instanceof GoogleCalendarSync
        'jsmith' == sync1.userName
        'jsmith' == sync2.userName
    }


    //-- Non-public methods ---------------------

    protected GoogleSyncFactory getFactory() {
        grailsApplication.mainContext.getBean('googleSyncFactory')
    }
}
