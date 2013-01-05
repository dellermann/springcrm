/*
 * ProjectDocument.groovy
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
 * The class {@code ProjectDocument} represents a document which is associated
 * to a project.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.2
 */
class ProjectDocument {

    //-- Class variables ------------------------

    static constraints = {
        phase()
        path(blank: false)
        title(blank: false)
    }
    static mapping = {
        path index: 'path'
    }
    static belongsTo = [project: Project]


    //-- Instance variables ---------------------

    ProjectPhase phase
    String path
    String title


    //-- Public methods -------------------------

    boolean equals(Object o) {
        if (o instanceof ProjectDocument) {
            return (o.project == project) && (o.phase == phase) && (o.path == path)
        } else {
            return false
        }
    }

    int hashCode() {
        return "${project.ident()}-${phase}-${path}".hashCode()
    }

    String toString() {
        return title
    }
}
