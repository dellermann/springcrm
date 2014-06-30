/*
 * TicketShowPage.groovy
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


package org.amcworld.springcrm.page

import org.amcworld.springcrm.module.AddressModule
import org.amcworld.springcrm.module.ButtonGroupModule
import org.amcworld.springcrm.module.ButtonModule
import org.amcworld.springcrm.module.DialogModule
import org.amcworld.springcrm.module.TicketLogEntryModule


class TicketShowPage extends DefaultShowPage {

    //-- Class variables ------------------------

    static at = { title == 'Ticket anzeigen' }
    static content = {
        address { module AddressModule, fieldset[1].colRight }
        assignUserButton { module ButtonGroupModule, $('#assign-user-menu').parent() }
        changeToInProcessButton { module ButtonModule, $('#change-to-in-process-btn') }
        closeButton { module ButtonModule, $('#close-ticket-btn') }
        createNoteButton { module ButtonModule, $('#create-note-btn') }
        logEntries {
            moduleList TicketLogEntryModule,
                fieldset[2].find('section.ticket-log-entry')
        }
        sendMsgDialog { module DialogModule, $('#send-message-dialog') }
        sendMsgToCustomerButton { module ButtonModule, $('#send-message-to-customer-btn') }
        sendMsgToUserButton { module ButtonGroupModule, $('#send-message-to-user-menu') }
        takeOnButton { module ButtonModule, $('#take-on-btn') }
    }
    static url = 'ticket/show'
}
