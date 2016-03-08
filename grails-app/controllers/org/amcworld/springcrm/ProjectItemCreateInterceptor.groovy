/*
 * ProjectItemCreateInterceptor.groovy
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

import grails.artefact.Interceptor
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code ProjectItemCreateInterceptor} creates a new project item when
 * a domain model instance is created.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class ProjectItemCreateInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    ProjectItemCreateInterceptor() {
        match action: 'save'
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method creates a new
     * project item.
     *
     * @return  always {@code true}
     */
    boolean after() {
        def instance = request["${controllerName}Instance"]
        if (!(instance instanceof GormEntity) || !params.project ||
            !params.projectPhase)
        {
            return true
        }

        Project project = Project.get(params.long('project'))
        if (!project) {
            return true
        }

        ProjectPhase phase =
            ProjectPhase.valueOf(params.projectPhase.toString())
        project.phase = phase
        project.save failOnError: true, flush: true

        GormEntity entity = (GormEntity) instance
        ProjectItem projectItem = new ProjectItem(
            project: project, phase: phase, controller: controllerName,
            itemId: entity.ident(), title: entity.toString()
        )
        projectItem.save failOnError: true, flush: true

        true
    }
}
