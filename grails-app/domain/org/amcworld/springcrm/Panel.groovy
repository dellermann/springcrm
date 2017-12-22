/*
 * Panel.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.bson.types.ObjectId
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code Panel} contains data about a panel on the overview page.
 *
 * @author	Daniel Ellermann
 * @version 3.0
 */
@EqualsAndHashCode(includes = ['user', 'panelId'])
@ToString(includes = ['user', 'panelId'])
class Panel implements GormEntity<Panel> {

    //-- Class fields ---------------------------

    static constraints = {
		pos min: 0i
		panelId blank: false
    }
	static mapping = {
        panelId index: true
        sort 'pos'
		version false
	}
	static transients = ['panelDef']


    //-- Fields ---------------------------------

    /**
     * The ID of the panel.
     */
    ObjectId id

    /**
     * The loaded panel definition.
     */
    OverviewPanel panelDef

    /**
     * The name of the panel.
     */
    String panelId

    /**
     * The position of the panel on the overview page.
     */
    int pos

    /**
     * The user the panel is associated to.
     */
    User user
}
