/*
 * ConfigTests.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


/**
 * The class {@code ConfigTests} contains the unit test cases for
 * {@code Config}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(Config)
@Mock(Config)
class ConfigTests {

    //-- Public methods -------------------------

    void testConstraints() {
        mockForConstraintsTests(Config)

        def config = new Config()
        assert !config.validate()
        assert 'nullable' == config.errors['name']

        config = new Config(name: '')
        assert !config.validate()
        assert 'blank' == config.errors['name']

        config = new Config(name: 'Test entry', value: 'Test value')
        assert config.validate()
    }

    void testAsType() {
        def config = new Config(name: 'int-test', value: '10')
        assert 10 == (config as int)
        config = new Config(name: 'float-test', value: '10.34')
        assert 10.34d == (config as double)
        config = new Config(name: 'boolean-test', value: 'true')
        assert (config as boolean)
        def d = new Date()
        d[Calendar.MILLISECOND] = 0
        config = new Config(name: 'date-test', value: d.toString())
        assert d == (config as Date)
        def cal = d.toCalendar()
        config = new Config(name: 'date-test', value: d.toString())
        assert cal == (config as Calendar)
    }

    void testEquals() {
        def config1 = new Config(name: 'foo', value: 'foo1 value')
        def config2 = new Config(name: 'foo', value: 'foo2 value')
        def config3 = new Config(name: 'bar', value: 'bar value')
        assert config1 == config2
        assert config1 != config3
    }

    void testHashCode() {
        def config1 = new Config(name: 'foo', value: 'foo1 value')
        def config2 = new Config(name: 'foo', value: 'foo2 value')
        def config3 = new Config(name: 'bar', value: 'bar value')
        assert config1.hashCode() == config2.hashCode()
        assert config1.hashCode() != config3.hashCode()
    }

    void testToString() {
        def config = new Config(name: 'Test entry', value: 'Test value')
        assert 'Test entry' == config.toString()
    }
}
