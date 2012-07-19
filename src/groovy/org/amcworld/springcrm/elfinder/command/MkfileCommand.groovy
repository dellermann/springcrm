/*
 * MkfileCommand.groovy
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
import org.amcworld.springcrm.elfinder.ConnectorThrowable
import org.amcworld.springcrm.elfinder.fs.Volume


/**
 * The class {@code MkfileCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class MkfileCommand extends Command {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        if (target) {
            String name = getParam('name')
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorException(
                    CE.MKFILE, name, CE.TRGDIR_NOT_FOUND, targetHash
                )
            }
            Map<String, Object> file
            try {
                file = volume.mkfile(target, name)
            } catch (ConnectorThrowable ct) {
                throw new ConnectorException(CE.MKFILE, name, ct)
            }
            if (!file) {
                throw new ConnectorException(CE.MKFILE, name)
            }
            response['added'] = [file]
        }
    }
}
