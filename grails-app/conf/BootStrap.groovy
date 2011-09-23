import org.amcworld.springcrm.ConfigHolder
import org.amcworld.springcrm.OverviewPanelRepository

class BootStrap {

	def exceptionHandler

    def init = { servletContext ->
		exceptionHandler.exceptionMappings = [
			'java.lang.Exception': '/error'
		]

		def configHolder = ConfigHolder.instance
		servletContext.config = configHolder

		OverviewPanelRepository opr = OverviewPanelRepository.instance
		opr.initialize(servletContext.getResourceAsStream('/WEB-INF/data/overview-panel-repository.xml'))
		servletContext.overviewPanelRepository = opr
    }

    def destroy = {}
}
