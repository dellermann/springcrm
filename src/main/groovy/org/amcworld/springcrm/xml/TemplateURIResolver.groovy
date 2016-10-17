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
import javax.validation.constraints.NotNull
import javax.xml.transform.Source
import javax.xml.transform.TransformerException
import javax.xml.transform.URIResolver
import javax.xml.transform.stream.StreamSource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader


/**
 * The class {@code TemplateURIResolver} represents a URI resolver which
 * search in system and user-defined templates.  The class handles URIs without
 * any prefix and with prefix {@code user-template:}.
 *
 * @author	Daniel Ellermann
 * @version 2.1
 * @since   0.9
 */
@CompileStatic
class TemplateURIResolver implements URIResolver {

    //-- Constants ------------------------------

    /**
     * The protocol prefix used to locate resources in user-defined templates.
     */
    public static final String USER_TEMPLATE_PROTO = 'user-template:'


    //-- Fields ---------------------------------

    /**
     * The resource loader used to load the resources.
     */
    final ResourceLoader resourceLoader

    /**
     * The path where user-defined template is located.
     */
    final String userTemplatePath


    //-- Constructors ---------------------------

    /**
     * Creates a new resolver which locates resources in system and user-defined
     * templates.
     *
     * @param servletContext    the given resource loader
     * @param userTemplatePath  the directory containing the user-defined
     *                          template
     */
    TemplateURIResolver(@NotNull ResourceLoader resourceLoader,
                        @NotNull String userTemplatePath)
    {
        this.resourceLoader = resourceLoader

        while (userTemplatePath.endsWith('/')) {
            userTemplatePath = userTemplatePath.substring(
                0, userTemplatePath.length() - 1
            )
        }
        this.userTemplatePath = userTemplatePath
    }


    //-- Public methods -------------------------

    @Override
    Source resolve(String href, String base) throws TransformerException {
        href.startsWith(USER_TEMPLATE_PROTO) \
            ? resolveInUserTemplate(
                href.substring(USER_TEMPLATE_PROTO.length())
            )
            : resolveInSystemTemplate(href)
    }


    //-- Non-public methods ---------------------

    /**
     * Resolves the given path in system template.
     *
     * @param path                  the given path
     * @return                      the resource
     * @throws TransformerException if the resource with the given path could
     *                              not be found
     */
    private Source resolveInSystemTemplate(String path)
        throws TransformerException
    {
        Resource res = resourceLoader.getResource('classpath:' + path)
        if (!res.exists()) {
            throw new TransformerException(
                """Resource does not exist. "${path}" is not available in classpath."""
            )
        }

        new StreamSource(res.inputStream)
    }

    /**
     * Resolves the given path in the user-defined template.
     *
     * @param path                  the given path without the protocol prefix
     *                              specified in {@code USER_TEMPLATE_PROTO}
     * @return                      the resource
     * @throws TransformerException if the resource with the given path could
     *                              not be found
     */
    private Source resolveInUserTemplate(String path)
        throws TransformerException
    {
        while (path.startsWith('/')) {
            path = path.substring(1)
        }

        Resource res = resourceLoader.getResource(
            "${userTemplatePath}/${path}".toString()
        )
        if (!res.exists()) {
            throw new TransformerException(
                """Resource does not exist. "${path}" is not available in user template directory ${userTemplatePath}."""
            )
        }

        new StreamSource(res.inputStream)
    }
}
