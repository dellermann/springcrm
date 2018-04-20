/*
 * InvoicingTransactionServiceSpec.groovy
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

import grails.testing.services.ServiceUnitTest
import org.amcworld.springcrm.xml.InvoicingTransactionXML
import org.amcworld.springcrm.xml.InvoicingTransactionXMLFactory
import spock.lang.Specification


class InvoicingTransactionServiceSpec extends Specification
    implements ServiceUnitTest<InvoicingTransactionService>
{

    //-- Feature methods ------------------------

    void 'Generate XML for an invoice'() {
        given: 'an invoice'
        def invoice = new Invoice()

        and: 'a user in the session'
        def user = new User()

        and: 'some example XML'
        String xml = '<? example XML ?>'

        and: 'a mocked XML converter'
        InvoicingTransactionXML converter = Mock()
        1 * converter.setDuplicate(false)
        //noinspection GroovyAssignabilityCheck
        1 * converter.toString() >> xml

        and: 'a mocked XML converter factory'
        InvoicingTransactionXMLFactory factory = Mock()
        //noinspection GroovyAssignabilityCheck
        1 * factory.newConverter() >> converter
        service.invoicingTransactionXMLFactory = factory

        when: 'XML is generated from this invoice'
        String res = service.generateXML(invoice, user)

        then: 'the converter has been populated'
        1 * converter.start(invoice, user)

        and: 'valid XML is returned'
        xml == res
    }

    // TODO test the other methods
}
