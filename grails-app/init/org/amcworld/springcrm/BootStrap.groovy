/*
 * BootStrap.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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


package org.amcworld.springcrm

import grails.core.GrailsApplication
import javax.servlet.ServletContext
import javax.sql.DataSource
import org.amcworld.springcrm.*
import org.amcworld.springcrm.google.GoogleContactSyncTask
import org.springframework.core.io.Resource
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver


/**
 * The class {@code BootStrap} performs initial operations when booting the
 * application.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class BootStrap {

    //-- Constants ------------------------------

    public static final int CURRENT_DB_VERSION = 5i


    //-- Fields ---------------------------------

    DataSource dataSource
    SimpleMappingExceptionResolver exceptionHandler
    GoogleContactSyncTask googleContactSyncTask
    GrailsApplication grailsApplication
    InstallService installService
    SearchService searchService


    //-- Public methods -------------------------

    def init = { ServletContext servletContext ->
        exceptionHandler.exceptionMappings = [
            'java.lang.Exception': '/error'
        ]

        /* apply difference sets */
        installService.applyAllDiffSets(
            dataSource.connection, CURRENT_DB_VERSION
        )

        /* perform data migration */
        installService.migrateData dataSource.connection

        /* start tasks */
        Config config = ConfigHolder.instance.getConfig('syncContactsFrequency')
        long interval = (config ? (config.value as Long) : 5L) * 60000L
        Timer timer = new Timer('tasks')
        timer.schedule googleContactSyncTask, interval, interval
        InstallerDisableTask installDisableTask =
            new InstallerDisableTask(installService: installService)
        timer.schedule installDisableTask, interval, interval

        /* initialize panels on overview page */
        initializeOverviewPanels()

        /* search */
        if (!searchService.searchIndexReady) {
            Thread.start('buildSearchIndex') {
                SearchData.withNewSession {
                    searchService.bulkIndex()
                }
            }
        }
    }

    def destroy = {}


    //-- Non-public methods ---------------------

    /**
     * Initializes the available panels of the overview page from an XML file.
     *
     * @since 2.1
     */
    private void initializeOverviewPanels() {
        Resource res = grailsApplication.mainContext.getResource(
            'classpath:public/overview-panel-repository.xml'
        )
        if (!res.exists()) {
            log.error 'Overview panel repository not found in classpath.'
            return
        }

        OverviewPanelRepository opr = OverviewPanelRepository.instance
        InputStream stream = res.inputStream
        opr.initialize stream
        stream.close()
    }
}
