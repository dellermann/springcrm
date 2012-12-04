/*
 * UserService.groovy
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


package org.amcworld.springcrm

import org.apache.commons.lang.LocaleUtils


/**
 * The class {@code UserService} contains methods for working with users and
 * user settings.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 * @since   1.3
 */
class UserService {

    //-- Constants ------------------------------

    private static final String [] AVAILABLE_LANGUAGES = ['de', 'en']


    //-- Public methods -------------------------

    /**
     * Gets a list of languages which are fully supported by this application.
     *
     * @return  the list of supported languages; the returned list is
     *          immutable
     */
    List<String> getAvailableLanguages() {
        return Collections.unmodifiableList(AVAILABLE_LANGUAGES as List)
    }

    /**
     * Gets a list of locales which are fully supported by this application.
     *
     * @return  the list of supported locales
     */
    List<Locale> getAvailableLocales() {
        def availLangs = availableLanguages
        List<Locale> res = []
        for (Locale l in Locale.availableLocales) {
            if (l.country && availLangs.contains(l.language)) {
                res << l
            }
        }
        return res
    }
}
