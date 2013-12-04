/*
 * Standalone.groovy
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


includeTargets << grailsScript('_GrailsInit')
includeTargets << new File(standalonePluginDir, 'scripts/BuildStandalone.groovy')


target(main: 'Builds a standalone version of SpringCRM.') {
    depends buildStandalone

    String targetDir = "${grailsSettings.projectTargetDir}/standalone"
    String targetDataDir = "${targetDir}/springcrm-${grailsAppVersion}"
    String zipFile = "${grailsSettings.projectTargetDir}/springcrm-${grailsAppVersion}.zip"

    event 'StatusUpdate', ["Packaging ${zipFile}"]
    ant.mkdir dir: targetDataDir
    ant.copy(toDir: targetDataDir) {
        filterSet {
            filter(token: 'VERSION', value: grailsAppVersion)
        }
        fileset(
            dir: "${basedir}/resources/standalone", includes: '*',
            excludes: '*.xcf'
        )
    }
    ant.copy(
        file: "${grailsSettings.projectTargetDir}/standalone-${grailsAppVersion}.jar",
        toDir: targetDataDir
    )
    ant.zip(destFile: zipFile, basedir: targetDir, level: 9)
    ant.delete(dir: targetDir, quiet: true)
    event 'StatusUpdate', ["Packaged standalone file ${zipFile}"]
}

setDefaultTarget(main)
