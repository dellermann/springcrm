/*
 * InvoicingTransactionXML.groovy
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
import grails.converters.XML
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.transform.TypeCheckingMode
import org.amcworld.springcrm.*
import org.apache.commons.io.output.StringBuilderWriter
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


/**
 * The class {@code InvoicingTransactionXML} represents a converter from
 * invoicing transactions to XML.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
@CompileStatic
class InvoicingTransactionXML extends XML {

    //-- Constants ------------------------------

    /**
     * A table that is used to declare the XHTML entities used in converted
     * Markdown code.  The table maps public IDs to a file path relative to
     * {@code public/print/dtd}.
     */
    public static final Map<String, String> ENTITY_CATALOG = [
            '-//W3C//ENTITIES Latin 1 for XHTML//EN': 'xhtml-lat1.ent',
            '-//W3C//ENTITIES Special for XHTML//EN': 'xhtml-special.ent',
            '-//W3C//ENTITIES Symbols for XHTML//EN': 'xhtml-symbol.ent'
        ].asImmutable()


    //-- Class fields ---------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Fields ---------------------------------

    /**
     * The internal data structure which is transformed to XML.
     */
    private Map<String, Object> data

    /**
     * The service used to convert Markdown text to HTML.
     */
    private MarkdownService markdownService

    /**
     * The service used to obtain sequence numbers.
     */
    private SeqNumberService seqNumberService


    //-- Properties -----------------------------

    /**
     * Gets a copy of the internal data structure.
     *
     * @return  the data
     */
    Map<String, Object> getData() {
        new HashMap<String, Object>(data)
    }

    /**
     * Checks whether or not a duplicate document should be created.
     *
     * @return  if {@code true} a duplicate document should be created;
     *          {@code false} otherwise
     */
    boolean isDuplicate() {
        !!data.watermark
    }

    /**
     * Sets whether or not a duplicate document should be created.
     *
     * @param duplicate whether or not the data structure of a duplicate
     *                  document should be created
     */
    void setDuplicate(boolean duplicate) {
        data.watermark = duplicate ? 'duplicate' : ''
    }


    //-- Public methods -------------------------

    /**
     * Adds additional data to the internal data structure.  All entries in the
     * map overwrite possible existing entries in the internal data structure.
     *
     * @param data  the data to add
     */
    void add(Map<String, Object> data) {
        this.data << data
    }

    /**
     * Adds additional data to the internal data structure.  All entries in the
     * map overwrite possible existing entries in the internal data structure.
     *
     * @param data  the data to add
     * @return      this object
     */
    InvoicingTransactionXML leftShift(Map<String, Object> data) {
        this.data << data

        this
    }

    @Override
    String toString() {
        toXML()
    }

    /**
     * Converts this invoicing transaction to XML.
     *
     * @return the converted XML
     */
    String toXML() {
        InvoicingTransaction transaction =
            (InvoicingTransaction) data.transaction
        if (log.debugEnabled) {
            log.debug "Data structure, type ${transaction.type}: ${data}"
        }

        /* generate XML from data structure */
        def xml = new XML(data)
        def w = new StringBuilderWriter()
        xml.render w

        /* inject DOCTYPE after XML prologue */
        StringBuilder buf = w.builder
        writeDocType buf, xml.getElementName(data)

        /* inject converted Markdown HTML before root end tag */
        writeAdditionalXML buf, transaction

        String res = buf.toString()
        if (log.debugEnabled) {
            log.debug "XML data structure, type ${transaction.type}: ${res}"
        }

        res
    }

    /**
     * Converts this invoicing transaction to XML and writes it to the given
     * writer.
     *
     * @param out   the given writer
     */
    void toXML(Writer out) {
        out << toXML()
    }


    //-- Non-public methods ---------------------

    /**
     * Collects all necessary data about the given invoicing transaction, the
     * client, and the currently logged in user in a map.
     *
     * @param transaction   the given invoicing transaction
     * @param user          the currently logged in user
     * @return              the collected data
     */
    @PackageScope
    void collectData(InvoicingTransaction transaction, User user) {
        data = [
            transaction: transaction,
            items: transaction.items,
            organization: transaction.organization,
            person: transaction.person,
            user: new User(user),
            fullNumber: seqNumberService.getFullNumber(transaction),
            taxRates: transaction.taxRateSums,
            values: [
                subtotalNet: transaction.subtotalNet,
                subtotalGross: transaction.subtotalGross,
                discountPercentAmount: transaction.discountPercentAmount,
                total: transaction.total
            ],
            watermark: '',
            client: Client.loadAsMap()
        ]
    }

    /**
     * Writes the description texts of the given invoicing items to the given
     * string builder after converting them from Markdown to HTML.
     *
     * @param buf   the given string builder
     * @param items the given invoicing items that should be processed
     */
    private void itemsXML(StringBuilder buf, List<InvoicingItem> items) {
        buf << '<itemsHtml>'
        for (InvoicingItem item : items) {
            buf << '<descriptionHtml id="' << item.id << '">'
            buf << markdownHTML(item.description)
            buf << '</descriptionHtml>'
        }
        buf << '</itemsHtml>'
    }

    /**
     * Generates a HTML block from the given Markdown text suitable for usage
     * in XML.  The HTML block declares the XHTML namespace and decodes all
     * entities.
     *
     * @param text  the given Markdown text; if {@code null} an empty string
     *              is used
     * @return      the generated XML block
     */
    private String markdownHTML(String text) {
        String html = markdownService.markdown(text ?: '')

        String s = "<html xmlns='http://www.w3.org/1999/xhtml'><head/><body>${html}</body></html>"
        println s
        s
    }

    /**
     * Writes an XML element with converted HTML for the given bean property
     * that contains Markdown content to the given string builder.  The
     * Markdown content is converted to HTML before writing.
     *
     * @param buf       the given string builder
     * @param bean      the given bean
     * @param propName  the name of the property
     * @param elemName  the name of the XML node that should be created; for a
     *                  given name {@code name} the XML node {@code nameHtml}
     *                  is created
     */
    private void markdownXML(StringBuilder buf, def bean, String propName,
                             String elemName = propName)
    {
        writeHTML buf, elemName, markdownHTML(getBeanProperty(bean, propName))
    }

    /**
     * Writes an XML element with converted HTML for the given bean property
     * that contains text content to the given string builder.  Any line
     * terminators are converted to {@code <br />} elements before writing.
     *
     * @param buf       the given string builder
     * @param bean      the given bean
     * @param propName  the name of the property
     * @param elemName  the name of the XML node that should be created; for a
     *                  given name {@code name} the XML node {@code nameHtml}
     *                  is created
     */
    private static void nl2BrXML(StringBuilder buf, def bean, String propName,
                                 String elemName = propName)
    {
        String br = '<br xmlns="http://www.w3.org/1999/xhtml" />'

        String s = getBeanProperty(bean, propName)
        s = s.replaceAll(~/&/, '&amp;').replaceAll(~/</, '&lt;')
        s = s.replaceAll(~/\r\n/, br).replaceAll(~/[\r\n]/, br)

        writeHTML buf, elemName, s
    }

    /**
     * Gets the value of the property with the given name from the bean.
     *
     * @param bean      the given bean
     * @param propName  the name of the property
     * @return          the value of the property; {@code null} if the property
     *                  is unset or has no value
     * @since           1.2
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    private static String getBeanProperty(def bean, String propName) {
        bean."${propName}"?.toString()
    }

    /**
     * Writes additional XML elements to the given string builder.
     *
     * @param buf           the given string builder
     * @param transaction   the given invoicing transaction
     */
    private void writeAdditionalXML(StringBuilder buf,
                                      InvoicingTransaction transaction)
    {
        int pos = buf.lastIndexOf('</')
        if (pos < 0) {
            log.fatal "Cannot find root end tag:\n${buf}"
            throw new IllegalStateException('Cannot find root end tag.')
        }

        StringBuilder sb = new StringBuilder()

        markdownXML sb, transaction.billingAddr, 'street', 'billingAddrStreet'
        markdownXML sb, transaction.shippingAddr, 'street', 'shippingAddrStreet'
        nl2BrXML sb, transaction, 'subject'
        markdownXML sb, transaction, 'headerText'
        markdownXML sb, transaction, 'footerText'
        itemsXML sb, transaction.items

        buf.insert pos, sb
    }

    /**
     * Writes the {@code DOCTYPE} declaration behind the XML prolog to the
     * given string builder.
     *
     * @param buf       the given string builder
     * @param elemName  the name of the root element
     */
    private static void writeDocType(StringBuilder buf, String elemName) {
        int pos = buf.indexOf('?>')
        if (pos < 0) {
            log.fatal "Cannot find XML prologue:\n${buf}"
            throw new IllegalStateException('Cannot find XML prologue.')
        }

        StringBuilder sb = new StringBuilder('<!DOCTYPE ')
        sb << elemName << ' ['

        int i = 0
        for (Map.Entry<String, String> entry : ENTITY_CATALOG) {
            sb << '<!ENTITY % entity-' << i << ' PUBLIC "' << entry.key
            sb << '" "http://www.w3.org/TR/xhtml1/DTD/' << entry.value << '">'
            sb << '%entity-' << i << ';'
            i++
        }

        sb << ']>'

        buf.insert pos + 2, sb
    }

    /**
     * Writes the given HTML content as XML node to the given string builder.
     *
     * @param buf       the given string builder
     * @param elemName  the name of the XML node that should be created; for a
     *                  given name {@code name} the XML node {@code nameHtml}
     *                  is created
     * @param content   any valid HTML content that should be written
     */
    private static void writeHTML(StringBuilder buf, String elemName,
                                  String content)
    {
        buf << '<' << elemName << 'Html>'
        buf << content
        buf << '</' << elemName << 'Html>'
    }
}
