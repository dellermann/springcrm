/*
 * DocumentService.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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

import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.NameScope
import org.apache.commons.vfs2.Selectors
import org.apache.commons.vfs2.VFS
import org.codehaus.groovy.grails.commons.GrailsApplication


class DocumentService {

    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    GrailsApplication grailsApplication


    //-- Public methods -------------------------

	/**
	 * Creates a new folder with the given name in the stated path.
	 *
	 * @param path	the path where the new folder should be created
	 * @param name	the name of the new folder
	 * @return		the created folder
	 */
	FileObject createFolder(String path, String name) {
		FileObject p = getFile(path)
		FileObject f = p.resolveFile(name, NameScope.CHILD)
		f.createFolder()
		f
	}

	/**
	 * Deletes the file or folder with the given path.  If it is a folder all
	 * its content is deleted, too.
	 *
	 * @param path	the path of the file or folder that should be deleted
	 * @return		the number of deleted files
	 */
	int deleteFileObject(String path) {
		int num = 0
		FileObject fo = getFile(path)
		if (fo != root) {
			num = fo.delete Selectors.SELECT_ALL
		}
		num
	}

    /**
     * Gets the {@code FileObject} with the given path.  The method ensures
     * that only pathes below the root are used.
     *
     * @param path  the given path
     * @return      the file
     */
    FileObject getFile(String path) {
        root.resolveFile path, NameScope.DESCENDENT_OR_SELF
    }

    /**
     * Gets the {@code FileObject} representing the root of the document file
     * system.
     *
     * @return  the document root
     */
    FileObject getRoot() {
        VFS.manager.resolveFile rootPath
    }

    /**
     * Gets the path where to store the documents of this application.
     *
     * @return  the root path to store documents
     */
    String getRootPath() {
        grailsApplication.config.springcrm.dir.documents
    }

    /**
     * Uploads a file to the given folder using the given name.
     *
     * @param path      the path where to store the file
     * @param fileName  the name of the file to create
     * @param data      the file data to store
     * @return			information about the uploaded file
     */
    FileObject uploadFile(String path, String fileName, InputStream data) {
        FileObject dir = getFile(path)
        FileObject f = dir.resolveFile(fileName, NameScope.DESCENDENT_OR_SELF)
        f.content.outputStream << data
        f.content.close()
		f
    }
}
