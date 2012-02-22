/*
 * TemplateURIResolver.groovy
 *
 * $Id: $
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


package org.amcworld.springcrm.util

import javax.xml.transform.stream.StreamSource
import javax.servlet.ServletContext
import org.apache.fop.servlet.ServletContextURIResolver
import javax.xml.transform.Source
import javax.xml.transform.TransformerException


/**
 * @author  Daniel Ellermann
 *
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
