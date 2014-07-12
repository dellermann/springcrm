/*
 * UserCredentialDataStoreFactorySpec.groovy
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

import spock.lang.Specification


class UserCredentialDataStoreFactorySpec extends Specification {

    //-- Feature methods ------------------------

    def 'Data store factory is singleton'() {
        when: 'I obtain an instance of the data store factory'
        def dsf1 = UserCredentialDataStoreFactory.defaultInstance

        then: 'I get a valid instance'
        null != dsf1

        when: 'I obtain another instance of this data store factory'
        def dsf2 = UserCredentialDataStoreFactory.defaultInstance

        then: 'I get the same instance'
        dsf2.is dsf1
    }

    def 'New data stores can be created'() {
        given: 'a data store factory'
        def factory = UserCredentialDataStoreFactory.defaultInstance

        when: 'I obtain a data store'
        def ds = factory.createDataStore('StoredCredential')

        then: 'I get a valid data store instance'
        null != ds
        'StoredCredential' == ds.id
        factory.is ds.dataStoreFactory
    }
}
