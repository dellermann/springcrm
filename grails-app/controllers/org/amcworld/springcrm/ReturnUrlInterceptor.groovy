/*
 * ReturnUrlInterceptor.groovy
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

import grails.artefact.Interceptor
import grails.web.mapping.ResponseRedirector
import groovy.transform.CompileStatic


/**
 * The class {@code ReturnUrlInterceptor} represents an interceptor which
 * redirects to a particular return URL if it is specified.  It overwrites any
 * previously set redirection.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class ReturnUrlInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    ReturnUrlInterceptor() {
        match action: ~/(save|update|delete|saveSelValues|saveTaxRates|saveSeqNumbers|updatePayment)/
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method overwrites the
     * redirect if a return URL has been specified.
     *
     * @return  always {@code true}
     */
    boolean after() {
        if (params.returnUrl) {

            /*
             * Implementation notes: ResponseRedirector.redirect refuses anew
             * redirection if redirect() has been called before in controller.
             * Thus, we need to remove the flag from request and call redirect()
             * anew.
             */
            String attrName = ResponseRedirector.GRAILS_REDIRECT_ISSUED
            if (request.getAttribute(attrName) != null) {
                request.removeAttribute attrName
            }
            redirect url: params.returnUrl
        }

        true
    }
}
