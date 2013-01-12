/*
 * LoginTest.groovy
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


package org.amcworld.springcrm

import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By


/**
 * The class {@code LoginTest} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class LoginTest extends GeneralTestCase {

    //-- Constants ------------------------------

    protected static final By BY_MESSAGE = By.xpath('//aside/div[@class="message"]')
    protected static final String TITLE = 'Anmeldung'


    //-- Public methods -------------------------

    @Before
    void openLoginPage() {
        open('', 'de')
    }

    @Test
    void testEmptyFieldsLogin() {
        assert getUrl('/user/login') == driver.currentUrl
        assert TITLE == driver.title
        driver.findElement(BY_LOGIN_BTN).click()
        assert getUrl('/user/login') == driver.currentUrl
        assert 'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == driver.findElement(BY_MESSAGE).text
        driver.quit()
    }

    @Test
    void testInvalidInputLogin() {
        assert getUrl('/user/login') == driver.currentUrl
        assert TITLE == driver.title
        driver.findElement(BY_USER_NAME).sendKeys('rkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()
        assert getUrl('/user/login') == driver.currentUrl
        assert 'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == driver.findElement(BY_MESSAGE).text
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1235')
        driver.findElement(BY_LOGIN_BTN).click()
        assert getUrl('/user/login') == driver.currentUrl
        assert 'Ungültiger Benutzername oder Kennwort. Bitte versuchen Sie es erneut.' == driver.findElement(BY_MESSAGE).text
        driver.quit()
    }

    @Test
    void testValidLoginAndLogout() {
        assert getUrl('/user/login') == driver.currentUrl
        assert TITLE == driver.title
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()
        assert getUrl('/') == driver.currentUrl
        assert 'SpringCRM' == driver.title
        driver.findElement(By.xpath('//aside[@id="login-area"]/p/a')).click()
        assert getUrl('/user/login') == driver.currentUrl
        assert TITLE == driver.title
        driver.quit()
    }
}
