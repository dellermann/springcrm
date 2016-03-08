/*
 * SecurityServiceSpec.groovy
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


package org.amcworld.springcrm

import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(SecurityService)
class SecurityServiceSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Encrypt password'() {
        expect:
        encrypted == service.encryptPassword(pwd)

        where:
        pwd                     | encrypted
        ''                      | 'da39a3ee5e6b4b0d3255bfef95601890afd80709'
        'a'                     | '86f7e437faa5a7fce15d1ddcb9eaeaea377667b8'
        'b'                     | 'e9d71f5ee7c92d6dc9e92ffdad17b8bd49418f98'
        'c'                     | '84a516841ba77a5b4648de2cd0dfcb30ea46dbb4'
        'ab'                    | 'da23614e02469a0d7c7bd1bdab5c9c474b1904dc'
        'abc'                   | 'a9993e364706816aba3e25717850c26c9cd0d89d'
        'Test!34#X/494'         | 'da138f194448c3504e39b9c9bb941cf54a7eeb6e'
    }

    def 'Cannot encrypt null passwords'() {
        when: 'I try to encrypt a null password'
        service.encryptPassword(null)

        then:
        thrown(NullPointerException)
    }
}
