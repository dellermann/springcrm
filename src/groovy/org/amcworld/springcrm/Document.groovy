/*
 * Document.groovy
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


/**
 * The class {@code Document} represents a document which is used in the
 * document management of this application.
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class Document {

    //-- Instance variables ---------------------

    String id
    String name
    long size
    Date lastModified


    //-- Constructors ---------------------------

    /**
     * Creates a new document represented by the given file which is stored in
     * the stated root directory.
     *
     * @param rootDir   the stated root directory
     * @param f         the file representing the document
     */
    Document(String rootDir, File f) {
        this.id = encode(rootDir, f.path)
        this.name = f.name
        this.size = f.length()
        this.lastModified = new Date(f.lastModified())
    }


    //-- Public methods -------------------------

    /**
     * Decodes the given hash code and returns the associated file.
     *
     * @param rootDir   the root path
     * @param id        the given ID
     * @return          the associated file
     */
    static File decode(String rootDir, String id) {
        String relPath = new String(id.tr('-_.', '+/=').decodeBase64())
        if (relPath == '/') {
            relPath = ''
        }
        return new File(rootDir, relPath)
    }

    /**
     * Gets the size of this document in a human readable form.
     *
     * @return  the size as string
     */
    String getSizeAsString() {
        if (size >= 1024 ** 3) {
            return "${((size / 1024 ** 3) as Double).round(1)} GB"
        } else if (size >= 1024 ** 2) {
            return "${((size / 1024 ** 2) as Double).round(1)} MB"
        } else if (size >= 1024) {
            return "${((size / 1024) as Double).round(1)} KB"
        } else {
            return "${size} B"
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Encodes the given file path.
     *
     * @param rootDir   the given root path
     * @param path      the given file path; may be {@code null}
     * @return          the computed hash code; {@code null} if the path is
     *                  {@code null}
     */
    protected String encode(String rootDir, String path) {
        if (path == null) {
            return null
        }

        String relPath = relPath(rootDir, path) ?: '/'
        return relPath.bytes.encodeBase64().toString().tr('+/=', '-_.').replaceFirst(/\.+$/, '')
    }

    /**
     * Computes the path relative to the given root path.
     *
     * @param rootDir   the given root path
     * @param path      the given path
     * @return          the relative path
     */
    protected String relPath(String rootDir, String path) {
        return (path == rootDir) ? '' : path.substring(rootDir.length() + 1)
    }
}
