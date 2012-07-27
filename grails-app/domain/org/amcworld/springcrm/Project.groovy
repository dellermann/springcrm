/*
 * Project.groovy
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
 * The class {@code Project} represents a project with several project phases
 * and associated project items such as quotes, sales orders, invoices,
 * documents etc.
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.0
 */
class Project {

    //-- Class variables ------------------------

    static constraints = {
        number(unique: true, widget: 'autonumber')
        title(nullable: false, blank: false)
        description(nullable: true, blank: true, widget: 'textarea')
        organization()
        person(nullable: true)
        phase(nullable: false)
        status(nullable: false)
        dateCreated()
        lastUpdated()
    }
    static belongsTo = [organization: Organization, person: Person]
    static hasMany = [items: ProjectItem, documents: ProjectDocument]
    static mapping = {
        sort 'title'
        description type: 'text'
    }
    static searchable = true
    static transients = ['fullNumber']


    //-- Instance variables ---------------------

    def seqNumberService

    int number
    String title
    String description
    ProjectPhase phase = ProjectPhase.planning
    ProjectStatus status
    Date dateCreated
    Date lastUpdated


    //-- Constructors ---------------------------

    Project() {}

    Project(Project p) {
        title = p.title
        description = p.description
        organization = p.organization
        person = p.person
    }


    //-- Public methods -------------------------

    def beforeInsert() {
        if (number == 0) {
            number = seqNumberService.nextNumber(getClass())
        }
    }

    String getFullNumber() {
        return seqNumberService.format(getClass(), number)
    }

    String toString() {
        return title ?: ''
    }
}


/**
 * The enumeration {@code ProjectPhase} represents the phases of projects.
 *
 * @author  Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
enum ProjectPhase {
    planning,
    costing,
    quote,
    ordering,
    fulfillmentConfirmation,
    implementation,
    acceptance,
    accounting,
    dunning,
    maintenance
}
