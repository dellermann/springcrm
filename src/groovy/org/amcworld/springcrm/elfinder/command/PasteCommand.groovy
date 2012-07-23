/*
 * PasteCommand.groovy
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
import org.amcworld.springcrm.elfinder.ConnectorWarning
import org.amcworld.springcrm.elfinder.fs.Volume


/**
 * The class {@code PasteCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class PasteCommand extends Command {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        String dest = getParam('dst')
        String [] targets = connector.request.params['targets'] ?: []
        boolean cut = !!getParam('cut')
        CE error = cut ? CE.MOVE : CE.COPY

        Volume destVolume = getVolume(dest)
        if (!destVolume) {
            throw new ConnectorException(
                error, '#' + targets[0], CE.TRGDIR_NOT_FOUND, '#' + dest
            )
        }

        List<Map<String, Object>> added = []
        for (String target : targets) {
            Volume srcVolume = getVolume(target)
            if (!srcVolume) {
                throw new ConnectorWarning(
                    error, '#' + target, CE.FILE_NOT_FOUND
                )
            }
            try {
                added << destVolume.paste(srcVolume, target, dest, cut)
            } catch (ConnectorException e) {
                throw new ConnectorWarning(e)
            }
        }
        response['added'] = added
        response['removed'] = []
    }
}
