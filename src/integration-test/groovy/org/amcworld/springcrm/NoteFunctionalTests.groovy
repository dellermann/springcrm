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
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.openqa.selenium.By
import org.openqa.selenium.Keys


/**
 * The class {@code NoteFunctionalTests} represents a functional test case for
 * the notes section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
@RunWith(JUnit4)
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
        clickToolbarButton 0, getUrl('/note/create')
        checkTitles 'Notiz anlegen', 'Notizen', 'Neue Notiz'
        setInputValue 'title', 'Besprechung vom 21.01.2013'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        assert 'Henry Brackmann' == selectAutocompleteEx('person', 'Brack')
        setInputValue 'content', '''# Besprechung der PR-Aktion am 21.01.2013

Am 21.01.2013 trafen wir uns mit Henry Brackmann und besprachen die Vorgehensweise bei der geplanten PR-Aktion. Herr Brackmann will den Schwerpunkt auf Werbung in lokalen Medien (z. B. regionale Tageszeitungen) legen.

Wir vereinbarten folgende Vorgehensweise:

* Kalkulation des verfügbaren Werbebudgets durch Landschaftsbau Duvensee GbR
* Konzeption des Werbekonzepts
* Kostenermittlung der einzelnen Werbemöglichkeiten'''
        submitForm getUrl('/note/show/')

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
        clickToolbarButton 0, getUrl('/note/create')
        checkTitles 'Notiz anlegen', 'Notizen', 'Neue Notiz'
        submitForm getUrl('/note/save')

        assert checkErrorFields(['title'])
        cancelForm getUrl('/note/list')

        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Notiz anlegen' == link.text
        assert getUrl('/note/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == Note.count()
    }

    @Test
    void testShowNote() {
        int id = clickListItem 0, 1, '/note/show'
        checkTitles 'Notiz anzeigen', 'Notizen', 'Besprechung vom 21.01.2013'
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

        checkDefaultShowToolbar 'note', id
        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testListNotes() {
        checkTitles 'Notizen', 'Notizen'
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
        clickListActionButton 0, 0, getUrl('/note/edit/')
        checkTitles 'Notiz bearbeiten', 'Notizen', 'Besprechung vom 21.01.2013'
        def col = driver.findElement(By.xpath('//form[@id="note-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert getShowField(col, 1).text.startsWith('N-')
        assert '10000' == getInputValue('number')
        assert getInputValue('autoNumber')
        assert 'Besprechung vom 21.01.2013' == getInputValue('title')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Henry Brackmann' == driver.findElement(By.id('person')).getAttribute('value')
        assert '''# Besprechung der PR-Aktion am 21.01.2013

Am 21.01.2013 trafen wir uns mit Henry Brackmann und besprachen die
Vorgehensweise bei der geplanten PR-Aktion. Herr Brackmann will den Schwerpunkt
auf Werbung in lokalen Medien (z. B. regionale Tageszeitungen) legen.

Wir vereinbarten folgende Vorgehensweise:

* Kalkulation des verfügbaren Werbebudgets durch Landschaftsbau Duvensee GbR
* Konzeption des Werbekonzepts
* Kostenermittlung der einzelnen Werbemöglichkeiten''' == getInputValue('content')

        setInputValue 'title', 'Besprechung vom 22.01.2013'
        setInputValue 'content', '''# Besprechung der PR-Aktion am 22.01.2013

Am 22.01.2013 trafen wir uns mit Henry Brackmann und besprachen die Vorgehensweise bei der geplanten PR-Aktion. Herr Brackmann will den Schwerpunkt auf Werbung in lokalen Medien (z. B. regionale Tageszeitungen) legen.

Wir vereinbarten folgende Vorgehensweise:

1. Kalkulation des verfügbaren Werbebudgets durch Landschaftsbau Duvensee GbR
2. Konzeption des Werbekonzepts
3. Kostenermittlung der einzelnen Werbemöglichkeiten'''
        submitForm getUrl('/note/show/')

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
        assert 'Besprechung der PR-Aktion am 22.01.2013' == field.findElement(By.tagName('h1')).text
        assert field.findElement(By.tagName('p')).text.startsWith('Am 22.01.2013 trafen')
        assert 1 == field.findElements(By.tagName('ol')).size()
        assert 3 == field.findElements(By.tagName('li')).size()
        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testEditNoteErrors() {
        clickListActionButton 0, 0, getUrl('/note/edit/')
        checkTitles 'Notiz bearbeiten', 'Notizen', 'Besprechung vom 21.01.2013'

        clearInput 'title'
        submitForm getUrl('/note/update')

        assert checkErrorFields(['title'])
        cancelForm getUrl('/note/list')

        driver.quit()

        assert 1 == Note.count()
    }

    @Test
    void testDeleteNoteAction() {
        clickListActionButton 0, 1
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
        clickListActionButton 0, 1
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
}
