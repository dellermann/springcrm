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
import spock.lang.Specification


class OrganizationSpec extends Specification
    implements DomainUnitTest<Organization>
{

    //-- Feature methods ------------------------

    def 'Copy using constructor'() {
        given: 'An organization'
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

        when: 'I copy that organization using the constructor'
        def o2 = new Organization(o1)

        then: 'I have some properties of the first organization in the second one'
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

    def 'Get the full number'() {
        given: 'an organization with mocked sequence number service'
        def org = new Organization()
        org.seqNumberService = Mock(SeqNumberService)
        org.seqNumberService.format(_, _) >> 'O-14847'

        expect:
        'O-14847' == org.fullNumber
    }

    def 'Get the short name'() {
        given: 'an empty organization'
        def org = new Organization()

        when: 'I set the name'
        org.name = n

        then: 'the short name must be the chopped version of the name'
        e == org.shortName

        where:
        n                   | e
        ''                  | ''
        '0123456789'        | '0123456789'
        '0123456789' * 2    | '0123456789' * 2
        '0123456789' * 3    | '0123456789' * 3
        '0123456789' * 4    | '0123456789' * 4
        '0123456789' * 5    | ('0123456789' * 4) + '...'
    }

    def 'Check for customer and vendor flags'() {
        given: 'an empty organization'
        def org = new Organization()

        when: 'I set a record type'
        org.recType = r

        then: 'the customer and vendor flags should be set correctly'
        c == org.customer
        v == org.vendor

        where:
        r | c       | v
        0 | false   | false
        1 | true    | false
        2 | false   | true
        3 | true    | true
    }

    def 'Set a website'() {
        given: 'an empty organization'
        def org = new Organization()

        when: 'I set a URL'
        org.website = url

        then: 'the URL must be transformed'
        e == org.website

        where:
        url                             | e
        ''                              | ''
        'http://'                       | 'http://'
        'https://'                      | 'https://'
        'www.amc-world.de'              | 'http://www.amc-world.de'
        'http://www.amc-world.de'       | 'http://www.amc-world.de'
        'https://www.amc-world.de'      | 'https://www.amc-world.de'
        'www.amc-world[http://].de'     | 'http://www.amc-world[http://].de'
    }

    def 'Simulate the save method in insert mode and check number'() {
        given: 'an organization without number'
        def org = new Organization()
        org.seqNumberService = Mock(SeqNumberService)
        org.seqNumberService.nextNumber(_) >> 70374

        when: 'I simulate calling save() in insert mode'
        org.beforeInsert()

        then: 'the sequence number must be set'
        70374 == org.number
    }

    def 'Check for equality'() {
        given: 'two organizations with different content'
        def o1 = new Organization(number: 49494, name: 'MyOrganization Ltd.')
        def o2 = new Organization(number: 43373, name: 'YourOrganization Ltd.')

        and: 'the same IDs'
        o1.id = 937
        o2.id = 937

        expect: 'both these organizations are equal'
        o2 == o1
        o1 == o2
    }

    def 'Check for inequality'() {
        given: 'two organizations with the same content'
        def o1 = new Organization(number: 49494, name: 'MyOrganization Ltd.')
        def o2 = new Organization(number: 49494, name: 'MyOrganization Ltd.')

        and: 'both the IDs set to different values'
        o1.id = 937
        o2.id = 938

        when: 'I compare both these organizations'
        boolean b1 = (o2 != o1)
        boolean b2 = (o1 != o2)

        then: 'they are not equal'
        b1
        b2

        when: 'I compare to null'
        o2 = null

        then: 'they are not equal'
        o2 != o1
        o1 != o2

        when: 'I compare to another type'
        String s = 'foo'

        then: 'they are not equal'
        o1 != s
    }

    def 'Compute hash code'() {
        when: 'I create an organization with no ID'
        def org = new Organization()

        then: 'I get a valid hash code'
        0 == org.hashCode()

        when: 'I create an organization with discrete IDs'
        org.id = id

        then: 'I get a hash code using this ID'
        e == org.hashCode()

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
        given: 'an empty organization'
        def org = new Organization()

        when: 'I set the name'
        org.name = 'YourOrganization Ltd.'

        then: 'I get a useful string representation'
        'YourOrganization Ltd.' == org.toString()

        when: 'I empty the name'
        org.name = ''

        then: 'I get an empty string representation'
        '' == org.toString()

        when: 'I unset the name'
        org.name = null

        then: 'I get an empty string representation'
        '' == org.toString()
    }

    def 'RecType constraints'() {
        when:
        def org = new Organization(
            recType: recType, name: 'foo', billingAddr: new Address(),
            shippingAddr: new Address()
        )
        org.validate()

        then:
        !valid == org.hasErrors()

        where:
        recType     | valid
        0           | false
        1           | true
        2           | true
        3           | true
        4           | false
        10          | false
        -1          | false
    }

    def 'Name constraints'() {
        when:
        def org = new Organization(
            recType: 1, name: name, billingAddr: new Address(),
            shippingAddr: new Address()
        )
        org.validate()

        then:
        !valid == org.hasErrors()

        where:
        name            | valid
        null            | false
        ''              | false
        ' '             | false
        '      '        | false
        '  \t \n '      | false
        'foo'           | true
        'any name'      | true
    }

    def 'BillingAddr constraints'() {
        when: 'I create an organization with a billing address and validate it'
        def org = new Organization(
            recType: 1, name: 'YourOrganization Ltd.',
            billingAddr: new Address(), shippingAddr: new Address()
        )
        org.validate()

        then: 'it is valid'
        !org.hasErrors()

        when: 'I unset the billing address and validate it'
        org.billingAddr = null
        org.validate()

        then: 'it is not valid'
        org.hasErrors()
    }

    def 'ShippingAddr constraints'() {
        when: 'I create an organization with a shipping address and validate it'
        def org = new Organization(
            recType: 1, name: 'YourOrganization Ltd.',
            billingAddr: new Address(), shippingAddr: new Address()
        )
        org.validate()

        then: 'it is valid'
        !org.hasErrors()

        when: 'I unset the shipping address and validate it'
        org.shippingAddr = null
        org.validate()

        then: 'it is not valid'
        org.hasErrors()
    }

    def 'Phone constraints'() {
        when:
        def org = new Organization(
            recType: 1, name: 'foo', billingAddr: new Address(),
            shippingAddr: new Address(), phone: phone
        )
        org.validate()

        then:
        !valid == org.hasErrors()

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

    def 'Fax constraints'() {
        when:
        def org = new Organization(
            recType: 1, name: 'foo', billingAddr: new Address(),
            shippingAddr: new Address(), fax: fax
        )
        org.validate()

        then:
        !valid == org.hasErrors()

        where:
        fax             | valid
        null            | true
        ''              | true
        ' '             | true
        'foo'           | true
        'any name'      | true
        'x' * 40        | true
        'x' * 50        | false
    }

    def 'PhoneOther constraints'() {
        when:
        def org = new Organization(
            recType: 1, name: 'foo', billingAddr: new Address(),
            shippingAddr: new Address(), phoneOther: phone
        )
        org.validate()

        then:
        !valid == org.hasErrors()

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

    def 'E-mail 1 constraints'(String email, boolean valid) {
        when:
        def org = new Organization(
            recType: 1, name: 'foo', billingAddr: new Address(),
            shippingAddr: new Address(), email1: email
        )

        then:
        valid == org.validate()

        where:
        email               || valid
        null                || true
        ''                  || true
        ' '                 || true
        'foo'               || false
        'any name'          || false
        'foobar@'           || false
        '@mydomain.com'     || false
        'user@mydomain'     || false
        'user@.com'         || false
        'user@mydomain.com' || true
        'user@härbört.com'  || true
    }

    def 'E-mail 2 constraints'(String email, boolean valid) {
        when:
        def org = new Organization(
            recType: 1, name: 'foo', billingAddr: new Address(),
            shippingAddr: new Address(), email2: email
        )

        then:
        valid == org.validate()

        where:
        email               || valid
        null                || true
        ''                  || true
        ' '                 || true
        'foo'               || false
        'any name'          || false
        'foobar@'           || false
        '@mydomain.com'     || false
        'user@mydomain'     || false
        'user@.com'         || false
        'user@mydomain.com' || true
        'user@härbört.com'  || true
    }

    def 'Website constraints'(String url, boolean valid) {
        when:
        def org = new Organization(
            recType: 1, name: 'foo', billingAddr: new Address(),
            shippingAddr: new Address(), website: url
        )

        then:
        valid == org.validate()

        where:
        url                                         || valid
        null                                        || true
        ''                                          || true
        ' '                                         || true
        'foobar'                                    || true
        'any name'                                  || true      // XXX should be false
        'mydomain.com'                              || true
        'www.mydomain.com'                          || true
        'http://www.mydomain.com'                   || true
        'https://www.mydomain.com'                  || true
        'ftp://www.mydomain.com'                    || true
        'file:///foo/bar/whee'                      || true
        'www.mydomain.com/foo/bar.html?id=5'        || true
        'http://www.mydomain.com/foo/bar.html?id=5' || true
        'http://www.mydomain.com/foo.html#bar'      || true
        'http://www.härbört.com'                    || true
    }
}
