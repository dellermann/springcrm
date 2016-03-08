/*
 * InvoicingTransactionXMLSpec.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
import groovy.util.slurpersupport.GPathResult


@TestFor(InvoicingTransactionService)
@Mock([
    Config, Invoice, InvoiceStage, InvoicingItem, Organization, Person,
    TermsAndConditions, User
])
class InvoicingTransactionServiceSpec extends InvoicingTransactionXMLBase {

    //-- Fixture methods ------------------------

    def setup() {
        service.invoicingTransactionXMLFactory =
            initInvoicingTransactionXMLFactory()
    }


    //-- Feature methods ------------------------

    def 'Generate XML for an invoice'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'some client data'
        makeClientFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        when: 'I generate XML from this invoice'
        String xml = service.generateXML(invoice, user)

        then: 'I get valid XML'
        matchValidXML xml
    }

    // TODO test the other methods
}
