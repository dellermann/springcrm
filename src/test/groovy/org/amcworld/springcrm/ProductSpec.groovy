/*
 * ProductSpec.groovy
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

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification


class ProductSpec extends Specification implements DomainUnitTest<Product> {

    //-- Feature methods ------------------------

    def 'Creating an empty item initializes the properties'() {
        when: 'I create an empty product'
        def p = new Product()

        then: 'the properties are initialized properly'
        'P' == p.type
        null == p.category
        null == p.manufacturer
        null == p.retailer
        null == p.weight
    }

    def 'Copy an empty instance using constructor'() {
        given: 'an empty product'
        def p1 = new Product()

        when: 'I copy the product using the constructor'
        def p2 = new Product(p1)

        then: 'the properties are set properly'
        p1.type == p2.type
        null == p2.category
        null == p2.manufacturer
        null == p2.retailer
        null == p2.weight
    }

    def 'Copy a product using constructor'() {
        given: 'a product with various properties'
        def p1 = new Product(
            category: new ProductCategory(),
            manufacturer: 'manufacturer',
            retailer: 'retailer',
            weight: 1993
        )

        when: 'I copy the product using the constructor'
        def p2 = new Product(p1)

        then: 'some properties are the equal'
        p1.type == p2.type
        p1.manufacturer == p2.manufacturer
        p1.retailer == p2.retailer
        p1.weight == p2.weight

        and: 'some instances are the same'
        p1.category.is p2.category
    }

    def 'Weight must be positive or zero'(BigDecimal w, boolean v) {
        given: 'a quite valid product'
        def p = new Product(
            number: 39999,
            name: '8" pipe',
            quantity: 45,
            unit: new Unit(),
            unitPrice: 3.45,
            category: new ProductCategory(),
            manufacturer: 'manufacturer',
            retailer: 'retailer'
        )

        when: 'I set a weight'
        p.weight = w

        then:
        v == p.validate()

        where:
        w           | v
        null        | true
        0.0         | true
        0.000000001 | true
        4           | true
        475.3703    | true
        94735.75432 | true
        -5          | false
        -14001.5    | false
    }
}
