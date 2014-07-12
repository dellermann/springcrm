/*
 * ProxyCredential.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.CredentialRefreshListener
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.auth.oauth2.Credential.AccessMethod
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory


/**
 * The class {@code ProxyCredential} represents a subclass of
 * {@code Credential} which sends refresh requests to the AMC World proxy
 * instead of to the Google server.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.0
 */
class ProxyCredential extends Credential {

    //-- Constructors ---------------------------

    protected ProxyCredential(Builder builder) {
        super(builder)
    }


    //-- Non-public methods ---------------------

    protected TokenResponse executeRefreshToken() throws IOException {
        if (refreshToken == null) {
            return null
        }

        def req = new ProxyRequest(transport, jsonFactory, 'refresh')
        req.put 'refreshToken', refreshToken
        ProxyResponse response = req.execute()
        response.tokenResponse
    }


    //-- Inner classes --------------------------

    /**
     * The class {@code Builder} represents a {@code ProxyCredential} builder.
     *
     * @author  Daniel Ellermann
     * @version 1.4
     * @since   1.4
     */
    static class Builder extends Credential.Builder {

        //-- Constructors -----------------------

        Builder(AccessMethod method) {
            super(method)
        }


        //-- Public methods ---------------------

        ProxyCredential build() {
            new ProxyCredential(this)
        }
    }
}
