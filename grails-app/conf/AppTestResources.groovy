/*
 * AppTestResources.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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


/*
 * IMPLEMENTATION NOTES
 * ====================
 *
 * This resource declaration loads the `test.less` file to disable CSS
 * transformations in test cases which cause the Selenium WebDriver to not
 * working.  See `/web-app/less/test.less` for more details.
 */
environments {
    test {
        modules = {
            test {
                resource url: '/less/test.less', attrs: [media: 'all', rel: 'stylesheet/less', type: 'css'], bundle: 'core'
            }
        }
    }
}

