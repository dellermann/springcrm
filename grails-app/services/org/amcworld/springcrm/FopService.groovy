/*
 * FopService.groovy
 *
 * Copyright (c) 2011-2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


package org.amcworld.springcrm

import java.io.File
import java.io.OutputStream
import java.io.Reader
import java.util.Map
import javax.servlet.ServletContext
import javax.servlet.http.HttpServletResponse
import javax.xml.transform.*
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource
import org.amcworld.springcrm.util.TemplateURIResolver
import org.apache.avalon.framework.configuration.Configuration
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder
import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code FopService} represents a service which generates PDF
 * documents using XSL-FO.  The service supports multiple templates for a
 * particular type of document, either in a system directory or in a user
 * directory.
 *
 * @author  Daniel Ellermann
 * @version 0.9
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

	ServletContext servletContext = SCH.servletContext
    def messageSource


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

        URIResolver uriResolver = new TemplateURIResolver(servletContext, templateDir)
        FopFactory fopFactory = FopFactory.newInstance()
        fopFactory.URIResolver = uriResolver
        fopFactory.userConfig = getFopConfiguration(templateDir)

		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out)

		TransformerFactory tfFactory = TransformerFactory.newInstance()
		tfFactory.URIResolver = uriResolver

        try {
            InputStream is = getTemplateFile("${templateDir}/${type}-fo.xsl")
    		Transformer transformer =
                tfFactory.newTransformer(new StreamSource(is))
    		transformer.URIResolver = uriResolver
    		transformer.setParameter('versionParam', '2.0')

    		Source src = new StreamSource(data)
    		Result res = new SAXResult(fop.getDefaultHandler())
    		transformer.transform src, res
        } catch (TransformerException e) {
            println e.locator?.dump()
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
                InputStream is = getTemplateFile("${entry.value}/meta.properties")
                if (is) props.load(is)
            } catch (IOException e) { /* ignored */ }
            String name = props.getProperty('name')
            if (!name) {
                name = messageSource.getMessage(
                    "print.template.${entry.key}", null, entry.key, LCH.locale
                )
            }
            res[entry.key] = name
        }
        return res
    }

    /**
     * Gets all the pathes containing print templates.  Any template
     * directories defined in the user template directory overwrites the
     * directory of the same name in the system template directory.
     *
     * @return  a map containing the directory names as key and the absolute
     *          pathes to the template directory as value
     */
    Map<String, String> getTemplatePathes() {
        Map<String, String> res = [: ]
        servletContext.getResourcePaths(SYSTEM_FOLDER).each {
            def m = (it =~ /^.*\/([-\w]+)\/$/)
            if (m.matches()) {
                res[m[0][1]] = "SYSTEM:${it}"
            }
        }
        File dir = userTemplateDir
        if (dir.exists()) {
            dir.eachDir { res[it.name] = it.absolutePath }
        }
        return res
    }

    /**
     * Gets the directory containing the print templates defined by the user.
     *
     * @return  the user print template directory
     */
    File getUserTemplateDir() {
        return new File("${System.getProperty('user.home')}/.springcrm/print")
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
        generatePdf(new StringReader(xml), type, template ?: 'default', baos)

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
            is = getTemplateFile("SYSTEM:${SYSTEM_FOLDER}/default/fop-conf.xml")
        }
		return new DefaultConfigurationBuilder().build(is)
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
        } else {
            return new File(path).newInputStream()
        }
    }
}
