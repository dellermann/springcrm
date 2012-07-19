/*
 * FileCommand.groovy
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

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import org.amcworld.springcrm.elfinder.ConnectorException
import org.amcworld.springcrm.elfinder.fs.Volume


/**
 * The class {@code FileCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class FileCommand extends Command {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        if (target) {
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorException(SC_NOT_FOUND)
            }
            Map<String, Object> stat = volume.file(target)
            if (stat == null) {
                throw new ConnectorException(SC_NOT_FOUND)
            }
            if (!stat.read) {
                throw new ConnectorException(SC_FORBIDDEN)
            }
            InputStream stream = volume.open(target)
            if (stream == null) {
                throw new ConnectorException(SC_NOT_FOUND)
            }

            boolean download = !!getParam('download')
            String name = stat.name //URLEncoder.encode(stat.name, 'utf-8')
            String mime = stat.mime
            String disposition = 'attachment'
            if (!download &&
                (mime.matches(~/^(image|text)/) ||
                (mime == 'application/x-shockwave-flash')))
            {
                disposition = 'inline'
            }
            response.contentType = download ? 'application/octet-stream' : mime
            response.headers += [
                'Content-Disposition': "${disposition}; filename=\"${name}\"",
                'Content-Location': name,
                'Content-Transfer-Encoding': 'binary',
                'Content-Length': stat.size,
                'Connection': 'close'
            ]
            response << stream
        }
    }
}
