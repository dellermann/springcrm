/*
 * AbstractUserCredentialDataStore.groovy
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

import static org.amcworld.springcrm.google.GoogleSync.JSON_FACTORY

import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.util.Preconditions
import com.google.api.client.util.store.AbstractDataStore
import com.google.api.client.util.store.DataStore
import com.google.api.client.util.store.DataStoreFactory
import groovy.transform.CompileStatic
import org.amcworld.springcrm.User
import org.amcworld.springcrm.UserSetting


/**
 * The class {@code AbstractUserCredentialDataStore} represents a data store
 * to store Google credentials in the {@code UserSetting} domain model.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.4
 */
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
            for (UserSetting entry in entries) {
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
                res = UserSetting.countByUserAndName(user, SETTINGS_KEY) > 0
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
            res = UserSetting.countByNameAndValue(SETTINGS_KEY, json) > 0
        }

        res
    }

    @Override
    AbstractUserCredentialDataStore delete(String key) {
        UserSetting.withTransaction {
            User user = getUser(key)
            if (user) {
                user.settings.remove SETTINGS_KEY
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
                s = user.settings[SETTINGS_KEY]
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
            for (UserSetting entry in entries) {
                res << entry.user.userName
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
            }
        }

        this
    }

    @Override
    int size() {
        int res
        UserSetting.withTransaction {
            res = UserSetting.countByName(SETTINGS_KEY)
        }

        res
    }

    @Override
    Collection<StoredCredential> values() {
        Collection<StoredCredential> res = new ArrayList<StoredCredential>()
        UserSetting.withTransaction {
            for (UserSetting entry in entries) {
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
    @CompileStatic
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
    @CompileStatic
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
     * Gets all Google credential entries.
     *
     * @return  the credential entries
     * @since   2.0
     */
    private List<UserSetting> getEntries() {
        UserSetting.findAllByName SETTINGS_KEY
    }

    /**
     * Gets the user with the given name.
     *
     * @param userName  the given user name
     * @return          the user object
     */
    private User getUser(String userName) {
        User.findByUserName userName
    }
}
