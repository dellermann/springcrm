/*
 * _Events.groovy
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


import java.text.SimpleDateFormat


eventCompileStart = { kind ->
    def buildNumber = metadata.'app.buildNumber'
    if (!buildNumber) {
        buildNumber = 1L
    } else {
        buildNumber = Long.valueOf(buildNumber) + 1
    }
    metadata.'app.buildNumber' = buildNumber.toString()

    def formatter = new SimpleDateFormat('''yyyy-MM-dd'T'HH:mm:ssZ''')
    metadata.'app.buildDate' = formatter.format(new Date())
    metadata.'app.buildProfile' = grailsEnv

    metadata.persist()
    println "| Set build number #${buildNumber}."
}

eventWebXmlStart = { webXmlFile ->
    ant.echo message: 'Change display-name for web.xml'
    def tmpWebXmlFile = new File(projectWorkDir, webXmlFile)
    ant.replace(
        file: tmpWebXmlFile, token: "@grails.app.name.version@",
        value: "SpringCRM ${grailsAppVersion} (Build ${metadata.'app.buildNumber'})"
    )
}

//eventAllTestsStart = {
//    if (!functionalTests.contains("functional")) {
//        functionalTests << "functional"
//    }
//}

