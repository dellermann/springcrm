/*
 * AbstractCommand.groovy
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


package org.amcworld.springcrm.elfinder.command

import org.amcworld.springcrm.elfinder.Connector
import org.amcworld.springcrm.elfinder.Response
import org.amcworld.springcrm.elfinder.fs.Volume


/**
 * The class {@code AbstractCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
abstract class Command {

    //-- Instance variables ---------------------

    Connector connector


    //-- Public methods -------------------------

    /**
     * Executes this command and performs the actual work.
     */
    abstract void execute()

    /**
     * Initializes this command.  Derived classes may implement this method
     * for their own.
     */
    void init() {}


    //-- Non-public methods ---------------------

    /**
     * Gets the request parameter with the given name.
     *
     * @param name  the given name
     * @return      the value of the parameter; {@code null} if no parameter
     *              with that name exists
     */
    protected String getParam(String name) {
        return connector.request.params[name]?.toString()?.trim()
    }

    /**
     * Gets access to the ElFinder response object.
     *
     * @return  the response object
     */
    protected Response getResponse() {
        return connector.response
    }

    /**
     * Gets the target hash code of the command, that is, the parameter
     * {@code target} of the request.
     *
     * @return  the target hash code
     */
    protected String getTarget() {
        return getParam('target')
    }

    /**
     * Gets the target in hash form, that is in form "#target", which is used
     * when sending errors back to the client.
     *
     * @return  the target hash code with a "#" prefix
     */
    protected String getTargetHash() {
        return '#' + target
    }

    /**
     * Gets an array of target hash codes of the command, that is, the
     * parameter {@code targets} of the request.
     *
     * @return  the target hash codes or an empty array if no targets have been
     *          submitted
     */
    protected String [] getTargets() {
        return connector.request.params['targets'] ?: []
    }

    /**
     * Gets the volume associated to the given hash code.
     *
     * @param hash  the given hash code
     * @return      the associated volume; {@code null} if no such volume
     *              exists or the given hash code is invalid
     */
    protected Volume getVolume(String hash) {
        return connector.getVolume(Volume.decodeVolumeId(hash))
    }
}
