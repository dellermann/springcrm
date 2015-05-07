/*
 * BuildConfig.groovy
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


def gebVersion = '0.9.3'

grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
//grails.project.work.dir = 'target/work'
grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.war.resources = { stagingDir ->
    delete dir: "${stagingDir}/test-data"
}

if (grailsSettings.grailsEnv != 'test') {
    def forkConfig = [
        maxMemory: 1024, minMemory: 550, debug: false, maxPerm: 192
    ]
    def jvmConfig = [
        jvmArgs: [
            '-XX:+PrintGC', '-XX:+PrintGCDetails', '-XX:+PrintGCTimeStamps',
            '-XX:PermSize=128M'
        ]
    ]
    grails.project.fork = [
       console: forkConfig,             // settings for the Swing console JVM
       run: forkConfig, // + jvmConfig,     // settings for the run-app JVM
       test: forkConfig,                     // settings for the test-app JVM
       war: forkConfig                  // settings for the run-war JVM
    ]
}

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

    // inherit Grails' default dependencies
    inherits('global') {
        // uncomment to disable ehcache
        // excludes 'ehcache'
        excludes 'xml-apis', 'xml-apis-ext', 'xml-resolver', 'commons-digester', 'junit'
    }

    log 'warn' // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        inherits true

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
        mavenRepo 'http://mavenrepo.google-api-java-client.googlecode.com/hg/'
        mavenRepo 'http://maven.springframework.org/milestone/'
        mavenRepo 'http://repo.grails.org/grails/core'
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {

        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        compile(
            'com.google.apis:google-api-services-calendar:v3-rev85-1.18.0-rc',
            'com.google.http-client:google-http-client-jackson2:1.18.0-rc',
            'commons-fileupload:commons-fileupload:1.3.1',
            'commons-io:commons-io:2.4',
            'jmimemagic:jmimemagic:0.1.2',
            //'org.apache.xmlgraphics:fop:1.0',
            'org.apache.commons:commons-vfs2:2.0',
            'org.apache.httpcomponents:httpclient:4.3.2'
        )

        /*
         * XXX Including FOP dependencies does not work because the xml-apis
         * are loaded twice.
         *
        compile('org.apache.xmlgraphics:fop:1.0') {
//            transitive = false
//            excludes 'xml-apis', 'xml-resolver'
//            excludes([group: 'xml-apis', name: 'xml-apis'], [group: 'xml-resolver', name: 'xml-resolver'])
        }
        */
        runtime(
            'com.google.guava:guava:17.0',
            'mysql:mysql-connector-java:5.1.27',
            'net.sf.offo:fop-hyph:1.2'
        )

        /*
         * XXX Same dependency problem as above at FOP
         *
        runtime('net.sf.barcode4j:barcode4j-fop-ext-complete:2.0') {
            transitive = false
        }
        */

        test(
            'junit:junit:4.11',
            "org.gebish:geb-spock:${gebVersion}",
            'org.seleniumhq.selenium:selenium-java:2.43.1'
        )
    }

    plugins {
        compile(
            ':asset-pipeline:1.9.9',
//            ':codenarc:0.21',
            ':coffee-asset-pipeline:1.9.0',
            ':dbunit-operator:1.7',
            ':fields:1.3',
            ':handlebars-asset-pipeline:1.3.0.3',
            ':i18n-asset-pipeline:0.9.2',
            ':less-asset-pipeline:1.10.0',
            ':mail:1.0.6',
            ':markdown:1.1.1',
            ':scaffolding:2.1.2',
            ':searchable:0.6.9'
        )
        build(
            ':tomcat:7.0.54'
        )
        runtime(
            ':database-migration:1.4.0',
            ':hibernate:3.6.10.17'
        )
        test(
            ":geb:${gebVersion}"
        )
    }
}

/* CodeNarc configuration */
codenarc.properties = {
    CatchException.enabled = false
    DoubleNegative.enabled = false
    EmptyMethod.enabled = false
    GrailsPublicControllerMethod.enabled = false
    GrailsServletContextReference.enabled = false
    ReturnNullFromCatchBlock.enabled = false
}
