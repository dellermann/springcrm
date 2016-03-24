/*
 * UserCredentialDataStoreSpec.groovy
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

import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.util.store.DataStore
import grails.test.mixin.Mock
import org.amcworld.springcrm.User
import org.amcworld.springcrm.UserSetting
import org.amcworld.springcrm.UserSettings
import spock.lang.Specification


@Mock([User, UserSetting])
class UserCredentialDataStoreSpec extends Specification {

    //-- Fields ---------------------------------

    DataStore dataStore
    User userBwayne
    User userJsmith


    //-- Fixture methods ------------------------

    def setup() {
        dataStore = UserCredentialDataStoreFactory
            .defaultInstance
            .createDataStore('StoredCredential')
        userJsmith = new User(
            userName: 'jsmith', password: 'secret', firstName: 'John',
            lastName: 'Smith', email: 'j.smith@example.com'
        )
        userBwayne = new User(
            userName: 'bwayne', password: 'very-secret',
            firstName: 'Barbra', lastName: 'Wayne',
            email: 'b.wayne@example.com'
        )
        mockDomain User, [userJsmith, userBwayne]
    }


    //-- Feature methods ------------------------

    def 'Store credential of existing user'() {
        given: 'a credential'
        def credential = prepareCredential()

        when: 'I store this credential'
        def res = dataStore.set('jsmith', credential)

        then: 'I get the same data store instance'
        res.is dataStore

        and: 'there is one UserSetting object with the credential'
        1 == UserSetting.countByUser(User.get(1))
        def credentialData = UserSetting.findByName(SETTINGS_KEY)
        prepareCredentialJsonString() == credentialData.value

        and: 'there is no UserSetting object for the other user'
        0 == UserSetting.countByUser(User.get(2))
    }

    def 'Store credential of non-existing user'() {
        given: 'a credential'
        def credential = prepareCredential()

        when: 'I store this credential'
        def res = dataStore.set('jdoe', credential)

        then: 'I get the same data store instance'
        res.is dataStore

        and: 'there are no UserSetting objects'
        0 == UserSetting.count()
    }

    def 'Load credential with null key'() {
        expect:
        null == dataStore.get(null)
    }

    def 'Load credential of existing user'() {
        given: 'credential data in user settings'
        prepareCredentialUserSetting()

        when: 'I load the credential'
        StoredCredential credential = dataStore.get('jsmith')

        then: 'I get a valid credential'
        null != credential
        'access4040$Token-4711' == credential.accessToken
        3600000L == credential.expirationTimeMilliseconds
        'refresh8403%Token-2041' == credential.refreshToken
    }

    def 'Load credential of non-existing user'() {
        given: 'credential data in user settings'
        prepareCredentialUserSetting()

        when: 'I load the credential'
        StoredCredential credential = dataStore.get('jdoe')

        then: 'I get no credential'
        null == credential
    }

    def 'Load non-existing credential of existing user'() {
        when: 'I load the credential'
        StoredCredential credential = dataStore.get('jsmith')

        then: 'I get no credential'
        null == credential
    }

    def 'Delete credential of existing user'() {
        given: 'credential data in user settings'
        prepareCredentialUserSetting()

        when: 'I delete the credential'
        def res = dataStore.delete('jsmith')

        then: 'I get the same data store instance'
        res.is dataStore

        and: 'I there are no UserSetting objects'
        0 == UserSetting.count()
    }

    def 'Delete credential of non-existing user'() {
        given: 'credential data in user settings'
        prepareCredentialUserSetting()

        when: 'I delete the credential'
        def res = dataStore.delete('jdoe')

        then: 'I get the same data store instance'
        res.is dataStore

        and: 'I there is still one UserSetting object'
        1 == UserSetting.count()
        def credentialData = UserSetting.findByName(SETTINGS_KEY)
        User.get(1) == credentialData.user
        prepareCredentialJsonString() == credentialData.value
    }

    def 'Delete non-existing credential of existing user'() {
        given: 'credential data in user settings'
        prepareCredentialUserSetting()

        when: 'I delete the credential'
        def res = dataStore.delete('bwayne')

        then: 'I get the same data store instance'
        res.is dataStore

        and: 'I there is still one UserSetting object'
        1 == UserSetting.count()
        def credentialData = UserSetting.findByName(SETTINGS_KEY)
        User.get(1) == credentialData.user
        prepareCredentialJsonString() == credentialData.value
    }

    def 'Obtain user names of two stored credentials'() {
        given: 'two stored credentials'
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(1)
            ],
            [
                user: User.get(2), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(2)
            ]
        ]

        when: 'I obtain the user names'
        Set<String> userNames = dataStore.keySet()

        then: 'I get two user names'
        2 == userNames.size()
        'jsmith' in userNames
        'bwayne' in userNames

        when: 'I try to change the set'
        userNames << 'fooBar'

        then: 'I get an exeption'
        thrown UnsupportedOperationException
    }

    def 'Obtain user names of one stored credential'() {
        given: 'two stored credentials'
        prepareCredentialUserSetting()

        when: 'I obtain the user names'
        Set<String> userNames = dataStore.keySet()

        then: 'I get one user name'
        1 == userNames.size()
        'jsmith' in userNames
    }

    def 'Obtain user names of no stored credentials'() {
        expect:
        dataStore.keySet().empty
    }

    def 'Obtain two stored credentials'() {
        given: 'two stored credentials'
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(1)
            ],
            [
                user: User.get(2), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(2)
            ]
        ]

        and: 'reset to original method'
        User.metaClass.getSettings = null

        and: 'two expected credentials'
        def p1 = prepareCredential(1)
        def p2 = prepareCredential(2)

        when: 'I obtain credentials'
        Collection<StoredCredential> credentials = dataStore.values()

        then: 'I get two credentials'
        2 == credentials.size()

        and: 'they are the same as the expected ones'
        if (credentials[0] == p1) {
            assert credentials[1] == p2
        } else if (credentials[0] == p2) {
            assert credentials[1] == p1
        } else {
            assert false
        }

        when: 'I try to change the collection'
        credentials << new StoredCredential()

        then: 'I get an exeption'
        thrown UnsupportedOperationException
    }

    def 'Obtain one stored credential'() {
        given: 'one stored credentials'
        prepareCredentialUserSetting()

        when: 'I obtain credentials'
        Collection<StoredCredential> credentials = dataStore.values()

        then: 'I get one credential'
        1 == credentials.size()
        prepareCredential() == credentials.first()
    }

    def 'Obtain no stored credentials'() {
        expect:
        dataStore.values().empty
    }

    def 'Clear all credentials'() {
        given: 'two stored credentials'
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(1)
            ],
            [
                user: User.get(2), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(2)
            ],
            [user: User.get(1), name: 'fooBarConfig', value: 'wheezy']
        ]

        when: 'I clear all credentials'
        def res = dataStore.clear()

        then: 'I get the same data store instance'
        res.is dataStore

        and: 'I there is still one UserSetting object'
        1 == UserSetting.count()

        and: 'but no credentials'
        0 == UserSetting.countByName(SETTINGS_KEY)
    }

    def 'Check for keys in two stored credentials'() {
        given: 'two stored credentials'
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(1)
            ],
            [
                user: User.get(2), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(2)
            ]
        ]

        expect:
        found == dataStore.containsKey(key)

        where:
        key         | found
        null        | false
        'jsmith'    | true
        'bwayne'    | true
        'jdoe'      | false
    }

    def 'Check for keys in one stored credential'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        expect:
        found == dataStore.containsKey(key)

        where:
        key         | found
        null        | false
        'jsmith'    | true
        'bwayne'    | false
        'jdoe'      | false
    }

    def 'Check for values in two stored credentials'() {
        given: 'two stored credentials'
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(1)
            ],
            [
                user: User.get(2), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(2)
            ]
        ]

        expect:
        found == dataStore.containsValue(credential)

        where:
        credential              | found
        null                    | false
        prepareCredential(1)    | true
        prepareCredential(2)    | true
        new StoredCredential()  | false
    }

    def 'Check for values in one stored credential'(StoredCredential credential,
                                                    boolean found)
    {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        expect:
        found == dataStore.containsValue(credential)

        where:
        credential              | found
        null                    | false
        prepareCredential(1)    | true
        prepareCredential(2)    | false
        new StoredCredential()  | false
    }

    def 'Get size of two stored credentials'() {
        given: 'two stored credentials'
        mockDomain UserSetting, [
            [
                user: User.get(1), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(1)
            ],
            [
                user: User.get(2), name: SETTINGS_KEY,
                value: prepareCredentialJsonString(2)
            ]
        ]

        expect:
        2 == dataStore.size()
        !dataStore.empty
    }

    def 'Get size of one stored credential'() {
        given: 'a stored credential'
        prepareCredentialUserSetting()

        expect:
        1 == dataStore.size()
        !dataStore.empty
    }

    def 'Get size of no stored credentials'() {
        expect:
        0 == dataStore.size()
        dataStore.empty
    }


    //-- Non-public methods ---------------------

    protected StoredCredential prepareCredential(int userId = 1) {
        new StoredCredential(
            accessToken: "access4040\$Token-471${userId}",
            expirationTimeMilliseconds: 3600000L,
            refreshToken: "refresh8403%Token-204${userId}"
        )
    }

    protected String prepareCredentialJsonString(int userId = 1) {
        def buf = new StringBuilder('{"accessToken":"access4040$Token-471')
        buf << userId
        buf << '","refreshToken":"refresh8403%Token-204'
        buf << userId
        buf << '","expirationTimeMilliseconds":3600000}'
        buf.toString()
    }

    protected void prepareCredentialUserSetting() {
        mockDomain UserSetting, [
            [
                user: userJsmith, name: SETTINGS_KEY,
                value: prepareCredentialJsonString()
            ]
        ]
    }
}
