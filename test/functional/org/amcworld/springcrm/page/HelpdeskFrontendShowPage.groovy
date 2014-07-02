/*
 * HelpdeskFrontendShowPage.groovy
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
import org.amcworld.springcrm.module.ButtonModule
import org.amcworld.springcrm.module.DialogModule
import org.amcworld.springcrm.module.ShowFieldsetModule
import org.amcworld.springcrm.module.TicketLogEntryModule


class HelpdeskFrontendShowPage extends DefaultPage {

    //-- Class variables ------------------------

    static at = { title == 'LB Duvensee – Helpdesk – Kundenbereich' }
    static content = {
        address { module AddressModule, fieldset[1].colRight }
        actionBar { $('aside#action-bar') }
        actionButtons { moduleList ButtonModule, actionBar.find('ul > li > .button') }
        backBtn { toolbarButtons[0] }
        dataSheet { $('div#content > div.data-sheet') }
        fieldset { moduleList ShowFieldsetModule, dataSheet.find('section.fieldset') }
        logEntries {
            moduleList TicketLogEntryModule,
                fieldset[2].find('section.ticket-log-entry')
        }
        sendMsgDialog { module DialogModule, $('#send-message-dialog') }
        subheader { $('h2').text() }
    }
    static url = 'ticket/frontend-show'
}
