/*
 * OverviewPanel.groovy
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

import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GA


/**
 * The class {@code OverviewPanel} stores information about a panel which can
 * be used on the overview page.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
class OverviewPanel {

    //-- Instance variables ---------------------

    String controller
    String action
    String url
    String defTitle
    String style
    Map<Locale, String> localizedTitles


    //-- Constructors ---------------------------

    /**
     * Creates a new overview page panel instance with the given data.
     *
     * @param controller    the name of the controller which is called to
     *                      generate the content of the panel
     * @param action        the name of the action which is called to generate
     *                      the content of the panel
     * @param defTitle      the title in the default language
     * @param style         any CSS style attributes which are applied to the
     *                      panel
     */
    OverviewPanel(String controller, String action, String defTitle,
                  String style) {
        this.controller = controller
        this.action = action
        this.defTitle = defTitle
        this.style = style
        localizedTitles = new HashMap<Locale, String>()
    }


    //-- Properties -----------------------------

    /**
     * Gets the name of the controller which is called to generate the content
     * of the panel.
     *
     * @return  the name of the controller
     */
    String getController() {
        return controller
    }

    /**
     * Gets the name of the action which is called to generate the content of
     * the panel.
     *
     * @return  the name of the action
     */
    String getAction() {
        return action
    }

    /**
     * Gets any CSS style attributes which are applied to the panel.
     *
     * @return  the CSS styles
     */
    String getStyle() {
        return style
    }

    /**
     * Gets the URL which is called to obtain the content of the panel.
     *
     * @return  the content loading URL
     */
    String getUrl() {
        if (!url) {
            def ctx = SCH.servletContext.getAttribute(GA.APPLICATION_CONTEXT)
            def g = ctx.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
            url = g.createLink(controller: controller, action: action)
        }
        return url
    }


    //-- Public methods -------------------------

    /**
     * Adds a title in the given language.
     *
     * @param locale    the locale representing the given language
     * @param title     the title
     */
    void addLocalizedTitle(Locale locale, String title) {
        localizedTitles[locale] = title
    }

    /**
     * Adds a title in the given language.
     *
     * @param locale    the locale ID representing the given language; the ID
     *                  must be specified in the form
     *                  {@code language[-country[-variant]]}
     * @param title     the title
     */
    void addLocalizedTitle(String locale, String title) {
        addLocalizedTitle(locale.tokenize('-') as Locale, title)
    }

    /**
     * Gets the title in the given language or in the default language.
     *
     * @param locale    the locale representing the given language; if
     *                  <code>null</code> the default locale is used
     * @return          the title in that language
     */
    String getTitle(Locale locale = null) {
        locale = locale ?: Locale.default
        String title = localizedTitles[locale]
        if (!title) {
            locale = new Locale(locale.language, locale.country)
        }
        title = localizedTitles[locale]
        if (!title) {
            locale = new Locale(locale.language)
        }
        return localizedTitles[locale] ?: defTitle
    }
}
