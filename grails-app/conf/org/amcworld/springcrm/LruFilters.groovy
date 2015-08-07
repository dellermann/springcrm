/*
 * LruFilters.groovy
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


package org.amcworld.springcrm

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClass


/**
 * The class {@code LruFilters} contains filters which are store shown and
 * edited content items in the LRU (last recently used) list.
 *
 * @author  Daniel Ellermann
 * @version 1.5
 */
class LruFilters {

    //-- Instance variables ---------------------

    GrailsApplication grailsApplication
    LruService lruService


    //-- Public methods -------------------------

    def filters = {
        lruRecord(controller: '*',
                  action: 'show|edit|save|update|updatePayment')
        {
            after = { model ->
                def instance = null
                if (model) {
                    instance = model["${controllerName}Instance"]
                }
                if (!instance) {
                    instance = request["${controllerName}Instance"]
                }
                if (!instance?.ident()) {
                    return
                }
                if (instance?.hasErrors()) {
                    return
                }

                lruService.recordItem controllerName, instance
            }
        }

        lruRemove(controller: '*', action: 'delete',
                  controllerExclude: 'document')
        {
            before = {
                if (params.confirmed) {
                    lruService.removeItem controllerName, params.id as long
                }
            }
        }
    }
}
