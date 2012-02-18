import org.amcworld.springcrm.OverviewPanelRepository

class BootStrap {

	def exceptionHandler
    def installService

    def init = { servletContext ->
		exceptionHandler.exceptionMappings = [
			'java.lang.Exception': '/error'
		]

        def installStatus = org.amcworld.springcrm.Config.findByName('installStatus')
        if (!installStatus || !installStatus.value) {
            installService.enableInstaller()
        }

		OverviewPanelRepository opr = OverviewPanelRepository.instance
		opr.initialize(servletContext.getResourceAsStream('/WEB-INF/data/overview-panel-repository.xml'))
    }

    def destroy = {}
}
