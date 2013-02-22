/*
 * LdapSyncStatus.groovy
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


package org.amcworld.springcrm


/**
 * The class {@code LdapSyncStatus} stores the status of the synchronization of
 * a content item with LDAP.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class LdapSyncStatus {

    //-- Class variables ------------------------

    static constraints = {}
    static mapping = {
        itemId index: 'item_id'
    }


    //-- Instance variables ---------------------

	Long itemId
	String dn
	Date lastSync = new Date()


    //-- Public methods -------------------------

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LdapSyncStatus) {
            return obj.itemId == itemId
        } else {
            return false
        }
    }

    @Override
    public int hashCode() {
        return itemId as int
    }

    @Override
    String toString() {
        return "${itemId} → ${dn}"
    }
}
