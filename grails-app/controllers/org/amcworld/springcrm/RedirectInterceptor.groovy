/*
 * RedirectInterceptor.groovy
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

import grails.artefact.Interceptor
import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code RedirectInterceptor} handles the redirection for various
 * actions concerning parameter {@code redirectUrl}.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@CompileStatic
class RedirectInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    RedirectInterceptor() {
        match action: ~/(delete|save|saveSelValues|saveSeqNumbers|saveTaxRates|update|updatePayment)/
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.
     *
     * @return  always {@code true}
     */
    boolean after() {
        Map<String, Object> args = [: ]
        String action = actionName

        if (view == null
            && (action == 'saveSelValues' || action == 'saveSeqNumbers'
                || action == 'saveTaxRates'))
        {
            setActionOrUrl args, 'index'
        } else {
            GormEntity<?> inst = domainInstance
            if (inst) {
                args.params = request['redirectParams'] ?: [: ]
                if (action == 'delete') {
                    setActionOrUrl args, 'index'
                } else {
                    args.id = inst.ident()
                    if (params.close) {
                        setActionOrUrl args, 'show'
                    } else {
                        args.action = 'edit'
                        String returnUrl = params.returnUrl
                        if (returnUrl) {
                            args.params['returnUrl'] = returnUrl
                        }
                    }
                }
            }
        }

        if (!args.isEmpty()) {
            redirect args
        }

        true
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the saved or updated domain model instance which has been stored in
     * request context by the controller action after successful saving.
     *
     * @return  the domain model instance; {@code null} if no such instance has
     *          been stored
     */
    private GormEntity<?> getDomainInstance() {
        (GormEntity<?>) request["${controllerName}Instance"]
    }

    /**
     * Adds either the given action or the return URL to the given map of
     * arguments for the {@code redirect} method.
     *
     * @param args      the argument map
     * @param action    the given action
     */
    private void setActionOrUrl(Map<String, Object> args, String action) {
        String returnUrl = params.returnUrl
        if (returnUrl) {
            args.url = returnUrl
        } else {
            args.action = action
        }
    }
}
