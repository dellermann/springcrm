/*
 * DomainService.groovy
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

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainBinder
import org.codehaus.groovy.grails.orm.hibernate.cfg.Mapping


class DomainService {

    //-- Instance variables ---------------------

    GrailsApplication grailsApplication
    UserService userService


    //-- Public methods -------------------------

    /**
     * Loads the next record after the one with the given ID.
     *
     * @param clazz the given type of record (domain model class)
     * @param id    the ID of the current record
     * @return      the next record; {@code null} if no such record exists
     */
    def nextRecord(Class<?> clazz, long id) {
        browseRecord clazz, id, true
    }

    /**
     * Loads the previous record before the one with the given ID.
     *
     * @param clazz the given type of record (domain model class)
     * @param id    the ID of the current record
     * @return      the previous record; {@code null} if no such record exists
     */
    def previousRecord(Class<?> clazz, long id) {
        browseRecord clazz, id, false
    }


    //-- Non-public methods ---------------------

    /**
     * Browses the next or previous record of the given type after or before
     * the record with the given ID.
     *
     * @param clazz the given type of record (domain model class)
     * @param id    the ID of the current record
     * @param next  if {@code true} the next record is selected; {@code false}
     *              otherwise
     * @return      the next or previous record; {@code null} if no such
     *              record exists
     */
    protected def browseRecord(Class<?> clazz, long id, boolean next) {
        def instance = clazz.get(id)
        if (!instance) return null

        Mapping mapping = GrailsDomainBinder.getMapping(clazz)
        String defProperty = mapping.sort ?: 'id'
        String property = userService.getSortProperty(clazz, defProperty)
        String defOrder = mapping.order ?: 'asc'
        String order = userService.getSortOrder(clazz, defOrder)
        if (!next) order = (order == 'asc') ? 'desc' : 'asc'
        String rel = (order == 'asc') ? '>' : '<'
        def value = instance."${property}"

        /* compose WHERE clause */
        StringBuilder sql = new StringBuilder('from ')
        sql << clazz.simpleName
        sql << ' where '
        sql << property << ' '
        if (value == null) {
            sql << 'is not null'
        } else {
            sql << rel << ' :value'
        }
        sql << ' or '
        sql << property << ' '
        if (value == null) {
            sql << 'is null'
        } else {
            sql << ' = :value'
        }
        sql << ' and id ' << rel << ' :id order by '
        sql << property << ' ' << order
        sql << ', id ' << order
        sql << ' limit 1'
        println "SQL: ${sql}, id: ${instance.id}: value: ${value}"

        clazz.find sql, [id: instance.id, value: value]
    }
}

