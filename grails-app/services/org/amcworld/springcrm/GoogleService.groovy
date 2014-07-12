/*
 * GoogleService.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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

import javax.servlet.http.HttpSession
import org.amcworld.springcrm.google.GoogleSync
import org.springframework.web.context.request.RequestContextHolder


/**
 * The class {@code GoogleService} represents a common base class for all
 * classes which exchange data with Google.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.0
 */
abstract class GoogleService
    implements org.amcworld.springcrm.google.GoogleService
{
    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    def grailsApplication


    //-- Non-public methods ---------------------

    /**
     * Returns access to the user session.
     *
     * @return the session instance
     */
    protected HttpSession getSession() {
        RequestContextHolder.currentRequestAttributes().session
    }

    /**
     * Gets the underlying instance which performs the synchronization with
     * Google.
     *
     * @param name  the name of the bean representing the synchronization
     *              instance as defined in {@code conf/spring/resources.groovy}
     * @return      the synchronization instance; {@code null} if no such
     *              instance with the given name exists
     */
    protected GoogleSync getSyncInstance(String name) {
        (GoogleSync) grailsApplication.mainContext.getBean(name)
    }

    /**
     * Gets the currently logged in user.
     *
     * @return  the current user
     */
    protected User getUser() {
        session.user
    }
}
