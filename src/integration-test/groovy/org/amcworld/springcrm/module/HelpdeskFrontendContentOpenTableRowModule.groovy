/*
 * HelpdeskFrontendContentOpenTableRowModule.groovy
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

import geb.Page
import org.amcworld.springcrm.Helpdesk
import org.amcworld.springcrm.Ticket


class HelpdeskFrontendContentOpenTableRowModule
    extends HelpdeskFrontendContentTableRowModule
{

    //-- Class variables ------------------------

    static content = {
        customerName { td(4).text() }
        dateCreated { td(5).text() }
        status { td(3).text() }
    }


    //-- Public methods -------------------------

    void checkActionButtons(Helpdesk hd, Ticket t) {
        def ab = actionButtons[0]
        ab.checkColor 'white'
        ab.checkSize 'small'
        ab.checkIcon 'envelope-o'
        ab.checkCssClasses 'send-btn'
        assert 'Nachricht' == ab.text()

        ab = actionButtons[1]
        ab.checkColor 'red'
        ab.checkSize 'small'
        ab.checkIcon 'check'
        ab.checkCssClasses 'close-btn'
        assert 'Schließen' == ab.text()
        def urlClose = Page.makeAbsUrl(
            'ticket/frontend-close-ticket', t.id, "?helpdesk=${hd.id}",
            "accessCode=${hd.accessCode}"
        )
        assert urlClose == ab.@href
    }
}
