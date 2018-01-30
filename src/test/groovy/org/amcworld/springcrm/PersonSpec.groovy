/*
 * PersonSpec.groovy
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


class PersonSpec extends Specification implements DomainUnitTest<Person> {

    //-- Feature methods ------------------------

    void 'Copy using constructor works'() {
        given:
        def p1 = new Person(
            number: 10003,
            organization: new Organization(
                recType: 1, name : 'YourOrganizationLtd'
            ),
            salutation: new Salutation(name: 'Herr', orderId: 10),
            title: 'test',
            firstName: 'foo',
            lastName: 'bar',
            mailingAddr: new Address(),
            otherAddr: new Address(),
            phone: '3030303',
            phoneHome: '030330303',
            mobile: '1944392',
            fax: '8008088',
            phoneAssistant: '2002002',
            phoneOther: '10012100',
            email1: 'info@yourorganization.example',
            email2: 'office@yourorganization.example',
            jobTitle: 'test title',
            department: 'test department',
            assistant: 'test assistant',
            birthday: new Date(),
            picture: 'foobar'.getBytes(),
            notes: 'some notes',
            assessmentPositive: 'friendly person',
            assessmentNegative: 'a little bit slow'
        )

        when:
        def p2 = new Person(p1)

        then:
        p2.organization == p1.organization
        p2.salutation == p1.salutation
        p2.title == p1.title
        p2.firstName == p1.firstName
        p2.lastName == p1.lastName
        p2.mailingAddr == p1.mailingAddr
        p2.otherAddr == p1.otherAddr
        p2.phone == p1.phone
        p2.phoneHome == p1.phoneHome
        p2.mobile == p1.mobile
        p2.fax == p1.fax
        p2.phoneAssistant == p1.phoneAssistant
        p2.phoneOther == p1.phoneOther
        p2.email1 == p1.email1
        p2.email2 == p1.email2
        p2.jobTitle == p1.jobTitle
        p2.department == p1.department
        p2.assistant == p1.assistant
        p2.birthday == p1.birthday
        p2.picture == p1.picture
        p2.notes == p1.notes
        p2.assessmentPositive == p1.assessmentPositive
        p2.assessmentNegative == p1.assessmentNegative

        and: 'some properties are unset'
        0 == p2.number
        null == p2.dateCreated
        null == p2.lastUpdated
    }

    void 'Can compute the full name'(String firstName, String lastName,
                                     String result)
    {
        given:
        def person = new Person()

        when:
        person.firstName = firstName
        person.lastName = lastName

        then:
        result == person.fullName

        where:
        firstName		| lastName		|| result
        null			| null			|| ''
        null			| 'Smith'		|| 'Smith'
        'John'			| null			|| 'John'
        'John'			| 'Smith'		|| 'John Smith'
        ' '				| null			|| ''
        null			| ' '			|| ''
        'abc' * 1000	| ' '			|| 'abc' * 1000
        'abc' * 1000	| 'abc'*1000	|| 'abc' * 1000 + ' ' + 'abc' * 1000
        ',.-'			| ',.-'			|| ',.- ,.-'
        '10001'			| '202022'		|| '10001 202022'
        'user@mydomain' | ''			|| 'user@mydomain'
        'string123'		| '321string'	|| 'string123 321string'
    }

    void 'Equals is null-safe'() {
        given: 'an instance'
        def person = new Person()

        expect:
        null != person
        person != null
        !person.equals(null)
    }

    void 'Instances of other types are always unequal'() {
        given: 'an instance'
        def person = new Person()

        expect:
        person != 'foo'
        person != 45
        person != 45.3
        person != new Date()
    }

    void 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def p1 = new Person(lastName: 'Name 1')
        def p2 = new Person(lastName: 'Name 2')
        def p3 = new Person(lastName: 'Name 3')

        expect: 'equals() is reflexive'
        p1 == p1
        p2 == p2
        p3 == p3

        and: 'all instances are equal and equals() is symmetric'
        p1 == p2
        p2 == p1
        p2 == p3
        p3 == p2

        and: 'equals() is transitive'
        p1 == p3
        p3 == p1
    }

    void 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def p1 = new Person(lastName: 'Name 1')
        p1.id = id
        def p2 = new Person(lastName: 'Name 2')
        p2.id = id
        def p3 = new Person(lastName: 'Name 3')
        p3.id = id

        expect: 'equals() is reflexive'
        p1 == p1
        p2 == p2
        p3 == p3

        and: 'all instances are equal and equals() is symmetric'
        p1 == p2
        p2 == p1
        p2 == p3
        p3 == p2

        and: 'equals() is transitive'
        p1 == p3
        p3 == p1
    }

    void 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with different IDs'
        def p1 = new Person(lastName: 'Name 1')
        p1.id = new ObjectId()
        def p2 = new Person(lastName: 'Name 1')
        p2.id = new ObjectId()
        def p3 = new Person(lastName: 'Name 1')
        p3.id = new ObjectId()

        expect: 'equals() is reflexive'
        p1 == p1
        p2 == p2
        p3 == p3

        and: 'all instances are unequal and equals() is symmetric'
        p1 != p2
        p2 != p1
        p2 != p3
        p3 != p2

        and: 'equals() is transitive'
        p1 != p3
        p3 != p1
    }

    void 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def person = new Person()

        expect:
        3937i == person.hashCode()
    }

    void 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def person = new Person(lastName: 'Doe')

        expect:
        3937i == person.hashCode()
    }

    void 'Hash codes are consistent'() {
        given: 'an instance'
        def id = new ObjectId()
        def person = new Person(lastName: 'Doe')
        person.id = id

        when: 'I compute the hash code'
        int h = person.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            person = new Person(lastName: 'Doe')
            person.id = id
            h == person.hashCode()
        }
    }

    void 'Equal instances produce the same hash code'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def p1 = new Person(lastName: 'Name 1')
        p1.id = id
        def p2 = new Person(lastName: 'Name 2')
        p2.id = id
        def p3 = new Person(lastName: 'Name 3')
        p3.id = id

        expect:
        p1.hashCode() == p2.hashCode()
        p2.hashCode() == p3.hashCode()
    }

    void 'Different instances produce different hash codes'() {
        given: 'three instances with different properties'
        def p1 = new Person(lastName: 'Name 1')
        p1.id = new ObjectId()
        def p2 = new Person(lastName: 'Name 1')
        p2.id = new ObjectId()
        def p3 = new Person(lastName: 'Name 1')
        p3.id = new ObjectId()

        expect:
        p1.hashCode() != p2.hashCode()
        p2.hashCode() != p3.hashCode()
    }

    void 'Can convert to string'(String firstName, String lastName, String e) {
        given: 'an instance'
        def person = new Person(firstName: firstName, lastName: lastName)

        expect:
        e == person.toString()

        where:
        firstName   | lastName  || e
        null        | null      || ''
        null        | ''        || ''
        null        | '  '      || ''
        null        | 'Doe'     || 'Doe'
        ''          | null      || ''
        ''          | ''        || ''
        ''          | '  '      || ''
        ''          | 'Doe'     || 'Doe'
        '  '        | null      || ''
        '  '        | ''        || ''
        '  '        | '  '      || ''
        '  '        | 'Doe'     || 'Doe'
        'John'      | null      || 'John'
        'John'      | ''        || 'John'
        'John'      | '  '      || 'John'
        'John'      | 'Doe'     || 'Doe, John'
    }

    void 'First name must not be blank'(String firstName, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the first name is set'
        person.firstName = firstName

        then: 'the instance is valid or not'
        valid == person.validate()

        where:
        firstName		|| valid
        null			|| false
        ''				|| false
        ' '				|| true
        '    '			|| true
        ' \t \n'		|| true
        1003			|| true
        'John'			|| true
        'Derk 1003'		|| true
        'abc' * 100		|| true
        'abc' * 1000	|| true
    }

    void 'The last name must not be blank'(String lastName, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the last name is set'
        person.lastName = lastName

        then: 'the instance is valid or not'
        valid == person.validate()

        where:
        lastName		|| valid
        null			|| false
        ''				|| false
        ' '				|| true
        '    '			|| true
        ' \t \n'		|| true
        1003			|| true
        'Doe'			|| true
        'Derk 1003'		|| true
        'abc'*100		|| true
        'abc'*1000		|| true
    }

    void 'Phone must have a maximum length'(String phone, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the phone number is set'
        person.phone = phone

        then: 'the instance is valid or not'
        valid == person.validate()

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

    void 'Mobile must have a maximum length'(String phone, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the phone number is set'
        person.mobile = phone

        then: 'the instance is valid or not'
        valid == person.validate()

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

    void 'Fax must have a maximum length'(String phone, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the fax number is set'
        person.fax = phone

        then: 'the instance is valid or not'
        valid == person.validate()

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

    void 'Assistant phone must have a maximum length'(String phone,
                                                      boolean valid)
    {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the phone number is set'
        person.phoneAssistant = phone

        then: 'the instance is valid or not'
        valid == person.validate()

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

    void 'Other phone must have a maximum length'(String phone, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the phone number is set'
        person.phoneOther = phone

        then: 'the instance is valid or not'
        valid == person.validate()

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

    void 'E-mail 1 must be valid e-mail address'(String email, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the email is set'
        person.email1 = email

        then: 'the instance is valid or not'
        valid == person.validate()

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

    void 'E-mail 2 must be valid e-mail address'(String email, boolean valid) {
        given: 'an instance'
        def person = new Person(
            organization: new Organization(
                recType: 1, name: 'foo', billingAddr: new Address(),
                shippingAddr: new Address()
            ),
            firstName: 'John', lastName: 'Doe', mailingAddr: new Address(),
            otherAddr: new Address()
        )

        when: 'the email is set'
        person.email2 = email

        then: 'the instance is valid or not'
        valid == person.validate()

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
