/*
 * RoleGroup.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId


/**
 * The class {@code RoleGroup} represents a user group which has one or more
 * roles associated.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@GrailsCompileStatic
@EqualsAndHashCode(includes = 'name')
class RoleGroup implements Serializable {

    //-- Constants ------------------------------

    @SuppressWarnings("GroovyUnusedDeclaration")
    private static final long serialVersionUID = 1L


    //-- Class fields ---------------------------

    static constraints = {
        name blank: false, unique: true
    }
    static hasMany = [authorities: Role]
    static mapping = {
        name index: true, indexAttributes: [unique: true, dropDups: true]
    }


    //-- Fields ---------------------------------

    /**
     * The authorities associated to the group.
     */
    Set<Role> authorities

    /**
     * The timestamp when the user group has been created.
     */
    Date dateCreated

    /**
     * The ID of the group.
     */
    ObjectId id

    /**
     * The timestamp when the user group has been modified.
     */
    Date lastUpdated

    /**
     * The name of the group.
     */
    String name


    //-- Constructors -------------------------------

    /**
     * Creates an empty user group.
     */
    RoleGroup() {}

    /**
     * Creates a copy of the given user group.
     *
     * @param roleGroup the given user group
     */
    RoleGroup(RoleGroup roleGroup) {
        name = roleGroup.name
        authorities = new HashSet<>(roleGroup.authorities)
    }


    //-- Public methods -------------------------

    /**
     * Checks whether or not the user group represents an administrator group.
     *
     * @return  {@code true} if the user group represents administrators;
     *          {@code false} otherwise
     */
    boolean isAdministrators() {
        authorities?.any { it.authority == 'ROLE_ADMIN' } ?: false
    }

    @Override
    String toString() {
        name ?: ''
    }
}
