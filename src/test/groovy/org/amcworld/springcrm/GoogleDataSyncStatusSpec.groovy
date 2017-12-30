/*
 * GoogleDataSyncStatusSpec.groovy
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

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class GoogleDataSyncStatusSpec extends Specification
	implements DomainUnitTest<GoogleDataSyncStatus>
{

    //-- Feature methods ------------------------

	def 'Check for equality'() {
		given: 'two google data sync statuses with same user, type and itemId'
		def g1 = new GoogleDataSyncStatus(
			user: new User(
				username: 'username',
				password: 'password',
				firstName: 'firstName',
				lastName: 'lastName',
				phone: '111',
				phoneHome: '111',
				mobile: '111',
				fax: '111',
				email: 'email',
//				admin: false,
				dateCreated: new Date(),
				lastUpdated: new Date(),
			),
			type: 'T',
			itemId: 14221,
			url: 'url',
			etag: 'etag',
			lastSync: new Date()
		)
		g1.user.id = 1
		println g1.user.ident()

		def g2 = new GoogleDataSyncStatus(
			user: new User(
				username: 'username',
				password: 'password',
				firstName: 'firstName',
				lastName: 'lastName',
				phone: '111',
				phoneHome: '111',
				mobile: '111',
				fax: '111',
				email: 'email',
//				admin: false,
				dateCreated: new Date(),
				lastUpdated: new Date(),
			),
			type: 'T',
			itemId: 14221,
			url: 'url',
			etag: 'etag',
			lastSync: new Date()
		)
		g2.user.id = 1
		println g2.user.ident()

		expect:
		g1 == g2
		g2 == g1
	}

	def 'Convert to string'() {
		given:
		def g = new GoogleDataSyncStatus(
			user: new User(
				username: 'username',
				password: 'password',
				firstName: 'firstName',
				lastName: 'lastName',
				email: 'email',
//				admin: false,
				dateCreated: new Date(),
				lastUpdated: new Date()
			),
			type: 'T',
			itemId: 14221,
			url: 'url',
			etag: 'etag',
			lastSync: new Date()
		)

		expect:
		g.toString() == 'username/T/14221'

	}

	def 'Compute hash code'() {
		when:
		def g = new GoogleDataSyncStatus(
			user: new User(
				username: 'username',
				password: 'password',
				firstName: 'firstName',
				lastName: 'lastName',
				email: 'email',
//				admin: false,
				dateCreated: new Date(),
				lastUpdated: new Date()
			),
			type: 'T',
			itemId: 14221,
			url: 'url',
			etag: 'etag',
			lastSync: new Date()
		)

		then: 'I get a valid hash code'
		g.hashCode() == -838664358

	}
}
