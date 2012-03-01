/*
 * BootStrap.groovy
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


import org.amcworld.springcrm.OverviewPanelRepository


/**
 * The class {@code BootStrap} performs initial operations when booting the
 * application.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class BootStrap {

    //-- Instance variables ---------------------

	def exceptionHandler
    def installService


    //-- Public methods -------------------------

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
