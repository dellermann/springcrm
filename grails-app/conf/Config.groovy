/*
 * Config.groovy
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


/*
 * Locations to search for configuration files that get merged into the main
 * configuration.  Configuration files can either be Java properties files or
 * ConfigSlurper scripts.  Furthermore, the user can set the environment
 * property "springcrm.config.location" to specify a configuration file.
 */
grails.config.locations = [
    "classpath: ${appName}-config.properties",
    "classpath: ${appName}-config.groovy",
    "file: ${userHome}/.${appName}/config.properties"
]
if (System.properties["${appName}.config.location"]) {
    grails.config.locations << "file: " + System.properties["${appName}.config.location"]
}

/*
 * Change this to alter the default package name and Maven publishing
 * destination.
 */
grails.project.groupId = appName

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
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']

/* mail configuration */
grails {
    mail {
        host = '192.168.0.1'
        port = 25
        props = ['mail.smtp.auth': 'false']
    }
}

/* SpringCRM settings */
springcrm {
    documents.base = "${System.getProperty('user.home')}/.${appName}/documents"
    lruList.numEntries = 10
}

/* environment specific settings */
environments {

    /* development environment */
    development {
        grails.plugin.databasemigration.updateOnStart = true
    }

    /* test environment */
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

    /* production (deployment) environment */
    production {}

    /* live enviroment on the AMC World server */
    live {
        grails.plugin.databasemigration.updateOnStart = true
    }

    /* standalone environment for demonstration purposes */
    standalone {
//        grails.logging.jul.usebridge = false
    }
}

/* logger configuration */
log4j = {
    appenders {
//        console name: 'stdout', layout: pattern(conversionPattern: '%c{2} %m%n')
        environments {
            live {
                file name: 'stacktrace', file: '/var/log/tomcat-5.5/stacktrace.log'
            }
        }
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
        'grails.app.controllers.org.amcworld.springcrm.InvoiceController',
        'grails.app.controllers.org.amcworld.springcrm.PurchaseInvoiceController',
        'grails.app.controllers.org.amcworld.springcrm.QuoteController',
        'grails.app.controllers.org.amcworld.springcrm.SalesOrderController'
    )
}
