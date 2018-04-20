/*
 * InvoicingTransactionXMLFactory.groovy
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
import groovy.transform.CompileStatic
import org.amcworld.springcrm.ConfigService
import org.amcworld.springcrm.SeqNumberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * The class {@code InvoicingTransactionXMLFactory} represents a factory to
 * create XML converters for invoicing transactions.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.4
 */
@Component
@CompileStatic
class InvoicingTransactionXMLFactory {

    //-- Fields ---------------------------------

    @Autowired
    ConfigService configService

    @Autowired
    MarkdownService markdownService

    @Autowired
    SeqNumberService seqNumberService


    //-- Public methods -------------------------

    /**
     * Creates a new XML converter for the given invoicing transaction.
     *
     * @param transaction   the given invoicing transaction
     * @param user          the currently logged in user
     * @return              the XML converter
     */
    InvoicingTransactionXML newConverter() {
        InvoicingTransactionXML inst = new InvoicingTransactionXML()
        inst.configService = configService
        inst.markdownService = markdownService
        inst.seqNumberService = seqNumberService

        inst
    }
}
