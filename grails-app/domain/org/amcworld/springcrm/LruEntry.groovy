/*
 * LruEntry.groovy
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


/**
 * The class {@code LruEntry} stores the data of an entry in the last recently
 * used (LRU) list.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class LruEntry {

    //-- Class fields ---------------------------

    static constraints = {
        controller blank: false
        itemId unique: ['user', 'controller']
        name nullable: true, blank: true
    }
    static mapping = {
        version false
    }


    //-- Fields ---------------------------------

    User user
    String controller
    long itemId
    long pos
    String name


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof LruEntry && obj.user == user &&
            obj.controller == controller && obj.itemId == itemId
    }

    @Override
    int hashCode() {
        toString().hashCode()
    }

    @Override
    String toString() {
        "${user.userName}/${controller}/${itemId}"
    }
}
