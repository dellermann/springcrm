/*
 * ConfigHolder.groovy
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


/**
 * The class {@code ConfigHolder} holds the system configuration of the
 * application.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class ConfigHolder {

	//-- Class variables ------------------------

	private static ConfigHolder instance


	//-- Instance variables ---------------------

	private Map<String, Config> cache


	//-- Constructors ---------------------------

	private ConfigHolder() {
		cache = new HashMap<String, Config>()
	}


	//-- Public methods -------------------------

	synchronized Config getAt(String name) {
		return getConfig(name)
	}

	synchronized Config getConfig(String name) {
        Config config = cache[name]
        if (config == null) {
            config = Config.findByName(name)
            if (config != null) {
                cache[name] = config
            }
        }
        return config
	}

	static synchronized ConfigHolder getInstance() {
		if (instance == null) {
			instance = new ConfigHolder()
		}
		return instance
	}

	synchronized void putAt(String name, String value) {
		setConfig(name, value)
	}

	synchronized void setConfig(String name, String value) {
		Config config = getConfig(name)
		if (config == null) {
			config = new Config(name: name)
			cache[name] = config
		}
		config.value = value
		config.save(flush: true)
    }
}
