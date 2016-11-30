/*
 * ConfigHolder.groovy
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

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.PackageScope


/**
 * The class {@code ConfigHolder} holds the system configuration of the
 * application.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
@CompileStatic
class ConfigHolder {

    //-- Constructors ---------------------------

    @PackageScope
    ConfigHolder() {}


    //-- Public methods -------------------------

    /**
     * Gets all configurations.
     *
     * @return  a list of all configurations
     */
    List<Config> getAllConfig() {
        Config.list()
    }

    /**
     * Gets the configuration with the given name.
     *
     * @param name  the name of configuration
     * @return      the configuration; {@code null} if no configuration with
     *              the given name exists
     * @see         #getConfig(String)
     */
    Config getAt(String name) {
        getConfig name
    }

    /**
     * Gets the configuration with the given name.
     *
     * @param name  the name of configuration
     * @return      the configuration; {@code null} if no configuration with
     *              the given name exists
     * @see         #getAt(String)
     */
    @CompileDynamic
    Config getConfig(String name) {
        Config.findByName name
    }

    /**
     * Gets the only instance of this class.
     *
     * @return    the instance
     */
    static ConfigHolder getInstance() {
        InstanceHolder.INSTANCE
    }

    /**
     * Removes the configuration with the given name.
     *
     * @param name  the name of the configuration
     */
    void removeConfig(String name) {
        Config config = getConfig(name)
        if (config) {
            config.delete flush: true
        }
    }

    /**
     * Sets the configuration of the given name to the specified value.
     *
     * @param name  the name of the configuration
     * @param value the value that should be set
     * @see #setConfig(String, String)
     */
    void putAt(String name, String value) {
        setConfig name, value
    }

    /**
     * Sets the configuration of the given name to the specified value.
     *
     * @param name  the name of the configuration
     * @param value the value that should be set
     * @see #putAt(String, String)
     */
    void setConfig(String name, String value) {
        Config config = getConfig(name) ?: new Config(name: name)
        config.value = value
        config.save flush: true
    }


    //-- Inner classes --------------------------

    private static class InstanceHolder {

        //-- Constants --------------------------

        public static final ConfigHolder INSTANCE = new ConfigHolder()
    }
}

