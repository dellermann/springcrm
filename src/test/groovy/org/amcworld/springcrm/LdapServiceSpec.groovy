/*
 * LdapServiceSpec.groovy
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


package org.amcworld.springcrm

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.directory.groovyldap.LDAP
import spock.lang.Specification


@TestFor(LdapService)
@Mock([Config, LdapService, LdapSyncStatus, Organization, Person])
class LdapServiceSpec extends Specification {

    //-- Instance variables ---------------------

    Organization org
    Person person


    //-- Fixture methods ------------------------

    void setup() {
        makeOrganizationFixture()
        org = Organization.first()

        makePersonFixture(org)
        person = Person.first()
    }


    //-- Feature methods ------------------------

    def 'Configuration without port'() {
        given: 'a configuration'
        mockDomain Config, [
            [name: 'ldapHost', value: 'localhost'],
            [name: 'ldapBindDn', value: 'cn=admin,dc=example,dc=org'],
            [name: 'ldapBindPasswd', value: 'secret']
        ]

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(1) { String host, String bindDn, String bindPw ->
            assert 'ldap://localhost' == host
            assert 'cn=admin,dc=example,dc=org' == bindDn
            assert 'secret' == bindPw
        }

        when: 'I save the person to DIT'
        service.save person
        ctrl.verify()

        then: 'there nothing has been saved'
        notThrown(Exception)
    }

    def 'Configuration with port'() {
        given: 'a configuration'
        mockDomain Config, [
            [name: 'ldapHost', value: 'localhost'],
            [name: 'ldapPort', value: '400'],
            [name: 'ldapBindDn', value: 'cn=admin,dc=example,dc=org'],
            [name: 'ldapBindPasswd', value: 'secret']
        ]

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(1) { String host, String bindDn, String bindPw ->
            assert 'ldap://localhost:400' == host
            assert 'cn=admin,dc=example,dc=org' == bindDn
            assert 'secret' == bindPw
        }

        when: 'I save the person to DIT'
        service.save person
        ctrl.verify()

        then: 'there nothing has been saved'
        notThrown(Exception)
    }

    def 'Delete with non-configured LDAP server'() {
        given: 'an empty configuration'
        mockDomain Config

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(0) { -> }

        when: 'I delete the person from DIT'
        service.delete person
        ctrl.verify()

        then: 'there nothing has been deleted'
        notThrown(Exception)
    }

    def 'Delete an existing person'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'an LDAP sync status entry'
        mockDomain LdapSyncStatus, [
            [itemId: 1, dn: 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org']
        ]

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(1) { String host, String bindDn, String bindPw ->
            new LDAP()
        }
        ctrl.demand.exists(1) { String dn ->
            assert 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == dn
            true
        }
        ctrl.demand.delete(1) { String dn ->
            assert 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == dn
        }

        when: 'I delete the person from DIT'
        service.delete person
        ctrl.verify()

        then: 'there is no entry in DIT'
        notThrown(Exception)

        and: 'no LDAP sync status entry'
        0 == LdapSyncStatus.count()
    }

    def 'Delete a person not existing in sync table'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(1) { String host, String bindDn, String bindPw ->
            new LDAP()
        }
        ctrl.demand.delete(0) { String dn -> }

        when: 'I delete the person from DIT'
        service.delete person
        ctrl.verify()

        then: 'there is no entry in DIT'
        notThrown(Exception)

        and: 'no LDAP sync status entry'
        0 == LdapSyncStatus.count()
    }

    def 'Delete a person not existing in DIT'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'an LDAP sync status entry'
        mockDomain LdapSyncStatus, [
            [itemId: 1, dn: 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org']
        ]

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(1) { String host, String bindDn, String bindPw ->
            new LDAP()
        }
        ctrl.demand.exists(1) { String dn ->
            assert 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == dn
            false
        }
        ctrl.demand.delete(0) { String dn -> }

        when: 'I delete the person from DIT'
        service.delete person
        ctrl.verify()

        then: 'there is no entry in DIT'
        notThrown(Exception)

        and: 'no LDAP sync status entry'
        0 == LdapSyncStatus.count()
    }

    def 'Save with non-configured LDAP server'() {
        given: 'an empty configuration'
        mockDomain Config

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(0) { -> }

        when: 'I save the person to DIT'
        service.save person
        ctrl.verify()

        then: 'there nothing has been saved'
        notThrown(Exception)
    }

    def 'Save a new person'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(1) { String host, String bindDn, String bindPw ->
            new LDAP()
        }
        ctrl.demand.add(1) { String dn, Map data ->
            assert 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == dn
            assert ['top', 'inetOrgPerson'] == data.objectclass
            assert 'Daniel' == data.givenname
            assert 'Ellermann' == data.sn
            assert 'Daniel Ellermann' == data.displayname
            assert 'AMC World Technologies GmbH' == data.o
            assert 'Fischerinsel 1' == data.street
            assert '10179' == data.postalcode
            assert 'Berlin' == data.l
            assert 'Berlin' == data.st
            assert 'Fischerinsel 1, 10179 Berlin' == data.postaladdress
            assert ['123456789', '987654321'] == data.telephonenumber
            assert ['daniel@example.com', 'info@example.com'] == data.mail
            assert 'http://www.example.com' == data.labeleduri
        }

        when: 'I save the person to DIT'
        service.save person
        ctrl.verify()

        then: 'there is one entry in DIT'
        notThrown(Exception)

        and: 'one LDAP sync status entry'
        LdapSyncStatus status = LdapSyncStatus.first()
        null != status
        1 == status.itemId
        'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == status.dn
    }

    def 'Save an existing person'() {
        given: 'some configuration data'
        makeConfigFixture()

        and: 'an LDAP sync status entry'
        mockDomain LdapSyncStatus, [
            [itemId: 1, dn: 'cn=Ellermann Peter,ou=contacts,dc=example,dc=org']
        ]

        and: 'a mock LDAP class'
        def ctrl = mockFor(LDAP)
        ctrl.demand.static.newInstance(1) { String host, String bindDn, String bindPw ->
            new LDAP()
        }
        ctrl.demand.exists(1) { String dn ->
            assert 'cn=Ellermann Peter,ou=contacts,dc=example,dc=org' == dn
            true
        }
        ctrl.demand.delete(1) { String dn ->
            assert 'cn=Ellermann Peter,ou=contacts,dc=example,dc=org' == dn
        }
        ctrl.demand.add(1) { String dn, Map data ->
            assert 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == dn
            assert ['top', 'inetOrgPerson'] == data.objectclass
            assert 'Daniel' == data.givenname
            assert 'Ellermann' == data.sn
            assert 'Daniel Ellermann' == data.displayname
            assert 'AMC World Technologies GmbH' == data.o
            assert 'Fischerinsel 1' == data.street
            assert '10179' == data.postalcode
            assert 'Berlin' == data.l
            assert 'Berlin' == data.st
            assert 'Fischerinsel 1, 10179 Berlin' == data.postaladdress
            assert ['123456789', '987654321'] == data.telephonenumber
            assert ['daniel@example.com', 'info@example.com'] == data.mail
            assert 'http://www.example.com' == data.labeleduri
        }

        when: 'I save the person to DIT'
        service.save person
        ctrl.verify()

        then: 'there is one entry in DIT'
        notThrown(Exception)

        and: 'one LDAP sync status entry'
        LdapSyncStatus status = LdapSyncStatus.first()
        null != status
        1 == status.itemId
        'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == status.dn
    }


    //-- Non-public methods ---------------------

    protected void makeConfigFixture() {
        mockDomain Config, [
            [name: 'ldapHost', value: 'localhost'],
            [name: 'ldapPort', value: '400'],
            [name: 'ldapBindDn', value: 'cn=admin,dc=example,dc=org'],
            [name: 'ldapBindPassword', value: 'secret'],
            [name: 'ldapContactDn', value: 'ou=contacts,dc=example,dc=org']
        ]
    }

    protected void makeOrganizationFixture() {
        mockDomain Organization, [
            [
                id: 1, number: 10000, recType: 1,
                name: 'AMC World Technologies GmbH', legalForm: 'GmbH',
                billingAddr: new Address(
                    street: '45, Tulip rd.',
                    postalCode: '39339',
                    location: 'Mt Helena',
                    state: 'NY',
                    country: 'USA'
                ), shippingAddr: new Address(),
                phone: '987654321',
                email1: 'info@example.com',
                website: 'http://www.example.com'
            ]
        ]
    }

    protected void makePersonFixture(Organization org) {
        mockDomain Person, [
            [
                id: 1, number: 10000, organization: org, firstName: 'Daniel',
                lastName: 'Ellermann',
                mailingAddr: new Address(
                    street: 'Fischerinsel 1',
                    postalCode: '10179',
                    location: 'Berlin',
                    state: 'Berlin',
                    country: 'Deutschland'
                ),
                otherAddr: new Address(), phone: '123456789',
                email1: 'daniel@example.com',
                email2: 'info@example.com'

            ]
        ]
    }
}
