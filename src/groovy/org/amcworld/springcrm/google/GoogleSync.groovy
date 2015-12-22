/*
 * GoogleSync.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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


package org.amcworld.springcrm.google

import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory


/**
 * The interface {@code GoogleSync} represents a common base class for
 * Google data access services.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.0
 */
interface GoogleSync {

    //-- Constants ------------------------------

    /**
     * The HTTP transport instance which is to use to communicate with the
     * Google server.
     */
    static final HttpTransport HTTP_TRANSPORT =
        new com.google.api.client.http.javanet.NetHttpTransport()

    /**
     * The JSON factory instance which is to use to create and parse JSON data.
     */
    static final JsonFactory JSON_FACTORY =
        com.google.api.client.json.jackson2.JacksonFactory.defaultInstance
}
