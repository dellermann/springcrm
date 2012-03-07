/*
 * ConfigControllerTests.groovy
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


/**
 * The class {@code ConfigControllerTests} contains the unit test cases for
 * {@code ConfigController}.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
@TestFor(ConfigController)
@Mock(Config)
class ConfigControllerTests {

    //-- Public methods -------------------------

    void testIndex() {}

    void testShowEmpty() {
        params.page = 'page1'
        controller.show()
        assert '/config/page1' == view
        assert null != model.configData
        assert model.configData.isEmpty()
    }

    void testShowNonEmpty() {
        makeConfigFixture()
        params.page = 'page2'
        controller.show()
        assert '/config/page2' == view
        assert null != model.configData
        assert 5 == model.configData.size()
        assert 'foo-value' == model.configData['foo']
        assert '15' == model.configData['int-val']
    }

    void testSave() {
        makeConfigFixture()
        params.config = [
            [key: 'foo', value: 'new-foo-value1'],
            [key: 'new-val', value: 'new']
        ]
        controller.save()
        assert 'default.updated.message' == flash.message
        assert '/config/index' == response.redirectedUrl
        def configHolder = ConfigHolder.instance
        assert 'new-foo-value1' == configHolder['foo'].value
        assert 'new' == configHolder['new-val'].value
        assert '15' == configHolder['int-val'].value
    }

    void testSaveWithReturnUrl() {
        makeConfigFixture()
        params.config = [
            [key: 'foo', value: 'new-foo-value2'],
            [key: 'xyz-val', value: 'xyz']
        ]
        params.returnUrl = '/organization/show/5'
        controller.save()
        assert 'default.updated.message' == flash.message
        assert '/organization/show/5' == response.redirectedUrl
        def configHolder = ConfigHolder.instance
        assert 'new-foo-value2' == configHolder['foo'].value
        assert 'xyz' == configHolder['xyz-val'].value
        assert '15' == configHolder['int-val'].value
    }


    //-- Non-public methods ---------------------

    protected void makeConfigFixture() {
        mockDomain(
            Config, [
                [name: 'foo', value: 'foo-value'],
                [name: 'bar', value: 'bar-value'],
                [name: 'int-val', value: '15'],
                [name: 'float-val', value: '8.45'],
                [name: 'boolean-val', value: 'true']
            ]
        )
    }
}
