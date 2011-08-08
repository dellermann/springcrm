import org.amcworld.springcrm.ConfigHolder
import org.amcworld.springcrm.util.DataSourceUtils

class BootStrap {

    def init = { servletContext ->
		DataSourceUtils.tune(servletContext)

		def configHolder = new ConfigHolder()
		servletContext.config = configHolder
    }

    def destroy = {
    }
}
