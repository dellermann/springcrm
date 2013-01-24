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
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.Select


/**
 * The class {@code OrganizationTest} represents a functional test case for the
 * organization section of SpringCRM.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class OrganizationTest extends GeneralTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        if (!name.methodName.startsWith('testCreate')) {
            prepareOrganization()
        }

        open('/', 'de')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        new Actions(driver).moveToElement(driver.findElement(By.xpath('//ul[@id="main-menu"]/li[2]/a')))
            .moveToElement(driver.findElement(By.xpath('//ul[@id="main-menu"]/li[2]/ul/li[1]/a')))
            .click()
            .perform()
    }

    @Test
    void testCreateOrganizationSuccess() {
        assert getUrl('/organization/list') == driver.currentUrl
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li/a')).click()
        assert getUrl('/organization/create?recType=0') == driver.currentUrl
        assert 'Organisation anlegen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        assert 'Neue Organisation' == driver.findElement(BY_SUBHEADER).text
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

        assert 1 == Organization.count()
    }

    @Test
    void testCreateOrganizationErrors() {
        assert getUrl('/organization/list') == driver.currentUrl
        assert 'Organisationen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li/a')).click()
        assert getUrl('/organization/create?recType=0') == driver.currentUrl
        assert 'Organisation anlegen' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        assert 'Neue Organisation' == driver.findElement(BY_SUBHEADER).text
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert getUrl('/organization/save') == driver.currentUrl
        assert checkErrorFields(['recType', 'name'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert getUrl('/organization/list') == driver.currentUrl
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Organisation anlegen' == link.text
        assert getUrl('/organization/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Organization.count()
    }

    @Test
    void testShowOrganization() {
        assert getUrl('/organization/list') == driver.currentUrl
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[2]/a')).click()
        def m = (driver.currentUrl =~ '/organization/show/(\\d+)')
        assert !!m
        int id = m[0][1] as Integer
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

        String param = "organization=${id}"
        fieldSet = getFieldset(dataSheet, 4)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/person/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Personen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/person/create?organization.id=${id}"))
        assert 'Person anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 5)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/quote/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Angebote' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/quote/create?organization.id=${id}"))
        assert 'Angebot anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 6)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/sales-order/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Verkaufsbestellungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/sales-order/create?organization.id=${id}"))
        assert 'Verkaufsbestellung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 7)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/invoice/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Rechnungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?organization.id=${id}"))
        assert 'Rechnung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 8)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/dunning/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Mahnungen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/dunning/create?organization.id=${id}"))
        assert 'Mahnung anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 9)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/credit-memo/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Gutschriften' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/create?organization.id=${id}"))
        assert 'Gutschrift anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 10)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/project/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Projekte' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/project/create?organization.id=${id}"))
        assert 'Projekt anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 11)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/document/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Dokumente' == fieldSet.findElement(By.tagName('h4')).text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 12)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/call/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Anrufe' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/call/create?organization.id=${id}"))
        assert 'Anruf anlegen' == link.text
        assert 1 == fieldSet.findElements(By.xpath('div[@class="fieldset-content"]/div[@class="empty-list-inline"]')).size()

        fieldSet = getFieldset(dataSheet, 13)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/note/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Notizen' == fieldSet.findElement(By.tagName('h4')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="menu"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/note/create?organization.id=${id}"))
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
        assert getUrl("/organization/edit/${id}") == link.getAttribute('href')
        assert 'Bearbeiten' == link.text
        link = toolbar.findElement(By.xpath('li[4]/a'))
        assert 'blue' == link.getAttribute('class')
        assert getUrl("/organization/copy/${id}") == link.getAttribute('href')
        assert 'Kopieren' == link.text
        link = toolbar.findElement(By.xpath('li[5]/a'))
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert getUrl("/organization/delete/${id}") == link.getAttribute('href')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl("/organization/show/${id}?type=") == driver.currentUrl

        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
        link = actions.findElement(By.xpath('li[1]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/call/create?organization.id=${id}"))
        assert 'Anruf anlegen' == link.text
        link = actions.findElement(By.xpath('li[2]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/quote/create?organization.id=${id}"))
        assert 'Angebot anlegen' == link.text
        link = actions.findElement(By.xpath('li[3]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?organization.id=${id}"))
        assert 'Rechnung anlegen' == link.text
        driver.quit()

        assert 1 == Organization.count()
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
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'O-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
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
        assert link.getAttribute('href').startsWith(getUrl('/organization/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/organization/list') == driver.currentUrl
        driver.quit()

        assert 1 == Organization.count()
    }

    @Test
    void testEditOrganization() {
        assert getUrl('/organization/list') == driver.currentUrl
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert driver.currentUrl.startsWith(getUrl('/organization/edit/'))
        assert 'Organisation bearbeiten' == driver.title
        assert 'Organisationen' == driver.findElement(BY_HEADER).text
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(BY_SUBHEADER).text
        def col = driver.findElement(By.xpath('//form[@id="organization-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('O-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert null != driver.findElement(By.id('rec-type-1')).getAttribute('checked')
        assert 'Landschaftsbau Duvensee GbR' == getInputValue('name')
        assert 'GbR' == getInputValue('legalForm')
        def select = new Select(driver.findElement(By.id('type')))
        assert 'Kunde' == select.firstSelectedOption.text
        select = new Select(driver.findElement(By.id('industry')))
        assert 'Umwelt' == select.firstSelectedOption.text
        select = new Select(driver.findElement(By.id('rating')))
        assert '' == select.firstSelectedOption.text
        assert '04543 31233' == getInputValue('phone')
        assert '04543 31235' == getInputValue('fax')
        assert 'info@landschaftsbau-duvensee.example' == getInputValue('email1')
        assert '' == getInputValue('email2')
        assert 'http://www.landschaftsbau-duvensee.example' == getInputValue('website')
        assert '' == getInputValue('owner')
        assert '' == getInputValue('numEmployees')
        assert 'Dörpstraat 25' == getInputValue('billingAddrStreet')
        assert '' == getInputValue('billingAddrPoBox')
        assert '23898' == getInputValue('billingAddrPostalCode')
        assert 'Duvensee' == getInputValue('billingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('billingAddrState')
        assert 'Deutschland' == getInputValue('billingAddrCountry')
        assert 'Dörpstraat 25' == getInputValue('shippingAddrStreet')
        assert '' == getInputValue('shippingAddrPoBox')
        assert '23898' == getInputValue('shippingAddrPostalCode')
        assert 'Duvensee' == getInputValue('shippingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddrState')
        assert 'Deutschland' == getInputValue('shippingAddrCountry')
        assert 'Kontakt über Peter Hermann hergestellt. Erstes Treffen am 13.06.2012.' == getInputValue('notes')

        assert 1 == Organization.count()
    }


    //-- Non-public methods ---------------------

    @Override
    protected Object getDatasets() {
        return ['test-data/install-data.xml']
    }
}
