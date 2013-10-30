/*
 * OrganizationFunctionalTests.groovy
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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.Select


/**
 * The class {@code OrganizationFunctionalTests} represents a functional test
 * case for the organization section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
@RunWith(JUnit4)
class OrganizationFunctionalTests extends GeneralFunctionalTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        if (!name.methodName.startsWith('testCreate')) {
            prepareOrganization()
        }

        open '/', 'de'
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open '/organization/list'
    }

    @Test
    void testCreateOrganizationSuccess() {
        clickToolbarButton 0, getUrl('/organization/create?recType=0')
        checkTitles 'Organisation anlegen', 'Organisationen', 'Neue Organisation'
        driver.findElement(By.id('rec-type-1')).click()
        setInputValue 'name', 'Landschaftsbau Duvensee GbR'
        setInputValue 'legalForm', 'GbR'
        setInputValue 'type.id', '100'
        setInputValue 'industry.id', '1012'
        setInputValue 'phone', '04543 31233'
        setInputValue 'fax', '04543 31235'
        setInputValue 'email1', 'info@landschaftsbau-duvensee.example'
        setInputValue 'website', 'http://www.landschaftsbau-duvensee.example'
        setInputValue 'billingAddr.street', 'Dörpstraat 25'
        setInputValue 'billingAddr.postalCode', '23898'
        setInputValue 'billingAddr.location', 'Duvensee'
        setInputValue 'billingAddr.state', 'Schleswig-Holstein'
        setInputValue 'billingAddr.country', 'Deutschland'
        setInputValue 'shippingAddr.street', 'Dörpstraat 25'
        setInputValue 'shippingAddr.postalCode', '23898'
        setInputValue 'shippingAddr.location', 'Duvensee'
        setInputValue 'shippingAddr.state', 'Schleswig-Holstein'
        setInputValue 'shippingAddr.country', 'Deutschland'
        setInputValue 'notes', 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.'
        submitForm getUrl('/organization/show/')

        assert 'Organisation Landschaftsbau Duvensee GbR wurde angelegt.' == flashMessage
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
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
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        def notes = getShowField(fieldSet, 1)
        assert 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.' == notes.text
        assert 1 == notes.findElements(By.tagName('br')).size()
        driver.quit()

        assert 1 == Organization.count()
    }

    @Test
    void testCreateOrganizationErrors() {
        clickToolbarButton 0, getUrl('/organization/create?recType=0')
        checkTitles 'Organisation anlegen', 'Organisationen', 'Neue Organisation'
        submitForm getUrl('/organization/save')

        assert checkErrorFields(['recType', 'name'])
        cancelForm getUrl('/organization/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Organisation anlegen' == link.text
        assert getUrl('/organization/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Organization.count()
    }

    @Test
    void testShowOrganization() {
        int id = clickListItem 0, 1, '/organization/show'
        checkTitles 'Organisation anzeigen', 'Organisationen', 'Landschaftsbau Duvensee GbR'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        assert 'Allgemeine Informationen' == fieldSet.findElement(By.tagName('h3')).text
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
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Rechnungsanschrift' == col.findElement(By.tagName('h3')).text
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Lieferanschrift' == col.findElement(By.tagName('h3')).text
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Bemerkungen' == fieldSet.findElement(By.tagName('h3')).text
        def notes = getShowField(fieldSet, 1)
        assert 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.' == notes.text
        assert 1 == notes.findElements(By.tagName('br')).size()

        String param = "organization=${id}"
        fieldSet = getFieldset(dataSheet, 4)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/person/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Personen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/person/create?organization.id=${id}"))
        assert 'Person anlegen' == link.text
        assert waitForEmptyRemoteList(4)

        fieldSet = getFieldset(dataSheet, 5)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/quote/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Angebote' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/quote/create?organization.id=${id}"))
        assert 'Angebot anlegen' == link.text
        assert waitForEmptyRemoteList(5)

        fieldSet = getFieldset(dataSheet, 6)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/sales-order/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Verkaufsbestellungen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/sales-order/create?organization.id=${id}"))
        assert 'Verkaufsbestellung anlegen' == link.text
        assert waitForEmptyRemoteList(6)

        fieldSet = getFieldset(dataSheet, 7)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/invoice/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Rechnungen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?organization.id=${id}"))
        assert 'Rechnung anlegen' == link.text
        assert waitForEmptyRemoteList(7)

        fieldSet = getFieldset(dataSheet, 8)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/dunning/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Mahnungen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/dunning/create?organization.id=${id}"))
        assert 'Mahnung anlegen' == link.text
        assert waitForEmptyRemoteList(8)

        fieldSet = getFieldset(dataSheet, 9)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/credit-memo/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Gutschriften' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/create?organization.id=${id}"))
        assert 'Gutschrift anlegen' == link.text
        assert waitForEmptyRemoteList(9)

        fieldSet = getFieldset(dataSheet, 10)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/project/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Projekte' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/project/create?organization.id=${id}"))
        assert 'Projekt anlegen' == link.text
        assert waitForEmptyRemoteList(10)

        fieldSet = getFieldset(dataSheet, 11)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/document/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Dokumente' == fieldSet.findElement(By.tagName('h3')).text
        assert waitForEmptyRemoteList(11)

        fieldSet = getFieldset(dataSheet, 12)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/call/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Anrufe' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/call/create?organization.id=${id}"))
        assert 'Anruf anlegen' == link.text
        assert waitForEmptyRemoteList(12)

        fieldSet = getFieldset(dataSheet, 13)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/note/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Notizen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/note/create?organization.id=${id}"))
        assert 'Notiz anlegen' == link.text
        assert waitForEmptyRemoteList(13)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkToolbar 'organization', [
            [
                color: 'white',
                icon: 'list',
                label: 'Liste',
                url: '/organization/list?type='
            ],
            [
                action: 'create',
                color: 'green',
                icon: 'plus',
                label: 'Anlegen'
            ],
            [
                action: 'edit',
                color: 'green',
                icon: 'edit',
                id: id,
                label: 'Bearbeiten'
            ],
            [
                action: 'copy',
                color: 'blue',
                icon: 'copy',
                id: id,
                label: 'Kopieren'
            ],
            [
                action: 'delete',
                check: {
                    it.click()
                    driver.switchTo().alert().dismiss()
                    assert getUrl("/organization/show/${id}?type=") == driver.currentUrl
                },
                color: 'red',
                cssClasses: 'delete-btn',
                icon: 'trash',
                id: id,
                label: 'Löschen'
            ]
        ]

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
        checkTitles 'Organisationen', 'Organisationen'
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
    void testEditOrganizationSuccess() {
        clickListActionButton 0, 0, getUrl('/organization/edit/')
        checkTitles 'Organisation bearbeiten', 'Organisationen', 'Landschaftsbau Duvensee GbR'
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
        assert 'Dörpstraat 25' == getInputValue('billingAddr.street')
        assert '' == getInputValue('billingAddr.poBox')
        assert '23898' == getInputValue('billingAddr.postalCode')
        assert 'Duvensee' == getInputValue('billingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('billingAddr.state')
        assert 'Deutschland' == getInputValue('billingAddr.country')
        assert 'Dörpstraat 25' == getInputValue('shippingAddr.street')
        assert '' == getInputValue('shippingAddr.poBox')
        assert '23898' == getInputValue('shippingAddr.postalCode')
        assert 'Duvensee' == getInputValue('shippingAddr.location')
        assert 'Schleswig-Holstein' == getInputValue('shippingAddr.state')
        assert 'Deutschland' == getInputValue('shippingAddr.country')
        assert 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.' == getInputValue('notes')

        driver.findElement(By.id('rec-type-1')).click()
        driver.findElement(By.id('rec-type-2')).click()
        setInputValue 'name', 'Arne Friesing'
        setInputValue 'legalForm', 'Einzelunternehmen'
        setInputValue 'type.id', '104'
        setInputValue 'industry.id', '1021'
        setInputValue 'phone', '04541 428717'
        setInputValue 'fax', '04541 428719'
        setInputValue 'email1', 'arne@friesing.example'
        setInputValue 'website', 'http://friesing.example'
        setInputValue 'numEmployees', '1'
        setInputValue 'billingAddr.street', 'Kirschenallee 17a'
        setInputValue 'billingAddr.postalCode', '23909'
        setInputValue 'billingAddr.location', 'Ratzeburg'
        setInputValue 'billingAddr.state', 'Schleswig-Holstein'
        setInputValue 'billingAddr.country', 'Deutschland'
        setInputValue 'shippingAddr.street', 'Kirschenallee 17a'
        setInputValue 'shippingAddr.postalCode', '23909'
        setInputValue 'shippingAddr.location', 'Ratzeburg'
        setInputValue 'shippingAddr.state', 'Schleswig-Holstein'
        setInputValue 'shippingAddr.country', 'Deutschland'
        setInputValue 'notes', 'Guter, zuverlässiger Designer'
        submitForm getUrl('/organization/show/')

        assert 'Organisation Arne Friesing wurde geändert.' == flashMessage
        assert 'Arne Friesing' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'O-10000' == getShowFieldText(col, 1)
        assert 'Lieferant' == getShowFieldText(col, 2)
        assert 'Arne Friesing' == getShowFieldText(col, 3)
        assert 'Einzelunternehmen' == getShowFieldText(col, 4)
        assert 'Verkäufer' == getShowFieldText(col, 5)
        assert 'Medien' == getShowFieldText(col, 6)
        assert '1' == getShowFieldText(col, 8)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04541 428717' == getShowFieldText(col, 1)
        assert '04541 428719' == getShowFieldText(col, 2)
        def link = getShowField(col, 4).findElement(By.tagName('a'))
        assert 'mailto:arne@friesing.example' == link.getAttribute('href')
        assert 'arne@friesing.example' == link.text
        link = getShowField(col, 6).findElement(By.tagName('a'))
        assert 'http://friesing.example/' == link.getAttribute('href')
        assert '_blank' == link.getAttribute('target')
        assert 'http://friesing.example' == link.text
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Kirschenallee 17a' == getShowFieldText(col, 1)
        assert '23909' == getShowFieldText(col, 3)
        assert 'Ratzeburg' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        col = fieldSet.findElement(By.className('col-r'))
        assert 'Kirschenallee 17a' == getShowFieldText(col, 1)
        assert '23909' == getShowFieldText(col, 3)
        assert 'Ratzeburg' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Guter, zuverlässiger Designer' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Organization.count()
    }

    @Test
    void testEditOrganizationErrors() {
        clickListActionButton 0, 0, getUrl('/organization/edit/')
        checkTitles 'Organisation bearbeiten', 'Organisationen', 'Landschaftsbau Duvensee GbR'

        driver.findElement(By.id('rec-type-1')).click()
        clearInput 'name'
        submitForm getUrl('/organization/update')

        assert checkErrorFields(['recType', 'name'])
        cancelForm getUrl('/organization/list')

        driver.quit()

        assert 1 == Organization.count()
    }

    @Test
    void testDeleteOrganizationAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/organization/list'))
        assert 'Organisation wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Organization.count()
    }

    @Test
    void testDeleteOrganizationNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/organization/list') == driver.currentUrl
        driver.quit()

        assert 1 == Organization.count()
    }


    //-- Non-public methods ---------------------

    @Override
    protected Object getDatasets() {
        return ['test-data/install-data.xml']
    }
}
