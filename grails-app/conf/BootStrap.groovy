import org.amcworld.springcrm.ConfigHolder

class BootStrap {

	def exceptionHandler

    def init = { servletContext ->
		exceptionHandler.exceptionMappings = [
			'java.lang.Exception': '/error'
		]

		def configHolder = new ConfigHolder()
		servletContext.config = configHolder
    }

    def destroy = {
    }
}
