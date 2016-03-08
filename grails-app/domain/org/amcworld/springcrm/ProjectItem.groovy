/*
 * ProjectItem.groovy
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
 * The class {@code ProjectItem} represents an item, such a task, call, notes,
 * quote, invoice etc. which belongs to a phase of a particular project.
 *
 * @author	Daniel Ellermann
 * @version 2.1
 * @since   1.3
 */
class ProjectItem {

    //-- Class fields ---------------------------

    static constraints = {
        controller blank: false
        title nullable: true
    }
    static belongsTo = [project: Project]


    //-- Fields ---------------------------------

    ProjectPhase phase
    String controller
    long itemId
    String title


    //-- Public methods -------------------------

    boolean equals(Object o) {
        o instanceof ProjectItem && o.project == project &&
            o.phase == phase && o.controller == controller &&
            o.itemId == itemId
    }

    int hashCode() {
        "${project?.ident()}-${phase}-${controller}-${itemId}".hashCode()
    }

    String toString() {
        title ?: ''
    }
}
