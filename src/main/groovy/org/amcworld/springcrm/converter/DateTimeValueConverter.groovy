/*
 * DateTimeValueConverter.groovy
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


package org.amcworld.springcrm.converter

import grails.databinding.converters.ValueConverter
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.xml.bind.DatatypeConverter
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code DateTimeValueConverter} represents a converter from strings
 * containing date, time, or date/time values to {@code Date} objects.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.4
 */
class DateTimeValueConverter implements ValueConverter {

    //-- Fields ---------------------------------

    MessageSource messageSource


    //-- Public methods -------------------------

    @Override
    boolean canConvert(Object value) {
        value instanceof String
    }

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
        Date
    }


    //-- Non-public methods ---------------------

    private Date parseDate(String text) throws IllegalArgumentException {
        Date res
        try {

            /* try ISO8601 dates first */
            res = DatatypeConverter.parseDate(text).time
        } catch (IllegalArgumentException ignored) {

            /*
             * otherwise try either the short format or the locale specific one
             */
            String fmt
            if (text.isLong()) {
                fmt = text.length() > 6 ? 'ddMMyyyy' : 'ddMMyy'
            } else {
                fmt = messageSource.getMessage(
                    'default.format.date', null, 'yyyy-MM-dd', LCH.locale
                )
            }
            try {
                res = new SimpleDateFormat(fmt).parse(text)
            } catch (ParseException ignore) {
                throw new IllegalArgumentException()
            }
        }

        res
    }

    private Date parseDateTime(String text) throws IllegalArgumentException {
        Date res
        try {

            /* try ISO8601 dates/times first */
            res = DatatypeConverter.parseDateTime(text).time
        } catch (IllegalArgumentException ignored) {

            /*
             * otherwise try either the short format or the locale specific one
             */
            Locale locale = LCH.locale
            StringBuilder fmt = new StringBuilder()
            int pos = text.indexOf(' ')
            if (pos >= 0) {
                String s = text.substring(0, pos)
                if (s.isLong()) {
                    fmt << (s.length() > 6 ? 'ddMMyyyy' : 'ddMMyy')
                } else {
                    fmt << messageSource.getMessage(
                        'default.format.date', null, 'yyyy-MM-dd', locale
                    )
                }
                s = text.substring(pos + 1).trim()
                fmt << ' '
                if (s.isLong()) {
                    fmt << 'HHmm'
                } else {
                    fmt << messageSource.getMessage(
                        'default.format.time', null, 'HH:mm', locale
                    )
                }
            } else {
                fmt << messageSource.getMessage(
                    'default.format.datetime', null, 'yyyy-MM-dd HH:mm', locale
                )
            }
            try {
                res = new SimpleDateFormat(fmt.toString()).parse(text)
            } catch (ParseException ignore) {
                throw new IllegalArgumentException()
            }
        }

        res
    }
}
