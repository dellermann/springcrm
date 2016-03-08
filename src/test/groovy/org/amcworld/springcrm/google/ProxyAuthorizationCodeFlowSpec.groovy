/*
 * ProxyAuthorizationCodeFlowSpec.groovy
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

import static org.amcworld.springcrm.google.AbstractUserCredentialDataStore.SETTINGS_KEY
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.http.GenericUrl
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.http.MockLowLevelHttpResponse
import grails.test.mixin.Mock
import org.amcworld.springcrm.User
import org.amcworld.springcrm.UserSetting
import org.amcworld.springcrm.UserSettings
import org.amcworld.springcrm.google.UserCredentialDataStoreFactory.UserCredentialDataStore
import spock.lang.Specification


@Mock([User, UserSetting])
class ProxyAuthorizationCodeFlowSpec extends Specification {

    //-- Fixture methods ------------------------

    def setup() {
        mockDomain User, [
            [
                userName: 'jsmith', password: 'secret', firstName: 'John',
                lastName: 'Smith', email: 'j.smith@example.com'
            ],
            [
                userName: 'bwayne', password: 'very-secret',
                firstName: 'Barbra', lastName: 'Wayne',
                email: 'b.wayne@example.com'
            ]
        ]

        User.metaClass.getSettings = { -> new UserSettings(delegate) }
    }


    //-- Feature methods ------------------------

    def 'Create and store a credential of existing user'() {
        given: 'a token response'
        def tokenResponse = new TokenResponse(
            accessToken: 'access4040-Token-4711',
            expiresInSeconds: 3600L,
            refreshToken: 'refresh8403%Token-2041',
            scope: 'fooBarScope',
            tokenType: 'bearer'
        )

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        and: 'a time stamp'
        long timeStamp = System.currentTimeMillis()

        when: 'I create and store a credential from the token response'
        def credential =
            authorizationCodeFlow.createAndStoreCredential(tokenResponse, 'jsmith')

        then: 'I get a valid credential'
        null != credential
        'access4040-Token-4711' == credential.accessToken
        3000L <= credential.expiresInSeconds    // expiration time has just begun
        timeStamp + 3600L <= credential.expirationTimeMilliseconds
        'refresh8403%Token-2041' == credential.refreshToken
        transport == credential.transport
        jsonFactory == credential.jsonFactory
        1 == credential.refreshListeners.size()
        credential.refreshListeners.first().credentialDataStore instanceof UserCredentialDataStore

        and: 'the credential is stored in user settings'
        def user = User.get(1)
        user.settings[SETTINGS_KEY] ==~ /^\{"accessToken":"access4040-Token-4711","refreshToken":"refresh8403%Token-2041","expirationTimeMilliseconds":\d+\}$/
        1 == UserSetting.count()
    }

    def 'Create and store a credential of non-existing user'() {
        given: 'a token response'
        def tokenResponse = new TokenResponse(
            accessToken: 'access4040-Token-4711',
            expiresInSeconds: 3600L,
            refreshToken: 'refresh8403%Token-2041',
            scope: 'fooBarScope',
            tokenType: 'bearer'
        )

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        and: 'a time stamp'
        long timeStamp = System.currentTimeMillis()

        when: 'I create and store a credential from the token response'
        def credential =
            authorizationCodeFlow.createAndStoreCredential(tokenResponse, 'jdoe')

        then: 'I get a valid credential'
        null != credential
        'access4040-Token-4711' == credential.accessToken
        3000L <= credential.expiresInSeconds    // expiration time has just begun
        timeStamp + 3000000L <= credential.expirationTimeMilliseconds
        'refresh8403%Token-2041' == credential.refreshToken
        transport == credential.transport
        jsonFactory == credential.jsonFactory
        1 == credential.refreshListeners.size()
        credential.refreshListeners.first().credentialDataStore instanceof UserCredentialDataStore

        and: 'the credential is not stored in user settings'
        0 == UserSetting.count()
    }

    def 'Load credential of existing user'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        and: 'a time stamp'
        long timeStamp = System.currentTimeMillis()

        when: 'I load the credential'
        def credential = authorizationCodeFlow.loadCredential('jsmith')

        then: 'I get a valid credential'
        null != credential
        'access4040-Token-4711' == credential.accessToken
        3000L <= credential.expiresInSeconds    // expiration time has just begun
        timeStamp + 3000000L <= credential.expirationTimeMilliseconds
        'refresh8403%Token-2041' == credential.refreshToken
        transport == credential.transport
        jsonFactory == credential.jsonFactory
        1 == credential.refreshListeners.size()
        credential.refreshListeners.first().credentialDataStore instanceof UserCredentialDataStore
    }

    def 'Load credential of non-existing user'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        expect:
        null == authorizationCodeFlow.loadCredential('jdoe')
    }

    def 'Load non-existing credential of existing user'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        expect:
        null == authorizationCodeFlow.loadCredential('bwayne')
    }

    def 'Obtain credential from proxy and store it'() {
        given: 'a mocked execution method'
        def urlParams = null
        ProxyRequest.metaClass.execute = { ->
            urlParams = delegate
            new ProxyResponse(
                code: 'abc', 'message': '',
                tokenResponse: new TokenResponse(
                    accessToken: 'access4040-Token-4711',
                    expiresInSeconds: 3600,
                    refreshToken: 'refresh8403%Token-2041',
                    scope: 'fooBarScope',
                    tokenType: 'bearer',
                )
            )
        }

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(new MockLowLevelHttpResponse())
            .build()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        and: 'a time stamp'
        long timeStamp = System.currentTimeMillis()

        when: 'I obtain the credential from proxy and store it'
        def credential = authorizationCodeFlow.obtainAndStoreCredential(
            'jsmith', 'A450-8201'
        )

        then: 'the request was executed with the correct parameters'
        null != urlParams
        'A450-8201' == urlParams.get('clientId')

        and: 'I get a valid credential'
        null != credential
        'access4040-Token-4711' == credential.accessToken
        3000L <= credential.expiresInSeconds    // expiration time has just begun
        timeStamp + 3600L <= credential.expirationTimeMilliseconds
        'refresh8403%Token-2041' == credential.refreshToken
        transport == credential.transport
        jsonFactory == credential.jsonFactory
        1 == credential.refreshListeners.size()
        credential.refreshListeners.first().credentialDataStore instanceof UserCredentialDataStore

        and: 'the credential is stored in user settings'
        def user = User.get(1)
        user.settings[SETTINGS_KEY] ==~ /^\{"accessToken":"access4040-Token-4711","refreshToken":"refresh8403%Token-2041","expirationTimeMilliseconds":\d+\}$/
        1 == UserSetting.count()
    }

    def 'Register application instance at proxy'() {
        given: 'a mocked execution method'
        def urlParams = null
        ProxyRequest.metaClass.execute = { ->
            urlParams = delegate
            new ProxyResponse(url: 'http://www.google.com/login')
        }

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(new MockLowLevelHttpResponse())
            .build()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        when: 'I obtain the credential from proxy and store it'
        def url = authorizationCodeFlow.register(
            'http://localhost:8080/springcrm'
        )

        then: 'the request was executed with the correct parameters'
        null != urlParams
        'http://localhost:8080/springcrm' == urlParams.get('redirectUrl')

        and: 'I get a valid credential'
        'http://www.google.com/login' == url
    }

    def 'Revoke a credential of existing user'() {
        given: 'a mocked execution method'
        def urlParams = null
        ProxyRequest.metaClass.execute = { ->
            urlParams = delegate
            new ProxyResponse()
        }

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(new MockLowLevelHttpResponse())
            .build()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        and: 'a stored credential'
        prepareCredentialUserSetting()

        when: 'I obtain the credential from proxy and store it'
        authorizationCodeFlow.revoke 'jsmith'

        then: 'the request was executed with the correct parameters'
        null != urlParams
        'access4040-Token-4711' == urlParams.get('token')

        and: 'there are no UserSetting objects'
        0 == UserSetting.count()
    }

    def 'Revoke a credential of non-existing user'() {
        given: 'a mocked execution method'
        def requestExecuted = false
        ProxyRequest.metaClass.execute = { ->
            requestExecuted = true
            new ProxyResponse()
        }

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(new MockLowLevelHttpResponse())
            .build()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        and: 'a stored credential'
        prepareCredentialUserSetting()

        when: 'I obtain the credential from proxy and store it'
        authorizationCodeFlow.revoke 'jdoe'

        then: 'no request was sent to the AMC World proxy'
        !requestExecuted

        and: 'there is still one UserSetting object'
        1 == UserSetting.count()
    }

    def 'Revoke a non-existing credential of existing user'() {
        given: 'a mocked execution method'
        def requestExecuted = false
        ProxyRequest.metaClass.execute = { ->
            requestExecuted = true
            new ProxyResponse()
        }

        and: 'an authorization code flow instance'
        def transport = new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(new MockLowLevelHttpResponse())
            .build()
        def jsonFactory = GoogleSync.JSON_FACTORY
        def authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)

        and: 'a stored credential'
        prepareCredentialUserSetting()

        when: 'I obtain the credential from proxy and store it'
        authorizationCodeFlow.revoke 'bwayne'

        then: 'no request was sent to the AMC World proxy'
        !requestExecuted

        and: 'there is still one UserSetting object'
        1 == UserSetting.count()
    }


    //-- Non-public methods ---------------------

    protected String prepareCredentialJsonString(int userId = 1) {
        def buf = new StringBuilder('{"accessToken":"access4040-Token-471')
        buf << userId
        buf << '","refreshToken":"refresh8403%Token-204'
        buf << userId
        buf << '","expirationTimeMilliseconds":'
        buf << (System.currentTimeMillis() + 3600000L)
        buf << '}'
        buf.toString()
    }

    protected void prepareCredentialUserSetting() {
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString()
            ]
        ]
    }
}
