/*
 * InvoicingTransactionXMLSpec.groovy
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
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.util.slurpersupport.GPathResult
import org.codehaus.groovy.grails.commons.GrailsApplication


@TestMixin([GrailsUnitTestMixin, DomainClassUnitTestMixin])
@Mock([
    Config, Invoice, InvoiceStage, InvoicingItem, Organization, Person,
    TermsAndConditions, User
])
class InvoicingTransactionXMLSpec extends InvoicingTransactionXMLBase {

    //-- Instance variables ---------------------

    InvoicingTransactionXMLFactory invoicingTransactionXMLFactory


    //-- Fixture methods ------------------------

    def setup() {
        invoicingTransactionXMLFactory =
            initInvoicingTransactionXMLFactory()
    }


    //-- Feature methods ------------------------

    def 'Convert to XML without writer'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'some client data'
        makeClientFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        when: 'I generate XML from this invoice'
        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)
        String xml = conv.toXML()

        then: 'I get valid XML'
        matchValidXML xml
    }

    def 'Convert to XML with toString'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'some client data'
        makeClientFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        when: 'I generate XML from this invoice'
        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)
        String xml = conv.toString()

        then: 'I get valid XML'
        matchValidXML xml
    }

    def 'Convert to XML with writer'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'some client data'
        makeClientFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        and: 'a writer'
        def out = new StringWriter()

        when: 'I generate XML from this invoice'
        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)
        conv.toXML(out)
        String xml = out.toString()

        then: 'the XML is the same as in the writer'
        conv.toString() == xml

        and: 'I get valid XML'
        matchValidXML xml
    }
}
