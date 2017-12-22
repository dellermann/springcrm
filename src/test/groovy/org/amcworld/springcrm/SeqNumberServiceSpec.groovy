/*
 * SeqNumberServiceSpec.groovy
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
import spock.lang.Specification

@TestFor(SeqNumberService)
@Mock([
    CreditMemo, CreditMemoController, Dunning, DunningController, Invoice,
    InvoiceController, Organization, OrganizationController, Quote,
    QuoteController, SalesOrder, SalesOrderController, SeqNumber, User,
    UserSetting
])
class SeqNumberServiceSpec extends Specification {

    //-- Fixture methods ------------------------

    def setup() {
        mockDomain SeqNumber, [
            new SeqNumber(controllerName: 'invoice', prefix: 'R', suffix: 'M'),
            new SeqNumber(controllerName: 'organization', prefix: 'O')
        ]
        assert 2 == SeqNumber.count()
    }


    //-- Feature methods ------------------------

    def 'Check suitable sequence numbers'() {
        given: 'the current year'
        String year = new Date().format('YY')

        and: 'a suitable start value'
        int start = (year + '000').toInteger()

        and: 'some sequence numbers'
        mockDomain SeqNumber, [
            new SeqNumber(
                controllerName: 'quote', prefix: 'Q', startValue: start
            ),
            new SeqNumber(
                controllerName: 'salesOrder', prefix: 'S', startValue: start
            ),
            new SeqNumber(
                controllerName: 'creditMemo', prefix: 'R', startValue: start
            ),
            new SeqNumber(
                controllerName: 'dunning', prefix: 'D', startValue: start
            )
        ]

        and: 'an already existing sequence number'
        SeqNumber seqNumber = service.loadSeqNumber('invoice')
        seqNumber.startValue = start
        seqNumber.save()

        and: 'a user with mocked settings'
        User user = makeUser()
        UserSettings settings = Mock()
        user.settings = settings

        and: 'a credential of that user'
        Credential cred = new Credential(user)

        expect: 'the sequence number scheme is suitable'
        service.checkNumberScheme()
        !service.getShowHint(cred)
    }

    def 'Check not suitable sequence numbers'() {
        given: 'the current year'
        String year = new Date().format('YY')

        and: 'a suitable start value'
        int start = (year + '000').toInteger()

        and: 'some sequence numbers'
        mockDomain SeqNumber, [
            new SeqNumber(
                controllerName: 'quote', prefix: 'Q', startValue: start
            ),
            new SeqNumber(
                controllerName: 'salesOrder', prefix: 'S', startValue: start
            ),
            new SeqNumber(
                controllerName: 'creditMemo', prefix: 'R', startValue: start
            ),
            new SeqNumber(
                controllerName: 'dunning', prefix: 'D', startValue: start
            )
        ]
        /* leave sequence number for invoices at start value 10000 */

        expect: 'the sequence number scheme is suitable'
        !service.checkNumberScheme()
    }

    def 'Don\'t show again hint for this year'(int year, boolean res) {
        given: 'a user with mocked settings'
        User user = makeUser()

        and: 'a credential of that user'
        Credential cred = new Credential(user)

        /* leave sequence number for invoices at start value 10000 */

        when:
        Date d = new Date()
        d.set(date: 31, month: Calendar.MARCH, year: year)
        new UserSetting(
                user: user, name: 'seqNumberHintYear', value: d.format('YYYY')
            ).save failOnError: true
        user.afterLoad()

        then:
        res == service.getShowHint(cred)

        where:
        year                            || res
        new Date()[Calendar.YEAR] - 4   || true
        new Date()[Calendar.YEAR] - 2   || true
        new Date()[Calendar.YEAR] - 1   || true
        new Date()[Calendar.YEAR]       || false
        new Date()[Calendar.YEAR] + 1   || false
        new Date()[Calendar.YEAR] + 2   || false
        new Date()[Calendar.YEAR] + 4   || false
        9999i                           || false
    }

    def 'Don\'t show again hint for non-admins'(int year) {
        given: 'a non-admin user with mocked settings'
        User user = makeUser()
        user.admin = false
        user.save failOnError: true

        and: 'a credential of that user'
        Credential cred = new Credential(user)

        /* leave sequence number for invoices at start value 10000 */

        when:
        Date d = new Date()
        d.set(date: 31, month: Calendar.MARCH, year: year)
        new UserSetting(
                user: user, name: 'seqNumberHintYear', value: d.format('YYYY')
            ).save failOnError: true
        user.afterLoad()

        then:
        !service.getShowHint(cred)

        where:
        year                            || _
        new Date()[Calendar.YEAR] - 4   || _
        new Date()[Calendar.YEAR] - 2   || _
        new Date()[Calendar.YEAR] - 1   || _
        new Date()[Calendar.YEAR]       || _
        new Date()[Calendar.YEAR] + 1   || _
        new Date()[Calendar.YEAR] + 2   || _
        new Date()[Calendar.YEAR] + 4   || _
        9999i                           || _
    }

    def 'Get fixed sequence numbers'() {
        given: 'the current year and another year'
        int year = new Date().format('YY').toInteger()
        int otherYear = year - 5
        int futureYear = year + 8

        and: 'suitable start values'
        int start = year * 1000
        int otherStart = otherYear * 1000
        int futureStart = futureYear * 1000

        and: 'some sequence numbers'
        mockDomain SeqNumber, [
            new SeqNumber(
                controllerName: 'quote', prefix: 'Q', startValue: otherStart
            ),
            new SeqNumber(
                controllerName: 'salesOrder', prefix: 'S', startValue: start
            ),
            new SeqNumber(
                controllerName: 'creditMemo', prefix: 'R',
                startValue: futureStart
            ),
            new SeqNumber(
                controllerName: 'dunning', prefix: 'D', startValue: otherStart
            )
        ]
        /* leave sequence number for invoices at start value 10000 */

        when: 'I obtain the fixed sequence numbers'
        List<SeqNumber> l = service.getFixedSeqNumbers()

        then: 'this list contains all sequence numbers'
        6 == l.size()

        and: 'all old customer accounts have the correct start value'
        start == l.find { it.controllerName == 'quote' }.startValue
        start == l.find { it.controllerName == 'salesOrder' }.startValue
        start == l.find { it.controllerName == 'invoice' }.startValue
        start == l.find { it.controllerName == 'dunning' }.startValue

        and: 'future customer accounts are untouched'
        futureStart == l.find { it.controllerName == 'creditMemo' }.startValue

        and: 'all other sequence numbers are untouched'
        10000 == l.find { it.controllerName == 'organization' }.startValue
    }

    def 'Store year for sequence number hint'() {
        given: 'a user with mocked settings'
        User user = makeUser()
        UserSettings settings = Mock()
        user.settings = settings

        and: 'a credential of that user'
        Credential cred = new Credential(user)

        when: 'I store the year'
        service.dontShowAgain = cred

        then: 'the current year has been stored'
        1 * settings.put('seqNumberHintYear', new Date().format('YYYY'))
    }

    def 'Store never show again for sequence number hint'() {
        given: 'a user with mocked settings'
        User user = makeUser()
        UserSettings settings = Mock()
        user.settings = settings

        and: 'a credential of that user'
        Credential cred = new Credential(user)

        when: 'I store the selection'
        service.neverShowAgain = cred

        then: 'a year in the future has been stored'
        1 * settings.put('seqNumberHintYear', '9999')
    }

    def 'Load sequence number of a known class'() {
        when: 'I load a sequence number by an artifact type'
        SeqNumber sn = service.loadSeqNumber('invoice')

        then: 'I get a valid sequence number object'
        'invoice' == sn.controllerName
        'R' == sn.prefix
        'M' == sn.suffix
        10000 == sn.startValue
        99999 == sn.endValue

        when: 'I load a sequence number by a domain model class'
        sn = service.loadSeqNumber(Invoice)

        then: 'I get a valid sequence number object'
        'invoice' == sn.controllerName
        'R' == sn.prefix
        'M' == sn.suffix
        10000 == sn.startValue
        99999 == sn.endValue

        when: 'I load a sequence number by a controller class'
        sn = service.loadSeqNumber(InvoiceController)

        then: 'I get a valid sequence number object'
        'invoice' == sn.controllerName
        'R' == sn.prefix
        'M' == sn.suffix
        10000 == sn.startValue
        99999 == sn.endValue
    }

    def 'Load sequence number of an unknown class'() {
        expect:
        null == service.loadSeqNumber('foo')
    }

    def 'Get next default sequence number'() {
        when: 'I retrieve the next sequence number by an artifact type'
        int n = service.nextNumber('invoice')

        then: 'I get a valid sequence number'
        10000 == n

        when: 'I retrieve the next sequence number by a domain model class'
        n = service.nextNumber(Invoice)

        then: 'I get a valid sequence number'
        10000 == n

        when: 'I retrieve the next sequence number by a controller class'
        n = service.nextNumber(InvoiceController)

        then: 'I get a valid sequence number'
        10000 == n
    }

    def 'Get next sequence number'() {
        given: 'an organization with a particular sequence number'
        createOrganization()

        when: 'I retrieve the next sequence number by an artifact type'
        int n = service.nextNumber('organization')

        then: 'I get a valid sequence number after the given one'
        40000 == n

        when: 'I retrieve the next sequence number by a domain model class'
        n = service.nextNumber(Organization)

        then: 'I get a valid sequence number after the given one'
        40000 == n

        when: 'I retrieve the next sequence number by a controller class'
        n = service.nextNumber(OrganizationController)

        then: 'I get a valid sequence number after the given one'
        40000 == n
    }

    def 'Get next sequence number using maxNumber method'() {
        given: 'an invoice and a quote with a particular sequence number'
        Organization org = createOrganization()
        Quote quote = createQuote(org)
        createInvoice org, quote

        when: 'I retrieve the next sequence number by an artifact type'
        int n = service.nextNumber('invoice')

        then: 'I get a valid sequence number after the given one'
        14001 == n

        when: 'I retrieve the next sequence number by a domain model class'
        n = service.nextNumber(Invoice)

        then: 'I get a valid sequence number'
        14001 == n

        when: 'I retrieve the next sequence number by a controller class'
        n = service.nextNumber(InvoiceController)

        then: 'I get a valid sequence number'
        14001 == n
    }

    def 'Get next full number with prefix'() {
        given: 'an organization with a particular sequence number'
        createOrganization()

        when: 'I retrieve the next full number by an artifact type'
        String s = service.nextFullNumber('organization')

        then: 'I get a valid full number with prefix'
        'O-40000' == s

        when: 'I retrieve the next full number by a domain model class'
        s = service.nextFullNumber(Organization)

        then: 'I get a valid full number with prefix'
        'O-40000' == s

        when: 'I retrieve the next full number by a controller class'
        s = service.nextFullNumber(OrganizationController)

        then: 'I get a valid full number with prefix'
        'O-40000' == s
    }

    def 'Get next full number with prefix and suffix'() {
        given: 'an invoice with a particular sequence number'
        createInvoice()

        when: 'I retrieve the next full number by an artifact type'
        String s = service.nextFullNumber('invoice')

        then: 'I get a valid full number with prefix'
        'R-14001-M' == s

        when: 'I retrieve the next full number by a domain model class'
        s = service.nextFullNumber(Invoice)

        then: 'I get a valid full number with prefix'
        'R-14001-M' == s

        when: 'I retrieve the next full number by a controller class'
        s = service.nextFullNumber(InvoiceController)

        then: 'I get a valid full number with prefix'
        'R-14001-M' == s
    }

    def 'Format a number with prefix'() {
        expect:
        s == service.format('organization', n)
        s == service.format(Organization, n)
        s == service.format(OrganizationController, n)

        where:
            n || s
            0 || 'O-0'
            1 || 'O-1'
           10 || 'O-10'
         1005 || 'O-1005'
        10000 || 'O-10000'
        10608 || 'O-10608'
        39999 || 'O-39999'
        99999 || 'O-99999'
    }

    def 'Format a number with prefix and suffix'() {
        expect:
        s == service.format('invoice', n)
        s == service.format(Invoice, n)
        s == service.format(InvoiceController, n)

        where:
            n || s
            0 || 'R-0-M'
            1 || 'R-1-M'
           10 || 'R-10-M'
         1005 || 'R-1005-M'
        10000 || 'R-10000-M'
        10608 || 'R-10608-M'
        39999 || 'R-39999-M'
        99999 || 'R-99999-M'
    }

    def 'Format a prefix sequence number with prefix only'() {
        expect:
        s == service.formatWithPrefix('organization', n)
        s == service.formatWithPrefix(Organization, n)
        s == service.formatWithPrefix(OrganizationController, n)

        where:
            n || s
            0 || 'O-0'
            1 || 'O-1'
           10 || 'O-10'
         1005 || 'O-1005'
        10000 || 'O-10000'
        10608 || 'O-10608'
        39999 || 'O-39999'
        99999 || 'O-99999'
    }

    def 'Format a prefix/suffix sequence number with prefix only'() {
        expect:
        s == service.formatWithPrefix('invoice', n)
        s == service.formatWithPrefix(Invoice, n)
        s == service.formatWithPrefix(InvoiceController, n)

        where:
            n || s
            0 || 'R-0'
            1 || 'R-1'
           10 || 'R-10'
         1005 || 'R-1005'
        10000 || 'R-10000'
        10608 || 'R-10608'
        39999 || 'R-39999'
        99999 || 'R-99999'
    }

    def 'Format a prefix sequence number with suffix only'() {
        expect:
        s == service.formatWithSuffix('organization', n)
        s == service.formatWithSuffix(Organization, n)
        s == service.formatWithSuffix(OrganizationController, n)

        where:
            n || s
            0 || '0'
            1 || '1'
           10 || '10'
         1005 || '1005'
        10000 || '10000'
        10608 || '10608'
        39999 || '39999'
        99999 || '99999'
    }

    def 'Format a prefix/suffix sequence number with suffix only'() {
        expect:
        s == service.formatWithSuffix('invoice', n)
        s == service.formatWithSuffix(Invoice, n)
        s == service.formatWithSuffix(InvoiceController, n)

        where:
            n || s
            0 || '0-M'
            1 || '1-M'
           10 || '10-M'
         1005 || '1005-M'
        10000 || '10000-M'
        10608 || '10608-M'
        39999 || '39999-M'
        99999 || '99999-M'
    }


    //-- Non-public methods ---------------------

    private Invoice createInvoice(Organization org = createOrganization(),
                                  Quote quote = null) {
        def invoice = new Invoice(
            number: 14000, subject: 'Foo', dueDatePayment: new Date(),
            organization: org, quote: quote, docDate: new Date(),
            stage: new InvoiceStage(name: 'paid'),
            billingAddr: new Address(), shippingAddr: new Address(),
            items: []
        )
        invoice.items << new InvoicingItem(
            number: 'P-10000', quantity: 4, unit: 'pcs.',
            name: 'books', unitPrice: 44.99, tax: 19
        )

        mockDomain Invoice, [invoice]

        invoice
    }

    private Organization createOrganization() {
        def org = new Organization(
            number: 39999, recType: 1, name: 'YourOrganization Ltd.',
            billingAddr: new Address(), shippingAddr: new Address()
        )

        mockDomain Organization, [org]

        org
    }

    private Quote createQuote(Organization org = createOrganization()) {
        def q = new Quote(
            number: 29999, subject: 'Foo', organization: org,
            docDate: new Date(), stage: new QuoteStage(name: 'delivered'),
            billingAddr: new Address(), shippingAddr: new Address(),
            items: []
        )
        q.items << new InvoicingItem(
            number: 'P-10000', quantity: 4, unit: 'pcs.',
            name: 'books', unitPrice: 44.99, tax: 19
        )

        mockDomain Quote, [q]

        q
    }

    private User makeUser() {
        def u = new User(
            username: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com',
            admin: true,
            allowedModules: 'CALL, TICKET, NOTE'
        )
        u.id = 1704L

        u.save failOnError: true
    }
}
