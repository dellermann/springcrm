/*
 * ProjectDocument.groovy
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
 * The class {@code ProjectDocument} represents a document which is associated
 * to a project.
 *
 * @author	Daniel Ellermann
 * @version 2.1
 * @since   1.2
 */
class ProjectDocument {

    //-- Class fields ---------------------------

    static belongsTo = [project: Project]
    static constraints = {
        path blank: false
        title blank: false
    }
    static mapping = {
        path index: 'path'
    }


    //-- Fields ---------------------------------

    ProjectPhase phase
    String path
    String title


    //-- Public methods -------------------------

    boolean equals(Object o) {
        o instanceof ProjectDocument && o.project == project &&
            o.phase == phase && o.path == path
    }

    int hashCode() {
        "${project.ident()}-${phase}-${path}".hashCode()
    }

    String toString() {
        title
    }
}
