/*
 * DocumentController.groovy
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


package org.amcworld.springcrm

import org.amcworld.springcrm.elfinder.fs.VolumeConfig;
import org.amcworld.springcrm.elfinder.fs.LocalFileSystemVolume;
import org.amcworld.springcrm.elfinder.Connector
import org.amcworld.springcrm.elfinder.ConnectorConfig


class DocumentController {

    //-- Public methods -------------------------

    def index() {}

    def command() {
        def conn = new Connector(request, response)
        conn.addVolume(new LocalFileSystemVolume(
            'local', '/net/nike/data/docs/amc-gmbh/Kunden'
        ))
        conn.process()
    }
}
