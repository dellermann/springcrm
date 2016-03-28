/*
 * LdapServiceSpec.groovy
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
import org.amcworld.springcrm.ldap.Ldap
import org.amcworld.springcrm.ldap.LdapFactory
import org.apache.directory.groovyldap.LDAP
import spock.lang.Specification


@TestFor(LdapService)
@Mock([Config, LdapSyncStatus, Organization, Person])
class LdapServiceSpec extends Specification {

    //-- Fields ---------------------------------

    Organization org = makeOrganization()
    Person person = makePerson(org)


    //-- Feature methods ------------------------

    def 'No delete if no host has been configured'() {
        given: 'a mocked LDAP'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newInstance(null, null, null, null) >> ldap
        service.ldapFactory = factory

        when: 'I call the delete method'
        service.delete person

        then: 'no LDAP method has been called'
        0 * ldap.exists(_)
        0 * ldap.delete(_)
    }

    def 'No delete if an empty host has been configured'() {
        given: 'a configuration'
        mockDomain Config, [
            [name: 'ldapHost', value: '']
        ]

        and: 'a mocked LDAP'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newInstance('', null, null, null) >> ldap
        service.ldapFactory = factory

        when: 'I call the delete method'
        service.delete person

        then: 'no LDAP method has been called'
        0 * ldap.exists(_)
        0 * ldap.delete(_)
    }

    def 'No save if no host has been configured'() {
        given: 'a mocked LDAP'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newInstance(null, null, null, null) >> ldap
        service.ldapFactory = factory

        when: 'I call the save method'
        service.save person

        then: 'no LDAP method has been called'
        0 * ldap.add(_, _)
    }

    def 'No save if an empty host has been configured'() {
        given: 'a configuration'
        mockDomain Config, [
            [name: 'ldapHost', value: '']
        ]

        and: 'a mocked LDAP'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newInstance('', null, null, null) >> ldap
        service.ldapFactory = factory

        when: 'I call the save method'
        service.save person

        then: 'no LDAP method has been called'
        0 * ldap.add(_, _)
    }

    def 'Establish an LDAP connection without bind data'() {
        given: 'a configuration'
        mockDomain Config, [
            [name: 'ldapHost', value: 'localhost']
        ]

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', null, null, null) >> ldap
        service.ldapFactory = factory

        and: 'a mocked sync status'
        mockDomain LdapSyncStatus, [
            [itemId: person.id, dn: 'cn=foo']
        ]

        when: 'I call for example the delete method'
        service.delete person

        then: 'the LDAP methods have been called'
        1 * ldap.exists(_) >> true
        1 * ldap.delete(_)
    }

    def 'Establish an LDAP connection without port'() {
        given: 'a configuration'
        mockDomain Config, [
            [name: 'ldapHost', value: 'localhost'],
            [name: 'ldapBindDn', value: 'cn=admin,dc=example,dc=org'],
            [name: 'ldapBindPasswd', value: 'secret']
        ]

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', null) >> ldap
        service.ldapFactory = factory

        and: 'a mocked sync status'
        mockDomain LdapSyncStatus, [
            [itemId: person.id, dn: 'cn=foo']
        ]

        when: 'I call for example the delete method'
        service.delete person

        then: 'the LDAP methods have been called'
        1 * ldap.exists(_) >> true
        1 * ldap.delete(_)
    }

    def 'Establish an LDAP connection with port'() {
        given: 'a configuration'
        makeConfig()

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', 400) >> ldap
        service.ldapFactory = factory

        and: 'a mocked sync status'
        mockDomain LdapSyncStatus, [
            [itemId: person.id, dn: 'cn=foo']
        ]

        when: 'I call for example the delete method'
        service.delete person

        then: 'the LDAP methods have been called'
        1 * ldap.exists(_) >> true
        1 * ldap.delete(_)
    }

    def 'Delete a person not existing in sync table'() {
        given: 'a configuration'
        makeConfig()

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', 400) >> ldap
        service.ldapFactory = factory

        when: 'I delete the person from DIT'
        service.delete person

        then: 'no LDAP entry has been deleted'
        0 * ldap.exists(_)
        0 * ldap.delete(_)
    }

    def 'Delete a person not existing in DIT'() {
        given: 'a configuration'
        makeConfig()

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()
        1 * ldap.exists('cn=Ellermann Daniel,ou=contacts,dc=example,dc=org') >> false

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', 400) >> ldap
        service.ldapFactory = factory

        and: 'an LDAP sync status entry'
        mockDomain LdapSyncStatus, [
            [
                itemId: person.id,
                dn: 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org'
            ]
        ]

        when: 'I delete the person from DIT'
        service.delete person

        then: 'the LDAP entry is not deleted'
        0 * ldap.delete('cn=Ellermann Daniel,ou=contacts,dc=example,dc=org')
    }

    def 'Delete an existing person'() {
        given: 'a configuration'
        makeConfig()

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()
        1 * ldap.exists('cn=Ellermann Daniel,ou=contacts,dc=example,dc=org') >> true

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', 400) >> ldap
        service.ldapFactory = factory

        and: 'an LDAP sync status entry'
        mockDomain LdapSyncStatus, [
            [
                itemId: person.id,
                dn: 'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org'
            ]
        ]

        when: 'I delete the person from DIT'
        service.delete person

        then: 'the LDAP entry has been deleted'
        1 * ldap.delete('cn=Ellermann Daniel,ou=contacts,dc=example,dc=org')

        and: 'there is no LDAP sync status entry'
        0 == LdapSyncStatus.count()
    }

    def 'Save a new person'() {
        given: 'a configuration'
        makeConfig()

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', 400) >> ldap
        service.ldapFactory = factory

        when: 'I save the person to DIT'
        service.save person

        then: 'there is one entry in DIT'
        1 * ldap.add('cn=Ellermann Daniel,ou=contacts,dc=example,dc=org', _) >> { String dn, Map data ->
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

        and: 'there is one LDAP sync status entry'
        LdapSyncStatus status = LdapSyncStatus.first()
        1 == status.itemId
        'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == status.dn
    }

    def 'Save a person only existing in sync table'() {
        given: 'a configuration'
        makeConfig()

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()
        ldap.exists('cn=Ellermann Peter,ou=contacts,dc=example,dc=org') >> false

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', 400) >> ldap
        service.ldapFactory = factory

        and: 'an LDAP sync status entry'
        mockDomain LdapSyncStatus, [
            [
                itemId: person.id,
                dn: 'cn=Ellermann Peter,ou=contacts,dc=example,dc=org'
            ]
        ]

        when: 'I save the person to DIT'
        service.save person

        then: 'the no old LDAP entry has been deleted'
        0 * ldap.delete(_)

        and: 'a new LDAP entry has been created'
        1 * ldap.add('cn=Ellermann Daniel,ou=contacts,dc=example,dc=org', _) >> { String dn, Map data ->
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

        and: 'there is one LDAP sync status entry'
        LdapSyncStatus status = LdapSyncStatus.first()
        1 == status.itemId
        'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == status.dn
    }

    def 'Save an existing person'() {
        given: 'a configuration'
        makeConfig()

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()
        ldap.exists('cn=Ellermann Peter,ou=contacts,dc=example,dc=org') >> true

        and: 'a mocked LDAP factory'
        LdapFactory factory = Mock()
        factory.newLdap('localhost', 'cn=admin,dc=example,dc=org', 'secret', 400) >> ldap
        service.ldapFactory = factory

        and: 'an LDAP sync status entry'
        mockDomain LdapSyncStatus, [
            [
                itemId: person.id,
                dn: 'cn=Ellermann Peter,ou=contacts,dc=example,dc=org'
            ]
        ]

        when: 'I save the person to DIT'
        service.save person

        then: 'the old LDAP entry has been deleted'
        1 * ldap.delete('cn=Ellermann Peter,ou=contacts,dc=example,dc=org')

        and: 'a new LDAP entry has been created'
        1 * ldap.add('cn=Ellermann Daniel,ou=contacts,dc=example,dc=org', _) >> { String dn, Map data ->
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

        and: 'there is one LDAP sync status entry'
        LdapSyncStatus status = LdapSyncStatus.first()
        1 == status.itemId
        'cn=Ellermann Daniel,ou=contacts,dc=example,dc=org' == status.dn
    }


    //-- Non-public methods ---------------------

    private void makeConfig() {
        mockDomain Config, [
            [name: 'ldapHost', value: 'localhost'],
            [name: 'ldapPort', value: '400'],
            [name: 'ldapBindDn', value: 'cn=admin,dc=example,dc=org'],
            [name: 'ldapBindPasswd', value: 'secret'],
            [name: 'ldapContactDn', value: 'ou=contacts,dc=example,dc=org']
        ]
    }

    private Organization makeOrganization() {
        def org = new Organization(
            number: 10000, recType: 1,
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
        )
        mockDomain Organization, [org]

        org
    }

    private Person makePerson(Organization org) {
        def p = new Person(
            number: 10000, organization: org, firstName: 'Daniel',
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
        )
        mockDomain Person, [p]

        p
    }
}
