/*
 * InvoicingTransactionXMLSpec.groovy
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


package org.amcworld.springcrm.xml

import com.naleid.grails.MarkdownService
import grails.testing.gorm.DataTest
import groovy.util.slurpersupport.GPathResult
import org.amcworld.springcrm.*
import org.bson.types.ObjectId
import org.grails.testing.GrailsUnitTest
import org.grails.web.converters.configuration.ConvertersConfigurationInitializer
import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import spock.lang.Specification


class InvoicingTransactionXMLSpec extends Specification
    implements GrailsUnitTest, DataTest
{

    //-- Fields ---------------------------------

    XmlSlurper slurper


    //-- Fixture methods ------------------------

    def setup() {
        mockDomain Address
        mockDomain Invoice
        mockDomain InvoiceStage
        mockDomain InvoicingItem
        mockDomain Organization
        mockDomain Person
        mockDomain TermsAndConditions
        mockDomain User

        initXmlSlurper()
    }


    //-- Feature methods ------------------------

    void 'Object is initialized'() {
        given: 'an XML converter'
        def converter = new InvoicingTransactionXML()

        expect:
        null != converter.data
        1 == converter.data.size()
        '' == converter.data.watermark
        !converter.duplicate
    }

    void 'Cannot modify internal data structure from outside'() {
        given: 'an XML converter'
        def converter = new InvoicingTransactionXML()

        when: 'the data structure is obtained and then modified'
        Map d = converter.data
        d.foo = 'bar'

        then: 'the internal data structure has not been modified'
        !('foo' in converter.data)
    }

    void 'Can get and set duplicate property'() {
        given: 'an XML converter'
        def converter = new InvoicingTransactionXML()

        when: 'the duplicate property is set to false'
        converter.duplicate = false

        then: 'the duplicate property and the watermark have the correct values'
        '' == converter.data.watermark
        !converter.duplicate

        when: 'the duplicate property is set to true'
        converter.duplicate = true

        then: 'the duplicate property and the watermark have the correct values'
        'duplicate' == converter.data.watermark
        converter.duplicate
    }

    void 'Can add additional data via add method'() {
        given: 'an XML converter'
        def converter = new InvoicingTransactionXML()

        when: 'additional data are added'
        converter.add foo: 'bar', whee: 5.78

        then: 'these values are in the internal data structure'
        Map d1 = converter.data
        'bar' == d1.foo
        5.78 == d1.whee
        '' == d1.watermark

        when: 'existing values are overwritten'
        converter.add foo: 'bizz', watermark: 'bar'

        then: 'these values have been overwritten'
        Map d2 = converter.data
        'bizz' == d2.foo
        5.78 == d2.whee
        'bar' == d2.watermark
    }

    void 'Can add additional data via left shift operator'() {
        given: 'an XML converter'
        def converter = new InvoicingTransactionXML()

        when: 'additional data are added'
        converter << [foo: 'bar'] << [whee: 5.78]

        then: 'these values are in the internal data structure'
        Map d1 = converter.data
        'bar' == d1.foo
        5.78 == d1.whee
        '' == d1.watermark

        when: 'existing values are overwritten'
        converter << [foo: 'bizz'] << [watermark: 'bar']

        then: 'these values have been overwritten'
        Map d2 = converter.data
        'bizz' == d2.foo
        5.78 == d2.whee
        'bar' == d2.watermark
    }

    void 'Populate with data from invoicing transaction'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user'
        def user = makeUserFixture()

        and: 'tenant data'
        def tenant = makeTenantFixture()

        and: 'some service instances'
        SeqNumberService seqNumberService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(invoice) >> 'R-10004-18000'
        ConfigService configService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * configService.loadTenantAsMap() >> tenant

        and: 'an XML converter'
        def converter = new InvoicingTransactionXML()
        converter.seqNumberService = seqNumberService
        converter.configService = configService

        when: 'the converter is populated'
        converter.start invoice, user

        then: 'the internal data structure contains correct values'
        Map data = converter.data
        invoice.is data.transaction
        invoice.items.is data.items
        invoice.organization.is data.organization
        invoice.person.is data.person
        user == data.user
        'R-10004-18000' == data.fullNumber
        invoice.taxRateSums == data.taxRates
        null != data.values
        invoice.subtotalNet == data.values.subtotalNet
        invoice.subtotalGross == data.values.subtotalGross
        invoice.discountPercentAmount == data.values.discountPercentAmount
        invoice.total == data.values.total
        '' == data.watermark
        tenant.is data.client
    }

    void 'Cannot convert to XML without calling start()'() {
        given: 'an XML converter'
        def converter = new InvoicingTransactionXML()

        when: 'conversion to XML is attempted'
        converter.toXML()

        then: 'an exception is thrown'
        thrown IllegalStateException

        when: 'conversion to string is attempted'
        converter.toXML()

        then: 'an exception is thrown'
        thrown IllegalStateException

        when: 'writing to a writer is attempted'
        converter.toXML new StringWriter()

        then: 'an exception is thrown'
        thrown IllegalStateException
    }

    void 'Convert to XML without writer'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user'
        def user = makeUserFixture()

        and: 'tenant data'
        def tenant = makeTenantFixture()

        and: 'registered data converters'
        registerDefaultConverters()

        and: 'some service instances'
        SeqNumberService seqNumberService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(invoice) >> 'R-39999-10000'
        ConfigService configService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * configService.loadTenantAsMap() >> tenant
        MarkdownService markdownService = makeMarkdownService()

        and: 'an XML converter'
        def converter = new InvoicingTransactionXML()
        converter.seqNumberService = seqNumberService
        converter.configService = configService
        converter.markdownService = markdownService
        converter.start invoice, user

        when: 'XML is generated from this invoice'
        String xml = converter.toXML()

        then: 'valid XML is returned'
        matchValidXML xml
    }

    def 'Convert to XML using toString'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user'
        def user = makeUserFixture()

        and: 'tenant data'
        def tenant = makeTenantFixture()

        and: 'registered data converters'
        registerDefaultConverters()

        and: 'some service instances'
        SeqNumberService seqNumberService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(invoice) >> 'R-39999-10000'
        ConfigService configService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * configService.loadTenantAsMap() >> tenant
        MarkdownService markdownService = makeMarkdownService()

        and: 'an XML converter'
        def converter = new InvoicingTransactionXML()
        converter.seqNumberService = seqNumberService
        converter.configService = configService
        converter.markdownService = markdownService
        converter.start invoice, user

        when: 'XML is generated from this invoice'
        String xml = converter.toString()

        then: 'valid XML is returned'
        matchValidXML xml
    }

    def 'Convert to XML with writer'() {
        given: 'an invoice'
        def invoice = makeInvoiceFixture()

        and: 'a user'
        def user = makeUserFixture()

        and: 'tenant data'
        def tenant = makeTenantFixture()

        and: 'registered data converters'
        registerDefaultConverters()

        and: 'some service instances'
        SeqNumberService seqNumberService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * seqNumberService.getFullNumber(invoice) >> 'R-39999-10000'
        ConfigService configService = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * configService.loadTenantAsMap() >> tenant
        MarkdownService markdownService = makeMarkdownService()

        and: 'an XML converter'
        def converter = new InvoicingTransactionXML()
        converter.seqNumberService = seqNumberService
        converter.configService = configService
        converter.markdownService = markdownService
        converter.start invoice, user

        and: 'a writer'
        def out = new StringWriter()

        when: 'XML is generated from this invoice'
        converter.toXML(out)

        then: 'the XML is the same as in the writer'
        String xml = out.toString()
        converter.toString() == xml

        and: 'valid XML is returned'
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

            InputStream input = this.class.getResourceAsStream(
                "/public/print/dtd/${path}".toString()
            )
            new InputSource(input)
        } as EntityResolver
    }

    private Invoice makeInvoiceFixture() {
        def org = new Organization(
            number: 10000, recType: 1, name: 'AMC World Technologies GmbH',
            legalForm: 'GmbH', billingAddr: new Address(),
            shippingAddr: new Address(), phone: '987654321'
        )
        org.id = new ObjectId()

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
        invoice.id = new ObjectId()

        UserService userService = Mock()
        userService.getNumFractionDigitsExt() >> 2
        invoice.userService = userService

        invoice
    }

    private MarkdownService makeMarkdownService() {
        MarkdownService markdownService = Mock()
        markdownService.markdown(_) >> {
            //noinspection GroovyAssignabilityCheck
            String s = it[0].toString()
                .replace('&', '&amp;')
                .replace('<', '&lt;')

            "<p>${s}</p>".toString()
        }

        markdownService
    }

    private static Map<String, String> makeTenantFixture() {
        [
            name: 'MyOrganization Ltd.',
            street: '45, Park Ave.',
            postalCode: 'NY-39344',
            location: 'Santa Barbara',
            phone: '+1 21 20404044',
            email: 'info@myorganization.example',
            website: 'www.myorganization.example',
            bankName: 'YourBank',
            bankCode: '123456789',
            accountNumber: '987654321'
        ]
    }

    private static User makeUserFixture() {
        new User(
            username: 'jsmith', password: 'secret', firstName: 'John',
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
        def addr = entry.billingAddr
        assert '12, Leonardo Rd.' == addr.street.text()
        assert 'NY-39830' == addr.postalCode.text()
        assert 'Mt. Elber' == addr.location.text()
        assert 'NY' == addr.state.text()
        assert 'USA' == addr.country.text()
        assert 'my footer text... & more' == entry.footerText.text()
        assert 'my header text' == entry.headerText.text()
        assert '39999' == entry.number.text()
        assert null != entry.organization.@id.text()
        assert entry.organization.empty
        assert '' == entry.organization.text()
        assert 'Test invoice\nand more' == entry.subject.text()
    }

    private static void matchItems(GPathResult entry) {
        assert 3 == entry.invoicingItem.size()
        //noinspection GroovyAssignabilityCheck
        def item = entry.invoicingItem[0]
        assert 4.0 == item.quantity.toBigDecimal()
        assert 'pcs.' == item.unit.text()
        assert 'books' == item.name.text()
        assert 44.99 == item.unitPrice.toBigDecimal()
        assert 19.0 == item.tax.toBigDecimal()
    }

    private static void matchOrganization(GPathResult entry) {
        assert null != entry.@id.text()
        assert 'AMC World Technologies GmbH' == entry['name'].text()
        assert '10000' == entry.number.text()
    }

    private static void matchTaxRates(GPathResult entry) {
        assert 2 == entry.entry.size()
        assert '65.6849' == getEntryText(entry, '19.0')
        assert '0.96915' == getEntryText(entry, '7.0')
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
