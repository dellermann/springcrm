/*
 * ProxyRequest.groovy
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


package org.amcworld.springcrm.google

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.GenericData


/**
 * The class {@code ProxyRequest} encapsulates a requests which is sent to the
 * AMC World proxy in order to perform OAuth2 communication.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
class ProxyRequest extends GenericData {

    //-- Constants ------------------------------

    protected static final String PROXY_URL = 'http://www.amc-world.de/oauth-proxy/index.php'


    //-- Instance variables ---------------------

    String action
    JsonFactory jsonFactory
    HttpTransport transport


    //-- Constructors ---------------------------

    /**
     * Creates a new request to the AMC World proxy.
     *
     * @param transport     the HTTP transport instance
     * @param jsonFactory   the JSON factory used to create JSON parsers
     * @param action        the action which is sent to the proxy
     */
    ProxyRequest(HttpTransport transport, JsonFactory jsonFactory,
                 String action)
    {
        this.transport = transport
        this.jsonFactory = jsonFactory
        this.action = action
    }


    //-- Public methods -------------------------

    /**
     * Sends the request and parses the response.
     *
     * @return              the parsed response
     * @throws IOException  if an error occurred while sending the request or
     *                      parsing the response, or if the proxy returned an
     *                      error code
     */
    ProxyResponse execute() throws IOException {
        HttpResponse response = executeUnparsed()
        ProxyResponse res = response.parseAs(ProxyResponse)
        if (res.code != 200) {
            throw new HttpResponseException(response)
        }
        return res
    }

    /**
     * Sends the request and returns the unparsed response.
     *
     * @return              the HTTP response
     * @throws IOException  if an error occurred while sending the request or
     *                      receiving the response
     */
    HttpResponse executeUnparsed() throws IOException {
        HttpRequestFactory requestFactory = transport.createRequestFactory()
        GenericUrl url = new GenericUrl(PROXY_URL)
        url.put('action', action)
        url.putAll(this)
        HttpRequest request = requestFactory.buildGetRequest(url)
        request.addParser(new ProxyHttpParser(jsonFactory));
        HttpResponse response = request.execute()
        if (response.isSuccessStatusCode()) {
            return response
        }
        throw new HttpResponseException(response)
    }
}
