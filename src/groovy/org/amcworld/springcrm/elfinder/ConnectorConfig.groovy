/*
 * ConnectorConfig.groovy
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
 * The class {@code ConnectorConfig} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class ConnectorConfig {

    //-- Constants ------------------------------

    private static final List<String> ALLOWED_COMMANDS = [
        'open', 'tree', 'parents', 'ls', 'file', 'mkdir', 'mkfile', 'rename',
        'upload', 'rm', 'paste', 'get', 'put'
    ]


    //-- Instance variables ---------------------

    List<String> allowedCommands = ALLOWED_COMMANDS

    /**
     * The maximum allowed upload size in MB per file.
     */
    long uploadMaxSize = 50L


    //-- Public methods -------------------------

    /**
     * Checks whether or not the given command is allowed.
     *
     * @param command   the command to test
     * @return          {@code true} if the command is allowed; {@code false}
     *                  otherwise
     */
    boolean isCommandAllowed(String command) {
        return command in allowedCommands
    }
}
