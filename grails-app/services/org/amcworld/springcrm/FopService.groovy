/*
 * FopService.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import grails.core.GrailsApplication
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import javax.servlet.http.HttpServletResponse
import javax.xml.transform.*
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamSource
import org.amcworld.springcrm.xml.TemplateURIResolver
import org.apache.avalon.framework.configuration.Configuration
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder
import org.apache.fop.apps.FOUserAgent
import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.xml.sax.InputSource
import org.xml.sax.XMLReader


/**
 * The class {@code FopService} represents a service which generates PDF
 * documents using XSL-FO.  The service supports multiple templates for a
 * particular type of document, either in a system directory or in a user
 * directory.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@CompileStatic
class FopService {

    //-- Constants ------------------------------

    /**
     * The folder in the web application containing the system-wide print
     * configuration and template files.
     */
    public static final String SYSTEM_FOLDER = 'classpath:public/print'


    //-- Class fields ---------------------------

    static transactional = false


    //-- Fields ---------------------------------

    FopFactory fopFactory
    GrailsApplication grailsApplication
    MessageSource messageSource
    TransformerFactory transformerFactory
    XMLReader xmlReader


    //-- Public methods -------------------------

    /**
     * Generates a PDF document from the given XML, template and document type
     * and writes the data to the given output stream.  The template must be a
     * XSL-FO template which is processed by FOP.
     *
     * @param data      a reader containing the XML data to process by the
     *                  template
     * @param type      the type of document to process; this value determines
     *                  the name of the template file
     * @param template  the key of the template as obtained by
     *                  {@code getTemplatePaths} or {@code getTemplateNames}
     * @param out       the output stream to write the generated PDF data to
     */
    void generatePdf(Reader data, String type, String template,
                     OutputStream out)
    {
        Map<String, String> paths = templatePaths
        String templateDir = paths[template]
        URIResolver uriResolver =
            new TemplateURIResolver(grailsApplication.mainContext, templateDir)

        /*
         * Implementation notes: this sets the URI resolver for XSLT.  The bean
         * is injected by Spring and defined in
         * "grails-app/conf/spring/resources.groovy".  The bean must not be
         * singleton because each instance is modified by setting the URI
         * resolver.
         *
         * If there are URLs in the XML (stored in "data") are to be resolved
         * you must set a suitable URI resolver at "transformer" using
         * transformer.URIResolver = …
         */
        transformerFactory.URIResolver = uriResolver

        /*
         * Implementation notes: normally, the FopFactory instance should be
         * re-used.  In our configuration we have to configure that instance
         * depending on the template directory.  Thus, we cannot re-use it
         * because each thread may have its own template and thus, template
         * directory.
         */
        fopFactory.URIResolver = uriResolver
        fopFactory.userConfig = getFopConfiguration(templateDir)
        FOUserAgent ua = getUserAgent(fopFactory)
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, ua, out)

        try {
            InputStream is = getTemplateFile("${templateDir}/${type}-fo.xsl")
            Transformer transformer =
                transformerFactory.newTransformer(new StreamSource(is))
            transformer.setParameter('versionParam', '2.0')

            Source src = new SAXSource(xmlReader, new InputSource(data))
            Result res = new SAXResult(fop.defaultHandler)
            transformer.transform src, res
        } catch (TransformerException e) {
            log.error "XSL-FO transformer error: ${e.messageAndLocation}"
            throw e
        }
    }

    /**
     * Gets all the display names for all print templates.
     *
     * @return  a map containing the directory name as key and the display
     *          name as value
     */
    Map<String, String> getTemplateNames() {
        Map<String, String> paths = templatePaths
        Map<String, String> res = [: ]
        for (Map.Entry<String, String> entry : paths) {
            Properties props = new Properties()
            try {
                InputStream is = getTemplateFile(
                    "${entry.value}/meta.properties"
                )
                if (is != null) props.load is
            } catch (IOException ignored) { /* ignored */ }
            String name = props.getProperty('name')
            if (!name) {
                name = messageSource.getMessage(
                    "print.template.${entry.key}".toString(), null,
                    entry.key, LCH.locale
                )
            }
            res[entry.key] = name
        }

        res
    }

    /**
     * Gets all the paths containing print templates.  Any template
     * directories defined in the user template directory overwrites the
     * directory of the same name in the system template directory.
     *
     * @return  a map containing the directory names as key and the absolute
     *          paths to the template directory as value
     */
    Map<String, String> getTemplatePaths() {
        Map<String, String> res = ['default': SYSTEM_FOLDER + '/default']

        File dir = userTemplateDir
        if (dir?.exists()) {
            dir.eachDir { res[it.name] = 'file://' + it.absolutePath }
        }

        res
    }

    /**
     * Gets the directory containing the print templates defined by the user.
     *
     * @return  the user print template directory; {@code null} if no user
     *          print template directory is defined
     */
    @CompileDynamic
    File getUserTemplateDir() {
        String dir = grailsApplication.config.springcrm?.dir?.print?.toString()

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
     *                  {@code getTemplatePaths} or {@code getTemplateNames}
     * @param response  the response object to write the PDF data to
     * @param fileName  the file name to use when writing to the output
     */
    void outputPdf(String xml, String type, String template,
                   HttpServletResponse response, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        generatePdf new StringReader(xml), type, template ?: 'default', baos
        byte [] buf = baos.toByteArray()

        response.contentType = 'application/pdf'
        response.addHeader 'Content-Disposition',
            "attachment; filename=\"${fileName}\""
        response.contentLength = buf.length
        response.outputStream.write buf
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
    private Configuration getFopConfiguration(String templateDir) {
        InputStream is = getTemplateFile("${templateDir}/fop-conf.xml")
        if (!is) {
            is = getTemplateFile(SYSTEM_FOLDER + '/default/fop-conf.xml')
        }

        new DefaultConfigurationBuilder().build(is)
    }

    /**
     * Gets the instance to load resources.
     *
     * @return  the resource loader
     * @since   2.1
     */
    private ResourceLoader getResourceLoader() {
        grailsApplication.mainContext
    }

    /**
     * Gets the input stream to the file with the given path.
     *
     * @param path  the path to the file to load
     * @return      the input stream of the file; {@code null} if no such file
     *              exists
     */
    private InputStream getTemplateFile(String path) {
        Resource res = resourceLoader.getResource(path)

        res.exists() ? res.inputStream : null
    }

    /**
     * Creates a new FOP user agent instance and fills it with particular data.
     *
     * @param fopFactory    the FOP factory used to create the user agent
     * @return              the user agent
     * @since               1.4
     */
    private FOUserAgent getUserAgent(FopFactory fopFactory) {
        FOUserAgent ua = fopFactory.newFOUserAgent()
        StringBuilder buf =
            new StringBuilder(grailsApplication.metadata.getApplicationName())
        buf << ' v' << grailsApplication.metadata.getApplicationVersion()
        ua.producer = buf.toString()
        ua.creationDate = new Date()

        ua
    }
}
