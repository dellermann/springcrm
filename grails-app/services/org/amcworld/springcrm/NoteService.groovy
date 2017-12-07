/*
 * NoteService.groovy
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

import grails.gorm.services.Service


/**
 * The interface {@code NoteService} represents a service which allows access
 * to notes.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(Note)
interface NoteService {

    //-- Public methods -------------------------

    /**
     * Counts all notes.
     *
     * @return  the number of all notes
     */
    int count()

    /**
     * Counts the notes which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of notes
     */
    int countByOrganization(Organization organization)

    /**
     * Counts the notes which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of notes
     */
    int countByPerson(Person person)

    /**
     * Counts the notes with a title alphabetically before the given title.
     *
     * @param title the given title
     * @return      the number of notes
     */
    int countByTitleLessThan(String title)

    /**
     * Counts the notes with a title matching the given {@code LIKE} pattern.
     *
     * @param title the given title pattern
     * @return      the number of notes
     */
    int countByTitleLike(String title)

    /**
     * Deletes the note with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the notes which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments used for retrieval (sort, order etc.)
     * @return              a list of notes
     */
    List<Note> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds the notes which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments used for retrieval (sort, order etc.)
     * @return          a list of notes
     */
    List<Note> findAllByPerson(Person person, Map args)

    /**
     * Finds the notes with a title matching the given {@code LIKE} pattern.
     *
     * @param title the given title pattern
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of notes
     */
    List<Note> findAllByTitleLike(String title, Map args)

    /**
     * Gets the note with the given ID.
     *
     * @param id    the given ID
     * @return      the note or {@code null} if no such note with the given ID
     *              exists
     */
    Note get(Serializable id)

    /**
     * Gets a list of all notes.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of notes
     */
    List<Note> list(Map args)

    /**
     * Saves the given note.
     *
     * @param instance  the given note
     * @return          the saved note
     */
    Note save(Note instance)
}
