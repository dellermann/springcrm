package org.amcworld.springcrm

import com.google.gdata.client.http.AuthSubUtil

class GDataFilters {
	
	def dependsOn = [LoginFilters]

	def filters = {
		gdataSync(controller:'*', action:'gdatasync') {
			before = {
				def token = session.gdataToken
				if (!token) {
					token = params.token
					if (token) {
						def sessionToken = 
							AuthSubUtil.exchangeForSessionToken(token, null)
						session.gdataToken = sessionToken
					} else {
						def next = request.request.requestURL.toString()
						def queryString = request.queryString
						if (queryString) {
							next << '?' << queryString
						}
						def url = AuthSubUtil.getRequestUrl(
							next.toString(), 
							'https://www.google.com/m8/feeds/',
							false, true
						)
						redirect(url:url)
						return false
					}
				}
			}
		}
	}
}
