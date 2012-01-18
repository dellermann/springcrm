package org.amcworld.springcrm

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
