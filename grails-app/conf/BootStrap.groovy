/*
 * BootStrap.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
import org.amcworld.springcrm.InstallerDisableTask
import org.amcworld.springcrm.OverviewPanelRepository
import org.amcworld.springcrm.google.GoogleContactSyncTask
import org.codehaus.groovy.grails.commons.GrailsApplication


/**
 * The class {@code BootStrap} performs initial operations when booting the
 * application.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class BootStrap {

    //-- Constants ------------------------------

    public static final int CURRENT_DB_VERSION = 4i


    //-- Instance variables ---------------------

    def dataSource
    def exceptionHandler
    GoogleContactSyncTask googleContactSyncTask
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
            File file = new File(springcrmConfig.toString())
            if (file.exists()) {
                def whatToParse
                if (file.name.endsWith('.groovy')) {
                    whatToParse = file.toURI().toURL()
                } else {
                    whatToParse = new Properties()
                    whatToParse.load file.newReader()
                }
                ConfigObject config = new ConfigSlurper().parse(whatToParse)
                grailsApplication.config.merge config
            }
        }

        /* apply difference sets */
        installService.applyAllDiffSets dataSource.connection,
            CURRENT_DB_VERSION

        /* perform data migration */
        installService.migrateData dataSource.connection

        /* start tasks */
        Config config =
            ConfigHolder.instance.getConfig('syncContactsFrequency')
        long interval = (config ? (config.value as Long) : 5L) * 60000L
        Timer timer = new Timer('tasks')
        timer.schedule googleContactSyncTask, interval, interval
        InstallerDisableTask installDisableTask =
            new InstallerDisableTask(installService: installService)
        timer.schedule installDisableTask, interval, interval

        /* initialize panels on overview page */
        OverviewPanelRepository opr = OverviewPanelRepository.instance
        opr.initialize(servletContext.getResourceAsStream(
            '/WEB-INF/data/overview-panel-repository.xml'
        ))
    }

    def destroy = {}
}
