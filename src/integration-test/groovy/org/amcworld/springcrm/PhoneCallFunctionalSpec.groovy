/*
 * CallFunctionalSpec.groovy
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

import org.amcworld.springcrm.page.CallCreatePage
import org.amcworld.springcrm.page.CallEditPage
import org.amcworld.springcrm.page.CallListPage
import org.amcworld.springcrm.page.CallShowPage
import org.amcworld.springcrm.page.OrganizationShowPage
import org.amcworld.springcrm.page.PersonShowPage


class PhoneCallFunctionalSpec extends GeneralFunctionalTest {

    //-- Fixture methods ------------------------

    def setup() {
        preparePerson prepareOrganization()

        login()
    }


    //-- Feature methods ------------------------

    def 'Create phone call successfully'() {
        given: 'an organization and person'
        def org = Organization.first()
        def p = Person.first()

        and: 'I go to the phone phoneCall list view'
        to CallListPage

        when: 'I click the create button'
        createBtn.click()

        then: 'I get to the create form'
        waitFor { at CallCreatePage }
        'Anrufe' == header
        'Neuer Anruf' == subheader

        when: 'I fill in this form and submit it'
        form.subject = 'Bitte um Angebot'
        form.start_date = '13.02.2013'
        form.start_time = '09:15'
        assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
        assert 'Henry Brackmann' == person.select('Brack')
        assert '04543 31233' == phone.select('04')
        form.status = 'COMPLETED'
        form.notes = 'Herr Brackmann bittet um die Zusendung eines Angebots für die **geplante Marketing-Aktion**.'
        submitBtn.click()

        then: 'a phoneCall is created and I get to the show view'
        waitFor { at CallShowPage }
        'Anruf Bitte um Angebot wurde angelegt.' == flashMessage
        'Anrufe' == header
        'Bitte um Angebot' == subheader

        and: 'the fields are set correctly'
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL = fs0.colLeft
        'Bitte um Angebot' == colL.row[0].fieldText
        '13.02.2013 09:15' == colL.row[1].fieldText
        def colR = fs0.colRight
        colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
        colR.row[1].link.checkLinkToPage PersonShowPage, p.id
        'Brackmann, Henry' == colR.row[1].link.text()
        '04543 31233' == colR.row[2].fieldText
        'eingehend' == colR.row[3].fieldText
        'durchgeführt' == colR.row[4].fieldText
        def fs1 = fieldset[1]
        'Bemerkungen' == fs1.title
        'Herr Brackmann bittet um die Zusendung eines Angebots für die geplante Marketing-Aktion.' == fs1.row[0].htmlContent.text()
        'geplante Marketing-Aktion' == fs1.row[0].htmlContent.find('strong').text()

        and: 'there is one PhoneCall object'
        1 == PhoneCall.count()
    }

    def 'Create phone call with errors'() {
        given:
        to CallListPage

        when: 'I click the create button'
        createBtn.click()

        then: 'I get to the create form'
        waitFor { at CallCreatePage }
        'Anrufe' == header
        'Neuer Anruf' == subheader

        when: 'I submit the form without filling in any value'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at CallCreatePage }
        page.checkErrorFields 'subject'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list is empty'
        waitFor { at CallListPage }
        page.emptyList.check CallCreatePage, 'Anruf anlegen'

        and: 'no PhoneCall object has been created'
        0 == PhoneCall.count()
    }

    def 'Show phone call'() {
        given: 'a phone phoneCall'
        def org = Organization.first()
        def p = Person.first()
        def call = prepareCall org, p

        and:
        to CallListPage

        when: 'I click an item to show'
        tr[0].subjectLink.click()

        then: 'I get the show view with all data'
        waitFor { at CallShowPage }
        'Anrufe' == header
        'Bitte um Angebot' == subheader
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL = fs0.colLeft
        'Bitte um Angebot' == colL.row[0].fieldText
        '13.02.2013 09:15' == colL.row[1].fieldText
        def colR = fs0.colRight
        colR.row[0].link.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
        colR.row[1].link.checkLinkToPage PersonShowPage, p.id
        'Brackmann, Henry' == colR.row[1].link.text()
        '04543 31233' == colR.row[2].fieldText
        'eingehend' == colR.row[3].fieldText
        'durchgeführt' == colR.row[4].fieldText
        def fs1 = fieldset[1]
        'Bemerkungen' == fs1.title
        'Herr Brackmann bittet um die Zusendung eines Angebots für die geplante Marketing-Aktion.' == fs1.row[0].htmlContent.text()
        'geplante Marketing-Aktion' == fs1.row[0].htmlContent.find('strong').text()

        timestamps.startsWith 'Erstellt am '
        page.checkToolbar 'phoneCall', call.id

        and: 'there is still one PhoneCall object'
        1 == PhoneCall.count()
    }

    def 'List phone calls'() {
        given: 'a phone phoneCall'
        def org = Organization.first()
        def p = Person.first()
        def call = prepareCall org, p

        when: 'I go to the list view'
        to CallListPage

        then: 'I get to the list view'
        waitFor { at CallListPage }
        'Anrufe' == header

        and: 'the letter bar has one clickable letter'
        1 == letterBar.availableLetters.size()
        def letter = letterBar.availableLetters[0]
        makeAbsUrl('phoneCall/list?letter=B') == letter.@href
        'B' == letter.text()

        and: 'the table contains one entry'
        1 == tr.size()
        def row = tr[0]
        row.checkTdClasses 'row-selector', 'string', 'ref', 'ref', 'date', 'status', 'status'
        row.subjectLink.checkLinkToPage CallShowPage, call.id
        'Bitte um Angebot' == row.subject
        row.organizationLink.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == row.organization
        row.personLink.checkLinkToPage PersonShowPage, p.id
        'Brackmann, Henry' == row.person
        '13.02.2013 09:15' == row.start
        'eingehend' == row.type
        'durchgeführt' == row.status
        row.checkActionButtons 'phoneCall', call.id

        and: 'there is still one PhoneCall object'
        1 == PhoneCall.count()
    }

    def 'Edit phone call successfully'() {
        given: 'a phone phoneCall'
        def org = Organization.first()
        def p = Person.first()
        def call = prepareCall org, p

        and: 'I go to the list view'
        to CallListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at CallEditPage }
        'Anrufe' == header
        'Bitte um Angebot' == subheader

        and: 'the form is pre-filled correctly'
        'Bitte um Angebot' == form.subject
        '13.02.2013' == form.start_date
        '09:15' == form.start_time
        'Landschaftsbau Duvensee GbR' == organization.input.value()
        'Henry Brackmann' == person.input.value()
        '04543 31233' == form.phone
        'INCOMING' == form.type
        'eingehend' == type.selectedText
        'COMPLETED' == form.status
        'durchgeführt' == status.selectedText
        'Herr Brackmann bittet um die Zusendung eines Angebots für die **geplante Marketing-Aktion**.' == form.notes

        when: 'I set new values and submit the form'
        form.subject = 'Fragen zur Marketing-Aktion'
        form.start_date = '14.02.2013'
        form.start_time = '13:45'
        form.phone = ''
        assert '0163 3343267' == phone.select('016')
        form.type = 'OUTGOING'
        form.status = 'PLANNED'
        form.notes = 'Sollen zur *geplanten* Marketing-Aktion auch Flyer gedruckt werden?'
        submitBtn.click()

        then: 'the phone phoneCall is updated and I get to the show view'
        waitFor { at CallShowPage }
        'Anruf Fragen zur Marketing-Aktion wurde geändert.' == flashMessage
        'Anrufe' == header
        'Fragen zur Marketing-Aktion' == subheader

        and: 'the fields are set correctly'
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL = fs0.colLeft
        'Fragen zur Marketing-Aktion' == colL.row[0].fieldText
        '14.02.2013 13:45' == colL.row[1].fieldText
        def colR = fs0.colRight
        colR.row[0].link.checkLinkToPage OrganizationShowPage
        'Landschaftsbau Duvensee GbR' == colR.row[0].link.text()
        colR.row[1].link.checkLinkToPage PersonShowPage
        'Brackmann, Henry' == colR.row[1].link.text()
        '0163 3343267' == colR.row[2].fieldText
        'ausgehend' == colR.row[3].fieldText
        'geplant' == colR.row[4].fieldText
        def fs1 = fieldset[1]
        'Bemerkungen' == fs1.title
        'Sollen zur geplanten Marketing-Aktion auch Flyer gedruckt werden?' == fs1.row[0].htmlContent.text()
        'geplanten' == fs1.row[0].htmlContent.find('em').text()

        and: 'there is still one PhoneCall object'
        1 == PhoneCall.count()
    }

    def 'Edit phone call with errors'() {
        given: 'a phone phoneCall'
        def org = Organization.first()
        def p = Person.first()
        def call = prepareCall org, p

        and: 'I go to the list view'
        to CallListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at CallEditPage }
        'Anrufe' == header
        'Bitte um Angebot' == subheader

        when: 'I clear mandatory fields and submit the form'
        form.subject = ''
        submitBtn.click()

        then: 'I get to the edit form anew and there are user errors'
        page.checkErrorFields 'subject'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list has one entry'
        waitFor { at CallListPage }
        1 == tr.size()

        and: 'there is one PhoneCall object'
        1 == PhoneCall.count()
    }

    def 'Delete phone call really'() {
        given: 'a phone phoneCall'
        def org = Organization.first()
        def p = Person.first()
        def call = prepareCall org, p

        and: 'I go to the list view'
        to CallListPage

        when: 'I click on the delete button and confirm'
        withConfirm { tr[0].deleteButton.click() }

        then: 'I get to the list view'
        waitFor { at CallListPage }
        'Anruf wurde gelöscht.' == flashMessage
        page.emptyList.check CallCreatePage, 'Anruf anlegen'

        and: 'there is no PhoneCall object'
        0 == PhoneCall.count()
    }

    def 'Delete phone call but cancel'() {
        given: 'a phone phoneCall'
        def org = Organization.first()
        def p = Person.first()
        def call = prepareCall org, p

        and: 'I go to the list view'
        to CallListPage

        when: 'I click on the delete button but cancel'
        withConfirm(false) { tr[0].deleteButton.click() }

        then: 'I am still on the list view'
        browser.isAt CallListPage

        and: 'there is still one PhoneCall object'
        1 == PhoneCall.count()
    }
}
