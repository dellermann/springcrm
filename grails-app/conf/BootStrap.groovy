import org.amcworld.springcrm.OverviewPanelRepository

class BootStrap {

	def exceptionHandler

    def init = { servletContext ->
		exceptionHandler.exceptionMappings = [
			'java.lang.Exception': '/error'
		]

		OverviewPanelRepository opr = OverviewPanelRepository.instance
		opr.initialize(servletContext.getResourceAsStream('/WEB-INF/data/overview-panel-repository.xml'))
    }

    def destroy = {}
}
