/*
 * ListInterceptor.groovy
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


/**
 * The class {@code ListInterceptor} limits the maximum number of items which
 * are visible in a list.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
class ListInterceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    ListInterceptor() {
        match action: ~/(index|listEmbedded)/
    }


    //-- Public methods -------------------------

    /**
     * Limits the maximum number of items in a list to be in range 10 til 100.
     *
     * @return  always {@code true}
     */
    boolean before() {
        params.max = Math.max(
            Math.min(params.max ? params.int('max') : 10, 100), 10
        )

        true
    }
}
