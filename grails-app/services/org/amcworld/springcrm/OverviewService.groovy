/*
 * OverviewService.groovy
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

import java.util.regex.Matcher
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.VersionComparator
import org.springframework.core.io.Resource
import org.springframework.core.io.support.LocalizedResourceHelper


/**
 * The class {@code OverviewService} contains auxiliary methods for the
 * overview controller.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   2.0
 */
class OverviewService {

    //-- Constants ------------------------------

    private static final String CHANGELOG_PATH = 'WEB-INF/data/changelog'


    //-- Static variables -----------------------

    static transactional = false


    //-- Fields ---------------------------------

    GrailsApplication grailsApplication


    //-- Public methods -------------------------

    /**
     * Sets the current version in the user settings to prevent display of
     * changelog for this version.
     *
     * @param credential    the credential representing the currently logged in
     *                      user
     */
    void dontShowAgain(Credential credential) {
        String currentVersion = grailsApplication.metadata['app.version']
        credential.settings['changelogVersion'] = currentVersion
    }

    /**
     * Gets the part of the localized changelog file in
     * {@code WEB-INF/data/changelog.md} until the marker
     * {@code [comment]: STOP} is found.
     *
     * @param locale                    the given locale
     * @return                          the part of the Markdown changelog
     * @throws FileNotFoundException    if the localized changelog file could
     *                                  not be found
     */
    String getChangelog(Locale locale) {
        LocalizedResourceHelper helper =
            new LocalizedResourceHelper(grailsApplication.mainContext)
        Resource res =
            helper.findLocalizedResource(CHANGELOG_PATH, '.md', locale)
        String text = res.inputStream?.getText('UTF-8')
        if (text) {
            Matcher m = text =~ /(?m)^\[comment\]:\s*STOP$/
            if (m) {
                text = text.substring(0, m.start())
            }
        }

        text ?: ''
    }

    /**
     * Checks whether or not the changelog should be displayed for the given
     * user.
     *
     * @param credential    the credential representing the currently logged in
     *                      user
     * @return              {@code true} if the changelog should be displayed;
     *                      {@code false} otherwise
     */
    boolean showChangelog(Credential credential) {
        String version = credential.settings['changelogVersion']?.toString()
        if (!version) {
            return true
        }

        String currentVersion = grailsApplication.metadata['app.version']
        new VersionComparator().compare(version, currentVersion) < 0
    }
}
