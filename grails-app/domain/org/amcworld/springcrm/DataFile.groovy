/*
 * DataFile.groovy
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

import net.sf.jmimemagic.Magic
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code DataFile} represents a data file stored along other
 * instances such as purchase invoices or ticket messages.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
class DataFile {

    //-- Class fields ---------------------------

    static constraints = {
        fileName blank: false
        mimeType nullable: true
        fileSize range: 0L..104_857_600L        // 0..100M
    }
    static transients = ['storageName']


    //-- Fields ---------------------------------

    String fileName
    String mimeType
    long fileSize
    Date dateCreated
    Date lastUpdated


    //-- Constructors ---------------------------

    DataFile() {}

    /**
     * Creates a new data file instance using the given file.
     *
     * @param file  the given file; the file must exist
     */
    DataFile(File file) {
        populate file
    }

    /**
     * Creates a new data file instance using the given uploaded file.
     *
     * @param file  the given uploaded file; the file must not be empty
     */
    DataFile(MultipartFile file) {
        populate file
    }


    //-- Properties -----------------------------

    /**
     * Gets the file name under which this data file should be stored in file
     * system.  The method uses the ID of this object and therefore, must be
     * persisted before calling this method.
     *
     * @return  the storage file name
     */
    String getStorageName() {
        if (!id) {
            throw new IllegalStateException(
                'No ID set. Store DataFile before calling getStorageName().'
            )
        }

        Long.toHexString(id).toUpperCase().padLeft(16, '0')
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof DataFile && id == obj.id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    /**
     * Populates this instance with the data of the given file.
     *
     * @param file  the given file; the file must exist
     */
    void populate(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("File ${file} must exist.")
        }

        this.fileName = file.name
        this.mimeType = Magic.getMagicMatch(file, true).mimeType
        this.fileSize = file.length()
    }

    /**
     * Populates this instance with the data of the given uploaded file.
     *
     * @param file  the given uploaded file; the file must not be empty
     */
    void populate(MultipartFile file) {
        if (file.empty) {
            throw new IllegalArgumentException('Given MultipartFile is empty.')
        }

        this.fileName = file.originalFilename
        this.mimeType = Magic.getMagicMatch(file.bytes).mimeType
        this.fileSize = file.size
    }

    @Override
    String toString() {
        fileName ?: ''
    }
}


/**
 * The enumeration {@code DataFileType} contains various types of data files
 * which can be stored in the system.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
enum DataFileType {
    purchaseInvoice,
    ticketMessage
}
