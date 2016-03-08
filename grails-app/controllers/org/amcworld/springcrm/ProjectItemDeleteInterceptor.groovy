/*
 * ProjectItemDeleteInterceptor.groovy
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
import grails.gorm.DetachedCriteria
import groovy.transform.CompileStatic


/**
 * The class {@code ProjectItemUpdateInterceptor} deletes project items when a
 * domain model instance is deleted.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class ProjectItemDeleteInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    ProjectItemDeleteInterceptor() {
        match action: 'delete'
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method deletes all
     * project item which refer to the deleted domain model instance.
     *
     * @return  always {@code true}
     */
    boolean after() {
        if (params.id && params.confirmed) {
            DetachedCriteria query = ProjectItem.where {
                controller == controllerName && itemId == params.long('id')
            }
            def res = query.deleteAll()
        }

        true
    }
}
