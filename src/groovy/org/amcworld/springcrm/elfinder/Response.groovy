/*
 * Response.groovy
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

import javax.servlet.http.HttpServletResponse


/**
 * The class {@code Response} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class Response {

    //-- Instance variables ---------------------

    Connector connector
    String contentType
    Map<String, Object> data = [: ]
    List<InputStream> dataStreams = []
    Map<String, Object> debugInfo
    List<List<Object>> errors = []
    Map<String, String> headers = [: ]
    HttpServletResponse response
    int status
    List<List<Object>> warnings = []


    //-- Constructors ---------------------------

    /**
     * Creates a new response with the underlying HTTP response.
     *
     * @param connector the connector associated to this response
     * @param response  the underlying HTTP response
     */
    Response(Connector connector, HttpServletResponse response) {
        this.connector = connector
        this.response = response
    }


    //-- Public methods -------------------------

    /**
     * Gets the data value with the given key.  Data values are stored in the
     * response and send back to the client in JSON format.
     *
     * @param key   the given key
     * @return      the data value; {@code null} if no such value exists
     */
    Object getAt(String key) {
        return data[key]
    }

    /**
     * Stores the given input stream of data to the list of input streams.  All
     * input streams are send back to the client.
     *
     * @param stream    the input stream to add
     * @return          a reference to this response
     */
    Response leftShift(InputStream stream) {
        dataStreams << stream
        return this
    }

    /**
     * Produces the output by writing all data stored in this class to the
     * underlying HTTP response.
     */
    void output() {
        response.characterEncoding = 'UTF-8'
        if (status) {
            response.status = status
        }
        if (errors || warnings || !dataStreams) {
            if (!contentType) {
                contentType = 'application/json'
            }
            if (debugInfo) {
                data << debugInfo
            }
        }
        response.contentType = contentType
        for (String name : headers.keySet()) {
            response.setHeader(name, headers[name].toString())
        }

        if (errors) {
            def errData = [error: errors]
            response.writer.write(errData.encodeAsJSON())
        } else if (warnings) {
            def wrnData = [warning: warnings]
            response.writer.write(wrnData.encodeAsJSON())
        } else if (dataStreams) {
            for (InputStream dataStream : dataStreams) {
                response.outputStream << dataStream
            }
        } else {
            response.writer.write(data.encodeAsJSON())
        }
        response.flushBuffer()

        for (InputStream dataStream : dataStreams) {
            dataStream.close()
        }
    }

    /**
     * Sets the data value with the given key.  Data values are stored in the
     * response and send back to the client in JSON format.
     *
     * @param key   the given key
     * @param value the data value to set
     */
    void putAt(String key, Object value) {
        data[key] = value
    }

    /**
     * Sets the response header with the given name.
     *
     * @param name  the name of the header
     * @param value the value to set
     */
    void setHeader(String name, String value) {
        headers[name] = value
    }
}
