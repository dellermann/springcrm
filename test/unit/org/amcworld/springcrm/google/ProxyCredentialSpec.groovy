/*
 * ProxyCredentialSpec.groovy
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


package org.amcworld.springcrm.google

import com.google.api.client.auth.oauth2.BearerToken
import com.google.api.client.http.BasicAuthentication
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.http.MockLowLevelHttpResponse
import spock.lang.Specification


class ProxyCredentialSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Create ProxyCredential with a builder'() {
        given: 'some necessary values'
        HttpTransport transport = Mock()
        JsonFactory jsonFactory = Mock()
        def accessMethod = BearerToken.authorizationHeaderAccessMethod()
        def clientAuth =
            new BasicAuthentication('s6BhdRkqt3', '7Fjfp0ZBr1KtDRbnfVdmIw')

        when: 'I create a proxy credential via builder'
        def c = new ProxyCredential.Builder(accessMethod)
            .setTransport(transport)
            .setJsonFactory(jsonFactory)
            .setTokenServerUrl(
                new GenericUrl('https://server.example.com/token')
            )
            .setClientAuthentication(clientAuth)
            .setRefreshListeners([])
            .build()

        then: 'the properties are set correctly'
        accessMethod == c.method
        transport == c.transport
        jsonFactory == c.jsonFactory
        'https://server.example.com/token' == c.tokenServerEncodedUrl
        clientAuth == c.clientAuthentication
        null != c.refreshListeners
        c.refreshListeners.empty
    }

    def 'Execute non-existent refresh token'() {
        given: 'some necessary values'
        HttpTransport transport = Mock()
        JsonFactory jsonFactory = Mock()
        def accessMethod = BearerToken.authorizationHeaderAccessMethod()
        def clientAuth =
            new BasicAuthentication('s6BhdRkqt3', '7Fjfp0ZBr1KtDRbnfVdmIw')

        and: 'a ProxyCredential'
        def c = new ProxyCredential.Builder(accessMethod)
            .setTransport(transport)
            .setJsonFactory(jsonFactory)
            .setTokenServerUrl(
                new GenericUrl('https://server.example.com/token')
            )
            .setClientAuthentication(clientAuth)
            .setRefreshListeners([])
            .build()

        and: 'no refresh token in the ProxyCredential'
        c.refreshToken = null

        when: 'I execute the refresh token'
        def tokenResponse = c.executeRefreshToken()

        then: 'I get no token response'
        null == tokenResponse
    }

    def 'Execute existent refresh token'() {
        given: 'an example token response'
        String t = '''200 OK
tokenResponse={ "access_token":"1/fFAGRNJru1FTz70BzhT3Zg", "expires_in":3920, "token_type":"Bearer" }'''

        and: 'a mocked HTTP response'
        def response = new MockLowLevelHttpResponse()
        response.statusCode = 200
        response.content = new ByteArrayInputStream(t.bytes)

        and: 'a HTTP transport'
        HttpTransport transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(response)
            .build()

        and: 'other necessary objects'
        def accessMethod = BearerToken.authorizationHeaderAccessMethod()
        def clientAuth =
            new BasicAuthentication('s6BhdRkqt3', '7Fjfp0ZBr1KtDRbnfVdmIw')

        and: 'a ProxyCredential'
        def c = new ProxyCredential.Builder(accessMethod)
            .setTransport(transport)
            .setJsonFactory(GoogleSync.JSON_FACTORY)
            .setTokenServerUrl(
                new GenericUrl('https://server.example.com/token')
            )
            .setClientAuthentication(clientAuth)
            .setRefreshListeners([])
            .build()

        and: 'a refresh token in the ProxyCredential'
        c.refreshToken = 'fooBar$RefreshToken-0814'

        when: 'I execute the refresh token'
        def tokenResponse = c.executeRefreshToken()

        then: 'I get a correct token response'
        null != tokenResponse
        // Note! Don't use property names such as accessToken here because
        // TokenResponse implements Map!
        '1/fFAGRNJru1FTz70BzhT3Zg' == tokenResponse.getAccessToken()
        3920L == tokenResponse.getExpiresInSeconds()
        'Bearer' == tokenResponse.getTokenType()
    }
}
