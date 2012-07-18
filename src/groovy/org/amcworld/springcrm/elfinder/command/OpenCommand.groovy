/*
 * OpenCommand.groovy
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
import org.amcworld.springcrm.elfinder.ConnectorError
import org.amcworld.springcrm.elfinder.ConnectorException
import org.amcworld.springcrm.elfinder.fs.Volume


/**
 * The class {@code OpenCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class OpenCommand extends Command {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        boolean init = !!getParam('init')

        Volume volume
        Map<String, Object> cwd
        if (target) {
            volume = getVolume(target)
            if (volume) {
                cwd = volume.dir(target)
            }
        }
        if ((!cwd || !cwd.read) && init) {
            volume = connector.defaultVolume
            cwd = volume.dir(volume.defaultPathHash)
        }
        if (!cwd) {
            throw new ConnectorException(
                ConnectorError.OPEN, ConnectorError.DIR_NOT_FOUND
            )
        } else if (!cwd.read) {
            throw new ConnectorException(
                ConnectorError.OPEN, ConnectorError.PERM_DENIED
            )
        }

        List<Map<String, Object>> files = []
        if (!!getParam('tree')) {
            for (Volume vol : connector.volumes.values()) {
                List<Map<String, Object>> tree = vol.tree()
                if (tree != null) {
                    files += tree
                }
            }
            if (log.debugEnabled) {
                log.debug "Loaded trees of ${connector.volumes.size()} volumes: ${files.size()} items in file list."
            }
        }

        List<Map<String, Object>> ls = volume.scanDir(cwd.hash)
        files += ls
        if (log.debugEnabled) {
            log.debug "Scanned ${volume.decode(cwd.hash)}: ${files.size()} items in file list."
        }
        files.unique { return it.hash }
        if (log.debugEnabled) {
            log.debug "Removed duplicates: ${files.size()} items in file list."
        }

        response['cwd'] = cwd
        response['files'] = files
        response['options'] = volume.options(cwd.hash)

        if (getParam('init')) {
            response['api'] = Connector.VERSION
            response['uplMaxSize'] = connector.config.uploadMaxSize + 'M'
        }
    }
}
