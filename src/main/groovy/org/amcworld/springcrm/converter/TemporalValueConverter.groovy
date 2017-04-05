/*
 * TemporalValueConverter.groovy
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


package org.amcworld.springcrm.converter

import grails.databinding.converters.ValueConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code TemporalValueConverter} represents a base class for value
 * converters which convert strings to classes in package {@code java.time}.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
abstract class TemporalValueConverter implements ValueConverter {

    //-- Fields ---------------------------------

    MessageSource messageSource


    //-- Public methods -------------------------

    @Override
    boolean canConvert(Object value) {
        value instanceof String
    }


    //-- Non-public methods ---------------------

    /**
     * Parses the given string as a date value.  The method tries the
     * following format, in that order:
     * <ul>
     *   <li>ISO 8601 date format</li>
     *   <li>{@code ddMMyyyy}</li>
     *   <li>{@code ddMMyy}</li>
     *   <li>the locale-specific format in {@code default.format.date}</li>
     *   <li>{@code yyyy-MM-dd}</li>
     * </ul>
     *
     * @param text                      the given string
     * @return                          the parsed date
     * @throws IllegalArgumentException if the given string cannot be parsed as
     *                                  date
     */
    protected LocalDate parseAsLocalDate(String text)
        throws IllegalArgumentException
    {
        LocalDate res
        try {

            /* try ISO 8601 dates first */
            res = LocalDate.parse(text)
        } catch (DateTimeParseException ignored) {

            /*
             * otherwise try either the short format or the locale specific one
             */
            String format
            if (text.isLong()) {
                format = text.length() > 6 ? 'ddMMyyyy' : 'ddMMyy'
            } else {
                format = messageSource.getMessage(
                    'default.format.date', null, 'yyyy-MM-dd', LCH.locale
                )
            }
            try {
                res = LocalDate.parse(text, DateTimeFormatter.ofPattern(format))
            } catch (DateTimeParseException ignore) {
                throw new IllegalArgumentException()
            }
        }

        res
    }
}
