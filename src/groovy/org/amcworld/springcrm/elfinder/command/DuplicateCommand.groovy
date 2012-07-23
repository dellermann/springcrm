/*
 * DuplicateCommand.groovy
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
 * The class {@code DuplicateCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class DuplicateCommand extends Command {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        List<Map<String, Object>> added = []
        for (String target : targets) {
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorException(
                    CE.COPY, '#' + target, CE.FILE_NOT_FOUND
                )
            }
            Map<String, Object> src = volume.file(target)
            if (!src) {
                throw new ConnectorException(
                    CE.COPY, '#' + target, CE.FILE_NOT_FOUND
                )
            }
            added << volume.duplicate(target)
        }
        response['added'] = added
    }
}
