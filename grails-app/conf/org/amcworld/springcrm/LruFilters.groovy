/*
 * LruFilters.groovy
 *
 * Copyright (c) 2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
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

        lruRemove(controller: '*', action: 'delete') {
            before = {
                if (params.confirmed) {
                    lruService.removeItem(controllerName, params.id as long)
                }
            }
        }
    }
}
