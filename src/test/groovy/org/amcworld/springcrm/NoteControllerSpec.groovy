/*
 * NoteControllerSpec.groovy
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

import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationErrors
import grails.validation.ValidationException
import grails.web.servlet.mvc.GrailsParameterMap
import org.bson.types.ObjectId
import spock.lang.Specification


class NoteControllerSpec extends Specification
    implements ControllerUnitTest<NoteController>
{

    //-- Feature methods ------------------------

    void 'The copy action returns the correct model and create view'() {
        given: 'an instance'
        Note note = instance

        when: 'the action is executed'
        controller.copy note

        then: 'the model is correctly created'
        null != model.note
        note.content == model.note.content
        0i == model.note.number
        note.title == model.note.title

        and: 'the view is correctly set'
        'create' == view
    }

    void 'The create action returns the correct model'() {
        when: 'the action is executed'
        controller.create()

        then: 'the model is correctly created'
        null != model.note

        when: 'the action is executed with parameters'
        populateValidParams params
        controller.create()

        then: 'the model is correctly created'
        null != model.note
        params.content == model.note.content
        params.title == model.note.title
    }

    void 'The delete action deletes an instance if it exists'() {
        given: 'an instance'
        Note note = instance
        note.id = new ObjectId()

        and: 'a service instance'
        NoteService service = Mock()
        service.delete(note.id) >> note
        controller.noteService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete null

        then: 'a 404 error is returned'
        0 * service.delete(_)
        '/note/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for a non-existing instance'
        response.reset()
        controller.delete new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.delete(note.id)
        '/note/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called for an existing instance'
        response.reset()
        controller.delete note.id.toString()

        then: 'the instance is deleted'
        1 * service.delete(note.id) >> note
        '/note/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is called with an LDAP service'
        response.reset()
        controller.delete note.id.toString()

        then: 'the instance is deleted in LDAP, too'
        1 * service.delete(note.id) >> note
        '/note/index' == response.redirectedUrl
        null != flash.message
    }

    void 'The edit action returns the correct model'() {
        given: 'an instance'
        Note note = instance
        note.id = new ObjectId()

        and: 'a service instance'
        NoteService service = Mock()
        controller.noteService = service

        when: 'the action is executed with a null domain'
        controller.edit null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.edit new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(note.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.edit note.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(note.id) >> note
        note == model.note
    }

    void 'The index action returns the correct model'() {
        given: 'a list of notes'
        def list = [
            new Note(title: 'Note 1'),
            new Note(title: 'Note 2'),
            new Note(title: 'Note 3'),
        ]

        and: 'a service instance'
        NoteService service = Mock()
        1 * service.count() >> list.size()
        1 * service.list(getParameterMap(max: 10, offset: 20)) >> list
        controller.noteService = service

        when: 'the action is executed'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.noteList.size()
        list == (List) model.noteList
        list.size() == model.noteCount
    }

    void 'The index action with letter returns the correct model'() {
        given: 'a list of notes'
        def list = [
            new Note(title: 'Note 1'),
            new Note(title: 'Note 2'),
            new Note(title: 'Note 3'),
        ]

        and: 'a service instance'
        NoteService service = Mock()
        2 * service.countByTitleLessThan('E') >>> [45, 40]
        2 * service.count() >> list.size()
        2 * service.list(getParameterMap(
            letter: 'E', max: 10, offset: 40, sort: 'title'
        )) >> list
        controller.noteService = service

        when: 'the action is executed without search parameter'
        params.letter = 'E'
        params.max = 10
        params.offset = 20
        controller.index()

        then: 'the model is correct'
        list.size() == model.noteList.size()
        list == (List) model.noteList
        list.size() == model.noteCount

        when: 'the action is executed with search parameter'
        response.reset()
        params.letter = 'E'
        params.max = 10
        params.offset = 20
        params.search = 'foobar'
        controller.index()

        then: 'the model is correct'
        list.size() == model.noteList.size()
        list == (List) model.noteList
        list.size() == model.noteCount
    }

    void 'The index action with search term returns the correct model'() {
        given: 'a list of notes'
        def list = [
            new Note(title: 'Note 1'),
            new Note(title: 'Note 2'),
            new Note(title: 'Note 3'),
        ]

        and: 'a service instance'
        NoteService service = Mock()
        1 * service.countByTitleLike('%ote%') >> list.size()
        1 * service.findAllByTitleLike(
            '%ote%', getParameterMap(max: 10, offset: 20, search: 'ote')
        ) >> list
        controller.noteService = service

        when: 'the action is executed'
        params.max = 10
        params.offset = 20
        params.search = 'ote'
        controller.index()

        then: 'the model is correct'
        list.size() == model.noteList.size()
        list == (List) model.noteList
        list.size() == model.noteCount
    }

    void 'The listEmbedded action returns the correct model'() {
        given: 'a list of notes'
        def list = [
            new Note(title: 'Note 1'),
            new Note(title: 'Note 2'),
            new Note(title: 'Note 3'),
        ]

        and: 'an organization'
        Organization org = new Organization(name: 'My organization ltd.')
        org.id = new ObjectId()

        and: 'a person'
        Person person = new Person(firstName: 'John', lastName: 'Doe')
        person.id = new ObjectId()

        and: 'a service instance'
        NoteService service = Mock()
        controller.noteService = service

        and: 'an organization service instance'
        OrganizationService organizationService = Mock()
        controller.organizationService = organizationService

        and: 'a person service instance'
        PersonService personService = Mock()
        controller.personService = personService

        when: 'the action is called for a null instance'
        controller.listEmbedded null, null

        then: 'a 404 error is returned'
        0 * organizationService.get(_)
        0 * personService.get(_)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for a non-existing organization'
        response.reset()
        controller.listEmbedded new ObjectId().toString(), null

        then: 'a 404 error is returned'
        0 * organizationService.get(org.id)
        0 * personService.get(_)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for an existing organization'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded org.id.toString(), null

        then: 'the model is correct'
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        list == (List) model.noteList
        list.size() == model.noteCount
        [organization: org.id.toString()] == (Map) model.linkParams

        when: 'the action is called for a non-existing person'
        response.reset()
        controller.listEmbedded null, new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * organizationService.get(_)
        0 * personService.get(person.id)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for an existing person'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded null, person.id.toString()

        then: 'the model is correct'
        0 * organizationService.get(_)
        1 * personService.get(person.id) >> person
        0 * service.findAllByOrganization(_)
        0 * service.countByOrganization(_)
        1 * service.findAllByPerson(
            person, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByPerson(person) >> list.size()
        list == (List) model.noteList
        list.size() == model.noteCount
        [person: person.id.toString()] == (Map) model.linkParams

        when: 'the action is called for a non-existing organization and a person'
        response.reset()
        controller.listEmbedded new ObjectId().toString(), person.id.toString()

        then: 'a 404 error is returned'
        0 * organizationService.get(org.id)
        0 * personService.get(_)
        0 * service.findAllByOrganization(_, _)
        0 * service.countByOrganization(_)
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        404 == response.status

        when: 'the action is called for an existing organization and person'
        response.reset()
        params.max = 20
        params.offset = 40
        controller.listEmbedded org.id.toString(), person.id.toString()

        then: 'the model is correct'
        1 * organizationService.get(org.id) >> org
        0 * personService.get(_)
        1 * service.findAllByOrganization(
            org, getParameterMap(max: 20, offset: 40)
        ) >> list
        1 * service.countByOrganization(org) >> list.size()
        0 * service.findAllByPerson(_, _)
        0 * service.countByPerson(_)
        list == (List) model.noteList
        list.size() == model.noteCount
        [organization: org.id.toString()] == (Map) model.linkParams
    }

    void 'The save action correctly persists an instance'() {
        given: 'an instance'
        Note note = instance
        note.id = new ObjectId()

        and: 'a service instance'
        NoteService service = Mock()
        controller.noteService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save null

        then: 'a 404 error is returned'
        0 * service.save(_)
        '/note/index' == response.redirectedUrl
        null != flash.message

        when: 'the action is executed with an invalid instance'
        response.reset()
        controller.save note

        then: 'the create view is rendered again with the correct model'
        1 * service.save(note) >> {
            throw new ValidationException('', new ValidationErrors(note))
        }
        note == model.note
        'create' == view

        when: 'the action is executed with a valid instance'
        response.reset()
        controller.save note

        then: 'a redirect is issued to the show action'
        1 * service.save(note) >> note
        '/note/show/' + note.id == response.redirectedUrl
        null != controller.flash.message
    }

    void 'The show action returns the correct model'() {
        given: 'an instance'
        Note note = instance
        note.id = new ObjectId()

        and: 'a service instance'
        NoteService service = Mock()
        controller.noteService = service

        when: 'the action is executed with a null domain'
        controller.show null

        then: 'a 404 error is returned'
        0 * service.get(_)
        404 == response.status

        when: 'the action is executed with a non-existing domain'
        response.reset()
        controller.show new ObjectId().toString()

        then: 'a 404 error is returned'
        0 * service.get(note.id)
        404 == response.status

        when: 'the action is executed with an existing domain'
        response.reset()
        controller.show note.id.toString()

        then: 'a model is populated containing the domain instance'
        1 * service.get(note.id) >> note
        note == model.note
    }

    void 'The update action performs an update on a valid domain instance'() {
        given: 'an instance'
        Note note = instance
        note.id = new ObjectId()

        and: 'a service instance'
        NoteService service = Mock()
        controller.noteService = service

        when: 'the action is called for a null instance'
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update null

        then: 'a 404 error is returned'
        0 * service.save(_)
        '/note/index' == response.redirectedUrl
        null != flash.message

        when: 'an invalid domain instance is passed to the action'
        response.reset()
        controller.update note

        then: 'the edit view is rendered again with the invalid instance'
        1 * service.save(note) >> {
            throw new ValidationException('', new ValidationErrors(note))
        }
        note == model.note
        'edit' == view

        when: 'a valid domain instance is passed to the action'
        response.reset()
        controller.update note

        then: 'a redirect is issued to the show action'
        1 * service.save(note) >> note
        '/note/show/' + note.id == response.redirectedUrl
        null != controller.flash.message
    }


    //-- Non-public methods -------------------------

    private static Note getInstance() {
        Map properties = [: ]
        populateValidParams properties

        new Note(properties)
    }

    private GrailsParameterMap getParameterMap(Map map) {
        new GrailsParameterMap(map, request)
    }

    private static void populateValidParams(Map params, Integer number = null) {
        assert params != null

        params.content = 'This is my first note.'
        params.number = number
        params.title = 'My first note'
    }
}
