/*
 * InvoicingTransactionXMLBase.groovy
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

import com.naleid.grails.MarkdownService
import groovy.util.slurpersupport.GPathResult
import org.codehaus.groovy.grails.web.converters.configuration.ConvertersConfigurationInitializer
import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import spock.lang.Specification


/**
 * The class {@code InvoicingTransactionXMLBase} represents a base class for
 * all Spock specifications that test the generation of XML from invoicing
 * transactions.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class InvoicingTransactionXMLBase extends Specification {

    //-- Instance variables ---------------------

    XmlSlurper slurper


    //-- Fixture methods ------------------------

    def setup() {
        registerDefaultConverters()
        initXmlSlurper()
    }


    //-- Non-public methods ---------------------

    protected GPathResult getEntry(GPathResult parent, String name) {
        parent.entry.find { it.@key == name }
    }

    protected String getEntryText(GPathResult parent, String name) {
        getEntry(parent, name)?.text()
    }

    protected InvoicingTransactionXMLFactory initInvoicingTransactionXMLFactory()
    {
        def markdownService = new MarkdownService()
        markdownService.grailsApplication = grailsApplication

        new InvoicingTransactionXMLFactory(markdownService: markdownService)
    }

    protected void initXmlSlurper() {
        slurper = new XmlSlurper(false, true, true)
        slurper.entityResolver = { String publicId, String systemId ->
            String path = InvoicingTransactionXML.ENTITY_CATALOG[publicId]
            if (!path) {
                return null
            }

            File f = new File("web-app${FopService.SYSTEM_FOLDER}/dtd", path)
            InputStream input = f.newInputStream()
            new InputSource(input)
        } as EntityResolver
    }

    protected void makeClientFixture() {
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

    protected Invoice makeInvoiceFixture(Organization org = null,
                                         Person p = null)
    {
        makeInvoiceStageFixture()
        if (!org) {
            makeOrganizationFixture()
            org = Organization.get(1)
        }
        if (!p) {
            makePersonFixture()
            p = Person.get(1)
        }

        mockDomain Invoice, [
            [
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
                items: [
                    new InvoicingItem(
                        number: 'P-10000', quantity: 4, unit: 'pcs.',
                        name: 'books', unitPrice: 44.99, tax: 19
                    ),
                    new InvoicingItem(
                        number: 'P-20000', quantity: 10.5, unit: 'm',
                        name: 'tape', unitPrice: 0.89, tax: 7
                    ),
                    new InvoicingItem(
                        number: 'S-10100', quantity: 4.25, unit: 'h',
                        name: 'repairing', unitPrice: 39, tax: 19
                    )
                ],
                notes: 'my notes',
                number: 39999,
                organization: org,
                person: p,
                shippingAddr: new Address(),
                shippingCosts: 4.5,
                shippingTax: 7,
                stage: InvoiceStage.get(903),
                subject: 'Test invoice\nand more',
                termsAndConditions: [
                    new TermsAndConditions(id: 700, name: 'wares'),
                    new TermsAndConditions(id: 701, name: 'services')
                ],
                total: 414.632541
            ]
        ]

        def invoice = Invoice.first()
        invoice.seqNumberService = Mock(SeqNumberService)
        invoice.seqNumberService.formatWithPrefix(_, _) >> 'R-39999'
        invoice
    }

    protected void makeInvoiceStageFixture() {
        def names = ['sent', 'paid', 'reminded', 'collection']
        for (int i = 0; i < names.size(); i++) {
            def is = new InvoiceStage(name: names.get(i))
            is.id = 902 + i     // setting the ID in constructor doesn't work
            is.save failOnError: true
        }
    }

    protected void makeOrganizationFixture() {
        mockDomain Organization, [
            [
                id: 1, number: 10000, recType: 1,
                name: 'AMC World Technologies GmbH', legalForm: 'GmbH',
                billingAddr: new Address(), shippingAddr: new Address(),
                phone: '987654321'
            ]
        ]
    }

    protected void makePersonFixture(Organization org) {
        mockDomain Person, [
            [
                id: 1, number: 10000, organization: org, firstName: 'Daniel',
                lastName: 'Ellermann', mailingAddr: new Address(),
                otherAddr: new Address(), phone: '123456789'
            ]
        ]
    }

    protected User makeUserFixture() {
        new User(
            userName: 'jsmith', password: 'secret', firstName: 'John',
            lastName: 'Smith', email: 'j.smith@example.com',
            phone: '+49 1 8984466', mobile: '+49 1 464163464'
        )
    }

    protected void matchClient(entry) {
        assert 'MyOrganization Ltd.' == getEntryText(entry, 'name')
        assert '45, Park Ave.' == getEntryText(entry, 'street')
        assert 'NY-39344' == getEntryText(entry, 'postalCode')
        assert 'Santa Barbara' == getEntryText(entry, 'location')
        assert '+1 21 20404044' == getEntryText(entry, 'phone')
        assert 'info@myorganization.example' == getEntryText(entry, 'email')
        assert 'www.myorganization.example' == getEntryText(entry, 'website')
        assert 'YourBank' == getEntryText(entry, 'bankName')
        assert '123456789' == getEntryText(entry, 'bankCode')
        assert '987654321' == getEntryText(entry, 'accountNumber')
    }

    protected void matchHtmlValues(entry) {
        assert '12, Leonardo Rd.' == entry.billingAddrStreetHtml.'html:html'.'html:body'.'html:p'.text()
        assert !entry.shippingAddrStreetHtml.'html:html'.'html:body'.text()
        def el = entry.subjectHtml
        assert 'Test invoiceand more' == el.text()
        assert !el.'html:br'.isEmpty()
        // XXX currently we cannot test where <br /> is within the string; it's not implemented by Groovy (see http://jira.codehaus.org/browse/GROOVY-2115)
        assert 'my header text' == entry.headerTextHtml.'html:html'.'html:body'.text()
        assert 'my footer text… & more' == entry.footerTextHtml.'html:html'.'html:body'.text()
        el = entry.itemsHtml.descriptionHtml
        assert 3 == el.size()
    }

    protected void matchInvoicingTransaction(entry) {
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

    protected void matchItems(entry) {
        assert 3 == entry.invoicingItem.size()
        def item = entry.invoicingItem[0]
        assert 'P-10000' == item.number.text()
        assert '4.0' == item.quantity.text()
        assert 'pcs.' == item.unit.text()
        assert 'books' == item.name.text()
        assert '44.99' == item.unitPrice.text()
        assert '19.0' == item.tax.text()
    }

    protected void matchOrganization(entry) {
        assert '1' == entry.@id.text()
        assert 'AMC World Technologies GmbH' == entry.name.text()
        assert '10000' == entry.number.text()
    }

    protected void matchTaxRates(entry) {
        assert 2 == entry.entry.size()
        assert '65.6849' == getEntryText(entry, '19.0')
        assert '0.96915' == getEntryText(entry, '7.0')
    }

    protected void matchUser(entry) {
        assert 'John' == entry.firstName.text()
        assert 'Smith' == entry.lastName.text()
        assert '+49 1 8984466' == entry.phone.text()
        assert '+49 1 464163464' == entry.mobile.text()
        assert 'j.smith@example.com' == entry.email.text()
        assert entry.password.empty
        assert !entry.password.text()
    }

    protected void matchValidXML(String xml) {
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

    protected void matchValues(entry) {
        assert '359.555' == getEntryText(entry, 'subtotalNet')
        assert '426.20905' == getEntryText(entry, 'subtotalGross')
        assert '8.524181' == getEntryText(entry, 'discountPercentAmount')
        assert '413.224869' == getEntryText(entry, 'total')
    }

    protected void registerDefaultConverters() {
        def convertersInit = new ConvertersConfigurationInitializer()
        convertersInit.initialize(grailsApplication)
    }
}
