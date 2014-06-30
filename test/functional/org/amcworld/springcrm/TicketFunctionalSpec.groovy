/*
 * TicketFunctionalSpec.groovy
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

import org.amcworld.springcrm.page.HelpdeskShowPage
import org.amcworld.springcrm.page.TicketCreatePage
import org.amcworld.springcrm.page.TicketEditPage
import org.amcworld.springcrm.page.TicketListPage
import org.amcworld.springcrm.page.TicketShowPage
import spock.lang.Shared


class TicketFunctionalSpec extends GeneralFunctionalTest {

    @Shared def mailData


    //-- Fixture methods ------------------------

    def setup() {
        def org = prepareOrganization()
        prepareUser()
        assert 2 == User.count()
        prepareHelpdesk org

        TicketService.metaClass.sendMail = { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
        }

        login()
    }

    def cleanup() {
        Ticket.executeUpdate 'delete from Ticket'
        TicketLogEntry.executeUpdate 'delete from TicketLogEntry'
        HelpdeskUser.executeUpdate 'delete from HelpdeskUser'
        Helpdesk.executeUpdate 'delete from Helpdesk'
    }


    //-- Feature methods ------------------------

    def 'Create ticket successfully'() {
        given: 'a helpdesk'
        def hd = Helpdesk.first()

        and: 'I go to the ticket list view'
        to TicketListPage

        when: 'I click the create button'
        createBtn.click()

        then: 'I get to the create form'
        waitFor { at TicketCreatePage }
        'Tickets' == header
        'Neues Ticket' == subheader
        '10000' == form.number
        form.number().disabled
        hd.id == helpdesk.value().toLong()
        'LB Duvensee' == helpdesk.selectedText

        when: 'I fill in this form and submit it'
        form.subject = 'Drucker im Verkauf funktioniert nicht'
        form.'priority.id' = 1102
        form.'salutation.id' = 2
        form.firstName = 'Marlen'
        form.lastName = 'Thoss'
        form.phone = '04543 31234'
        form.mobile = '0170 1896043'
        form.fax = '04543 31235'
        form.email1 = 'm.thoss@landschaftsbau-duvensee.example'
        form.'address.street' = 'Dörpstraat 25'
        form.'address.postalCode' = '23898'
        form.'address.location' = 'Duvensee'
        form.'address.state' = 'Schleswig-Holstein'
        form.'address.country' = 'Deutschland'
        form.messageText = '''Ich habe versucht, auf Drucker **3** im _Verkauf_ zu drucken, allerdings kommt kein Ausdruck heraus.

Der Drucker zeigt nur an: „Bereit für Druck“. Das Problem besteht seit gestern.'''
        submitBtn.click()

        then: 'a call is created and I get to the show view'
        waitFor { at TicketShowPage }
        'Ticket Drucker im Verkauf funktioniert nicht wurde angelegt.' == flashMessage
        'Tickets' == header
        'Drucker im Verkauf funktioniert nicht' == subheader

        and: 'the fields are set correctly'
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL0 = fs0.colLeft
        'T-10000' == colL0.row[0].fieldText
        colL0.row[1].link.checkLinkToPage HelpdeskShowPage, hd.id
        'LB Duvensee' == colL0.row[1].link.text()
        'Drucker im Verkauf funktioniert nicht' == colL0.row[2].fieldText
        def colR0 = fs0.colRight
        'erstellt' == colR0.row[0].fieldText
        'hoch' == colR0.row[1].fieldText
        'Kunde' == colR0.row[2].fieldText
        '' == colR0.row[3].fieldText
        def fs1 = fieldset[1]
        'Kundendaten' == fs1.title
        def colL1 = fs1.colLeft
        'Frau' == colL1.row[0].fieldText
        'Marlen' == colL1.row[1].fieldText
        'Thoss' == colL1.row[2].fieldText
        '04543 31234' == colL1.row[3].fieldText
        '' == colL1.row[4].fieldText
        '0170 1896043' == colL1.row[5].fieldText
        '04543 31235' == colL1.row[6].fieldText
        'mailto:m.thoss@landschaftsbau-duvensee.example' == colL1.row[7].link.@href
        'm.thoss@landschaftsbau-duvensee.example' == colL1.row[7].link.text()
        '' == colL1.row[8].fieldText
        'Dörpstraat 25' == address.street
        '' == address.poBox
        '23898' == address.postalCode
        'Duvensee' == address.location
        'Schleswig-Holstein' == address.state
        'Deutschland' == address.country
        address.checkMapButton()

        and: 'two activies were stored'
        'Aktivitätenverlauf' == fieldset[2].title
        2 == logEntries.size()
        checkDefaultLogEntries()

        and: 'there is one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Create ticket with errors'() {
        given:
        assert 0 == Ticket.count()
        to TicketListPage

        when: 'I click the create button'
        createBtn.click()

        then: 'I get to the create form'
        waitFor { at TicketCreatePage }
        'Tickets' == header
        'Neues Ticket' == subheader

        when: 'I submit the form without filling in any value'
        submitBtn.click()

        then: 'I get to the create form anew and there are user errors'
        waitFor { at TicketCreatePage }
        page.checkErrorFields 'subject', 'firstName', 'lastName', 'phone',
            'phoneHome', 'mobile', 'email1', 'email2', 'messageText'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list is empty'
        waitFor { at TicketListPage }
        page.emptyList.check TicketCreatePage, 'Ticket anlegen'

        and: 'no Ticket object has been created'
        0 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Show ticket'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and:
        to TicketListPage

        when: 'I click an item to show'
        tr[0].numberLink.click()

        then: 'I get the show view with all data'
        waitFor { at TicketShowPage }
        'Tickets' == header
        'Drucker im Verkauf funktioniert nicht' == subheader
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL0 = fs0.colLeft
        'T-10000' == colL0.row[0].fieldText
        colL0.row[1].link.checkLinkToPage HelpdeskShowPage, hd.id
        'LB Duvensee' == colL0.row[1].link.text()
        'Drucker im Verkauf funktioniert nicht' == colL0.row[2].fieldText
        def colR0 = fs0.colRight
        'erstellt' == colR0.row[0].fieldText
        'hoch' == colR0.row[1].fieldText
        'Kunde' == colR0.row[2].fieldText
        '' == colR0.row[3].fieldText
        def fs1 = fieldset[1]
        'Kundendaten' == fs1.title
        def colL1 = fs1.colLeft
        'Frau' == colL1.row[0].fieldText
        'Marlen' == colL1.row[1].fieldText
        'Thoss' == colL1.row[2].fieldText
        '04543 31234' == colL1.row[3].fieldText
        '' == colL1.row[4].fieldText
        '0170 1896043' == colL1.row[5].fieldText
        '04543 31235' == colL1.row[6].fieldText
        'mailto:m.thoss@landschaftsbau-duvensee.example' == colL1.row[7].link.@href
        'm.thoss@landschaftsbau-duvensee.example' == colL1.row[7].link.text()
        '' == colL1.row[8].fieldText
        'Dörpstraat 25' == address.street
        '' == address.poBox
        '23898' == address.postalCode
        'Duvensee' == address.location
        'Schleswig-Holstein' == address.state
        'Deutschland' == address.country
        address.checkMapButton()

        and: 'two activies were stored'
        'Aktivitätenverlauf' == fieldset[2].title
        2 == logEntries.size()
        checkDefaultLogEntries()

        and: 'the action buttons are set correctly'
        checkActionButtonsCreated ticket, users.tail()

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Take on ticket'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view'
        to TicketShowPage, ticket.id

        when: 'I click the button to take on the ticket but cancel'
        String msg = withConfirm(false) { takeOnButton.click() }

        then:
        'Wollen Sie das Ticket wirklich übernehmen?' == msg
        isAt TicketShowPage
        '' == fieldset[0].colRight.row[3].fieldText

        and: 'the action buttons are unchanged'
        checkActionButtonsCreated ticket, users.tail()

        when: 'I click the button to take on the ticket and confirm'
        withConfirm { takeOnButton.click() }

        then:
        isAt TicketShowPage
        'zugewiesen' == fieldset[0].colRight.row[0].fieldText
        'Marcus Kampe' == fieldset[0].colRight.row[3].fieldText

        and: 'the action button have changed'
        checkActionButtonsAssigned ticket, users.tail(), users

        and: 'there is a new log entry'
        3 == logEntries.size()
        logEntries[0].entry.check 'Marcus Kampe', 'Marcus Kampe'
        checkDefaultLogEntries 1

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Send message to customer'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view'
        to TicketShowPage, ticket.id

        when: 'I click the button to send a message'
        sendMsgToCustomerButton.click()

        then: 'I am still on the show view and a dialog has been opened'
        isAt TicketShowPage
        sendMsgDialog.displayed
        'Nachricht an Kunden senden' == sendMsgDialog.title

        when: 'I fill in the form but cancel'
        sendMsgDialog.find('#messageText').value '''Können Sie uns bitte die Druckernummer (das ist eine Zahl in der Form **PRT-xxx**) mitteilen?

Vielen Dank.'''
        sendMsgDialog.buttons[1].click()

        then: 'the dialog disappears'
        !sendMsgDialog.displayed
        isAt TicketShowPage
        2 == logEntries.size()
        checkDefaultLogEntries()

        when: 'I click the button again, fill in the form, and submit'
        sendMsgToCustomerButton.click()
        sendMsgDialog.find('#messageText').value '''Können Sie uns bitte die Druckernummer (das ist eine Zahl in der Form **PRT-xxx**) mitteilen?

Vielen Dank.'''
        sendMsgDialog.buttons[0].click()

        then: 'I get to the show view with a new log entry'
        waitFor { at TicketShowPage }
        3 == logEntries.size()
        logEntries[0].entry.check('Marcus Kampe', 'Kunde') {
            assert 2 == it.p.size()
            assert 'Können Sie uns bitte die Druckernummer (das ist eine Zahl in der Form PRT-xxx) mitteilen?' == it.p[0].text()
            assert 'PRT-xxx' == it.p[0].find('strong').text()
            assert 'Vielen Dank.' == it.p[1].text()
        }
        checkDefaultLogEntries 1

        and: 'the mail sent contains the correct values'
        'SpringCRM Service <noreply@springcrm.de>' == mailData.from.text()
        'm.thoss@landschaftsbau-duvensee.example' == mailData.to.text()
        'true' == mailData.multipart.text()
        'Neue Nachricht zu Ticket' == mailData.subject.text()
        mailData.text.text().startsWith '''Guten Tag Frau Marlen Thoss,

das Helpdesk-Team hat Ihnen eine neue Nachricht zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht mit folgenden Daten geschickt:'''
        mailData.html.text().startsWith '''<p>Guten Tag Frau Marlen Thoss,</p><p>das Helpdesk-Team hat Ihnen eine neue Nachricht zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht mit folgenden Daten geschickt:</p>'''

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Send message to user'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view'
        to TicketShowPage, ticket.id

        when: 'I click the button to send a message'
        sendMsgToUserButton.selectItem 0, 'Regina Wendt'

        then: 'I am still on the show view and a dialog has been opened'
        isAt TicketShowPage
        sendMsgDialog.displayed
        'Nachricht an Benutzer senden' == sendMsgDialog.title

        when: 'I fill in the form but cancel'
        sendMsgDialog.find('#messageText').value '''Würden Sie sich bitte um diesen Fall kümmern?

Vielen Dank.'''
        sendMsgDialog.buttons[1].click()

        then: 'the dialog disappears'
        !sendMsgDialog.displayed
        isAt TicketShowPage
        2 == logEntries.size()
        checkDefaultLogEntries()

        when: 'I click the button again, fill in the form, and submit'
        sendMsgToUserButton.selectItem 0, 'Regina Wendt'
        sendMsgDialog.find('#messageText').value '''Würden Sie sich bitte um diesen Fall kümmern?

Vielen Dank.'''
        sendMsgDialog.buttons[0].click()

        then: 'I get to the show view with a new log entry'
        waitFor { at TicketShowPage }
        3 == logEntries.size()
        logEntries[0].entry.check('Marcus Kampe', 'Regina Wendt') {
            assert 2 == it.p.size()
            assert 'Würden Sie sich bitte um diesen Fall kümmern?' == it.p[0].text()
            assert 'Vielen Dank.' == it.p[1].text()
        }
        checkDefaultLogEntries 1

        and: 'the mail sent contains the correct values'
        'SpringCRM Service <noreply@springcrm.de>' == mailData.from.text()
        'r.wendt@kampe.example' == mailData.to.text()
        'true' == mailData.multipart.text()
        'Neue Nachricht zu Ticket' == mailData.subject.text()
        mailData.text.text().startsWith '''Liebes Helpdesk-Team,

zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht wurde eine neue Nachricht mit folgenden Daten verschickt:'''
        mailData.html.text().startsWith '''<p>Liebes Helpdesk-Team,</p><p>zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht wurde eine neue Nachricht mit folgenden Daten verschickt:</p>'''

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Add note'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view'
        to TicketShowPage, ticket.id

        when: 'I click the button to add a note'
        createNoteButton.click()

        then: 'I am still on the show view and a dialog has been opened'
        isAt TicketShowPage
        sendMsgDialog.displayed
        'Notiz erstellen' == sendMsgDialog.title

        when: 'I fill in the form but cancel'
        sendMsgDialog.find('#messageText').value '''# Hinweis zur weiteren Vorgehensweise

- Kunden aufsuchen
- Drucker überprüfen
- ggf. Toner wechseln'''
        sendMsgDialog.buttons[1].click()

        then: 'the dialog disappears'
        !sendMsgDialog.displayed
        isAt TicketShowPage
        2 == logEntries.size()
        checkDefaultLogEntries()

        when: 'I click the button again, fill in the form, and submit'
        createNoteButton.click()
        sendMsgDialog.find('#messageText').value '''# Hinweis zur weiteren Vorgehensweise

- Kunden aufsuchen
- Drucker überprüfen
- ggf. Toner wechseln'''
        sendMsgDialog.buttons[0].click()

        then: 'I get to the show view with a new log entry'
        waitFor { at TicketShowPage }
        3 == logEntries.size()
        logEntries[0].entry.check('Marcus Kampe') {
            assert 'Hinweis zur weiteren Vorgehensweise' == it.find('h1').text()
            def ul = it.find('ul')
            assert 'Kunden aufsuchen' == ul.find('li', 0).text()
            assert 'Drucker überprüfen' == ul.find('li', 1).text()
            assert 'ggf. Toner wechseln' == ul.find('li', 2).text()
        }
        checkDefaultLogEntries 1

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Change to in process'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view and take on the ticket'
        to TicketShowPage, ticket.id
        withConfirm { takeOnButton.click() }

        when: 'I change to in process'
        changeToInProcessButton.click()

        then: 'I get to the show view with the new status'
        waitFor { at TicketShowPage }
        'in Bearbeitung' == fieldset[0].colRight.row[0].fieldText

        and: 'the action button have changed'
        checkActionButtonsInProcess ticket, users.tail(), users

        and: 'there is a new log entry'
        4 == logEntries.size()
        logEntries[0].entry.check 'Marcus Kampe', 'in Bearbeitung'
        logEntries[1].entry.check 'Marcus Kampe', 'Marcus Kampe'
        checkDefaultLogEntries 2

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Assign to another user'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view and take on the ticket'
        to TicketShowPage, ticket.id
        withConfirm { takeOnButton.click() }

        when: 'I assign the ticket to a new user'
        assignUserButton.selectItem 1, 'Regina Wendt'

        then: 'I get to the show view'
        waitFor { at TicketShowPage }
        'zugewiesen' == fieldset[0].colRight.row[0].fieldText
        'Regina Wendt' == fieldset[0].colRight.row[3].fieldText

        and: 'the action button have changed'
        checkActionButtonsAssigned ticket, users.tail(), users

        and: 'there is a new log entry'
        4 == logEntries.size()
        logEntries[0].entry.check 'Marcus Kampe', 'Regina Wendt'
        logEntries[1].entry.check 'Marcus Kampe', 'Marcus Kampe'
        checkDefaultLogEntries 2

        and: 'the mail sent contains the correct values'
        'SpringCRM Service <noreply@springcrm.de>' == mailData.from.text()
        'r.wendt@kampe.example' == mailData.to.text()
        'true' == mailData.multipart.text()
        'Ticket zugewiesen' == mailData.subject.text()
        mailData.text.text().startsWith '''Hallo Regina Wendt,

das Ticket mit den folgenden Daten wurde Ihnen von Marcus Kampe zugewiesen. Bitte bearbeiten Sie dieses Ticket.'''
        mailData.html.text().startsWith '''<p>Hallo Regina Wendt,</p><p>das Ticket mit den folgenden Daten wurde Ihnen von Marcus Kampe zugewiesen. Bitte bearbeiten Sie dieses Ticket.</p>'''

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Assign to myself'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view and take on the ticket'
        to TicketShowPage, ticket.id
        withConfirm { takeOnButton.click() }

        when: 'I assign the ticket to myself'
        assignUserButton.selectItem 0, 'Marcus Kampe'

        then: 'I get to the show view'
        waitFor { at TicketShowPage }
        'zugewiesen' == fieldset[0].colRight.row[0].fieldText
        'Marcus Kampe' == fieldset[0].colRight.row[3].fieldText

        and: 'the action button have changed'
        checkActionButtonsAssigned ticket, users.tail(), users

        and: 'there is no new log entry'
        3 == logEntries.size()
        logEntries[0].entry.check 'Marcus Kampe', 'Marcus Kampe'
        checkDefaultLogEntries 1

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Close ticket'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view and take on the ticket'
        to TicketShowPage, ticket.id
        withConfirm { takeOnButton.click() }

        when: 'I click the button to close the ticket but cancel'
        String msg = withConfirm(false) { closeButton.click() }

        then:
        'Wollen Sie das Ticket wirklich schließen?' == msg
        isAt TicketShowPage
        'zugewiesen' == fieldset[0].colRight.row[0].fieldText

        and: 'the action buttons are unchanged'
        checkActionButtonsAssigned ticket, users.tail(), users

        when: 'I click the button to close the ticket and confirm'
        withConfirm { closeButton.click() }

        then: 'I get to the show view'
        waitFor { at TicketShowPage }
        'geschlossen' == fieldset[0].colRight.row[0].fieldText
        'Marcus Kampe' == fieldset[0].colRight.row[3].fieldText

        and: 'the action button have changed'
        checkActionButtonsClosed ticket, users.tail()

        and: 'there is a new log entry'
        4 == logEntries.size()
        logEntries[0].entry.check 'Marcus Kampe', 'geschlossen'
        logEntries[1].entry.check 'Marcus Kampe', 'Marcus Kampe'
        checkDefaultLogEntries 2

        and: 'two e-mails have been sent'
        'SpringCRM Service <noreply@springcrm.de>' == mailData.from.text()
        'm.thoss@landschaftsbau-duvensee.example' == mailData.to.text()
        'true' == mailData.multipart.text()
        'Ticket geschlossen' == mailData.subject.text()
        mailData.text.text().startsWith '''Guten Tag Frau Marlen Thoss,

das Ticket mit den folgenden Daten wurde geschlossen. Sollte das Problem weiter bestehen, können Sie das Ticket jederzeit wieder öffnen (Wiedervorlage).'''
        mailData.html.text().startsWith '''<p>Guten Tag Frau Marlen Thoss,</p><p>das Ticket mit den folgenden Daten wurde geschlossen. Sollte das Problem weiter bestehen, können Sie das Ticket jederzeit wieder öffnen (Wiedervorlage).</p>'''

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Resubmit ticket'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the show view and take on the ticket'
        to TicketShowPage, ticket.id
        withConfirm { takeOnButton.click() }
        waitFor { at TicketShowPage }
        withConfirm { closeButton.click() }

        when: 'I click the button to resubmit the ticket'
        resubmitButton.click()

        then: 'I get to the show view'
        waitFor { at TicketShowPage }
        'Wiedervorlage' == fieldset[0].colRight.row[0].fieldText
        'Marcus Kampe' == fieldset[0].colRight.row[3].fieldText

        and: 'the action button have changed'
        checkActionButtonsCreated ticket, users.tail()

        and: 'there is a new log entry'
        5 == logEntries.size()
        logEntries[0].entry.check 'Marcus Kampe', 'Wiedervorlage'
        logEntries[1].entry.check 'Marcus Kampe', 'geschlossen'
        logEntries[2].entry.check 'Marcus Kampe', 'Marcus Kampe'
        checkDefaultLogEntries 3

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'List tickets'() {
        given: 'a helpdesk and a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        when: 'I go to the list view'
        to TicketListPage

        then: 'I get to the list view'
        waitFor { at TicketListPage }
        'Tickets' == header

        and: 'the table contains one entry'
        1 == tr.size()
        def row = tr[0]
        row.checkTdClasses 'row-selector', 'string', 'string', 'ref', 'status', 'string', 'date'
        row.numberLink.checkLinkToPage TicketShowPage, ticket.id
        'T-10000' == row.number
        row.subjectLink.checkLinkToPage TicketShowPage, ticket.id
        'Drucker im Verkauf funktioniert nicht' == row.subject
        row.helpdeskLink.checkLinkToPage HelpdeskShowPage, hd.id
        'LB Duvensee' == row.helpdesk
        'erstellt' == row.stage
        'ticket-stage-created' in row.stageClasses
        'Thoss, Marlen' == row.customerNames
        row.checkActionButtons 'ticket', ticket.id

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        2 == TicketLogEntry.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Edit ticket successfully'() {
        given: 'a helpdesk and a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        def users = User.list()

        and: 'I go to the list view'
        to TicketListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at TicketEditPage }
        'Tickets' == header
        'Drucker im Verkauf funktioniert nicht' == subheader

        and: 'the form is pre-filled correctly'
        '10000' == form.number
        form.number().disabled
        hd.id == helpdesk.value().toLong()
        'LB Duvensee' == helpdesk.selectedText
        'Drucker im Verkauf funktioniert nicht' == form.subject
        '1102' == form.'priority.id'
        '2' == form.'salutation.id'
        'Marlen' == form.firstName
        'Thoss' == form.lastName
        '04543 31234' == form.phone
        '0170 1896043' == form.mobile
        '04543 31235' == form.fax
        'm.thoss@landschaftsbau-duvensee.example' == form.email1
        'Dörpstraat 25' == form.'address.street'
        '23898' == form.'address.postalCode'
        'Duvensee' == form.'address.location'
        'Schleswig-Holstein' == form.'address.state'
        'Deutschland' == form.'address.country'

        when: 'I set new values and submit the form'
        form.subject = 'Drucker in der Buchhaltung funktioniert nicht'
        form.'priority.id' = 1101
        submitBtn.click()

        then: 'the ticket is updated and I get to the show view'
        waitFor { at TicketShowPage }
        'Ticket Drucker in der Buchhaltung funktioniert nicht wurde geändert.' == flashMessage
        'Tickets' == header
        'Drucker in der Buchhaltung funktioniert nicht' == subheader

        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL0 = fs0.colLeft
        'T-10000' == colL0.row[0].fieldText
        colL0.row[1].link.checkLinkToPage HelpdeskShowPage, hd.id
        'LB Duvensee' == colL0.row[1].link.text()
        'Drucker in der Buchhaltung funktioniert nicht' == colL0.row[2].fieldText
        def colR0 = fs0.colRight
        'erstellt' == colR0.row[0].fieldText
        'normal' == colR0.row[1].fieldText
        'Kunde' == colR0.row[2].fieldText
        '' == colR0.row[3].fieldText
        def fs1 = fieldset[1]
        'Kundendaten' == fs1.title
        def colL1 = fs1.colLeft
        'Frau' == colL1.row[0].fieldText
        'Marlen' == colL1.row[1].fieldText
        'Thoss' == colL1.row[2].fieldText
        '04543 31234' == colL1.row[3].fieldText
        '' == colL1.row[4].fieldText
        '0170 1896043' == colL1.row[5].fieldText
        '04543 31235' == colL1.row[6].fieldText
        'mailto:m.thoss@landschaftsbau-duvensee.example' == colL1.row[7].link.@href
        'm.thoss@landschaftsbau-duvensee.example' == colL1.row[7].link.text()
        '' == colL1.row[8].fieldText
        'Dörpstraat 25' == address.street
        '' == address.poBox
        '23898' == address.postalCode
        'Duvensee' == address.location
        'Schleswig-Holstein' == address.state
        'Deutschland' == address.country
        address.checkMapButton()

        and: 'two activies were stored'
        'Aktivitätenverlauf' == fieldset[2].title
        2 == logEntries.size()
        checkDefaultLogEntries()

        and: 'the action buttons are set correctly'
        checkActionButtonsCreated ticket, users.tail()

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Edit ticket with errors'() {
        given: 'a helpdesk and a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)
        assert 2 == TicketLogEntry.count()

        and: 'I go to the list view'
        to TicketListPage

        when: 'I click on the edit button'
        tr[0].editButton.click()

        then: 'I get to the edit form'
        waitFor { at TicketEditPage }
        'Tickets' == header
        'Drucker im Verkauf funktioniert nicht' == subheader

        when: 'I clear mandatory fields and submit the form'
        form.subject = ''
        form.firstName = ''
        form.lastName = ''
        form.phone = ''
        form.phoneHome = ''
        form.mobile = ''
        form.email1 = ''
        form.email2 = ''
        submitBtn.click()

        then: 'I get to the edit form anew and there are user errors'
        page.checkErrorFields 'subject', 'firstName', 'lastName', 'phone',
            'phoneHome', 'mobile', 'email1', 'email2'

        when: 'I click the cancel button'
        cancelBtn.click()

        then: 'I get to the list view and the list has one entry'
        waitFor { at TicketListPage }
        1 == tr.size()

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        2 == TicketLogEntry.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Delete ticket really'() {
        given: 'a helpdesk and a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        and: 'I go to the list view'
        to TicketListPage

        when: 'I click on the delete button and confirm'
        withConfirm { tr[0].deleteButton.click() }

        then: 'I get to the list view'
        waitFor { at TicketListPage }
        'Ticket wurde gelöscht.' == flashMessage
        page.emptyList.check TicketCreatePage, 'Ticket anlegen'

        and: 'there is no Ticket object'
        0 == Ticket.count()
        0 == TicketLogEntry.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }

    def 'Delete ticket but cancel'() {
        given: 'a helpdesk and a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        and: 'I go to the list view'
        to TicketListPage

        when: 'I click on the delete button but cancel'
        withConfirm(false) { tr[0].deleteButton.click() }

        then: 'I am still on the list view'
        browser.isAt TicketListPage

        and: 'there is still one Ticket object'
        1 == Ticket.count()
        2 == TicketLogEntry.count()
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
    }


    //-- Non-public methods ---------------------

    protected void checkActionButtonsAssigned(Ticket ticket,
                                              List<User> usersMessage,
                                              List<User> usersAssignTo)
    {
        def btn = actionButtons[0]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'send-message-to-customer-btn' == btn.@id
        assert 'Nachricht an Kunden' == btn.text()

        def btnGroup = actionButtonGroups[0]
        btn = btnGroup.button
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'Nachr. an Ben.' == btn.text()
        assert usersMessage.size() == btnGroup.dropdownMenuItems.size()
        for (int i = 0; i < usersMessage.size(); i++) {
            User user = usersMessage[i]
            def a = btnGroup.dropdownMenuLinks(i)
            assert user.id == a.@'data-user-id'.toLong()
//            assert user.toString() == a.text()    // not visible → text() == ''
        }

        btn = actionButtons[1]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'pencil'
        assert 'create-note-btn' == btn.@id
        assert 'Notiz erstellen' == btn.text()

        btn = actionButtons[2]
        btn.checkColor 'green'
        btn.checkSize 'medium'
        assert makeAbsUrl('ticket', 'change-stage', ticket.id, '?stage=inProcess') == btn.@href
        assert 'In Bearbeitung setzen' == btn.text()

        btnGroup = actionButtonGroups[1]
        btn = btnGroup.button
        btn.checkColor 'blue'
        btn.checkSize 'medium'
        assert 'assign-user-menu' == btn.@id
        assert 'Benutzer zuweisen' == btn.text()
        assert usersAssignTo.size() == btnGroup.dropdownMenuItems.size()
        for (int i = 0; i < usersAssignTo.size(); i++) {
            User user = usersAssignTo[i]
            def a = btnGroup.dropdownMenuLinks(i)
            assert makeAbsUrl('ticket', 'assign-to-user', ticket.id, '?user=' + user.id) == a.@href
//            assert user.toString() == a.text()    // not visible → text() == ''
        }

        btn = actionButtons[3]
        btn.checkColor 'red'
        btn.checkSize 'medium'
        assert 'close-ticket-btn' == btn.@id
        assert makeAbsUrl('ticket', 'change-stage', ticket.id, '?stage=closed') == btn.@href
        assert 'Ticket schließen' == btn.text()
    }

    protected void checkActionButtonsClosed(Ticket ticket, List<User> users) {
        def btn = actionButtons[0]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'send-message-to-customer-btn' == btn.@id
        assert 'Nachricht an Kunden' == btn.text()

        def btnGroup = actionButtonGroups[0]
        btn = btnGroup.button
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'Nachr. an Ben.' == btn.text()
        assert users.size() == btnGroup.dropdownMenuItems.size()
        for (int i = 0; i < users.size(); i++) {
            User user = users[i]
            def a = btnGroup.dropdownMenuLinks(i)
            assert user.id == a.@'data-user-id'.toLong()
//            assert user.toString() == a.text()    // not visible → text() == ''
        }

        btn = actionButtons[1]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'pencil'
        assert 'create-note-btn' == btn.@id
        assert 'Notiz erstellen' == btn.text()

        btn = actionButtons[2]
        btn.checkColor 'orange'
        btn.checkSize 'medium'
        btn.checkIcon 'share-square-o'
        assert 'Wiedervorlage' == btn.text()
    }

    protected void checkActionButtonsCreated(Ticket ticket, List<User> users) {
        def btn = actionButtons[0]
        btn.checkColor 'green'
        btn.checkSize 'medium'
        assert 'take-on-btn' == btn.@id
        assert makeAbsUrl('ticket', 'take-on', ticket.id) == btn.@href
        assert 'Ticket übernehmen' == btn.text()

        btn = actionButtons[1]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'send-message-to-customer-btn' == btn.@id
        assert 'Nachricht an Kunden' == btn.text()

        def btnGroup = actionButtonGroups[0]
        btn = btnGroup.button
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'Nachr. an Ben.' == btn.text()
        assert users.size() == btnGroup.dropdownMenuItems.size()
        for (int i = 0; i < users.size(); i++) {
            User user = users[i]
            def a = btnGroup.dropdownMenuLinks(i)
            assert user.id == a.@'data-user-id'.toLong()
//            assert user.toString() == a.text()    // not visible → text() == ''
        }

        btn = actionButtons[2]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'pencil'
        assert 'create-note-btn' == btn.@id
        assert 'Notiz erstellen' == btn.text()
    }

    protected void checkActionButtonsInProcess(Ticket ticket,
                                               List<User> usersMessage,
                                               List<User> usersAssignTo)
    {
        def btn = actionButtons[0]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'send-message-to-customer-btn' == btn.@id
        assert 'Nachricht an Kunden' == btn.text()

        def btnGroup = actionButtonGroups[0]
        btn = btnGroup.button
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'envelope-o'
        assert 'Nachr. an Ben.' == btn.text()
        assert usersMessage.size() == btnGroup.dropdownMenuItems.size()
        for (int i = 0; i < usersMessage.size(); i++) {
            User user = usersMessage[i]
            def a = btnGroup.dropdownMenuLinks(i)
            assert user.id == a.@'data-user-id'.toLong()
//            assert user.toString() == a.text()    // not visible → text() == ''
        }

        btn = actionButtons[1]
        btn.checkColor 'white'
        btn.checkSize 'medium'
        btn.checkIcon 'pencil'
        assert 'create-note-btn' == btn.@id
        assert 'Notiz erstellen' == btn.text()

        btnGroup = actionButtonGroups[1]
        btn = btnGroup.button
        btn.checkColor 'blue'
        btn.checkSize 'medium'
        assert 'assign-user-menu' == btn.@id
        assert 'Benutzer zuweisen' == btn.text()
        assert usersAssignTo.size() == btnGroup.dropdownMenuItems.size()
        for (int i = 0; i < usersAssignTo.size(); i++) {
            User user = usersAssignTo[i]
            def a = btnGroup.dropdownMenuLinks(i)
            assert makeAbsUrl('ticket', 'assign-to-user', ticket.id, '?user=' + user.id) == a.@href
//            assert user.toString() == a.text()    // not visible → text() == ''
        }

        btn = actionButtons[2]
        btn.checkColor 'red'
        btn.checkSize 'medium'
        assert 'close-ticket-btn' == btn.@id
        assert makeAbsUrl('ticket', 'change-stage', ticket.id, '?stage=closed') == btn.@href
        assert 'Ticket schließen' == btn.text()
    }

    protected void checkDefaultLogEntries(int offset = 0) {
        logEntries[offset].entry.check('Kunde', 'Helpdesk-Techniker') {
            assert 2 == it.p.size()
            assert 'Ich habe versucht, auf Drucker 3 im Verkauf zu drucken, allerdings kommt kein Ausdruck heraus.' == it.p[0].text()
            assert '3' == it.p[0].find('strong').text()
            assert 'Verkauf' == it.p[0].find('em').text()
            assert 'Der Drucker zeigt nur an: „Bereit für Druck“. Das Problem besteht seit gestern.' == it.p[1].text()
        }
        logEntries[offset + 1].entry.check 'Kunde'
    }
}
