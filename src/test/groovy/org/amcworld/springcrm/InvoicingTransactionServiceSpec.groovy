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

import grails.test.mixin.TestFor
import org.amcworld.springcrm.xml.InvoicingTransactionXML
import org.amcworld.springcrm.xml.InvoicingTransactionXMLFactory
import spock.lang.Specification


@TestFor(InvoicingTransactionService)
class InvoicingTransactionServiceSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Generate XML for an invoice'() {
        given: 'an invoice'
        def invoice = new Invoice()

        and: 'a user in the session'
        def user = new User()

        and: 'some example XML'
        String xml = '<? example XML ?>'

        and: 'a mocked XML converter'
        InvoicingTransactionXML conv = Mock()
        1 * conv.setDuplicate(false)
        1 * conv.toString() >> xml

        and: 'a mocked XML converter factory'
        InvoicingTransactionXMLFactory invoicingTransactionXMLFactory = Mock()
        1 * invoicingTransactionXMLFactory.newConverter(invoice, user) >> conv
        service.invoicingTransactionXMLFactory = invoicingTransactionXMLFactory

        when: 'I generate XML from this invoice'
        String res = service.generateXML(invoice, user)

        then: 'I get valid XML'
        xml == res
    }

    // TODO test the other methods
}
