/*
 * Address.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
 * The class {@code Address} represents an address for organizations, persons,
 * invoices etc.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.4
 */
class Address {

    //-- Class variables ------------------------

    static constraints = {
        street nullable: true, widget: 'textarea'
        poBox nullable: true
        postalCode nullable: true
        location nullable: true
        state nullable: true
        country nullable: true
    }
    static transients = ['empty']


    //-- Instance variables ---------------------

    String street = ''
    String poBox = ''
    String postalCode = ''
    String location = ''
    String state = ''
    String country = ''


    //-- Constructors ---------------------------

    /**
     * Creates an empty address.
     */
    Address() {}

    /**
     * Creates a copy of the given address.
     *
     * @param addr  the given address
     */
    Address(Address addr) {
        street = addr.street
        poBox = addr.poBox
        postalCode = addr.postalCode
        location = addr.location
        state = addr.state
        country = addr.country
    }


    //-- Properties -----------------------------

    /**
     * Checks whether or not this address is empty.
     *
     * @return  {@code true} if this address is empty; {@code false} otherwise
     */
    boolean isEmpty() {
        !street && !poBox && !postalCode && !location && !state && !country
    }


    //-- Public methods -------------------------

    /**
     * Clears all fields of this address.
     *
     * @since 2.0
     */
    void clear() {
        street = ''
        poBox = ''
        postalCode = ''
        location = ''
        state = ''
        country = ''
    }

    @Override
    boolean equals(Object obj) {
        obj instanceof Address &&
            obj.street == street &&
            obj.poBox == poBox &&
            obj.postalCode == postalCode &&
            obj.location == location &&
            obj.state == state &&
            obj.country == country
    }

    @Override
    String toString() {
        StringBuilder s = new StringBuilder(street ?: '')
        if (location) {
            if (s) s << ','
            if (postalCode) {
                if (s) s << ' '
                s << postalCode ?: ''
            }
            if (s) s << ' '
            s << location ?: ''
        }

        s.toString()
    }
}
