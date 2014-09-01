/*
 * GoogleContactSyncTask.groovy
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

import com.google.api.client.auth.oauth2.Credential
import org.amcworld.springcrm.GoogleOAuthService
import org.amcworld.springcrm.User
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication


/**
 * The class {@code GoogleContactSyncTask} represents a task which is executed
 * regularly in order to synchronize the person data with Google.  The task is
 * executed in {@link BootStrap} with the interval specified by the
 * administrator.  It checks each user which has a connection to its Google
 * account and synchronizes the person entries.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.0
 */
class GoogleContactSyncTask extends TimerTask {

    //-- Class variables ------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    GoogleOAuthService googleOAuthService
    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    @Override
    void run() {
        List<User> users = User.list()
        for (User user : users) {
            try {
                sync user
            } catch (e) {
                log.warn "Cannot synchronize Google account for user ${user}.", e
            }
        }
    }


    //-- Non-public methods ---------------------

    protected void sync(User user) {
        if (log.debugEnabled) {
            log.debug "Looking for Google account connection for user ${user} (${user.userName})…"
        }
        Credential credential = googleOAuthService.loadCredential(user.userName)
        if (credential) {
            log.debug '    → found'
            GoogleContactSync googleSync =
                grailsApplication.mainContext.getBean('googleContactSync')
            googleSync.userName = user.userName
            googleSync.sync()
        } else if (log.debugEnabled) {
            log.debug '    → not found'
        }
    }
}
