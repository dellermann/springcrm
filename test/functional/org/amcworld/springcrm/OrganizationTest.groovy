/*
 * OrganizationTest.groovy
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
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.Select

/**
 * The class {@code OrganizationTest} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class OrganizationTest extends GeneralTestCase {

    //-- Public methods -------------------------

    @Before
    void login() {
        open('/')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        new Actions(driver).moveToElement(driver.findElement(By.xpath('//ul[@id="main-menu"]/li[2]/a')))
            .moveToElement(driver.findElement(By.xpath('//ul[@id="main-menu"]/li[2]/ul/li[1]/a')))
            .click()
            .perform()
    }

    @Test
    void testCreateOrganization() {
        assert getUrl('/organization/list') == driver.currentUrl
        assert 'Organisationen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li/a')).click()
        assert getUrl('/organization/create?recType=0') == driver.currentUrl
        assert 'Organisation anlegen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        assert 'Neue Organisation' == driver.findElement(BY_SUBHEADER).text
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert checkErrorFields(['recType', 'name'])
        driver.findElement(By.id('rec-type-1')).click()
        driver.findElement(By.name('name')).sendKeys('Landschaftsbau Duvensee GbR')
        driver.findElement(By.name('legalForm')).sendKeys('GbR')
        new Select(driver.findElement(By.id('type'))).selectByValue('100')
        new Select(driver.findElement(By.id('industry'))).selectByValue('1012')
        driver.findElement(By.name('phone')).sendKeys('04543 31233')
        driver.findElement(By.name('fax')).sendKeys('04543 31235')
        driver.findElement(By.name('email1')).sendKeys('info@landschaftsbau-duvensee.example')
        driver.findElement(By.name('website')).sendKeys('http://www.landschaftsbau-duvensee.example')
        driver.findElement(By.name('billingAddrStreet')).sendKeys('Dörpstraat 25')
        driver.findElement(By.name('billingAddrPostalCode')).sendKeys('23898')
        driver.findElement(By.name('billingAddrLocation')).sendKeys('Duvensee')
        driver.findElement(By.name('billingAddrState')).sendKeys('Schleswig-Holstein')
        driver.findElement(By.name('billingAddrCountry')).sendKeys('Deutschland')
        driver.findElement(By.name('shippingAddrStreet')).sendKeys('Dörpstraat 25')
        driver.findElement(By.name('shippingAddrPostalCode')).sendKeys('23898')
        driver.findElement(By.name('shippingAddrLocation')).sendKeys('Duvensee')
        driver.findElement(By.name('shippingAddrState')).sendKeys('Schleswig-Holstein')
        driver.findElement(By.name('shippingAddrCountry')).sendKeys('Deutschland')
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert getUrl('/organization/show/1') == driver.currentUrl
        assert 'Organisation Landschaftsbau Duvensee GbR wurde angelegt.' == driver.findElement(By.className('flash-message')).text
        driver.quit()
    }

    @Test
    void testShowOrganization() {
        assert getUrl('/organization/list') == driver.currentUrl
        assert 'Organisationen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[2]/a')).click()
        assert getUrl('/organization/show/1?type=') == driver.currentUrl
        assert 'Organisation anzeigen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(BY_SUBHEADER).text
    }
}
