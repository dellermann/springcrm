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

	Object getAt(String name) {
		return getConfig(name)
	}

	Object getConfig(String name) {
		Config config = cache[name]
		if (config == null) {
			config = Config.findByName(name)
			if (config != null) {
				cache[name] = config
			}
		}
		return config?.value
	}

	static ConfigHolder getInstance() {
		if (instance == null) {
			instance = new ConfigHolder()
		}
		return instance
	}

	void setAt(String name, Object value) {
		setConfig(name, value)
	}

	void setConfig(String name, Object value) {
		Config config = getConfig(name)
		if (config == null) {
			config = new Config(name:name)
			cache[name] = config
		}
		config.value = value
		config.save(flush:true)
	}
}
