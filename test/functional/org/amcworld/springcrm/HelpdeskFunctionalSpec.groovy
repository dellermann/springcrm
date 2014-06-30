/*
 * HelpdeskFunctionalSpec.groovy
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

import org.amcworld.springcrm.page.HelpdeskCreatePage
import org.amcworld.springcrm.page.HelpdeskEditPage
import org.amcworld.springcrm.page.HelpdeskListPage
import org.amcworld.springcrm.page.HelpdeskShowPage
import org.amcworld.springcrm.page.OrganizationShowPage
import org.amcworld.springcrm.page.TicketListPage
import org.amcworld.springcrm.page.UserShowPage


class HelpdeskFunctionalSpec extends GeneralFunctionalTest {

    //-- Fixture methods ------------------------

    def setup() {
        prepareOrganization()
        prepareUser()
        assert 2 == User.count()

        login()
    }

    def cleanup() {
        HelpdeskUser.executeUpdate 'delete from HelpdeskUser'
        Helpdesk.executeUpdate 'delete from Helpdesk'
    }


    //-- Feature methods ------------------------

    def 'Create helpdesk successfully'() {
        given: 'an organization'
        def org = Organization.first()

        and: 'I go to the helpdesk list view'
        to HelpdeskListPage

        when: 'I click the create button'
        createBtn.click()

        then: 'I get to the create form'
        waitFor { at HelpdeskCreatePage }
        'Helpdesks' == header
        'Neuer Helpdesk' == subheader
        form.accessCode ==~ /[A-Z0-9]{6}/

        expect: 'I click on the generate code button a few times'
        for (int i = 0; i < 20; i++) {
            generateAccessCodeBtn.click()
            form.accessCode ==~ /[A-Z0-9]{6}/
        }

        when: 'I fill in this form and submit it'
        assert 'Landschaftsbau Duvensee GbR' == organization.select('Landschaftsbau')
        form.name = 'LB Duvensee'
        form.users = [1, 2]
        def lastCode = form.accessCode
        submitBtn.click()

        then: 'a call is created and I get to the show view'
        waitFor { at HelpdeskShowPage }
        'Helpdesk LB Duvensee wurde angelegt.' == flashMessage
        'Helpdesks' == header
        'LB Duvensee' == subheader

        and: 'the fields are set correctly'
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL = fs0.colLeft
        colL.row[0].link.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == colL.row[0].link.text()
        def colR = fs0.colRight
        'LB Duvensee' == colR.row[0].fieldText
        lastCode == colR.row[1].fieldText
        def url = makeAbsUrl('tickets', 'lb-duvensee', lastCode)
        url == feLink.@href
        feLink.opensNewWindow
        url == feLink.text()
        def fs1 = fieldset[1]
        'Zuständige Benutzer' == fs1.title
        users(0).checkLinkToPage UserShowPage, 1L
        'Marcus Kampe' == users(0).text()
        users(1).checkLinkToPage UserShowPage, 2L
        'Regina Wendt' == users(1).text()

        and: 'there is one Helpdesk object'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Create helpdesk with errors'() {
        given:
        assert 0 == Helpdesk.count()
        to HelpdeskListPage

        when: 'I click the create button'
        createBtn.click()

        then: 'I get to the create form'
        waitFor { at HelpdeskCreatePage }
        'Helpdesks' == header
        'Neuer Helpdesk' == subheader

        when: 'I submit the form without filling in any value'
        form.accessCode = ''
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at HelpdeskCreatePage }
        page.checkErrorFields 'organization', 'name', 'accessCode', 'users'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list is empty'
        waitFor { at HelpdeskListPage }
        page.emptyList.check HelpdeskCreatePage, 'Helpdesk anlegen'

        and: 'no Helpdesk object has been created'
        0 == Helpdesk.count()
        0 == HelpdeskUser.count()
    }

    def 'Show helpdesk'() {
        given: 'a helpdesk'
        def org = Organization.first()
        def helpdesk = prepareHelpdesk org
        def userList = User.list()

        and:
        to HelpdeskListPage

        when: 'I click an item to show'
        tr[0].nameLink.click()

        then: 'I get the show view with all data'
        waitFor { at HelpdeskShowPage }
        'Helpdesks' == header
        'LB Duvensee' == subheader
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL = fs0.colLeft
        colL.row[0].link.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == colL.row[0].link.text()
        def colR = fs0.colRight
        'LB Duvensee' == colR.row[0].fieldText
        '4A51VZ' == colR.row[1].fieldText
        def url = makeAbsUrl('tickets', 'lb-duvensee', '4A51VZ')
        url == feLink.@href
        feLink.opensNewWindow
        url == feLink.text()
        def fs1 = fieldset[1]
        'Zuständige Benutzer' == fs1.title
        users(0).checkLinkToPage UserShowPage, userList[0].id
        'Marcus Kampe' == users(0).text()
        users(1).checkLinkToPage UserShowPage, userList[1].id
        'Regina Wendt' == users(1).text()
        timestamps.startsWith 'Erstellt am '
        page.checkToolbar 'helpdesk', helpdesk.id

        and: 'the action buttons are set correctly'
        2 == actionButtons.size()
        def btn0 = actionButtons[0]
        btn0.checkColor 'white'
        btn0.checkSize 'medium'
        url == btn0.@href
        btn0.opensNewWindow
        'Kundenansicht' == btn0.text()
        def btn1 = actionButtons[1]
        btn1.checkColor 'white'
        btn1.checkSize 'medium'
        btn1.checkLinkToPage TicketListPage, '?helpdesk=' + helpdesk.id
        'Tickets anzeigen' == btn1.text()

        and: 'there is still one Helpdesk object'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'List helpdesks'() {
        given: 'a helpdesk'
        def org = Organization.first()
        def helpdesk = prepareHelpdesk(org)

        when: 'I go to the list view'
        to HelpdeskListPage

        then: 'I get to the list view'
        waitFor { at HelpdeskListPage }
        'Helpdesks' == header

        and: 'the table contains one entry'
        1 == tr.size()
        def row = tr[0]
        row.checkTdClasses 'row-selector', 'string', 'string', 'ref', 'string'
        row.nameLink.checkLinkToPage HelpdeskShowPage, helpdesk.id
        'LB Duvensee' == row.name
        '4A51VZ' == row.accessCode
        row.organizationLink.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == row.organization
        'Marcus Kampe, Regina Wendt' == row.users
        row.checkActionButtons 'lb-duvensee', '4A51VZ', helpdesk.id

        and: 'there is still one Helpdesk object'
        1 == Helpdesk.count()
    }

    def 'Edit helpdesk successfully'() {
        given: 'a helpdesk'
        def org = Organization.first()
        def helpdesk = prepareHelpdesk org
        def userList = User.list()

        and: 'I go to the list view'
        to HelpdeskListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at HelpdeskEditPage }
        'Helpdesks' == header
        'LB Duvensee' == subheader

        and: 'the form is pre-filled correctly'
        'Landschaftsbau Duvensee GbR' == organization.input.value()
        'LB Duvensee' == form.name
        '4A51VZ' == form.accessCode
        userList*.id == form.users*.toLong()

        when: 'I set new values and submit the form'
        form.name = 'L-Bau Duvensee'
        form.accessCode = '7BY92I'
        form.users = [userList[0].id]
        submitBtn.click()

        then: 'the helpdesk is updated and I get to the show view'
        waitFor { at HelpdeskShowPage }
        'Helpdesk L-Bau Duvensee wurde geändert.' == flashMessage
        'Helpdesks' == header
        'L-Bau Duvensee' == subheader

        and: 'the fields are set correctly'
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL = fs0.colLeft
        colL.row[0].link.checkLinkToPage OrganizationShowPage, org.id
        'Landschaftsbau Duvensee GbR' == colL.row[0].link.text()
        def colR = fs0.colRight
        'L-Bau Duvensee' == colR.row[0].fieldText
        '7BY92I' == colR.row[1].fieldText
        def url = makeAbsUrl('tickets', 'l-bau-duvensee', '7BY92I')
        url == feLink.@href
        feLink.opensNewWindow
        url == feLink.text()
        def fs1 = fieldset[1]
        'Zuständige Benutzer' == fs1.title
        users(0).checkLinkToPage UserShowPage, 1L
        'Marcus Kampe' == users(0).text()

        and: 'there is still one Helpdesk object'
        1 == Helpdesk.count()
    }

    def 'Edit helpdesk with errors'() {
        given: 'a helpdesk'
        def org = Organization.first()
        def helpdesk = prepareHelpdesk org

        and: 'I go to the list view'
        to HelpdeskListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at HelpdeskEditPage }
        'Helpdesks' == header
        'LB Duvensee' == subheader

        when: 'I clear mandatory fields and submit the form'
        form.name = ''
        form.accessCode = ''
        form.users = []
        submitBtn.click()

        then: 'I get to the edit form anew and there are user errors'
        page.checkErrorFields 'name', 'accessCode', 'users'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list has one entry'
        waitFor { at HelpdeskListPage }
        1 == tr.size()

        and: 'there is one Helpdesk object'
        1 == Helpdesk.count()
    }

    def 'Delete helpdesk really'() {
        given: 'a helpdesk'
        def org = Organization.first()
        def helpdesk = prepareHelpdesk org

        and: 'I go to the list view'
        to HelpdeskListPage

        when: 'I click on the delete button and confirm'
        withConfirm { tr[0].deleteButton.click() }

        then: 'I get to the list view'
        waitFor { at HelpdeskListPage }
        'Helpdesk wurde gelöscht.' == flashMessage
        page.emptyList.check HelpdeskCreatePage, 'Helpdesk anlegen'

        and: 'there is no Helpdesk object'
        0 == Helpdesk.count()
    }

    def 'Delete helpdesk but cancel'() {
        given: 'a helpdesk'
        def org = Organization.first()
        def helpdesk = prepareHelpdesk org

        and: 'I go to the list view'
        to HelpdeskListPage

        when: 'I click on the delete button but cancel'
        withConfirm(false) { tr[0].deleteButton.click() }

        then: 'I am still on the list view'
        browser.isAt HelpdeskListPage

        and: 'there is still one Helpdesk object'
        1 == Helpdesk.count()
    }
}
