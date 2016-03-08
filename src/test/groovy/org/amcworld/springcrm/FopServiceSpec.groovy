/*
 * FopServiceSpec.groovy
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
import grails.test.mixin.TestFor
import javax.servlet.ServletContext


@TestFor(FopService)
@Mock([
    Config, FopService, Invoice, InvoiceStage, InvoicingItem, Organization,
    Person, TermsAndConditions, User
])
class FopServiceSpec extends InvoicingTransactionXMLBase {

    def setup() {
        def control = mockFor(ServletContext)
        control.demand.getResourcePaths(1) { String path ->
            def f = new File(System.getProperty('user.dir'), "web-app/${path}")
            def l = f.list()
            def res = new HashSet<String>(l.length)
            l.each { res << "${f}/${it}/" }
            res
        }
        control.demand.getResourceAsStream(2) { String path ->
            try {
                new File(path).newInputStream()
            } catch (IOException e) {
                null
            }
        }
        service.servletContext = control.createMock()
    }


    //-- Feature methods ------------------------

    def 'Get template names'() {
        when: 'I call the method'
        def names = service.templateNames

        then: 'I get at least the default template'
        1 <= names.size()
        'default' == names['default']
        !('dtd' in names)
    }

    def 'Get template paths'() {
        when: 'I call the method'
        def paths = service.templatePaths

        then: 'I get at least the path of the default template'
        1 <= paths.size()
        String p = paths['default']
        null != p
        p.startsWith 'SYSTEM:'
        p.endsWith '/default/'
        !('dtd' in paths)
    }

    def 'Get user template directory'() {
        given: 'the name of this application'
        String appName = grailsApplication.metadata['app.name']

        when: 'I call the method'
        File f = service.userTemplateDir

        then: 'I get a valid path to the user template directory'
        null != f
        f.absolutePath.endsWith "/.${appName}/print"
    }

//    def 'Generate PDF of an invoice'() {
//        given: 'an invoicing transaction XML converter factory'
//        InvoicingTransactionXMLFactory invoicingTransactionXMLFactory =
//            initInvoicingTransactionXMLFactory()
//
//        and: 'an invoice'
//        def invoice = makeInvoiceFixture()
//
//        and: 'some client data'
//        makeClientFixture()
//
//        and: 'a user in the session'
//        def user = makeUserFixture()
//
//        and: 'XML created from this invoice'
//        def conv = invoicingTransactionXMLFactory.createConverter(invoice, user)
//        String xml = conv.toXML()
//
//        when: 'I generate PDF from this XML'
//        ByteArrayOutputStream baos = new ByteArrayOutputStream()
//        service.generatePdf new StringReader(xml), 'invoice', 'default', baos
//
//        then: 'I get valid PDF data'
//        println baos.toString()
//        0 > baos.size()
//    }

    // TODO write test for the other methods
}
