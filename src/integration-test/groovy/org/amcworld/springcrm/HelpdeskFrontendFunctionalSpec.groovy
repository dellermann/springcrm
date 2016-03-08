/*
 * HelpdeskFrontendFunctionalSpec.groovy
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

import org.amcworld.springcrm.page.HelpdeskFrontendListPage
import org.amcworld.springcrm.page.HelpdeskFrontendShowPage
import spock.lang.Shared


class HelpdeskFrontendFunctionalSpec extends GeneralFunctionalTest {

    //-- Instance variables ---------------------

    @Shared def mailData


    //-- Fixture methods ------------------------

    def setup() {
        def org = prepareOrganization()
        prepareUser()
        prepareHelpdesk org

        MailSystemService.metaClass.sendRawMail = { Closure mail ->
            def builder = new NodeBuilder()
            mailData = builder(mail)
            null
        }
    }

    def cleanup() {
        Ticket.executeUpdate 'delete from Ticket'
        TicketLogEntry.executeUpdate 'delete from TicketLogEntry'
        HelpdeskUser.executeUpdate 'delete from HelpdeskUser'
        Helpdesk.executeUpdate 'delete from Helpdesk'
    }


    //-- Feature methods ------------------------

    def 'Show helpdesk in frontend'() {
        given: 'a helpdesk'
        def hd = Helpdesk.first()

        when: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        then: 'I get to the frontend view'
        waitFor { at HelpdeskFrontendListPage }
        'Helpdesk LB Duvensee' == header

        and: 'the ticket list is empty'
        'Diese Liste enthält keine Einträge.' == mainContent.emptyList.message
        1 == mainContent.emptyList.buttons.size()
        'Ticket anlegen' == mainContent.emptyList.buttons[0].text()

        and: 'the form to create a ticket is visible'
        'Ticket erstellen' == createForm.header
    }

    def 'Create ticket'() {
        given: 'a helpdesk'
        def hd = Helpdesk.first()

        and: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        when: 'I fill in the ticket form'
        createForm.subject = 'Drucker im Verkauf funktioniert nicht'
        createForm.priority = 1102
        createForm.salutation = 2
        createForm.firstName = 'Marlen'
        createForm.lastName = 'Thoss'
        createForm.phone = '04543 31234'
        createForm.mobile = '0170 1896043'
        createForm.email1 = 'm.thoss@landschaftsbau-duvensee.de'
        createForm.messageText = '''Ich habe versucht, auf Drucker **3** im _Verkauf_ zu drucken, allerdings kommt kein Ausdruck heraus.

Der Drucker zeigt nur an: „Bereit für Druck“. Das Problem besteht seit gestern.'''
        createForm.submitBtn.click()

        then: 'a new ticket is created'
        waitFor { at HelpdeskFrontendListPage }
        'Helpdesk LB Duvensee' == header
        'Ticket Drucker im Verkauf funktioniert nicht wurde angelegt.' == flashMessage
        'Offene Tickets' == mainContent.fieldset[0].header

        and: 'there is one ticket in the list'
        checkOpenTicket mainContent.fieldset[0], hd, Ticket.first()

        and: 'there is one Ticket object'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        1 == Ticket.count()
        2 == TicketLogEntry.count()
    }

    def 'List open tickets'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        when: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        then: 'I get to the frontend page'
        waitFor { at HelpdeskFrontendListPage }
        'Helpdesk LB Duvensee' == header
        'Offene Tickets' == mainContent.fieldset[0].header

        and: 'there is one ticket in the list'
        checkOpenTicket mainContent.fieldset[0], hd, ticket

        and: 'there is one Ticket object'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        1 == Ticket.count()
        2 == TicketLogEntry.count()
    }

    def 'List open and closed tickets'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticketOpen = prepareTicket(hd)
        def ticketClosed = prepareClosedTicket(hd)

        when: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        then: 'I get to the frontend page'
        waitFor { at HelpdeskFrontendListPage }
        'Helpdesk LB Duvensee' == header

        and: 'there are lists for open and closed tickets'
        2 == mainContent.fieldset.size()

        and: 'there is one ticket in the open ticket list'
        def openTickets = mainContent.fieldset[0]
        'Offene Tickets' == openTickets.header
        checkOpenTicket openTickets, hd, ticketOpen

        and: 'there is one ticket in the closed ticket list'
        def closedTickets = mainContent.fieldset[1]
        'Geschlossene Tickets' == closedTickets.header
        checkClosedTicket closedTickets, hd, ticketClosed

        and: 'there are two Ticket objects'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        2 == Ticket.count()
        5 == TicketLogEntry.count()
    }

    def 'Send a message in list view'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        and: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        when: 'I click the button to send a message'
        def fieldset = mainContent.fieldset[0]
        fieldset.trOpenTickets[0].actionButtons[0].click()

        then: 'the form to send a message appears'
        waitFor { !createForm.displayed }
        messageForm.displayed
        'active' in fieldset.trOpenTickets[0].classes()

        when: 'I click the cancel button'
        messageForm.cancelBtn.click()

        then: 'the form to send a message disappears'
        waitFor { !messageForm.displayed }
        createForm.displayed
        !('active' in fieldset.trOpenTickets[0].classes())

        when: 'I click the send message button again and fill in the message form'
        fieldset.trOpenTickets[0].actionButtons[0].click()
        waitFor { !createForm.displayed }
        messageForm.messageText = '''Seit gestern zeigt Drucker **3** folgende Meldung:

_Printer error #3401_

Hilft Ihnen das weiter?'''
        messageForm.submitBtn.click()

        then: 'I get to the frontend page'
        waitFor { at HelpdeskFrontendListPage }
        'Nachricht wurde verschickt.' == flashMessage

        and: 'there is one ticket in the list'
        checkOpenTicket mainContent.fieldset[0], hd, ticket

        and: 'the mail sent contains the correct values'
        'SpringCRM Service <noreply@springcrm.de>' == mailData.from.text()
        ['m.kampe@kampe.de', 'r.wendt@kampe.de'] == mailData.to[0].children()
        'true' == mailData.multipart.text()
        'Neue Nachricht zu Ticket' == mailData.subject.text()
        mailData.text.text().startsWith '''Liebes Helpdesk-Team,

zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht wurde eine neue Nachricht mit folgenden Daten verschickt:'''
        mailData.html.text().startsWith '''<p>Liebes Helpdesk-Team,</p><p>zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht wurde eine neue Nachricht mit folgenden Daten verschickt:</p>'''

        and: 'there is one Ticket object and one more TicketLogEntry'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        1 == Ticket.count()
        3 == TicketLogEntry.count()
    }

    def 'Show a ticket'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        and: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        when: 'I click on a link to show the ticket'
        mainContent.fieldset[0].trOpenTickets[0].numberLink.click()

        then: 'I get to the show page'
        waitFor { at HelpdeskFrontendShowPage }
        'Helpdesk LB Duvensee' == header

        and: 'the ticket data is displayed correctly'
        'Ticket T-10000 – Drucker im Verkauf funktioniert nicht' == subheader
        def fs0 = fieldset[0]
        'Allgemeine Informationen' == fs0.title
        def colL0 = fs0.colLeft
        'T-10000' == colL0.row[0].fieldText
        'Drucker im Verkauf funktioniert nicht' == colL0.row[1].fieldText
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
        'mailto:m.thoss@landschaftsbau-duvensee.de' == colL1.row[7].link.@href
        'm.thoss@landschaftsbau-duvensee.de' == colL1.row[7].link.text()
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
        // actionButtons[0] is tested in "Send a message in show view"
        actionButtons[1].checkColor 'red'
        actionButtons[1].checkSize 'medium'
        actionButtons[1].checkIcon 'check'
        actionButtons[1].checkCssClasses 'close-btn'
        'Schließen' == actionButtons[1].text()
        def url = makeAbsUrl(
            'ticket/frontend-close-ticket', ticket.id, "?helpdesk=${hd.id}",
            "accessCode=${hd.accessCode}"
        )
        url == actionButtons[1].@href

        when: 'I click the back button'
        backBtn.click()

        then: 'I get to the frontend page'
        waitFor { at HelpdeskFrontendListPage }
        'Helpdesk LB Duvensee' == header
        'Offene Tickets' == mainContent.fieldset[0].header

        and: 'there is one Ticket object'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        1 == Ticket.count()
        2 == TicketLogEntry.count()
    }

    def 'Send a message in show view'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        and: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        and: 'I click on a link to show the ticket'
        waitFor { at HelpdeskFrontendListPage }
        mainContent.fieldset[0].trOpenTickets[0].numberLink.click()
        waitFor { at HelpdeskFrontendShowPage }

        when: 'I click the button to send a message'
        actionButtons[0].click()

        then: 'the form to send a message appears'
        sendMsgDialog.displayed

        when: 'I fill in the form but cancel'
        sendMsgDialog.find('#messageText').value '''Seit gestern zeigt Drucker **3** folgende Meldung:

_Printer error #3401_

Hilft Ihnen das weiter?'''
        sendMsgDialog.buttons[1].click()

        then: 'the dialog disappears'
        !sendMsgDialog.displayed
        isAt HelpdeskFrontendShowPage
        2 == logEntries.size()
        checkDefaultLogEntries()

        when: 'I click the button again, fill in the form, and submit'
        actionButtons[0].click()
        sendMsgDialog.find('#messageText').value '''Seit gestern zeigt Drucker **3** folgende Meldung:

_Printer error #3401_

Hilft Ihnen das weiter?'''
        sendMsgDialog.buttons[0].click()

        then: 'I get to the show view with a new log entry'
        waitFor { at HelpdeskFrontendShowPage }
        3 == logEntries.size()
        logEntries[0].entry.check('Kunde', 'Helpdesk-Techniker') {
            assert 3 == it.p.size()
            assert 'Seit gestern zeigt Drucker 3 folgende Meldung:' == it.p[0].text()
            assert '3' == it.p[0].find('strong').text()
            assert 'Printer error #3401' == it.p[1].text()
            assert 'Printer error #3401' == it.p[1].find('em').text()
            assert 'Hilft Ihnen das weiter?' == it.p[2].text()
        }
        checkDefaultLogEntries 1

        and: 'the mail sent contains the correct values'
        'SpringCRM Service <noreply@springcrm.de>' == mailData.from.text()
        ['m.kampe@kampe.de', 'r.wendt@kampe.de'] == mailData.to[0].children()
        'true' == mailData.multipart.text()
        'Neue Nachricht zu Ticket' == mailData.subject.text()
        mailData.text.text().startsWith '''Liebes Helpdesk-Team,

zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht wurde eine neue Nachricht mit folgenden Daten verschickt:'''
        mailData.html.text().startsWith '''<p>Liebes Helpdesk-Team,</p><p>zu Ticket T-10000 – Drucker im Verkauf funktioniert nicht wurde eine neue Nachricht mit folgenden Daten verschickt:</p>'''

        and: 'there is one Ticket object and one more TicketLogEntry'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        1 == Ticket.count()
        3 == TicketLogEntry.count()
    }

    def 'Close ticket'() {
        given: 'a ticket'
        def hd = Helpdesk.first()
        def ticket = prepareTicket(hd)

        and: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        when: 'I click the button to close the ticket but cancel'
        def trO = mainContent.fieldset[0].trOpenTickets[0]
        String msg = withConfirm(false) { trO.actionButtons[1].click() }

        then:
        'Wollen Sie das Ticket wirklich schließen?' == msg
        isAt HelpdeskFrontendListPage
        'erstellt' == trO.status

        when: 'I click the button to close the ticket and confirm'
        withConfirm { trO.actionButtons[1].click() }

        then: 'I get to the show view and there is one closed ticket'
        waitFor { at HelpdeskFrontendListPage }
        'Ticket wurde geschlossen.' == flashMessage
        def fieldset = mainContent.fieldset[0]
        'Geschlossene Tickets' == fieldset.header
        def trC = fieldset.trClosedTickets[0]
        'T-10000' == trC.number
        def urlShow = makeAbsUrl(
            'ticket/frontend-show', ticket.id, "?helpdesk=${hd.id}",
            "accessCode=${hd.accessCode}"
        )
        urlShow == trC.numberLink.@href
        'Drucker im Verkauf funktioniert nicht' == trC.subject
        urlShow == trC.subjectLink.@href
        'Thoss, Marlen' == trC.customerName
        trC.checkDateCreated()
        trC.checkActionButtons hd, ticket

        and: 'there is one Ticket object and one more TicketLogEntry'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        1 == Ticket.count()
        3 == TicketLogEntry.count()
    }

    def 'Resubmit a ticket'() {
        given: 'a closed ticket'
        def hd = Helpdesk.first()
        def ticket = prepareClosedTicket(hd)

        and: 'I go to the frontend page'
        to HelpdeskFrontendListPage

        when: 'I click the resubmit button'
        mainContent.fieldset[0].actionButtons[0].click()

        then: 'I get to the frontend page'
        waitFor { at HelpdeskFrontendListPage }
        'Ticket wurde zur Wiedervorlage gestellt.' == flashMessage

        and: 'there is one resubmitted ticket in the list'
        def fieldset = mainContent.fieldset[0]
        'Offene Tickets' == fieldset.header
        def tr = fieldset.trOpenTickets[0]
        assert 'T-10000' == tr.number
        def urlShow = makeAbsUrl(
            'ticket/frontend-show', ticket.id, "?helpdesk=${hd.id}",
            "accessCode=${hd.accessCode}"
        )
        urlShow == tr.numberLink.@href
        'Kasse K-2010 reagiert nicht' == tr.subject
        urlShow == tr.subjectLink.@href
        'Wiedervorlage' == tr.status
        'Thoss, Marlen' == tr.customerName
        tr.checkDateCreated()
        tr.checkActionButtons hd, ticket

        and: 'there is one Ticket object and one more TicketLogEntry'
        1 == Helpdesk.count()
        2 == HelpdeskUser.count()
        1 == Ticket.count()
        4 == TicketLogEntry.count()
    }


    //-- Non-public methods ---------------------

    protected void checkClosedTicket(def fieldset, Helpdesk hd, Ticket t) {
        def tr = fieldset.trClosedTickets[0]
        assert 'T-10001' == tr.number
        def urlShow = makeAbsUrl(
            'ticket/frontend-show', t.id, "?helpdesk=${hd.id}",
            "accessCode=${hd.accessCode}"
        )
        urlShow == tr.numberLink.@href
        'Kasse K-2010 reagiert nicht' == tr.subject
        urlShow == tr.subjectLink.@href
        'Thoss, Marlen' == tr.customerName
        tr.checkDateCreated()
        tr.checkActionButtons hd, t
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

    protected void checkOpenTicket(def fieldset, Helpdesk hd, Ticket t) {
        def tr = fieldset.trOpenTickets[0]
        assert 'T-10000' == tr.number
        def urlShow = makeAbsUrl(
            'ticket/frontend-show', t.id, "?helpdesk=${hd.id}",
            "accessCode=${hd.accessCode}"
        )
        urlShow == tr.numberLink.@href
        'Drucker im Verkauf funktioniert nicht' == tr.subject
        urlShow == tr.subjectLink.@href
        'erstellt' == tr.status
        'Thoss, Marlen' == tr.customerName
        tr.checkDateCreated()
        tr.checkActionButtons hd, t
        // tr.actionButtons[0].@href tested in "Send a message in show view"
    }

    /**
     * Prepares a ticket for the given helpdesk in the stage "closed".
     *
     * @param helpdesk  the given helpdesk
     * @return          the prepared ticket
     */
    protected Ticket prepareClosedTicket(Helpdesk helpdesk) {
        def addr = prepareAddress()
        def ticket = new Ticket(
                helpdesk: helpdesk,
                subject: 'Kasse K-2010 reagiert nicht',
                salutation: Salutation.get(2),
                firstName: 'Marlen',
                lastName: 'Thoss',
                address: addr,
                phone: '04543 31234',
                mobile: '0170 1896043',
                fax: '04543 31235',
                email1: 'm.thoss@landschaftsbau-duvensee.de',
                priority: TicketPriority.get(1101),
                stage: TicketStage.closed
            ).addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.create
            )).addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.sendMessage,
                message: '''Kasse *K-2010* reagiert nicht mehr auf Eingaben. Auch ein Neustart brachte nichts.'''
            )).addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.changeStage,
                stage: TicketStage.closed
            ))
        ticket.save flush: true, failOnError: true
    }
}
