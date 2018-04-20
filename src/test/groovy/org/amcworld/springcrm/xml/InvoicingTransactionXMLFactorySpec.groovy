/*
 * InvoicingTransactionXMLFactorySpec.groovy
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
import org.amcworld.springcrm.ConfigService
import org.amcworld.springcrm.Invoice
import org.amcworld.springcrm.SeqNumberService
import org.amcworld.springcrm.User
import spock.lang.Specification


class InvoicingTransactionXMLFactorySpec extends Specification {

    //-- Feature methods ------------------------

    void 'Create a new converter'() {
        given: 'some service instances'
        ConfigService configService = Mock()
        MarkdownService markdownService = Mock()
        SeqNumberService seqNumberService = Mock()

        and: 'a converter factory'
        def factory = new InvoicingTransactionXMLFactory()
        factory.configService = configService
        factory.markdownService = markdownService
        factory.seqNumberService = seqNumberService

        when: 'a new converter is created'
        InvoicingTransactionXML converter = factory.newConverter()

        then: 'a valid converter is returned'
        null != converter
        configService == converter.configService
        markdownService == converter.markdownService
        seqNumberService == converter.seqNumberService
    }
}
