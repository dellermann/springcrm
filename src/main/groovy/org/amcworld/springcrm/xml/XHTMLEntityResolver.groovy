/*
 * XHTMLEntityResolver.groovy
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
import org.amcworld.springcrm.FopService
import org.springframework.context.ApplicationContext
import org.springframework.core.io.Resource
import org.xml.sax.EntityResolver
import org.xml.sax.InputSource
import org.xml.sax.SAXException


/**
 * The class {@code XHTMLEntityResolver} represents a resolver for entities by
 * looking in a DTD folder in servlet context.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
@CompileStatic
class XHTMLEntityResolver implements EntityResolver {

    //-- Fields ---------------------------------

    ApplicationContext applicationContext


    //-- Public methods -------------------------

    @Override
    InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException
    {
        String path = InvoicingTransactionXML.ENTITY_CATALOG[publicId]
        if (!path) {
            return null
        }

        Resource resource = applicationContext.getResource(
            "${FopService.SYSTEM_FOLDER}/dtd/${path}"
        )

        resource.exists() ? new InputSource(resource.inputStream) : null
    }
}
