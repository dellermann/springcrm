package org.amcworld.springcrm

import org.codehaus.groovy.grails.commons.GrailsClass;

class ViewFilters {

	def dependsOn = [LoginFilters]

	def filters = {
		pagination(controller:'*', action:'list') {
			def sessionKey = { String name ->
				String key = name + controllerName.capitalize()
				if (params.type)  key += params.type
				return key
			}

			before = {
				def f = { pk, s, sk ->
					if (params[pk] == null) params[pk] = s[sk]
					else s[sk] = params[pk]
				}

				/* store or restore offset */
				String key = sessionKey('offset')
				f('offset', session, key)
				
				/* compute number of entries of the associated domain */
				GrailsClass cls = 
					grailsApplication.getArtefactByLogicalPropertyName(
						'Domain', controllerName
					)
				int count = cls.clazz.'count'()
				int max = Math.min(params.max ? params.int('max') : 10, 100)
				int maxOffset = Math.floor((count - 1) / max) * max
				params.offset = Math.min(maxOffset, params.int('offset') ?: 0)
				session[key] = params.offset

				/* store or restore sorting and order */
				String name = controllerName.capitalize()
				User user = User.get(session.user.id)
				f('sort', user.settings, "sort${name}")
				f('order', user.settings, "order${name}")
				user.save(flush:true)
			}
			
			after = {
				session[sessionKey('offset')] = params.offset
			}
		}
	}
}
