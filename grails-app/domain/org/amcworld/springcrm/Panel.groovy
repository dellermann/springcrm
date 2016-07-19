/*
 * Panel.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code Panel} contains data about a panel on the overview page.
 *
 * @author	Daniel Ellermann
 * @version 2.1
 */
class Panel implements GormEntity<Panel> {

    //-- Class fields ---------------------------

    static constraints = {
		pos min: 0
		panelId blank: false
    }
	static mapping = {
        panelId index: 'panel_id'
        sort 'pos'
		version false
	}
	static transients = ['panelDef']


    //-- Fields ---------------------------------

	User user
	int pos
	String panelId
	OverviewPanel panelDef


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Panel && obj.user == user && obj.panelId == panelId
    }

    @Override
    int hashCode() {
        toString().hashCode()
    }

    @Override
	String toString() {
		"${user.userName}-${panelId}"
	}
}
