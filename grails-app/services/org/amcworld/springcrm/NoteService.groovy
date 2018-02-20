/*
 * NoteService.groovy
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

import grails.gorm.services.Service
import org.bson.types.ObjectId


/**
 * The class {@code NoteService} contains methods for working with notes.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(Note)
interface NoteService {

    //-- Public methods -----------------------------

    /**
     * Counts all notes.
     *
     * @return  the number of notes
     */
    int count()

    /**
     * Counts all notes which belong to the given organization.
     *
     * @param organization  the given organization
     * @return              the number of matching notes
     */
    int countByOrganization(Organization organization)

    /**
     * Counts all notes which belong to the given person.
     *
     * @param person    the given person
     * @return          the number of matching notes
     */
    int countByPerson(Person person)

    /**
     * Counts the notes which title matches the given pattern.
     *
     * @param title the title pattern
     * @return      the number of matching notes
     */
    int countByTitleLike(String title)

    /**
     * Counts all notes that title is alphabetically before the given title.
     *
     * @param title the given title
     * @return      the number of matching notes
     */
    int countByTitleLessThan(String title)

    /**
     * Deletes the note with the given ID.
     *
     * @param id    the given ID
     * @return      the deleted note or {@code null} if no note has been deleted
     */
    Note delete(ObjectId id)

    /**
     * Finds all notes which belong to the given organization.
     *
     * @param organization  the given organization
     * @param args          any arguments for retrieving the notes
     * @return              a list of matching notes
     */
    List<Note> findAllByOrganization(Organization organization, Map args)

    /**
     * Finds all notes which belong to the given person.
     *
     * @param person    the given person
     * @param args      any arguments for retrieving the notes
     * @return          a list of matching notes
     */
    List<Note> findAllByPerson(Person person, Map args)

    /**
     * Finds all notes which title matches the given pattern.
     *
     * @param title the title pattern
     * @param args  any arguments for retrieving the notes
     * @return      a list of matching notes
     */
    List<Note> findAllByTitleLike(String title, Map args)

    /**
     * Gets the note with the given ID.
     *
     * @param id    the given ID
     * @return      the note or {@code null} if no such note has been found
     */
    Note get(ObjectId id)

    /**
     * Lists all notes.
     *
     * @param args  any arguments for retrieving the notes
     * @return      the notes
     */
    List<Note> list(Map args)

    /**
     * Creates or updates the given note.
     *
     * @param note  the given note
     * @return      the saved note or {@code null} if an error occurred
     */
    Note save(Note note)
}
