/*
 * GoogleSyncFactory.groovy
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


/**
 * The class {@code GoogleSyncFactory} represents a factory to obtain Google
 * synchronization instances for a particular type of data and user name.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class GoogleSyncFactory {

    //-- Instance variables ---------------------

    def grailsApplication
    EnumMap<GoogleSyncType, Map<String, GoogleSync>> syncInstances =
        new EnumMap<>(GoogleSyncType)


    //-- Constructors ---------------------------

    protected GoogleSyncFactory() {}


    //-- Public methods -------------------------

    /**
     * Gets a global thread-safe instance of this class.
     */
    static GoogleSyncFactory getDefaultInstance() {
        InstanceHolder.INSTANCE
    }

    /**
     * Gets a synchronization instance for the given data type and user name.
     *
     * @param type      the given type of data that should be synchronized
     * @param userName  the given user name
     * @return          the synchronization instance
     */
    GoogleSync getSyncInstance(GoogleSyncType type, String userName) {
        Map<String, GoogleSync> userMap
        synchronized (syncInstances) {
            userMap = syncInstances[type]
            if (userMap == null) {
                userMap = new HashMap<String, GoogleSync>()
                syncInstances[type] = userMap
            }
        }

        GoogleSync sync
        synchronized (userMap) {
            sync = userMap[userName]
            if (sync == null) {
                sync = (GoogleSync) grailsApplication.mainContext.getBean(
                    type.beanName
                )
                sync.userName = userName
                userMap[userName] = sync
            }
        }

        sync
    }


    //-- Inner classes --------------------------

    static class InstanceHolder {

        //-- Class variables --------------------

        static final GoogleSyncFactory INSTANCE = new GoogleSyncFactory()
    }
}
