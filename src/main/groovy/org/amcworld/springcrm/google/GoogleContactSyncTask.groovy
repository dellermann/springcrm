/*
 * GoogleContactSyncTask.groovy
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

import org.amcworld.springcrm.User
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


/**
 * The class {@code GoogleContactSyncTask} represents a task which is executed
 * regularly in order to synchronize the person data with Google.  The task is
 * executed in {@link BootStrap} with the interval specified by the
 * administrator.  It checks each user which has a connection to its Google
 * account and synchronizes the person entries.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.0
 */
class GoogleContactSyncTask extends TimerTask {

    //-- Class fields ---------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Fields ---------------------------------

    GoogleContactSync googleContactSync


    //-- Public methods -------------------------

    @Override
    void run() {
        List<User> users = User.list()
        for (User user : users) {
            try {
                if (log.debugEnabled) {
                    log.debug "Syncing with Google account of user ${user} (${user.userName})…"
                }
                googleContactSync.sync user
            } catch (GoogleAuthException ignored) {
                if (log.infoEnabled) {
                    log.info "User ${user} not authenticated at Google."
                }
            } catch (e) {
                if (log.warnEnabled) {
                    log.warn "Cannot synchronize Google account for user ${user}.", e
                }
            }
        }
    }
}
