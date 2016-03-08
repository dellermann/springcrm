/*
 * CalendarEventTableRowModule.groovy
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


class CalendarEventTableRowModule extends ListTableRowModule {

    //-- Class variables ------------------------

    static content = {
    	end { td(3).text() }
    	location { td(6).text() }
        organization { organizationLink.text() }
        organizationLink { module LinkModule, td(5).find('a') }
    	recurrence { td(4).text() }
        start { td(2).text() }
        subject { subjectLink.text() }
        subjectLink { module LinkModule, td(1).find('a') }
    }
}
