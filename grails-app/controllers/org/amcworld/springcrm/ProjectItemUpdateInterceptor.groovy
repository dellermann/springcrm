/*
 * ProjectItemUpdateInterceptor.groovy
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
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code ProjectItemUpdateInterceptor} updates project items when a
 * domain model instance is updated.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class ProjectItemUpdateInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    ProjectItemUpdateInterceptor() {
        match action: 'update'
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method updates all
     * project item which refer to the updated domain model instance.
     *
     * @return  always {@code true}
     */
    boolean after() {
        def instance = request["${controllerName}Instance"]
        if (!(instance instanceof GormEntity)) {
            return true
        }

        GormEntity entity = (GormEntity) instance
        DetachedCriteria query = ProjectItem.where {
            controller == controllerName && itemId == entity.ident()
        }
        query.updateAll title: instance.toString()

        true
    }
}
