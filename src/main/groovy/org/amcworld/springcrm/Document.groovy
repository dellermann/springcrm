/*
 * Document.groovy
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

import java.text.NumberFormat


/**
 * The class {@code Document} represents a document which is used in the
 * document management of this application.
 *
 * @author	Daniel Ellermann
 * @version 2.0
 * @since   1.2
 */
class Document {

    //-- Fields ---------------------------------

    /**
     * The ID of this document.
     */
    String id

    /**
     * The file name of this document.
     */
    String name

    /**
     * The size of this document in bytes.
     */
    long size

    /**
     * The timestamp of the last modification.
     */
    Date lastModified


    //-- Constructors ---------------------------

    /**
     * Creates a new document represented by the given ID and file instance.
     *
     * @param id    the given ID
     * @param f     the given file representing the document
     */
    Document(String id, File f) {
        this.id = id
        this.name = f.name
        this.size = f.length()
        this.lastModified = new Date(f.lastModified())
    }


    //-- Properties -----------------------------

    /**
     * Gets the timestamp of the last modification.  The method returns a copy
     * of the internal last modification timestamp.
     *
     * @return  the timestamp of the last modification
     */
    Date getLastModified() {
        new Date(lastModified.time)
    }


    //-- Public methods -------------------------

    /**
     * Gets the size of this document in a human readable form.
     *
     * @return  the size as string
     */
    String getSizeAsString() {
        BigDecimal val
        String unit
        if (size >= 1024 ** 3) {
            val = size / 1024 ** 3
            unit = 'GB'
        } else if (size >= 1024 ** 2) {
            val = size / 1024 ** 2
            unit = 'MB'
        } else if (size >= 1024) {
            val = size / 1024
            unit = 'KB'
        } else {
            val = size
            unit = 'B'
        }

        def format = NumberFormat.instance
        format.maximumFractionDigits = (unit == 'B') ? 0 : 1
        StringBuilder buf = new StringBuilder(format.format(val.doubleValue()))
        buf << ' ' << unit

        buf.toString()
    }
}
