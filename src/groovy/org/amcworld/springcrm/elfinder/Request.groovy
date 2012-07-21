/*
 * Request.groovy
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
import org.apache.commons.logging.LogFactory
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest


/**
 * The class {@code Request} represents ...
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
    Collection<MultipartFile> files


    //-- Constructors ---------------------------

    /**
     * Creates a new request using the given HTTP request.
     *
     * @param connector the connector associated to this request
     * @param request   the given HTTP request used to obtain uploaded files
     *                  and parameters
     */
    Request(Connector connector, HttpServletRequest request) {
        this.connector = connector
        this.request = request
    }


    //-- Public methods -------------------------

    /**
     * Adds debug information to the response if the client requests it.
     */
    void addDebugInfo() {
        if (!!params.debug) {
            response.debugInfo = [
                connector: 'springcrm',
                time: 1000,
                volumes: connector.volumes*.debug()
            ]
        }
    }

    /**
     * Closes this request and frees all used resources.
     */
    void close() {}

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
     *
     * @throws ConnectorException   if an error occurred while processing the
     *                              request
     */
    void process() {
        Command cmd = this.connector.getCommand(command)
        if (cmd) {
            cmd.execute()
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
            response.errors << ConnectorError.UPLOAD
            response.errors << ConnectorError.UPLOAD_TOTAL_SIZE
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
        params = [: ]
        files = (request instanceof MultipartHttpServletRequest) \
            ? request.fileMap.values() \
            : []
        if (log.debugEnabled && files) {
            log.debug "Uploaded ${files.size()} file(s)."
        }

        for (String key : request.parameterMap.keySet()) {
            def value
            if (key.endsWith('[]')) {
                value = []
                value = request.getParameterValues(key).each {
                    value << it?.trim()
                }
                key = key.substring(0, key.length() - 2)
            } else {
                value = request.getParameter(key)?.trim()
            }
            this.params[key] = value
        }
        if (log.debugEnabled) {
            log.debug "Request parameters: ${params.toMapString()}"
        }
    }
}
