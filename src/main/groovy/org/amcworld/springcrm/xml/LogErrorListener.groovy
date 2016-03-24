/*
 * LogErrorListener.groovy
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
import javax.xml.transform.ErrorListener
import javax.xml.transform.TransformerException
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


/**
 * The class {@code FopService} represents a service which generates PDF
 * documents using XSL-FO.  The service supports multiple templates for a
 * particular type of document, either in a system directory or in a user
 * directory.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class LogErrorListener implements ErrorListener {

    //-- Constants ------------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Public methods -------------------------

    @Override
    void error(TransformerException e) {
        log.error "XSLT error: ${e.messageAndLocation}"
    }

    @Override
    void fatalError(TransformerException e) {
        log.fatal "XSLT fatal error: ${e.messageAndLocation}"
    }

    @Override
    void warning(TransformerException e) {
        log.warn "XSLT warning: ${e.messageAndLocation}"
    }
}