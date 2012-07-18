/*
 * UploadCommand.groovy
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
import org.amcworld.springcrm.elfinder.fs.Volume
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code UploadCommand} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class UploadCommand extends Command {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        if (target) {
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorException(
                    ConnectorError.UPLOAD, ConnectorError.TRGDIR_NOT_FOUND
                )
            }
            List<Map<String, Object>> added = []
            for (MultipartFile item : connector.request.files) {
                InputStream stream = item.inputStream
                try {
                    Map<String, Object> stat = volume.upload(
                        stream, target, item.originalFilename
                    )
                    if (!stat) {
                        throw new ConnectorException(ConnectorError.UPLOAD_FILE)
                    }
                    added += stat
                } finally {
                    stream.close()
                }
            }
            response['added'] = added
        }
    }
}
