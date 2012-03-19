/*
 * FopServiceTests.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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
import javax.servlet.ServletContext


/**
 * The class {@code FopServiceTests} contains the unit test cases for
 * {@code FopService}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(FopService)
@Mock(FopService)
class FopServiceTests {

    //-- Public methods -------------------------

    void setUp() {
        def control = mockFor(ServletContext)
        control.demand.getResourcePaths(1) { String path ->
            def f = new File(System.getProperty('user.dir'), "web-app/${path}")
            def l = f.list()
            def res = new HashSet<String>(l.length)
            l.each { res.add("${f}/${it}/") }
            return res
        }
        control.demand.getResourceAsStream(1) { String path ->
            try {
                return new File(path).newInputStream()
            } catch (IOException e) {
                return null
            }
        }
        service.servletContext = control.createMock()
    }

    void testGetTemplateNames() {
        def names = service.templateNames
        assert 1 <= names.size()
        assert 'default' == names['default']
    }

    void testGetTemplatePathes() {
        def pathes = service.templatePathes
        assert 1 <= pathes.size()
        assert null != pathes['default']
        assert pathes['default'].startsWith('SYSTEM:')
        assert pathes['default'].endsWith('/default/')
    }

    void testGetUserTemplateDir() {
        File f = service.userTemplateDir
        assert null != f
        assert f.absolutePath.endsWith('/.springcrm/print')
    }
}
