/*
 * Note.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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


/**
 * The class {@code Note} represents a note.
 *
 * @author	Daniel Ellermann
 * @author	Philip Drozd
 * @version 2.1
 */
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
        title index: 'title'
    }
    static transients = ['fullNumber']


    //-- Fields ---------------------------------

    String title
    String content
    Organization organization
    Person person
    Date dateCreated
    Date lastUpdated


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
    boolean equals(Object obj) {
        obj instanceof Note && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        title ?: ''
    }
}
