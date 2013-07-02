/*
 * Config.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


/*
 * Locations to search for configuration files that get merged into the main
 * configuration.  Configuration files can either be Java properties files or
 * ConfigSlurper scripts.  Furthermore, the user can set the environment
 * property "springcrm.config.location" to specify a configuration file.
 */
grails.config.locations = [
    "classpath:${appName}-config.properties",
    "classpath:${appName}-config.groovy",
    "file:${userHome}/.${appName}/config.properties"
]
if (System.properties["${appName}.config.location"]) {
    grails.config.locations << 'file:' + System.properties["${appName}.config.location"]
}

/*
 * Change this to alter the default package name and Maven publishing
 * destination.
 */
grails.project.groupId = 'org.amcworld.springcrm'

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

/* URL Mapping Cache Max Size, defaults to 5000 */
//grails.urlmapping.cache.maxsize = 1000

/* The default codec used to encode data with ${} */
grails.views.default.codec = 'none' // none, html, base64
grails.views.gsp.encoding = 'UTF-8'
grails.converters.encoding = 'UTF-8'

grails.web.url.converter = 'hyphenated'

/* Enable Sitemesh preprocessing of GSP pages */
grails.views.gsp.sitemesh.preprocess = true

/* Scaffolding templates configuration */
grails.scaffolding.templates.domainSuffix = 'Instance'

/* Set to false to use the new Grails 1.2 JSONBuilder in the render method */
grails.json.legacy.builder = false

/* Enabled native2ascii conversion of i18n properties files */
grails.enable.native2ascii = true

/*
 * Whether to install the java.util.logging bridge for sl4j. Disable for
 * AppEngine!
 */
grails.logging.jul.usebridge = false

/* Packages to include in Spring bean scanning */
grails.spring.bean.packages = []

/* Request parameters to mask when logging exceptions */
grails.exceptionresolver.params.exclude = ['password']

/* Settings for database migration */
grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']
grails.plugin.databasemigration.updateOnStart = true
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']

/* mail configuration */
grails {
    mail {
        host = '192.168.0.1'
        port = 25
        props = ['mail.smtp.auth': 'false']
    }
}

/* CoffeeScript compiler settings */
'coffeescript-compiler' {
    appSource {
        coffeeSourcePath = 'src/coffee'
        jsOutputPath = 'web-app/js/app'
    }
    pluginConfig {
        minifyInEnvironment = ['production', 'live', 'cloud', 'standalone']
        purgeJS = true
    }
}

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
    dir {
        data = "${springcrm.dir.base}/data"
        documents = "${springcrm.dir.base}/documents"
        installer = "${springcrm.dir.base}/install"
        print = "${springcrm.dir.base}/print"
    }
    cacheDocs = true
    lruList.numEntries = 10
}

/* environment specific settings */
environments {

    /* development environment */
    development {}

    /* test environment */
    test {
        grails.plugin.databasemigration.dropOnStart = true
        springcrm.dir.base = System.getProperty('java.io.tmpdir') + '/springcrm'
        springcrm.dir.data = "${springcrm.dir.base}/data"
        springcrm.dir.documents = "${springcrm.dir.base}/documents"
        springcrm.dir.installer = "${springcrm.dir.base}/install"
    }

    /* production (deployment) environment */
    production {}

    /* live enviroment on the AMC World server */
    live {}

    /* CloudFoundry environment */
    cloud {}

    /* standalone environment for demonstration purposes */
    standalone {}
}

/* logger configuration */
log4j = {
    appenders {
        String logDir = grails.util.Environment.warDeployed ? System.getProperty('catalina.home') + '/logs' : 'target'
        file name: 'stacktrace',
        file: "${logDir}/stacktrace.log",
        layout: pattern(conversionPattern: "'%d [%t] %-5p %c{2} %x - %m%n'")
//        console name: 'stdout', layout: pattern(conversionPattern: '%c{2} %m%n')
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

    debug(
//        'grails.app.controllers.org.amcworld.springcrm.InvoiceController',
//        'grails.app.controllers.org.amcworld.springcrm.PurchaseInvoiceController',
//        'grails.app.controllers.org.amcworld.springcrm.QuoteController',
//        'grails.app.controllers.org.amcworld.springcrm.SalesOrderController',
        'grails.app.services.org.amcworld.springcrm.FopService',
//        'grails.app.jobs.GoogleContactSyncJob',
//        'grails.app.services.org.amcworld.springcrm.GoogleDataService',
//        'grails.app.services.org.amcworld.springcrm.GoogleDataContactService',
//        'org.amcworld.springcrm.google.GoogleContactSync',
//        'org.amcworld.springcrm.google.GoogleSync',
//        'org.amcworld.springcrm.elfinder.Connector',
//        'org.amcworld.springcrm.elfinder.Request',
//        'org.amcworld.springcrm.elfinder.command',
//        'org.amcworld.springcrm.elfinder.fs',
    )

    info 'org.grails.plugins.coffee.compiler'

    environments {
        test {
            debug 'grails.app.controllers.org.amcworld.springcrm'
        }
    }
}
