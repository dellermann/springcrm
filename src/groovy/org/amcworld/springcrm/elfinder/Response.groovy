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
    List<ConnectorError> errors = []
    Map<String, String> headers = [: ]
    HttpServletResponse response


    //-- Constructors ---------------------------

    Response(Connector connector, HttpServletResponse response) {
        this.connector = connector
        this.response = response
    }


    //-- Public methods -------------------------

    Object getAt(String key) {
        return data[key]
    }

    Response leftShift(Object fileOrStream) {
        if (fileOrStream instanceof File) {
            dataStreams += fileOrStream.newInputStream()
        } else if (fileOrStream instanceof InputStream) {
            dataStreams += fileOrStream
        }
        return this
    }

    void output() {
        response.characterEncoding = 'UTF-8'
        if (!contentType && (!errors || dataStreams)) {
            contentType = 'application/json; charset=UTF-8'
        }
        response.contentType = contentType
        for (String name : headers.keySet()) {
            response.setHeader(name, headers[name])
        }

        if (errors) {
            def errData = [error: errors*.code]
            response.writer.write(errData.encodeAsJSON())
        } else if (dataStreams) {
            for (InputStream dataStream : dataStreams) {
                response.writer << dataStream
            }
        } else {
            response.writer.write(data.encodeAsJSON())
        }
        response.flushBuffer()
    }

    void putAt(String key, Object value) {
        data[key] = value
    }

    void setHeader(String name, String value) {
        headers[name] = value
    }
}
