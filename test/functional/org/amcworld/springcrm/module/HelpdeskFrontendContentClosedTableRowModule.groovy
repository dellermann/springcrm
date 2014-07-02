/*
 * HelpdeskFrontendContentClosedTableRowModule.groovy
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


class HelpdeskFrontendContentClosedTableRowModule
    extends HelpdeskFrontendContentTableRowModule
{

    //-- Class variables ------------------------

    static content = {
        customerName { td(3).text() }
        dateCreated { td(4).text() }
    }


    //-- Public methods -------------------------

    void checkActionButtons(Helpdesk hd, Ticket t) {
        def ab = actionButtons[0]
        ab.checkColor 'orange'
        ab.checkSize 'small'
        ab.checkIcon 'share-square-o'
        assert 'Wiedervorlage' == ab.text()
        def urlResubmission = Page.makeAbsUrl(
            'ticket/frontend-resubmit-ticket', t.id,
            "?helpdesk=${hd.id}", "accessCode=${hd.accessCode}"
        )
        assert urlResubmission == ab.@href
    }
}
