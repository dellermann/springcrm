/*
 * application.groovy
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


//== Configuration file loading =================

/*
 * Locations to search for configuration files that get merged into the main
 * configuration.  Configuration files can either be Java properties files or
 * ConfigSlurper scripts.  Furthermore, the user can set the environment
 * property "springcrm.config.location" to specify a configuration file.
 */
grails.config.locations = [
    "classpath:${appName}-config.properties",
    "classpath:${appName}-config.groovy",
    "file:${userHome}/.${appName}/config.properties",
    "file:${userHome}/.${appName}/config.groovy"
]
if (System.properties["${appName}.config.location"]) {
    grails.config.locations << 'file:' + System.properties["${appName}.config.location"]
}


//== Build settings =============================

/* Scaffolding templates configuration */
grails.scaffolding.templates.domainSuffix = 'Instance'


//== Content handling ===========================

/*
 * Enables the parsing of file extensions from URLs into the request format.
 */
grails.mime.use.accept.header = false


//== Data binding and validation ================

grails.databinding.convertEmptyStringsToNull = false


//== Views ======================================

/* GSP settings */
grails {
    views {
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}

/* Set to false to use the new Grails 1.2 JSONBuilder in the render method */
grails.json.legacy.builder = false


//== Data sources ===============================

/* environment specific settings */
environments {

    /* standalone environment quick installation */
    standalone {
        dataSource {
            url = "jdbc:h2:file:${userHome}/.${appName}/database/${appName}"
            username = appName
        }
    }
}


//== Plugins ====================================

/* Settings for database migration */
grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']
grails.plugin.databasemigration.forceAutoMigrate = true
grails.plugin.databasemigration.updateOnStart = true
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']

/* Markdown settings */
markdown {
    abbreviations = true
    definitionLists = true
    hardwraps = true
    removeHtml = true
    smartPunctuation = true
    tables = true
}


//== Miscellaneous ==============================

/* Packages to include in Spring bean scanning */
grails.spring.bean.packages = []

grails.resources.adhoc.excludes = ['**/WEB-INF/**','**/META-INF/**']


//== Application-specific settings ==============

/* SpringCRM settings */
springcrm.dir.base = "${userHome}/.${appName}"
if (System.getenv('SPRINGCRM_HOME')) {
    springcrm.dir.base = System.getenv('SPRINGCRM_HOME')
}
if (System.properties["${appName}.dir.base"]) {
    springcrm.dir.base = System.properties["${appName}.dir.base"]
}
grails.config.locations << "file:${springcrm.dir.base}/config.properties"
springcrm {
    cacheDocs = true
    dir {
        data = "${springcrm.dir.base}/data"
        documents = "${springcrm.dir.base}/documents"
        installer = "${springcrm.dir.base}/install"
        log = "${springcrm.dir.base}/log"
        print = "${springcrm.dir.base}/print"
    }
    lruList.numEntries = 10
    mail {
        from = "SpringCRM Service <noreply@springcrm.de>"
    }
}


//== Logging ====================================

/* Request parameters to mask when logging exceptions */
grails.exceptionresolver.params.exclude = ['password']

/*
 * Whether to install the java.util.logging bridge for sl4j. Disable for
 * AppEngine!
 */
grails.logging.jul.usebridge = false


//== Environment-specific settings ==============

environments {

    /* test environment */
    test {
        grails.plugin.databasemigration.dropOnStart = true
        springcrm.dir.base = System.getProperty('java.io.tmpdir') + '/springcrm'
        springcrm.dir.data = "${springcrm.dir.base}/data"
        springcrm.dir.documents = "${springcrm.dir.base}/documents"
        springcrm.dir.installer = "${springcrm.dir.base}/install"

        log4j.env = {
            debug 'grails.app.controllers.org.amcworld.springcrm'
        }
    }

    // TODO migrate to logback
    // /* standalone environment for demonstration purposes */
    // standalone {
    //     log4j.env = {
    //         appenders {
    //             rollingFile(
    //                 name: 'stacktrace', maxBackupIndex: 10,
    //                 file: "${System.properties['user.home']}/.springcrm/stacktrace.log",
    //                 layout: pattern(conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n')
    //             )
    //         }
    //         root {
    //             warn 'stacktrace'
    //         }
    //     }
    // }
}
