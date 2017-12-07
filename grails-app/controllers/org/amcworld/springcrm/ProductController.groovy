/*
 * ProductController.groovy
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


/**
 * The class {@code ProductController} contains actions which manage products.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class ProductController extends SalesItemController<Product> {

    //-- Fields -------------------------------------

    ProductService productService


    //-- Constructors ---------------------------

    ProductController() {
        super(Product)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def get(Long id) {
        super.get id
    }

    def index() {
        if (params.letter) {
            int num =
                productService.countByNameLessThan(params.letter.toString())
            params.sort = 'name'
            int max = params.int('max')
            params.offset = Math.floor(num / max) * max
        }

        getIndexModel productService.list(params), productService.count()
    }

    def save() {
        super.save()
    }

    def selectorList() {
        String searchFilter =
            params.search ? "%${params.search}%".toString() : ''
        String letter = params.letter?.toString()
        if (letter) {
            int num
            if (params.search) {
                num = productService.countByNameLessThanAndNameLike(
                    letter, searchFilter
                )
            } else {
                num = productService.countByNameLessThan(letter)
            }
            params.sort = 'name'
            int max = params.int('max')
            params.offset = Math.floor(num / max) * max
        }

        List<Product> list
        int count
        if (params.search) {
            list = productService.findAllByNameLike(searchFilter, params)
            count = productService.countByNameLike(searchFilter)
        } else {
            list = productService.list(params)
            count = productService.count()
        }

        getIndexModel list, count
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods -------------------------

    @Override
    protected Product lowLevelGet(Long id) {
        productService.get id
    }

    @Override
    protected Product lowLevelSave(Product instance) {
        productService.save instance
    }
}
