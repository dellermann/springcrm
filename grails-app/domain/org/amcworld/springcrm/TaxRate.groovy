/*
 * TaxRate.groovy
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

import static java.math.BigDecimal.ZERO

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code TaxRate} represents tax rates.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class TaxRate extends SelValue {

    //-- Class fields ---------------------------

    static constraints = {
        taxValue min: ZERO, scale: 2
    }


    //-- Fields ---------------------------------

    /**
     * The value of this tax rate in percent.
     */
    BigDecimal taxValue = ZERO


    //-- Properties -----------------------------

    /**
     * Sets the value of this tax rate in percent.
     *
     * @param taxValue  the tax value that should be set; if {@code null} it is
     *                  converted to zero
     * @since 2.0
     */
    void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue == null ? ZERO : taxValue
    }
}
