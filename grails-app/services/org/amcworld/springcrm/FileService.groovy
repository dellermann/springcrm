/*
 * FileService.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import org.amcworld.springcrm.elfinder.fs.LocalFileSystemVolume
import org.amcworld.springcrm.elfinder.fs.Volume
import org.amcworld.springcrm.elfinder.fs.VolumeConfig
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code FileService} handles files in the document and data space.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.2
 */
class FileService {

    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    GrailsApplication grailsApplication
    def messageSource


    //-- Public methods -------------------------

    /**
     * Gets the local volume to retrieve and store documents.
     *
     * @return  the local volume
     */
    Volume getLocalVolume() {
        def alias = messageSource.getMessage(
            'document.rootAlias', null, 'Documents', LCH.locale
        )
        def config = new VolumeConfig(
            alias: alias,
            useCache: grailsApplication.config.springcrm.cacheDocs
        )
        new LocalFileSystemVolume('l', rootDir, config)
    }

    /**
     * Returns the directory containing the documents of the given
     * organization.  The method uses the base directory specified in the
     * application configuration under key {@code springcrm.dir.documents} and
     * the tries several modifications of the organization name.
     *
     * @param org   the given organization
     * @return      the directory of that organization; {@code null} if no such
     *              directory could be found
     */
    File getOrgDocumentDir(Organization org) {
        String baseDir = grailsApplication.config.springcrm.dir.documents
        String pathSpec = ConfigHolder.instance['pathDocumentByOrg']?.value ?: '%o'
        def checkDir = { s ->
            File dir = new File(baseDir, pathSpec.replace('%o', s))
            if (dir.exists()) {
                org.docPlaceholderValue = s
                org.save flush: true
                return dir
            }

            null
        }

        File dir
        String name = org.docPlaceholderValue
        if (name) {
            dir = new File(baseDir, pathSpec.replace('%o', name))
            if (dir.exists()) {
                return dir
            }
        }

        name = org.name
        dir = checkDir(name)
        if (!dir) {
            dir = checkDir(name.replace(' ', '-'))
            if (!dir) {
                dir = checkDir(name.replace(' ', '_'))
                if (!dir) {
                    name = name.toLowerCase()
                    dir = checkDir(name)
                    if (!dir) {
                        dir = checkDir(name.replace(' ', '-'))
                        if (!dir) {
                            dir = checkDir(name.replace(' ', '_'))
                        }
                    }
                }
            }
        }
        dir
    }

    /**
     * Gets the directory where to store the documents of this application.
     *
     * @return  the root directory to store documents
     */
    String getRootDir() {
        grailsApplication.config.springcrm.dir.documents
    }
}
