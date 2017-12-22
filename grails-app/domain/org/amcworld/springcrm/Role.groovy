/*
 * Role.groovy
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
import groovy.transform.ToString
import org.bson.types.ObjectId


/**
 * The class {@code Role} represents various roles which control the access to
 * modules and components of the software.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@GrailsCompileStatic
@EqualsAndHashCode(includes = 'authority')
@ToString(includes = 'authority')
class Role implements Serializable {

    //-- Constants ------------------------------

    private static final long serialVersionUID = 1L


    //-- Class fields ---------------------------

    static constraints = {
        authority blank: false, unique: true
    }
    static mapping = {
        authority index: true, indexAttributes: [unique: true, dropDups: true]
    }


    //-- Fields ---------------------------------

    /**
     * The name of the role.
     */
    String authority

    /**
     * The ID of the role.
     */
    ObjectId id
}
