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

	synchronized Object getAt(String name) {
		return getConfig(name)
	}

	synchronized Object getConfig(String name) {
		return getConfigObject(name)?.value
	}

	static synchronized ConfigHolder getInstance() {
		if (instance == null) {
			instance = new ConfigHolder()
		}
		return instance
	}

	synchronized void setAt(String name, Object value) {
		setConfig(name, value)
	}

	synchronized void setConfig(String name, Object value) {
		Config config = getConfigObject(name)
		if (config == null) {
			config = new Config(name: name)
			cache[name] = config
		}
		config.value = value
		config.save(flush: true)
	}


    //-- Non-public methods ---------------------

    protected Config getConfigObject(String name) {
        Config config = cache[name]
        if (config == null) {
            config = Config.findByName(name)
            if (config != null) {
                cache[name] = config
            }
        }
        return config
    }
}
