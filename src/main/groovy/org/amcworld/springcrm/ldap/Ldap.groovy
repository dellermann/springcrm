/*
 * Ldap.groovy
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


package org.amcworld.springcrm.ldap

import groovy.transform.CompileStatic
import org.apache.directory.groovyldap.LDAP


/**
 * The class {@code Ldap} represents a subclass of {@code LDAP} which allows
 * read access to the URL, bind DN and bind password of the LDAP server.
 * <p>
 * This class is intended to be used by {@LdapFactory}, only.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 * @See     LdapFactory
 */
@CompileStatic
class Ldap extends LDAP {

    //-- Constants ------------------------------

    private static final String DEFAULT_URL = 'ldap://localhost:389/'


    //-- Fields ---------------------------------

    /**
     * The distinguished name used to bind to the LDAP server.
     */
    final String bindDn

    /**
    * The password used to bind to the LDAP server.
    */
    final String bindPasswd

    /**
     * The URL of the LDAP server.
     */
    final String url


    //-- Constructors ---------------------------

    /**
     * Creates a new instance using the default URL
     * {@code ldap://localhost:389/} and anonymous binding.
     */
    protected Ldap() {
        super(DEFAULT_URL)

        this.url = DEFAULT_URL
    }

    /**
     * Creates a new instance using the given URL and anonymous binding.
     *
     * @param url   the given URL of the LDAP server
     */
    protected Ldap(String url) {
        super(url)

        this.url = url
    }

    /**
     * Creates a new instance using the given URL, bind distinguished name
     * (bind DN) and bind password.
     *
     * @param url           the URL of the LDAP server
     * @param bindDn        the distinguished name (DN) used to bind to the
     *                      LDAP server
     * @param bindPasswd    the password used to bind to the LDAP server
     */
    protected Ldap(String url, String bindDn, String bindPasswd) {
        super(url, bindDn, bindPasswd)

        this.url = url
        this.bindDn = bindDn
        this.bindPasswd = bindPasswd
    }
}
