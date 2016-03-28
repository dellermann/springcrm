/*
 * LdapSyncStatus.groovy
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


package org.amcworld.springcrm

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code LdapSyncStatus} stores the status of the synchronization of
 * a content item with LDAP.
 *
 * @author	Daniel Ellermann
 * @version 2.1
 */
class LdapSyncStatus implements GormEntity<LdapSyncStatus> {

    //-- Class fields ---------------------------

    static constraints = {}
    static mapping = {
        itemId index: 'item_id'
    }


    //-- Fields ---------------------------------

    Long itemId
    String dn
    Date lastSync = new Date()


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof LdapSyncStatus && obj.itemId == itemId
    }

    @Override
    int hashCode() {
        (itemId ?: 0L) as int
    }

    @Override
    String toString() {
        "${itemId} → ${dn}"
    }
}
