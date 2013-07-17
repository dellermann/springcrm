/*
 * UrlPartCodec.groovy
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


package org.amcworld.springcrm


/**
 * The class {@code UrlPartCodec} represents a codec for converting strings to
 * URL parts by replacing sensitive characters such as non-word characters by
 * dashes.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class UrlPartCodec {

    //-- Public methods -------------------------

    static encode(o) {
        if (!o) return o

        o.toString()
            .toLowerCase()
            .replaceAll(',', '.')
            .replaceAll('ä', 'ae')
            .replaceAll('ö', 'oe')
            .replaceAll('ü', 'ue')
            .replaceAll('ß', 'ss')
            .replaceAll(/[^\w.]+/, '-')
            .replaceFirst(~/^-+/, '')
            .replaceFirst(~/-+$/, '')
    }
}
