/*
 * InstallService.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import com.mongodb.BasicDBList
import com.mongodb.DBObject
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import grails.converters.JSON
import grails.core.GrailsApplication
import groovy.transform.CompileStatic
import org.bson.Document as MDocument
import org.grails.web.json.JSONArray
import org.springframework.core.io.Resource


/**
 * The class {@code InstallService} implements various methods to handle
 * installer issues, such as enabling or disabling the installer for security
 * reasons or obtaining the available base data packages.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@CompileStatic
class InstallService {

    //-- Constants ------------------------------

    /**
     * The directory containing the packages with base data which are loaded
     * into the database during installation.
     */
    private static final String BASE_PACKAGE_DIR = 'classpath:public/install'

    /**
     * The expiration time of the enable file in milliseconds.  After that time
     * the enable file should be removed.
     *
     * @since 2.0
     */
    private static final long ENABLE_FILE_EXPIRATION_TIME = 15 * 60000

    /**
     * The name of the enable file.
     *
     * @since 2.0
     */
    private static final String ENABLE_FILE_NAME = 'ENABLE_INSTALLER'

    /**
     * The available roles in this application.
     *
     * @since 3.0
     */
    private static final List<String> ROLES = [
        'ROLE_ADMIN', 'ROLE_BOILERPLATE', 'ROLE_CALENDAR', 'ROLE_CALL',
        'ROLE_CONTACT', 'ROLE_CREDIT_MEMO', 'ROLE_DOCUMENT', 'ROLE_DUNNING',
        'ROLE_HELPDESK', 'ROLE_INVOICE', 'ROLE_NOTE', 'ROLE_PRODUCT',
        'ROLE_PROJECT', 'ROLE_PURCHASE_INVOICE', 'ROLE_QUOTE', 'ROLE_REPORT',
        'ROLE_SALES_ORDER', 'ROLE_TICKET', 'ROLE_USER', 'ROLE_WORK'
    ].asImmutable()


    //-- Fields ---------------------------------

    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    /**
     * Disables the installer by deleting the installer enable file
     * {@code ENABLE_INSTALLER} in the installer directory as specified in the
     * configuration file in key {@code springcrm.dir.installer}.
     */
    void disableInstaller() {
        enableFile.delete()
    }

    /**
     * Enables the installer by creating the installer enable file
     * {@code ENABLE_INSTALLER} in the installer directory as specified in the
     * configuration file in key {@code springcrm.dir.installer}.
     */
    void enableInstaller() {
        log.info enableFile.toString()
        enableFile.createNewFile()
    }

    /**
     * Gets a list of package keys containing base data for installation.  The
     * packages must be located in directory specified by
     * {@link #BASE_PACKAGE_DIR} and named {@code base-data-<key>.sql},
     * where {@code <key>} represents the package key.  The package keys itself
     * must be stored in file {@code base-data.json} in the same directory.
     *
     * @return  the package keys as defined above
     */
    List<String> getBaseDataPackages() {
        List<String> files = []

        Resource res = grailsApplication.mainContext.getResource(
            BASE_PACKAGE_DIR + '/base-data.json'
        )
        if (res.exists()) {
            JSONArray json = (JSONArray) JSON.parse(
                new InputStreamReader(res.inputStream)
            )
            for (Object elem : json) {
                files << elem.toString()
            }
        }

        files
    }

    /**
     * Installs a user group for administrators with the given localized name
     * if it does not exist yet.
     *
     * @param name  the localized group name
     * @return      the installed user group
     * @since 3.0
     */
    RoleGroup installAdminGroup(String name) {
        Role role = Role.find(Filters.eq('authority','ROLE_ADMIN')).first()

        RoleGroup group =
            RoleGroup.find(Filters.eq('authorities', role.id)).first()
        if (group == null) {
            group = new RoleGroup(name: name, authorities: [role] as Set)
                .save failOnError: true, flush: true
        }

        group
    }

    /**
     * Installs the base data package with the given key in the database.
     *
     * @param key   the given package key
     * @since       1.4
     */
    void installBaseDataPackage(String key) {
        InputStream stream = loadBaseDataPackage(key)
        if (stream != null) {
            MongoDatabase db = Organization.DB

            DBObject obj = (DBObject) com.mongodb.util.JSON.parse(
                stream.getText('utf-8')
            )
            for (String collectionName : obj.keySet()) {
                List<MDocument> list = []
                for (Object elem : obj.get(collectionName) as BasicDBList) {
                    list << new MDocument(elem as Map)
                }
                MongoCollection<MDocument> collection =
                    db.getCollection(collectionName)
                collection.drop()
                collection.insertMany(list)
            }
        }
    }

    /**
     * Installs all necessary roles for this application.
     *
     * @since 3.0
     */
    void installRoles() {
        for (String role : ROLES) {
            installRole role
        }
    }

    /**
     * Checks whether or not the enable file has been expired.
     *
     * @return  {@code true} if the enable file has been expired; {@code false}
     *          otherwise
     * @since   2.0
     */
    boolean isEnableFileExpired() {
        long mod = enableFile.lastModified()

        mod == 0L ? false
            : System.currentTimeMillis() - mod > ENABLE_FILE_EXPIRATION_TIME
    }

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
        !enableFile.exists()
    }

    /**
     * Loads the base data package with the given key.
     *
     * @param key   the given package key
     * @return      an input stream to read the package; {@code null} if no
     *              package with the given key exists
     */
    InputStream loadBaseDataPackage(String key) {
        getResource "${BASE_PACKAGE_DIR}/base-data-${key}.json"
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the object representing the installer enable file.  This file is
     * stored in the installer directory as specified in the configuration
     * file in key {@code springcrm.dir.installer}.
     *
     * @return  the installer enable file
     */
    private File getEnableFile() {
        File dir = new File(
            grailsApplication.config.getProperty('springcrm.dir.installer')
        )
        if (!dir.exists()) {
            dir.mkdirs()
        }

        new File(dir, ENABLE_FILE_NAME)
    }

    /**
     * Gets the input stream to the resource with the given path.
     *
     * @param path  the given path
     * @return      the input stream or {@code null} if no such resource exists
     * @since       2.1
     */
    private InputStream getResource(CharSequence path) {
        Resource res = grailsApplication.mainContext.getResource(
            path.toString()
        )

        res.exists() ? res.inputStream : null
    }

    /**
     * Installs the role with the given name if it does not exist already.
     *
     * @param name  the name of the role
     * @return      the installed role
     */
    private static Role installRole(String name) {
        Role role = Role.find(Filters.eq('authority', name)).first()
        if (role == null) {
            role = new Role(authority: name)
                .save failOnError: true, flush: true
        }

        role
    }
}
