import grails.util.BuildSettings
import grails.util.Environment

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

root(ERROR, ['STDOUT'])

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}

if (Environment.current == Environment.PRODUCTION) {
    appender("FULL_STACKTRACE", RollingFileAppender) {
        file = "${springcrm.dir.log}/stacktrace.log"
        maxBackupIndex = 10
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
}

logger 'grails.app.services.org.amcworld.springcrm.InstallService', INFO
logger 'org.grails.plugins.coffee.compiler', INFO

logger 'grails.app.conf.BootStrap', DEBUG
// logger 'grails.app.controllers.org.amcworld.springcrm.TicketController', DEBUG
// logger 'grails.artefact.Interceptor', DEBUG, ['STDOUT'], false   // show interceptor order
// logger 'org.amcworld.springcrm.google.GoogleContactSync', DEBUG
// logger 'org.amcworld.springcrm.google.GoogleContactSyncTask', DEBUG
// logger 'org.amcworld.springcrm.google.AbstractGoogleSync', DEBUG

if (Environment.isDevelopmentMode()) {
    logger 'org.amcworld.springcrm.xml.InvoicingTransactionXML', DEBUG
}
