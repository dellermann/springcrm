/*
 * BuildConfig.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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


grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.war.file = "target/${appName}.war"
grails.war.resources = { stagingDir ->
    delete(dir: "${stagingDir}/test-data")
}
grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {

    // inherit Grails' default dependencies
    inherits('global') {
        // uncomment to disable ehcache
        // excludes 'ehcache'
        excludes 'xml-apis', 'xml-apis-ext', 'xml-resolver', 'commons-digester'
    }

    log 'warn' // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        mavenCentral()
        mavenRepo 'http://mavenrepo.google-api-java-client.googlecode.com/hg/'
        mavenRepo 'http://maven.springframework.org/milestone/'
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {

        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        compile(
            'com.google.apis:google-api-services-calendar:v3-rev7-1.6.0-beta',
            'commons-fileupload:commons-fileupload:1.2.2',
            'commons-io:commons-io:2.1',
            'jmimemagic:jmimemagic:0.1.2'
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
            'mysql:mysql-connector-java:5.1.21',
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
            'org.seleniumhq.selenium:selenium-java:2.35.0'
        )
    }

    plugins {
        compile(':cloud-foundry:1.2.3') {
            export = false
        }
        compile(
            ':codenarc:0.19',
            ':coffeescript-compiler:0.9.4',
            ':dbunit-operator:1.7',
            ':fields:1.3',
            ':hibernate:3.6.10.M3',
            ':less-resources:1.3.3.2',
            ':mail:1.0.1',
            ':markdown:1.1.1',
            ':quartz:1.0-RC13',
            ':scaffolding:1.0.0',
            ':searchable:0.6.5'
        )
        build(
            ':standalone:1.1.1',
            ':tomcat:7.0.40.1'
        )
        runtime(
            ':database-migration:1.3.6',
            ':jquery:1.8.3',
            ':jquery-json:2.2.2',
            ':jquery-ui:1.8.24',
            ':resources:1.2.1'
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
