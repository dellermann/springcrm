import org.amcworld.springcrm.ConfigHolder;

class BootStrap {

    def init = { servletContext ->
		def configHolder = new ConfigHolder()
		servletContext.config = configHolder
    }

    def destroy = {
    }
}
