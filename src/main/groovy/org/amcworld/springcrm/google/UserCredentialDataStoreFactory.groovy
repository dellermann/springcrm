/*
 * UserCredentialDataStoreFactory.groovy
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


package org.amcworld.springcrm.google

import com.google.api.client.util.store.AbstractDataStoreFactory
import groovy.transform.CompileStatic
import groovy.transform.PackageScope


/**
 * The class {@code UserCredentialDataStoreFactory} represents a factory which
 * produces {@codeUserCredentialDataStore} instances to store Google
 * credentials.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
@CompileStatic
class UserCredentialDataStoreFactory extends AbstractDataStoreFactory {

    //-- Constructors ---------------------------

    @PackageScope
    UserCredentialDataStoreFactory() {}


    //-- Public methods -------------------------

    /**
     * Gets a global thread-safe instance of this class.
     *
     * @return  the instance of this class
     */
    static UserCredentialDataStoreFactory getDefaultInstance() {
        InstanceHolder.INSTANCE
    }


    //-- Non-public methods ---------------------

    @Override
    protected UserCredentialDataStore createDataStore(String id)
        throws IOException
    {
        new UserCredentialDataStore(this, id)
    }


    //-- Inner classes --------------------------

    /**
     * The inner class {@code InstanceHolder} stores the only instance of the
     * outer factory class.
     *
     * @author	Daniel Ellermann
     * @version 2.1
     * @since   1.4
     */
    private static class InstanceHolder {

        //-- Class fields -----------------------

        private static final UserCredentialDataStoreFactory INSTANCE =
            new UserCredentialDataStoreFactory()
    }

    /**
     * The inner class {@code UserCredentialDataStore} represents a data store
     * which handles user credentials.
     *
     * @author	Daniel Ellermann
     * @version 2.0
     * @since   1.4
     */
    private static class UserCredentialDataStore
        extends AbstractUserCredentialDataStore
    {

        //-- Constructors -----------------------

        /**
         * Creates a new user credential data store using the given factory and
         * ID.
         *
         * @param factory   the given factory
         * @param id        the given ID
         */
        protected UserCredentialDataStore(
            UserCredentialDataStoreFactory factory, String id
        ) {
            super(factory, id)
        }

        //-- Public methods ---------------------

        @Override
        UserCredentialDataStoreFactory getDataStoreFactory() {
            (UserCredentialDataStoreFactory) super.getDataStoreFactory()
        }
    }
}
