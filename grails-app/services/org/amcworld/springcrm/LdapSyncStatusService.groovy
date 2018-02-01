/*
 * LdapSyncStatusService.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.gorm.services.Service
import org.bson.types.ObjectId


/**
 * The interface {@code LdapSyncStatusService} contains general methods to
 * handle synchronization states with LDAP in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(LdapSyncStatus)
interface LdapSyncStatusService {

    //-- Public methods -------------------------

    /**
     * Deletes the LDAP sync status with the given ID.
     *
     * @param id    the given ID
     */
    void delete(ObjectId id)

    /**
     * Gets the LDAP sync status by the given ID of the synchronized item.
     *
     * @param id    the given ID
     * @return      the sync status or {@code null} if no such status exist
     */
    LdapSyncStatus findByItemId(ObjectId id)

    /**
     * Saves the given LDAP sync status.
     *
     * @param status    the given LDAP sync status
     * @return          the saved instance
     */
    LdapSyncStatus save(LdapSyncStatus status)
}
