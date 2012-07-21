/*
 * ConnectorWarning.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


package org.amcworld.springcrm.elfinder


/**
 * The class {@code ConnectorWarning} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class ConnectorWarning extends ConnectorThrowable {

    //-- Constructors ---------------------------

    /**
     * Creates a new warning with the given data.  The arguments may be of the
     * following type:
     * <ul>
     *   <li>{@code List}. The elements of this list are added to the data list
     *   in this object.</li>
     *   <li>{@code ConnectorThrowable}. The data list of this error or warning
     *   are added to the data list of this object.</li>
     *   <li>any other type. The string representation of this object is added
     *   to the data list of this object.</li>
     * </ul>
     */
    ConnectorWarning(Object... args) {
        super(args)
    }

    /**
     * Creates a new warning with the given HTTP status code.
     *
     * @param statusCode    the given status code
     */
    ConnectorWarning(int statusCode) {
        super(statusCode)
    }
}
