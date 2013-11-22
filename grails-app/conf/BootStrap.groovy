/*
 * BootStrap.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
import org.amcworld.springcrm.InstallService
import org.amcworld.springcrm.OverviewPanelRepository
import org.codehaus.groovy.grails.commons.GrailsApplication


/**
 * The class {@code BootStrap} performs initial operations when booting the
 * application.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 */
class BootStrap {

    //-- Constants ------------------------------

    public static final int CURRENT_DB_VERSION = 3i


    //-- Instance variables ---------------------

    def dataSource
    def exceptionHandler
    GrailsApplication grailsApplication
    InstallService installService
    def springcrmConfig


    //-- Public methods -------------------------

    def init = { servletContext ->
        exceptionHandler.exceptionMappings = [
            'java.lang.Exception': '/error'
        ]

        /* load instance specific configuration file */
        if (springcrmConfig) {
            def file = new File(springcrmConfig)
            if (file.exists()) {
                def properties = new Properties()
                properties.load file.newReader()
                def config = new ConfigSlurper().parse(properties)
                grailsApplication.config.merge config
            }
        }

        /* apply difference sets */
        installService.applyAllDiffSets dataSource.connection, CURRENT_DB_VERSION

        /* perform data migration */
        installService.migrateData dataSource.connection

        /* start Quartz jobs */
        Config config = ConfigHolder.instance.getConfig('syncContactsFrequency')
        Long interval = config ? (config.value as Long) : 5L
        GoogleContactSyncJob.schedule interval * 60000L, -1

        /* initialize panels on overview page */
        OverviewPanelRepository opr = OverviewPanelRepository.instance
        opr.initialize servletContext.getResourceAsStream('/WEB-INF/data/overview-panel-repository.xml')
    }

    def destroy = {}
}
