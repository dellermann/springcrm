/*
 * CallShowPage.groovy
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

import org.amcworld.springcrm.module.LinkModule


class HelpdeskShowPage extends DefaultShowPage {

    //-- Class variables ------------------------

    static at = { title == 'Helpdesk anzeigen' }
    static content = {
        feLink { module LinkModule, fieldset[0].colRight.row[2].field }
        users { module LinkModule, fieldset[1].row[0].field.find('ul > li', it) }
    }
    static url = 'helpdesk/show'
}
