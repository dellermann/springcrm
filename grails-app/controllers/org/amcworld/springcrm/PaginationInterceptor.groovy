/*
 * PaginationInterceptor.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.core.GrailsClass
import groovy.transform.CompileStatic
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code PaginationInterceptor} initializes parameters used for
 * pagination and sorting and stores these settings in user session.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class PaginationInterceptor extends SettingsInterceptorBase {

    //-- Constants ------------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    PaginationInterceptor() {
        match action: 'index'
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method initializes parameters
     * used for pagination and sorting and stores these settings in user session.
     *
     * @return  always {@code true}
     */
    boolean before() {

        /* store or restore offset */
        String key = getSessionKey('offset')
        if (controllerName != 'search') {
            exchangeSetting params, 'offset', session, key
        }

        /* compute number of entries of the associated domain */
        int count = getCount()
        if (count < 0) {
            return true
        }

        /* store or restore number of items per page, sorting and order */
        Credential credential = (Credential) session?.getAttribute('credential')
        if (credential) {
            User user = credential.loadUser()
            exchangeSetting(
                params, 'max', user.settings, getSessionKey('max')
            )
            exchangeSetting(
                params, 'sort', user.settings, getSessionKey('sort')
            )
            exchangeSetting(
                params, 'order', user.settings, getSessionKey('order')
            )
        }

        /* limit offset */
        int max = Math.min(params.max ? params.int('max') : 10, 100)
        int maxOffset =
            count > 0 ? (int) (Math.floor((count - 1.0f) / max) * max) : 0
        params.offset =
            Math.max(0, Math.min(maxOffset, params.int('offset') ?: 0))
        session.setAttribute key, params.offset

        true
    }

    /**
     * Called after the action has been executed.  The method stores the
     * pagination offset in session.
     *
     * @return  always {@code true}
     */
    boolean after() {
        if (controllerName != 'search') {
            session.setAttribute getSessionKey('offset'), params.offset
        }

        true
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the number of records of the domain model class associated to the
     * current controller.
     *
     * @return  the number of records for this domain model in data store; -1 if
     *          no domain model class has been found
     */
    private int getCount() {
        GrailsClass cls = grailsApplication.getArtefactByLogicalPropertyName(
            'Domain', controllerName
        )
        Class<?> c = cls?.clazz
        if (!c || !GormEntity.isAssignableFrom(c)) {
            return -1
        }

        int count = 0
        try {
            count = (Integer) c.getMethod('count').invoke(null)
        } catch (NoSuchMethodException ignore) {
            log.error "Method count does not exist in domain model class ${cls.name}."
        }

        count
    }

    /**
     * Gets a key to store a value for the given controller in the session.
     *
     * @param name  the name of the value
     * @return      the computed session key
     */
    private String getSessionKey(String name) {
        String key = name + controllerName.capitalize()
        if (params.type) key += params.type

        key
    }
}
