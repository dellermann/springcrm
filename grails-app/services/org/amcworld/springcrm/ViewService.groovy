/*
 * ViewService.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import org.springframework.context.i18n.LocaleContextHolder as LCH
import org.springframework.validation.FieldError


/**
 * The class {@code ViewService} contains several service methods for reducing
 * code in the views.
 *
 * @author  Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class ViewService {

    //-- Instance variables ---------------------

    def messageSource


    //-- Public methods -------------------------

    /**
     * Returns a list of error messages concerning the embedded items of the
     * given domain object.  The items are treated as list and submitted as
     * parameters in the form {@code items[i].fieldname}.
     *
     * @param domain    the given domain object
     * @return          a list of strings representing the error messages, if
     *                  any
     */
    List<String> getItemErrorMessages(def domain) {
        Locale locale = LCH.locale
        List<String> res = []
        for (FieldError err : domain.errors.getFieldErrors('items[*')) {
            def matcher = err.field =~ /^items\[(\d+)\]\.(\w+)$/
            if (matcher) {
                int idx = matcher[0][1] as Integer
                String field = matcher[0][2]
                StringBuilder buf = new StringBuilder() <<
                    messageSource.getMessage(
                        "invoicingTransaction.${field}.label", null, field,
                        locale
                    ) <<
                    ' ' <<
                    messageSource.getMessage(
                        'invoicingTransaction.pos.in', [idx + 1] as Object[],
                        'Row {0}', locale
                    ) <<
                    ': ' <<
                    messageSource.getMessage(err, locale)
                res << buf.toString()
            }
        }
        return res
    }
}
