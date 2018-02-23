/*
 * OverviewService.groovy
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

import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import java.util.regex.Matcher
import grails.core.GrailsApplication
import grails.plugins.VersionComparator
import org.springframework.core.io.Resource
import org.springframework.core.io.support.LocalizedResourceHelper


/**
 * The class {@code OverviewService} contains auxiliary methods for the
 * overview controller.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.0
 */
@CompileStatic
@Transactional(readOnly = true)
class OverviewService {

    //-- Constants ------------------------------

    private static final String CHANGELOG_PATH = 'classpath:public/changelog'


    //-- Fields ---------------------------------

    GrailsApplication grailsApplication
    UserSettingService userSettingService


    //-- Public methods -------------------------

    /**
     * Sets the current version in the user settings to prevent display of
     * changelog for this version.
     *
     * @param user  the currently logged in user
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Transactional
    void dontShowAgain(User user) {
        userSettingService.store(
            user, 'changelogVersion',
            grailsApplication.metadata.applicationVersion
        )
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
            Matcher m = text =~ /(?m)^\[comment]:\s*STOP$/
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
     * @param user  the currently logged in user
     * @return      {@code true} if the changelog should be displayed;
     *              {@code false} otherwise
     */
    boolean showChangelog(User user) {
        String version = userSettingService.getString(user, 'changelogVersion')
        if (!version) {
            return true
        }

        String currentVersion = grailsApplication.metadata.applicationVersion
        new VersionComparator().compare(version, currentVersion) < 0
    }
}
