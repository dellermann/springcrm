/*
 * DocumentService.groovy
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
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.apache.commons.vfs2.*


@CompileStatic
class DocumentService {

    //-- Class fields ---------------------------

    static transactional = false


    //-- Fields ---------------------------------

    ConfigService configService
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
     * that only paths below the root are used.
     *
     * @param path  the given path
     * @return      the file
     */
    FileObject getFile(String path) {
        root.resolveFile path, NameScope.DESCENDENT_OR_SELF
    }

    /**
    * Gets all files belonging to the given organization.
    *
    * @param org    the given organization
    * @return		a list of files belonging to the given organization
    */
    List<FileObject> getFilesOfOrganization(Organization org) {
        FileObject folder = getFolderOfOrganization(org)
        if (!folder) {
            return []
        }

        folder.findFiles(new FileSelector() {
                boolean includeFile(FileSelectInfo fi) {
                    fi.depth > 0 && fi.file.type == FileType.FILE &&
                        !fi.file.hidden
                }

                boolean traverseDescendents(FileSelectInfo fi) { true }
            }) as List
    }

    /**
    * Gets the folder containing the documents of the given organization.  The
    * method uses the root directory (see {@code getRootPath} and then tries
    * various modifications of the organization name.
    *
    * @param org    the given organization
    * @return       the folder of that organization; {@code null} if no such
    *               folder could be found
    * @see			#getRootPath()
    */
    FileObject getFolderOfOrganization(Organization org) {
        String pathSpec = configService.getString('pathDocumentByOrg') ?: '%o'

        // check the document placeholder of the organization
        FileObject folder
        String name = org.docPlaceholderValue
        if (name) {
            folder = getFile(pathSpec.replace('%o', name))
            if (folder.exists()) {
                return folder
            }
        }

        // check variations of the organization name
        name = org.name
        folder = checkDirAndUpdate(org, pathSpec, name)
        if (folder) return folder

        folder = checkDirAndUpdate(org, pathSpec, name.replace(' ', '-'))
        if (folder) return folder

        folder = checkDirAndUpdate(org, pathSpec, name.replace(' ', '_'))
        if (folder) return folder

        name = name.toLowerCase()
        folder = checkDirAndUpdate(org, pathSpec, name)
        if (folder) return folder

        folder = checkDirAndUpdate(org, pathSpec, name.replace(' ', '-'))
        if (folder) return folder

        checkDirAndUpdate org, pathSpec, name.replace(' ', '_')
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
    @CompileStatic(TypeCheckingMode.SKIP)
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


    //-- Non-public methods ---------------------

    private FileObject checkDirAndUpdate(Organization org, String pathSpec,
                                         String name)
    {
        FileObject folder = getFile(pathSpec.replace('%o', name))
        if (!folder.exists()) {
            return null
        }

        org.docPlaceholderValue = name
        org.save flush: true

        folder
    }
}
