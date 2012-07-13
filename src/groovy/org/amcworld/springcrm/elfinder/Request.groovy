/*
 * ElFinderRequest.groovy
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


package org.amcworld.springcrm.elfinder

import javax.servlet.http.HttpServletRequest
import org.amcworld.springcrm.elfinder.command.Command
import org.apache.commons.fileupload.FileItemIterator
import org.apache.commons.fileupload.FileItemStream
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.fileupload.util.Streams
import org.apache.commons.logging.LogFactory


/**
 * The class {@code ElFinderRequest} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class Request {

    //-- Constants ------------------------------

    private static final log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    String command
    Connector connector
    HttpServletRequest request
    Map<String, Object> params


    //-- Constructors ---------------------------

    Request(Connector connector, HttpServletRequest request) {
        this.connector = connector
        this.request = request
    }


    //-- Public methods -------------------------

    /**
     * Initializes this request.
     */
    void initialize() {
        parseRequest()
        command = params.cmd
    }

    /**
     * Processes the request by instantiating the command class and letting do
     * it its job.
     */
    void process() {
        Command cmd = this.connector.getCommand(command)
        if (cmd) {
            try {
                cmd.execute()
            } catch (ConnectorException e) {
                log.warn "Error executing command ${command}.", e
                response.errors += e.errorCodes ?: ConnectorError.UNKNOWN
            }
        }
    }

    /**
     * Validates the pre-conditions of this request.
     *
     * @return  {@code true} if the pre-conditions are fulfilled; {@code false}
     *          otherwise
     */
    boolean validate() {
        if (!command && ('POST' == this.connector.httpRequest.method)) {
            response.errors += ConnectorError.UPLOAD
            response.errors += ConnectorError.UPLOAD_TOTAL_SIZE
        }
        return response.errors.empty
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the response associated with the connector handling this request.
     *
     * @return  the associated response
     */
    protected Response getResponse() {
        return this.connector.response
    }

    /**
     * Parses this request, handles uploaded files, and stores all request
     * parameters to {@code params}.
     */
    protected void parseRequest() {
        this.params = [: ]
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                ServletFileUpload upload = new ServletFileUpload()
                FileItemIterator iter = upload.getItemIterator(request)
                while (iter.hasNext()) {
                    FileItemStream item = iter.next()
                    String name = item.fieldName
                    InputStream stream = item.openStream()
                    if (item.formField) {
                        this.params[name] = Streams.asString(stream)
                    } else {
                        String fileName = item.name
                        if (fileName?.trim()) {
                            // TODO do we really need this?
                        }
                    }
                }
            } catch (Exception e) {
                log.error 'Unexpected error parsing multipart request.', e
            }
        }

        for (String key : request.parameterMap.keySet()) {
            def value
            if (key.endsWith('[]')) {
                value = []
                request.getParameterValues(key).each {
                    value += it?.trim()
                }
            } else {
                value = request.getParameter(key)?.trim()
            }
            this.params[key] = value
        }
    }
}
