/*
 * Work.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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
 * The class {@code Work} represents a work from the service catalog.
 *
 * @author	Daniel Ellermann
 * @version 3.0
 * @see     Product
 */
class Work extends SalesItem {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'name', 'description', 'pricing.items.*name', 'category.name'
    ].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
		category nullable: true
    }


    //-- Fields ---------------------------------

    /**
     * The category this work belongs to.
     */
	WorkCategory category


    //-- Constructors ---------------------------

    /**
     * Creates an empty work.
     */
	Work() {
        super()
        type = 'S'
    }

    /**
     * Creates a work using the data of the given work.
     *
     * @param w the given work
     */
	Work(Work w) {
        super(w)
        type = w.type
		category = w.category
	}
}
