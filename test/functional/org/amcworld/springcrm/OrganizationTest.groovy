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
import org.openqa.selenium.WebElement
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
        driver.findElement(By.name('notes')).sendKeys('Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.')
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
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        assert 'Allgemeine Informationen' == fieldSet.findElement(By.tagName('h4')).text
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'O-10000' == getShowFieldText(col, 1)
        assert 'Kunde' == getShowFieldText(col, 2)
        assert 'Landschaftsbau Duvensee GbR' == getShowFieldText(col, 3)
        assert 'GbR' == getShowFieldText(col, 4)
        assert 'Kunde' == getShowFieldText(col, 5)
        assert 'Umwelt' == getShowFieldText(col, 6)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04543 31233' == getShowFieldText(col, 1)
        assert '04543 31235' == getShowFieldText(col, 2)
        def link = getShowField(col, 4).findElement(By.tagName('a'))
        assert 'mailto:info@landschaftsbau-duvensee.example' == link.getAttribute('href')
        assert 'info@landschaftsbau-duvensee.example' == link.text
        link = getShowField(col, 6).findElement(By.tagName('a'))
        assert 'http://www.landschaftsbau-duvensee.example/' == link.getAttribute('href')
        assert '_blank' == link.getAttribute('target')
        assert 'http://www.landschaftsbau-duvensee.example' == link.text
        fieldSet = dataSheet.findElement(By.xpath('div[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Rechnungsanschrift' == col.findElement(By.tagName('h4')).text
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Lieferanschrift' == col.findElement(By.tagName('h4')).text
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Bemerkungen' == fieldSet.findElement(By.tagName('h4')).text
        def notes = getShowField(fieldSet, 1)
        assert 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.' == notes.text
        assert 1 == notes.findElements(By.tagName('br')).size()

        fieldSet = getFieldset(dataSheet, 4)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/person/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Personen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/create?organization.id=1'))
        assert 'Person anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 5)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/quote/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Angebote' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/quote/create?organization.id=1'))
        assert 'Angebot anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 6)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/sales-order/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Verkaufsbestellungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/sales-order/create?organization.id=1'))
        assert 'Verkaufsbestellung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 7)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/invoice/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Rechnungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/invoice/create?organization.id=1'))
        assert 'Rechnung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 8)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/dunning/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Mahnungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/dunning/create?organization.id=1'))
        assert 'Mahnung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 9)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/credit-memo/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Gutschriften' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/credit-memo/create?organization.id=1'))
        assert 'Gutschrift anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 10)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/project/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Projekte' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/project/create?organization.id=1'))
        assert 'Projekt anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 11)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/document/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Dokumente' == fieldSet.findElement(By.tagName('h4')).text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 12)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/call/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Anrufe' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/call/create?organization.id=1'))
        assert 'Anruf anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 13)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert 'organization=1' == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/note/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Notizen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl('/note/create?organization.id=1'))
        assert 'Notiz anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
        link = toolbar.findElement(By.xpath('li[1]/a'))
        assert 'white' == link.getAttribute('class')
        assert getUrl('/organization/list?type=') == link.getAttribute('href')
        assert 'Liste' == link.text
        link = toolbar.findElement(By.xpath('li[2]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl('/organization/create') == link.getAttribute('href')
        assert 'Anlegen' == link.text
        link = toolbar.findElement(By.xpath('li[3]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl('/organization/edit/1') == link.getAttribute('href')
        assert 'Bearbeiten' == link.text
        link = toolbar.findElement(By.xpath('li[4]/a'))
        assert 'blue' == link.getAttribute('class')
        assert getUrl('/organization/copy/1') == link.getAttribute('href')
        assert 'Kopieren' == link.text
        link = toolbar.findElement(By.xpath('li[5]/a'))
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert getUrl('/organization/delete/1') == link.getAttribute('href')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/organization/show/1?type=') == driver.currentUrl

        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
        link = actions.findElement(By.xpath('li[1]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl('/call/create?organization.id=1'))
        assert 'Anruf anlegen' == link.text
        link = actions.findElement(By.xpath('li[2]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl('/quote/create?organization.id=1'))
        assert 'Angebot anlegen' == link.text
        link = actions.findElement(By.xpath('li[3]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl('/invoice/create?organization.id=1'))
        assert 'Rechnung anlegen' == link.text
        driver.quit()
    }

    @Test
    void testListOrganizations() {
        assert getUrl('/organization/list') == driver.currentUrl
        assert 'Organisationen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        def link = driver.findElement(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]/a'))
        assert getUrl('/organization/list?letter=L') == link.getAttribute('href')
        assert 'L' == link.text
        assert 1 == driver.findElements(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]')).size()

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        link = td.findElement(By.tagName('a'))
        assert getUrl('/organization/show/1?type=') == link.getAttribute('href')
        assert 'O-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert getUrl('/organization/show/1?type=') == link.getAttribute('href')
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('string')
        assert 'Dörpstraat 25, 23898 Duvensee' == td.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert 'tel:04543%2031233' == link.getAttribute('href')
        assert '04543 31233' == link.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert 'mailto:info@landschaftsbau-duvensee.example' == link.getAttribute('href')
        assert 'info@landschaftsbau-duvensee.example' == link.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert 'http://www.landschaftsbau-duvensee.example/' == link.getAttribute('href')
        assert '_blank' == link.getAttribute('target')
        assert 'www.landschaftsbau-duvensee.example' == link.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert getUrl('/organization/edit/1?listType=') == link.getAttribute('href')
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert getUrl('/organization/delete/1?type=') == link.getAttribute('href')
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/organization/list') == driver.currentUrl
        driver.quit()
    }

    @Test
    void testEditOrganization() {
        assert getUrl('/organization/list') == driver.currentUrl
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert getUrl('/organization/edit/1?listType=') == driver.currentUrl
        assert 'Organisation bearbeiten' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(BY_SUBHEADER).text
    }
}
