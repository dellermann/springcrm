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

import javax.xml.transform.Source
import javax.xml.transform.TransformerException
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import spock.lang.Specification


class TemplateURIResolverSpec extends Specification {

    //-- Fields ---------------------------------

    ResourceLoader resourceLoader = Mock()


    //-- Feature methods ------------------------

    def 'Create an instance with an invalid path'() {
        when:
        new TemplateURIResolver(resourceLoader, null)

        then:
        thrown NullPointerException
    }

    def 'Create an instance with a valid path'(String path, String res) {
        when: 'I create a URI resolve with the given path'
        def instance = new TemplateURIResolver(resourceLoader, path)

        then: 'the user template path is correct'
        res == instance.userTemplatePath

        where:
        path                || res
        '/foo'              || '/foo'
        '/bar'              || '/bar'
        '/foo/bar'          || '/foo/bar'
        '/foo/bar/whee'     || '/foo/bar/whee'
        'foo'               || 'foo'
        'bar'               || 'bar'
        'foo/bar'           || 'foo/bar'
        'foo/bar/whee'      || 'foo/bar/whee'
        '/foo/'             || '/foo'
        '/foo//'            || '/foo'
        '/foo///////'       || '/foo'
        'foo/bar/whee/'     || 'foo/bar/whee'
        'foo/bar/whee//'    || 'foo/bar/whee'
        'foo/bar/whee/////' || 'foo/bar/whee'
    }

    def 'Resolve a user-defined path'() {
        given: 'a mocked user defined template folder'
        String templateDir = '/path/to/template'

        and: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> true
        1 * res.inputStream >> new ByteArrayInputStream()

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, templateDir)

        when: 'I resolve a user-defined path'
        Source s = instance.resolve('user-template:whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('/path/to/template/whee.dat') >> res

        and: 'the source is valid'
        s != null
    }

    def 'Resolve a deep user-defined path'() {
        given: 'a mocked user defined template folder'
        String templateDir = '/path/to/template'

        and: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> true
        1 * res.inputStream >> new ByteArrayInputStream()

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, templateDir)

        when: 'I resolve a user-defined path'
        Source s = instance.resolve('user-template:bazz/whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('/path/to/template/bazz/whee.dat') >> res

        and: 'the source is valid'
        s != null
    }

    def 'Resolve a user-defined path with leading slashes'() {
        given: 'a mocked user defined template folder'
        String templateDir = '/path/to/template'

        and: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> true
        1 * res.inputStream >> new ByteArrayInputStream()

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, templateDir)

        when: 'I resolve a user-defined path'
        Source s = instance.resolve('user-template:////bazz/whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('/path/to/template/bazz/whee.dat') >> res

        and: 'the source is valid'
        s != null
    }

    def 'Resolve a non-existing user-defined path'() {
        given: 'a mocked user defined template folder'
        String templateDir = '/path/to/template'

        and: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> false
        0 * res.inputStream

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, templateDir)

        when: 'I resolve a user-defined path'
        instance.resolve('user-template:whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('/path/to/template/whee.dat') >> res

        and: 'an exception is thrown'
        thrown TransformerException
    }

    def 'Resolve a system path'() {
        given: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> true
        1 * res.inputStream >> new ByteArrayInputStream()

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, '/user/dir')

        when: 'I resolve a system path'
        Source s = instance.resolve('whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('classpath:whee.dat') >> res

        and: 'the source is valid'
        s != null
    }

    def 'Resolve a deep system path'() {
        given: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> true
        1 * res.inputStream >> new ByteArrayInputStream()

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, '/user/dir')

        when: 'I resolve a system path'
        Source s = instance.resolve('bazz/whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('classpath:bazz/whee.dat') >> res

        and: 'the source is valid'
        s != null
    }

    def 'Resolve a system path with leading slash'() {
        given: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> true
        1 * res.inputStream >> new ByteArrayInputStream()

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, '/user/dir')

        when: 'I resolve a system path'
        Source s = instance.resolve('/bazz/whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('classpath:/bazz/whee.dat') >> res

        and: 'the source is valid'
        s != null
    }

    def 'Resolve a non-existing system path'() {
        given: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> false
        0 * res.inputStream

        and: 'a resolver'
        def instance = new TemplateURIResolver(resourceLoader, '/user/dir')

        when: 'I resolve a system path'
        instance.resolve('whee.dat', '/foo/bar')

        then: 'the resource is obtained correctly'
        1 * resourceLoader.getResource('classpath:whee.dat') >> res

        and: 'an exception is thrown'
        thrown TransformerException
    }
}