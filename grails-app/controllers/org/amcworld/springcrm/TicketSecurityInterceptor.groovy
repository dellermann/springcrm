/*
 * TicketSecurityInterceptor.groovy
 *
 * Copyright (c) 2011-2019, Daniel Ellermann
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

import grails.artefact.Interceptor
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic


/**
 * The class {@code TicketSecurityInterceptor} controls access from frontend to
 * tickets by ID and access code.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class TicketSecurityInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    TicketSecurityInterceptor() {
        match controller: 'ticket', action: ~/frontend.*/
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method controls access from
     * frontend to tickets by ID and access code.
     *
     * @return  {@code true} if the action should be called; {@code false}
     *          otherwise
     */
    boolean before() {
        Helpdesk helpdeskInstance = loadHelpdesk()
        if (!helpdeskInstance) {
            render status: SC_NOT_FOUND
            return false
        }

        if (helpdeskInstance.accessCode != params.accessCode) {
            render status: SC_FORBIDDEN
            return false
        }

        if (params.id) {
            Ticket ticketInstance = loadTicket(params.id.toString())
            if (!ticketInstance) {
                render status: SC_NOT_FOUND
                return false
            }

            if (ticketInstance.helpdesk != helpdeskInstance) {
                render status: SC_FORBIDDEN
                return false
            }

            params.id = ticketInstance.id
        }

        true
    }


    //-- Non-public methods ---------------------

    @CompileDynamic
    private Helpdesk loadHelpdesk() {
        Helpdesk.read params.helpdesk
    }

    @CompileDynamic
    private static Ticket loadTicket(String id) {
        try {
            Ticket ticket = Ticket.read(new BigInteger(id, 10))
            ticket.code ? null : ticket
        } catch (NumberFormatException ignored) {
            Ticket.findByCode id
        }
    }
}
