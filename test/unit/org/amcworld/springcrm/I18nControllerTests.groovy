/*
 * I18nControllerTests.groovy
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

import grails.test.mixin.TestFor
import javax.servlet.ServletContext


/**
 * The class {@code I18nControllerTests} contains the unit test cases for
 * {@code I18nController}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(I18nController)
class I18nControllerTests {

    //-- Public methods -------------------------

    void testIndex() {
        controller.servletContext.metaClass.getResourceAsStream = { String path ->
            String s = '''
//
// i18n-source.js
// Key definitions for i18n retrieval.
[
    'foo.label',
    'bar.format',
    'foo.text{day,month,today,week}'
];
'''
            return new ByteArrayInputStream(s.bytes)
        }

        controller.index()
    }
}
