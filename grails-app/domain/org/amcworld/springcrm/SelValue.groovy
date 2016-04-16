/*
 * SelValue.groovy
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

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code SelValue} represents a base class for selector values
 * which may be defined by the user.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class SelValue implements GormEntity<SelValue> {

    //-- Class fields ---------------------------

    static constraints = {
        name blank: false
    }
    static mapping = {
        sort 'orderId'
        id(
            generator: 'org.hibernate.id.enhanced.SequenceStyleGenerator',
            params: [initial_value: 50000]
        )
    }


    //-- Fields ---------------------------------

    /**
     * The name of this selection value.
     */
    String name

    /**
     * A value used to order various selection values.
     */
    int orderId = 0


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof SelValue && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        name ?: ''
    }
}
