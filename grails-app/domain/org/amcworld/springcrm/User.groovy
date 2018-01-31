/*
 * User.groovy
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
 * The class {@code User} represents a user which can authorize at the system.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@GrailsCompileStatic
@EqualsAndHashCode(includes = 'username')
class User implements Serializable {

    //-- Constants ------------------------------

    private static final long serialVersionUID = 1L


    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'username', 'firstName', 'lastName', 'phone', 'phoneHome', 'mobile',
        'fax', 'email'
    ].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
        username blank: false, unique: true
        password blank: false, password: true
        firstName blank: false
        lastName blank: false
        phone nullable: true, maxSize: 40
        phoneHome nullable: true, maxSize: 40
        mobile nullable: true, maxSize: 40
        fax nullable: true, maxSize: 40
        email blank: false, email: true
    }
    static hasMany = [authorities: RoleGroup]
    static mapping = {
        sort 'username'
        username index: true, indexAttributes: [unique: true, dropDups: true]
    }
    static transients = ['administrator', 'fullName']


    //-- Fields ---------------------------------

    /**
     * Whether or not this user account has expired.
     */
    boolean accountExpired

    /**
     * Whether or not this user account has been locked.
     */
    boolean accountLocked

    /**
     * The roles which have been associated to this user.
     */
    Set<RoleGroup> authorities

    /**
     * The timestamp when this user has been created.
     */
    Date dateCreated

    /**
     * The e-mail address of this user.
     */
    String email

    /**
     * Whether or not this user is enabled.
     */
    boolean enabled = true

    /**
     * The fax number of this user.
     */
    String fax

    /**
     * The first name of this user.
     */
    String firstName

    /**
     * The ID of the user.
     */
    ObjectId id

    /**
     * The last name of this user.
     */
    String lastName

    /**
     * The timestamp when this user has been modified.
     */
    Date lastUpdated

    /**
     * The mobile phone number of this user.
     */
    String mobile

    /**
     * The encrypted password of this user.
     */
    String password

    /**
     * Whether or not the password has expired.
     */
    boolean passwordExpired

    /**
     * The office phone number of this user.
     */
    String phone

    /**
     * The home phone number of this user.
     */
    String phoneHome

    /**
     * The user name of this user used for authentication.
     */
    String username


    //-- Constructors ---------------------------

    /**
     * Creates an empty user.
     */
    User() {}

    /**
     * Creates a user using the data of the given one.
     *
     * @param user  the given user
     * @since       2.0
     */
    User(User user) {
        username = user.username
        firstName = user.firstName
        lastName = user.lastName
        phone = user.phone
        phoneHome = user.phoneHome
        mobile = user.mobile
        fax = user.fax
        email = user.email
        enabled = user.enabled
        authorities = user.authorities
    }


    //-- Properties -----------------------------

    /**
     * Gets the full name of the user.  The full name consists of the first
     * and the last name of the user separated by a space character.
     *
     * @return  the full name of the user
     */
    String getFullName() {
        String fn = firstName?.trim()
        String ln = lastName?.trim()

        StringBuilder buf = new StringBuilder()
        if (fn) buf << fn
        if (fn && ln) buf << ' '
        if (ln) buf << ln

        buf.toString()
    }

    /**
     * Checks whether or not the user is an administrator.
     *
     * @return  {@code true} if the user is an administrator; {@code false}
     *          otherwise
     * @since   3.0
     */
    boolean isAdministrator() {
        authorities ? authorities.any { it.administrators } : false
    }


    //-- Public methods -------------------------

    @Override
    String toString() {
        fullName
    }
}
