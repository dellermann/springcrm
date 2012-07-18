/*
 * RmCommand.groovy
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

import org.amcworld.springcrm.elfinder.ConnectorError
import org.amcworld.springcrm.elfinder.ConnectorException
import org.amcworld.springcrm.elfinder.ConnectorWarning
import org.amcworld.springcrm.elfinder.fs.Volume
import org.apache.commons.logging.LogFactory


/**
 * The class {@code RmCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class RmCommand extends Command {

    //-- Constants ------------------------------

    private static final log = LogFactory.getLog(this)


    //-- Public methods -------------------------

    @Override
    public void execute() {
        String [] targets = connector.request.params['targets'] ?: []
        for (String target : targets) {
            if (log.debugEnabled) {
                log.debug "Deleting file ${target}…"
            }
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorWarning(
                    ConnectorError.RM, ConnectorError.FILE_NOT_FOUND
                )
            }
            try {
                if (!volume.rm(target)) {
                    throw new ConnectorWarning(ConnectorError.RM)
                }
            } catch (ConnectorException e) {
                throw new ConnectorWarning(e.errorCodes)
            }
        }
        response['removed'] = []
    }
}
