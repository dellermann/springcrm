/*
 * ConfigHolder.groovy
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


package org.amcworld.springcrm


/**
 * The class {@code ConfigHolder} holds the system configuration of the
 * application.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   0.9
 */
class ConfigHolder {

    //-- Class variables ------------------------

    static ConfigHolder instance


    //-- Public methods -------------------------

    List<Config> getAllConfig() {
        Config.list()
    }

    Config getAt(String name) {
        getConfig name
    }

    Config getConfig(String name) {
        Config.findByName name
    }

    static synchronized ConfigHolder getInstance() {
        if (instance == null) {
            instance = new ConfigHolder()
        }
        instance
    }

    void removeConfig(String name) {
        Config config = getConfig(name)
        if (config) {
            config.delete flush: true
        }
    }

    void putAt(String name, String value) {
        setConfig name, value
    }

    void setConfig(String name, String value) {
        Config config = getConfig(name) ?: new Config(name: name)
        config.value = value
        config.save flush: true
    }
}

