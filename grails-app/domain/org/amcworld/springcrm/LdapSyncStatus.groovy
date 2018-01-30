/*
 * LdapSyncStatus.groovy
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

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code LdapSyncStatus} stores the status of the synchronization of
 * a content item with LDAP.
 *
 * @author	Daniel Ellermann
 * @version 3.0
 */
@EqualsAndHashCode(includes = ['itemId'])
class LdapSyncStatus implements GormEntity<LdapSyncStatus> {

    //-- Class fields ---------------------------

    static constraints = {}
    static mapping = {
        itemId index: true, indexAttributes: [unique: true, dropDups: true]
    }


    //-- Fields ---------------------------------

    /**
     * The distinguished name (DN) of the item which was synchronized with
     * LDAP.
     */
    String dn

    /**
     * The ID of the LDAP synchronization status instance.
     */
    ObjectId id

    /**
     * The ID of the item which was synchronized with LDAP.
     */
    ObjectId itemId

    /**
     * The timestamp of the last synchronization.
     */
    Date lastSync = new Date()


    //-- Public methods -------------------------

    @Override
    String toString() {
        "${itemId} → ${dn}"
    }
}
