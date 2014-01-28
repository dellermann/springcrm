/*
 * FopService.groovy
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

import javax.servlet.ServletContext
import javax.servlet.http.HttpServletResponse
import javax.xml.transform.*
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamSource
import org.amcworld.springcrm.util.TemplateURIResolver
import org.apache.avalon.framework.configuration.Configuration
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder
import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.XMLReaderFactory


/**
 * The class {@code FopService} represents a service which generates PDF
 * documents using XSL-FO.  The service supports multiple templates for a
 * particular type of document, either in a system directory or in a user
 * directory.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 */
class FopService {

    //-- Constants ------------------------------

    /**
     * The folder in the web application containing the system-wide print
     * configuration and template files.
     */
    protected static final String SYSTEM_FOLDER = '/WEB-INF/data/print'


    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    GrailsApplication grailsApplication
    MessageSource messageSource
    ServletContext servletContext = SCH.servletContext


    //-- Public methods -------------------------

    /**
     * Generates a PDF document from the given template and document type and
     * writes the data to the given output stream.  The template must be a
     * XSL-FO template which is processed by FOP.
     *
     * @param data      a reader containing the XML data to process by the
     *                  template
     * @param type      the type of document to process; this value determines
     *                  the name of the template file
     * @param template  the key of the template as obtained by
     *                  {@code getTemplatePathes()} or {@code getTemplateNames}
     * @param out       the output stream to write the generated PDF data to
     */
    void generatePdf(Reader data, String type, String template,
                     OutputStream out) {
        Map<String, String> pathes = templatePathes
        String templateDir = pathes[template]

        URIResolver uriResolver = new TemplateURIResolver(
            servletContext, templateDir
        )
        FopFactory fopFactory = FopFactory.newInstance()
        fopFactory.URIResolver = uriResolver
        fopFactory.userConfig = getFopConfiguration(templateDir)

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out)

        TransformerFactory tfFactory = TransformerFactory.newInstance()
        tfFactory.URIResolver = uriResolver
        tfFactory.errorListener = [
            error: { e ->
                log.error "XSL-FO error: ${e.messageAndLocation}"
            },
            fatalError: { e ->
                log.fatal "XSL-FO fatal error: ${e.messageAndLocation}"
            },
            warning: { e ->
                log.warn "XSL-FO warning: ${e.messageAndLocation}"
            }
        ] as ErrorListener

        try {
            InputStream is = getTemplateFile("${templateDir}/${type}-fo.xsl")
            Transformer transformer =
                tfFactory.newTransformer(new StreamSource(is))
            transformer.URIResolver = uriResolver
            transformer.setParameter('versionParam', '2.0')

            def xmlReader = XMLReaderFactory.createXMLReader()
            xmlReader.entityResolver = new XHTMLEntityResolver(servletContext)

            Source src = new SAXSource(xmlReader, new InputSource(data))
            Result res = new SAXResult(fop.getDefaultHandler())
            transformer.transform src, res
        } catch (TransformerException e) {
            log.error "XSL-FO transformer error: ${e.messageAndLocation}"
            throw e
        }
    }

    /**
     * Gets all the display names for all print templates.
     *
     * @return  a map containing the directory names as key and the display
     *          name as value
     */
    Map<String, String> getTemplateNames() {
        Map<String, String> pathes = templatePathes
        Map<String, String> res = [: ]
        for (Map.Entry entry in pathes) {
            Properties props = new Properties()
            try {
                InputStream is = getTemplateFile(
                    "${entry.value}/meta.properties"
                )
                if (is) props.load(is)
            } catch (IOException ignored) { /* ignored */ }
            String name = props.getProperty('name')
            if (!name) {
                name = messageSource.getMessage(
                    "print.template.${entry.key}", null, entry.key, LCH.locale
                )
            }
            res[entry.key] = name
        }
        res
    }

    /**
     * Gets all the pathes containing print templates.  Any template
     * directories defined in the user template directory overwrites the
     * directory of the same name in the system template directory.
     *
     * @return  a map containing the directory names as key and the absolute
     *          paths to the template directory as value
     */
    Map<String, String> getTemplatePathes() {
        Map<String, String> res = [: ]
        servletContext.getResourcePaths(SYSTEM_FOLDER).each {
            def m = (it =~ /^.*\/([-\w]+)\/$/)
            String id = m[0][1]
            if (m.matches() && id != 'dtd') {
                res[id] = "SYSTEM:${it}"
            }
        }
        File dir = userTemplateDir
        if (dir?.exists()) {
            dir.eachDir { res[it.name] = it.absolutePath }
        }
        res
    }

    /**
     * Gets the directory containing the print templates defined by the user.
     *
     * @return  the user print template directory; {@code null} if no user
     *          print template directory is defined
     */
    File getUserTemplateDir() {
        def dir = grailsApplication.config.springcrm.dir.print
        dir ? new File(dir) : null
    }

    /**
     * Generates a PDF document from the given template and document type and
     * writes the data to the response with the given file name.  The template
     * must be a XSL-FO template which is processed by FOP.
     *
     * @param data      a reader containing the XML data to process by the
     *                  template
     * @param type      the type of document to process; this value determines
     *                  the name of the template file
     * @param template  the key of the template as obtained by
     *                  {@code getTemplatePathes()} or {@code getTemplateNames}
     * @param response  the response object to write the PDF data to
     * @param fileName  the file name to use when writing to the output
     */
    void outputPdf(String xml, String type, String template,
                   HttpServletResponse response, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        generatePdf new StringReader(xml), type, template ?: 'default', baos

        response.contentType = 'application/pdf'
        response.addHeader 'Content-Disposition',
            "attachment; filename=\"${fileName}\""
        response.contentLength = baos.size()
        response.outputStream.write(baos.toByteArray())
        response.outputStream.flush()
    }


    //-- Non-public methods ---------------------

    /**
     * Retrieves the FOP configuration data from a file either in the given
     * template directory or the system template directory.
     *
     * @param templateDir   the directory containing the configuration data of
     *                      the template
     * @return              the FOP configuration data
     */
    protected Configuration getFopConfiguration(String templateDir) {
        InputStream is = getTemplateFile("${templateDir}/fop-conf.xml")
        if (!is) {
            is = getTemplateFile(
                "SYSTEM:${SYSTEM_FOLDER}/default/fop-conf.xml"
            )
        }
        new DefaultConfigurationBuilder().build(is)
    }

    /**
     * Gets the input stream to the file with the given path.  The method
     * handles files from the system folder which must be prefixed by
     * {@code SYSTEM:}.
     *
     * @param path  the path to the file to load
     * @return      the input stream of the file; {@code null} if no such file
     *              exists
     */
    protected InputStream getTemplateFile(String path) {
        if (path.startsWith('SYSTEM:')) {
            return servletContext.getResourceAsStream(path.substring(7))
        }

        new File(path).newInputStream()
    }
}


class XHTMLEntityResolver implements EntityResolver {

    //-- Instance variables ---------------------

    ServletContext servletContext


    //-- Constructors ---------------------------

    XHTMLEntityResolver(ServletContext servletContext) {
        this.servletContext = servletContext
    }


    //-- Public methods -------------------------

    @Override
    InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException
    {
        String path = InvoicingTransactionXML.ENTITY_CATALOG[publicId]
        if (!path) {
            return null
        }

        InputStream input = servletContext.getResourceAsStream(
            "${FopService.SYSTEM_FOLDER}/dtd/${path}"
        )
        new InputSource(input)
    }
}
