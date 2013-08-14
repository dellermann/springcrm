/*
 * PersonFunctionalTests.groovy
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
import org.openqa.selenium.support.ui.Select


/**
 * The class {@code PersonFunctionalTests} represents a functional test case for the
 * person section of SpringCRM.
 *
 * @author	Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
class PersonFunctionalTests extends GeneralFunctionalTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        if (!name.methodName.startsWith('testCreate')) {
            preparePerson(org)
        }

        open('/', 'de')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open('/person/list')
    }

    @Test
    void testCreatePersonSuccess() {
        clickToolbarButton 1, getUrl('/person/create')
        checkTitles 'Person anlegen', 'Personen', 'Neue Person'
        setInputValue 'salutation.id', '1'
        setInputValue 'firstName', 'Henry'
        setInputValue 'lastName', 'Brackmann'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'department', 'Geschäftsleitung'
        setInputValue 'jobTitle', 'Geschäftsführer'
        setInputValue 'assistant', 'Anna Schmarge'
        setInputValue 'birthday_date', '14.02.1962'
        setInputValue 'phone', '04543 31233'
        setInputValue 'mobile', '0163 3343267'
        setInputValue 'fax', '04543 31235'
        setInputValue 'email1', 'h.brackmann@landschaftsbau-duvensee.example'
        setInputValue 'mailingAddrStreet', 'Dörpstraat 25'
        setInputValue 'mailingAddrPostalCode', '23898'
        setInputValue 'mailingAddrLocation', 'Duvensee'
        setInputValue 'mailingAddrState', 'Schleswig-Holstein'
        setInputValue 'mailingAddrCountry', 'Deutschland'
        submitForm getUrl('/person/show/')

        assert 'Person Brackmann, Henry wurde angelegt.' == flashMessage
        assert 'Brackmann, Henry' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'E-10000' == getShowFieldText(col, 1)
        def link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Herr' == getShowFieldText(col, 3)
        assert 'Henry' == getShowFieldText(col, 4)
        assert 'Brackmann' == getShowFieldText(col, 5)
        assert 'Geschäftsführer' == getShowFieldText(col, 6)
        assert 'Geschäftsleitung' == getShowFieldText(col, 7)
        assert 'Anna Schmarge' == getShowFieldText(col, 8)
        assert '14.02.1962' == getShowFieldText(col, 9)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04543 31233' == getShowFieldText(col, 1)
        assert '0163 3343267' == getShowFieldText(col, 3)
        assert '04543 31235' == getShowFieldText(col, 4)
        link = getShowField(col, 7).findElement(By.tagName('a'))
        assert 'mailto:h.brackmann@landschaftsbau-duvensee.example' == link.getAttribute('href')
        assert 'h.brackmann@landschaftsbau-duvensee.example' == link.text
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        driver.quit()

        assert 1 == Person.count()
    }

    @Test
    void testCreatePersonErrors() {
        clickToolbarButton 1, getUrl('/person/create')
        checkTitles 'Person anlegen', 'Personen', 'Neue Person'
        submitForm getUrl('/person/save')

        assert checkErrorFields(['firstName', 'lastName', 'organization.id'])
        cancelForm getUrl('/person/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.green'))
        assert 'Person anlegen' == link.text
        assert getUrl('/person/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Person.count()
    }

    @Test
    void testShowPerson() {
        int id = clickListItem 0, 1, '/person/show'
        checkTitles 'Person anzeigen', 'Personen', 'Brackmann, Henry'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'E-10000' == getShowFieldText(col, 1)
        def link = getShowField(col, 2).findElement(By.tagName('a'))
        def m = (link.getAttribute('href') =~ '/organization/show/(\\d+)')
        assert !!m
        int organizationId = m[0][1] as Integer
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Herr' == getShowFieldText(col, 3)
        assert 'Henry' == getShowFieldText(col, 4)
        assert 'Brackmann' == getShowFieldText(col, 5)
        assert 'Geschäftsführer' == getShowFieldText(col, 6)
        assert 'Geschäftsleitung' == getShowFieldText(col, 7)
        assert 'Anna Schmarge' == getShowFieldText(col, 8)
        assert '14.02.1962' == getShowFieldText(col, 9)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04543 31233' == getShowFieldText(col, 1)
        assert '0163 3343267' == getShowFieldText(col, 3)
        assert '04543 31235' == getShowFieldText(col, 4)
        link = getShowField(col, 7).findElement(By.tagName('a'))
        assert 'mailto:h.brackmann@landschaftsbau-duvensee.example' == link.getAttribute('href')
        assert 'h.brackmann@landschaftsbau-duvensee.example' == link.text
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text

        String param = "person=${id}"
        fieldSet = getFieldset(dataSheet, 2)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/quote/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Angebote' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/quote/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Angebot anlegen' == link.text
        assert waitForEmptyRemoteList(2)

        fieldSet = getFieldset(dataSheet, 3)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/sales-order/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Verkaufsbestellungen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/sales-order/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Verkaufsbestellung anlegen' == link.text
        assert waitForEmptyRemoteList(3)

        fieldSet = getFieldset(dataSheet, 4)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/invoice/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Rechnungen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Rechnung anlegen' == link.text
        assert waitForEmptyRemoteList(4)

        fieldSet = getFieldset(dataSheet, 5)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/dunning/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Mahnungen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/dunning/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Mahnung anlegen' == link.text
        assert waitForEmptyRemoteList(5)

        fieldSet = getFieldset(dataSheet, 6)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/credit-memo/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Gutschriften' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/credit-memo/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Gutschrift anlegen' == link.text
        assert waitForEmptyRemoteList(6)

        fieldSet = getFieldset(dataSheet, 7)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/project/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Projekte' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/project/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Projekt anlegen' == link.text
        assert waitForEmptyRemoteList(7)

        fieldSet = getFieldset(dataSheet, 8)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/call/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Anrufe' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/call/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Anruf anlegen' == link.text
        assert waitForEmptyRemoteList(8)

        fieldSet = getFieldset(dataSheet, 9)
        assert fieldSet.getAttribute('class').contains('remote-list')
        assert param == fieldSet.getAttribute('data-load-params')
        assert '/springcrm/note/list-embedded' == fieldSet.getAttribute('data-load-url')
        assert 'Notizen' == fieldSet.findElement(By.tagName('h3')).text
        link = fieldSet.findElement(By.xpath('.//div[@class="buttons"]/a'))
        assert link.getAttribute('href').startsWith(getUrl("/note/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Notiz anlegen' == link.text
        assert waitForEmptyRemoteList(9)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'person', id

        def actions = driver.findElement(By.xpath('//aside[@id="action-bar"]/ul'))
        link = actions.findElement(By.xpath('li[1]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/call/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Anruf anlegen' == link.text
        link = actions.findElement(By.xpath('li[2]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/quote/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Angebot anlegen' == link.text
        link = actions.findElement(By.xpath('li[3]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/invoice/create?person.id=${id}&organization.id=${organizationId}"))
        assert 'Rechnung anlegen' == link.text
        link = actions.findElement(By.xpath('li[4]/a'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('href').startsWith(getUrl("/person/ldapexport/${id}"))
        assert 'Nach LDAP' == link.text
        driver.quit()

        assert 1 == Person.count()
    }

    @Test
    void testListPersons() {
        checkTitles 'Personen', 'Personen'
        def link = driver.findElement(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]/a'))
        assert getUrl('/person/list?letter=B') == link.getAttribute('href')
        assert 'B' == link.text
        assert 1 == driver.findElements(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]')).size()

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'E-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('string')
        assert 'Henry' == td.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert 'tel:04543%2031233' == link.getAttribute('href')
        assert '04543 31233' == link.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert 'mailto:h.brackmann@landschaftsbau-duvensee.example' == link.getAttribute('href')
        assert 'h.brackmann@landschaftsbau-duvensee.example' == link.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/person/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/person/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/person/list') == driver.currentUrl
        driver.quit()

        assert 1 == Person.count()
    }

    @Test
    void testEditPersonSuccess() {
        clickListActionButton 0, 0, getUrl('/person/edit/')
        checkTitles 'Person bearbeiten', 'Personen', 'Brackmann, Henry'
        def col = driver.findElement(By.xpath('//form[@id="person-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('E-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        def select = new Select(getInput('salutation.id'))
        def option = select.firstSelectedOption
        assert '1' == option.getAttribute('value')
        assert 'Herr' == option.text
        assert 'Henry' == getInputValue('firstName')
        assert 'Brackmann' == getInputValue('lastName')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Geschäftsleitung' == getInputValue('department')
        assert 'Geschäftsführer' == getInputValue('jobTitle')
        assert 'Anna Schmarge' == getInputValue('assistant')
        assert '14.02.1962' == getInputValue('birthday_date')
        assert '04543 31233' == getInputValue('phone')
        assert '0163 3343267' == getInputValue('mobile')
        assert '04543 31235' == getInputValue('fax')
        assert 'h.brackmann@landschaftsbau-duvensee.example' == getInputValue('email1')
        assert 'Dörpstraat 25' == getInputValue('mailingAddrStreet')
        assert '' == getInputValue('mailingAddrPoBox')
        assert '23898' == getInputValue('mailingAddrPostalCode')
        assert 'Duvensee' == getInputValue('mailingAddrLocation')
        assert 'Schleswig-Holstein' == getInputValue('mailingAddrState')
        assert 'Deutschland' == getInputValue('mailingAddrCountry')

        select.selectByValue('2')
        setInputValue 'firstName', 'Marlen'
        setInputValue 'lastName', 'Thoss'
        setInputValue 'department', ''
        setInputValue 'jobTitle', 'Landschaftsarchitektin'
        setInputValue 'assistant', ''
        setInputValue 'birthday_date', '26.05.1970'
        setInputValue 'phone', '04543 31234'
        setInputValue 'mobile', '0170 1896043'
        setInputValue 'email1', 'm.thoss@landschaftsbau-duvensee.example'
        setInputValue 'notes', 'Häufig unterwegs; mobil anrufen.'
        submitForm getUrl('/person/show/')

        assert 'Person Thoss, Marlen wurde geändert.' == flashMessage
        assert 'Thoss, Marlen' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'E-10000' == getShowFieldText(col, 1)
        def link = getShowField(col, 2).findElement(By.tagName('a'))
        def m = (link.getAttribute('href') =~ '/organization/show/(\\d+)')
        assert !!m
        int organizationId = m[0][1] as Integer
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Frau' == getShowFieldText(col, 3)
        assert 'Marlen' == getShowFieldText(col, 4)
        assert 'Thoss' == getShowFieldText(col, 5)
        assert 'Landschaftsarchitektin' == getShowFieldText(col, 6)
        assert '26.05.1970' == getShowFieldText(col, 9)
        col = fieldSet.findElement(By.className('col-r'))
        assert '04543 31234' == getShowFieldText(col, 1)
        assert '0170 1896043' == getShowFieldText(col, 3)
        assert '04543 31235' == getShowFieldText(col, 4)
        link = getShowField(col, 7).findElement(By.tagName('a'))
        assert 'mailto:m.thoss@landschaftsbau-duvensee.example' == link.getAttribute('href')
        assert 'm.thoss@landschaftsbau-duvensee.example' == link.text
        fieldSet = dataSheet.findElement(By.xpath('section[@class="multicol-content"][1]'))
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Dörpstraat 25' == getShowFieldText(col, 1)
        assert '23898' == getShowFieldText(col, 3)
        assert 'Duvensee' == getShowFieldText(col, 4)
        assert 'Schleswig-Holstein' == getShowFieldText(col, 5)
        assert 'Deutschland' == getShowFieldText(col, 6)
        assert 'Auf der Karte zeigen' == getShowField(col, 7).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Häufig unterwegs; mobil anrufen.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Person.count()
    }

    @Test
    void testEditPersonErrors() {
        clickListActionButton 0, 0, getUrl('/person/edit/')
        checkTitles 'Person bearbeiten', 'Personen', 'Brackmann, Henry'

        clearInput 'firstName'
        clearInput 'lastName'
        driver.findElement(By.id('organization')).clear()
        submitForm getUrl('/person/update')

        assert checkErrorFields(['firstName', 'lastName', 'organization.id'])
        cancelForm getUrl('/person/list')

        driver.quit()

        assert 1 == Person.count()
    }

    @Test
    void testDeletePersonAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/person/list'))
        assert 'Person wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Person.count()
    }

    @Test
    void testDeletePersonNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/person/list') == driver.currentUrl
        driver.quit()

        assert 1 == Person.count()
    }


    //-- Non-public methods ---------------------

    @Override
    protected Object getDatasets() {
        return ['test-data/install-data.xml']
    }
}
