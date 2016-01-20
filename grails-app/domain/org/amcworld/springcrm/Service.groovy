/*
 * Service.groovy
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


/**
 * The class {@code Service} represents a service from the service catalog.
 *
 * @author	Daniel Ellermann
 * @version 2.0
 * @see     Product
 */
class Service extends SalesItem {

    //-- Class fields ---------------------------

    static constraints = {
		category nullable: true
    }


    //-- Fields ---------------------------------

    /**
     * The category this service belongs to.
     */
	ServiceCategory category


    //-- Constructors ---------------------------

    /**
     * Creates an empty service.
     */
	Service() {
        super()
        type = 'S'
    }

    /**
     * Creates a service using the data of the given service.
     *
     * @param s the given service
     */
	Service(Service s) {
        super(s)
        type = s.type
		category = s.category
	}
}
