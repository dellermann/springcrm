/*
 * LruRecordInterceptor.groovy
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

import grails.artefact.Interceptor
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code LruRecordInterceptor} records the last recently used (LRU)
 * items.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
@CompileStatic
class LruRecordInterceptor implements Interceptor {

    //-- Fields ---------------------------------

    LruService lruService


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    LruRecordInterceptor() {
        match action: ~/(show|edit|save|update|updatePayment)/
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method records the
     * instance object which has been processed by the action as LRU item.
     *
     * @return  always {@code true}
     */
    boolean after() {
        if (!params.boolean('noLruRecord')) {
            def instance = model?.get(controllerName)
            if (instance == null) {
                instance = request?.getAttribute(controllerName)
            }
            if (instance instanceof GormEntity) {
                GormEntity entity = (GormEntity) instance
                if (entity.ident() && !entity.hasErrors()) {
                    lruService.recordItem controllerName, instance
                }
            }
        }

        true
    }
}
