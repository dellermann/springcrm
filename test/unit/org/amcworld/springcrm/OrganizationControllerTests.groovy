package org.amcworld.springcrm

import grails.test.*

class OrganizationControllerTests extends ControllerUnitTestCase {
	
	private static final String ERROR_MSG = 'error message'

    protected void setUp() {
        super.setUp()
		controller.metaClass.message = { Map map -> return ERROR_MSG }
		Organization.metaClass.static.executeQuery = { String sql -> return [10002] }

		def org1 = new Organization(number:10000, name:'Organization 1')
		def org2 = new Organization(number:10001, name:'Organization 2')
		mockDomain(Organization, [org1, org2])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndex() {
		controller.index()
		assertEquals 'list', controller.redirectArgs['action']
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
		assertEquals 10002, map.organizationInstance.number
		assertNull map.organizationInstance.name
	}

	void testSaveSuccessfully() {
		controller.params.number = 10010
		controller.params.name = 'Organization 3'
		controller.params.phone = '030 1234567'
		controller.params.email = 'info@example.com'
		controller.save()
		assertEquals 3, Organization.count()
		assertEquals 'show', controller.redirectArgs['action']
	}
	
	void testSaveFailed() {
		controller.params.number = 10001
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
		controller.params.number = 10000
		controller.params.name = 'Organization 3'
		controller.params.phone = '030 7654321'
		controller.params.email = 'info@example.com'
		controller.update()
		assertEquals 'show', controller.redirectArgs['action']
		assertEquals 2, Organization.count()
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
