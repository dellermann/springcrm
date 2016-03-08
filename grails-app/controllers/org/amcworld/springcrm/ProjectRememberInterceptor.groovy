/*
 * ProjectRememberInterceptor.groovy
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

import groovy.transform.CompileStatic


/**
 * The class {@code ProjectRememberInterceptor} prepares create forms of other
 * domain model classes when a project has been specified.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class ProjectRememberInterceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    ProjectRememberInterceptor() {
        match action: 'create'
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method sets the organization
     * and person as parameters if a project has been specified.
     *
     * @return  always {@code true}
     */
    boolean before() {
        if (params.project) {
            Project project = Project.get(params.long('project'))
            if (project) {
                params.organization = project.organization
                params.person = project.person
            }
        }

        true
    }

    /**
     * Called after the action has been executed.  The method stores the
     * submitted project and project phase in the model to be rendered in the
     * view.
     *
     * @return  always {@code true}
     */
    boolean after() {
        Map<String, Object> model = getModel()
        if (model != null && params.project && params.projectPhase) {
            model.project = params.project
            model.projectPhase = params.projectPhase
        }

        true
    }
}
