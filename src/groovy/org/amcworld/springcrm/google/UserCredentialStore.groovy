/*
 * UserCredentialStore.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.CredentialStore
import org.amcworld.springcrm.User


/**
 * The class {@code UserCredentialStore} represents a store to load and save
 * the OAuth2 credentials which are used to synchronize data with Google.  It
 * uses the underlying database and GORM access to manage data.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.0
 */
class UserCredentialStore implements CredentialStore {

    //-- Public methods -------------------------

    @Override
    void delete(String userId, Credential credential) {
        User user = getUser(userId)
        if (user) {
            user.settings.remove 'googleAccessToken'
            user.settings.remove 'googleRefreshToken'
            user.settings.remove 'googleExpirationTime'
        }
    }

    @Override
    boolean load(String userId, Credential credential) {
        User user = getUser(userId)
        if (!user) {
            return false
        }

        String s = user.settings['googleAccessToken']
        if (!s) {
            return false
        }
        credential.accessToken = s

        s = user.settings['googleRefreshToken']
        if (!s) {
            return false
        }
        credential.refreshToken = s

        s = user.settings['googleExpirationTime']
        if (!s) {
            return false
        }

        credential.expirationTimeMilliseconds = (s as Long)
        true
    }

    @Override
    void store(String userId, Credential credential) {
        User user = getUser(userId)
        if (user) {
            user.settings['googleAccessToken'] = credential.accessToken
            user.settings['googleRefreshToken'] = credential.refreshToken
            user.settings['googleExpirationTime'] =
                credential.expirationTimeMilliseconds.toString()
        }
    }


    //-- Non-public methods ---------------------

    protected User getUser(String userName) {
        User.findByUserName userName
    }
}