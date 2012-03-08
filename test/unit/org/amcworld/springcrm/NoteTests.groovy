package org.amcworld.springcrm

import grails.test.*

class NoteTests extends GrailsUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConstruct() {
        def note = new Note(number:10000, title:'Test note', content:'This is a test.')
		assertEquals 10000, note.number
        assertEquals 'Test note', note.title
        assertEquals 'This is a test.', note.content
        assertNull note.organization
		assertNull note.person
    }

	void testOrganization() {
		def note = new Note()
		note.organization = new Organization(name:'AMC World Technologies')
		assertNotNull note.organization
		assertEquals 'AMC World Technologies', note.organization.name
	}

	void testPerson() {
		def note = new Note()
		note.person = new Person(firstName:'Daniel', lastName:'Ellermann')
		assertNotNull note.person
		assertEquals 'Daniel', note.person.firstName
		assertEquals 'Ellermann', note.person.lastName
	}

	void testBlankConstraints() {
		mockForConstraintsTests Note
		def validationFields = ['title', 'content']
		def note = new Note()
		assertFalse note.validate(validationFields)
		assertEquals 'nullable', note.errors['title']
		assertEquals 'nullable', note.errors['content']

		note = new Note(title:'')
		assertFalse note.validate(['title'])
		assertEquals 'blank', note.errors['title']

		note = new Note(title:'Test note', content:'This is a test.')
		assertTrue note.validate(validationFields)
	}

	void testUniqueConstraints() {
		def note1 = new Note(number:10000, title:'Note 1')
		def note2 = new Note(number:10010, title:'Note 2')
		mockDomain(Note, [note1, note2])

		def badNote = new Note(number:10000, title:'Note 3')
		assertNull badNote.save()
		assertEquals 2, Note.count()
		assertEquals 'unique', badNote.errors['number']
		assertNull badNote.errors['title']

		def goodNote = new Note(number:10020, title:'Note 4', content:'Test')
		assertNotNull goodNote.save()
		assertEquals 3, Note.count()
	}

	void testSizeConstraints() {
		mockForConstraintsTests Note
		def validationFields = ['title']
		String s = '1234567890' * 21
		def note = new Note(title:s)
		assertFalse note.validate(validationFields)
		assertEquals 'maxSize', note.errors['title']

		s = '1234567890' * 20
		note = new Note(title:s)
		assertTrue note.validate(validationFields)

		s = 'Test note'
		note = new Note(title:s)
		assertTrue note.validate(validationFields)
	}

	void testFullNumber() {
//		def seqNumber = new SeqNumber(controllerName:'note', nextNumber:10002, prefix:'N', suffix:'')
//		mockDomain(SeqNumber, [seqNumber])

		def note = new Note(number:10000)
		note.seqNumberService = new SeqNumberService()
		assertEquals 'N-10000', note.fullNumber
		note = new Note(number:10)
		note.seqNumberService = new SeqNumberService()
		assertEquals 'N-10', note.fullNumber
		note = new Note(number:100000)
		note.seqNumberService = new SeqNumberService()
		assertEquals 'N-100000', note.fullNumber
	}

	void testToString() {
        def note = new Note(title:'Test note', content:'This is a test.')
		assertToString note, 'Test note'

		note = new Note()
		assertToString note, ''
	}
}
