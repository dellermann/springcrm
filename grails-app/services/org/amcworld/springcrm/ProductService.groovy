/*
 * ProductService.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import grails.gorm.services.Service


/**
 * The interface {@code ProductService} represents a service which allows access
 * to products.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
interface ProductService {

    //-- Public methods -------------------------

    /**
     * Counts all products.
     *
     * @return  the number of all products
     */
    int count()

    /**
     * Counts the product with a name alphabetically before the given name.
     *
     * @param name  the given name
     * @return      the number of products
     */
    int countByNameLessThan(String name)

    int countByNameLessThanAndNameLike(String name, String pattern)

    /**
     * Counts the products with a name matching the given {@code LIKE} pattern.
     *
     * @param name  the given name pattern
     * @return      the number of products
     */
    int countByNameLike(String name)

    /**
     * Deletes the product with the given ID.
     *
     * @param id    the given ID
     */
    void delete(Serializable id)

    /**
     * Finds the products with a name matching the given {@code LIKE} pattern.
     *
     * @param name  the given name pattern
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of products
     */
    List<Product> findAllByNameLike(String name, Map args)

    /**
     * Gets the product with the given ID.
     *
     * @param id    the given ID
     * @return      the product or {@code null} if no such product with the
     *              given ID exists
     */
    Product get(Serializable id)

    /**
     * Gets a list of all products.
     *
     * @param args  any arguments used for retrieval (sort, order etc.)
     * @return      a list of products
     */
    List<Product> list(Map args)

    /**
     * Saves the given product.
     *
     * @param instance  the given product
     * @return          the saved product
     */
    Product save(Product instance)
}


/**
 * The class {@code ProductServiceImpl} represents an implementation of the
 * service which allows access to products.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 * @since   2.2
 */
@Service(value = Product, name = 'productService')
abstract class ProductServiceImpl implements ProductService {

    //-- Public methods -----------------------------

    int countByNameLessThan(String name) {
        Product.countByNameLessThan name
    }

    int countByNameLessThanAndNameLike(String name, String pattern) {
        Product.countByNameLessThanAndNameLike name, pattern
    }

    int countByNameLike(String name) {
        Product.countByNameLike name
    }

    List<Product> findAllByNameLike(String name, Map args) {
        Product.findAllByNameLike name, args
    }
}
