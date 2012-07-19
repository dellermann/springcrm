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

import org.amcworld.springcrm.elfinder.ConnectorError as CE
import org.amcworld.springcrm.elfinder.ConnectorException
import org.amcworld.springcrm.elfinder.ConnectorThrowable
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
            List<MultipartFile> files = connector.request.files
            if (!files) {
                throw new ConnectorException(CE.UPLOAD, CE.UPLOAD_NO_FILES)
            }
            Volume volume = getVolume(target)
            if (!volume) {
                throw new ConnectorException(
                    CE.UPLOAD, CE.TRGDIR_NOT_FOUND, targetHash
                )
            }
            List<Map<String, Object>> added = []
            for (MultipartFile item : files) {
                InputStream stream = item.inputStream
                try {
                    String name = item.originalFilename
                    Map<String, Object> stat
                    try {
                        stat = volume.upload(stream, target, name)
                    } catch (ConnectorThrowable ct) {
                        throw new ConnectorException(CE.UPLOAD_FILE, name, ct)
                    }
                    if (!stat) {
                        throw new ConnectorException(CE.UPLOAD_FILE, name)
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
