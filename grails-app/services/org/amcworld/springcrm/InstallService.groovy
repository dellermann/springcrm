/*
 * InstallService.groovy
 *
 * Copyright (c) 2011-2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
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
 * @version 0.9
 */
class InstallService {

    //-- Constants ------------------------------

    /**
     * The directory containing the packages with base data which are loaded
     * into the database during installation.
     */
    protected static final String BASE_PACKAGE_DIR = '/WEB-INF/data/install'


    //-- Instance variables ---------------------

    ServletContext servletContext = SCH.servletContext


    //-- Public methods -------------------------

    /**
     * Checks whether or not the installer is enabled.  The installer is enable
     * if there is a file {@code ENABLE_INSTALLER} in
     * {@code .springcrm/install/} in the user's home directory.
     *
     * @return  {@code true} if the installer is enabled; {@code false}
     *          otherwise
     */
    boolean isInstallerDisabled() {
        return !enableFile.exists()
    }

    /**
     * Enables the installer by creating the installer enable file
     * {@code ENABLE_INSTALLER} in {@code .springcrm/install/} in the user's
     * home directory.
     */
    void enableInstaller() {
        enableFile.createNewFile()
    }

    /**
     * Disables the installer by deleting the installer enable file
     * {@code ENABLE_INSTALLER} in {@code .springcrm/install/} in the user's
     * home directory.
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
     * stored in {@code .springcrm/install/} in the user's home directory.
     *
     * @return  the installer enable file
     */
    protected File getEnableFile() {
        def f = new File("${System.getProperty('user.home')}/.springcrm/install")
        if (!f.exists()) {
            f.mkdirs()
        }
        return new File(f, 'ENABLE_INSTALLER')
    }
}
