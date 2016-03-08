/*
 * NumberedDomain.groovy
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


/**
 * The trait {@code NumberedDomain} represents domain model classes which
 * support sequence numbers.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
trait NumberedDomain {

    //-- Fields ---------------------------------

    /**
     * The service to obtain sequence numbers.
     */
    def seqNumberService        // must be "def" to prevent DB field generation

    /**
     * The sequence number.
     */
    int number


    //-- Properties -----------------------------

    /**
     * Gets the formatted full number of this domain model instance.
     *
     * @return  the formatted full number with prefix and suffix
     */
    String getFullNumber() {
        seqNumberService.format getClass(), number
    }


    //-- Public methods -------------------------

    def beforeInsert() {
        if (number == 0) {
            number = seqNumberService.nextNumber(getClass())
        }
    }
}
