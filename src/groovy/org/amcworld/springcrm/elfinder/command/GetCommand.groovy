/*
 * GetCommand.groovy
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

import org.amcworld.springcrm.elfinder.ConnectorError as CE
import org.amcworld.springcrm.elfinder.ConnectorException
import org.amcworld.springcrm.elfinder.fs.Volume


/**
 * The class {@code GetCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class GetCommand extends Command {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        if (target) {
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorException(
                    CE.OPEN, targetHash, CE.FILE_NOT_FOUND
                )
            }
            Map<String, Object> file = volume.file(target)
            if (!file) {
                throw new ConnectorException(
                    CE.OPEN, targetHash, CE.FILE_NOT_FOUND
                )
            }

            try {
                response['content'] = volume.loadContent(target)
            } catch (ConnectorException e) {
                throw new ConnectorException(CE.OPEN, volume.path(target), e)
            }
        }
    }
}
