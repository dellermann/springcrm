/*
 * GoogleAuthException.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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


package org.amcworld.springcrm.google


/**
 * The class {@code GoogleAuthException} represents exceptions which occur
 * during the authentication at Google API services.  Possible reasons could
 * be
 * <ul>
 *   <li>the user has either not authenticated at Google or not permitted
 *   access to the required Google services</li>
 *   <li>an error occurred during the Google API authentication, for example,
 *   if the user has revoked the access by this application</li>
 * </ul>
 *
 * @author	Daniel Ellermann
 * @version 2.0
 * @since   1.0
 */
class GoogleAuthException extends Exception {

    //-- Constructors ---------------------------

    public GoogleAuthException() {
        super()
    }

    public GoogleAuthException(String message, Throwable cause) {
        super(message, cause)
    }

    public GoogleAuthException(String message) {
        super(message)
    }

    public GoogleAuthException(Throwable cause) {
        super(cause)
    }
}
