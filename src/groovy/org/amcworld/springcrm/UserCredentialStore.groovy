/*
 * UserCredentialStore.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


package org.amcworld.springcrm

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.auth.oauth2.CredentialStore


class UserCredentialStore implements CredentialStore {

    //-- Public methods -------------------------

    @Override
    public boolean load(String userId, Credential credential) {
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
        return true
    }

    @Override
    public void store(String userId, Credential credential) {
        User user = getUser(userId)
        if (user) {
            user.settings['googleAccessToken'] = credential.accessToken
            user.settings['googleRefreshToken'] = credential.refreshToken
            user.settings['googleExpirationTime'] = credential.expirationTimeMilliseconds.toString()
            user.save(flush: true)
        }
    }

    @Override
    public void delete(String userId, Credential credential) {
        User user = getUser(userId)
        if (user) {
            user.settings.remove('googleAccessToken')
            user.settings.remove('googleRefreshToken')
            user.settings.remove('googleExpirationTime')
            user.save(flush: true)
        }
    }


    //-- Non-public methods ---------------------

    protected User getUser(String userName) {
        return User.findByUserName(userName)
    }
}