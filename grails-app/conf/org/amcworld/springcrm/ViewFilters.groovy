package org.amcworld.springcrm

import org.codehaus.groovy.grails.commons.GrailsClass;

class ViewFilters {

	def filters = {
		pagination(controller:'*', action:'list') {
			before = {

				/* save or restore offset */
				String key = "offset${controllerName.capitalize()}".toString()
				if (params.offset != null) {
					session[key] = params.offset
				} else {
					params.offset = session[key] ?: 0
				}

				/* compute number of entries of the associated domain */
				GrailsClass cls = 
					grailsApplication.getArtefactByLogicalPropertyName(
						'Domain', controllerName
					)
				int count = cls.clazz.'count'()
				int max = Math.min(params.max ? params.int('max') : 10, 100)
				int maxOffset = Math.floor((count - 1) / max) * max
				params.offset = Math.min(maxOffset, params.int('offset'))
				session[key] = params.offset
			}
		}
	}
}
