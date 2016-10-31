/*
 * SearchInterceptor.groovy
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
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code SearchInterceptor} represents an interceptor which reflect
 * changes to domain model classes in search index.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class SearchInterceptor {

    //-- Fields ---------------------------------

    SearchService searchService


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    SearchInterceptor() {
        match action: ~/(save|update|delete)/
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  Depending on the action the
     * method creates, updates or removes an entry in search index.
     *
     * @return  always {@code true}
     */
    boolean after() {
        def instance = request["${controllerName}Instance"]
        if (instance instanceof GormEntity) {
            GormEntity entity = (GormEntity) instance
            switch (actionName) {
            case 'save':
                searchService.index entity
                break
            case 'update':
                searchService.reindex entity
                break
            case 'delete':
                searchService.removeFromIndex entity
                break
            }
        }

        true
    }
}
