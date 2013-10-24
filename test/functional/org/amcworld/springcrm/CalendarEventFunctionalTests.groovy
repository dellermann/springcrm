/*
 * CalendarEventFunctionalTests.groovy
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

import java.text.DateFormat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.Select
import org.openqa.selenium.support.ui.WebDriverWait


/**
 * The class {@code CalendarEventFunctionalTests} represents a functional test
 * case for the calendar section of SpringCRM.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
@RunWith(JUnit4)
class CalendarEventFunctionalTests extends GeneralFunctionalTestCase {

    //-- Instance variables ---------------------

    @Rule
    public TestName name = new TestName()


    //-- Public methods -------------------------

    @Before
    void login() {
        def org = prepareOrganization()
        if (!name.methodName.startsWith('testCreate')) {
            prepareCalendarEvent org
        }

        open('/', 'de')
        driver.findElement(BY_USER_NAME).sendKeys('mkampe')
        driver.findElement(BY_PASSWORD).sendKeys('abc1234')
        driver.findElement(BY_LOGIN_BTN).click()

        open('/calendar-event/list')
    }

    @Test
    void testCreateCalendarEventNoRecurrenceSuccess() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'subject', 'Besprechung Werbekonzept'
        setInputValue 'start_date', '23.01.2013'
        setInputValue 'start_time', '10:00'
        setInputValue 'end_date', '23.01.2013'
        setInputValue 'end_time', '12:00'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'location', 'Büro Landschaftsbau Duvensee GbR'
        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '2h'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        setInputValue 'description', 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
        assert 'Besprechung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '23.01.2013 10:00' == getShowFieldText(col, 2)
        assert '23.01.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'keine' == getShowFieldText(fieldSet, 1)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 2 == li.size()
        assert '2 Stunden' == li[0].text
        assert '1 Tag' == li[1].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventNoRecurrenceErrors() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        submitForm getUrl('/calendar-event/save')

        assert checkErrorFields(['subject', 'start', 'start_date', 'start_time', 'end', 'end_date', 'end_time'])
        cancelForm getUrl('/calendar-event/list')
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        def link = emptyList.findElement(By.cssSelector('div.buttons > a.button'))
        assert 'Kalendereintrag anlegen' == link.text
        assert getUrl('/calendar-event/create') == link.getAttribute('href')
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence10Success() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'subject', 'Besprechung Werbekonzept'
        setInputValue 'start_date', '23.01.2013'
        setInputValue 'start_time', '10:00'
        setInputValue 'end_date', '23.01.2013'
        setInputValue 'end_time', '12:00'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'location', 'Büro Landschaftsbau Duvensee GbR'

        setInputValue 'recurrence.type', '10'
        assert getInput('recurrence-interval-10').displayed
        setInputValue 'recurrence-interval-10', '3'
        setInputValue 'recurrence.endType', 'none'
        assert !getInput('recurrence.until_date').enabled
        assert !getInput('recurrence.cnt').enabled
        setInputValue 'recurrence.endType', 'count'
        setInputValue 'recurrence.cnt', '5'

        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '2h'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        setInputValue 'description', 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
        assert 'Besprechung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '23.01.2013 10:00' == getShowFieldText(col, 2)
        assert '23.01.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'täglich' == getShowFieldText(fieldSet, 1)
        assert 'aller 3 Tage' == getShowFieldText(fieldSet, 2)
        assert 'am 04.02.2013' == getShowFieldText(fieldSet, 3)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 2 == li.size()
        assert '2 Stunden' == li[0].text
        assert '1 Tag' == li[1].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence10Errors() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'recurrence.type', '10'
        setInputValue 'recurrence.endType', 'count'
        submitForm getUrl('/calendar-event/save')

        assert checkErrorFields(['subject', 'start', 'start_date', 'start_time', 'end', 'end_date', 'end_time'])
        assert '1' == getInputValue('recurrence-interval-10')
        assert 'none' == getInputValue('recurrence.endType')
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence30Success() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'subject', 'Besprechung Werbekonzept'
        setInputValue 'start_date', '23.01.2013'
        setInputValue 'start_time', '10:00'
        setInputValue 'end_date', '23.01.2013'
        setInputValue 'end_time', '12:00'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'location', 'Büro Landschaftsbau Duvensee GbR'

        setInputValue 'recurrence.type', '30'
        assert getInput('recurrence-interval-30').displayed
        setInputValue 'recurrence-weekdays-30-3', true
        setInputValue 'recurrence-weekdays-30-5', true
        setInputValue 'recurrence-interval-30', '3'
        setInputValue 'recurrence.endType', 'count'
        setInputValue 'recurrence.cnt', '5'

        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '2h'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        setInputValue 'description', 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
        assert 'Besprechung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '24.01.2013 10:00' == getShowFieldText(col, 2)
        assert '24.01.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'wöchentlich' == getShowFieldText(fieldSet, 1)
        assert 'aller 3 Wochen am Dienstag, Donnerstag' == getShowFieldText(fieldSet, 2)
        assert 'am 07.03.2013' == getShowFieldText(fieldSet, 3)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 2 == li.size()
        assert '2 Stunden' == li[0].text
        assert '1 Tag' == li[1].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence30Errors() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'recurrence.type', '30'
        setInputValue 'recurrence.endType', 'count'
        submitForm getUrl('/calendar-event/save')

        assert checkErrorFields(['subject', 'start', 'start_date', 'start_time', 'end', 'end_date', 'end_time'])
        assert 'Mindestens ein Wochentag muss ausgewählt sein.' == driver.findElement(By.xpath('//div[@id="tabs-recurrence-type-30"]/ul[@class="field-msgs"]/li[@class="error-msg"]')).text
        assert '1' == getInputValue('recurrence-interval-30')
        assert 'none' == getInputValue('recurrence.endType')
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence40Success() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'subject', 'Besprechung Werbekonzept'
        setInputValue 'start_date', '23.01.2013'
        setInputValue 'start_time', '10:00'
        setInputValue 'end_date', '23.01.2013'
        setInputValue 'end_time', '12:00'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'location', 'Büro Landschaftsbau Duvensee GbR'

        setInputValue 'recurrence.type', '40'
        assert getInput('recurrence-interval-40').displayed
        setInputValue 'recurrence-monthDay-40', '6'
        setInputValue 'recurrence-interval-40', '2'
        setInputValue 'recurrence.endType', 'count'
        setInputValue 'recurrence.cnt', '5'

        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '2h'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        setInputValue 'description', 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
        assert 'Besprechung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '06.03.2013 10:00' == getShowFieldText(col, 2)
        assert '06.03.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'monatlich (Tag)' == getShowFieldText(fieldSet, 1)
        assert 'aller 2 Monate am 06.' == getShowFieldText(fieldSet, 2)
        assert 'am 06.11.2013' == getShowFieldText(fieldSet, 3)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 2 == li.size()
        assert '2 Stunden' == li[0].text
        assert '1 Tag' == li[1].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence40Errors() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'recurrence.type', '40'
        setInputValue 'recurrence.endType', 'count'
        submitForm getUrl('/calendar-event/save')

        assert checkErrorFields(['subject', 'start', 'start_date', 'start_time', 'end', 'end_date', 'end_time'])
        assert 'Feld darf nicht leer sein.' == driver.findElement(By.xpath('//div[@id="tabs-recurrence-type-40"]/ul[@class="field-msgs"]/li[@class="error-msg"]')).text
        assert '1' == getInputValue('recurrence-interval-40')
        assert 'none' == getInputValue('recurrence.endType')
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence50Success() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'subject', 'Besprechung Werbekonzept'
        setInputValue 'start_date', '23.01.2013'
        setInputValue 'start_time', '10:00'
        setInputValue 'end_date', '23.01.2013'
        setInputValue 'end_time', '12:00'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'location', 'Büro Landschaftsbau Duvensee GbR'

        setInputValue 'recurrence.type', '50'
        assert getInput('recurrence-interval-50').displayed
        setInputValue 'recurrence-weekdayOrd-50', '3'
        setInputValue 'recurrence-weekdays-50', '5'
        setInputValue 'recurrence-interval-50', '2'
        setInputValue 'recurrence.endType', 'count'
        setInputValue 'recurrence.cnt', '5'

        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '2h'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        setInputValue 'description', 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
        assert 'Besprechung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '21.03.2013 10:00' == getShowFieldText(col, 2)
        assert '21.03.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'monatlich (Wochentag)' == getShowFieldText(fieldSet, 1)
        assert 'aller 2 Monate am 3. Donnerstag' == getShowFieldText(fieldSet, 2)
        assert 'am 21.11.2013' == getShowFieldText(fieldSet, 3)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 2 == li.size()
        assert '2 Stunden' == li[0].text
        assert '1 Tag' == li[1].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence50Errors() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'recurrence.type', '50'
        setInputValue 'recurrence.endType', 'count'
        submitForm getUrl('/calendar-event/save')

        assert checkErrorFields(['subject', 'start', 'start_date', 'start_time', 'end', 'end_date', 'end_time'])
        assert 'Feld darf nicht leer sein.' == driver.findElement(By.xpath('//div[@id="tabs-recurrence-type-50"]/ul[@class="field-msgs"]/li[@class="error-msg"]')).text
        assert '1' == getInputValue('recurrence-interval-50')
        assert 'none' == getInputValue('recurrence.endType')
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence60Success() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'subject', 'Besprechung Werbekonzept'
        setInputValue 'start_date', '23.01.2013'
        setInputValue 'start_time', '10:00'
        setInputValue 'end_date', '23.01.2013'
        setInputValue 'end_time', '12:00'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'location', 'Büro Landschaftsbau Duvensee GbR'

        setInputValue 'recurrence.type', '60'
        assert getInput('recurrence-monthDay-60').displayed
        setInputValue 'recurrence-monthDay-60', '25'
        setInputValue 'recurrence-month-60', '1'
        setInputValue 'recurrence.endType', 'count'
        setInputValue 'recurrence.cnt', '5'

        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '2h'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        setInputValue 'description', 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
        assert 'Besprechung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '25.02.2013 10:00' == getShowFieldText(col, 2)
        assert '25.02.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'jährlich (Tag)' == getShowFieldText(fieldSet, 1)
        assert 'jedes Jahr am 25. Februar' == getShowFieldText(fieldSet, 2)
        assert 'am 25.02.2017' == getShowFieldText(fieldSet, 3)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 2 == li.size()
        assert '2 Stunden' == li[0].text
        assert '1 Tag' == li[1].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence60Errors() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'recurrence.type', '60'
        setInputValue 'recurrence.endType', 'count'
        submitForm getUrl('/calendar-event/save')

        assert checkErrorFields(['subject', 'start', 'start_date', 'start_time', 'end', 'end_date', 'end_time'])
        assert 'Feld darf nicht leer sein.' == driver.findElement(By.xpath('//div[@id="tabs-recurrence-type-60"]/ul[@class="field-msgs"]/li[@class="error-msg"]')).text
        assert 'none' == getInputValue('recurrence.endType')
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence70Success() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'subject', 'Besprechung Werbekonzept'
        setInputValue 'start_date', '23.01.2013'
        setInputValue 'start_time', '10:00'
        setInputValue 'end_date', '23.01.2013'
        setInputValue 'end_time', '12:00'
        assert 'Landschaftsbau Duvensee GbR' == selectAutocompleteEx('organization', 'Landschaftsbau')
        setInputValue 'location', 'Büro Landschaftsbau Duvensee GbR'

        setInputValue 'recurrence.type', '70'
        assert getInput('recurrence-weekdayOrd-70').displayed
        setInputValue 'recurrence-weekdayOrd-70', '2'
        setInputValue 'recurrence-weekdays-70', '1'
        setInputValue 'recurrence-month-70', '4'
        setInputValue 'recurrence.endType', 'count'
        setInputValue 'recurrence.cnt', '5'

        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '2h'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        setInputValue 'description', 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
        assert 'Besprechung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '12.05.2013 10:00' == getShowFieldText(col, 2)
        assert '12.05.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'jährlich (Wochentag)' == getShowFieldText(fieldSet, 1)
        assert 'jedes Jahr am 2. Sonntag im Monat Mai' == getShowFieldText(fieldSet, 2)
        assert 'am 14.05.2017' == getShowFieldText(fieldSet, 3)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 2 == li.size()
        assert '2 Stunden' == li[0].text
        assert '1 Tag' == li[1].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testCreateCalendarEventRecurrence70Errors() {
        maximizeWindow()
        clickToolbarButton 0, getUrl('/calendar-event/create')
        checkTitles 'Kalendereintrag anlegen', 'Kalendereinträge', 'Neuer Kalendereintrag'
        setInputValue 'recurrence.type', '70'
        setInputValue 'recurrence.endType', 'count'
        submitForm getUrl('/calendar-event/save')

        assert checkErrorFields(['subject', 'start', 'start_date', 'start_time', 'end', 'end_date', 'end_time'])
        assert 'Feld darf nicht leer sein.' == driver.findElement(By.xpath('//div[@id="tabs-recurrence-type-70"]/ul[@class="field-msgs"]/li[@class="error-msg"]')).text
        assert 'none' == getInputValue('recurrence.endType')
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testShowCalendarEvent() {
        int id = clickListItem 0, 1, '/calendar-event/show'
        checkTitles 'Kalendereintrag anzeigen', 'Kalendereinträge', 'Besprechung Werbekonzept'
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        def col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getShowFieldText(col, 1)
        assert '23.01.2013 10:00' == getShowFieldText(col, 2)
        assert '23.01.2013 12:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'keine' == getShowFieldText(fieldSet, 1)
        fieldSet = getFieldset(dataSheet, 3)
        assert 'keine' == getShowFieldText(fieldSet, 1)
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)

        assert driver.findElement(By.className('record-timestamps')).text.startsWith('Erstellt am ')

        checkDefaultShowToolbar 'calendar-event', id
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testListCalendarEventsListView() {
        checkTitles 'Kalendereinträge', 'Kalendereinträge'

        def tbody = driver.findElement(By.xpath('//table[@class="content-table"]/tbody'))
        assert 1 == tbody.findElements(By.tagName('tr')).size()
        def tr = tbody.findElement(By.xpath('tr[1]'))
        def td = tr.findElement(By.xpath('td[2]'))
        assert td.getAttribute('class').contains('string')
        def link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/calendar-event/show/'))
        assert 'Besprechung Werbekonzept' == link.text
        td = tr.findElement(By.xpath('td[3]'))
        assert td.getAttribute('class').contains('date')
        assert '23.01.2013 10:00' == td.text
        td = tr.findElement(By.xpath('td[4]'))
        assert td.getAttribute('class').contains('date')
        assert '23.01.2013 12:00' == td.text
        td = tr.findElement(By.xpath('td[5]'))
        assert td.getAttribute('class').contains('string')
        assert '' == td.text
        td = tr.findElement(By.xpath('td[6]'))
        assert td.getAttribute('class').contains('ref')
        link = td.findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        td = tr.findElement(By.xpath('td[7]'))
        assert td.getAttribute('class').contains('string')
        assert 'Büro Landschaftsbau Duvensee GbR' == td.text
        td = tr.findElement(By.xpath('td[8]'))
        assert td.getAttribute('class').contains('action-buttons')
        link = td.findElement(By.xpath('a[1]'))
        assert link.getAttribute('href').startsWith(getUrl('/calendar-event/edit/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('green')
        assert 'Bearbeiten' == link.text
        link = td.findElement(By.xpath('a[2]'))
        assert link.getAttribute('href').startsWith(getUrl('/calendar-event/delete/'))
        assert link.getAttribute('class').contains('button')
        assert link.getAttribute('class').contains('red')
        assert link.getAttribute('class').contains('delete-btn')
        assert 'Löschen' == link.text
        link.click()
        driver.switchTo().alert().dismiss()
        assert getUrl('/calendar-event/list') == driver.currentUrl
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testListCalendarEventsDayView() {
        checkTitles 'Kalendereinträge', 'Kalendereinträge'
        driver.findElement(By.xpath('//div[@id="content"]/div[@class="fc-header"]/span[1]')).click()

        def date = new Date()
        def dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.GERMANY)
        WebElement header = driver.findElement(By.xpath('//div[@id="content"]//table[@class="fc-header"]'))
        assert dateFormat.format(date) == header.findElement(By.tagName('h2')).text
        header.findElement(By.xpath('.//td[@class="fc-header-left"]/span[2]')).click()
        assert dateFormat.format(date + 1) == header.findElement(By.tagName('h2')).text
        header.findElement(By.xpath('.//td[@class="fc-header-left"]/span[2]')).click()
        assert dateFormat.format(date + 2) == header.findElement(By.tagName('h2')).text
        header.findElement(By.xpath('.//td[@class="fc-header-left"]/span[1]')).click()
        assert dateFormat.format(date + 1) == header.findElement(By.tagName('h2')).text
        header.findElement(By.xpath('.//td[@class="fc-header-left"]/span[4]')).click()
        assert dateFormat.format(date) == header.findElement(By.tagName('h2')).text

        goToDate '23.01.2013', 'Mittwoch, 23. Januar 2013'
        WebElement table = driver.findElement(By.className('fc-agenda-slots'))
        List<WebElement> trs = table.findElements(By.xpath('./tbody/tr'))
        assert 2 * 24 == trs.size()     // 2 rows per hour
        for (int i = 0; i < 24; i++) {
            assert "${i}:00" == trs[2 * i].findElement(By.tagName('th')).text
        }
        checkCalendarEvents([
            [time: '10:00 - 12:00', title: 'Besprechung Werbekonzept']
        ])
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testListCalendarEventsWeekView() {
        checkTitles 'Kalendereinträge', 'Kalendereinträge'
        driver.findElement(By.xpath('//div[@id="content"]/div[@class="fc-header"]/span[2]')).click()

        WebElement table = driver.findElement(By.className('fc-agenda-days'))
        List<WebElement> ths = table.findElements(By.xpath('./thead/tr/th'))
        assert 9 == ths.size()          // 7 days + 1 label column + 1 gutter column

        goToDate '23.01.2013', '21. – 27. Jan 2013'
        table = driver.findElement(By.className('fc-agenda-slots'))
        List<WebElement> trs = table.findElements(By.xpath('./tbody/tr'))
        assert 2 * 24 == trs.size()     // 2 rows per hour
        for (int i = 0; i < 24; i++) {
            assert "${i}:00" == trs[2 * i].findElement(By.tagName('th')).text
        }
        checkCalendarEvents([
            [time: '10:00 - 12:00', title: 'Besprechung Werbekonzept']
        ])
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testListCalendarEventsMonthView() {
        checkTitles 'Kalendereinträge', 'Kalendereinträge'
        driver.findElement(By.xpath('//div[@id="content"]/div[@class="fc-header"]/span[3]')).click()

        WebElement div = driver.findElement(By.className('fc-view-month'))
        List<WebElement> ths = div.findElements(By.xpath('.//thead/tr/th'))
        assert 7 == ths.size()          // 7 days

        goToDate '23.01.2013', 'Januar 2013'
        checkCalendarEvents([
            [time: '10:00 - 12:00', title: 'Besprechung Werbekonzept']
        ])
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testEditCalendarEventSuccess() {
        clickListActionButton 0, 0, getUrl('/calendar-event/edit/')
        checkTitles 'Kalendereintrag bearbeiten', 'Kalendereinträge', 'Besprechung Werbekonzept'
        def col = driver.findElement(By.xpath('//form[@id="calendarEvent-form"]/fieldset[1]')).findElement(By.className('col-l'))
        assert 'Besprechung Werbekonzept' == getInputValue('subject')
        assert '23.01.2013' == getInputValue('start_date')
        assert '10:00' == getInputValue('start_time')
        assert '23.01.2013' == getInputValue('end_date')
        assert '12:00' == getInputValue('end_time')
        assert 'Landschaftsbau Duvensee GbR' == driver.findElement(By.id('organization')).getAttribute('value')
        assert 'Büro Landschaftsbau Duvensee GbR' == getInputValue('location')
        assert 0 == reminderSelectors.size()
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getInputValue('description')

        setInputValue 'subject', 'Besprechung und Planung Werbekonzept'
        setInputValue 'start_time', '14:30'
        setInputValue 'end_time', '17:00'
        List<WebElement> reminderSelectors = addReminderSelector()
        assert 1 == reminderSelectors.size()
        selectReminder 0, '30m'
        reminderSelectors = addReminderSelector()
        assert 2 == reminderSelectors.size()
        selectReminder 1, '1d'
        reminderSelectors = addReminderSelector()
        assert 3 == reminderSelectors.size()
        selectReminder 2, '2d'
        submitForm getUrl('/calendar-event/show/')

        assert 'Kalendereintrag Besprechung und Planung Werbekonzept wurde geändert.' == flashMessage
        assert 'Besprechung und Planung Werbekonzept' == driver.findElement(BY_SUBHEADER).text
        def dataSheet = driver.findElement(By.className('data-sheet'))
        def fieldSet = getFieldset(dataSheet, 1)
        col = fieldSet.findElement(By.className('col-l'))
        assert 'Besprechung und Planung Werbekonzept' == getShowFieldText(col, 1)
        assert '23.01.2013 14:30' == getShowFieldText(col, 2)
        assert '23.01.2013 17:00' == getShowFieldText(col, 3)
        col = fieldSet.findElement(By.className('col-r'))
        def link = getShowField(col, 1).findElement(By.tagName('a'))
        assert link.getAttribute('href').startsWith(getUrl('/organization/show/'))
        assert 'Landschaftsbau Duvensee GbR' == link.text
        assert 'Büro Landschaftsbau Duvensee GbR' == getShowFieldText(col, 2)
        assert 'Auf der Karte zeigen' == getShowField(col, 3).findElement(By.tagName('a')).text
        fieldSet = getFieldset(dataSheet, 2)
        assert 'keine' == getShowFieldText(fieldSet, 1)
        fieldSet = getFieldset(dataSheet, 3)
        List<WebElement> li = fieldSet.findElements(By.tagName('li'))
        assert 3 == li.size()
        assert '30 Minuten' == li[0].text
        assert '1 Tag' == li[1].text
        assert '2 Tage' == li[2].text
        fieldSet = getFieldset(dataSheet, 4)
        assert 'Besprechung des Konzepts für die geplante Marketing-Aktion.' == getShowFieldText(fieldSet, 1)
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testEditCalendarEventErrors() {
        clickListActionButton 0, 0, getUrl('/calendar-event/edit/')
        checkTitles 'Kalendereintrag bearbeiten', 'Kalendereinträge', 'Besprechung Werbekonzept'
        clearInput 'subject'
        submitForm getUrl('/calendar-event/update')

        assert checkErrorFields(['subject'])
        cancelForm getUrl('/calendar-event/list')
        driver.quit()

        assert 1 == CalendarEvent.count()
    }

    @Test
    void testDeleteCalendarEventAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().accept()
        assert driver.currentUrl.startsWith(getUrl('/calendar-event/list'))
        assert 'Kalendereintrag wurde gelöscht.' == flashMessage
        def emptyList = driver.findElement(By.className('empty-list'))
        assert 'Diese Liste enthält keine Einträge.' == emptyList.findElement(By.tagName('p')).text
        driver.quit()

        assert 0 == CalendarEvent.count()
    }

    @Test
    void testDeleteCalendarEventNoAction() {
        clickListActionButton 0, 1
        driver.switchTo().alert().dismiss()
        assert getUrl('/calendar-event/list') == driver.currentUrl
        driver.quit()

        assert 1 == CalendarEvent.count()
    }


    //-- Non-public methods ---------------------

    /**
     * Adds a new reminder selector field.
     *
     * @return  the currently displayed reminder selectors
     */
    protected List<WebElement> addReminderSelector() {
        driver.findElement(By.id('reminder-add-btn')).click()
        reminderSelectors
    }

    @Override
    protected Object getDatasets() {
        ['test-data/install-data.xml']
    }

    /**
     * Checks whether events with the given data are displayed in the calendar.
     *
     * @param expectedEventData a list of maps containing the expected event
     *                          data; each map may contain the following keys:
     *                          <ul>
     *                            <li>time. The time specification.</li>
     *                            <li>title. The title of the event.</li>
     *                          </ul>
     */
    protected void checkCalendarEvents(List<Map<String, String>> expectedEventData)
    {
        List<WebElement> events = driver.findElements(By.className('fc-event'))
        int n = events.size()
        assert expectedEventData.size() == n

        for (int i = 0; i < n; i++) {
            WebElement event = events[i]
            Map<String, String> expectedData = expectedEventData[i]
            assert event.getAttribute('href').startsWith(getUrl('/calendar-event/show'))
            assert expectedData.time == event.findElement(By.className('fc-event-time')).text
            assert expectedData.title == event.findElement(By.className('fc-event-title')).text
        }
    }

    /**
     * Gets the currently displayed reminder selectors.
     *
     * @return  the reminder selectors
     */
    protected List<WebElement> getReminderSelectors() {
        driver.findElements By.xpath('//div[@id="reminder-selectors"]//li')
    }

    /**
     * Opens the dialog to go to the given date.
     *
     * @param date              the date to go to in the format of the
     *                          currently active locale
     * @param expectedHeader    the expected text of the header after changing
     *                          the date; if {@code null} the header is not
     *                          checked
     */
    protected void goToDate(String date, String expectedHeader = null) {
        WebElement gotoBtn = driver.findElement(By.className('fc-button-goto'))
        assert 'Gehe zu Datum' == gotoBtn.text
        gotoBtn.click()

        By byDlg = By.id('goto-date-dialog')
        WebDriverWait wait = new WebDriverWait(driver, 10)
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(byDlg))
        WebElement input = dialog.findElement(By.xpath('./div[@class="field"]/input'))
        input.clear()
        input.sendKeys date

        dialog.findElement(By.xpath('..//div[@class="ui-dialog-buttonset"]/button[1]')).click()
        wait.until ExpectedConditions.invisibilityOfElementLocated(byDlg)
        if (expectedHeader) {
            assert expectedHeader == driver.findElement(By.xpath('//div[@id="content"]//table[@class="fc-header"]//h2')).text
        }
    }

    /**
     * Selects the given value of the reminder with the stated index.
     *
     * @param reminderIdx   the zero-based index of the reminder to be selected
     * @param value         the value to be selected
     */
    protected void selectReminder(int reminderIdx, String value) {
        Select select = new Select(
            reminderSelectors[reminderIdx].findElement(By.tagName('select'))
        )
        select.selectByValue value
    }
}
