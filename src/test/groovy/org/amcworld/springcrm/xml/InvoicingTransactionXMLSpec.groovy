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


package org.amcworld.springcrm.xml

import com.naleid.grails.MarkdownService
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import groovy.util.slurpersupport.GPathResult
import org.amcworld.springcrm.*
import org.grails.web.converters.configuration.ConvertersConfigurationInitializer
import org.springframework.core.io.Resource
import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import spock.lang.Specification


@TestMixin([GrailsUnitTestMixin, DomainClassUnitTestMixin])
@Mock([
    Invoice, InvoiceStage, InvoicingItem, Organization, Person,
    TermsAndConditions, User
])
class InvoicingTransactionXMLSpec extends Specification {

    //-- Fields ---------------------------------

    XmlSlurper slurper


    //-- Fixture methods ------------------------

    def setup() {
        registerDefaultConverters()
        makeClientFixture()
        initXmlSlurper()
    }


    //-- Feature methods ------------------------

    def 'Cannot modify internal data structure from outside'() {
        given: 'an invoice'
        def invoice = new Invoice()

        and: 'a user'
        def user = new User()

        and: 'an XML converter'
        def conv = new InvoicingTransactionXML(invoice, user)

        when: 'I obtain the data structure and modify it'
        Map d = conv.data
        d.foo = 'bar'

        then: 'the internal data structure must not be modified'
        !('foo' in conv.data)
    }

    def 'Cannot set internal data structure from outside'() {
        given: 'an invoice'
        def invoice = new Invoice()

        and: 'a user'
        def user = new User()

        and: 'an XML converter'
        def conv = new InvoicingTransactionXML(invoice, user)

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
        def invoice = new Invoice()

        and: 'a user'
        def user = new User()

        when: 'I create an XML converter'
        def conv = new InvoicingTransactionXML(invoice, user)

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
        def invoice = new Invoice()

        and: 'a user'
        def user = new User()

        and: 'an XML converter'
        def conv = new InvoicingTransactionXML(invoice, user)

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
        def invoice = new Invoice()

        and: 'a user'
        def user = new User()

        and: 'an XML converter'
        def conv = new InvoicingTransactionXML(invoice, user)

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

        and: 'a user'
        def user = makeUserFixture()

        and: 'the converter'
        def conv = new InvoicingTransactionXML(invoice, user)
        conv.markdownService = makeMarkdownService()

        when: 'I generate XML from this invoice'
        String xml = conv.toXML()

        then: 'I get valid XML'
        matchValidXML xml
    }

    def 'Convert to XML with toString'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user'
        def user = makeUserFixture()

        and: 'the converter'
        def conv = new InvoicingTransactionXML(invoice, user)
        conv.markdownService = makeMarkdownService()

        when: 'I generate XML from this invoice'
        String xml = conv.toString()

