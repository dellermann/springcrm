/*
 * Note.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * @version 1.3
 */
class Note {

    //-- Class variables ------------------------

    static constraints = {
        number(unique: true, widget: 'autonumber')
		title(blank: false, maxSize: 200)
		content(blank: true, widget: 'textarea')
		organization(nullable: true)
		person(nullable: true)
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [ organization: Organization, person: Person ]
	static mapping = {
		sort 'title'
		content type: 'text'
        title index: 'title'
	}
	static searchable = true
	static transients = [ 'fullNumber' ]


    //-- Instance variables ---------------------

	def seqNumberService

	int number
	String title
	String content
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

	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Note) {
            return obj.id == id
        } else {
            return false
        }
    }

    @Override
    public int hashCode() {
        (id ?: 0i) as int
    }

    @Override
	String toString() {
		title ?: ''
	}

	def beforeInsert() {
		if (number == 0) {
			number = seqNumberService.nextNumber(getClass())
		}
	}
}
