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
import org.amcworld.springcrm.page.HelpdeskEditPage


class HelpdeskTableRowModule extends ListTableRowModule {

    //-- Class variables ------------------------

    static content = {
        accessCode { td(2).text() }
        deleteButton { $('td.action-buttons > a', 2) }
        editButton { $('td.action-buttons > a', 1) }
        feButton { $('td.action-buttons > a', 0) }
        name { nameLink.text() }
        nameLink { module LinkModule, td(1) }
        organization { organizationLink.text() }
        organizationLink { module LinkModule, td(3) }
        users { td(4).text() }
    }


    //-- Public methods -------------------------

    void checkActionButtons(String name, String code, long id) {
        assert Page.makeAbsUrl('tickets', name, code) == feButton.@href
        assert '_blank' in feButton.@target
        assert ['button', 'small', 'white'] == feButton.classes()
        assert 'Kundenansicht' == feButton.text()

        super.checkActionButtons HelpdeskEditPage, 'helpdesk', id
    }
}
