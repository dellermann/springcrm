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


package org.amcworld.springcrm


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


//== Miscellaneous ==============================

/* Packages to include in Spring bean scanning */
grails.spring.bean.packages = []

grails.resources.adhoc.excludes = ['**/WEB-INF/**','**/META-INF/**']


//== Application-specific settings ==============

/* SpringCRM settings -> remaining settings see application.yml */
springcrm.dir.base = "${userHome}/.${appName}"
if (System.getenv('SPRINGCRM_HOME')) {
    springcrm.dir.base = System.getenv('SPRINGCRM_HOME')
}
if (System.properties["${appName}.dir.base"]) {
    springcrm.dir.base = System.properties["${appName}.dir.base"]
}


//== Logging ====================================

/* Request parameters to mask when logging exceptions */
grails.exceptionresolver.params.exclude = ['password']

/*
 * Whether to install the java.util.logging bridge for sl4j. Disable for
 * AppEngine!
 */
grails.logging.jul.usebridge = false
