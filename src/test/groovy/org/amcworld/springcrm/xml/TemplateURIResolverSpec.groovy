/*
 * TemplateURIResolverSpec.groovy
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


package org.amcworld.springcrm.xml

import javax.servlet.ServletContext
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import spock.lang.Specification


class TemplateURIResolverSpec extends Specification {

    //-- Fields ---------------------------------

    ServletContext servletContext = Mock()


    //-- Feature methods ------------------------

    def 'Create an instance with an invalid path'() {
        when:
        new TemplateURIResolver(servletContext, null)

        then:
        thrown IllegalArgumentException
    }

    def 'Create an instance with a valid path'() {
        when:
        def instance = new TemplateURIResolver(servletContext, '/foo/bar')

        then:
        '/foo/bar' == instance.userTemplatePath
    }

    def 'Resolve a user-defined path'() {
        given: 'a temporary source'
        File tempDir = File.createTempDir('springcrm-test', '')
        File resource = new File(tempDir, 'whee.dat')
        resource.text = 'This is a test resource.'

        and: 'a resolver'
        def instance =
            new TemplateURIResolver(servletContext, tempDir.absolutePath)

        when: 'I resolve a user-defined path'
        Source s = instance.resolve('user-template:whee.dat', '/foo/bar')

        then:
        null != s
        s instanceof StreamSource
        'This is a test resource.' == ((StreamSource) s).inputStream.text

        cleanup:
        tempDir.deleteDir()
    }

    def 'Resolve a deep user-defined path'() {
        given: 'a temporary source'
        File tempDir = File.createTempDir('springcrm-test', '')
        File bazzDir = new File(tempDir, 'bazz')
        bazzDir.mkdir()
        File resource = new File(bazzDir, 'whee.dat')
        resource.text = 'This is a test resource.'

        and: 'a resolver'
        def instance =
            new TemplateURIResolver(servletContext, tempDir.absolutePath)

        when: 'I resolve a user-defined path'
        Source s = instance.resolve('user-template:bazz/whee.dat', '/foo/bar')

        then:
        null != s
        s instanceof StreamSource
        'This is a test resource.' == ((StreamSource) s).inputStream.text

        cleanup:
        tempDir.deleteDir()
    }

    def 'Resolve a user-defined path with leading slashes'() {
        given: 'a temporary source'
        File tempDir = File.createTempDir('springcrm-test', '')
        File bazzDir = new File(tempDir, 'bazz')
        bazzDir.mkdir()
        File resource = new File(bazzDir, 'whee.dat')
        resource.text = 'This is a test resource.'

        and: 'a resolver'
        def instance =
            new TemplateURIResolver(servletContext, tempDir.absolutePath)

        when: 'I resolve a user-defined path'
        Source s = instance.resolve(
            'user-template:////bazz/whee.dat', '/foo/bar'
        )

        then:
        null != s
        s instanceof StreamSource
        'This is a test resource.' == ((StreamSource) s).inputStream.text

        cleanup:
        tempDir.deleteDir()
    }
}