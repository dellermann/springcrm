/*
 * BuildConfig.groovy
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


grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.war.file = "target/${appName}.war"
grails.project.dependency.resolution = {

    // inherit Grails' default dependencies
    inherits('global') {
        // uncomment to disable ehcache
        // excludes 'ehcache'
        // excludes 'xml-apis', 'commons-logging'
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
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {

        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        /*
         * XXX These dependencies conflict in the transient dependency
         * jackson-core-asl with the cloudfoundry plugin
         *
        compile(
            'com.google.api-client:google-api-client:1.7.0-beta',
            'com.google.apis:google-api-services-calendar:v3-rev3-1.4.0-beta',
            'com.google.oauth-client:google-oauth-client:1.7.0-beta'
        )
         *
         * this works:
        compile(
            'jmimemagic:jmimemagic:0.1.2'
        )
         *
         * XXX Including FOP dependencies does not work because the xml-apis
         * are loaded twice.
         *
        compile('org.apache.xmlgraphics:fop:1.0') {
            excludes([group: 'xml-apis', name: 'xml-apis'], [group: 'xml-resolver', name: 'xml-resolver'])
        }
         *
         * this works without problems:
         *
        runtime(
            'net.sf.offo:fop-hyph:1.2'
        )
         *
         * this works, too:
        runtime(
            'mysql:mysql-connector-java:5.1.19'
        )
         *
         */
    }
}
