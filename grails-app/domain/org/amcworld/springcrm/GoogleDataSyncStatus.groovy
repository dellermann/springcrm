/*
 * GoogleDataSyncStatus.groovy
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
 * The class {@code GoogleDataSyncStatus} stores information about the
 * synchronization state of a content item with Google.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class GoogleDataSyncStatus {

    //-- Class variables ------------------------

    static constraints = {}
    static mapping = {
        type index: 'type'
    }


    //-- Instance variables ---------------------

	User user
	String type
	Long itemId
	String url
    String etag
	Date lastSync = new Date()


    //-- Public methods -------------------------

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Call) {
            return obj.user == user && obj.type == type && obj.itemId == itemId
        } else {
            return false
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode()
    }

    @Override
    String toString() {
        return "${user.userName}/${type}/${itemId}"
    }

    /**
     * Sets the synchronization status to the current date and ETag.
     *
     * @param etag  the given ETag
     */
    void updateToCurrent(String etag) {
        this.lastSync = new Date()
        this.etag = etag
    }
}
