/*
 * SelectorViewInterceptor.groovy
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

import grails.artefact.Interceptor
import groovy.transform.CompileStatic


/**
 * The class {@code SelectorViewInterceptor} redirects to the correct list view
 * depending on the {@code view} parameter.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
@CompileStatic
class SelectorViewInterceptor implements Interceptor {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    SelectorViewInterceptor() {
        match(
            controller: ~/(calendarEvent|phoneCall|creditMemo|dunning|invoice|note|product|purchaseInvoice|quote|salesOrder|work)/,
            action: 'index'
        )
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method renders the
     * appropriate view according to the value of the {@code view} parameter.
     *
     * @return  always {@code true}
     */
    boolean after() {
        view = (params.view == 'selector') ? 'selectorList' : 'index'

        true
    }
}
