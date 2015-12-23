/*
 * InstallService.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import groovy.sql.Sql
import java.sql.Connection
import javax.servlet.ServletContext
import org.amcworld.springcrm.install.diffset.StartupDiffSet
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.springframework.context.ApplicationContext


/**
 * The class {@code InstallService} implements various methods to handle
 * installer issues, such as enabling or disabling the installer for security
 * reasons or obtaining the available base data packages.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class InstallService {

    //-- Constants ------------------------------

    /**
     * The directory containing the packages with base data which are loaded
     * into the database during installation.
     */
    private static final String BASE_PACKAGE_DIR = '/WEB-INF/data/install'

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


    //-- Instance variables ---------------------

    DataFileService dataFileService
    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    /**
     * Applies all difference sets from the current version to the given
     * database version.
     *
     * @param connection    the SQL connection of the database where to apply
     *                      the difference sets
     * @param upToVersion   the number of the last difference set to include
     * @since               1.4
     */
    void applyAllDiffSets(Connection connection, int upToVersion) {
        ConfigHolder configHolder = ConfigHolder.instance
        String lang = configHolder['baseDataLocale']?.toString() ?: 'de-DE'
        int fromVersion = configHolder['dbVersion']?.toType(Integer) ?: 0i
        for (int i = fromVersion + 1; i <= upToVersion; i++) {
            applyDiffSet connection, i, lang
        }
        configHolder.setConfig 'dbVersion', upToVersion.toString()
    }

    /**
     * Applies the difference set with the given version and language.  The
     * method first looks for a bean definition of the form "startupDiffSet#",
     * where # is the given version number.  If found, it is instantiated and
     * executed.  Otherwise, it looks for a SQL file to execute.
     *
     * @param connection    the SQL connection of the database where to apply
     *                      the difference set
     * @param version       the given number of the difference set
     * @param lang          the given language of the difference set in the
     *                      form {@code lang-COUNTRY}; if {@code null} the
     *                      current base data language is used
     * @since               1.4
     */
    void applyDiffSet(Connection connection, int version, String lang = null) {
        ApplicationContext ctx = grailsApplication.mainContext
        String name = "startupDiffSet${version}"
        if (ctx.containsBean(name)) {
            StartupDiffSet diffSet = ctx.getBean(name)
            diffSet.execute()
            return
        }

        if (!lang) {
            lang = (ConfigHolder.instance['baseDataLocale'] as String) ?: 'de-DE'
        }
        executeSqlFile connection, loadDiffSet(version, lang)
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
     * Enables the installer by creating the installer enable file
     * {@code ENABLE_INSTALLER} in the installer directory as specified in the
     * configuration file in key {@code springcrm.dir.installer}.
     */
    void enableInstaller() {
        log.info enableFile
        enableFile.createNewFile()
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
        SCH.servletContext.getResourcePaths(BASE_PACKAGE_DIR).each {
                def m = (it =~ /^.*\/base-data-([-\w]+)\.sql$/)
                if (m.matches()) {
                    files.add(m[0][1])
                }
            }
        files
    }

    /**
     * Installs the base data package with the given key in the database with
     * the stated connection.
     *
     * @param connection    the SQL connection where to install the base data
     *                      package
     * @param key           the given package key
     * @see                 #loadPackage(String)
     * @since               1.4
     */
    void installBaseDataPackage(Connection connection, String key) {
        executeSqlFile connection, loadBaseDataPackage(key)
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
        mod == 0 ? false
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
        SCH.servletContext.getResourceAsStream "${BASE_PACKAGE_DIR}/base-data-${key}.sql"
    }

    /**
     * Loads the difference set for the given version and language.  The method
     * first looks for a difference set with the given language.  If not found,
     * it falls back to a general difference set for the given version.
     *
     * @param version   the given version of the difference set
     * @param lang      the given language of the difference set in the form
     *                  {@code lang-COUNTRY}
     * @return          an input stream to the difference set; {@code null} if
     *                  no such difference set exists
     * @since           1.4
     */
    InputStream loadDiffSet(int version, String lang) {
        String name1 = "${BASE_PACKAGE_DIR}/db-diff-set-${version}-${lang}.sql"
        InputStream is = SCH.servletContext.getResourceAsStream(name1)
        if (is == null) {
            String name2 = "${BASE_PACKAGE_DIR}/db-diff-set-${version}.sql"
            is = SCH.servletContext.getResourceAsStream(name2)
            if (is == null && log.errorEnabled) {
                log.error "Can find neither diff set ${name1} nor ${name2}."
            }
        }
        is
    }

    /**
     * Performs all needed data migrations.
     *
     * @param connection    the SQL connection where the SQL commands should be
     *                      executed
     * @since               1.4
     */
    void migrateData(Connection connection) {
        migratePurchaseInvoiceDocuments connection
    }


    //-- Non-public methods ---------------------

    /**
     * Executes all SQL commands in the given input stream.
     *
     * @param connection    the SQL connection where the SQL commands should be
     *                      executed
     * @param is            an input stream containing the SQL commands to
     *                      execute
     * @since               1.4
     */
    protected void executeSqlFile(Connection connection, InputStream is) {
        Sql sql = new Sql(connection)
        sql.withTransaction {
            is.newReader('utf-8').eachLine {
                if (!(it =~ /^\s*$/) && !(it =~ /^\s*--/)) {
                    sql.execute it
                }
            }
        }
    }

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
        new File(dir, ENABLE_FILE_NAME)
    }

    /**
     * Migrates all documents stored in purchase invoice records to
     * {@code DataFile} records.
     *
     * @param connection    the SQL connection where the SQL commands should be
     *                      executed
     * @since               1.4
     */
    protected void migratePurchaseInvoiceDocuments(Connection connection) {
        ConfigHolder configHolder = ConfigHolder.instance
        String stage = configHolder['purchaseInvoiceMigrationStage']?.toString()
        if (stage == '1') {
            log.info 'Performing migration of purchase invoice documents…'

            File oldBaseDir = dataFileService.getBaseDir('purchase-invoice')
            File newBaseDir = dataFileService.getBaseDir(DataFileType.purchaseInvoice)
            Sql sql = new Sql(connection)
            sql.withTransaction {
                sql.eachRow('select id, document_file from purchase_invoice where document_file IS NOT NULL') {
                    File f = new File(oldBaseDir, it.document_file)
                    if (f.exists()) {
                        DataFile df = new DataFile(f)
                        df.save failOnError: true
                        PurchaseInvoice p = PurchaseInvoice.get(it.id)
                        p.documentFile = df
                        p.save failOnError: true
                        f.renameTo new File(newBaseDir, df.storageName)
                    }
                }
            }
            oldBaseDir.delete()

            log.info 'Migration of purchase invoice documents finished.'
            configHolder.setConfig 'purchaseInvoiceMigrationStage', '2'
        }
    }
}
