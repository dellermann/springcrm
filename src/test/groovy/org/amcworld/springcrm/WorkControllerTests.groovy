/*
 * WorkControllerTests.groovy
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

import org.springframework.transaction.TransactionStatus


//@TestFor(WorkController)
class WorkControllerTests {

	private static final String ERROR_MSG = 'error message'

    void setUp() {
        super.setUp()

		controller.metaClass.message = { Map map -> return ERROR_MSG }
		def ts = mockFor(TransactionStatus, true)
		ts.demand.setRollbackOnly { -> }
		Work.metaClass.static.withTransaction = { Closure c -> c(ts.createMock()) }

		def s1 = new Work(number:10000, name:'Service 1')
		def s2 = new Work(number:10001, name:'Service 2')
		mockDomain(Work, [s1, s2])

//		def seqNumber = new SeqNumber(controllerName:'work', nextNumber:10002, prefix:'S', suffix:'')
//		mockDomain(SeqNumber, [seqNumber])

		controller.seqNumberService = new SeqNumberService()
    }

    void testIndex() {
		controller.index()
		assertEquals 'list', controller.redirectArgs['action']
    }

	void testList() {
		def map = controller.list()
		assertEquals 2, map.workInstanceTotal
		assertEquals 2, map.workInstanceList.size()
		assertEquals 'Service 1', map.workInstanceList[0].name
		assertEquals 'Service 2', map.workInstanceList[1].name
	}

	void testCreate() {
		def map = controller.create()
		assertNotNull map.workInstance
		assertNull map.workInstance.name
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.name = 'Service 3'
		controller.params.quantity = 35d
		controller.params.unitPrice = 4.7d
		controller.save()
		assertEquals 3, Work.count()
		assertEquals 'show', controller.redirectArgs['action']
		SeqNumber seqNumber = SeqNumber.findByControllerName('work')
		assertEquals 10003, seqNumber.nextNumber
	}

	void testSaveFailed() {
		controller.params.number = 10001
		controller.params.name = ''
		controller.params.quantity = 35
		controller.params.unitPrice = 4.7d
		def map = controller.save()
		assertEquals 2, Work.count()
		assertEquals 'unique', map.workInstance.errors['number']
		assertEquals 'blank', map.workInstance.errors['name']
	}

	void testShow() {
		controller.params.id = 2
		def map = controller.show()
		assertEquals 'Service 2', map.workInstance.name

		controller.params.id = 10
		controller.show()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testEdit() {
		controller.params.id = 1
		def map = controller.edit()
		assertEquals 'Service 1', map.workInstance.name

		controller.params.id = 10
		controller.edit()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testUpdate() {
		controller.params.id = 1
		controller.params.number = 10000
		controller.params.name = 'Service 3'
		controller.params.quantity = 35
		controller.params.unitPrice = 4.7d
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Work.count()
		SeqNumber seqNumber = SeqNumber.findByControllerName('work')
		assertEquals 10002, seqNumber.nextNumber
		def p = Work.get(1)
		assertEquals 10000, p.number
		assertEquals 'Service 3', p.name
		assertEquals 35, p.quantity
		assertEquals 4.7d, p.unitPrice

		controller.params.name = ''
		def map = controller.update()
		assertEquals 2, Work.count()
		assertEquals 'blank', map.workInstance.errors['name']

		controller.params.id = 10
		controller.update()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}

	void testDelete() {
		controller.params.id = 1
		controller.delete()
		assertEquals 1, Work.count()
		assertNull Work.get(1)
		assertNotNull Work.get(2)

		controller.params.id = 10
		controller.delete()
		assertEquals 'list', controller.redirectArgs['action']
		assertEquals ERROR_MSG, controller.flash['message']
	}
}
