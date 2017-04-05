/*
 * LocalDateTimeValueConverter.groovy
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

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code LocalDateTimeValueConverter} represents a converter from
 * strings containing date and time values to {@code LocalDateTime} objects.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class LocalDateTimeValueConverter extends TemporalValueConverter {

    //-- Public methods -------------------------

    @Override
    Object convert(Object value) {
        if (!value) {
            return null
        }

        String text = value.toString()
        text.indexOf(' ') < 0 && text.indexOf('T') < 0 ? parseDate(text)
            : parseDateTime(text)
    }

    @Override
    Class<?> getTargetType() {
        LocalDateTime
    }


    //-- Non-public methods ---------------------

    private LocalDateTime parseDate(String text)
        throws IllegalArgumentException
    {
        LocalDateTime.of super.parseAsLocalDate(text), LocalTime.MIDNIGHT
    }

    private LocalDateTime parseDateTime(String text)
        throws IllegalArgumentException
    {
        LocalDateTime res
        try {

            /* try ISO 8601 dates/times first */
            res = LocalDateTime.parse(text)
        } catch (IllegalArgumentException ignored) {

            /*
             * otherwise try either the short format or the locale specific one
             */
            Locale locale = LCH.locale
            StringBuilder format = new StringBuilder()
            int pos = text.indexOf(' ')
            if (pos >= 0) {
                String s = text.substring(0, pos)
                if (s.isLong()) {
                    format << (s.length() > 6 ? 'ddMMyyyy' : 'ddMMyy')
                } else {
                    format << messageSource.getMessage(
                        'default.format.date', null, 'yyyy-MM-dd', locale
                    )
                }
                s = text.substring(pos + 1).trim()
                format << ' '
                if (s.isLong()) {
                    format << 'HHmm'
                } else {
                    format << messageSource.getMessage(
                        'default.format.time', null, 'HH:mm', locale
                    )
                }
            } else {
                format << messageSource.getMessage(
                    'default.format.datetime', null, 'yyyy-MM-dd HH:mm', locale
                )
            }
            try {
                res = LocalDateTime.parse(
                    text, DateTimeFormatter.ofPattern(format.toString())
                )
            } catch (DateTimeParseException ignore) {
                throw new IllegalArgumentException()
            }
        }

        res
    }
}
