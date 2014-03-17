/*
 * TicketLogEntryModule.groovy
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


package org.amcworld.springcrm.module


class TicketLogEntryModule extends geb.Module {

    //-- Class variables ------------------------

    static content = {
        entry {
            def cls = $().classes()
            if ('ticket-log-entry-action-create' in cls) {
                module TicketLogEntryCreateModule
            } else if ('ticket-log-entry-action-sendMessage' in cls) {
                module TicketLogEntrySendMessageModule
            } else {
                this
            }
        }
        row { moduleList TicketLogEntryRowModule, $('div.row') }
        title { $('h4').text() }
    }


    //-- Public methods -------------------------

    void checkAction(String action) {
        assert classes().contains('ticket-log-entry-action-' + action)
    }

    void checkTitle(String text) {
        assert title ==~ /^\d\d\.\d\d\.\d\d\d\d\s\d\d:\d\d\s–\s\Q${text}\E$/
    }
}
