/*
 * CallSpec.groovy
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
import spock.lang.Specification


@TestFor(Call)
@Mock([Call, Organization, Person])
class CallSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Preset values'() {
        when: 'I create an empty phone call'
        def c = new Call()

        then: 'some values are preset'
        null != c.start
    }

    def 'Copy using constructor'() {
        given: 'A phone call'
        def c1 = new Call(
            subject: 'My phone call',
            notes: 'foo',
            organization: new Organization(
                recType: 1, name: 'YourOrganization Ltd.'
            ),
            person: new Person(firstName: 'Homer', lastName: 'Simpson'),
            phone: '+49 1234848494',
            type: CallType.incoming,
            status: CallStatus.completed
        )

        when: 'I copy that phone call using the constructor'
        def c2 = new Call(c1)

        then: 'I have some properties of the first phone call in the second one'
        c2.subject == c1.subject
        c2.notes == c1.notes
        c2.organization.is c1.organization
        c2.person.is c1.person
        c2.phone == c1.phone
        c2.start == c1.start
        c2.type == c1.type
        c2.status == c1.status

        and: 'some properties are unset'
        !c2.id
    }

    def 'Check for equality'() {
        given: 'two phone calls with different content'
        def c1 = new Call(subject: 'My call', notes: 'foo')
        def c2 = new Call(subject: 'Your call', notes: 'bar')

        and: 'the same IDs'
        c1.id = 4903
        c2.id = 4903

        expect: 'both these phone calls are equal'
        c2 == c1
        c1 == c2
    }

    def 'Check for inequality'() {
        given: 'two phone calls with the same content'
        def c1 = new Call(subject: 'My call', notes: 'foo')
        def c2 = new Call(subject: 'My call', notes: 'foo')

        and: 'both the IDs set to different values'
        c1.id = 4903
        c2.id = 4904

        when: 'I compare both these phone calls'
        boolean b1 = (c2 != c1)
        boolean b2 = (c1 != c2)

        then: 'they are not equal'
        b1
        b2

        when: 'I compare to null'
        c2 = null

        then: 'they are not equal'
        c2 != c1
        c1 != c2

        when: 'I compare to another type'
        String s = 'foo'

        then: 'they are not equal'
        c1 != s
    }

    def 'Compute hash code'() {
        when: 'I create a phone call with no ID'
        def c = new Call()

        then: 'I get a valid hash code'
        0 == c.hashCode()

        when: 'I create a phone call with discrete IDs'
        c.id = id

        then: 'I get a hash code using this ID'
        e == c.hashCode()

        where:
           id |     e
            0 |     0
            1 |     1
           10 |    10
          105 |   105
         9404 |  9404
        37603 | 37603
    }

    def 'Convert to string'() {
        given: 'an empty phone call'
        def c = new Call()

        when: 'I set the subject'
        c.subject = 'My phone call'

        then: 'I get a useful string representation'
        'My phone call' == c.toString()

        when: 'I empty the subject'
        c.subject = ''

        then: 'I get an empty string representation'
        '' == c.toString()

        when: 'I unset the subject'
        c.subject = null

        then: 'I get an empty string representation'
        '' == c.toString()
    }

    def 'Subject constraints'() {
        setup:
        mockForConstraintsTests(Call)

        when:
        def c = new Call(
            subject: subject, type: CallType.incoming,
            status: CallStatus.completed
        )
        c.validate()

        then:
        !valid == c.hasErrors()

        where:
        subject         | valid
        null            | false
        ''              | false
        ' '             | false
        '      '        | false
        '  \t \n '      | false
        'foo'           | true
        'any subject'   | true
    }

    def 'Phone constraints'() {
        setup:
        mockForConstraintsTests(Call)

        when:
        def c = new Call(
            subject: 'My phone call', phone: phone, type: CallType.incoming,
            status: CallStatus.completed
        )
        c.validate()

        then:
        !valid == c.hasErrors()

        where:
        phone           | valid
        null            | true
        ''              | true
        ' '             | true
        'foo'           | true
        'any name'      | true
        'x' * 40        | true
        'x' * 50        | false
    }

    def 'Start constraints'() {
        setup:
        mockForConstraintsTests(Call)

        when: 'I create a phone call without start time and validate it'
        def c = new Call(
            subject: 'My phone call', type: CallType.incoming,
            status: CallStatus.completed
        )
        c.validate()

        then: 'it is valid'
        !c.hasErrors()

        when: 'I set the start time to a discret value and validate it'
        c.start = (new Date() + 5)
        c.validate()

        then: 'it is valid'
        !c.hasErrors()

        when: 'I unset the start time and validate it'
        c.start = null
        c.validate()

        then: 'it is not valid'
        c.hasErrors()
    }

    def 'Type constraints'() {
        setup:
        mockForConstraintsTests(Call)

        when: 'I create a phone call with a discrete type value and validate it'
        def c = new Call(
            subject: 'My phone call', type: CallType.incoming,
            status: CallStatus.completed
        )
        c.validate()

        then: 'it is valid'
        !c.hasErrors()

        when: 'I unset the type and validate it'
        c.type = null
        c.validate()

        then: 'it is not valid'
        c.hasErrors()
    }

    def 'Status constraints'() {
        setup:
        mockForConstraintsTests(Call)

        when: 'I create a phone call with a discrete status value and validate it'
        def c = new Call(
            subject: 'My phone call', type: CallType.incoming,
            status: CallStatus.completed
        )
        c.validate()

        then: 'it is valid'
        !c.hasErrors()

        when: 'I unset the status and validate it'
        c.type = null
        c.validate()

        then: 'it is not valid'
        c.hasErrors()
    }
}