        then: 'I get valid XML'
        matchValidXML xml
    }

    def 'Convert to XML with writer'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user'
        def user = makeUserFixture()

        and: 'a writer'
        def out = new StringWriter()

        and: 'the converter'
        def conv = new InvoicingTransactionXML(invoice, user)
        conv.markdownService = makeMarkdownService()

        when: 'I generate XML from this invoice'
        conv.toXML(out)

        then: 'the XML is the same as in the writer'
        String xml = out.toString()
        conv.toString() == xml

        and: 'I get valid XML'
        matchValidXML xml
    }


    //-- Non-public methods ---------------------

    private static GPathResult getEntry(GPathResult parent, String name) {
        parent.entry.find { it.@key == name } as GPathResult
    }

    private static String getEntryText(GPathResult parent, String name) {
        getEntry(parent, name)?.text()
    }

    private void initXmlSlurper() {
        slurper = new XmlSlurper(false, true, true)
        slurper.entityResolver = { String publicId, String systemId ->
            String path = InvoicingTransactionXML.ENTITY_CATALOG[publicId]
            if (!path) {
                return null
            }

            Resource res = applicationContext.getResource(
                "${FopService.SYSTEM_FOLDER}/dtd/${path}"
            )
            new InputSource(res.inputStream)
        } as EntityResolver
    }

    private void makeClientFixture() {
        mockDomain Config, [
            [name: 'clientName', value: 'MyOrganization Ltd.'],
            [name: 'clientStreet', value: '45, Park Ave.'],
            [name: 'clientPostalCode', value: 'NY-39344'],
            [name: 'clientLocation', value: 'Santa Barbara'],
            [name: 'clientPhone', value: '+1 21 20404044'],
            [name: 'clientEmail', value: 'info@myorganization.example'],
            [name: 'clientWebsite', value: 'www.myorganization.example'],
            [name: 'clientBankName', value: 'YourBank'],
            [name: 'clientBankCode', value: '123456789'],
            [name: 'clientAccountNumber', value: '987654321'],
        ]
    }

    private Invoice makeInvoiceFixture() {
        def org = new Organization(
            number: 10000, recType: 1, name: 'AMC World Technologies GmbH',
            legalForm: 'GmbH', billingAddr: new Address(),
            shippingAddr: new Address(), phone: '987654321'
        )
        mockDomain Organization, [org]

        def invoice = new Invoice(
            adjustment: 0.54,
            billingAddr: new Address(
                street: '12, Leonardo Rd.', postalCode: 'NY-39830',
                location: 'Mt. Elber', state: 'NY', country: 'USA'
            ),
            discountAmount: 5,
            discountPercent: 2,
            dueDatePayment: new Date(),
            footerText: 'my footer text... & more',
            headerText: 'my header text',
            items: [],
            notes: 'my notes',
            number: 39999,
            organization: org,
            person: new Person(
                number: 10000, organization: org, firstName: 'Daniel',
                lastName: 'Ellermann', mailingAddr: new Address(),
                otherAddr: new Address(), phone: '123456789'
            ),
            shippingAddr: new Address(),
            shippingCosts: 4.5,
            shippingTax: 7,
            stage: new InvoiceStage(id: 903, name: 'paid'),
            subject: 'Test invoice\nand more',
            termsAndConditions: [
                new TermsAndConditions(id: 700, name: 'wares'),
                new TermsAndConditions(id: 701, name: 'works')
            ],
            total: 414.632541
        )
        invoice.items << new InvoicingItem(
            quantity: 4, unit: 'pcs.', name: 'books', unitPrice: 44.99, tax: 19
        )
        invoice.items << new InvoicingItem(
            quantity: 10.5, unit: 'm', name: 'tape', unitPrice: 0.89, tax: 7
        )
        invoice.items << new InvoicingItem(
            quantity: 4.25, unit: 'h', name: 'repairing', unitPrice: 39,
            tax: 19
        )

        SeqNumberService seqNumberService = Mock()
        seqNumberService.formatWithPrefix(_, _) >> 'R-39999'
        invoice.seqNumberService = seqNumberService

        mockDomain Invoice, [invoice]

        invoice
    }

    private MarkdownService makeMarkdownService() {
        MarkdownService markdownService = Mock()
        markdownService.markdown(_) >> {
            String s = it[0].toString()
                .replace('&', '&amp;')
                .replace('<', '&lt;')
            println "converted XML: $s"

            "<p>${s}</p>".toString()
        }

        markdownService
    }

    private static User makeUserFixture() {
        new User(
            userName: 'jsmith', password: 'secret', firstName: 'John',
            lastName: 'Smith', email: 'j.smith@example.com',
            phone: '+49 1 8984466', mobile: '+49 1 464163464'
        )
    }

    private static void matchHtmlValues(GPathResult entry) {
        assert '12, Leonardo Rd.' == entry.billingAddrStreetHtml.'html:html'.'html:body'.'html:p'.text()
        assert !entry.shippingAddrStreetHtml.'html:html'.'html:body'.text()
        def el = entry.subjectHtml
        assert 'Test invoiceand more' == el.text()
        assert !el.'html:br'.isEmpty()
        // XXX currently we cannot test where <br /> is within the string; it's not implemented by Groovy (see http://jira.codehaus.org/browse/GROOVY-2115)
        assert 'my header text' == entry.headerTextHtml.'html:html'.'html:body'.text()
        assert 'my footer text... & more' == entry.footerTextHtml.'html:html'.'html:body'.text()
        el = entry.itemsHtml.descriptionHtml
        assert 3 == el.size()
    }

    private static void matchInvoicingTransaction(GPathResult entry) {
        assert '1' == entry.@id.text()
        def addr = entry.billingAddr
        assert '12, Leonardo Rd.' == addr.street.text()
        assert 'NY-39830' == addr.postalCode.text()
        assert 'Mt. Elber' == addr.location.text()
        assert 'NY' == addr.state.text()
        assert 'USA' == addr.country.text()
        assert 'my footer text... & more' == entry.footerText.text()
        assert 'my header text' == entry.headerText.text()
        assert '39999' == entry.number.text()
        assert '1' == entry.organization.@id.text()
        assert entry.organization.empty
        assert '' == entry.organization.text()
        assert 'Test invoice\nand more' == entry.subject.text()
    }

    private static void matchItems(GPathResult entry) {
        assert 3 == entry.invoicingItem.size()
        def item = entry.invoicingItem[0]
        assert 4.0 == item.quantity.toBigDecimal()
        assert 'pcs.' == item.unit.text()
        assert 'books' == item.name.text()
        assert 44.99 == item.unitPrice.toBigDecimal()
        assert 19.0 == item.tax.toBigDecimal()
    }

    private static void matchOrganization(GPathResult entry) {
        assert '1' == entry.@id.text()
        assert 'AMC World Technologies GmbH' == entry.name.text()
        assert '10000' == entry.number.text()
    }

    private static void matchTaxRates(GPathResult entry) {
        assert 2 == entry.entry.size()
        assert '65.68490000000000' == getEntryText(entry, '19.0')
        assert '0.96915000000000' == getEntryText(entry, '7.0')
    }

    private static void matchUser(GPathResult entry) {
        assert 'John' == entry.firstName.text()
        assert 'Smith' == entry.lastName.text()
        assert '+49 1 8984466' == entry.phone.text()
        assert '+49 1 464163464' == entry.mobile.text()
        assert 'j.smith@example.com' == entry.email.text()
        assert entry.password.empty
        assert !entry.password.text()
    }

    private void matchValidXML(String xml) {
        assert null != xml
        assert '' != xml
        assert xml.startsWith('<?xml version="1.0" encoding="UTF-8"?>')

        def map = slurper.parseText(xml)
            .declareNamespace(html: 'http://www.w3.org/1999/xhtml')
        assert 10 == map.entry.size()

        matchInvoicingTransaction getEntry(map, 'transaction')
        matchItems getEntry(map, 'items')
        matchOrganization getEntry(map, 'organization')
        matchUser getEntry(map, 'user')
        assert 'R-39999-10000' == getEntryText(map, 'fullNumber')
        matchTaxRates getEntry(map, 'taxRates')
        assert !getEntryText(map, 'watermark')
        matchValues getEntry(map, 'values')
        matchHtmlValues map
    }

    private static void matchValues(GPathResult entry) {
        assert 359.555 == getEntry(entry, 'subtotalNet').toBigDecimal()
        assert 426.20905 == getEntry(entry, 'subtotalGross').toBigDecimal()
        assert 8.524181 == getEntry(entry, 'discountPercentAmount')
            .toBigDecimal()
        assert 413.224869 == getEntry(entry, 'total').toBigDecimal()
    }

    private void registerDefaultConverters() {
        def initializer = new ConvertersConfigurationInitializer()
        initializer.applicationContext = applicationContext
        initializer.grailsApplication = grailsApplication
        initializer.initialize()
    }
}
