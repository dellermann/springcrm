/*
 * AbstractUserCredentialDataStore.groovy
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

import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.json.JsonFactory
import com.google.api.client.util.Preconditions
import com.google.api.client.util.store.AbstractDataStore
import com.google.api.client.util.store.DataStore
import com.google.api.client.util.store.DataStoreFactory
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import org.amcworld.springcrm.User
import org.amcworld.springcrm.UserSetting


/**
 * The class {@code AbstractUserCredentialDataStore} represents a data store
 * to store Google credentials in the {@code UserSetting} domain model.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class AbstractUserCredentialDataStore
    extends AbstractDataStore<StoredCredential>
{

    //-- Constants ------------------------------

    public static final String SETTINGS_KEY = 'googleCredential'


    //-- Instance variables ---------------------

    /**
     * Lock on access to the store.
     */
    private final Lock lock = new ReentrantLock()


    //-- Constructors ---------------------------

    protected AbstractUserCredentialDataStore(DataStoreFactory dataStoreFactory,
                                              String id)
    {
        super(dataStoreFactory, id)
    }


    //-- Public methods -------------------------

    @Override
    AbstractUserCredentialDataStore clear() {
        lock.lock()

        try {
            List entries = UserSetting.findAllByName(SETTINGS_KEY)
            for (UserSetting entry in entries) {
                entry.delete flush: true
            }
        } finally {
            lock.unlock()
        }

        this
    }

    @Override
    boolean containsKey(String key) {
        if (key == null) {
            return false
        }

        lock.lock()
        try {
            User user = getUser(key)
            if (!user) {
                return false
            }

            UserSetting.countByUserAndName(user, SETTINGS_KEY) > 0
        } finally {
            lock.unlock()
        }
    }

    @Override
    boolean containsValue(StoredCredential credential) {
        if (credential == null) {
            return false
        }

        lock.lock()
        try {
            String json = convertCredentialToJson(credential)
            UserSetting.countByNameAndValue(SETTINGS_KEY, json) > 0
        } finally {
            lock.unlock()
        }
    }

    @Override
    AbstractUserCredentialDataStore delete(String key) {
        lock.lock()

        try {
            User user = getUser(key)
            if (user) {
                user.settings.remove SETTINGS_KEY
            }
        } finally {
            lock.unlock()
        }

        this
    }

    @Override
    StoredCredential get(String key) {
        if (key == null) {
            return null
        }

        lock.lock()

        try {
            User user = getUser(key)
            if (!user) {
                return null
            }

            String s = user.settings[SETTINGS_KEY]
            if (!s) {
                return null
            }

            convertJsonToCredential(s)
        } finally {
            lock.unlock()
        }
    }

    @Override
    boolean isEmpty() {
        size() == 0
    }

    @Override
    Set<String> keySet() {
        lock.lock()

        try {
            Set res = new HashSet()
            List entries = UserSetting.findAllByName(SETTINGS_KEY)
            for (UserSetting entry in entries) {
                res << entry.user.userName
            }
            res.asImmutable()
        } finally {
            lock.unlock()
        }
    }

    @Override
    AbstractUserCredentialDataStore set(String key, StoredCredential credential)
    {
        Preconditions.checkNotNull key
        Preconditions.checkNotNull credential

        lock.lock()

        try {
            User user = getUser(key)
            if (user) {
                user.settings[SETTINGS_KEY] =
                    convertCredentialToJson(credential)
            }
        } finally {
            lock.unlock()
        }

        this
    }

    @Override
    int size() {
        lock.lock()

        try {
            UserSetting.countByName(SETTINGS_KEY)
        } finally {
            lock.unlock()
        }
    }

    @Override
    Collection<StoredCredential> values() {
        lock.lock()

        try {
            Collection res = new ArrayList()
            List entries = UserSetting.findAllByName(SETTINGS_KEY)
            for (UserSetting entry in entries) {
                res << convertJsonToCredential(entry.value)
            }
            res.asImmutable()
        } finally {
            lock.unlock()
        }

    }


    //-- Non-public methods ---------------------

    protected String convertCredentialToJson(StoredCredential credential) {
        jsonFactory.toString([
            accessToken: credential.accessToken,
            refreshToken: credential.refreshToken,
            expirationTimeMilliseconds: credential.expirationTimeMilliseconds
        ])
    }

    protected StoredCredential convertJsonToCredential(String json) {
        new StoredCredential(jsonFactory.fromString(json, HashMap))
    }

    protected JsonFactory getJsonFactory() {
        GoogleService.JSON_FACTORY
    }

    protected User getUser(String userName) {
        User.findByUserName userName
    }
}
