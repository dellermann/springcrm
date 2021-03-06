/*
 * GeneralFunctionalTest.groovy
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

import geb.Browser
import geb.Page
import org.amcworld.springcrm.page.LoginPage
import org.amcworld.springcrm.page.OverviewPage
import spock.lang.Ignore


/**
 * The class {@code GeneralFunctionalTest} represents a general base class for
 * functional Spock specifications.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
@Ignore
class GeneralFunctionalTest extends DbUnitSpecBase {

    //-- Fixture methods ------------------------

    def setupSpec() {
        def f = { String controller, String action, Long id = null ->
            StringBuilder buf = new StringBuilder(controller)
            buf << '/' << action
            if (id != null) buf << '/' << id
            makeAbsUrl buf.toString()
        }
        Browser.metaClass.static.makeUrl = f
        Page.metaClass.static.makeUrl = f
        GeneralFunctionalTest.metaClass.static.makeUrl = f

        f = { String url, Object... args ->
            StringBuilder buf = new StringBuilder()
            Browser.drive { buf << baseUrl }
            buf << url
            if (args) {
                String glue = '/'
                for (int i = 0; i < args.length; i++) {
                    String arg = args[i].toString()
                    if (arg.startsWith('?')) {
                        glue = '&'
                    } else {
                        buf << glue
                    }
                    buf << arg
                }
            }
            buf.toString()
        }
        Browser.metaClass.static.makeAbsUrl = f
        Page.metaClass.static.makeAbsUrl = f
        GeneralFunctionalTest.metaClass.static.makeAbsUrl = f

        Page.metaClass.static.getAbsUrl = { Object... args ->
            makeAbsUrl(delegate.url, *args)
        }
    }


    //-- Non-public methods ---------------------

    @Override
    protected List<String> getDatasets() {
        ['test-data/install-data.xml']
    }

    /**
     * Logs in at the application.
     */
    protected void login() {
        Browser.drive {
            LoginPage page = to(LoginPage)
            page.userName = 'mkampe'
            page.password = 'abc1234'
            page.loginBtn.click()
            waitFor { at OverviewPage }
        }
    }

    /**
     * Prepares an address for storing as embedded property of another domain
     * model instance.
     *
     * @return  the prepared address
     */
    protected Address prepareAddress() {
        new Address(
            street: 'Dörpstraat 25',
            postalCode: '23898',
            location: 'Duvensee',
            state: 'Schleswig-Holstein',
            country: 'Deutschland',
        )
    }

    /**
     * Prepares a calendar event and stores it into the database.
     *
     * @param org   the organization the calendar event belongs to
     * @return      the prepared calendar event
     */
    protected CalendarEvent prepareCalendarEvent(Organization org) {
        def calEvent = new CalendarEvent(
            subject: 'Besprechung Werbekonzept',
            location: 'Büro Landschaftsbau Duvensee GbR',
            description: 'Besprechung des Konzepts für die geplante Marketing-Aktion.',
            start: new GregorianCalendar(2013, Calendar.JANUARY, 23, 10, 00, 0).time,
            end: new GregorianCalendar(2013, Calendar.JANUARY, 23, 12, 00, 0).time,
            organization: org,
            owner: User.get(1)
        )
        calEvent.save flush: true, failOnError: true
    }

	/**
	 * Prepares a phone call and stores it into the database.
	 *
	 * @param org   the organization the phone call belongs to
	 * @param p     the person the phone call belongs to
	 * @return      the prepared phone call
	 */
	protected Call prepareCall(Organization org, Person p) {
		def call = new Call(
			subject: 'Bitte um Angebot',
			start: new GregorianCalendar(2013, Calendar.FEBRUARY, 13, 9, 15, 0).time,
			organization: org,
			person: p,
			phone: '04543 31233',
			type: CallType.incoming,
			status: CallStatus.completed,
			notes: 'Herr Brackmann bittet um die Zusendung eines Angebots für die **geplante Marketing-Aktion**.'
		)
		call.save flush: true, failOnError: true
	}

    /**
     * Prepares a helpdesk and stores it into the database.
     *
     * @param org   the organization the helpdesk belongs to
     * @return      the prepared helpdesk
     */
    protected Helpdesk prepareHelpdesk(Organization org) {
        def helpdesk = new Helpdesk(
            organization: org,
            name: 'LB Duvensee',
            accessCode: '4A51VZ',
            users: User.list() as Set
        )
        helpdesk.save flush: true, failOnError: true
    }

    /**
     * Prepares an organization fixture and stores it into the database.
     *
     * @return  the prepared organization
     */
    protected Organization prepareOrganization() {
        def addr = prepareAddress()
        def org = new Organization(
            recType: (byte) 1,
            name: 'Landschaftsbau Duvensee GbR',
            legalForm: 'GbR',
            type: OrgType.get(100),
            industry: Industry.get(1012),
            phone: '04543 31233',
            fax: '04543 31235',
            email1: 'info@landschaftsbau-duvensee.de',
            website: 'http://www.landschaftsbau-duvensee.de',
            billingAddr: addr,
            shippingAddr: addr,
            notes: 'Kontakt über Peter Hermann hergestellt.\nErstes Treffen am 13.06.2012.'
        )
        org.save flush: true, failOnError: true
    }

    /**
     * Prepares a person fixture and stores it into the database.
     *
     * @param org   the organization the person belongs to; if {@code null} a
     *              new organization is created
     * @return      the prepared person
     */
    protected Person preparePerson(Organization org = prepareOrganization()) {
        def addr = prepareAddress()
        def person = new Person(
            organization: org,
            salutation: Salutation.get(1),
            firstName: 'Henry',
            lastName: 'Brackmann',
            mailingAddr: addr,
            otherAddr: new Address(),
            phone: '04543 31233',
            mobile: '0163 3343267',
            fax: '04543 31235',
            email1: 'h.brackmann@landschaftsbau-duvensee.de',
            jobTitle: 'Geschäftsführer',
            department: 'Geschäftsleitung',
            assistant: 'Anna Schmarge',
            birthday: new GregorianCalendar(1962, Calendar.FEBRUARY, 14).time
        )
        person.save flush: true, failOnError: true
    }

    /**
     * Prepares a ticket for the given helpdesk in the stage "created".
     *
     * @param helpdesk  the given helpdesk
     * @return          the prepared ticket
     */
    protected Ticket prepareTicket(Helpdesk helpdesk) {
        def addr = prepareAddress()
        def ticket = new Ticket(
                helpdesk: helpdesk,
                subject: 'Drucker im Verkauf funktioniert nicht',
                salutation: Salutation.get(2),
                firstName: 'Marlen',
                lastName: 'Thoss',
                address: addr,
                phone: '04543 31234',
                mobile: '0170 1896043',
                fax: '04543 31235',
                email1: 'm.thoss@landschaftsbau-duvensee.de',
                priority: TicketPriority.get(1102)
            ).addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.create
            )).addToLogEntries(new TicketLogEntry(
                action: TicketLogAction.sendMessage,
                message: '''Ich habe versucht, auf Drucker **3** im _Verkauf_ zu drucken, allerdings kommt kein Ausdruck heraus.

Der Drucker zeigt nur an: „Bereit für Druck“. Das Problem besteht seit gestern.'''
            ))
        ticket.save flush: true, failOnError: true
    }

    /**
     * Prepares an additional user fixture and stores it into the database.
     *
     * @return  the prepared user
     */
    protected User prepareUser() {
        def user = new User(
            userName: 'rwendt',
            password: 'secret123',
            firstName: 'Regina',
            lastName: 'Wendt',
            phone: '04536 45301-21',
            fax: '04536 45301-90',
            mobile: '0162 37493393',
            phoneHome: '04536 37471',
            email: 'r.wendt@kampe.de'
        )
        user.save flush: true, failOnError: true
    }
}
