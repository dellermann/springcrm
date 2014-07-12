/*
 * ProxyCredentialSpec.groovy
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

import com.google.api.client.auth.oauth2.BearerToken
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.http.BasicAuthentication
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import spock.lang.Specification


class ProxyCredentialSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Create ProxyCredential with a builder'() {
        given: 'some necessary values'
        def transport = Mock(HttpTransport)
        def jsonFactory = Mock(JsonFactory)
        def accessMethod = BearerToken.authorizationHeaderAccessMethod()
        def clientAuth = new BasicAuthentication('s6BhdRkqt3', '7Fjfp0ZBr1KtDRbnfVdmIw')

        when:
        def credential = new ProxyCredential.Builder(accessMethod)
            .setTransport(transport)
            .setJsonFactory(jsonFactory)
            .setTokenServerUrl(new GenericUrl('https://server.example.com/token'))
            .setClientAuthentication(clientAuth)
            .setRefreshListeners([])
            .build()

        then:
        accessMethod == credential.method
        transport == credential.transport
        jsonFactory == credential.jsonFactory
        'https://server.example.com/token' == credential.tokenServerEncodedUrl
        clientAuth == credential.clientAuthentication
        null != credential.refreshListeners
        credential.refreshListeners.empty
    }

    def 'Execute non-existent refresh token'() {
        given: 'some necessary values'
        def transport = Mock(HttpTransport)
        def jsonFactory = Mock(JsonFactory)
        def accessMethod = BearerToken.authorizationHeaderAccessMethod()
        def clientAuth = new BasicAuthentication('s6BhdRkqt3', '7Fjfp0ZBr1KtDRbnfVdmIw')

        and: 'a ProxyCredential'
        def credential = new ProxyCredential.Builder(accessMethod)
            .setTransport(transport)
            .setJsonFactory(jsonFactory)
            .setTokenServerUrl(new GenericUrl('https://server.example.com/token'))
            .setClientAuthentication(clientAuth)
            .setRefreshListeners([])
            .build()

        and: 'no refresh token in the ProxyCredential'
        credential.refreshToken = null

        when: 'I execute the refresh token'
        def tokenResponse = credential.executeRefreshToken()

        then: 'I get no token response'
        null == tokenResponse
    }

    def 'Execute existent refresh token'() {
        given: 'some necessary values'
        def transport = Mock(HttpTransport)
        def jsonFactory = Mock(JsonFactory)
        def accessMethod = BearerToken.authorizationHeaderAccessMethod()
        def clientAuth = new BasicAuthentication('s6BhdRkqt3', '7Fjfp0ZBr1KtDRbnfVdmIw')

        and: 'a ProxyCredential'
        def credential = new ProxyCredential.Builder(accessMethod)
            .setTransport(transport)
            .setJsonFactory(jsonFactory)
            .setTokenServerUrl(new GenericUrl('https://server.example.com/token'))
            .setClientAuthentication(clientAuth)
            .setRefreshListeners([])
            .build()

        and: 'no refresh token in the ProxyCredential'
        credential.refreshToken = 'fooBar$RefreshToken-0814'

        and: 'a mock for ProxyRequest'
        ProxyRequest.metaClass.execute = { ->
            def tokenResponse = new TokenResponse(
                accessToken: 'access4040$Token-4711',
                expiresInSeconds: 3600L,
                refreshToken: 'refresh8403%Token-2041',
                scope: 'fooBarScope',
                tokenType: 'bearer'
            )
            new ProxyResponse(
                code: 'abcToken$4704',
                message: 'Test message',
                tokenResponse: tokenResponse
            )
        }

        when: 'I execute the refresh token'
        def tokenResponse = credential.executeRefreshToken()

        then: 'I get a correct token response'
        null != tokenResponse
        // Note! Don't use property names such as accessToken here because
        // TokenResponse implements Map!
        'access4040$Token-4711' == tokenResponse.getAccessToken()
        3600L == tokenResponse.getExpiresInSeconds()
        'refresh8403%Token-2041' == tokenResponse.getRefreshToken()
        'fooBarScope' == tokenResponse.getScope()
        'bearer' == tokenResponse.getTokenType()
    }
}
