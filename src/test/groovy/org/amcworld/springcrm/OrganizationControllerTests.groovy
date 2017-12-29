/*
 * OrganizationControllerTests.groovy
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


//@TestFor(OrganizationController)
//@Mock(Organization)
class OrganizationControllerTests {

    //-- Constants ------------------------------

	private static final String ERROR_MSG = 'error message'

//		controller.metaClass.message = { Map map -> return ERROR_MSG }
//		def ts = mockFor(TransactionStatus, true)
//		ts.demand.setRollbackOnly { -> }
//		Organization.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }
//
//		def org1 = new Organization(number:10000, recType:1, name:'Organization 1')
//		def org2 = new Organization(number:10001, recType:1, name:'Organization 2')
//		mockDomain(Organization, [org1, org2])
//
//		def seqNumber = new SeqNumber(controllerName:'organization', nextNumber:10002, prefix:'O', suffix:'')
//		mockDomain(SeqNumber, [seqNumber])
//
//		controller.seqNumberService = new SeqNumberService()


    //-- Public methods -------------------------

    void testIndex() {
		controller.index()
		assert '/organization/list' == response.redirectedUrl
    }

	void testList() {
		def map = controller.list()
		assertEquals 2, map.organizationInstanceTotal
		assertEquals 2, map.organizationInstanceList.size()
		assertEquals 'Organization 1', map.organizationInstanceList[0].name
		assertEquals 'Organization 2', map.organizationInstanceList[1].name
	}

	void testCreate() {
		def map = controller.create()
		assertNotNull map.organizationInstance
		assertNull map.organizationInstance.name
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.recType = 1
		controller.params.name = 'Organization 3'
		controller.params.phone = '030 1234567'
		controller.params.email = 'info@example.com'
		controller.save()
		assertEquals 3, Organization.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('organization')
		assertEquals 10003, seqNumber.nextNumber
	}

	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.recType = 1
		controller.params.name = ''
		controller.params.phone = '030 1234567'
		controller.params.email = 'info@example.com'
		def map = controller.save()
		assertEquals 2, Organization.count()
		assertEquals 'unique', map.organizationInstance.errors['number']
		assertEquals 'blank', map.organizationInstance.errors['name']
	}

	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Organization 2', map.organizationInstance.name

		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Organization 1', map.organizationInstance.name

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testUpdate() {
		controller.params.id = 1
		controller.params.recType = 1
		controller.params.number = 10000
		controller.params.name = 'Organization 3'
		controller.params.phone = '030 7654321'
		controller.params.email = 'info@example.com'
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Organization.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('organization')
		assertEquals 10002, seqNumber.nextNumber
		def org = Organization.get(1)
		assertEquals 10000, org.number
		assertEquals 'Organization 3', org.name
		assertEquals '030 7654321', org.phone

		controller.params.name = ''
		def map = controller.update()
		assertEquals 2, Organization.count()
		assertEquals 'blank', map.organizationInstance.errors['name']

		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Organization.count()
		assertNull Organization.get(1)
		assertNotNull Organization.get(2)

		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
