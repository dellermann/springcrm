/*
 * SeqNumber.groovy
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

import groovy.transform.EqualsAndHashCode
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code SeqNumber} represents a sequence number which is used to
 * number various content items.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@EqualsAndHashCode(includes = ['id'])
class SeqNumber implements GormEntity<SeqNumber> {

    //-- Class fields ---------------------------

    static constraints = {
        prefix nullable: true, maxSize: 5
        suffix nullable: true, maxSize: 5
        startValue min: 0i
        endValue min: 0i
        orderId min: 0i
    }
    static mapping = {
        sort 'orderId'
    }


    //-- Fields ---------------------------------

    /**
     * The end of the number range.
     */
    int endValue = 99999i

    /**
     * The ID of the sequence number, which is the controller name the sequence
     * number belongs to.
     */
    String id

    /**
     * A value specifying the order of the sequence number in a list.
     */
    int orderId

    /**
     * Any prefix which should be used to format the sequence number.
     */
    String prefix = ''

    /**
     * The start of the number range.
     */
    int startValue = 10000i

    /**
     * Any suffix which should be used to format the sequence number.
     */
    String suffix = ''


    //-- Public methods -------------------------

    @Override
    String toString() {
        id
    }
}
