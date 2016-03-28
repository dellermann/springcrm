/*
 * LdapSpec.groovy
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


package org.amcworld.springcrm.ldap

import spock.lang.Specification


class LdapSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Empty instances have default values'() {
        when: 'I create an empty instance'
        def instance = new Ldap()

        then: 'the properties are set correctly'
        'ldap://localhost:389/' == instance.url
        null == instance.bindDn
        null == instance.bindPasswd
    }

    def 'An instance with a URL has the URL set'() {
        when: 'I create an instance with a URL'
        def instance = new Ldap('ldaps://ldap.example.com:3889/')

        then: 'the properties are set correctly'
        'ldaps://ldap.example.com:3889/' == instance.url
        null == instance.bindDn
        null == instance.bindPasswd
    }

    def 'An instance with all parameters has all properties set'() {
        when: 'I create an instance with a URL'
        def instance = new Ldap(
            'ldaps://ldap.example.com:3889/', 'cn=admin,o=example,dc=com',
            'secret'
        )

        then: 'the properties are set correctly'
        'ldaps://ldap.example.com:3889/' == instance.url
        'cn=admin,o=example,dc=com' == instance.bindDn
        'secret' == instance.bindPasswd
    }
}
