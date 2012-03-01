/*
 * GDataFilters.groovy
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

import com.google.gdata.client.http.AuthSubUtil


/**
 * The class {@code GDataFilters} contains filters which authenticate at Google
 * before synchronizing data.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class GDataFilters {

    //-- Instance variables ---------------------

	def dependsOn = [LoginFilters]


    //-- Public methods -------------------------

	def filters = {
		gdataSync(controller: '*', action: 'gdatasync') {
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
						redirect(url: url)
						return false
					}
				}
			}
		}
	}
}
