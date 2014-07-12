/*
 * UserCredentialDataStoreFactory.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
import com.google.api.client.util.store.DataStore


/**
 * The class {@code UserCredentialDataStoreFactory} represents a factory which
 * produces {@codeUserCredentialDataStore} instances to store Google
 * credentials.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class UserCredentialDataStoreFactory extends AbstractDataStoreFactory {

    //-- Public methods -------------------------

    /**
     * Gets a global thread-safe instance of this class.
     */
    static UserCredentialDataStoreFactory getDefaultInstance() {
        InstanceHolder.INSTANCE
    }


    //-- Non-public methods ---------------------

    @Override
    protected <V extends Serializable> DataStore<V> createDataStore(String id)
        throws IOException
    {
        new UserCredentialDataStore(this, id)
    }


    //-- Inner classes --------------------------

    static class InstanceHolder {

        //-- Class variables --------------------

        static final UserCredentialDataStoreFactory INSTANCE =
            new UserCredentialDataStoreFactory()
    }

    static class UserCredentialDataStore
        extends AbstractUserCredentialDataStore
    {

        //-- Constructors -----------------------

        UserCredentialDataStore(UserCredentialDataStoreFactory dataStoreFactory,
                                String id)
        {
            super(dataStoreFactory, id)
        }

        //-- Public methods ---------------------

        @Override
        UserCredentialDataStoreFactory getDataStoreFactory() {
            (UserCredentialDataStoreFactory) super.getDataStoreFactory()
        }
    }
}
