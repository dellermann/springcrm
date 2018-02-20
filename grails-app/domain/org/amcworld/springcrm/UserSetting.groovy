/*
 * UserSetting.groovy
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


/**
 * The class {@code UserSetting} stores a setting of a particular user.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.2
 */
@EqualsAndHashCode(includes = ['user', 'name'])
class UserSetting {

    //-- Class fields ---------------------------

    static constraints = {
        name blank: false, unique: 'user'
        value nullable: true
    }
    static mapping = {
        compoundIndex(
            user: 1, name: 1, indexAttributes: [unique:true, dropDups:true]
        )
    }


    //-- Fields ---------------------------------

    /**
     * The ID of the user setting.
     */
    ObjectId id

    /**
     * The name of the user setting.
     */
    String name

    /**
     * The string representation of the user setting.
     */
    String value

    /**
     * The associated user.
     */
    User user


    //-- Public methods -------------------------

    @Override
    String toString() {
        "${user}: ${name} = ${value}".toString()
    }
}
