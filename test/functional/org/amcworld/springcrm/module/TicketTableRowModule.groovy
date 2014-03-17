/*
 * TicketTableRowModule.groovy
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


class TicketTableRowModule extends ListTableRowModule {

    //-- Class variables ------------------------

    static content = {
        customerNames { td(5).text() }
        dateCreated { td(6).text() }
        number { numberLink.text() }
        numberLink { module LinkModule, td(1).find('a') }
        helpdesk { helpdeskLink.text() }
        helpdeskLink { module LinkModule, td(3).find('a') }
        stage { td(4).text() }
        stageClasses { td(4).classes() }
        subject { subjectLink.text() }
        subjectLink { module LinkModule, td(2).find('a') }
    }
}
