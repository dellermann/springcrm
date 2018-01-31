/*
 * ConfigServiceSpec.groovy
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
import grails.testing.services.ServiceUnitTest
import java.beans.Introspector
import org.grails.datastore.mapping.services.Service
import spock.lang.Specification


class ConfigServiceSpec extends Specification
    implements ServiceUnitTest<ConfigService>, DomainUnitTest<Config>
{

    //-- Fixture methods ------------------------

    void 'Can get boolean value'(String value, Boolean b) {
        given:
        def config = new Config(value: value)
        config.id = 'a'
        mockDomain Config, [config]
        Config.count()      // use to persist instances

        expect:
        b == service.getBoolean('a')
        null == service.getBoolean('b')
        true == service.getBoolean('b', true)

        where:
        value   || b
        null    || null
        ''      || null
        'false' || false
        'False' || false
        'true'  || true
        'True'  || true
        'TRUE'  || true
        'TrUe'  || true
        'foo'   || false
    }

    void 'Can get calendar value'() {
        given:
        Date d1 = new Date()
        Date d2 = d1 + 45
        Date d3 = d1 - 23

        and:
        def config1 = new Config(value: d1.toString())
        config1.id = 'a1'
        def config2 = new Config(value: d2.toString())
        config2.id = 'a2'
        def config3 = new Config(value: d3.toString())
        config3.id = 'a3'
        def config4 = new Config(value: null)
        config4.id = 'a4'
        mockDomain Config, [config1, config2, config3]
        Config.count()      // use to persist instances

        expect:
        /* comparing Calendar instances directly doesn't work */
        d1.toCalendar().time.toString() ==
            service.getCalendar('a1').time.toString()
        d2.toCalendar().time.toString() ==
            service.getCalendar('a2').time.toString()
        d3.toCalendar().time.toString() ==
            service.getCalendar('a3').time.toString()
        null == service.getCalendar('a4')
        null == service.getCalendar('b')
        d3.toCalendar().time.toString() ==
            service.getCalendar('b', d3.toCalendar()).time.toString()
    }

    void 'Can get date value'() {
        given:
        Date d1 = new Date()
        Date d2 = d1 + 45
        Date d3 = d1 - 23

        and:
        def config1 = new Config(value: d1.toString())
        config1.id = 'a1'
        def config2 = new Config(value: d2.toString())
        config2.id = 'a2'
        def config3 = new Config(value: d3.toString())
        config3.id = 'a3'
        def config4 = new Config(value: null)
        config4.id = 'a4'
        mockDomain Config, [config1, config2, config3]
        Config.count()      // use to persist instances

        expect:
        /* comparing Date instances directly doesn't work */
        d1.toString() == service.getDate('a1').toString()
        d2.toString() == service.getDate('a2').toString()
        d3.toString() == service.getDate('a3').toString()
        null == service.getDate('a4')
        null == service.getDate('b')
        d3.toString() == service.getDate('b', d3).toString()
    }

    void 'Can get integer value'(String value, Integer i) {
        given:
        def config = new Config(value: value)
        config.id = 'a'
        mockDomain Config, [config]
        Config.count()      // use to persist instances

        expect:
        i == service.getInteger('a')
        null == service.getInteger('b')
        45i == service.getInteger('b', 45i)

        where:
        value   || i
        null    || null
        ''      || null
        '0'     || 0
        '1'     || 1
        '10'    || 10
        '17549' || 17549
    }

    void 'Can get long value'(String value, Long l) {
        given:
        def config = new Config(value: value)
        config.id = 'a'
        mockDomain Config, [config]
        Config.count()      // use to persist instances

        expect:
        l == service.getLong('a')
        null == service.getLong('b')
        3274L == service.getLong('b', 3274L)

        where:
        value       || l
        null        || null
        ''          || null
        '0'         || 0
        '1'         || 1
        '10'        || 10
        '17549'     || 17549
        '975397503' || 975397503
    }

    void 'Can get string value'(String value, String s) {
        given:
        def config = new Config(value: value)
        config.id = 'a'
        mockDomain Config, [config]
        Config.count()      // use to persist instances

        expect:
        s == service.getString('a')
        null == service.getString('b')
        'foo' == service.getString('b', 'foo')

        where:
        value   || s
        null    || null
        ''      || null
        '0'     || '0'
        'a'     || 'a'
        'abc'   || 'abc'
        'foo'   || 'foo'
    }

    void 'Can store configuration value'(Object value, String e) {
        when:
        Config config = service.store('a', value)

        then:
        'a' == config.id
        e == config.value

        and:
        1 == Config.count()

        and:
        Config c = Config.get('a')
        null != c
        'a' == c.id
        e == c.value

        where:
        value           || e
        null            || null
        ''              || null
        false           || 'false'
        true            || 'true'
        'abcdef'        || 'abcdef'
        'foobar'        || 'foobar'
        45730           || '45730'
        35_495_453_731  || '35495453731'
    }


    //-- Public methods -------------------------

    /*
     * XXX moved down from DataTest because line
     *
     *      Service service = (Service) dataStore.getService(serviceClass)
     *
     * throws a NoSuchMethodError when calling getService().  I really don't
     * know why.
     */
    void mockDataService(Class<?> serviceClass) {
        Service service = (Service) dataStore.getService(serviceClass)
        String name = Introspector.decapitalize(serviceClass.simpleName)
        if (!applicationContext.containsBean(name)) {
            applicationContext.beanFactory.autowireBean service
            service.datastore = dataStore
            applicationContext.beanFactory.registerSingleton name, service
        }
    }
}
