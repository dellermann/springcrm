/*
 * HelpdeskTableRowModule.groovy
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


class HelpdeskTableRowModule extends ListTableRowModule {

    //-- Class variables ------------------------

    static content = {
        accessCode { td(2).text() }
        deleteButton { actionButtons[2] }
        editButton { actionButtons[1] }
        feButton { actionButtons[0] }
        name { nameLink.text() }
        nameLink { module LinkModule, td(1).find('a') }
        organization { organizationLink.text() }
        organizationLink { module LinkModule, td(3).find('a') }
        users { td(4).text() }
    }


    //-- Public methods -------------------------

    void checkActionButtons(String name, String code, long id) {
        assert Page.makeAbsUrl('tickets', name, code) == feButton.@href
        assert feButton.opensNewWindow
        feButton.checkColor 'white'
        feButton.checkSize 'small'
        assert 'Kundenansicht' == feButton.text()

        super.checkActionButtons 'helpdesk', id
    }
}
