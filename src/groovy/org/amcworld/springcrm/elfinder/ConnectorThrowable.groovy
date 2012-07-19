/*
 * ConnectorThrowable.groovy
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
 * The class {@code ConnectorThrowable} represents a base class for exceptions
 * and warning classes which are thrown during the ElFinder connector
 * operations.
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
abstract class ConnectorThrowable extends Exception {

    //-- Instance variables ---------------------

    /**
     * A list of codes of type {@link ConnectorError}, string parameters, or
     * string error messages which are sent back to the client.
     */
    List data = []

    /**
     * An HTTP status code which is used when creating the HTTP response.
     */
    int statusCode


    //-- Constructors ---------------------------

    /**
     * Creates a new error or warning without any code or message.
     */
    ConnectorThrowable() {
        super()
    }

    /**
     * Creates a new error or warning with the given data.  The arguments may
     * be of the following type:
     * <ul>
     *   <li>{@code List}. The elements of this list are added to the data list
     *   in this object.</li>
     *   <li>{@code ConnectorThrowable}. The data list of this error or warning
     *   are added to the data list of this object.</li>
     *   <li>any other type. The string representation of this object is added
     *   to the data list of this object.</li>
     * </ul>
     */
    ConnectorThrowable(Object... args) {
        super()
        for (Object arg : args) {
            if (arg instanceof List) {
                data += arg
            } else if (arg instanceof ConnectorThrowable) {
                data += arg.data
            } else {
                data += arg.toString()
            }
        }
    }

    /**
     * Creates a new error or warning with the given HTTP status code.
     *
     * @param statusCode    the given status code
     */
    ConnectorThrowable(int statusCode) {
        super()
        this.statusCode = statusCode
    }


    //-- Public methods -------------------------

    /**
     * Checks whether or not the given code was stored in the data list of this
     * exception.
     *
     * @param code  the code to check
     * @return      {@code true} if this exception contains this code;
     *              {@code false} otherwise
     */
    boolean hasCode(ConnectorError code) {
        for (def item : data) {
            if (item && (item[0] instanceof ConnectorError) &&
                (item[0] == code))
            {
                return true
            }
        }
        return false
    }
}
