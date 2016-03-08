/*
 * InvoicingTransactionXMLSpec.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import grails.core.GrailsApplication
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.util.slurpersupport.GPathResult


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

    def 'Cannot modify internal data structure from outside'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        and: 'an XML converter'
        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)

        when: 'I obtain the data structure and modify it'
        Map d = conv.data
        d.foo = 'bar'

        then: 'the internal data structure must not be modified'
        !('foo' in conv.data)
    }

    def 'Cannot set internal data structure from outside'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        and: 'an XML converter'
        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)

        when: 'I set the data structure from outside'
        conv.data = [foo: 'bar']

        then: 'I get an exception'
        thrown ReadOnlyPropertyException

        and: 'the internal data structure is unmodified'
        !('foo' in conv.data)
        invoice == conv.data.transaction
        user == conv.data.user
    }

    def 'Can get and set duplicate property'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        when: 'I create an XML converter'
        InvoicingTransactionXML conv =
            invoicingTransactionXMLFactory.createConverter(invoice, user)

        then: 'the duplicate property is set to false'
        '' == conv.data.watermark
        !conv.duplicate

        when: 'I set the duplicate property to false'
        conv.duplicate = false

        then: 'the duplicate property is set to false'
        '' == conv.data.watermark
        !conv.duplicate

        when: 'I set the duplicate property to true'
        conv.duplicate = true

        then: 'the duplicate property is set to true'
        'duplicate' == conv.data.watermark
        conv.duplicate
    }

    def 'Can add additional data via add method'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        and: 'an XML converter'
        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)

        when: 'I add additional data'
        conv.add foo: 'bar', whee: 5.78

        then: 'these values are in the internal data structure'
        Map d1 = conv.data
        'bar' == d1.foo
        5.78 == d1.whee
        '' == d1.watermark

        when: 'I overwrite existing values'
        conv.add foo: 'bizz', watermark: 'bar'

        then: 'these values have been overwritten'
        Map d2 = conv.data
        'bizz' == d2.foo
        5.78 == d2.whee
        'bar' == d2.watermark
    }

    def 'Can add additional data via left shift operator'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user in the session'
        def user = makeUserFixture()

        and: 'an XML converter'
        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)

        when: 'I add additional data'
        conv << [foo: 'bar'] << [whee: 5.78]

        then: 'these values are in the internal data structure'
        Map d1 = conv.data
        'bar' == d1.foo
        5.78 == d1.whee
        '' == d1.watermark

        when: 'I overwrite existing values'
        conv << [foo: 'bizz'] << [watermark: 'bar']

        then: 'these values have been overwritten'
        Map d2 = conv.data
        'bizz' == d2.foo
        5.78 == d2.whee
        'bar' == d2.watermark
    }

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
