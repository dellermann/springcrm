/*
 * LruFilters.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import org.codehaus.groovy.grails.commons.GrailsClass


/**
 * The class {@code LruFilters} contains filters which are store shown and
 * edited content items in the LRU (last recently used) list.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class LruFilters {

    //-- Instance variables ---------------------

    def grailsApplication
	def lruService


    //-- Public methods -------------------------

    def filters = {
        lruRecord(controller: '*', action: 'show|edit') {
            after = { model ->
				if (model) {
	                def instance = model["${controllerName}Instance"]
					if (instance) {
						lruService.recordItem(
							controllerName, params.id as long,
                            instance.toString()
						)
					}
				}
            }
        }

        lruUpdate(controller: '*', action: 'save|update') {
            after = {
                if (params.id) {
                    GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
                        'Domain', controllerName
                    )
                    long id = params.id as long
                    def instance = cls.clazz.'get'(id)
                    if (instance) {
                        lruService.recordItem(
                            controllerName, id, instance.toString()
                        )
                    }
                }
            }
        }

        lruRemove(controller: '*', action: 'delete', controllerExclude: 'document') {
            before = {
                if (params.confirmed) {
                    lruService.removeItem(controllerName, params.id as long)
                }
            }
        }
    }
}
