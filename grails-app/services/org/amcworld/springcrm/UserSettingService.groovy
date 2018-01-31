/*
 * UserSettingService.groovy
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

import grails.gorm.services.Service
import groovy.transform.CompileStatic


/**
 * The interface {@code IUserSettingService} contains general methods to handle
 * user settings in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
interface IUserSettingService {

    //-- Public methods -------------------------

    /**
     * Finds a setting of the given user with the given name.
     *
     * @param user  the given user
     * @param name  the given name
     * @return      the found user setting or {@code null} if no such setting
     *              exists
     */
    UserSetting findByUserAndName(User user, String name)

    /**
     * Saves the given user setting.
     *
     * @param user  the user setting which should be saved
     * @return      the saved user setting
     */
    UserSetting save(UserSetting userSetting)
}


/**
 * The class {@code UserSettingService} implements additional methods to handle
 * user settings in the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@CompileStatic
@Service(UserSetting)
abstract class UserSettingService implements IUserSettingService {

    //-- Public methods -------------------------

    /**
     * Gets the integer value of the given user setting.
     *
     * @param user      the given user
     * @param name      the name of the setting
     * @param defValue  any default value which is used if no such setting was
     *                  found or the value is unset
     * @return          the setting value or {@code null} if no such setting
     *                  exists and no default value has been specified
     */
    Integer getInteger(User user, String name, Integer defValue = null) {
        Integer res = findByUserAndName(user, name)?.value?.asType(Integer)

        res == null ? defValue : res
    }

    /**
     * Gets the string value of the given user setting.
     *
     * @param user      the given user
     * @param name      the name of the setting
     * @param defValue  any default value which is used if no such setting was
     *                  found or the value is unset
     * @return          the setting value or {@code null} if no such setting
     *                  exists and no default value has been specified
     */
    String getString(User user, String name, String defValue = null) {
        String res = findByUserAndName(user, name)?.value

        res == null ? defValue : res
    }

    /**
     * Stores the given user setting.
     *
     * @param user  the given user
     * @param name  the name of the setting
     * @param value the associated value
     * @return      the stored user setting
     */
    UserSetting store(User user, String name, Object value) {
        save new UserSetting(user: user, name: name, value: value?.toString())
    }
}
