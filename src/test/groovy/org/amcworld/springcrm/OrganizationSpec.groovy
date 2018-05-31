/*
 * OrganizationSpec.groovy
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
import org.bson.types.ObjectId
import spock.lang.Specification


class OrganizationSpec extends Specification
    implements DomainUnitTest<Organization>
{

    //-- Feature methods ------------------------

    @SuppressWarnings("SpellCheckingInspection")
    void 'Copy using constructor'() {
        given: 'an organization'
        def o1 = new Organization(
            number: 40473,
            recType: 1,
            name: 'YourOrganization Ltd.',
            billingAddr: new Address(),
            shippingAddr: new Address(),
            phone: '3030303',
            fax: '703037494',
            phoneOther: '73903037',
            email1: 'info@yourorganization.example',
            email2: 'office@yourorganization.example',
            website: 'www.yourorganization.example',
            legalForm: 'Ltd.',
            type: new OrgType(name: 'foo'),
            industry: new Industry(name: 'bar'),
            owner: 'Mr. Smith',
            numEmployees: '5',
            rating: new Rating(name: 'active'),
            notes: 'whee',
            assessmentPositive: 'friendly company',
            assessmentNegative: 'a little bit slow'
        )

        when: 'that organization is copied using the constructor'
        def o2 = new Organization(o1)

        then: 'some properties of the first organization are copied into the second one'
        o2.recType == o1.recType
        o2.name == o1.name
        !o2.billingAddr.is(o1.billingAddr)
        o2.billingAddr == o1.billingAddr
        !o2.shippingAddr.is(o1.shippingAddr)
        o2.shippingAddr == o1.shippingAddr
        o2.phone == o1.phone
        o2.fax == o1.fax
        o2.phoneOther == o1.phoneOther
        o2.email1 == o1.email1
        o2.email2 == o1.email2
        o2.website == o1.website
        o2.legalForm == o1.legalForm
        o2.type == o1.type
        o2.industry == o1.industry
        o2.owner == o1.owner
        o2.numEmployees == o1.numEmployees
        o2.rating == o1.rating
        o2.notes == o1.notes
        o2.assessmentPositive == o1.assessmentPositive
        o2.assessmentNegative == o1.assessmentNegative

        and: 'some properties are unset'
        !o2.id
        !o2.number
    }

    void 'Get the short name'(String n, String e) {
        given: 'an empty organization'
        def org = new Organization()

        when: 'the name is set'
        org.name = n

        then: 'the short name is the chopped version of the name'
        e == org.shortName

        where:
        n                   || e
        ''                  || ''
        '0123456789'        || '0123456789'
        '0123456789' * 2    || '0123456789' * 2
        '0123456789' * 3    || '0123456789' * 3
        '0123456789' * 4    || '0123456789' * 4
        '0123456789' * 5    || ('0123456789' * 4) + '…'
    }

    void 'Check for customer and vendor flags'(Byte r, boolean c, boolean v) {
        given: 'an empty organization'
        def org = new Organization()

        when: 'I set a record type'
        org.recType = r

        then: 'the customer and vendor flags should be set correctly'
        c == org.client
        v == org.vendor

        where:
        r || c      | v
        0 || false  | false
        1 || true   | false
        2 || false  | true
        3 || true   | true
    }

    void 'Set a website fixes the URL'(String url, String e) {
        given: 'an empty organization'
        def org = new Organization()

        when: 'a URL is set'
        org.website = url

        then: 'the URL is transformed'
        e == org.website

        where:
        url                             || e
        ''                              || ''
        'http://'                       || 'http://'
        'https://'                      || 'https://'
        'www.amc-world.de'              || 'http://www.amc-world.de'
        'http://www.amc-world.de'       || 'http://www.amc-world.de'
        'https://www.amc-world.de'      || 'https://www.amc-world.de'
        'www.amc-world[http://].de'     || 'http://www.amc-world[http://].de'
    }

    void 'Equals is null-safe'() {
        given: 'an instance'
        def org = new Organization()

        expect:
        null != org
        org != null
        //noinspection ChangeToOperator
        !org.equals(null)
    }

    void 'Instances of other types are always unequal'() {
        given: 'an instance'
        def org = new Organization()

        expect:
        org != 'foo'
        org != 45
        org != 45.3
        org != new Date()
    }

    void 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def org1 = new Organization(name: 'My Organization 1')
        def org2 = new Organization(name: 'My Organization 2')
        def org3 = new Organization(name: 'My Organization 3')

        expect: 'equals() is reflexive'
        org1 == org1
        org2 == org2
        org3 == org3

        and: 'all instances are equal and equals() is symmetric'
        org1 == org2
        org2 == org1
        org2 == org3
        org3 == org2

        and: 'equals() is transitive'
        org1 == org3
        org3 == org1
    }

    void 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def org1 = new Organization(name: 'My Organization 1')
        org1.id = id
        def org2 = new Organization(name: 'My Organization 2')
        org2.id = id
        def org3 = new Organization(name: 'My Organization 3')
        org3.id = id

        expect: 'equals() is reflexive'
        org1 == org1
        org2 == org2
        org3 == org3

        and: 'all instances are equal and equals() is symmetric'
        org1 == org2
        org2 == org1
        org2 == org3
        org3 == org2

        and: 'equals() is transitive'
        org1 == org3
        org3 == org1
    }

    void 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with different IDs'
        def org1 = new Organization(name: 'My Organization 1')
        org1.id = new ObjectId()
        def org2 = new Organization(name: 'My Organization 1')
        org2.id = new ObjectId()
        def org3 = new Organization(name: 'My Organization 1')
        org3.id = new ObjectId()

        expect: 'equals() is reflexive'
        org1 == org1
        org2 == org2
        org3 == org3

        and: 'all instances are unequal and equals() is symmetric'
        org1 != org2
        org2 != org1
        org2 != org3
        org3 != org2

        and: 'equals() is transitive'
        org1 != org3
        org3 != org1
    }

    void 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def org = new Organization()

        expect:
        3937i == org.hashCode()
    }

    void 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def org = new Organization(name: 'My Organization 1')

        expect:
        3937i == org.hashCode()
    }

    void 'Hash codes are consistent'() {
        given: 'an instance'
        def id = new ObjectId()
        def org = new Organization(name: 'My Organization 1')
        org.id = id

        when: 'I compute the hash code'
        int h = org.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            org = new Organization(name: 'My Organization 1')
            org.id = id
            h == org.hashCode()
        }
    }

    void 'Equal instances produce the same hash code'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def org1 = new Organization(name: 'My Organization 1')
        org1.id = id
        def org2 = new Organization(name: 'My Organization 2')
        org2.id = id
        def org3 = new Organization(name: 'My Organization 3')
        org3.id = id

        expect:
        org1.hashCode() == org2.hashCode()
        org2.hashCode() == org3.hashCode()
    }

    void 'Different instances produce different hash codes'() {
        given: 'three instances with different properties'
        def org1 = new Organization(name: 'My Organization 1')
        org1.id = new ObjectId()
        def org2 = new Organization(name: 'My Organization 2')
        org2.id = new ObjectId()
        def org3 = new Organization(name: 'My Organization 3')
        org3.id = new ObjectId()

        expect:
        org1.hashCode() != org2.hashCode()
        org2.hashCode() != org3.hashCode()
    }

    void 'Can convert to string'(String name, String e) {
        given: 'an instance'
        def org = new Organization(name: name)

        expect:
        e == org.toString()

        where:
        name            || e
        null            || ''
        ''              || ''
        '  '            || ''
        'MyData ltd.'   || 'MyData ltd.'
        ' MyData ltd. ' || 'MyData ltd.'
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Record type must be within a particular value range'(Byte recType,
                                                               boolean valid)
    {
        given: 'an instance'
        def org = new Organization(
            name: 'My Organization ltd.', billingAddr: new Address(),
            shippingAddr: new Address()
        )

        when: 'the record type is set'
        org.recType = recType

        then: 'the instance is valid or not'
        valid == org.validate()

        where:
        recType     || valid
        0           || false
        1           || true
        2           || true
        3           || true
        4           || false
        10          || false
        -1          || false
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Name must not be blank'(String name, boolean valid) {
        given: 'an instance'
        def org = new Organization(
            recType: (byte) 1, billingAddr: new Address(),
            shippingAddr: new Address()
        )

        when: 'the name is set'
        org.name = name

        then: 'the instance is valid or not'
        valid == org.validate()

        where:
        name            || valid
        null            || false
        ''              || false
        ' '             || false
        '      '        || false
        '  \t \n '      || false
        'foo'           || true
        'any name'      || true
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Phone must have a maximum length'(String phone, boolean valid) {
        given: 'an instance'
        def org = new Organization(
            recType: (byte) 1, name: 'My Organization ltd.',
            billingAddr: new Address(),
            shippingAddr: new Address(),
        )

        when: 'the phone number is set'
        org.phone = phone

        then: 'the instance is valid or not'
        valid == org.validate()

        where:
        phone           || valid
        null            || true
        ''              || true
        ' '             || true
        'foo'           || true
        'any name'      || true
        'x' * 40        || true
        'x' * 41        || false
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Fax must have a maximum length'(String fax, boolean valid) {
        given: 'an instance'
        def org = new Organization(
            recType: (byte) 1, name: 'My Organization ltd.',
            billingAddr: new Address(),
            shippingAddr: new Address(),
        )

        when: 'the fax number is set'
        org.fax = fax

        then: 'the instance is valid or not'
        valid == org.validate()

        where:
        fax             || valid
        null            || true
        ''              || true
        ' '             || true
        'foo'           || true
        'any name'      || true
        'x' * 40        || true
        'x' * 41        || false
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    void 'Other phone must have a maximum length'(String phone, boolean valid) {
        given: 'an instance'
        def org = new Organization(
            recType: (byte) 1, name: 'My Organization ltd.',
            billingAddr: new Address(),
            shippingAddr: new Address(),
        )

        when: 'the other phone number is set'
        org.phoneOther = phone

        then: 'the instance is valid or not'
        valid == org.validate()

        where:
        phone           || valid
        null            || true
        ''              || true
        ' '             || true
        'foo'           || true
        'any name'      || true
        'x' * 40        || true
        'x' * 41        || false
    }

    @SuppressWarnings(["GroovyPointlessBoolean", "SpellCheckingInspection"])
    void 'E-mail 1 must be valid e-mail address'(String email, boolean valid) {
        given: 'an instance'
        def org = new Organization(
            recType: (byte) 1, name: 'My Organization ltd.',
            billingAddr: new Address(),
            shippingAddr: new Address(),
        )

        when: 'the email is set'
        org.email1 = email

        then: 'the instance is valid or not'
        valid == org.validate()

        where:
        email               || valid
        null                || true
        ''                  || true
        ' '                 || false
        'foo'               || false
        'any name'          || false
        'foobar@'           || false
        '@mydomain.com'     || false
        'user@mydomain'     || false
        'user@.com'         || false
        'user@mydomain.com' || true
        'user@härbört.com'  || true
    }

    @SuppressWarnings(["GroovyPointlessBoolean", "SpellCheckingInspection"])
    void 'E-mail 2 must be valid e-mail address'(String email, boolean valid) {
        given: 'an instance'
        def org = new Organization(
            recType: (byte) 1, name: 'My Organization ltd.',
            billingAddr: new Address(),
            shippingAddr: new Address(),
        )

        when: 'the email is set'
        org.email2 = email

        then: 'the instance is valid or not'
        valid == org.validate()

        where:
        email               || valid
        null                || true
        ''                  || true
        ' '                 || false
        'foo'               || false
        'any name'          || false
        'foobar@'           || false
        '@mydomain.com'     || false
        'user@mydomain'     || false
        'user@.com'         || false
        'user@mydomain.com' || true
        'user@härbört.com'  || true
    }
}
