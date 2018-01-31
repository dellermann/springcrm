/*
 * BootStrap.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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
import org.springframework.core.io.Resource
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver


/**
 * The class {@code BootStrap} performs initial operations when booting the
 * application.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class BootStrap {

    //-- Fields ---------------------------------

    ConfigService configService
    SimpleMappingExceptionResolver exceptionHandler
//    GoogleContactSyncTask googleContactSyncTask
    GrailsApplication grailsApplication
    InstallService installService
//    SearchService searchService


    //-- Public methods -------------------------

    def init = { ServletContext servletContext ->
        exceptionHandler.exceptionMappings = [
            'java.lang.Exception': '/error'
        ]

        /* initialize data */
        installService.installRoles()

        /* start tasks */
        long interval =
            configService.getLong('syncContactsFrequency', 5L) * 60000L
        Timer timer = new Timer('tasks')
//        timer.schedule googleContactSyncTask, interval, interval
        InstallerDisableTask installDisableTask =
            new InstallerDisableTask(installService: installService)
        timer.schedule installDisableTask, interval, interval

        /* initialize panels on overview page */
        initializeOverviewPanels()

//        /* search */
//        if (!searchService.searchIndexReady) {
//            Thread.start('buildSearchIndex') {
//                SearchData.withNewSession {
//                    searchService.bulkIndex()
//                }
//            }
//        }
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
