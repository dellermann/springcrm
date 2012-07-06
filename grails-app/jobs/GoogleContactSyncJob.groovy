/*
 * GoogleContactSyncJob.groovy
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


import com.google.api.client.auth.oauth2.Credential
import org.amcworld.springcrm.User
import org.amcworld.springcrm.google.GoogleSyncException;


/**
 * The class {@code GoogleContactSyncJob} represents a job which is executed
 * regularly in order to synchronize the person data with Google.  The job is
 * executed in {@link BootStrap} with the interval specified by the
 * administrator.  The job checks each user which has a connection to its
 * Google account and synchronizes the person entries.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
class GoogleContactSyncJob {

    //-- Class variables ------------------------

    static triggers = {}


    //-- Instance variables ---------------------

    def googleOAuthService
    def grailsApplication


    //-- Public methods -------------------------

    def execute() {
        List<User> users = User.list()
        for (User user : users) {
            log.debug "Looking for Google account connection for user ${user} (${user.userName})…"
            Credential credential = googleOAuthService.loadCredential(user.userName)
            if (credential) {
                log.debug '    → found'
                def googleSync = grailsApplication.mainContext.getBean('googleContactSync')
                googleSync.userName = user.userName
                try {
                    googleSync.sync()
                } catch (GoogleSyncException e) {
                    log.warn "Could not synchronize contacts: ${e}"
                }
            } else if (log.debugEnabled) {
                log.debug '    → not found'
            }
        }
    }
}
