/*
 * XHTMLEntityResolverSpec.groovy
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

import org.springframework.context.ApplicationContext
import org.springframework.core.io.Resource
import org.xml.sax.InputSource
import spock.lang.Specification


class XHTMLEntityResolverSpec extends Specification {

    //-- Fields ---------------------------------

    XHTMLEntityResolver instance = new XHTMLEntityResolver()


    //-- Feature methods ------------------------

    def 'Resolve unknown entities'(String publicId) {
        expect:
        null == instance.resolveEntity(publicId, '')

        where:
        publicId << [
            null, '', 'foo', '//bar/',
            '-//W3C//ENTITIES Latin 1 for XHTML//DE',
            '-//W3C//ENTITIES Latin 2 for XHTML//EN'
        ]
    }

    def 'Resolve a non-existing entity'() {
        given: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> false

        and: 'a mocked application context'
        ApplicationContext ctx = Mock()
        1 * ctx.getResource('classpath:public/print/dtd/xhtml-special.ent') >> res
        instance.applicationContext = ctx

        expect:
        null == instance.resolveEntity('-//W3C//ENTITIES Special for XHTML//EN', '')
    }

    def 'Resolve an existing entity'() {
        given: 'a mocked resource'
        Resource res = Mock()
        1 * res.exists() >> true
        1 * res.inputStream >> new ByteArrayInputStream('Test data'.bytes)

        and: 'a mocked application context'
        ApplicationContext ctx = Mock()
        1 * ctx.getResource('classpath:public/print/dtd/xhtml-special.ent') >> res
        instance.applicationContext = ctx

        when: 'I resolve the entity'
        InputSource source = instance.resolveEntity(
            '-//W3C//ENTITIES Special for XHTML//EN', ''
        )

        then: 'I get a valid definition'
        'Test data' == source.byteStream.text
    }
}