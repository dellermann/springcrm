/*
 * SearchServiceSpec.groovy
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


package org.amcworld.springcrm

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(SearchService)
@Mock(Config)
class SearchServiceSpec extends Specification {

    //-- Feature methods ------------------------

    void 'Can check whether search index is ready'() {
        when: 'when I check whether the search index is ready'
        boolean b = service.searchIndexReady

        then: 'it is not ready'
        !b

        when: 'the ready flag has been set and I check again'
        mockDomain(
            Config, [[name: 'searchIndexReady', value: Boolean.TRUE.toString()]]
        )
        b = service.searchIndexReady

        then: 'it is ready'
        b
    }
}
