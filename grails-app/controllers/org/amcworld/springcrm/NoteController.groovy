/*
 * NoteController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import groovy.transform.CompileStatic


/**
 * The class {@code NoteController} contains actions which manage notes.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
@CompileStatic
class NoteController extends GenericDomainController<Note> {

    //-- Fields ---------------------------------

    NoteService noteService


    //-- Constructors ---------------------------

    NoteController() {
        super(Note)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    /**
     * Loads all notes which are defined by the request parameters and shows
     * them in the index view.
     *
     * @return  the model for the view
     */
    def index() {
        if (params.letter) {
            int max = params.int('max')
            int num = noteService.countByTitleLessThan(params.letter.toString())
            params.sort = 'title'
            params.offset = Math.floor(num / max) * max
            params.search = null
        }

        List<Note> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = noteService.findAllByTitleLike(searchFilter, params)
            count = noteService.countByTitleLike(searchFilter)
        } else {
            list = noteService.list(params)
            count = noteService.count()
        }

        getIndexModel list, count
    }

    /**
     * Loads all notes which are defined by the request parameters and shows
     * them in the embedded list view.
     *
     * @param organization  an optional organization the notes must belong to;
     *                      may be {@code null}
     * @param person        an optional person the notes must belong to; may be
     *                      {@code null}
     * @return              the model for the view
     */
    def listEmbedded(Long organization, Long person) {
        List<Note> list = null
        int count = 0
        Map linkParams = null

        if (organization) {
            Organization organizationInstance = Organization.get(organization)
            list = noteService.findAllByOrganization(
                organizationInstance, params
            )
            count = noteService.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            Person personInstance = Person.get(person)
            list = noteService.findAllByPerson(personInstance, params)
            count = noteService.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }

        getListEmbeddedModel list, count, linkParams
    }

    def save() {
        super.save()
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods ---------------------

    @Override
    protected void lowLevelDelete(Note instance) {
        noteService.delete instance.id
    }

    @Override
    protected Note lowLevelGet(Long id) {
        noteService.get id
    }

    @Override
    protected Note lowLevelSave(Note instance) {
        noteService.save instance
    }
}
