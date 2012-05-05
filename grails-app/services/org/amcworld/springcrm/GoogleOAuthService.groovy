/*
 * GoogleOAuthService.groovy
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
import com.google.api.client.auth.oauth2.TokenResponse
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.calendar.CalendarScopes
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH


/**
 * The class {@code GoogleOAuthService} represents a service which performs
 * the OAuth2 authentication at the Google services.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
class GoogleOAuthService {

    //-- Instance variables ---------------------

    /**
     * The Google authorization code flow to obtain an authorization code for
     * the Google API.
     */
    protected GoogleAuthorizationCodeFlow flow


    //-- Public methods -------------------------

    /**
     * Gets the Google authorization code flow to obtain an authorization code
     * for the Google API.
     *
     * @return  the authorization code flow object which can be used to
     *          redirect the user to a authorization page
     */
    synchronized GoogleAuthorizationCodeFlow getAuthorizationCodeFlow() {
        if (this.flow == null) {
            this.flow = obtainAuthorizationCodeFlow()
        }
        return this.flow
    }

    /**
     * Gets a URL to an page where the user can authorize access to its data.
     *
     * @param redirectUri   the URI where to redirect after the user has
     *                      confirmed the access; this URI also gets the
     *                      authorization code submitted
     * @return              the URL to the authorization page
     */
    String getAuthorizationUrl(CharSequence redirectUri) {
        return authorizationCodeFlow.newAuthorizationUrl().
            setRedirectUri(redirectUri.toString()).build()
    }

    /**
     * Loads the credential for the given user.  The credential data are stored
     * in the user settings.
     *
     * @param userName  the name of the user
     * @return          the credential; {@code null} if no credential has been
     *                  stored
     */
    Credential loadCredential(CharSequence userName) {
        def credential = authorizationCodeFlow.loadCredential(userName.toString())
        if (credential) {
            if (credential.expiresInSeconds <= 0) {
                if (!credential.refreshToken()) {
                    throw new GoogleAuthException('error.googleAuthException.message.cannotRefreshAccess')
                }
            }
        }
        return credential
    }

    /**
     * Requests the access token from the given authorization code.
     *
     * @param code          the authorization code
     * @param redirectUri   the URI which was used to redirect when receiving
     *                      the authorization code
     * @return              the response containing the tokens, such as access
     *                      token and refresh token
     */
    TokenResponse requestAccessToken(CharSequence code,
                                     CharSequence redirectUri) {
        return authorizationCodeFlow.newTokenRequest(code.toString())
            .setRedirectUri(redirectUri.toString())
            .execute()
    }

    /**
     * Creates a new credential for the given user and token response and
     * stores it in the database in the user settings.
     *
     * @param userName      the name of the user
     * @param tokenResponse the response containing the access and refresh
     *                      tokens from the Google server
     * @return              the generated credential
     */
    Credential createAndStoreCredential(CharSequence userName,
                                        TokenResponse tokenResponse) {
        return authorizationCodeFlow.createAndStoreCredential(
            tokenResponse, userName.toString()
        )
    }


    //-- Non-public methods ---------------------

    /**
     * Obtains the Google authorization code flow to obtain an authorization
     * code for the Google API.  The method loads the client secrets and
     * builds the {@code GoogleAuthorizationCodeFlow} object.
     *
     * @return  the Google authorization code flow object
     */
    protected static GoogleAuthorizationCodeFlow obtainAuthorizationCodeFlow() {
        def httpTransport = new NetHttpTransport()
        def jsonFactory = new JacksonFactory()
        def clientSecretsStream = SCH.servletContext.getResourceAsStream('/WEB-INF/data/google/client_secrets.json')
        def clientSecrets = GoogleClientSecrets.load(jsonFactory, clientSecretsStream)
        return new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets,
                [CalendarScopes.CALENDAR, 'https://www.google.com/m8/feeds']
            )
            .setCredentialStore(new UserCredentialStore())
            .build()
    }
}
