/*
 * logback.groovy
 *
 * Copyright (c) 2011-2022, Daniel Ellermann
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


import static ch.qos.logback.classic.Level.*

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import grails.util.BuildSettings
import grails.util.Environment


// See http://logback.qos.ch/manual/groovy.html for details on configuration

appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%level %logger - %msg%n'
    }
}

root ERROR, ['STDOUT']

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir) {
    appender('FULL_STACKTRACE', FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = '%level %logger - %msg%n'
        }
    }
    logger('StackTrace', ERROR, ['FULL_STACKTRACE'], false)
}

//if (Environment.current == Environment.PRODUCTION) {
//    appender("FULL_STACKTRACE", RollingFileAppender) {
//        file = "${springcrm.dir.log}/stacktrace.log"
//        maxBackupIndex = 10
//        encoder(PatternLayoutEncoder) {
//            pattern = "%level %logger - %msg%n"
//        }
//    }
//}

logger 'grails.app.services.org.amcworld.springcrm.InstallService', INFO
logger 'org.grails.plugins.coffee.compiler', INFO

//logger 'springcrm.Application', DEBUG
//logger 'springcrm.LauncherFrame', DEBUG
//logger 'grails.app.conf.org.amcworld.springcrm.BootStrap', INFO
//logger 'grails.app.controllers.org.amcworld.springcrm.TicketController', DEBUG
//logger 'grails.artefact.Interceptor', DEBUG, ['STDOUT'], false   // show interceptor order
//logger 'org.amcworld.springcrm.google.GoogleContactSync', DEBUG
//logger 'org.amcworld.springcrm.google.GoogleContactSyncTask', DEBUG
//logger 'org.amcworld.springcrm.google.AbstractGoogleSync', DEBUG
//logger 'org.springframework.boot', DEBUG
//logger 'org.springframework.jdbc', DEBUG
//logger 'org.springframework.web.servlet.mvc', DEBUG
//logger 'liquibase', DEBUG
//logger 'org.grails.datastore', DEBUG
//logger 'org.grails.orm', DEBUG
//logger 'org.grails.plugins.databasemigration.liquibase', DEBUG

if (Environment.isDevelopmentMode()) {
    logger 'org.amcworld.springcrm.xml.InvoicingTransactionXML', DEBUG
}
