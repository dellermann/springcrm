/*
 * StaffSpec.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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
import java.time.LocalDate
import spock.lang.Specification


@TestFor(Staff)
@Mock([Department, Staff, User])
class StaffSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Preset values'() {
        when: 'I create an empty staff'
        def s = new Staff()

        then: 'some values are preset'
        null != s.address
        null != s.bankDetails
        false == s.healthInsurancePrivate
    }

    def 'Copy using constructor'() {
        given: 'a department'
        def dep = new Department(
            name: 'Field service',
            costCenter: '2748A'
        )

        and: 'a user'
        def user = new User(userName: 'jdoe')

        and: 'a staff'
        def s1 = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            title: 'Dr.',
            firstName: 'John',
            lastName: 'Doe',
            address: new Address(
                street: '16 Oak Road',
                postalCode: '45794',
                location: 'Twin Peaks',
                state: 'NY',
                country: 'USA'
            ),
            phone: '+1 65 47479543',
            phoneHome: '+1 65 7597936',
            mobile: '+1 399 34579327',
            email1: 'j.doe@example.com',
            email2: 'johndoe@mycompany.example.com',
            department: dep,
            gender: new Gender(name: 'male'),
            dateOfBirth: LocalDate.of(1964, 4, 20),
            nationality: 'US',
            civilStatus: new CivilStatus(name: 'married'),
            bankDetails: new BankDetails(
                iban: 'US483537435975398933',
                owner: 'John Doe'
            ),
            taxBracket: new TaxBracket(name: '2 - married'),
            numChildren: 3,
            socialSecurityNumber: '3759/479/1594209',
            healthInsurance: new HealthInsurance(name: 'GlobalHealth, ltd'),
            healthInsurancePrivate: true,
            graduation: new Graduation(name: 'Master'),
            weeklyWorkingTime: 40.0,
            dateOfEmployment: LocalDate.of(2007, 2, 1),
            user: user
        )

        when: 'I copy that staff using the constructor'
        def s2 = new Staff(s1)

        then: 'I have some properties of the first staff in the second one'
        s2.number == s1.number
        s2.salutation.is s1.salutation
        s2.phone == s1.phone
        s2.email1 == s1.email1
        s2.department.is s1.department
        s2.gender.is s1.gender
        s2.nationality == s1.nationality
        s2.civilStatus.is s1.civilStatus
        s2.taxBracket.is s1.taxBracket
        s2.healthInsurance.is s1.healthInsurance
        s2.healthInsurancePrivate == s1.healthInsurancePrivate
        s2.graduation.is s1.graduation
        s2.weeklyWorkingTime == s1.weeklyWorkingTime

        and: 'some properties are unset'
        !s2.id
        !s2.title
        !s2.firstName
        !s2.lastName
        !s2.phoneHome
        !s2.mobile
        !s2.email2
        !s2.dateOfBirth
        null == s2.numChildren
        !s2.socialSecurityNumber
        !s2.dateOfEmployment
        !s2.user

        and: 'the address and bank details are defined but empty'
        null != s2.address
        s2.address.empty
        null != s2.bankDetails
        s2.bankDetails.empty
    }

    def 'Equals is null-safe'() {
        given: 'a staff'
        def s = new Staff()

        expect:
        null != s
        s != null
        !s.equals(null)
    }

    def 'Instances of other types are always unequal'() {
        given: 'a staff'
        def s = new Staff()

        expect:
        s != 'foo'
        s != 45
        s != 45.3
        s != new Date()
    }

    def 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def s1 = new Staff(number: '49759/484')
        def s2 = new Staff(number: '63860/913')
        def s3 = new Staff(number: '32974/189')

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are equal and equals() is symmetric'
        s1 == s2
        s2 == s1
        s2 == s3
        s3 == s2

        and: 'equals() is transitive'
        s1 == s3
        s3 == s1
    }

    def 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with different properties but same IDs'
        def s1 = new Staff(number: '49759/484')
        s1.id = 7403L
        def s2 = new Staff(number: '63860/913')
        s2.id = 7403L
        def s3 = new Staff(number: '32974/189')
        s3.id = 7403L

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are equal and equals() is symmetric'
        s1 == s2
        s2 == s1
        s2 == s3
        s3 == s2

        and: 'equals() is transitive'
        s1 == s3
        s3 == s1
    }

    def 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new Staff(number: '49759/484')
        s1.id = 7403L
        def s2 = new Staff(number: '49759/484')
        s2.id = 7404L
        def s3 = new Staff(number: '49759/484')
        s3.id = 8473L

        expect: 'equals() is reflexive'
        s1 == s1
        s2 == s2
        s3 == s3

        and: 'all instances are unequal and equals() is symmetric'
        s1 != s2
        s2 != s1
        s2 != s3
        s3 != s2

        and: 'equals() is transitive'
        s1 != s3
        s3 != s1
    }

    def 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def s = new Staff()

        expect:
        0i == s.hashCode()
    }

    def 'Can compute hash code of a not persisted instance'() {
        given: 'an instance without ID'
        def s = new Staff(number: '49759/484')

        expect:
        0i == s.hashCode()
    }

    def 'Hash codes are consistent'() {
        given: 'an instance with ID'
        def s = new Staff(number: '49759/484')
        s.id = 7403L

        when: 'I compute the hash code'
        int h = s.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            s = new Staff(number: '97539/104')
            s.id = 7403L
            h == s.hashCode()
        }
    }

    def 'Equal instances produce the same hash code'() {
        given: 'three instances with different properties but same IDs'
        def s1 = new Staff(number: '49759/484')
        s1.id = 7403L
        def s2 = new Staff(number: '63860/913')
        s2.id = 7403L
        def s3 = new Staff(number: '32974/189')
        s3.id = 7403L

        expect:
        s1.hashCode() == s2.hashCode()
        s2.hashCode() == s3.hashCode()
    }

    def 'Different instances produce different hash codes'() {
        given: 'three instances with same properties but different IDs'
        def s1 = new Staff(number: '49759/484')
        s1.id = 7403L
        def s2 = new Staff(number: '49759/484')
        s2.id = 7404L
        def s3 = new Staff(number: '49759/484')
        s3.id = 8473L

        expect:
        s1.hashCode() != s2.hashCode()
        s2.hashCode() != s3.hashCode()
    }

    def 'Convert to string'(String fn, String ln, String e) {
        given: 'an empty staff'
        def s = new Staff()

        when: 'I set the name'
        s.firstName = fn
        s.lastName = ln

        then: 'I get a useful string representation'
        e == s.toString()
        e == s.fullName

        where:
        fn          | ln           || e
        null        | null         || ''
        null        | ''           || ''
        null        | '   \t '     || ''
        null        | 'a'          || 'a'
        null        | 'Smith'      || 'Smith'
        null        | ' Smith\t '  || 'Smith'
        ''          | null         || ''
        ''          | ''           || ''
        ''          | '   \t '     || ''
        ''          | 'a'          || 'a'
        ''          | 'Smith'      || 'Smith'
        ''          | ' Smith\t '  || 'Smith'
        '   \t '    | null         || ''
        '   \t '    | ''           || ''
        '   \t '    | '   \t '     || ''
        '   \t '    | 'a'          || 'a'
        '   \t '    | 'Smith'      || 'Smith'
        '   \t '    | ' Smith\t '  || 'Smith'
        'a'         | null         || 'a'
        'a'         | ''           || 'a'
        'a'         | '   \t '     || 'a'
        'a'         | 'a'          || 'a a'
        'a'         | 'Smith'      || 'a Smith'
        'a'         | ' Smith\t '  || 'a Smith'
        'John'      | null         || 'John'
        'John'      | ''           || 'John'
        'John'      | '   \t '     || 'John'
        'John'      | 'a'          || 'John a'
        'John'      | 'Smith'      || 'John Smith'
        'John'      | ' Smith\t '  || 'John Smith'
        ' John \t ' | null         || 'John'
        ' John \t ' | ''           || 'John'
        ' John \t ' | '   \t '     || 'John'
        ' John \t ' | 'a'          || 'John a'
        ' John \t ' | 'Smith'      || 'John Smith'
        ' John \t ' | ' Smith\t '  || 'John Smith'
    }

    def 'Number must not be blank and max 30 char long'(String n, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the number'
        s.number = n

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        n       	|| v
        null    	|| false
        ''      	|| false
        '  \t ' 	|| false
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 30	|| true
        'a' * 31	|| false
    }

    def 'Salutation must not null'() {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the salutation'
        s.salutation = new Salutation(name: 'Mr.')

        then: 'the instance is valid'
        s.validate()

        when: 'I unset the salutation'
        s.salutation = null

        then: 'the instance is not valid'
        !s.validate()
    }

    def 'Title must max 20 char long'(String t, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the title'
        s.title = t

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        t       	|| v
        null    	|| true
        ''      	|| true
        '  \t ' 	|| true
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 20	|| true
        'a' * 21	|| false
    }

    def 'First name must not be blank and max 50 char long'(String n, boolean v)
    {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the first name'
        s.firstName = n

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        n       	|| v
        null    	|| false
        ''      	|| false
        '  \t ' 	|| false
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 50	|| true
        'a' * 51	|| false
    }

    def 'Last name must not be blank and max 50 char long'(String n, boolean v)
    {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the last name'
        s.lastName = n

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        n       	|| v
        null    	|| false
        ''      	|| false
        '  \t ' 	|| false
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 50	|| true
        'a' * 51	|| false
    }

    def 'Phone must max 40 char long'(String p, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the phone'
        s.phone = p

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        p       	|| v
        null    	|| true
        ''      	|| true
        '  \t ' 	|| true
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 40	|| true
        'a' * 41	|| false
    }

    def 'Phone home must max 40 char long'(String p, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the phone'
        s.phoneHome = p

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        p       	|| v
        null    	|| true
        ''      	|| true
        '  \t ' 	|| true
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 40	|| true
        'a' * 41	|| false
    }

    def 'Mobile must max 40 char long'(String p, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the phone'
        s.mobile = p

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        p       	|| v
        null    	|| true
        ''      	|| true
        '  \t ' 	|| true
        'a'     	|| true
        'S'     	|| true
        'abc'   	|| true
        'a  x ' 	|| true
        ' name' 	|| true
        'a' * 40	|| true
        'a' * 41	|| false
    }

    def 'E-mail 1 must not be blank and must be valid'(String e, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the e-mail'
        s.email1 = e

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        e                       || v
        null                    || false
        ''                      || false
        '   '                   || false
        ' \t  '                 || false
        'a'                     || false
        'John'                  || false
        'j.smith'               || false
        'j.smith@'              || false
        '@example.com'          || false
        'j.smith@example.com'   || true
    }

    def 'E-mail 2 must be valid'(String e, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the e-mail'
        s.email2 = e

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        e                       || v
        null                    || true
        ''                      || true
        '   '                   || true
        ' \t  '                 || true
        'a'                     || false
        'John'                  || false
        'j.smith'               || false
        'j.smith@'              || false
        '@example.com'          || false
        'j.smith@example.com'   || true
    }

    def 'Number of children must be within limit'(Integer n, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the number of children'
        s.numChildren = n

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        n                       || v
        null                    || true
        -34i                    || false
        -1i                     || false
        0i                      || true
        1i                      || true
        2i                      || true
        3i                      || true
        10i                     || true
        99i                     || true
        100i                    || false
    }

    def 'Weekly working time must be within limit'(BigDecimal w, boolean v) {
        given: 'a quite valid staff'
        def s = new Staff(
            number: '4379',
            salutation: new Salutation(name: 'Mr.'),
            firstName: 'John',
            lastName: 'Doe',
            email1: 'j.doe@example.com',
            gender: new Gender(name: 'male'),
        )

        when: 'I set the weekly working time'
        s.weeklyWorkingTime = w

        then: 'the instance is valid or not'
        v == s.validate()

        where:
        w                       || v
        null                    || true
        -18.7                   || false
        -0.1                    || false
        0.0                     || true
        0.5                     || true
        10.25                   || true
        34.75                   || true
        40.0                    || true
        45.5                    || true
        168.0                   || true
        168.1                   || false
    }
}
