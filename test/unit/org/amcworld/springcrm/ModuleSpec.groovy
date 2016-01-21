/*
 * ModuleSpec.groovy
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

import static org.amcworld.springcrm.Module.*

import spock.lang.Specification


class ModuleSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Get controllers of modules'(Module m) {
        when: 'I obtain the controllers of a module'
        List<String> controllers = m.controllers

        then: 'I get a valid list'
        null != controllers
        !controllers.empty

        where:
        m << Module.values()
    }

    def 'The associated controllers are an immutable list'() {
        given: 'the controllers of a module'
        List<String> controllers = REPORT.controllers

        when: 'I change that list'
        controllers << 'foo'

        then: 'I get an exception'
        thrown UnsupportedOperationException
    }

    def 'The associated controllers cannot be changed'() {
        when: 'I try to change the controllers of a module'
        REPORT.controllers = ['foo', 'bar']

        then: 'I get an exception'
        thrown ReadOnlyPropertyException
    }

    def 'Obtain modules by their names'() {
        expect:
        EnumSet.noneOf(Module) == Module.modulesByName(null)
        EnumSet.noneOf(Module) == Module.modulesByName([])
        EnumSet.of(REPORT) == Module.modulesByName(['REPORT'])
        EnumSet.of(REPORT, CALL) == Module.modulesByName(['CALL', 'REPORT'])
        EnumSet.of(REPORT, CALL, QUOTE) == Module.modulesByName(['CALL', 'REPORT', 'QUOTE'])
    }

    def 'Obtain modules by invalid names'() {
        when: 'I obtain modules by invalid names'
        Module.modulesByName(['REPORT', 'call'])

        then: 'I get an exception'
        thrown IllegalArgumentException
    }

    def 'Resolve modules to controllers'() {
        expect:
        [] as Set == Module.resolveModules(null)
        [] as Set == Module.resolveModules(EnumSet.noneOf(Module))
        ['call'] as Set == Module.resolveModules(EnumSet.of(CALL))
        ['organization', 'person'] as Set == Module.resolveModules(EnumSet.of(CONTACT))
        ['organization', 'person', 'invoice'] as Set == Module.resolveModules(EnumSet.of(CONTACT, INVOICE))
        ['organization', 'person', 'invoice', 'dunning', 'creditMemo', 'report'] as Set == Module.resolveModules(EnumSet.of(CONTACT, INVOICE, REPORT))
    }
}
