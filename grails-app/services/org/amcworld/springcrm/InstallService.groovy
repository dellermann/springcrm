/*
 * InstallService.groovy
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

import groovy.io.FileType
import javax.servlet.ServletContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH


/**
 * The class {@code InstallService} implements various methods to handle
 * installer issues, such as enabling or disabling the installer for security
 * reasons or obtaining the available base data packages.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class InstallService {

    //-- Constants ------------------------------

    /**
     * The directory containing the packages with base data which are loaded
     * into the database during installation.
     */
    protected static final String BASE_PACKAGE_DIR = '/WEB-INF/data/install'


    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    def grailsApplication
    ServletContext servletContext = SCH.servletContext


    //-- Public methods -------------------------

    /**
     * Checks whether or not the installer is enabled.  The installer is enable
     * if there is a file {@code ENABLE_INSTALLER} in the installer directory
     * as specified in the configuration file in key
     * {@code springcrm.dir.installer}.
     *
     * @return  {@code true} if the installer is enabled; {@code false}
     *          otherwise
     */
    boolean isInstallerDisabled() {
        return !enableFile.exists()
    }

    /**
     * Enables the installer by creating the installer enable file
     * {@code ENABLE_INSTALLER} in the installer directory as specified in the
     * configuration file in key {@code springcrm.dir.installer}.
     */
    void enableInstaller() {
        log.info(enableFile)
        enableFile.createNewFile()
    }

    /**
     * Disables the installer by deleting the installer enable file
     * {@code ENABLE_INSTALLER} in the installer directory as specified in the
     * configuration file in key {@code springcrm.dir.installer}.
     */
    void disableInstaller() {
        enableFile.delete()
    }

    /**
     * Gets a list of package keys containing base data for installation.  The
     * packages must be located in directory specified by
     * {@link #BASE_PACKAGE_DIR} and named {@code base-data-<key>.sql},
     * where {@code <key>} represents the package key.
     *
     * @return  the package keys as defined above
     */
    List<String> getBaseDataPackages() {
        def files = []
        servletContext.getResourcePaths(BASE_PACKAGE_DIR).each {
                def m = (it =~ /^.*\/base-data-([-\w]+)\.sql$/)
                if (m.matches()) {
                    files.add(m[0][1])
                }
            }
        return files
    }

    /**
     * Loads the base data package with the given key.
     *
     * @param key   the given package key
     * @return      an input stream to read the package; {@code null} if no
     *              package with the given key exists
     */
    InputStream loadPackage(String key) {
        return servletContext.getResourceAsStream(
            "${BASE_PACKAGE_DIR}/base-data-${key}.sql"
        )
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the object representing the installer enable file.  This file is
     * stored in the installer directory as specified in the configuration
     * file in key {@code springcrm.dir.installer}.
     *
     * @return  the installer enable file
     */
    protected File getEnableFile() {
        def dir = new File(grailsApplication.config.springcrm.dir.installer)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return new File(dir, 'ENABLE_INSTALLER')
    }
}
