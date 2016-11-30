/*
 * LdapFactory.groovy
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
import groovy.transform.PackageScope


/**
 * The class {@code LdapFactory} represents a factory which produces
 * {@code LDAP} instances.  Its purpose is to allow dependency injection when
 * working with {@code LDAP} instances which class {@code LDAP} does not
 * support.
 * <p>
 * This class is singleton. In order to use this class you have to call the
 * static method {@code getInstance} to get the only instance.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class LdapFactory {

    //-- Constructors ---------------------------

    /**
     * Creates a new LDAP factory instance.
     */
    @PackageScope
    LdapFactory() {}


    //-- Public methods -------------------------

    /**
     * Gets the instance of this factory.
     *
     * @return  the factory instance
     */
    static LdapFactory getInstance() {
        InstanceHolder.INSTANCE
    }

    /**
     * Creates a new LDAP instance to connect to an LDAP host using the given
     * data.
     *
     * @param host                      the host name of the server; must not
     *                                  be {@code null} nor empty
     * @param bindDn                    the distinguished name (DN) to bind to
     *                                  the server
     * @param bindPasswd                the password used to bind to the server
     * @param port                      the port number or {@code null} to use
     *                                  the default port
     * @param proto                     the protocol to connect to the server,
     *                                  for example {@code ldap},
     *                                  {@code ldapi} or {@code ldaps}
     * @return                          the LDAP instance
     * @throws IllegalArgumentException if the given host name is either
     *         							{@code null} or empty
     */
    Ldap newLdap(String host, String bindDn, String bindPasswd,
                 Integer port = null, String proto = 'ldap')
    {
        if (!host) {
            throw new IllegalArgumentException(
                'Parameter "host" must not be null nor empty.'
            )
        }

        StringBuilder buf = new StringBuilder(proto)
        buf << '://'
        buf << host
        if (port != null) {
            buf << ':' << port
        }

        new Ldap(buf.toString(), bindDn, bindPasswd)
    }


    //-- Inner classes --------------------------

    private static class InstanceHolder {

        //-- Constants --------------------------

        public static final LdapFactory INSTANCE = new LdapFactory()
    }
}
