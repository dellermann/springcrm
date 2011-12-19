package org.amcworld.springcrm

import grails.test.*
import grails.test.mixin.*

@TestFor(CallController)
@Mock([Call, Organization, Person])
class CallControllerTests {

    void setUp() {
        def seqNumberService = mockFor(SeqNumberService)
        seqNumberService.demand.nextNumber(0..10) { cls -> 1 }

        def org = new Organization(name: 'AMC World', phone: '+49 30 83214750')
        org.seqNumberService = seqNumberService.createMock()
        org.save(validate: false)

        def p = new Person(
            firstName: 'Daniel', lastName: 'Ellermann', phone: '+49 30 1234567'
        )
        p.seqNumberService = seqNumberService.createMock()
        p.save(validate: false)

        new Call(subject: 'Call 1', organization: org).save(validate: false)
        new Call(subject: 'Call 2', organization: org, person: p).
            save(validate: false)

        Call.metaClass.index = { -> }
		Call.metaClass.reindex = { -> }
	}

    void testIndex() {
		controller.index()
		assert '/call/list' == response.redirectedUrl
    }

	void testList() {
        def model = controller.list()
		assert 2 == model.callInstanceTotal
		assert 2 == model.callInstanceList.size()
		assert 'Call 1' == model.callInstanceList[0].subject
		assert 'Call 2' == model.callInstanceList[1].subject
	}

    void testListEmbeddedByOrganization() {
        params.organization = 1
        def model = controller.listEmbedded()
        assert 2 == model.callInstanceTotal
        assert 2 == model.callInstanceList.size()
        assert 'Call 1' == model.callInstanceList[0].subject
        assert 'Call 2' == model.callInstanceList[1].subject
    }

    void testListEmbeddedByPerson() {
        params.person = 1
        def model = controller.listEmbedded()
        assert 1 == model.callInstanceTotal
        assert 1 == model.callInstanceList.size()
        assert 'Call 2' == model.callInstanceList[0].subject
    }

	void testCreate() {
		def model = controller.create()
		assert null != model.callInstance
		assert null == model.callInstance.subject
	}

    void testCreateWithPerson() {
        def s = '1234567890'
        params.person = new Person(
            number:10000, firstName:'Daniel', lastName:'Ellermann',
            phone:s
        )
        def model = controller.create()
        assert null != model.callInstance
        assert null == model.callInstance.subject
        assert s == model.callInstance.phone
    }

    void testCreateWithOrganization() {
        def s = '1234567890'
        params.organization = new Organization(
            number:10000, name:'AMC World Technologies GmbH', phone:s
        )
        def model = controller.create()
        assert null != model.callInstance
        assert null == model.callInstance.subject
        assert s == model.callInstance.phone
    }

    void testCopy() {
        params.id = 1
        controller.copy()
        assert '/call/create' == view
        assert 'Call 1' == model.callInstance.subject
    }

    void testCopyNonExisting() {
        params.id = 10
        controller.copy()
        assert 'default.not.found.message' == flash.message
        assert '/call/list' == response.redirectedUrl
    }

	void testSaveSuccessfully() {
		params.subject = 'Call 1'
		params.notes = 'Test'
		params.start = new Date()
		params.status = 'planned'
		params.type = 'incoming'
		controller.save()
		assert 3 == Call.count()
		assert '/call/show/3' == response.redirectedUrl
	}

	void testSaveFailed() {
		params.subject = ''
		params.notes = 'Test'
		params.start = new Date()
		params.status = 'foo'
		params.type = 'bar'
		controller.save()
        assert '/call/create' == view
		assert 2 == Call.count()
		assert 'call.subject.blank' in model.callInstance.errors['subject'].codes
		assert 'call.status.not.inList' in model.callInstance.errors['status'].codes
		assert 'call.type.not.inList' in model.callInstance.errors['type'].codes
	}

	void testShow() {
		params.id = 2
		def model = controller.show()
		assert 'Call 2' == model.callInstance.subject
	}

    void testShowNonExisting() {
        params.id = 10
        controller.show()
        assert '/call/list' == response.redirectedUrl
        assert 'default.not.found.message' == flash.message
    }

	void testEdit() {
		params.id = 1
		def model = controller.edit()
		assert 'Call 1' == model.callInstance.subject
	}

    void testEditNonExisting() {
        params.id = 10
        controller.edit()
        assert '/call/list' == response.redirectedUrl
        assert 'default.not.found.message' == flash.message
    }

	void testUpdate() {
		params.id = 1
		params.subject = 'Call 3'
		params.notes = 'Test'
		params.start = new Date()
		params.status = 'planned'
		params.type = 'incoming'
		controller.update()
		assert '/call/show/1' == response.redirectedUrl
		assert 2 == Call.count()
		def call = Call.get(1)
		assert 'Call 3' == call.subject
		assert 'Test' == call.notes
		assert 'planned' == call.status
	}

    void testUpdateFailed() {
        params.id = 1
        params.subject = ''
        params.notes = 'Test'
        params.start = new Date()
        params.status = 'planned'
        params.type = 'incoming'
        controller.update()
        assert '/call/edit' == view
        assert 2 == Call.count()
        assert 'call.subject.blank' in model.callInstance.errors['subject'].codes
    }

    void testUpdateNonExisting() {
        params.id = 10
        controller.update()
        assert '/call/list' == response.redirectedUrl
        assert 'default.not.found.message' == flash.message
    }

    void testDeleteUnconfirmed() {
        params.id = 1

        controller.delete()
        assert 2 == Call.count()
        assert '/call/list' == response.redirectedUrl
        assert 'default.not.found.message' == flash.message
    }

    void testDeleteConfirmed() {
        params.id = 1
        params.confirmed = true
        controller.delete()
        assert '/call/list' == response.redirectedUrl
        assert 1 == Call.count()
        assert null == Call.get(1)
        assert null != Call.get(2)
    }

	void testDeleteNonExisting() {
		params.id = 10
        params.confirmed = true
		controller.delete()
        assert '/call/list' == response.redirectedUrl
		assert 'default.not.found.message' == flash.message
	}
}
