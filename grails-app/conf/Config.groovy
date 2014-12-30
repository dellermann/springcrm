/*
 * Config.groovy
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

/*
 * Change this to alter the default package name and Maven publishing
 * destination.
 */
grails.project.groupId = 'org.amcworld.springcrm'

/* Enabled native2ascii conversion of i18n properties files */
grails.enable.native2ascii = true

/* Scaffolding templates configuration */
grails.scaffolding.templates.domainSuffix = 'Instance'


//== Content handling ===========================

/*
 * Enables the parsing of file extensions from URLs into the request format.
 */
grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [
    all: '*/*',
    atom: 'application/atom+xml',
    css: 'text/css',
    csv: 'text/csv',
    form: 'application/x-www-form-urlencoded',
    html: ['text/html', 'application/xhtml+xml'],
    js: 'text/javascript',
    json: ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss: 'application/rss+xml',
    text: 'text/plain',
    xml: ['text/xml', 'application/xml']
]


//== Data binding and validation ================

grails.databinding.convertEmptyStringsToNull = false


//== URL handling ===============================

/* URL Mapping Cache Max Size, defaults to 5000 */
//grails.urlmapping.cache.maxsize = 1000

grails.web.url.converter = 'hyphenated'


//== Views ======================================

grails.assets.less.compiler = 'less4j'
grails.assets.excludes = [
    'bootstrap/**/*.less', 'font-awesome/**/*.less',
    'bootstrap/*.js', 'js-calc/**/*.less'
]

/* Enable Sitemesh preprocessing of GSP pages */
grails.views.gsp.sitemesh.preprocess = true

/* GSP settings */
grails.converters.encoding = 'UTF-8'
grails.views.default.codec = 'html' // none, html, base64
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside null
                scriptlet = 'none' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}

/* Set to false to use the new Grails 1.2 JSONBuilder in the render method */
grails.json.legacy.builder = false


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

/* loggers */
log4j.main = {
    appenders {
        console(
            name: 'stdout', layout: pattern(conversionPattern: '%c{2} %m%n')
        )
    }

    error(
        'net.sf.ehcache.hibernate',
        'org.codehaus.groovy.grails.commons',            // core/classloading
        'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
        'org.codehaus.groovy.grails.plugins',            // plugins
        'org.codehaus.groovy.grails.web.mapping',        // URL mapping
        'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
        'org.codehaus.groovy.grails.web.pages',          // GSP
        'org.codehaus.groovy.grails.web.servlet',        // controllers
        'org.codehaus.groovy.grails.web.sitemesh',       // layouts
        'org.hibernate',
        'org.springframework'
    )

    warn(
        'org.mortbay.log'
    )

    info(
        'grails.app.services.org.amcworld.springcrm.InstallService',
        'org.grails.plugins.coffee.compiler'
    )

    debug(
//        'grails.app.controllers.org.amcworld.springcrm.InvoiceController',
//        'grails.app.controllers.org.amcworld.springcrm.PurchaseInvoiceController',
//        'grails.app.controllers.org.amcworld.springcrm.QuoteController',
//        'grails.app.controllers.org.amcworld.springcrm.SalesOrderController',
        'grails.app.controllers.org.amcworld.springcrm.TicketController',
//        'grails.app.jobs.GoogleContactSyncJob',
//        'grails.app.services.org.amcworld.springcrm.GoogleDataService',
//        'grails.app.services.org.amcworld.springcrm.GoogleDataContactService',
//        'org.amcworld.springcrm.google.GoogleContactSync',
//        'org.amcworld.springcrm.google.GoogleSync',
    )

}


//== Environment-specific settings ==============

environments {

    /* development environment */
    development {
        log4j.env = {
            debug 'org.amcworld.springcrm.InvoicingTransactionXML'
        }
    }

    /* production (deployment) environment */
    production {
        log4j.env = {
            appenders {
                rollingFile(
                    name: 'stacktrace', maxBackupIndex: 10,
                    file: "${springcrm.dir.log}/stacktrace.log",
                    layout: pattern(conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n')
                )
            }
            root {
                warn 'stacktrace'
            }
        }
    }

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

    /* standalone environment for demonstration purposes */
    standalone {
        log4j.env = {
            appenders {
                rollingFile(
                    name: 'stacktrace', maxBackupIndex: 10,
                    file: "${System.properties['user.home']}/.springcrm/stacktrace.log",
                    layout: pattern(conversionPattern: '%d [%t] %-5p %c{2} %x - %m%n')
                )
            }
            root {
                warn 'stacktrace'
            }
        }
    }
}
