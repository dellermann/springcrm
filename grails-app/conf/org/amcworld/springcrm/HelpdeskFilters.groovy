/*
 * HelpdeskFilters.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND


/**
 * The class {@code HelpdeskFilters} contains filters which check access codes
 * to helpdesks.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.4
 */
class HelpdeskFilters {

    //-- Public methods -------------------------

    def filters = {
        helpdeskSecurity(controller: 'helpdesk', action: 'frontend*') {
            before = {
                String urlName = params.urlName
                Helpdesk helpdeskInstance = Helpdesk.findByUrlName(urlName)
                if (!helpdeskInstance) {
                    render status: SC_NOT_FOUND
                    return false
                }
                if (helpdeskInstance.accessCode != params.accessCode) {
                    render status: SC_FORBIDDEN
                    return false
                }
            }
        }

        ticketSecurity(controller: 'ticket', action: 'frontend*') {
            before = {
                Helpdesk helpdeskInstance = Helpdesk.read(params.helpdesk)
                if (!helpdeskInstance) {
                    render status: SC_NOT_FOUND
                    return false
                }
                if (helpdeskInstance.accessCode != params.accessCode) {
                    render status: SC_FORBIDDEN
                    return false
                }
                if (params.id) {
                    Ticket ticketInstance = Ticket.read(params.id)
                    if (!ticketInstance) {
                        render status: SC_NOT_FOUND
                        return false
                    }
                    if (ticketInstance.helpdesk != helpdeskInstance) {
                        render status: SC_FORBIDDEN
                        return false
                    }
                }
            }
        }

        frontendDataFileSecurity(controller: 'dataFile',
                                 action: 'frontend-load-file')
        {
            before = {
                Helpdesk helpdeskInstance = Helpdesk.read(params.helpdesk)
                if (!helpdeskInstance) {
                    render status: SC_NOT_FOUND
                    return false
                }
                if (helpdeskInstance.accessCode != params.accessCode) {
                    render status: SC_FORBIDDEN
                    return false
                }
            }
        }
    }
}

