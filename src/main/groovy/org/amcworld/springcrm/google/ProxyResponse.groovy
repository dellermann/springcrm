/*
 * ProxyResponse.groovy
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

import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.util.GenericData
import com.google.api.client.util.Key
import groovy.transform.CompileStatic


/**
 * The class {@code ProxyResponse} represents a proxy for a HTTP response that
 * allows access to particular properties.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.2
 */
@CompileStatic
class ProxyResponse extends GenericData {

    //-- Fields ---------------------------------

    /*
     * Implementation notes: declaring private fields with @Key annotation
     * declares known data keys.
     */

    /**
     * The response code from proxy.
     */
    @Key
    short code

    /**
     * Any data submitted along the response which is typical used in error
     * messages.
     *
     * @since   2.0
     */
    @Key
    Map<String, Object> data

    /**
     * The response message from proxy.
     */
    @Key
    String message

    /**
     * The token response containing the access and refresh tokens.
     */
    @Key
    TokenResponse tokenResponse
}
