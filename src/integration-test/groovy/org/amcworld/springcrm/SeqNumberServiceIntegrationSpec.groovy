/*
 * SeqNumberServiceIntegrationSpec.groovy
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
 *
 */


package org.amcworld.springcrm

import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification


@Integration
@Rollback
class SeqNumberServiceIntegrationSpec extends Specification {

    //-- Fields -------------------------------------

    @Autowired
    SeqNumberService seqNumberService


    //-- Feature methods ------------------------

    void 'Get next sequence number'() {
        given: 'an organization with a particular sequence number'
        createOrganization()

        expect:
        40000 == seqNumberService.nextNumber('organization')
        40000 == seqNumberService.nextNumber(Organization)
        40000 == seqNumberService.nextNumber(OrganizationController)
    }

    void 'Get next sequence number using maxNumber method'() {
        given: 'an invoice and a quote with a particular sequence number'
        Organization org = createOrganization()
        Quote quote = createQuote(org)
        createInvoice org, quote

        expect:
        14001 == seqNumberService.nextNumber('invoice')
        14001 == seqNumberService.nextNumber(Invoice)
        14001 == seqNumberService.nextNumber(InvoiceController)
    }

    def 'Get next default sequence number'() {
        expect:
        10000 == seqNumberService.nextNumber('invoice')
        10000 == seqNumberService.nextNumber(Invoice)
        10000 == seqNumberService.nextNumber(InvoiceController)
    }


    //-- Non-public methods -------------------------

    private static Invoice createInvoice(
        Organization org = createOrganization(), Quote quote = null
    ) {
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

        invoice.save flush: true
    }

    private static Organization createOrganization() {
        new Organization(
            number: 39999, recType: 1, name: 'YourOrganization Ltd.',
            billingAddr: new Address(), shippingAddr: new Address()
        ).save flush: true
    }

    private static Quote createQuote(Organization org = createOrganization()) {
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

        quote.save flush: true
    }
}
