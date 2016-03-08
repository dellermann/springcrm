/*
 * LruRemoveInterceptor.groovy
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


/**
 * The class {@code LruRemoveInterceptor} remove last recently used (LRU) items
 * after their associated records have been deleted.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class LruRemoveInterceptor implements Interceptor {

    //-- Instance variables ---------------------

    LruService lruService


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    LruRemoveInterceptor() {
        match(action: 'delete')
            .excludes(controller: 'document')
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method removes an LRU item if
     * the deletion has been confirmed.
     *
     * @return  always {@code true}
     */
    boolean before() {
        if (params.confirmed) {
            lruService.removeItem controllerName, params.id as long
        }

        true
    }
}
