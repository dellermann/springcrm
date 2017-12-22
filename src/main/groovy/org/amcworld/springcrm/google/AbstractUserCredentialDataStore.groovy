/*
 * AbstractUserCredentialDataStore.groovy
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

import static org.amcworld.springcrm.google.GoogleSync.JSON_FACTORY

import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.util.Preconditions
import com.google.api.client.util.store.AbstractDataStore
import com.google.api.client.util.store.DataStore
import com.google.api.client.util.store.DataStoreFactory
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.amcworld.springcrm.User
import org.amcworld.springcrm.UserSetting


/**
 * The class {@code AbstractUserCredentialDataStore} represents a data store
 * to store Google credentials in the {@code UserSetting} domain model.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
@CompileStatic
class AbstractUserCredentialDataStore
    extends AbstractDataStore<StoredCredential>
{

    //-- Constants ------------------------------

    public static final String SETTINGS_KEY = 'googleCredential'


    //-- Constructors ---------------------------

    protected AbstractUserCredentialDataStore(DataStoreFactory factory,
                                              String id)
    {
        super(factory, id)
    }


    //-- Public methods -------------------------

    @Override
    AbstractUserCredentialDataStore clear() {
        UserSetting.withTransaction {
            for (UserSetting entry : getEntries()) {
                entry.delete flush: true
            }
        }

        this
    }

    @Override
    boolean containsKey(String key) {
        if (key == null) {
            return false
        }

        boolean res = false
        UserSetting.withTransaction {
            User user = getUser(key)
            if (user) {
                res = countByUser(user) > 0
            }
        }

        res
    }

    @Override
    boolean containsValue(StoredCredential credential) {
        if (credential == null) {
            return false
        }

        boolean res
        UserSetting.withTransaction {
            String json = convertCredentialToJson(credential)
            res = countByValue(json) > 0
        }

        res
    }

    @Override
    AbstractUserCredentialDataStore delete(String key) {
        UserSetting.withTransaction {
            User user = getUser(key)
            if (user) {
                findByUser(user)?.delete flush: true
            }
        }

        this
    }

    @Override
    StoredCredential get(String key) {
        if (key == null) {
            return null
        }

        String s = null
        UserSetting.withTransaction {
            User user = getUser(key)
            if (user) {
                s = findByUser(user)?.value
            }
        }

        s ? convertJsonToCredential(s) : null
    }

    @Override
    @CompileStatic
    boolean isEmpty() {
        size() == 0
    }

    @Override
    Set<String> keySet() {
        Set<String> res = new HashSet<String>()
        UserSetting.withTransaction {
            for (UserSetting entry : getEntries()) {
                res << entry.user.username
            }
        }

        res.asImmutable()
    }

    @Override
    AbstractUserCredentialDataStore set(String key, StoredCredential credential)
    {
        Preconditions.checkNotNull key
        Preconditions.checkNotNull credential

        UserSetting.withTransaction {
            User user = getUser(key)
            if (user) {
                user.settings[SETTINGS_KEY] =
                    convertCredentialToJson(credential)
                user.save flush: true
            }
        }

        this
    }

    @Override
    int size() {
        int res
        UserSetting.withTransaction { res = count() }

        res
    }

    @Override
    Collection<StoredCredential> values() {
        Collection<StoredCredential> res = new ArrayList<StoredCredential>()
        UserSetting.withTransaction {
            for (UserSetting entry : getEntries()) {
                res << convertJsonToCredential(entry.value)
            }
        }

        res.asImmutable()
    }


    //-- Non-public methods ---------------------

    /**
     * Converts the given credential to JSON.
     *
     * @param credential    the given credential
     * @return              the JSON string
     */
    private String convertCredentialToJson(StoredCredential credential) {
        JSON_FACTORY.toString([
            accessToken: credential.accessToken,
            refreshToken: credential.refreshToken,
            expirationTimeMilliseconds: credential.expirationTimeMilliseconds
        ])
    }

    /**
     * Converts the given JSON data to a credential.
     *
     * @param json  the given JSON data
     * @return      the stored credential
     */
    private StoredCredential convertJsonToCredential(String json) {
        HashMap<String, Object> data = JSON_FACTORY.fromString(json, HashMap)

        StoredCredential credential = new StoredCredential()
        credential.accessToken = (String) data.accessToken
        credential.refreshToken = (String) data.refreshToken
        credential.expirationTimeMilliseconds =
            (Long) data.expirationTimeMilliseconds

        credential
    }

    /**
     * Counts all Google credentials stored in the database.
     *
     * @return the number of Google credentials
     * @since 2.1
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    private int count() {
        UserSetting.countByName SETTINGS_KEY
    }

    /**
     * Counts the Google credentials of the given user.
     *
     * @param user  the given user
     * @return      the number of Google credentials of the given user
     * @since 2.1
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    private int countByUser(User user) {
        UserSetting.countByUserAndName user, SETTINGS_KEY
    }

    /**
     * Counts the Google credentials with the given value.
     *
     * @param value the given value
     * @return      the number of Google credentials with the given value
     * @since 2.1
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    private int countByValue(String value) {
        UserSetting.countByNameAndValue SETTINGS_KEY, value
    }

    /**
     * Finds the user setting with the Google credential of the given user.
     *
     * @param user  the given user
     * @return      the user setting with the Google credential
     * @since 2.1
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    private UserSetting findByUser(User user) {
        UserSetting.findByUserAndName user, SETTINGS_KEY
    }

    /**
     * Gets all Google credential entries.
     *
     * @return  the credential entries
     * @since   2.0
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    private List<UserSetting> getEntries() {
        UserSetting.findAllByName SETTINGS_KEY
    }

    /**
     * Gets the user with the given name.
     *
     * @param username  the given user name
     * @return          the user object
     */
    @CompileStatic(TypeCheckingMode.SKIP)
    private User getUser(String username) {
        User.findByUserName username
    }
}
