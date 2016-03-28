/*
 * LdapFactorySpec.groovy
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


class LdapFactorySpec extends Specification {

    //-- Fields ---------------------------------

    LdapFactory instance = LdapFactory.instance


    //-- Feature methods ------------------------

    def 'Class is singleton'() {
        when: 'I obtain two instances'
        def i1 = LdapFactory.instance
        def i2 = LdapFactory.instance

        then: 'both the instances are the same'
        i1.is i2
    }

    def 'Cannot obtain LDAP instance with invalid host name'() {
        when: 'I create an LDAP instance'
        instance.newLdap(null, 'foo', 'secret')

        then: 'an exception is thrown'
        thrown IllegalArgumentException

        when: 'I create another LDAP instance'
        instance.newLdap('', 'foo', 'secret')

        then: 'an exception is thrown'
        thrown IllegalArgumentException
    }

    def 'Obtain an LDAP instance with host name'() {
        when: 'I obtain an LDAP instance'
        def ldap = instance.newLdap('ldap.example.com', null, null)

        then: 'I get a valid LDAP object'
        'ldap://ldap.example.com' == ldap.url
        null == ldap.bindDn
        null == ldap.bindPasswd
    }

    def 'Obtain an LDAP instance with bind data'() {
        when: 'I obtain an LDAP instance'
        def ldap = instance.newLdap('ldap.example.com', 'cn=jdoe', 'secret')

        then: 'I get a valid LDAP object'
        'ldap://ldap.example.com' == ldap.url
        'cn=jdoe' == ldap.bindDn
        'secret' == ldap.bindPasswd
    }

    def 'Obtain an LDAP instance with port'() {
        when: 'I obtain an LDAP instance'
        def ldap = instance.newLdap(
            'ldap.example.com', 'cn=jdoe', 'secret', 3889
        )

        then: 'I get a valid LDAP object'
        'ldap://ldap.example.com:3889' == ldap.url
        'cn=jdoe' == ldap.bindDn
        'secret' == ldap.bindPasswd
    }

    def 'Obtain an LDAP instance with different protocol'() {
        when: 'I obtain an LDAP instance'
        def ldap = instance.newLdap(
            'ldap.example.com', 'cn=jdoe', 'secret', 3889, 'ldaps'
        )

        then: 'I get a valid LDAP object'
        'ldaps://ldap.example.com:3889' == ldap.url
        'cn=jdoe' == ldap.bindDn
        'secret' == ldap.bindPasswd
    }
}
