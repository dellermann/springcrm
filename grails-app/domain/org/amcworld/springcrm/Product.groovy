/*
 * Product.groovy
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

import static java.math.BigDecimal.ZERO

import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code Product} represents a product.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.3
 * @see     Work
 */
class Product extends SalesItem implements GormEntity<Product> {

    //-- Class fields ---------------------------

    static constraints = {
        category nullable: true
        manufacturer nullable: true
        retailer nullable: true
        weight nullable: true, scale: 6, min: ZERO
    }


    //-- Fields ---------------------------------

    /**
     * The category this product belongs to.
     */
    ProductCategory category

    /**
     * The name of the manufacturer of this product.
     */
    String manufacturer

    /**
     * The name of the retailer of this product.
     */
    String retailer

    /**
     * The optional weight of this product.
     */
    BigDecimal weight


    //-- Constructors ---------------------------

    /**
     * Creates an empty product.
     */
    Product() {
        super()
        type = 'P'
    }

    /**
     * Creates a product using the data of the given product.
     *
     * @param p the given product
     */
    Product(Product p) {
        super(p)
        type = p.type
        category = p.category
        manufacturer = p.manufacturer
        retailer = p.retailer
        weight = p.weight
    }
}
