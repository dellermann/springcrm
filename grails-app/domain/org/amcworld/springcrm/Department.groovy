/*
 * Department.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code Department} represents a department of a company.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class Department implements GormEntity<Department> {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS =
        ['name', 'costCenter'].asImmutable()


    //-- Class fields ---------------------------

    static constraints = {
        name blank: false, maxSize: 50
        costCenter nullable: true, maxSize: 40
        manager nullable: true
    }
    static mapping = {
        sort 'name'
        name index: 'name'
    }


    //-- Fields ---------------------------------

    /**
     * The name of this department.
     */
    String name

    /**
     * The cost center this department belongs to.
     */
    String costCenter

    /**
     * The manager of this department.
     */
    Staff manager

    /**
     * The timestamp when this department has been created.
     */
    Date dateCreated

    /**
     * The timestamp when this department has been modified.
     */
    Date lastUpdated


    //-- Constructors -------------------------------

    /**
     * Creates an empty department.
     */
    Department() {}

    /**
     * Creates a copy of the given department.
     *
     * @param department    the given department
     */
    Department(Department department) {
        name = department.name
        costCenter = department.costCenter
        manager = department.manager
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Department && obj.id == id
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
