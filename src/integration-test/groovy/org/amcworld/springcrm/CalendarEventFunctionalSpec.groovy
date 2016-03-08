/*
 * CalendarEventFunctionalSpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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
import java.text.SimpleDateFormat
import org.amcworld.springcrm.page.CalendarEventCreatePage
import org.amcworld.springcrm.page.CalendarEventDayViewPage
import org.amcworld.springcrm.page.CalendarEventEditPage
import org.amcworld.springcrm.page.CalendarEventListPage
import org.amcworld.springcrm.page.CalendarEventMonthViewPage
import org.amcworld.springcrm.page.CalendarEventShowPage
import org.amcworld.springcrm.page.CalendarEventWeekViewPage
import org.amcworld.springcrm.page.OrganizationShowPage


class CalendarEventFunctionalSpec extends GeneralFunctionalTest {

    //-- Fixture methods ------------------------

    def setup() {
        prepareOrganization()

        login()
    }


    //-- Feature methods ------------------------

    def 'Create calendar event with no recurrence successfully'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

		when: 'I fill in this form and submit it'
		form.subject = 'Besprechung Werbekonzept'
		form.start_date = '23.01.2013'
		form.start_time = '10:00'
		form.end_date = '23.01.2013'
		form.end_time = '12:00'
		assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
		form.location = 'Büro Landschaftsbau Duvensee GbR'
		form.description = 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '2h'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		submitBtn.click()

		then: 'a calendar event is created and I get to the show view'
		waitFor { at CalendarEventShowPage }
		'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
		'Kalendereinträge' == header
		'Besprechung Werbekonzept' == subheader

		and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'23.01.2013 10:00' == colL.row[1].fieldText
		'23.01.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'keine' == fs1.row[0].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		2 == reminders.size()
		'2 Stunden' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

		and: 'there is one CalendarEvent object'
		1 == CalendarEvent.count()
    }

    def 'Create calendar event with errors'() {
        given: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

        when: 'I submit the form without filling in any value'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CalendarEventCreatePage }
        page.checkErrorFields 'subject', 'start_date', 'start_time', 'end_date', 'end_time'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list is empty'
        waitFor { at CalendarEventListPage }
        page.emptyList.check CalendarEventCreatePage, 'Kalendereintrag anlegen'

        and: 'no CalendarEvent object has been created'
        0 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 10 successfully'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

		when: 'I fill in this form and submit it'
		form.subject = 'Besprechung Werbekonzept'
		form.start_date = '23.01.2013'
		form.start_time = '10:00'
		form.end_date = '23.01.2013'
		form.end_time = '12:00'
		assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
		form.location = 'Büro Landschaftsbau Duvensee GbR'
		form.description = 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        form.'recurrence.type' = '10'
		waitFor { form.'recurrence-interval-10'().displayed }
        form.'recurrence-interval-10' = '3'
        form.'recurrence.endType' = 'none'
        assert !form.'recurrence.until_date'().enabled
        assert !form.'recurrence.cnt'().enabled
        form.'recurrence.endType' = 'count'
        form.'recurrence.cnt' = '5'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '2h'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		submitBtn.click()

		then: 'a calendar event is created and I get to the show view'
		waitFor { at CalendarEventShowPage }
		'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
		'Kalendereinträge' == header
		'Besprechung Werbekonzept' == subheader

		and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'23.01.2013 10:00' == colL.row[1].fieldText
		'23.01.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'täglich' == fs1.row[0].fieldText
		'aller 3 Tage' == fs1.row[1].fieldText
		'am 04.02.2013' == fs1.row[2].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		2 == reminders.size()
		'2 Stunden' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

		and: 'there is one CalendarEvent object'
		1 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 10 with errors'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

        when: 'I only select recurrence type 10 and submit'
		form.'recurrence.type' = '10'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CalendarEventCreatePage }
        page.checkErrorFields 'subject', 'start_date', 'start_time', 'end_date', 'end_time'

		and: 'some reasonable values are filled in'
		'1' == form.'recurrence-interval-10'
		'none' == form.'recurrence.endType'

        and: 'no CalendarEvent object has been created'
        0 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 30 successfully'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

		when: 'I fill in this form and submit it'
		form.subject = 'Besprechung Werbekonzept'
		form.start_date = '23.01.2013'
		form.start_time = '10:00'
		form.end_date = '23.01.2013'
		form.end_time = '12:00'
		assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
		form.location = 'Büro Landschaftsbau Duvensee GbR'
		form.description = 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        form.'recurrence.type' = '30'
		waitFor { form.'recurrence-interval-30'().displayed }
        form.'recurrence-weekdays-30-3' = true
        form.'recurrence-weekdays-30-5' = true
        form.'recurrence-interval-30' = '3'
        form.'recurrence.endType' = 'none'
        assert !form.'recurrence.until_date'().enabled
        assert !form.'recurrence.cnt'().enabled
        form.'recurrence.endType' = 'count'
        form.'recurrence.cnt' = '5'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '2h'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		submitBtn.click()

		then: 'a calendar event is created and I get to the show view'
		waitFor { at CalendarEventShowPage }
		'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
		'Kalendereinträge' == header
		'Besprechung Werbekonzept' == subheader

		and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'24.01.2013 10:00' == colL.row[1].fieldText
		'24.01.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'wöchentlich' == fs1.row[0].fieldText
		'aller 3 Wochen am Dienstag, Donnerstag' == fs1.row[1].fieldText
		'am 07.03.2013' == fs1.row[2].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		2 == reminders.size()
		'2 Stunden' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

		and: 'there is one CalendarEvent object'
		1 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 30 with errors'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

        when: 'I only select recurrence type 30 and submit'
		form.'recurrence.type' = '30'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CalendarEventCreatePage }
        page.checkErrorFields 'subject', 'start_date', 'start_time', 'end_date', 'end_time'
        'Mindestens ein Wochentag muss ausgewählt sein.' == $('#tabs-recurrence-type-30 ul.field-msgs > li.error-msg').text()

		and: 'some reasonable values are filled in'
		'1' == form.'recurrence-interval-30'
		'none' == form.'recurrence.endType'

        and: 'no CalendarEvent object has been created'
        0 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 40 successfully'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

		when: 'I fill in this form and submit it'
		form.subject = 'Besprechung Werbekonzept'
		form.start_date = '23.01.2013'
		form.start_time = '10:00'
		form.end_date = '23.01.2013'
		form.end_time = '12:00'
		assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
		form.location = 'Büro Landschaftsbau Duvensee GbR'
		form.description = 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        form.'recurrence.type' = '40'
		waitFor { form.'recurrence-interval-40'().displayed }
        form.'recurrence-monthDay-40' = '6'
        form.'recurrence-interval-40' = '2'
        form.'recurrence.endType' = 'none'
        assert !form.'recurrence.until_date'().enabled
        assert !form.'recurrence.cnt'().enabled
        form.'recurrence.endType' = 'count'
        form.'recurrence.cnt' = '5'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '2h'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		submitBtn.click()

		then: 'a calendar event is created and I get to the show view'
		waitFor { at CalendarEventShowPage }
		'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
		'Kalendereinträge' == header
		'Besprechung Werbekonzept' == subheader

		and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'06.03.2013 10:00' == colL.row[1].fieldText
		'06.03.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'monatlich (Tag)' == fs1.row[0].fieldText
		'aller 2 Monate am 06.' == fs1.row[1].fieldText
		'am 06.11.2013' == fs1.row[2].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		2 == reminders.size()
		'2 Stunden' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

		and: 'there is one CalendarEvent object'
		1 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 40 with errors'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

        when: 'I only select recurrence type 40 and submit'
		form.'recurrence.type' = '40'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CalendarEventCreatePage }
        page.checkErrorFields 'subject', 'start_date', 'start_time', 'end_date', 'end_time'
        'Feld darf nicht leer sein.' == $('#tabs-recurrence-type-40 ul.field-msgs > li.error-msg').text()

		and: 'some reasonable values are filled in'
		'1' == form.'recurrence-interval-40'
		'none' == form.'recurrence.endType'

        and: 'no CalendarEvent object has been created'
        0 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 50 successfully'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

		when: 'I fill in this form and submit it'
		form.subject = 'Besprechung Werbekonzept'
		form.start_date = '23.01.2013'
		form.start_time = '10:00'
		form.end_date = '23.01.2013'
		form.end_time = '12:00'
		assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
		form.location = 'Büro Landschaftsbau Duvensee GbR'
		form.description = 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        form.'recurrence.type' = '50'
		waitFor { form.'recurrence-interval-50'().displayed }
        form.'recurrence-weekdayOrd-50' = '3'
        form.'recurrence-weekdays-50' = '5'
        form.'recurrence-interval-50' = '2'
        form.'recurrence.endType' = 'none'
        assert !form.'recurrence.until_date'().enabled
        assert !form.'recurrence.cnt'().enabled
        form.'recurrence.endType' = 'count'
        form.'recurrence.cnt' = '5'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '2h'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		submitBtn.click()

		then: 'a calendar event is created and I get to the show view'
		waitFor { at CalendarEventShowPage }
		'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
		'Kalendereinträge' == header
		'Besprechung Werbekonzept' == subheader

		and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'21.03.2013 10:00' == colL.row[1].fieldText
		'21.03.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'monatlich (Wochentag)' == fs1.row[0].fieldText
		'aller 2 Monate am 3. Donnerstag' == fs1.row[1].fieldText
		'am 21.11.2013' == fs1.row[2].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		2 == reminders.size()
		'2 Stunden' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

		and: 'there is one CalendarEvent object'
		1 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 50 with errors'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

        when: 'I only select recurrence type 50 and submit'
		form.'recurrence.type' = '50'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CalendarEventCreatePage }
        page.checkErrorFields 'subject', 'start_date', 'start_time', 'end_date', 'end_time'
        'Feld darf nicht leer sein.' == $('#tabs-recurrence-type-50 ul.field-msgs > li.error-msg').text()

		and: 'some reasonable values are filled in'
		'1' == form.'recurrence-interval-50'
		'none' == form.'recurrence.endType'

        and: 'no CalendarEvent object has been created'
        0 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 60 successfully'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

		when: 'I fill in this form and submit it'
		form.subject = 'Besprechung Werbekonzept'
		form.start_date = '23.01.2013'
		form.start_time = '10:00'
		form.end_date = '23.01.2013'
		form.end_time = '12:00'
		assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
		form.location = 'Büro Landschaftsbau Duvensee GbR'
		form.description = 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        form.'recurrence.type' = '60'
		waitFor { form.'recurrence-monthDay-60'().displayed }
        form.'recurrence-monthDay-60' = '25'
        form.'recurrence-month-60' = '1'
        form.'recurrence.endType' = 'none'
        assert !form.'recurrence.until_date'().enabled
        assert !form.'recurrence.cnt'().enabled
        form.'recurrence.endType' = 'count'
        form.'recurrence.cnt' = '5'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '2h'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		submitBtn.click()

		then: 'a calendar event is created and I get to the show view'
		waitFor { at CalendarEventShowPage }
		'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
		'Kalendereinträge' == header
		'Besprechung Werbekonzept' == subheader

		and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'25.02.2013 10:00' == colL.row[1].fieldText
		'25.02.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'jährlich (Tag)' == fs1.row[0].fieldText
		'jedes Jahr am 25. Februar' == fs1.row[1].fieldText
		'am 25.02.2017' == fs1.row[2].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		2 == reminders.size()
		'2 Stunden' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

		and: 'there is one CalendarEvent object'
		1 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 60 with errors'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

        when: 'I only select recurrence type 60 and submit'
		form.'recurrence.type' = '60'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CalendarEventCreatePage }
        page.checkErrorFields 'subject', 'start_date', 'start_time', 'end_date', 'end_time'
        'Feld darf nicht leer sein.' == $('#tabs-recurrence-type-60 ul.field-msgs > li.error-msg').text()

		and: 'some reasonable values are filled in'
		'none' == form.'recurrence.endType'

        and: 'no CalendarEvent object has been created'
        0 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 70 successfully'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

		when: 'I fill in this form and submit it'
		form.subject = 'Besprechung Werbekonzept'
		form.start_date = '23.01.2013'
		form.start_time = '10:00'
		form.end_date = '23.01.2013'
		form.end_time = '12:00'
		assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
		form.location = 'Büro Landschaftsbau Duvensee GbR'
		form.description = 'Besprechung des Konzepts für die geplante Marketing-Aktion.'
        form.'recurrence.type' = '70'
		waitFor { form.'recurrence-weekdayOrd-70'().displayed }
        form.'recurrence-weekdayOrd-70' = '2'
        form.'recurrence-weekdays-70' = '1'
        form.'recurrence-month-70' = '4'
        form.'recurrence.endType' = 'none'
        assert !form.'recurrence.until_date'().enabled
        assert !form.'recurrence.cnt'().enabled
        form.'recurrence.endType' = 'count'
        form.'recurrence.cnt' = '5'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '2h'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		submitBtn.click()

		then: 'a calendar event is created and I get to the show view'
		waitFor { at CalendarEventShowPage }
		'Kalendereintrag Besprechung Werbekonzept wurde angelegt.' == flashMessage
		'Kalendereinträge' == header
		'Besprechung Werbekonzept' == subheader

		and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'12.05.2013 10:00' == colL.row[1].fieldText
		'12.05.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'jährlich (Wochentag)' == fs1.row[0].fieldText
		'jedes Jahr am 2. Sonntag im Monat Mai' == fs1.row[1].fieldText
		'am 14.05.2017' == fs1.row[2].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		2 == reminders.size()
		'2 Stunden' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

		and: 'there is one CalendarEvent object'
		1 == CalendarEvent.count()
    }

    def 'Create calendar event with recurrence type 70 with errors'() {
        given: 'an organization and person'
        def org = Organization.first()

        and: 'I go to the calendar event list view'
        to CalendarEventListPage

		when: 'I click the create button'
		createBtn.click()

		then: 'I get to the create form'
		waitFor { at CalendarEventCreatePage }
		'Kalendereinträge' == header
		'Neuer Kalendereintrag' == subheader

        when: 'I only select recurrence type 70 and submit'
		form.'recurrence.type' = '70'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CalendarEventCreatePage }
        page.checkErrorFields 'subject', 'start_date', 'start_time', 'end_date', 'end_time'
        'Feld darf nicht leer sein.' == $('#tabs-recurrence-type-70 ul.field-msgs > li.error-msg').text()

		and: 'some reasonable values are filled in'
		'none' == form.'recurrence.endType'

        and: 'no CalendarEvent object has been created'
        0 == CalendarEvent.count()
    }

    def 'Show calendar event'() {
        given: 'a calendar event'
        def org = Organization.first()
        def calEvent = prepareCalendarEvent org

        and:
        to CalendarEventListPage

        when: 'I click an item to show'
        tr[0].subjectLink.click()

        then: 'I get the show view with all data'
        waitFor { at CalendarEventShowPage }
        'Kalendereinträge' == header
        'Besprechung Werbekonzept' == subheader
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung Werbekonzept' == colL.row[0].fieldText
		'23.01.2013 10:00' == colL.row[1].fieldText
		'23.01.2013 12:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'keine' == fs1.row[0].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		'keine' == fs2.row[0].fieldText
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

        timestamps.startsWith 'Erstellt am '
        page.checkToolbar 'calendar-event', calEvent.id

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
    }

    def 'List calendar events'() {
        given: 'a calendar event'
        def org = Organization.first()
        def calEvent = prepareCalendarEvent org

        when:
        to CalendarEventListPage

        then: 'I get to the list view'
        waitFor { at CalendarEventListPage }
        'Kalendereinträge' == header

        and: 'the table contains one entry'
        1 == tr.size()
        def row = tr[0]
        row.checkTdClasses 'row-selector', 'string', 'date', 'date', 'string', 'ref', 'string'
        row.subjectLink.checkLinkToPage CalendarEventShowPage, calEvent.id
        'Besprechung Werbekonzept' == row.subject
        '23.01.2013 10:00' == row.start
        '23.01.2013 12:00' == row.end
        '' == row.recurrence
        row.organizationLink.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == row.organization
        'Büro Landschaftsbau Duvensee GbR' == row.location
        row.checkActionButtons 'calendar-event', calEvent.id

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
    }

	def 'Calendar event in day view'() {
		given: 'a calendar event'
		def org = Organization.first()
		def calEvent = prepareCalendarEvent org

		and: 'the current date with a formatter'
		def date = new Date()
		def dateFormat = DateFormat.getDateInstance(
			DateFormat.LONG, Locale.GERMANY
		)

		when:
		to CalendarEventListPage

		then: 'I get to the list view'
		waitFor { at CalendarEventListPage }
		'Kalendereinträge' == header

		when: 'I change to day view'
		viewSelector(0).click()

		then: 'I get to the day view'
		waitFor { at CalendarEventDayViewPage }
		page.checkViewSelectors()
        dateFormat.format(date) == currentDate

		when: 'I go to the next day'
		nextBtn.click()

		then: 'the date is updated'
        dateFormat.format(date + 1) == currentDate

		when: 'I go to the day after next day'
		nextBtn.click()

		then: 'the date is updated'
        dateFormat.format(date + 2) == currentDate

		when: 'I go to the previous day'
		prevBtn.click()

		then: 'the date is updated'
        dateFormat.format(date + 1) == currentDate

		when: 'I go to the previous day'
		prevBtn.click()

		then: 'the date is updated'
        dateFormat.format(date) == currentDate

		when: 'I got to a particular date'
		assert 'Gehe zu Datum' == gotoDateBtn.text()
		gotoDate '23.01.2013'

		then: 'the day view has changed'
		'23. Januar 2013' == currentDate
        2 * 24 == slots.size()     // 2 rows per hour
        for (int i = 0; i < 24; i++) {
            "${i} Uhr" == slots[2 * i].find('th').text()
        }
		1 == events.size()
		events[0].checkLink calEvent.id
		'10:00 - 12:00 Uhr' == events[0].time
        'Besprechung Werbekonzept' == events[0].title

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
	}

	def 'Calendar event in week view'() {
		given: 'a calendar event'
		def org = Organization.first()
		def calEvent = prepareCalendarEvent org

		when:
		to CalendarEventListPage

		then: 'I get to the list view'
		waitFor { at CalendarEventListPage }
		'Kalendereinträge' == header

		when: 'I change to week view'
		viewSelector(1).click()

		then: 'I get to the week view'
		waitFor { at CalendarEventWeekViewPage }
		page.checkViewSelectors()
        9 == days.size()
		String title1 = currentDate

		when: 'I go to the next week'
		nextBtn.click()

		then: 'the date has been changed'
		String title2 = currentDate
        title1 != title2

		when: 'I go to the week after next week'
		nextBtn.click()

		then: 'the date has been changed again'
		title1 != currentDate
		title2 != currentDate

		when: 'I go to the previous week'
		prevBtn.click()

		then: 'the date has been changed'
        title2 == currentDate

		when: 'I go to the previous week'
		prevBtn.click()

		then: 'the date has been changed'
        title1 == currentDate

		when: 'I got to a particular date'
		assert 'Gehe zu Datum' == gotoDateBtn.text()
		gotoDate '23.01.2013'

		then: 'the week view has changed'
		'21 - 27. Jan. 2013' == currentDate
        2 * 24 == slots.size()     // 2 rows per hour
        for (int i = 0; i < 24; i++) {
            "${i < 10 ? '0' : ''}${i} Uhr" == slots[2 * i].find('th').text()
        }
		1 == events.size()
		events[0].checkLink calEvent.id
		'10:00 - 12:00 Uhr' == events[0].time
        'Besprechung Werbekonzept' == events[0].title

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
	}

	def 'Calendar event in month view'() {
		given: 'a calendar event'
		def org = Organization.first()
		def calEvent = prepareCalendarEvent org

		and: 'the current date with a formatter'
		def cal = Calendar.getInstance()
		def dateFormat = new SimpleDateFormat('MMMM YYYY')

		when:
		to CalendarEventListPage

		then: 'I get to the list view'
		waitFor { at CalendarEventListPage }
		'Kalendereinträge' == header

		when: 'I change to month view'
		viewSelector(2).click()

		then: 'I get to the month view'
		waitFor { at CalendarEventMonthViewPage }
		page.checkViewSelectors()
        7 == days.size()
		dateFormat.format(cal.time) == currentDate

		when: 'I go to the next month'
		nextBtn.click()

		then: 'the date has been changed'
		Calendar cal1 = cal.clone()
		cal1.add Calendar.MONTH, 1
		dateFormat.format(cal1.time) == currentDate

		when: 'I go to the month after next month'
		nextBtn.click()

		then: 'the date has been changed again'
		Calendar cal2 = cal.clone()
		cal2.add Calendar.MONTH, 2
		dateFormat.format(cal2.time) == currentDate

		when: 'I go to the previous month'
		prevBtn.click()

		then: 'the date has been changed'
		dateFormat.format(cal1.time) == currentDate

		when: 'I go to the previous month'
		prevBtn.click()

		then: 'the date has been changed'
		dateFormat.format(cal.time) == currentDate

		when: 'I got to a particular date'
		assert 'Gehe zu Datum' == gotoDateBtn.text()
		gotoDate '23.01.2013'

		then: 'the month view has changed'
		'Januar 2013' == currentDate
		1 == events.size()
		events[0].checkLink calEvent.id
		'10 Uhr' == events[0].time
        'Besprechung Werbekonzept' == events[0].title

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
	}

    def 'Edit calendar event successfully'() {
        given: 'a calendar event'
        def org = Organization.first()
        def calEvent = prepareCalendarEvent org

        and: 'I go to the list view'
        to CalendarEventListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at CalendarEventEditPage }
        'Kalendereinträge' == header
        'Besprechung Werbekonzept' == subheader

        and: 'the form is pre-filled correctly'
        'Besprechung Werbekonzept' == form.subject
        '23.01.2013' == form.start_date
        '10:00' == form.start_time
        '23.01.2013' == form.end_date
        '12:00' == form.end_time
        'Landschaftsbau Duvensee GbR' == organization.input.value()
        'Büro Landschaftsbau Duvensee GbR' == form.location
        0 == reminders.reminders.size()
        'Besprechung des Konzepts für die geplante Marketing-Aktion.' == form.description

        when: 'I set new values and submit the form'
        form.subject = 'Besprechung und Planung Werbekonzept'
        form.start_time = '14:30'
        form.end_time = '17:00'
		reminders.addButton.click()
		assert 1 == reminders.reminders.size()
		reminders.reminders[0].select = '30m'
		reminders.addButton.click()
		assert 2 == reminders.reminders.size()
		reminders.reminders[1].select = '1d'
		reminders.addButton.click()
		assert 3 == reminders.reminders.size()
		reminders.reminders[2].select = '2d'
        submitBtn.click()

        then: 'the calendar event is updated and I get to the show view'
        waitFor { at CalendarEventShowPage }
        'Kalendereintrag Besprechung und Planung Werbekonzept wurde geändert.' == flashMessage
        'Kalendereinträge' == header
        'Besprechung und Planung Werbekonzept' == subheader

        and: 'the fields are set correctly'
		def fs0 = fieldset[0]
		'Allgemeine Informationen' == fs0.title
		def colL = fs0.colLeft
		'Besprechung und Planung Werbekonzept' == colL.row[0].fieldText
		'23.01.2013 14:30' == colL.row[1].fieldText
		'23.01.2013 17:00' == colL.row[2].fieldText
		def colR = fs0.colRight
		colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
		'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
		'Büro Landschaftsbau Duvensee GbR' == colR.row[1].fieldText
		'Auf der Karte zeigen' == mapBtn.text()
		def fs1 = fieldset[1]
		'Terminwiederholung' == fs1.title
		'keine' == fs1.row[0].fieldText
		def fs2 = fieldset[2]
		'Erinnerungen' == fs2.title
		3 == reminders.size()
		'30 Minuten' == reminders[0].text()
		'1 Tag' == reminders[1].text()
		'2 Tage' == reminders[2].text()
		def fs3 = fieldset[3]
		'Terminbeschreibung' == fs3.title
		'Besprechung des Konzepts für die geplante Marketing-Aktion.' == fs3.row[0].htmlContent.text()

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
    }

    def 'Edit calendar event with errors'() {
        given: 'a calendar event'
        def org = Organization.first()
        def calEvent = prepareCalendarEvent org

        and: 'I go to the list view'
        to CalendarEventListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at CalendarEventEditPage }
        'Kalendereinträge' == header
        'Besprechung Werbekonzept' == subheader

        when: 'I clear mandatory fields and submit the form'
        form.subject = ''
        submitBtn.click()

        then: 'I get to the edit form anew and there are user errors'
        page.checkErrorFields 'subject'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list has one entry'
        waitFor { at CalendarEventListPage }
        1 == tr.size()

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
    }

    def 'Delete calendar event really'() {
        given: 'a calendar event'
        def org = Organization.first()
        def calEvent = prepareCalendarEvent org

        and: 'I go to the list view'
        to CalendarEventListPage

        when: 'I click on the delete button and confirm'
        withConfirm { tr[0].deleteButton.click() }

        then: 'I get to the list view'
        waitFor { at CalendarEventListPage }
        'Kalendereintrag wurde gelöscht.' == flashMessage
        page.emptyList.check CalendarEventCreatePage, 'Kalendereintrag anlegen'

        and: 'there is no CalendarEvent object'
        0 == CalendarEvent.count()
    }

    def 'Delete calendar event but cancel'() {
        given: 'a calendar event'
        def org = Organization.first()
        def calEvent = prepareCalendarEvent org

        and: 'I go to the list view'
        to CalendarEventListPage

        when: 'I click on the delete button but cancel'
        withConfirm(false) { tr[0].deleteButton.click() }

        then: 'I am still on the list view'
        browser.isAt CalendarEventListPage

        and: 'there is still one CalendarEvent object'
        1 == CalendarEvent.count()
    }
}
