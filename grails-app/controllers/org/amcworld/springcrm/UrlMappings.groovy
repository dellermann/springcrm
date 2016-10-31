/*
 * UrlMappings.groovy
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

import org.amcworld.springcrm.google.GoogleAuthException


/**
 * The class {@code org.amcworld.springcrm.UrlMappings} defines URL mappings.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class UrlMappings {

    //-- Class fields ---------------------------

    static mappings = {

        /* default URL rewriting */
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        /* redirect home to overview page */
        '/'(controller: 'overview', action: 'index')

        /* URLs for helpdesk frontend */
        name helpdeskFrontend: "/tickets/${urlName}/${accessCode}" {
            controller = 'helpdesk'
            action = 'frontendIndex'
        }

        /* error handling */
        '403'(controller: 'error', action: 'forbidden')
        '404'(controller: 'error', action: 'notFound')
        '500'(
            controller: 'error', action: 'googleAuthException',
            exception: GoogleAuthException
        )
        '500'(view: '/error/error')
    }
}
