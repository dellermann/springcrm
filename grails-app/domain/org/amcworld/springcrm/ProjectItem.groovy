/*
 * ProjectItem.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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
 * @version 1.0
 */
class ProjectItem {

    //-- Class variables ------------------------

    static constraints = {
        project()
        phase(nullable: false)
        controller(nullable: false, blank: false)
        itemId(nullable: false)
        title(nullable: true)
    }
    static belongsTo = [project: Project]


    //-- Instance variables ---------------------

    ProjectPhase phase
    String controller
    long itemId
    String title


    //-- Public methods -------------------------

    boolean equals(Object o) {
        if (o instanceof ProjectItem) {
            def item = (ProjectItem) o
            return (o.project == project) && (o.phase == phase) &&
                (o.controller == controller) && (o.itemId == itemId)
        } else {
            return false
        }
    }

    int hashCode() {
        return "${project.ident()}-${phase}-${controller}-${itemId}".hashCode()
    }

    String toString() {
        return title ?: ''
    }
}
