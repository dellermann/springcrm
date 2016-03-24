/*
 * TemplateURIResolver.groovy
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


package org.amcworld.springcrm.xml

import groovy.transform.CompileStatic
import javax.servlet.ServletContext
import javax.xml.transform.Source
import javax.xml.transform.TransformerException
import javax.xml.transform.stream.StreamSource
import org.apache.fop.servlet.ServletContextURIResolver


/**
 * The class {@code TemplateURIResolver} represents a URI resolver which
 * handles URIs with the prefix {@code user-template:} and
 * {@code servlet-context:}
 *
 * @author	Daniel Ellermann
 * @version 2.1
 * @since   0.9
 */
@CompileStatic
class TemplateURIResolver extends ServletContextURIResolver {

    //-- Constants ------------------------------

    /**
     * The protocol prefix used to locate user-defined templates.
     */
    public static final String USER_TEMPLATE_PROTO = 'user-template:'


    //-- Fields ---------------------------------

    /**
     * The path where user-defined templates are located.
     */
    final String userTemplatePath


    //-- Constructors ---------------------------

    /**
     * Creates a new resolver which locates templates in servlet context and a
     * directory containing user-defined templates.
     *
     * @param servletContext    the given servlet context
     * @param userTemplatePath  the directory containing user-defined templates
     */
    TemplateURIResolver(ServletContext servletContext, String userTemplatePath)
    {
        super(servletContext)

        if (userTemplatePath == null) {
            throw new IllegalArgumentException(
                'Parameter userTemplatePath must not be null.'
            )
        }

        this.userTemplatePath = userTemplatePath
    }


    //-- Public methods -------------------------

    @Override
    Source resolve(String href, String base) throws TransformerException {
        href.startsWith(USER_TEMPLATE_PROTO) \
            ? resolveUserTemplate(href.substring(USER_TEMPLATE_PROTO.length()))
            : super.resolve(href, base)
    }


    //-- Non-public methods ---------------------

    /**
     * Resolves the given path as user-defined template.
     *
     * @param path                  the given path without the protocol prefix
     *                              as specified in {@code USER_TEMPLATE_PROTO}
     * @return                      the source representing the template
     * @throws TransformerException if either the template at the given path
     *                              could not be found or the given path is
     *                              malformed
     */
    private Source resolveUserTemplate(String path)
        throws TransformerException
    {
        while (path.startsWith('//')) {
            path = path.substring(1)
        }

        try {
            File f = new File(userTemplatePath, path)
            if (!f.exists()) {
                throw new TransformerException(
                    """Resource does not exist. "${path}" is not available in user template directory ${userTemplatePath}."""
                )
            }

            new StreamSource(f.newInputStream())
        } catch (MalformedURLException e) {
            throw new TransformerException(
                "Error accessing resource using servlet context: ${path}", e
            )
        }
    }
}
