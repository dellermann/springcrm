/*
 * SecurityService.groovy
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


package org.amcworld.springcrm

import java.security.MessageDigest


/**
 * The class {@code SecurityService} represents a service for security issues
 * in this application.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
class SecurityService {

    //-- Public methods -------------------------

    /**
     * Encrypts the given password using SHA-256.  To store the password in
     * text fields the digest is hex encoded.
     *
     * @param password  the password to encrypt
     * @return          the encrypted password in hex encoding
     */
    String encryptPassword(String password) {
        MessageDigest digest = MessageDigest.getInstance('SHA-1')
        byte [] b = digest.digest(password.bytes)
        return b.encodeHex().toString()
    }
}
