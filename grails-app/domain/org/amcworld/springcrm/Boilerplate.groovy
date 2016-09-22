/*
 * Boilerplate.groovy
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
 * The class {@code Boilerplate} represents boilerplater which may be used in
 * text areas.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
class Boilerplate {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS =
        ['name', 'content'].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
        name blank: false
        content widget: 'textarea'
    }
    static mapping = {
        sort name: 'asc'
        name index: 'name'
        content type: 'text'
    }


    //-- Fields ---------------------------------

    /**
     * The name of the boilerplate.
     */
    String name

    /**
     * The text content of the boilerplate.
     */
    String content

    /**
     * The timestamp when the boilerplate has been created.
     */
    Date dateCreated

    /**
     * The timestamp when the boilerplate has been modified.
     */
    Date lastUpdated


    //-- Constructors ---------------------------

    /**
     * Creates an empty boilerplate.
     */
    Boilerplate() {}

    /**
     * Creates a copy of the given boilerplate.
     *
     * @param b the given boilerplate
     */
    Boilerplate(Boilerplate b) {
        name = b.name
        content = b.content
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Boilerplate && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        name ?: ''
    }
}
