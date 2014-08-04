/*
 * DocumentServiceSpec.groovy
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


package org.amcworld.springcrm

import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(DocumentService)
class DocumentServiceSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Get default root path'() {
        expect: 'I get a non-blank root path'
        null != service.rootPath
        '' != service.rootPath
    }

    def 'Get root path'() {
        when: 'I set various configuration values'
        service.grailsApplication.config.springcrm.dir.documents = path

        then: 'I get these values as root path'
        rootPath == service.rootPath

        where:
        path                        | rootPath
        '/tmp/springcrm'            | '/tmp/springcrm'
        '/home/jsmith/.springcrm'   | '/home/jsmith/.springcrm'
    }

    def 'Get root'() {
        when: 'I set various configuration values'
        service.grailsApplication.config.springcrm.dir.documents = path

        then: 'I get valid root objects'
        def root = service.root
        null != root
        rootUrl == root.URL.toString()

        where:
        path                            | rootUrl
        '/tmp/springcrm'                | 'file:///tmp/springcrm'
        '/home/jsmith/.springcrm'       | 'file:///home/jsmith/.springcrm'
    }
}
