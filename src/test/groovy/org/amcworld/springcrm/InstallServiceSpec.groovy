/*
 * InstallServiceSpec.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification


class InstallServiceSpec extends Specification
    implements ServiceUnitTest<InstallService>
{

    //-- Feature methods ------------------------

    // TODO applyAllDiffSets
    // TODO applyDiffSet

    def 'Can enable and disable installer'() {
        given: 'a configuration for installation data'
        File dir = File.createTempDir('springcrm-test-', '')
        grailsApplication.config.springcrm.dir.installer = dir.absolutePath

        when: 'I enable the installer'
        service.enableInstaller()

        then: 'a marker file has been created'
        new File(dir, 'ENABLE_INSTALLER').exists()
        !service.installerDisabled

        when: 'I disable the installer'
        service.disableInstaller()

        then: 'the marker file has been deleted'
        !new File(dir, 'ENABLE_INSTALLER').exists()
        service.installerDisabled

        cleanup:
        dir.delete()
    }

    def 'Can obtain base data packages'() {
        when: 'I obtain the base data packages'
        List<String> packages = service.baseDataPackages

        then: 'I get a complete list of base data packages'
        3 == packages.size()

        and: 'the packages are in correct order'
        'de-DE' == packages[0]
        'de-AT' == packages[1]
        'de-CH' == packages[2]

        and: 'all packages can be loaded'
        for (String p : packages) {
            service.loadBaseDataPackage(p) != null
        }
    }

    // TODO installBaseDataPackage

    def 'Can check if installer enable file is expired'() {
        given: 'a configuration for installation data'
        File dir = File.createTempDir('springcrm-test-', '')
        grailsApplication.config.springcrm.dir.installer = dir.absolutePath

        when: 'I enable the installer'
        service.enableInstaller()

        then: 'the enable file is not expired yet'
        !service.enableFileExpired

        when: 'I change the timestamp of the enable file'
        new File(dir, 'ENABLE_INSTALLER').lastModified = System.currentTimeMillis() - 15 * 60000 + 1000

        then: 'the enable file is not expired yet'
        !service.enableFileExpired

        when: 'I change the timestamp of the enable file'
        new File(dir, 'ENABLE_INSTALLER').lastModified = System.currentTimeMillis() - 15 * 60000 - 1000

        then: 'the enable file is expired yet'
        service.enableFileExpired
    }

    def 'Can obtain diff sets'() {
        expect:
        service.loadDiffSet(1, 'de-DE') != null
        service.loadDiffSet(1, 'de-AT') != null
        service.loadDiffSet(1, 'de-CH') != null
        service.loadDiffSet(1, '') == null
        service.loadDiffSet(2, 'de-DE') == null
        service.loadDiffSet(2, 'de-AT') == null
        service.loadDiffSet(2, 'de-CH') == null
        service.loadDiffSet(2, '') == null
        service.loadDiffSet(3, 'de-DE') != null
        service.loadDiffSet(3, 'de-AT') != null
        service.loadDiffSet(3, 'de-CH') != null
        service.loadDiffSet(3, '') != null
    }

    // TODO migrateData
}
