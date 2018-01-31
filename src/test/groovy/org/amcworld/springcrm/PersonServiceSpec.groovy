/*
 * PersonServiceSpec.groovy
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

import grails.test.mongodb.MongoSpec
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import java.beans.Introspector
import org.grails.datastore.mapping.services.Service
import spock.lang.Ignore


class PersonServiceSpec extends MongoSpec
    implements ServiceUnitTest<PersonService>, DataTest
{

    //-- Feature methods ------------------------

    @Ignore('Does not work because of https://github.com/grails/gorm-mongodb/issues/34')
    void 'Search for persons'() {
        given: 'an organization'
        def org1 = new Organization(recType: 1, name: 'My organization ltd.')
        def org2 = new Organization(recType: 1, name: 'Your organization ltd.')
        mockDomain Organization, [org1, org2]
        Organization.count()

        and: 'a list of persons'
        mockDomain Person, [
            new Person(firstName: 'John', lastName: 'Doe', organization: org1),
            new Person(
                firstName: 'Barbra', lastName: 'Wayne', organization: org1
            ),
            new Person(
                firstName: 'Marry', lastName: 'Johnson', organization: org2
            ),
        ]
        Person.count()

        when: 'the method is called with null values'
        List<Person> list = service.search(null, null)

        then: 'a null value is returned'
        null == list

        when: 'the method is called with a null value for organization'
        list = service.search(null, 'foo')

        then: 'a null value is returned'
        null == list

        when: 'the method is called with a search term'
        list = service.search(org1, 'ayn')

        then: 'a valid result is returned'
        null != list
        1 == list.size()
        'Wayne' == list.first().lastName
    }


    //-- Public methods -------------------------

    /*
     * XXX moved down from DataTest because line
     *
     *      Service service = (Service) dataStore.getService(serviceClass)
     *
     * throws a NoSuchMethodError when calling getService().  I really don't
     * know why.
     */
    void mockDataService(Class<?> serviceClass) {
        Service service = (Service) dataStore.getService(serviceClass)
        String name = Introspector.decapitalize(serviceClass.simpleName)
        if (!applicationContext.containsBean(name)) {
            applicationContext.beanFactory.autowireBean service
            service.datastore = dataStore
            applicationContext.beanFactory.registerSingleton name, service
        }
    }
}
