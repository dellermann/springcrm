/*
 * UserSetting.groovy
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
 * The class {@code UserSetting} stores a setting of a particular user.  The
 * class was necessary because the default implementation with a map in class
 * {@code User} is buggy.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.2
 */
class UserSetting {

    //-- Class variables ------------------------

    static constraints = {
        name blank: false, unique: 'user'
        value nullable: true
    }


    //-- Instance variables ---------------------

    String name
    String value
    User user


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        (obj instanceof UserSetting) ? name == obj.name : false
    }

    @Override
    int hashCode() {
        name.hashCode()
    }

    @Override
    String toString() {
        "${name}: ${value}"
    }
}
