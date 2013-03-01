/*
 * NoteFunctionalTests.groovy
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

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.openqa.selenium.By
import org.openqa.selenium.Keys


/**
 * The class {@code NoteFunctionalTests} represents a functional test case for
 * the notes section of SpringCRM.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class NoteFunctionalTests extends GeneralFunctionalTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        def p = preparePerson(org)
        if (!name.methodName.startsWith('testCreate')) {
            prepareNote(org, p)
        }

        open('/', 'de')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open('/note/list')
    }

    @After
    void deleteFixture() {
        Note.executeUpdate 'delete Note n'
    }

    @Test
    void testCreateNoteSuccess() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/note/create') == driver.currentUrl
        assert 'Notiz anlegen' == driver.title
        assert 'Notizen' == driver.findElement(BY_HEADER).text
        assert 'Neue Notiz' == driver.findElement(BY_SUBHEADER).text
        setInputValue('title', 'Besprechung vom 21.01.2013')
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        def iframeDriver = driver.switchTo().frame('note-content_ifr')
        def rte = iframeDriver.findElement(By.xpath('//body'))
        rte.sendKeys(
'''Besprechung der PR-Aktion am 21.01.2013
Am 21.01.2013 trafen wir uns mit Henry Brackmann und besprachen die Vorgehensweise bei der geplanten PR-Aktion. Herr Brackmann will den Schwerpunkt auf Werbung in lokalen Medien (z. B. regionale Tageszeitungen) legen.
Wir vereinbarten folgende Vorgehensweise:
'''
        )
        driver.switchTo().defaultContent()
        driver.findElement(By.id('note-content_bullist_action')).click()
        driver.switchTo().frame('note-content_ifr')
        rte.sendKeys(
'''Kalkulation des verfügbaren Werbebudgets durch Landschaftsbau Duvensee GbR
Konzeption des Werbekonzepts
Kostenermittlung der einzelnen Werbemöglichkeiten'''
        )
        rte.sendKeys(Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP)
        driver.switchTo().defaultContent()
        driver.findElement(By.id('note-content_formatselect_text')).click()
        driver.findElement(By.cssSelector('#menu_note-content_note-content_formatselect_menu_tbl .mce_h1 a')).click()
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/note/show/'))
        assert 'Notiz Besprechung vom 21.01.2013 wurde angelegt.' == flashMessage
        assert 'Besprechung vom 21.01.2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'N-10000' == getShowFieldText(col, 1)
        assert 'Besprechung vom 21.01.2013' == getShowFieldText(col, 2)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        fieldSet = getFieldset(dataSheet, 2)
        def field = getShowField(fieldSet, 1)
        assert 'Besprechung der PR-Aktion am 21.01.2013' == field.findElement(By.tagName('h1')).text
        assert 3 == field.findElements(By.tagName('li')).size()
        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testCreateNoteErrors() {
        driver.findElement(By.xpath('//ul[@id="toolbar"]/li[1]/a')).click()
        assert getUrl('/note/create') == driver.currentUrl
        assert 'Notiz anlegen' == driver.title
        assert 'Notizen' == driver.findElement(BY_HEADER).text
        assert 'Neue Notiz' == driver.findElement(BY_SUBHEADER).text
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert driver.currentUrl.startsWith(getUrl('/note/save'))
        assert checkErrorFields(['title'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert getUrl('/note/list') == driver.currentUrl
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.xpath('div[@class="buttons"]/a[@class="green"]'))
        assert 'Notiz anlegen' == link.text
        assert getUrl('/note/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Note.count()
    }

    @Test
    void testShowNote() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr[1]/td[2]/a')).click()
        def m = (driver.currentUrl =~ '/note/show/(\\d+)')
        assert !!m
        int id = m[0][1] as Integer
        assert 'Notiz anzeigen' == driver.title
        assert 'Notizen' == driver.findElement(BY_HEADER).text
        assert 'Besprechung vom 21.01.2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'N-10000' == getShowFieldText(col, 1)
        assert 'Besprechung vom 21.01.2013' == getShowFieldText(col, 2)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        fieldSet = getFieldset(dataSheet, 2)
        def field = getShowField(fieldSet, 1)
        assert 'Besprechung der PR-Aktion am 21.01.2013' == field.findElement(By.tagName('h1')).text
        assert 3 == field.findElements(By.tagName('li')).size()

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        def toolbar = driver.findElement(By.xpath('//ul[@id="toolbar"]'))
        link = toolbar.findElement(By.xpath('li[1]/a'))
        assert 'white' == link.getAttribute('class')
        assert getUrl('/note/list') == link.getAttribute('href')
        assert 'Liste' == link.text
        link = toolbar.findElement(By.xpath('li[2]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl('/note/create') == link.getAttribute('href')
        assert 'Anlegen' == link.text
        link = toolbar.findElement(By.xpath('li[3]/a'))
        assert 'green' == link.getAttribute('class')
        assert getUrl("/note/edit/${id}") == link.getAttribute('href')
        assert 'Bearbeiten' == link.text
        link = toolbar.findElement(By.xpath('li[4]/a'))
        assert 'blue' == link.getAttribute('class')
        assert getUrl("/note/copy/${id}") == link.getAttribute('href')
        assert 'Kopieren' == link.text
        link = toolbar.findElement(By.xpath('li[5]/a'))
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert getUrl("/note/delete/${id}") == link.getAttribute('href')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl("/note/show/${id}") == driver.currentUrl
        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testListNotes() {
        assert 'Notizen' == driver.title
        assert 'Notizen' == driver.findElement(BY_HEADER).text
        def link = driver.findElement(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]/a'))
        assert getUrl('/note/list?letter=B') == link.getAttribute('href')
        assert 'B' == link.text
        assert 1 == driver.findElements(By.xpath('//ul[@class="letter-bar"]/li[@class="available"]')).size()

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('id')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/note/show/'))
        assert 'N-10000' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('string')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/note/show/'))
        assert 'Besprechung vom 21.01.2013' == link.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/note/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/note/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/note/list') == driver.currentUrl
        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testEditNoteSuccess() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert driver.currentUrl.startsWith(getUrl('/note/edit/'))
        assert 'Notiz bearbeiten' == driver.title
        assert 'Notizen' == driver.findElement(BY_HEADER).text
        assert 'Besprechung vom 21.01.2013' == driver.findElement(BY_SUBHEADER).text
        def col = driver.findElement(By.xpath('//form[@id="note-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('N-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Besprechung vom 21.01.2013' == getInputValue('title')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')

        setInputValue('title', 'Besprechung vom 22.01.2013')
        def iframeDriver = driver.switchTo().frame('note-content_ifr')
        def rte = iframeDriver.findElement(By.xpath('//body'))
        assert 'Besprechung der PR-Aktion am 21.01.2013' == rte.findElement(By.tagName('h1')).text
        assert 3 == rte.findElements(By.tagName('li')).size()
        rte.sendKeys(Keys.DOWN, Keys.RIGHT, Keys.RIGHT, Keys.RIGHT, Keys.RIGHT, Keys.RIGHT, Keys.BACK_SPACE, '2')
        rte.sendKeys(Keys.DOWN, Keys.DOWN, Keys.DOWN)
        driver.switchTo().defaultContent()
        driver.findElement(By.id('note-content_numlist_action')).click()
        driver.switchTo().frame('note-content_ifr')
        rte.sendKeys(Keys.DOWN)
        driver.switchTo().defaultContent()
        driver.findElement(By.id('note-content_numlist_action')).click()
        driver.switchTo().frame('note-content_ifr')
        rte.sendKeys(Keys.DOWN)
        driver.switchTo().defaultContent()
        driver.findElement(By.id('note-content_numlist_action')).click()
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()

        assert driver.currentUrl.startsWith(getUrl('/note/show/'))
        assert 'Notiz Besprechung vom 22.01.2013 wurde geändert.' == flashMessage
        assert 'Besprechung vom 22.01.2013' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'N-10000' == getShowFieldText(col, 1)
        assert 'Besprechung vom 22.01.2013' == getShowFieldText(col, 2)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        link = getShowField(col, 2).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/person/show/'))
        assert 'Brackmann, Henry' == link.text
        fieldSet = getFieldset(dataSheet, 2)
        def field = getShowField(fieldSet, 1)
        assert 'Besprechung der PR-Aktion am 21.01.2013' == field.findElement(By.tagName('h1')).text
        assert field.findElement(By.tagName('p')).text.startsWith('Am 22.01.2013 trafen')
        assert 1 == field.findElements(By.tagName('ol')).size()
        assert 3 == field.findElements(By.tagName('li')).size()
        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testEditNoteErrors() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[1]')).click()
        assert driver.currentUrl.startsWith(getUrl('/note/edit/'))
        assert 'Notiz bearbeiten' == driver.title
        assert 'Notizen' == driver.findElement(BY_HEADER).text
        assert 'Besprechung vom 21.01.2013' == driver.findElement(BY_SUBHEADER).text

        driver.findElement(By.name('title')).clear()
        driver.findElement(By.cssSelector('#toolbar .submit-btn')).click()
        assert driver.currentUrl.startsWith(getUrl('/note/update'))
        assert checkErrorFields(['title'])
        driver.findElement(By.linkText('Abbruch')).click()
        assert driver.currentUrl.startsWith(getUrl('/note/list'))
        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testDeleteNoteAction() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/note/list'))
        assert 'Notiz wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == Note.count()
    }

    @Test
    void testDeleteNoteNoAction() {
        driver.findElement(By.xpath('//table[@class="content-table"]/tbody/tr/td[@class="action-buttons"]/a[2]')).click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/note/list') == driver.currentUrl
        driver.quit()

        assert 1 == Note.count()
    }


    //-- Non-public methods ---------------------

    @Override
    protected Object getDatasets() {
        return ['test-data/install-data.xml']
    }

    protected Note prepareNote(Organization org, Person p) {
        def note = new Note(
            title: 'Besprechung vom 21.01.2013',
            organization: org,
            person: p,
            content: '''<h1>Besprechung der PR-Aktion am 21.01.2013</h1>
<p>Am 21.01.2013 trafen wir uns mit Henry Brackmann und besprachen die
Vorgehensweise bei der geplanten PR-Aktion. Herr Brackmann will den Schwerpunkt
auf Werbung in lokalen Medien (z. B. regionale Tageszeitungen) legen.</p>
<p>Wir vereinbarten folgende Vorgehensweise:</p>
<ul>
  <li>Kalkulation des verfügbaren Werbebudgets durch Landschaftsbau Duvensee
  GbR</li>
  <li>Konzeption des Werbekonzepts</li>
  <li>Kostenermittlung der einzelnen Werbemöglichkeiten</li>
</ul>'''
        )
        note.save(flush: true)
        return note
    }
}
