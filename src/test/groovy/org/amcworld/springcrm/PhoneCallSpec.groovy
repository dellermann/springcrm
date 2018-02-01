/*
 * PhoneCallSpec.groovy
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


class PhoneCallSpec extends Specification implements DomainUnitTest<PhoneCall> {

    //-- Feature methods ------------------------

    def 'Preset values'() {
        when: 'an empty phone phoneCall is created'
        def call = new PhoneCall()

        then: 'some values are preset'
        null != call.start
    }

    def 'Copy using constructor'() {
        given: 'an instance'
        def pc1 = new PhoneCall(
            subject: 'My phone phoneCall',
            notes: 'foo',
            organization: new Organization(
                recType: 1, name: 'YourOrganization Ltd.'
            ),
            person: new Person(firstName: 'Homer', lastName: 'Simpson'),
            phone: '+49 1234848494',
            type: PhoneCallType.INCOMING,
            status: PhoneCallStatus.COMPLETED
        )

        when: 'the phone phoneCall is copied using the constructor'
        def pc2 = new PhoneCall(pc1)

        then: 'some properties are copied'
        pc2.subject == pc1.subject
        pc2.notes == pc1.notes
        pc2.organization.is pc1.organization
        pc2.person.is pc1.person
        pc2.phone == pc1.phone
        pc2.start == pc1.start
        pc2.type == pc1.type
        pc2.status == pc1.status

        and: 'some properties are unset'
        !pc2.id
    }

    void 'Equals is null-safe'() {
        given: 'an instance'
        def call = new PhoneCall()

        expect:
        null != call
        call != null
        !call.equals(null)
    }

    void 'Instances of other types are always unequal'() {
        given: 'an instance'
        def call = new PhoneCall()

        expect:
        call != 'foo'
        call != 45
        call != 45.3
        call != new Date()
    }

    void 'Not persisted instances are equal'() {
        given: 'three instances without ID'
        def pc1 = new PhoneCall(subject: 'Call 1')
        def pc2 = new PhoneCall(subject: 'Call 2')
        def pc3 = new PhoneCall(subject: 'Call 3')

        expect: 'equals() is reflexive'
        pc1 == pc1
        pc2 == pc2
        pc3 == pc3

        and: 'all instances are equal and equals() is symmetric'
        pc1 == pc2
        pc2 == pc1
        pc2 == pc3
        pc3 == pc2

        and: 'equals() is transitive'
        pc1 == pc3
        pc3 == pc1
    }

    void 'Persisted instances are equal if they have the same ID'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def pc1 = new PhoneCall(subject: 'Call 1')
        pc1.id = id
        def pc2 = new PhoneCall(subject: 'Call 2')
        pc2.id = id
        def pc3 = new PhoneCall(subject: 'Call 3')
        pc3.id = id

        expect: 'equals() is reflexive'
        pc1 == pc1
        pc2 == pc2
        pc3 == pc3

        and: 'all instances are equal and equals() is symmetric'
        pc1 == pc2
        pc2 == pc1
        pc2 == pc3
        pc3 == pc2

        and: 'equals() is transitive'
        pc1 == pc3
        pc3 == pc1
    }

    void 'Persisted instances are unequal if they have the different ID'() {
        given: 'three instances with different IDs'
        def pc1 = new PhoneCall(subject: 'Call 1')
        pc1.id = new ObjectId()
        def pc2 = new PhoneCall(subject: 'Call 1')
        pc2.id = new ObjectId()
        def pc3 = new PhoneCall(subject: 'Call 1')
        pc3.id = new ObjectId()

        expect: 'equals() is reflexive'
        pc1 == pc1
        pc2 == pc2
        pc3 == pc3

        and: 'all instances are unequal and equals() is symmetric'
        pc1 != pc2
        pc2 != pc1
        pc2 != pc3
        pc3 != pc2

        and: 'equals() is transitive'
        pc1 != pc3
        pc3 != pc1
    }

    void 'Can compute hash code of an empty instance'() {
        given: 'an empty instance'
        def call = new PhoneCall()

        expect:
        3937i == call.hashCode()
    }

    void 'Can compute hash code of a not persisted instance'() {
        given: 'an empty instance'
        def call = new PhoneCall(subject: 'Last phoneCall')

        expect:
        3937i == call.hashCode()
    }

    void 'Hash codes are consistent'() {
        given: 'an instance'
        def id = new ObjectId()
        def call = new PhoneCall(subject: 'Last phoneCall')
        call.id = id

        when: 'I compute the hash code'
        int h = call.hashCode()

        then: 'the hash code remains consistent'
        for (int j = 0; j < 500; j++) {
            call = new PhoneCall(subject: 'Last phoneCall')
            call.id = id
            h == call.hashCode()
        }
    }

    void 'Equal instances produce the same hash code'() {
        given: 'three instances with same ID'
        def id = new ObjectId()
        def pc1 = new PhoneCall(subject: 'Call 1')
        pc1.id = id
        def pc2 = new PhoneCall(subject: 'Call 2')
        pc2.id = id
        def pc3 = new PhoneCall(subject: 'Call 3')
        pc3.id = id

        expect:
        pc1.hashCode() == pc2.hashCode()
        pc2.hashCode() == pc3.hashCode()
    }

    void 'Different instances produce different hash codes'() {
        given: 'three instances with different properties'
        def pc1 = new PhoneCall(subject: 'Call 1')
        pc1.id = new ObjectId()
        def pc2 = new PhoneCall(subject: 'Call 1')
        pc2.id = new ObjectId()
        def pc3 = new PhoneCall(subject: 'Call 1')
        pc3.id = new ObjectId()

        expect:
        pc1.hashCode() != pc2.hashCode()
        pc2.hashCode() != pc3.hashCode()
    }

    def 'Can convert to string'(String subject) {
        given: 'an instance'
        def call = new PhoneCall(subject: subject)

        expect:
        (subject?.trim() ?: '') == call.toString()

        where:
        subject << [null, '', '  ', 'foo', '  bar  ', 'My phone phoneCall']
    }

    def 'Subject must not be blank'(String subject, boolean valid) {
        given: 'an instance'
        def call = new PhoneCall(
            subject: subject, type: PhoneCallType.INCOMING,
            status: PhoneCallStatus.COMPLETED
        )

        expect:
        valid == call.validate()

        where:
        subject         || valid
        null            || false
        ''              || false
        'foo'           || true
        'any subject'   || true
    }

    def 'Phone must have a maximum length'(String phone, boolean valid) {
        given: 'an instance'
        def call = new PhoneCall(
            subject: 'My phone phoneCall', phone: phone,
            type: PhoneCallType.INCOMING, status: PhoneCallStatus.COMPLETED
        )

        expect:
        valid == call.validate()

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

    def 'Type must not be null'(PhoneCallType type, boolean valid) {
        given: 'an instance'
        def call = new PhoneCall(
            subject: 'My phone phoneCall', type: type,
            status: PhoneCallStatus.COMPLETED
        )

        expect:
        valid == call.validate()

        where:
        type                    || valid
        null                    || false
        PhoneCallType.INCOMING  || true
        PhoneCallType.OUTGOING  || true
    }

    def 'Status must not be null'(PhoneCallStatus status, boolean valid) {
        given: 'an instance'
        def call = new PhoneCall(
            subject: 'My phone phoneCall', type: PhoneCallType.INCOMING,
            status: status
        )

        expect:
        valid == call.validate()

        where:
        status                      || valid
        null                        || false
        PhoneCallStatus.COMPLETED   || true
        PhoneCallStatus.PLANNED     || true
        PhoneCallStatus.CANCELLED   || true
    }
}
