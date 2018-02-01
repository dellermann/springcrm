/*
 * LdapServiceSpec.groovy
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

import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import javax.naming.NameAlreadyBoundException
import org.amcworld.springcrm.ldap.Ldap
import org.amcworld.springcrm.ldap.LdapFactory
import org.bson.types.ObjectId
import spock.lang.Specification


class LdapServiceSpec extends Specification
    implements ServiceUnitTest<LdapService>, DataTest
{

    //-- Feature methods ------------------------

    void 'No delete if no host has been configured'() {
        given: 'an LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        when: 'the delete method is called with a null host'
        service.delete new Person()

        then: 'no LDAP method has been called'
        1 * configService.getString('ldapHost') >> null
        0 * ldapFactory.newLdap(_, _, _, _, _)

        when: 'the delete method is called with an empty host'
        service.delete new Person()

        then: 'no LDAP method has been called'
        1 * configService.getString('ldapHost') >> ''
        0 * ldapFactory.newLdap(_, _, _, _, _)
    }

    void 'No save if no host has been configured'() {
        given: 'an LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        when: 'the delete method is called with a null host'
        service.save new Person()

        then: 'no LDAP method has been called'
        1 * configService.getString('ldapHost') >> null
        0 * ldapFactory.newLdap(_, _, _, _, _)

        when: 'the delete method is called with an empty host'
        service.save new Person()

        then: 'no LDAP method has been called'
        1 * configService.getString('ldapHost') >> ''
        0 * ldapFactory.newLdap(_, _, _, _, _)
    }

    void 'Delete a person not existing in sync table'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a LDAP sync status service instance'
        LdapSyncStatusService ldapSyncStatusService = Mock()
        service.ldapSyncStatusService = ldapSyncStatusService

        and: 'a person'
        def person = new Person(firstName: 'John', lastName: 'Doe')
        person.id = new ObjectId()

        when: 'the delete method is called'
        service.delete person

        then: 'the LDAP methods have been called'
        1 * configService.getString('ldapHost') >> 'localhost'
        1 * configService.getString('ldapBindDn') >>
            'cn=admin,dc=example,dc=org'
        1 * configService.getString('ldapBindPasswd') >> 'secret'
        1 * configService.getInteger('ldapPort') >> 389i
        1 * ldapFactory.newLdap(
            'localhost', 'cn=admin,dc=example,dc=org', 'secret', 389i
        ) >> ldap
        1 * ldapSyncStatusService.findByItemId(person.id) >> null
        0 * ldap.exists(_)
        0 * ldap.delete(_)
        0 * ldapSyncStatusService.delete(_)
    }

    def 'Delete a person not existing in DIT'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a LDAP sync status service instance'
        LdapSyncStatusService ldapSyncStatusService = Mock()
        service.ldapSyncStatusService = ldapSyncStatusService

        and: 'a person'
        def person = new Person(firstName: 'John', lastName: 'Doe')
        person.id = new ObjectId()

        and: 'an LDAP sync status'
        def status = new LdapSyncStatus(
            dn: 'cn=John Doe,dc=users,dc=example,dc=org'
        )
        status.id = new ObjectId()

        when: 'the delete method is called'
        service.delete person

        then: 'the LDAP methods have been called'
        1 * configService.getString('ldapHost') >> 'localhost'
        1 * configService.getString('ldapBindDn') >>
            'cn=admin,dc=example,dc=org'
        1 * configService.getString('ldapBindPasswd') >> 'secret'
        1 * configService.getInteger('ldapPort') >> 389i
        1 * ldapFactory.newLdap(
            'localhost', 'cn=admin,dc=example,dc=org', 'secret', 389i
        ) >> ldap
        1 * ldapSyncStatusService.findByItemId(person.id) >> status
        1 * ldap.exists(status.dn) >> false
        0 * ldap.delete(_)
        1 * ldapSyncStatusService.delete(status.id)
    }

    def 'Delete an existing person'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a LDAP sync status service instance'
        LdapSyncStatusService ldapSyncStatusService = Mock()
        service.ldapSyncStatusService = ldapSyncStatusService

        and: 'a person'
        def person = new Person(firstName: 'John', lastName: 'Doe')
        person.id = new ObjectId()

        and: 'an LDAP sync status'
        def status = new LdapSyncStatus(
            dn: 'cn=John Doe,dc=users,dc=example,dc=org'
        )
        status.id = new ObjectId()

        when: 'the delete method is called'
        service.delete person

        then: 'the LDAP methods have been called'
        1 * configService.getString('ldapHost') >> 'localhost'
        1 * configService.getString('ldapBindDn') >>
            'cn=admin,dc=example,dc=org'
        1 * configService.getString('ldapBindPasswd') >> 'secret'
        1 * configService.getInteger('ldapPort') >> 389i
        1 * ldapFactory.newLdap(
            'localhost', 'cn=admin,dc=example,dc=org', 'secret', 389i
        ) >> ldap
        1 * ldapSyncStatusService.findByItemId(person.id) >> status
        1 * ldap.exists(status.dn) >> true
        1 * ldap.delete(status.dn)
        1 * ldapSyncStatusService.delete(status.id)
    }

    def 'Save a new person'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a LDAP sync status service instance'
        LdapSyncStatusService ldapSyncStatusService = Mock()
        service.ldapSyncStatusService = ldapSyncStatusService

        and: 'a person'
        def person = makePerson()
        person.id = new ObjectId()

        when: 'the save method is called'
        service.save person

        then: 'the LDAP methods have been called'
        1 * configService.getString('ldapHost') >> 'localhost'
        1 * configService.getString('ldapBindDn') >>
            'cn=admin,dc=example,dc=org'
        1 * configService.getString('ldapBindPasswd') >> 'secret'
        1 * configService.getInteger('ldapPort') >> 389i
        1 * ldapFactory.newLdap(
            'localhost', 'cn=admin,dc=example,dc=org', 'secret', 389i
        ) >> ldap
        1 * ldapSyncStatusService.findByItemId(person.id) >> null
        1 * configService.getString('ldapContactDn') >>
            'dc=users,dc=example,dc=org'

        and: 'the entry is added to DIT'
        1 * ldap.add(
            'cn=Ellermann Daniel,dc=users,dc=example,dc=org',
            { Map data ->
                ['top', 'inetOrgPerson'] == data.objectclass &&
                'Daniel' == data.givenname &&
                'Ellermann' == data.sn &&
                'Daniel Ellermann' == data.displayname &&
                'AMC World Technologies GmbH' == data.o &&
                'Fischerinsel 1' == data.street &&
                '10179' == data.postalcode &&
                'Berlin' == data.l &&
                'Berlin' == data.st &&
                'Fischerinsel 1, 10179 Berlin' == data.postaladdress &&
                ['123456789', '987654321'] == data.telephonenumber &&
                ['daniel@example.com', 'info@example.com'] == data.mail &&
                'http://www.example.com' == data.labeleduri
            }
        )

        and: 'the LDAP sync status entry is stored'
        1 * ldapSyncStatusService.save({
            it.itemId == person.id &&
                it.dn == 'cn=Ellermann Daniel,dc=users,dc=example,dc=org'
        })
    }

    void 'Save a person only existing in sync table'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a LDAP sync status service instance'
        LdapSyncStatusService ldapSyncStatusService = Mock()
        service.ldapSyncStatusService = ldapSyncStatusService

        and: 'a person'
        def person = makePerson()
        person.id = new ObjectId()

        and: 'an LDAP sync status'
        def status = new LdapSyncStatus(
            dn: 'cn=Ellermann Daniel,dc=users,dc=example,dc=org',
            itemId: person.id
        )
        status.id = new ObjectId()

        when: 'the save method is called'
        service.save person

        then: 'the LDAP methods have been called'
        1 * configService.getString('ldapHost') >> 'localhost'
        1 * configService.getString('ldapBindDn') >>
            'cn=admin,dc=example,dc=org'
        1 * configService.getString('ldapBindPasswd') >> 'secret'
        1 * configService.getInteger('ldapPort') >> 389i
        1 * ldapFactory.newLdap(
            'localhost', 'cn=admin,dc=example,dc=org', 'secret', 389i
        ) >> ldap
        1 * ldapSyncStatusService.findByItemId(person.id) >> status
        1 * ldap.exists(status.dn) >> false
        0 * ldap.delete(_)
        1 * configService.getString('ldapContactDn') >>
            'dc=users,dc=example,dc=org'

        and: 'the entry is added to DIT'
        1 * ldap.add(
            'cn=Ellermann Daniel,dc=users,dc=example,dc=org',
            { Map data ->
                ['top', 'inetOrgPerson'] == data.objectclass &&
                    'Daniel' == data.givenname &&
                    'Ellermann' == data.sn &&
                    'Daniel Ellermann' == data.displayname &&
                    'AMC World Technologies GmbH' == data.o &&
                    'Fischerinsel 1' == data.street &&
                    '10179' == data.postalcode &&
                    'Berlin' == data.l &&
                    'Berlin' == data.st &&
                    'Fischerinsel 1, 10179 Berlin' == data.postaladdress &&
                    ['123456789', '987654321'] == data.telephonenumber &&
                    ['daniel@example.com', 'info@example.com'] == data.mail &&
                    'http://www.example.com' == data.labeleduri
            }
        )

        and: 'the LDAP sync status entry is stored'
        1 * ldapSyncStatusService.save({
            it.itemId == person.id &&
                it.dn == 'cn=Ellermann Daniel,dc=users,dc=example,dc=org'
        })
    }

    void 'Save an existing person'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a LDAP sync status service instance'
        LdapSyncStatusService ldapSyncStatusService = Mock()
        service.ldapSyncStatusService = ldapSyncStatusService

        and: 'a person'
        def person = makePerson()
        person.id = new ObjectId()

        and: 'an LDAP sync status'
        def status = new LdapSyncStatus(
            dn: 'cn=Ellermann Daniel,dc=users,dc=example,dc=org',
            itemId: person.id
        )
        status.id = new ObjectId()

        when: 'the save method is called'
        service.save person

        then: 'the LDAP methods have been called'
        1 * configService.getString('ldapHost') >> 'localhost'
        1 * configService.getString('ldapBindDn') >>
            'cn=admin,dc=example,dc=org'
        1 * configService.getString('ldapBindPasswd') >> 'secret'
        1 * configService.getInteger('ldapPort') >> 389i
        1 * ldapFactory.newLdap(
            'localhost', 'cn=admin,dc=example,dc=org', 'secret', 389i
        ) >> ldap
        1 * ldapSyncStatusService.findByItemId(person.id) >> status
        1 * ldap.exists(status.dn) >> true
        1 * ldap.delete(status.dn)
        1 * configService.getString('ldapContactDn') >>
            'dc=users,dc=example,dc=org'

        and: 'the entry is added to DIT'
        1 * ldap.add(
            'cn=Ellermann Daniel,dc=users,dc=example,dc=org',
            { Map data ->
                ['top', 'inetOrgPerson'] == data.objectclass &&
                    'Daniel' == data.givenname &&
                    'Ellermann' == data.sn &&
                    'Daniel Ellermann' == data.displayname &&
                    'AMC World Technologies GmbH' == data.o &&
                    'Fischerinsel 1' == data.street &&
                    '10179' == data.postalcode &&
                    'Berlin' == data.l &&
                    'Berlin' == data.st &&
                    'Fischerinsel 1, 10179 Berlin' == data.postaladdress &&
                    ['123456789', '987654321'] == data.telephonenumber &&
                    ['daniel@example.com', 'info@example.com'] == data.mail &&
                    'http://www.example.com' == data.labeleduri
            }
        )

        and: 'the LDAP sync status entry is stored'
        1 * ldapSyncStatusService.save({
            it.itemId == person.id &&
                it.dn == 'cn=Ellermann Daniel,dc=users,dc=example,dc=org'
        })
    }

    void 'Save an existing person with name conflict'() {
        given: 'a configuration service instance'
        ConfigService configService = Mock()
        service.configService = configService

        and: 'a mocked LDAP class'
        Ldap ldap = Mock()

        and: 'a mocked LDAP factory'
        LdapFactory ldapFactory = Mock()
        service.ldapFactory = ldapFactory

        and: 'a LDAP sync status service instance'
        LdapSyncStatusService ldapSyncStatusService = Mock()
        service.ldapSyncStatusService = ldapSyncStatusService

        and: 'a person'
        def person = makePerson()
        person.id = new ObjectId()

        and: 'an LDAP sync status'
        def status = new LdapSyncStatus(
            dn: 'cn=Ellermann Daniel,dc=users,dc=example,dc=org',
            itemId: person.id
        )
        status.id = new ObjectId()

        when: 'the save method is called'
        service.save person

        then: 'the LDAP methods have been called'
        1 * configService.getString('ldapHost') >> 'localhost'
        1 * configService.getString('ldapBindDn') >>
            'cn=admin,dc=example,dc=org'
        1 * configService.getString('ldapBindPasswd') >> 'secret'
        1 * configService.getInteger('ldapPort') >> 389i
        1 * ldapFactory.newLdap(
            'localhost', 'cn=admin,dc=example,dc=org', 'secret', 389i
        ) >> ldap
        1 * ldapSyncStatusService.findByItemId(person.id) >> status
        1 * ldap.exists(status.dn) >> true
        1 * ldap.delete(status.dn)
        1 * configService.getString('ldapContactDn') >>
            'dc=users,dc=example,dc=org'

        and: 'the entry is added to DIT'
        1 * ldap.add('cn=Ellermann Daniel,dc=users,dc=example,dc=org', _) >>
            { throw new NameAlreadyBoundException() }
        1 * ldap.add('cn=Ellermann Daniel 2,dc=users,dc=example,dc=org', _) >>
            { throw new NameAlreadyBoundException() }
        1 * ldap.add(
            'cn=Ellermann Daniel 3,dc=users,dc=example,dc=org',
            { Map data ->
                ['top', 'inetOrgPerson'] == data.objectclass &&
                    'Daniel' == data.givenname &&
                    'Ellermann' == data.sn &&
                    'Daniel Ellermann' == data.displayname &&
                    'AMC World Technologies GmbH' == data.o &&
                    'Fischerinsel 1' == data.street &&
                    '10179' == data.postalcode &&
                    'Berlin' == data.l &&
                    'Berlin' == data.st &&
                    'Fischerinsel 1, 10179 Berlin' == data.postaladdress &&
                    ['123456789', '987654321'] == data.telephonenumber &&
                    ['daniel@example.com', 'info@example.com'] == data.mail &&
                    'http://www.example.com' == data.labeleduri
            }
        )

        and: 'the LDAP sync status entry is stored'
        1 * ldapSyncStatusService.save({
            it.itemId == person.id &&
                it.dn == 'cn=Ellermann Daniel 3,dc=users,dc=example,dc=org'
        })
    }


    //-- Non-public methods ---------------------

    private static Organization makeOrganization() {
        new Organization(
            number: 10000i, recType: 1,
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
    }

    private static Person makePerson(Organization org = makeOrganization()) {
        new Person(
            number: 10000i, organization: org, firstName: 'Daniel',
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
    }
}
