/*
 * Connector.groovy
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

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.amcworld.springcrm.elfinder.command.Command
import org.amcworld.springcrm.elfinder.fs.Volume
import org.apache.commons.logging.LogFactory


/**
 * The class {@code Connector} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class Connector {

    //-- Constants ------------------------------

    public static final String VERSION = '2.0'
    private static final log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    /**
     * The configuration for the connector.
     */
    ConnectorConfig config = new ConnectorConfig()

    /**
     * The ID of the default volume.  If unset the any volume in the table
     * {@link volumes} is used.
     */
    String defaultVolumeId

    /**
     * The underlying HTTP request.
     */
    HttpServletRequest httpRequest

    /**
     * The underlying HTTP response.
     */
    HttpServletResponse httpResponse

    /**
     * The connector's request object which parses and handles the request.
     */
    Request request

    /**
     * The connector's response object which sends the requested data back to
     * the client.
     */
    Response response

    /**
     * A table containing the volumes by their ID.
     */
    protected Map<String, Volume> volumes = [: ]


    //-- Constructors ---------------------------

    Connector(HttpServletRequest request, HttpServletResponse response) {
        this.httpRequest = request
        this.httpResponse = response
        this.request = new Request(this, request)
        this.response = new Response(this, response)
    }


    //-- Public methods -------------------------

    /**
     * Adds the given volume to the volume table.
     *
     * @param volume    the volume to add
     */
    Connector addVolume(Volume volume) {
        volumes[volume.id] = volume
        return this
    }

    /**
     * Factory method to obtain the command class for the given command.  The
     * method loads and instantiates the command class and verifies whether
     * the access to this command is allowed.  If any errors occur it is stored
     * in the response object.
     *
     * @param command   the given command; may be {@code null}
     * @return          the instantiated command class; {@code null} if an
     *                  error occurred
     */
    Command getCommand(String command) {
        if (!command) {
            command = 'open'
        }
        if (!this.config.isCommandAllowed(command)) {
            this.response.errors += ConnectorError.ACCESS_DENIED
            return null
        }

        try {
            Command cmd = createCommandClass(command)
            cmd.connector = this
            cmd.init()
            return cmd
        } catch (ClassNotFoundException e) {
            this.response.errors += ConnectorError.UNKNOWN_CMD
            return null
        } catch (Exception e) {
            this.response.errors += ConnectorError.UNKNOWN
            return null
        }
    }

    /**
     * Gets the default volume.  The default volume is either specified by
     * {@code defaultVolumeId} or an arbitrary volume from the volume table is
     * selected.
     *
     * @return                      the default volume
     * @throws ConnectorException   if no volumes are defined
     */
    Volume getDefaultVolume() {
        try {
            String id = defaultVolumeId ?: volumes.keySet().iterator().next()
            return getVolume(id)
        } catch (NoSuchElementException e) {
            throw new ConnectorException(ConnectorError.CONF_NO_VOL)
        }
    }

    /**
     * Gets the volume with the given ID.
     *
     * @param id    the given ID
     * @return      the volume with the ID; {@code null} if no such volume
     *              exists
     */
    Volume getVolume(String id) {
        return volumes[id]
    }

    /**
     * Processes the current request and produces the response.
     */
    void process() {
        request.initialize()
        if (request.validate()) {
            try {
                request.process()
            } catch (ConnectorException e) {
                log.info "Error executing command ${request.command}.", e
                if (e.statusCode) {
                    response.status = e.statusCode
                } else {
                    response.errors += e.errorCodes ?: ConnectorError.UNKNOWN
                }
            } catch (ConnectorWarning e) {
                log.info "Warning executing command ${request.command}.", e
                if (e.statusCode) {
                    response.status = e.statusCode
                } else {
                    response.warnings += e.warningCodes ?: ConnectorError.UNKNOWN
                }
            }
        }
        request.close()
        response.output()
    }


    //-- Non-public methods ---------------------

    /**
     * Loads and instantiates the class for the given command.
     *
     * @param command                   the given command
     * @return                          an instance of the associated command
     *                                  class
     * @throws ClassNotFoundException   if the associated class for the given
     *                                  command was not found
     */
    protected Command createCommandClass(String command) {
        def name = "${this.class.package.name}.command.${command.capitalize()}Command"
        try {
            Class<?> cls = Class.forName(
                name, true, Thread.currentThread().contextClassLoader
            )
            log.debug "Found class ${name} for command '${command}'."
            return cls.newInstance()
        } catch (ClassNotFoundException up) {
            log.error "Cannot find class ${name} for command '${command}'."
            throw up
        }
    }
}
