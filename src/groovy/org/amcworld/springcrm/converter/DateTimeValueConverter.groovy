/*
 * DateTimeValueConverter.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import java.text.ParseException
import java.text.SimpleDateFormat
import org.grails.databinding.converters.ValueConverter
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code DateTimeValueConverter} represents a converter from strings
 * containing date, time, or date/time values to {@code Date} objects.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class DateTimeValueConverter implements ValueConverter {

    //-- Instance variables ---------------------

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
        (text.indexOf(' ') < 0) ? parseDate(text) : parseDateTime(text)
    }

    @Override
    Class<?> getTargetType() {
        Date
    }


    //-- Non-public methods ---------------------

    protected Date parseDate(String text) throws IllegalArgumentException {
        String fmt
        if (text.isLong()) {
            fmt = (text.length() > 6) ? 'ddMMyyyy' : 'ddMMyy'
        } else {
            Locale locale = LCH.locale
            fmt = messageSource.getMessage(
                'default.format.date', null, 'yyyy-MM-dd', locale
            )
        }
        try {
            return new SimpleDateFormat(fmt).parse(text)
        } catch (ParseException e) {
            throw new IllegalArgumentException()
        }
    }

    protected Date parseDateTime(String text) throws IllegalArgumentException {
        Locale locale = LCH.locale
        StringBuilder fmt = new StringBuilder()
        int pos = text.indexOf(' ')
        if (pos >= 0) {
            String s = text.substring(0, pos)
            if (s.isLong()) {
                fmt << ((s.length() > 6) ? 'ddMMyyyy' : 'ddMMyy')
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
            return new SimpleDateFormat(fmt.toString()).parse(text)
        } catch (ParseException e) {
            throw new IllegalArgumentException()
        }
    }
}
