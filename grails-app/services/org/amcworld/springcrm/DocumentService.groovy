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
import org.apache.commons.vfs2.FileSystemManager
import org.apache.commons.vfs2.VFS
import org.codehaus.groovy.grails.commons.GrailsApplication


class DocumentService {

    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    /**
     * Gets the {@code FileObject} representing the root of the document file
     * system.
     *
     * @return  the document root
     */
    FileObject getRoot() {
        FileSystemManager fsm = VFS.manager
        fsm.resolveFile rootPath
    }

    /**
     * Gets the path where to store the documents of this application.
     *
     * @return  the root path to store documents
     */
    String getRootPath() {
        grailsApplication.config.springcrm.dir.documents
    }
}
