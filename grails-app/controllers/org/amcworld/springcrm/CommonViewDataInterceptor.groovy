/*
 * CommonViewDataInterceptor.groovy
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
import org.springframework.beans.factory.annotation.Autowired


/**
 * The class {@code CommonViewDataInterceptor} stores important values in the
 * model of each view, if any.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.1
 */
@CompileStatic
class CommonViewDataInterceptor implements Interceptor {

    //-- Fields ---------------------------------

    int order = 10
    @Autowired UserService userService


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of the interceptor.
     */
    CommonViewDataInterceptor() {
        matchAll()
    }


    //-- Public methods -------------------------

    /**
     * Called after the action has been executed.  The method stores important
     * values for the view in the model.
     *
     * @return  always {@code true}
     */
    boolean after() {
        if (model != null) {
            Config.withNewSession {
                Locale l = userService.currentLocale
                model.locale = l.toString().replace('_', '-')
                model.lang = l.language
                model.currencySymbol = userService.currencySymbol
                model.numFractionDigits = userService.numFractionDigits
                model.numFractionDigitsExt = userService.numFractionDigitsExt
                model.decimalSeparator = userService.decimalSeparator
                model.groupingSeparator = userService.groupingSeparator

                List<TaxRate> taxRates = TaxRate.list(sort: 'orderId')
                model.taxRatesString =
                    taxRates.collect { it.taxValue * 100 }.join ','
            }
        }

        true
    }
}
