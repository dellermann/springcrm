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


import org.amcworld.springcrm.Config
import org.amcworld.springcrm.ConfigHolder
import org.amcworld.springcrm.OverviewPanelRepository


/**
 * The class {@code BootStrap} performs initial operations when booting the
 * application.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class BootStrap {

    //-- Instance variables ---------------------

	def exceptionHandler


    //-- Public methods -------------------------

    def init = { servletContext ->
		exceptionHandler.exceptionMappings = [
			'java.lang.Exception': '/error'
		]

        Config config = ConfigHolder.instance.getConfig('syncContactsFrequency')
        Long interval = config ? (config.value as Long) : 5L
        GoogleContactSyncJob.schedule(interval * 60000L, -1)

		OverviewPanelRepository opr = OverviewPanelRepository.instance
		opr.initialize(servletContext.getResourceAsStream('/WEB-INF/data/overview-panel-repository.xml'))
    }

    def destroy = {}
}
