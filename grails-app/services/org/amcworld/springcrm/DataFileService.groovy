/*
 * DataFileService.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import grails.core.GrailsApplication
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code DataFileService} handles files in the data space.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
@Transactional
class DataFileService {

    //-- Instance variables ---------------------

    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    /**
     * Gets the base directory of the data space for the given type of files
     * using the configuration value in key {@code springcrm.dir.data}.
     *
     * @param type  the given type of files
     * @return      the base directory
     */
    File getBaseDir(DataFileType type) {
        getBaseDir type.toString()
    }

    /**
     * Gets the base directory of the data space for the given type of files
     * using the configuration value in key {@code springcrm.dir.data}.  This
     * method is for backward compatibility, only.
     *
     * @param type  the given type of files
     * @return      the base directory
     */
    File getBaseDir(String type) {
        File dir = new File(grailsApplication.config.springcrm.dir.data, type)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        dir
    }

    /**
     * Removes the given file from the data space and deletes the data file
     * instance from the database.
     *
     * @param type      the type of file to remove
     * @param dataFile  the given data file
     * @return          {@code true} if the file was removed successfully;
     *                  {@code false} otherwise
     */
    boolean removeFile(DataFileType type, DataFile dataFile) {
        boolean res = retrieveFile(type, dataFile).delete()
        res && dataFile.delete()
    }

    /**
     * Retrieves the underlying file specified by the given data file from the
     * data space.
     *
     * @param type      the type of file to retrieve
     * @param dataFile  the given data file
     * @return          the file object representing the required file on file
     *                  system
     */
    @Transactional(readOnly = true)
    File retrieveFile(DataFileType type, DataFile dataFile) {
        new File(getBaseDir(type), dataFile.storageName)
    }

    /**
     * Stores the given uploaded file to the data space and a {@code DataFile}
     * instance in the database.
     *
     * @param type  the type of file to store
     * @param f     the uploaded file; may be {@code null} or empty
     * @return      the data file containing information about the stored file;
     *              {@code null} if the given file to store is empty
     */
    DataFile storeFile(DataFileType type, MultipartFile f) {
        DataFile dataFile = null
        if (f && !f.empty) {
            dataFile = new DataFile(f)
            dataFile.save failOnError: true
            f.transferTo retrieveFile(type, dataFile)
        }
        dataFile
    }

    /**
     * Updates the given data file by the stated uploaded file.
     *
     * @param type      the type of file to update
     * @param dataFile  the given data file; if {@code null} a new data file
     *                  will be created
     * @param f         the stated uploaded file; if the file is empty the
     *                  given data file is not updated
     * @return          the updated data file
     */
    DataFile updateFile(DataFileType type, DataFile dataFile, MultipartFile f) {
        if (!dataFile) {
            dataFile = storeFile(type, f)
        } else if (!f.empty) {
            retrieveFile(type, dataFile).delete()
            dataFile.populate f
            dataFile.save failOnError: true
            f.transferTo retrieveFile(type, dataFile)
        }
        dataFile
    }
}
