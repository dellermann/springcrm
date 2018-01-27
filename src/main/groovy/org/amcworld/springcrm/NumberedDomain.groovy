/*
 * NumberedDomain.groovy
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


/**
 * The trait {@code NumberedDomain} represents domain model classes which
 * support sequence numbers.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
trait NumberedDomain {

    //-- Fields ---------------------------------

    /**
     * The sequence number.
     */
    int number


    //-- Public methods -------------------------

    /**
     * Computes the sequence number in the instance.
     *
     * @param seqNumber the given sequence number which specifies prefix and
     *                  suffix; may be {@code null}
     * @return          the formatted sequence number
     * @since 3.0
     */
    String computeFullNumber(SeqNumber seqNumber) {
        computeFullNumber null, seqNumber
    }

    /**
     * Computes the sequence number in the instance as specified in the given
     * arguments.  The given argument map may contain the following keys:
     * <ul>
     *   <li>{@code withPrefix}.  If {@code true} or not specified the prefix
     *   is added to the returned string.</li>
     *   <li>{@code withSuffix}. If {@code true} or not specified the suffix is
     *   added to the returned string.</li>
     * </ul>
     *
     * @param args      any arguments as described above
     * @param seqNumber the given sequence number which specifies prefix and
     *                  suffix; may be {@code null}
     * @return          the formatted sequence number
     * @since 3.0
     */
    String computeFullNumber(Map args, SeqNumber seqNumber) {
        boolean withPrefix = (args?.withPrefix == null) ? true
            : (boolean) args.withPrefix
        boolean withSuffix = (args?.withSuffix == null) ? true
            : (boolean) args.withSuffix

        if (seqNumber) {
            StringBuilder s = new StringBuilder()
            if (withPrefix && seqNumber.prefix) {
                s << seqNumber.prefix << '-'
            }
            s << number
            if (withSuffix && seqNumber.suffix) {
                s << '-' << seqNumber.suffix
            }
            s.toString()
        } else {
            number.toString()
        }
    }
}
