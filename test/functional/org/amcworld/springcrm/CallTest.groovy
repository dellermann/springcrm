/*
 * CallTest.groovy
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
 * The class {@code CallTest} represents a functional test case for the phone
 * call section of SpringCRM.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class CallTest extends GeneralTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        if (!name.methodName.startsWith('testCreate')) {
            prepareCall(org, p)
        }

        open('/', 'de')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open('/call/list')
    }

    @Test
    void testCreateCallSuccess() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/call/create') == driver.currentUrl
        assert 'Anruf anlegen' == driver.title
        assert 'Anrufe' == driver.findElement(BY_HEADER).text
        assert 'Neuer Anruf' == driver.findElement(BY_SUBHEADER).text
        setInputValue('subject', 'Bitte um Angebot')
        setInputValue('start_date', '13.02.2013')
        setInputValue('start_time', '09:15')
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        assert '04543 31233' == selectAutocompleteEx('phone', '04')
        new Select(getInput('status')).selectByValue('completed')
        setInputValue('notes', 'Herr Brackmann bittet um die Zusendung eines Angebots für die geplante Marketing-Aktion.')
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/call/show/'))
        assert 'Anruf Bitte um Angebot wurde angelegt.' == flashMessage
        assert 'Bitte um Angebot' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Bitte um Angebot' == getShowFieldText(col, 1)
        assert '13.02.2013 09:15' == getShowFieldText(col, 2)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        assert '04543 31233' == getShowFieldText(col, 3)
        assert 'eingehend' == getShowFieldText(col, 4)
        assert 'durchgeführt' == getShowFieldText(col, 5)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Herr Brackmann bittet um die Zusendung eines Angebots für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Call.count()
    }

    @Test
    void testCreateCallErrors() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/call/create') == driver.currentUrl
        assert 'Anruf anlegen' == driver.title
        assert 'Anrufe' == driver.findElement(BY_HEADER).text
        assert 'Neuer Anruf' == driver.findElement(BY_SUBHEADER).text
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert driver.currentUrl.startsWith(getUrl('/call/save'))
        assert checkErrorFields(['subject'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert getUrl('/call/list') == driver.currentUrl
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Anruf anlegen' == link.text
        assert getUrl('/call/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Call.count()
    }

    @Test
    void testShowCall() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[2]/a')).click()
        def m = (driver.currentUrl =~ '/call/show/(\\d+)')
        assert !!m
        int id = m[0][1] as Integer
        assert 'Anruf anzeigen' == driver.title
        assert 'Anrufe' == driver.findElement(BY_HEADER).text
        assert 'Bitte um Angebot' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Bitte um Angebot' == getShowFieldText(col, 1)
        assert '13.02.2013 09:15' == getShowFieldText(col, 2)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        assert '04543 31233' == getShowFieldText(col, 3)
        assert 'eingehend' == getShowFieldText(col, 4)
        assert 'durchgeführt' == getShowFieldText(col, 5)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Herr Brackmann bittet um die Zusendung eines Angebots für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
        link = toolbar.findElement(By.xpath('li[1]/a'))
        assert 'white' == link.getAttribute('class')
        assert getUrl('/call/list') == link.getAttribute('href')
        assert 'Liste' == link.text
        link = toolbar.findElement(By.xpath('li[2]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl('/call/create') == link.getAttribute('href')
        assert 'Anlegen' == link.text
        link = toolbar.findElement(By.xpath('li[3]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl("/call/edit/${id}") == link.getAttribute('href')
        assert 'Bearbeiten' == link.text
        link = toolbar.findElement(By.xpath('li[4]/a'))
        assert 'blue' == link.getAttribute('class')
        assert getUrl("/call/copy/${id}") == link.getAttribute('href')
        assert 'Kopieren' == link.text
        link = toolbar.findElement(By.xpath('li[5]/a'))
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert getUrl("/call/delete/${id}") == link.getAttribute('href')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl("/call/show/${id}") == driver.currentUrl
        driver.quit()

        assert 1 == Call.count()
    }

    @Test
    void testListCalls() {
        assert 'Anrufe' == driver.title
        assert 'Anrufe' == driver.findElement(BY_HEADER).text
        def link = driver.findElement(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]/a'))
        assert getUrl('/call/list?letter=B') == link.getAttribute('href')
        assert 'B' == link.text
        assert 1 == driver.findElements(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]')).size()

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/call/show/'))
        assert 'Bitte um Angebot' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('date')
        assert '13.02.2013 09:15' == td.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('status')
        assert 'eingehend' == td.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('status')
        assert 'durchgeführt' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/call/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/call/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/call/list') == driver.currentUrl
        driver.quit()

        assert 1 == Call.count()
    }

    @Test
    void testEditCallSuccess() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert driver.currentUrl.startsWith(getUrl('/call/edit/'))
        assert 'Anruf bearbeiten' == driver.title
        assert 'Anrufe' == driver.findElement(BY_HEADER).text
        assert 'Bitte um Angebot' == driver.findElement(BY_SUBHEADER).text
        def col = driver.findElement(By.xpath('//form[@id="call-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert 'Bitte um Angebot' == getInputValue('subject')
        assert '13.02.2013' == getInputValue('start_date')
        assert '09:15' == getInputValue('start_time')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert '04543 31233' == getInputValue('phone')
        def select = new Select(getInput('type'))
        def option = select.firstSelectedOption
        assert 'incoming' == option.getAttribute('value')
        assert 'eingehend' == option.text
        select = new Select(getInput('status'))
        option = select.firstSelectedOption
        assert 'completed' == option.getAttribute('value')
        assert 'durchgeführt' == option.text
        assert 'Herr Brackmann bittet um die Zusendung eines Angebots für die geplante Marketing-Aktion.' == getInputValue('notes')

        setInputValue('subject', 'Fragen zur Marketing-Aktion')
        setInputValue('start_date', '14.02.2013')
        setInputValue('start_time', '13:45')
        setInputValue('phone', '')
        assert '0163 3343267' == selectAutocompleteEx('phone', '016')
        new Select(getInput('type')).selectByValue('outgoing')
        new Select(getInput('status')).selectByValue('planned')
        setInputValue('notes', 'Sollen zur geplanten Marketing-Aktion auch Flyer gedruckt werden?')
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/call/show/'))
        assert 'Anruf Fragen zur Marketing-Aktion wurde geändert.' == flashMessage
        assert 'Fragen zur Marketing-Aktion' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Fragen zur Marketing-Aktion' == getShowFieldText(col, 1)
        assert '14.02.2013 13:45' == getShowFieldText(col, 2)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        assert '0163 3343267' == getShowFieldText(col, 3)
        assert 'ausgehend' == getShowFieldText(col, 4)
        assert 'geplant' == getShowFieldText(col, 5)
        fieldSet = getFieldset(dataSheet, 2)
        assert 'Sollen zur geplanten Marketing-Aktion auch Flyer gedruckt werden?' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == Call.count()
    }

    @Test
    void testEditCallErrors() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert driver.currentUrl.startsWith(getUrl('/call/edit/'))
        assert 'Anruf bearbeiten' == driver.title
        assert 'Anrufe' == driver.findElement(BY_HEADER).text
        assert 'Bitte um Angebot' == driver.findElement(BY_SUBHEADER).text

        driver.findElement(By.name('subject')).clear()
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert driver.currentUrl.startsWith(getUrl('/call/update'))
        assert checkErrorFields(['subject'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert driver.currentUrl.startsWith(getUrl('/call/list'))
        driver.quit()

        assert 1 == Call.count()
    }

    @Test
    void testDeleteCallAction() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/call/list'))
        assert 'Anruf wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Call.count()
    }

    @Test
    void testDeleteCallNoAction() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/call/list') == driver.currentUrl
        driver.quit()

        assert 1 == Call.count()
    }


    //-- Non-public methods ---------------------

    @Override
    protected Object getDatasets() {
        return ['test-data/install-data.xml']
    }

    protected Call prepareCall(Organization org, Person p) {
        def call = new Call(
            subject: 'Bitte um Angebot',
            start: new GregorianCalendar(2013, Calendar.FEBRUARY, 13, 9, 15, 0).time,
            organization: org,
            person: p,
            phone: '04543 31233',
            type: CallType.incoming,
            status: CallStatus.completed,
            notes: 'Herr Brackmann bittet um die Zusendung eines Angebots für die geplante Marketing-Aktion.'
        )
        call.save(flush: true)
        return call
    }
}
