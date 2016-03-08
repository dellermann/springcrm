/*
 * TemplateURIResolver.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


package org.amcworld.springcrm.util

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
 * @version 0.9
 */
class TemplateURIResolver extends ServletContextURIResolver {

    //-- Constants ------------------------------

    public static final String USER_TEMPLATE_PROTOCOL = "user-template:"


    //-- Instance variables ---------------------

    protected String userTemplatePath


    //-- Constructors ---------------------------

    TemplateURIResolver(ServletContext servletContext, String userTemplatePath)
    {
        super(servletContext)
        this.userTemplatePath = userTemplatePath
    }


    //-- Public methods -------------------------

    @Override
    public Source resolve(String href, String base)
        throws TransformerException
    {
        if (href.startsWith(USER_TEMPLATE_PROTOCOL)) {
            return resolveUserTemplate(
                href.substring(USER_TEMPLATE_PROTOCOL.length())
            )
        } else {
            return super.resolve(href, base)
        }
    }


    //-- Non-public methods ---------------------

    protected Source resolveUserTemplate(String path) {
        while (path.startsWith('//')) {
            path = path.substring(1)
        }
        try {
            File f = new File("${userTemplatePath}/${path}")
            if (f.exists()) {
                return new StreamSource(f.newInputStream())
            } else {
                throw new TransformerException(
                    """Resource does not exist. "${path}" is not available in user template directory ${userTemplatePath}."""
                )
            }
        } catch (MalformedURLException mfue) {
            throw new TransformerException(
                "Error accessing resource using servlet context: ${path}", mfue
            )
        }
    }
}
