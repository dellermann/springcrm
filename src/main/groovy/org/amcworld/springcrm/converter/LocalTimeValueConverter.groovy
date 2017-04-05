/*
 * LocalTimeValueConverter.groovy
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

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code LocalTimeValueConverter} represents a converter from
 * strings containing time values to {@code LocalTime} objects.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class LocalTimeValueConverter extends TemporalValueConverter {

    //-- Public methods -------------------------

    @Override
    Object convert(Object value) {
        value ? parseTime(value.toString()) : null
    }

    @Override
    Class<?> getTargetType() {
        LocalTime
    }


    //-- Non-public methods ---------------------

    private LocalTime parseTime(String text) throws IllegalArgumentException {
        LocalTime res
        try {

            /* try ISO 8601 times first */
            res = LocalTime.parse(text)
        } catch (IllegalArgumentException ignored) {

            /*
             * otherwise try either the short format or the locale specific one
             */
            String format
            if (text.isLong()) {
                format = 'HHmm'
            } else {
                format = messageSource.getMessage(
                    'default.format.time', null, 'HH:mm', LCH.locale
                )
            }
            try {
                res = LocalTime.parse(
                    text, DateTimeFormatter.ofPattern(format.toString())
                )
            } catch (DateTimeParseException ignore) {
                throw new IllegalArgumentException()
            }
        }

        res
    }
}
