/*
 * DataFileController.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND


/**
 * The class {@code DataFileController} contains actions which handles access
 * to data files.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.4
 */
class DataFileController {

    //-- Instance variables ---------------------

    DataFileService dataFileService


    //-- Public methods -------------------------

    /**
     * Loads the data file with the given type and ID from frontend.
     *
     * @param type  the type of data file; must be a name defined by the
     *              enumeration {@link DataFileType}
     * @param id    the ID of the {@link DataFile} instance to retrieve
     */
    def frontendLoadFile(String type, Long id) {
        commonLoadFile type, id
    }

    /**
     * Loads the data file with the given type and ID from backend.
     *
     * @param type  the type of data file; must be a name defined by the
     *              enumeration {@link DataFileType}
     * @param id    the ID of the {@link DataFile} instance to retrieve
     */
    def loadFile(String type, Long id) {
        commonLoadFile type, id
    }


    //-- Non-public methods ---------------------

    /**
     * Loads the data file with the given type and ID.  This is a common method
     * for various actions.
     *
     * @param type  the type of data file; must be a name defined by the
     *              enumeration {@link DataFileType}
     * @param id    the ID of the {@link DataFile} instance to retrieve
     */
    private def commonLoadFile(String type, Long id) {
        def dataFileInstance = DataFile.get(id)
        if (dataFileInstance) {
            File f = dataFileService.retrieveFile(
                DataFileType.valueOf(type), dataFileInstance
            )
            if (f.exists()) {
                response.contentType = dataFileInstance.mimeType
                response.contentLength = dataFileInstance.fileSize
                response.addHeader(
                    'Content-Disposition',
                    "attachment; filename=\"${dataFileInstance.fileName}\""
                )
                response.outputStream << f.newInputStream()
                return null
            }
        }

        render status: SC_NOT_FOUND
    }
}
