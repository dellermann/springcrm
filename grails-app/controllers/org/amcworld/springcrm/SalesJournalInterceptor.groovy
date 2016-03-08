/*
 * SalesJournalInterceptor.groovy
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

import groovy.transform.CompileStatic


/**
 * The class {@code SalesJournalInterceptor} initializes month and year of the
 * sales journal.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class SalesJournalInterceptor extends SettingsInterceptorBase {

    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    SalesJournalInterceptor() {
        match controller: 'report', action: 'sales-journal'
    }


    //-- Public methods -------------------------

    /**
     * Called before the action is executed.  The method initializes the month
     * and year from the user settings.
     *
     * @return  always {@code true}
     */
    boolean before() {
        Credential credential = (Credential) session?.getAttribute('credential')
        if (credential) {
            Closure convert = { String s -> Integer.parseInt s }
            exchangeSetting(
                params, 'year', credential.settings, 'salesJournalYear',
                convert
            )
            exchangeSetting(
                params, 'month', credential.settings, 'salesJournalMonth',
                convert
            )
        }

        true
    }
}
