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
 * The class {@code ConnectorConfig} represents the configuration of the
 * ElFinder connector.
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class ConnectorConfig {

    //-- Constants ------------------------------

    /**
     * The list of available commands.
     */
    private static final String [] AVAILABLE_COMMANDS = [
        'duplicate', 'file', 'get', 'info', 'ls', 'mkdir', 'mkfile', 'parents',
        'open', 'paste', 'put', 'rename', 'rm', 'search', 'tree', 'upload'
    ]


    //-- Instance variables ---------------------

    /**
     * The list of allowed commands.
     */
    String [] allowedCommands = AVAILABLE_COMMANDS

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
