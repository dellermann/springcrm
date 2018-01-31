/*
 * SettingsInterceptorBase.groovy
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

import grails.artefact.Interceptor
import groovy.transform.CompileStatic
import javax.servlet.http.HttpSession


/**
 * The class {@code SettingsInterceptorBase} represents the base class of
 * interceptor classes which handle user settings.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
@CompileStatic
class SettingsInterceptorBase implements Interceptor {

    //-- Fields ---------------------------------

    UserService userService
    UserSettingService userSettingService


    //-- Non-public methods ---------------------

    /**
     * Depending on the availability of the parameter with the name
     * {@code paramName} in map {@code params} the method does the following:
     * <ul>
     *   <li>if the parameter exists its value is stored in map {@code settings}
     *   with key {@code settingsKey}</li>
     *   <li>if the parameter does not exist it is set to the value from map
     *   {@code settings} with key {@code settingsKey}</li>
     * </ul>
     *
     * @param param         the map containing the parameters
     * @param paramName     the name of the parameter in map {@code params}
     * @param settings      the map containing the settings
     * @param settingKey    the key use to retrieve or set the value in map
     *                      {@code settings}
     * @param convert       a closure used to convert the value when storing in
     *                      parameters; {@code null} if no conversion should be
     *                      performed
     */
    protected static void exchangeSetting(Map<?, ?> params, String paramName,
                                          Map<String, String> settings,
                                          String settingKey,
                                          Closure convert = null)
    {
        if (params[paramName] == null) {
            def value = settings[settingKey]
            if (convert != null) value = convert(value)
            params[paramName] = value
        } else {
            settings[settingKey] = params[paramName]?.toString()
        }
    }

    /**
     * Depending on the availability of the parameter with the name
     * {@code paramName} in map {@code params} the method does the following:
     * <ul>
     *   <li>if the parameter exists its value is stored as attribute with the
     *   name {@code attrName} in session {@code session}</li>
     *   <li>if the parameter does not exist it is set to the value of the
     *   attribute with the name {@code attrName} of session {@code session}</li>
     * </ul>
     *
     * @param param     the map containing the parameters
     * @param paramName the name of the parameter in map {@code params}
     * @param session   the given session
     * @param attrName  the name of the attribute in the session
     */
    protected static void exchangeSetting(Map<?, ?> params, String paramName,
                                          HttpSession session, String attrName)
    {
        if (params[paramName] == null) {
            params[paramName] = session.getAttribute(attrName)
        } else {
            session.setAttribute attrName, params[paramName]
        }
    }

    /**
     * Gets the currently logged in user.
     *
     * @return  the currently logged in user
     * @since   3.0
     */
    protected User getCurrentUser() {
        userService.getCurrentUser()
    }

    /**
     * Loads the settings with the given names of the currently logged in user.
     *
     * @param names the given setting names
     * @return      the setting names and their values
     * @since 3.0
     */
    protected Map<String, String> loadSettings(String... names) {
        Map<String, String> res = new HashMap<>(names.length)
        User user = currentUser
        if (user != null) {
            for (String name : names) {
                res[name] = userSettingService.getString(user, name)
            }
        }

        res
    }

    /**
     * Stores the given settings for the currently logged in user.
     *
     * @param settings  the given settings
     * @since 3.0
     */
    protected void storeSettings(Map<String, String> settings) {
        User user = currentUser
        if (user != null) {
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                userSettingService.store user, entry.key, entry.value
            }
        }
    }
}
