/*
 * ProxyAuthorizationCodeFlowSpec.groovy
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

import static org.amcworld.springcrm.google.AbstractUserCredentialDataStore.SETTINGS_KEY

import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.http.MockLowLevelHttpResponse
import grails.test.mixin.Mock
import org.amcworld.springcrm.User
import org.amcworld.springcrm.UserSetting
import org.amcworld.springcrm.google.UserCredentialDataStoreFactory.UserCredentialDataStore
import spock.lang.Specification


@Mock([User, UserSetting])
class ProxyAuthorizationCodeFlowSpec extends Specification {

    //-- Fields ---------------------------------

    ProxyAuthorizationCodeFlow authorizationCodeFlow
    JsonFactory jsonFactory = GoogleSync.JSON_FACTORY
    long timeStamp = System.currentTimeMillis()
    HttpTransport transport = new MockHttpTransport()


    //-- Fixture methods ------------------------

    def setup() {
        mockDomain User, [
            [
                username: 'jsmith', password: 'secret', firstName: 'John',
                lastName: 'Smith', email: 'j.smith@example.com'
            ],
            [
                username: 'bwayne', password: 'very-secret',
                firstName: 'Barbra', lastName: 'Wayne',
                email: 'b.wayne@example.com'
            ]
        ]

        buildCodeFlow()
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

        when: 'I create and store a credential from the token response'
        def credential = authorizationCodeFlow.createAndStoreCredential(
            tokenResponse, 'jsmith'
        )

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

        when: 'I create and store a credential from the token response'
        def credential = authorizationCodeFlow.createAndStoreCredential(
            tokenResponse, 'jdoe'
        )

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

        expect:
        null == authorizationCodeFlow.loadCredential('jdoe')
    }

    def 'Load non-existing credential of existing user'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        expect:
        null == authorizationCodeFlow.loadCredential('bwayne')
    }

    def 'Obtain credential from proxy and store it'() {
        given: 'a mocked HTTP response'
        String responseContent = '''200 OK
tokenResponse={"access_token":"access4040-Token-4711","token_type":"Bearer","expires_in":3600,"refresh_token":"refresh8403%Token-2041","created":1405420651}'''

        and: 'an authorization code flow instance'
        buildCodeFlow(responseContent)

        when: 'I obtain the credential from proxy and store it'
        def credential = authorizationCodeFlow.obtainAndStoreCredential(
            'jsmith', 'A450-8201'
        )

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

    def 'Register application instance at proxy'() {
        given: 'a mocked HTTP response'
        String responseContent = '''200 OK
url=http://www.google.com/login'''

        and: 'an authorization code flow instance'
        buildCodeFlow(responseContent)

        when: 'I obtain the credential from proxy and store it'
        def url = authorizationCodeFlow.register(
            'http://localhost:8080/springcrm'
        )

        then: 'I get a valid credential'
        'http://www.google.com/login' == url
    }

    def 'Revoke a credential of existing user'() {
        given: 'a mocked HTTP response'
        String responseContent = '200 OK'

        and: 'an authorization code flow instance'
        buildCodeFlow(responseContent)

        and: 'a stored credential'
        prepareCredentialUserSetting()

        when: 'I obtain the credential from proxy and store it'
        authorizationCodeFlow.revoke 'jsmith'

        then: 'there are no UserSetting objects'
        0 == UserSetting.count()
    }

    def 'Revoke a credential of non-existing user'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        when: 'I obtain the credential from proxy and store it'
        authorizationCodeFlow.revoke 'jdoe'

        then: 'there is still one UserSetting object'
        1 == UserSetting.count()
    }

    def 'Revoke a non-existing credential of existing user'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        when: 'I obtain the credential from proxy and store it'
        authorizationCodeFlow.revoke 'bwayne'

        then: 'there is still one UserSetting object'
        1 == UserSetting.count()
    }


    //-- Non-public methods ---------------------

    private void buildCodeFlow(String response = null) {
        if (response != null) {
            transport = buildTransport(response)
        }

        authorizationCodeFlow =
            new ProxyAuthorizationCodeFlow(transport, jsonFactory)
    }

    private HttpTransport buildTransport(String responseContent) {
        byte [] data = responseContent.bytes
        def response = new MockLowLevelHttpResponse()
            .setContent(data)
            .setContentLength(data.length)

        new MockHttpTransport.Builder()
            .setLowLevelHttpResponse(response)
            .build()
    }

    private String prepareCredentialJsonString(int userId = 1) {
        def buf = new StringBuilder('{"accessToken":"access4040-Token-471')
        buf << userId
        buf << '","refreshToken":"refresh8403%Token-204'
        buf << userId
        buf << '","expirationTimeMilliseconds":'
        buf << (System.currentTimeMillis() + 3600000L)
        buf << '}'

        buf.toString()
    }

    private void prepareCredentialUserSetting() {
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString()
            ]
        ]
    }
}
