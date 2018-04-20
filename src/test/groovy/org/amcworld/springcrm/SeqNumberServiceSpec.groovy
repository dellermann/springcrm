/*
 * SeqNumberServiceSpec.groovy
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

import com.github.fakemongo.Fongo
import com.mongodb.client.MongoCollection
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import java.beans.Introspector
import org.bson.Document as MDocument
import org.grails.datastore.mapping.services.Service
import spock.lang.Ignore
import spock.lang.Specification


class SeqNumberServiceSpec extends Specification
    implements ServiceUnitTest<SeqNumberService>, DataTest
{

    //-- Fixture methods ------------------------

    def setup() {
        Fongo fongo = new Fongo('mongo test server')
        service.mongo = fongo.mongo

        mockDomains(
            CreditMemo, Dunning, Invoice, Organization, Quote, SalesOrder
        )
        def seq1 = new SeqNumber(prefix: 'R', suffix: 'M')
        seq1.id = 'invoice'
        def seq2 = new SeqNumber(prefix: 'O')
        seq2.id = 'organization'
        mockDomain SeqNumber, [seq1, seq2]
        assert 2 == SeqNumber.count()
    }


    //-- Feature methods ------------------------

    def 'Check suitable sequence numbers'() {
        given: 'the current year'
        String year = new Date().format('YY')

        and: 'a suitable start value'
        int start = (year + '000').toInteger()

        and: 'some sequence numbers'
        def seq1 = new SeqNumber(prefix: 'Q', startValue: start)
        seq1.id = 'quote'
        def seq2 = new SeqNumber(prefix: 'S', startValue: start)
        seq2.id = 'salesOrder'
        def seq3 = new SeqNumber(prefix: 'R', startValue: start)
        seq3.id = 'creditMemo'
        def seq4 = new SeqNumber(prefix: 'D', startValue: start)
        seq4.id = 'dunning'
        mockDomain SeqNumber, [seq1, seq2, seq3, seq4]

        and: 'an already existing sequence number'
        SeqNumber seqNumber = service.get('invoice')
        seqNumber.startValue = start
        seqNumber.save flush: true

        and: 'a user'
        User user = makeUser()

        expect: 'the sequence number scheme is suitable'
        service.checkNumberScheme()
        !service.getShowHint(user)
    }

    def 'Check not suitable sequence numbers'() {
        given: 'the current year'
        String year = new Date().format('YY')

        and: 'a suitable start value'
        int start = (year + '000').toInteger()

        and: 'some sequence numbers'
        def seq1 = new SeqNumber(prefix: 'Q', startValue: start)
        seq1.id = 'quote'
        def seq2 = new SeqNumber(prefix: 'S', startValue: start)
        seq2.id = 'salesOrder'
        def seq3 = new SeqNumber(prefix: 'R', startValue: start)
        seq3.id = 'creditMemo'
        def seq4 = new SeqNumber(prefix: 'D', startValue: start)
        seq4.id = 'dunning'
        mockDomain SeqNumber, [seq1, seq2, seq3, seq4]
        /* leave sequence number for invoices at start value 10000 */

        expect: 'the sequence number scheme is suitable'
        !service.checkNumberScheme()
    }

    @SuppressWarnings("GroovyPointlessBoolean")
    def 'Do not show again hint for this year'(int year, boolean res) {
        given: 'a user with mocked settings'
        User user = makeUser()
        user.addToAuthorities(
            new RoleGroup(
                name: 'Admin', authorities: [new Role(authority: 'ROLE_ADMIN')]
            )
        )
        user.save failOnError: true

        /* leave sequence number for invoices at start value 10000 */

        and: 'a user setting service instance'
        UserSettingService userSettingService = Mock()
        service.userSettingService = userSettingService

        and: 'a date within the given year'
        Date d = new Date()
        d.set date: 31, month: Calendar.MARCH, year: year

        when: 'the method is called with the user'
        boolean b = service.getShowHint(user)

        then: 'the result is as expected'
        1 * userSettingService.getInteger(user, 'seqNumberHintYear') >>
            d.format('YYYY').toInteger()
        res == b

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

    def 'Do not show again hint for non-admins'(int year) {
        given: 'a non-admin user with mocked settings'
        User user = makeUser()

        /* leave sequence number for invoices at start value 10000 */

        and: 'a user setting service instance'
        UserSettingService userSettingService = Mock()
        service.userSettingService = userSettingService

        and: 'a date within the given year'
        Date d = new Date()
        d.set date: 31, month: Calendar.MARCH, year: year

        when: 'the method is called with the user'
        boolean b = service.getShowHint(user)

        then: 'the result is as expected'
        0 * userSettingService.getInteger(user, 'seqNumberHintYear')
        !b

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
        def seq1 = new SeqNumber(prefix: 'Q', startValue: otherStart)
        seq1.id = 'quote'
        def seq2 = new SeqNumber(prefix: 'S', startValue: start)
        seq2.id = 'salesOrder'
        def seq3 = new SeqNumber(prefix: 'R', startValue: futureStart)
        seq3.id = 'creditMemo'
        def seq4 = new SeqNumber(prefix: 'D', startValue: otherStart)
        seq4.id = 'dunning'
        mockDomain SeqNumber, [seq1, seq2, seq3, seq4]
        SeqNumber.count()       // XXX needed to persist above sequence numbers
        /* leave sequence number for invoices at start value 10000 */

        when: 'I obtain the fixed sequence numbers'
        List<SeqNumber> l = service.getFixedSeqNumbers()

        then: 'this list contains all sequence numbers'
        6 == l.size()

        and: 'all old customer accounts have the correct start value'
        start == l.find { it.id == 'quote' }.startValue
        start == l.find { it.id == 'salesOrder' }.startValue
        start == l.find { it.id == 'invoice' }.startValue
        start == l.find { it.id == 'dunning' }.startValue

        and: 'future customer accounts are untouched'
        futureStart == l.find { it.id == 'creditMemo' }.startValue

        and: 'all other sequence numbers are untouched'
        10000 == l.find { it.id == 'organization' }.startValue
    }

    def 'Store year for sequence number hint'() {
        given: 'a user'
        User user = makeUser()

        and: 'a user setting service'
        UserSettingService userSettingService = Mock()
        service.userSettingService = userSettingService

        when: 'the year is stored'
        service.dontShowAgain = user

        then: 'the current year has been persisted'
        1 * userSettingService.store(
            user, 'seqNumberHintYear', new Date().format('YYYY')
        )
    }

    def 'Store never show again for sequence number hint'() {
        given: 'a user'
        User user = makeUser()

        and: 'a user setting service'
        UserSettingService userSettingService = Mock()
        service.userSettingService = userSettingService

        when: 'the decision is stored'
        service.neverShowAgain = user

        then: 'a year in the future has been persisted'
        1 * userSettingService.store(user, 'seqNumberHintYear', '9999')
    }

    def 'Load sequence number of a known class'() {
        when: 'I load a sequence number by an artifact type'
        SeqNumber sn = service.get('invoice')

        then: 'I get a valid sequence number object'
        'invoice' == sn.id
        'R' == sn.prefix
        'M' == sn.suffix
        10000 == sn.startValue
        99999 == sn.endValue

        when: 'I load a sequence number by a domain model class'
        sn = service.loadSeqNumber(Invoice)

        then: 'I get a valid sequence number object'
        'invoice' == sn.id
        'R' == sn.prefix
        'M' == sn.suffix
        10000 == sn.startValue
        99999 == sn.endValue

//        when: 'I load a sequence number by a controller class'
//        sn = service.loadSeqNumber(InvoiceController)
//
//        then: 'I get a valid sequence number object'
//        'invoice' == sn.controllerName
//        'R' == sn.prefix
//        'M' == sn.suffix
//        10000 == sn.startValue
//        99999 == sn.endValue
    }

    def 'Load sequence number of an unknown class'() {
        expect:
        null == service.get('foo')
    }

    def 'Get next default sequence number'() {
        expect:
        10000 == service.nextNumber('invoice')
        10000 == service.nextNumber(Invoice)
//        10000 == service.nextNumber(InvoiceController)
    }

    @Ignore('Fongo aggregating does not work in the moment')
    def 'Get next sequence number'() {
        given: 'an organization with a particular sequence number'
        mockOrganization()

        expect:
        40000 == service.nextNumber('organization')
        40000 == service.nextNumber(Organization)
//        40000 == service.nextNumber(OrganizationController)
    }

    @Ignore('Fongo aggregating does not work in the moment')
    def 'Get next sequence number using maxNumber method'() {
        given: 'an invoice and a quote with a particular sequence number'
        Organization org = createOrganization()
        Quote quote = createQuote(org)
        createInvoice org, quote

        expect:
        14001 == service.nextNumber('invoice')
        14001 == service.nextNumber(Invoice)
//        14001 == service.nextNumber(InvoiceController)
    }

    def 'Get the full name for classes which do not support full names'() {
        given: 'an organization'
        Organization org = createOrganization()

        expect:
        'O-39999' == service.getFullName(org)
    }

    def 'Get the full name for classes which support full names'() {
        given: 'an organization'
        Organization org = createOrganization()

        and: 'an invoice'
        Invoice invoice = createInvoice(org)

        expect:
        'R-14000-39999-M Foo' == service.getFullName(invoice)
    }

    def 'Get the full number'() {
        given: 'an organization'
        Organization org = createOrganization()

        expect:
        'O-39999' == service.getFullNumber(org)
    }


    //-- Public methods -------------------------

    /*
     * XXX moved down from DataTest because line
     *
     *      Service service = (Service) dataStore.getService(serviceClass)
     *
     * throws a NoSuchMethodError when calling getService().  I really don't
     * know why.
     */
    void mockDataService(Class<?> serviceClass) {
        Service service = (Service) dataStore.getService(
            (Class<Service>) serviceClass
        )
        String name = Introspector.decapitalize(serviceClass.simpleName)
        if (!applicationContext.containsBean(name)) {
            applicationContext.beanFactory.autowireBean service
            service.datastore = dataStore
            applicationContext.beanFactory.registerSingleton name, service
        }
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

        invoice.save flush: true
    }

    private Organization createOrganization() {
        def org = new Organization(
            number: 39999, recType: 1, name: 'YourOrganization Ltd.',
            billingAddr: new Address(), shippingAddr: new Address()
        )

        mockDomain Organization, [org]

        org.save flush: true
    }

    private Quote createQuote(Organization org = createOrganization()) {
        def quote = new Quote(
            number: 29999, subject: 'Foo', organization: org,
            docDate: new Date(), stage: new QuoteStage(name: 'delivered'),
            billingAddr: new Address(), shippingAddr: new Address(),
            items: []
        )
        quote.items << new InvoicingItem(
            number: 'P-10000', quantity: 4, unit: 'pcs.',
            name: 'books', unitPrice: 44.99, tax: 19
        )

        mockDomain Quote, [quote]

        quote.save flush: true
    }

    private User makeUser() {
        User user = new User(
            username: 'jsmith',
            password: 'abcd',
            firstName: 'John',
            lastName: 'Smith',
            phone: '+49 30 1234567',
            phoneHome: '+49 30 9876543',
            mobile: '+49 172 3456789',
            fax: '+49 30 1234568',
            email: 'j.smith@example.com'
        )
        mockDomain User, [user]

        user
    }

    private void mockOrganization() {
        given: 'a fake mongo instance'
        Fongo fongo = new Fongo('mongo test server')
        service.mongo = fongo.mongo

        and: 'some note instances'
        String dbName = grailsApplication.config.getProperty(
            'grails.mongodb.databaseName'
        )
        MongoCollection<org.bson.Document> collection =
            fongo.mongo.getDatabase(dbName).getCollection('organization')
        MDocument doc = new MDocument(
            number: 39999, recType: 1, name: 'YourOrganization Ltd.'
        )
        collection.insertOne doc
    }
}
