/*
 * ParentsCommand.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
import org.apache.commons.logging.LogFactory


/**
 * The class {@code ParentsCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.2
 */
class ParentsCommand extends Command {

    //-- Constants ------------------------------

//    private static final log = LogFactory.getLog(this)


    //-- Public methods -------------------------

    @Override
    public void execute() {
        if (target) {
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorException(CE.OPEN, targetHash)
            }

            List<Map<String, Object>> tree = volume.parents(target)
            if (tree == null) {
                throw new ConnectorException(CE.OPEN, targetHash)
            }
            response['tree'] = tree
        }
    }
}
