/*
 * Note.groovy
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

import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId


/**
 * The class {@code Note} represents a note.
 *
 * @author	Daniel Ellermann
 * @author	Philip Drozd
 * @version 3.0
 */
@EqualsAndHashCode(includes = ['id'])
class Note implements NumberedDomain {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS =
        ['title', 'content'].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
        number unique: true, widget: 'autonumber'
        title blank: false, maxSize: 200
        content blank: true, widget: 'textarea'
        organization nullable: true
        person nullable: true
    }
    static belongsTo = [organization: Organization, person: Person]
    static mapping = {
        sort 'title'
        content type: 'text'
        title index: true
    }


    //-- Fields ---------------------------------

    /**
     * The content of the note.
     */
    String content

    /**
     * The timestamp of the creation of the record.
     */
    Date dateCreated

    /**
     * The ID of the note.
     */
    ObjectId id

    /**
     * The timestamp of the last update of the record.
     */
    Date lastUpdated

    /**
     * The organization associated to the note.
     */
    Organization organization

    /**
     * The person associated to the note.
     */
    Person person

    /**
     * The title of the note.
     */
    String title


    //-- Constructors ---------------------------

    Note() {}

    Note(Note n) {
        title = n.title
        content = n.content
        organization = n.organization
        person = n.person
    }


    //-- Public methods -------------------------

    @Override
    String toString() {
        title ?: ''
    }
}
