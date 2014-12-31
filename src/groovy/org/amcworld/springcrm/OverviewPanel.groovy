/*
 * OverviewPanel.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import grails.util.Holders
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import org.springframework.context.ApplicationContext


/**
 * The class {@code OverviewPanel} stores information about a panel which can
 * be used on the overview page.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class OverviewPanel {

    //-- Instance variables ---------------------

    String action
    String controller
    String defDescription
    String defTitle
    String style

    protected Map<Locale, String> localizedDescriptions = [: ]
    protected Map<Locale, String> localizedTitles = [: ]
    protected String url


    //-- Properties -----------------------------

    /**
     * Gets the URL which is called to obtain the content of the panel.
     *
     * @return  the content loading URL
     */
    String getUrl() {
        if (!url) {
            ApplicationContext ctx = Holders.applicationContext
            ApplicationTagLib g = ctx.getBean(ApplicationTagLib.class.name)
            url = g.createLink(controller: controller, action: action)
        }

        url
    }


    //-- Public methods -------------------------

    /**
     * Adds a description for the given locale.
     *
     * @param locale        the given locale
     * @param description   the description
     */
    void addLocalizedDescription(Locale locale, String description) {
        localizedDescriptions[locale] = description
    }

    /**
     * Adds a description for the given locale.
     *
     * @param locale        the locale ID as string in the form
     *                      {@code language[-country[-variant]]}
     * @param description   the description
     */
    void addLocalizedDescription(String locale, String description) {
        addLocalizedDescription locale.tokenize('-') as Locale, description
    }

    /**
     * Adds a title for the given locale.
     *
     * @param locale    the given locale
     * @param title     the title
     */
    void addLocalizedTitle(Locale locale, String title) {
        localizedTitles[locale] = title
    }

    /**
     * Adds a title for the given locale.
     *
     * @param locale    the locale ID as string in the form
     *                  {@code language[-country[-variant]]}
     * @param title     the title
     */
    void addLocalizedTitle(String locale, String title) {
        addLocalizedTitle locale.tokenize('-') as Locale, title
    }

    /**
     * Gets the description for the given locale or in the default language.
     *
     * @param locale    the given locale; if {@code null} the default locale
     *                  should be used
     * @return          the description for that locale
     */
    String getDescription(Locale locale = Locale.default) {
        String description = localizedDescriptions[locale]
        if (!description) {
            locale = new Locale(locale.language, locale.country)
        }

        description = localizedDescriptions[locale]
        if (!description) {
            locale = new Locale(locale.language)
        }

        localizedDescriptions[locale] ?: defDescription
    }

    /**
     * Gets the title for the given locale or in the default language.
     *
     * @param locale    the given locale; if {@code null} the default locale
     *                  should be used
     * @return          the title for that locale
     */
    String getTitle(Locale locale = Locale.default) {
        String title = localizedTitles[locale]
        if (!title) {
            locale = new Locale(locale.language, locale.country)
        }

        title = localizedTitles[locale]
        if (!title) {
            locale = new Locale(locale.language)
        }

        localizedTitles[locale] ?: defTitle
    }
}
