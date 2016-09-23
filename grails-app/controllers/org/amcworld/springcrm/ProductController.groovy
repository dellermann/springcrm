/*
 * ProductController.groovy
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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code ProductController} contains actions which manage products.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class ProductController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    SalesItemService salesItemService


    //-- Public methods -------------------------

    def index() {
        int max = params.max =
            Math.min(params.max ? params.int('max') : 10, 100)
        if (params.letter) {
            int num = Product.countByNameLessThan(params.letter.toString())
            params.sort = 'name'
            params.offset = Math.floor(num / max) * max
        }

        [
            productInstanceList: Product.list(params),
            productInstanceTotal: Product.count()
        ]
    }

    def selectorList() {
        int max = params.max =
            Math.min(params.max ? params.int('max') : 10, 100)
        String searchFilter = params.search \
            ? "%${params.search}%".toString() \
            : ''
        String letter = params.letter?.toString()
        if (letter) {
            int num
            if (params.search) {
                num = Product.countByNameLessThanAndNameLike(
                    letter, searchFilter
                )
            } else {
                num = Product.countByNameLessThan(letter)
            }
            params.sort = 'name'
            params.offset = Math.floor(num / max) * max
        }

        List<Product> list
        int count
        if (params.search) {
            list = Product.findAllByNameLike(searchFilter, params)
            count = Product.countByNameLike(searchFilter)
        } else {
            list = Product.list(params)
            count = Product.count()
        }

        [productInstanceList: list, productInstanceTotal: count]
    }

    def create() {
        [productInstance: new Product(params)]
    }

    def copy(Long id) {
        Product productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'product.label'), id]
            )
            redirect action: 'index'
            return
        }

        productInstance = new Product(productInstance)
        render view: 'create', model: [productInstance: productInstance]
    }

    def save() {
        Product productInstance = new Product()
        if (!salesItemService.saveSalesItemPricing(productInstance, params)) {
            render view: 'create', model: [productInstance: productInstance]
            return
        }

        request.productInstance = productInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'product.label'), productInstance.toString()]
        )

        redirect action: 'show', id: productInstance.id
    }

    def show(Long id) {
        Product productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'product.label'), id]
            )
            redirect action: 'index'
            return
        }

        [productInstance: productInstance]
    }

    def edit(Long id) {
        Product productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'product.label'), id]
            )
            redirect action: 'index'
            return
        }

        [productInstance: productInstance]
    }

    def update(Long id) {
        Product productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'product.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (productInstance.version > version) {
                productInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'product.label')] as Object[],
                    'Another user has updated this Product while you were editing'
                )
                render view: 'edit', model: [productInstance: productInstance]
                return
            }
        }

        if (!salesItemService.saveSalesItemPricing(productInstance, params)) {
            render view: 'edit', model: [productInstance: productInstance]
            return
        }

        request.productInstance = productInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'product.label'), productInstance.toString()]
        )

        redirect action: 'show', id: productInstance.id
    }

    def delete(Long id) {
        Product productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'product.label'), id]
            )

            redirect action: 'index'
            return
        }

        request.productInstance = productInstance
        try {
            if (productInstance.pricing) {
                productInstance.pricing.delete flush: true
            }
            productInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'product.label')]
            )

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignored) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'product.label')]
            )

            redirect action: 'show', id: id
        }
    }

    def get(Long id) {
        Product productInstance = Product.read(id)
        if (!productInstance) {
            render status: SC_NOT_FOUND
            return
        }

        [productInstance: productInstance]
    }
}
