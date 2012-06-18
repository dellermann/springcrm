/*
 * RecoverableGoogleSyncException.java
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


package org.amcworld.springcrm.google;


/**
 * The class {@code RecoverableGoogleSyncException} represents a recoverable
 * exception which is thrown during synchronization with Google.  The
 * synchronization routine should synchronize the next entry if this exception
 * was thrown.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
public class RecoverableGoogleSyncException extends GoogleSyncException {

    //-- Constructors ---------------------------

    public RecoverableGoogleSyncException() {
        super()
    }

    public RecoverableGoogleSyncException(String message, Throwable cause) {
        super(message, cause)
    }

    public RecoverableGoogleSyncException(String message) {
        super(message)
    }

    public RecoverableGoogleSyncException(Throwable cause) {
        super(cause)
    }
}
