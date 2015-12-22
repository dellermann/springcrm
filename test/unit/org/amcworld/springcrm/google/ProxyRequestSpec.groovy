/*
 * ProxyRequestSpec.groovy
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

import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.http.MockLowLevelHttpResponse
import spock.lang.Specification


class ProxyRequestSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Execute unparsed request'() {
        given: 'some mocks'
        String content = '''200 OK
tokenResponse={"access_token":"access4040$Token-4711","expires_in":3600,"refresh_token":"refresh8403%Token-2041","scope":"fooBarScope","token_type":"bearer"}
'''
        def response = new MockLowLevelHttpResponse()
            .setContent(content)
        def transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(response)
            .build()

        and: 'a proxy request'
        def req = new ProxyRequest(
            transport, GoogleSync.JSON_FACTORY, 'refreshToken'
        )

        when: 'I execute this request without parsing'
        def resp = req.executeUnparsed()

        then: 'I get the correct response and request URL'
        200 == resp.statusCode
        content == new String(resp.content.buffer)
        'http://www.amc-world.de/oauth-proxy/index.php?action=refreshToken' == resp.request?.url.toURI().toString()
    }

    def 'Execute parsed request'() {
        given: 'some mocks'
        String content = '''200 OK
tokenResponse={"access_token":"access4040$Token-4711","expires_in":3600,"refresh_token":"refresh8403%Token-2041","scope":"fooBarScope","token_type":"bearer"}
'''
        def response = new MockLowLevelHttpResponse()
            .setContent(content)
        def transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(response)
            .build()

        and: 'a proxy request'
        def req = new ProxyRequest(
            transport, GoogleSync.JSON_FACTORY, 'refreshToken'
        )

        when: 'I execute this request with parsing'
        def resp = req.execute()

        then: 'I get the correct response and request URL'
        200 == resp.code
        'OK' == resp.message
        null != resp.tokenResponse
        'access4040$Token-4711' == resp.tokenResponse.getAccessToken()
        3600L == resp.tokenResponse.getExpiresInSeconds()
        'refresh8403%Token-2041' == resp.tokenResponse.getRefreshToken()
        'fooBarScope' == resp.tokenResponse.getScope()
        'bearer' == resp.tokenResponse.getTokenType()
    }
}
