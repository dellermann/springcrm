/*
 * Panel.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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


package org.amcworld.springcrm


/**
 * The class {@code Panel} contains data about a panel on the overview page.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class Panel {

    //-- Constants ------------------------------

	public static final int NUM_COLUMNS = 3i


    //-- Class variables ------------------------

    static constraints = {
		user()
		col(range: 0..NUM_COLUMNS - 1)
		pos(min: 0)
		panelId(blank: false)
    }
	static mapping = {
        panelId index: 'panel_id'
		version false
	}
	static transients = ['panelDef']


    //-- Instance variables ---------------------

	User user
	int col
	int pos
	String panelId
	OverviewPanel panelDef


    //-- Public methods -------------------------

	String toString() {
		return "${user.userName}-${panelId}"
	}
}
