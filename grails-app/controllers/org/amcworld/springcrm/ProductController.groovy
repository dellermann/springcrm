/*
 * ProductController.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import grails.converters.JSON
import javax.servlet.http.HttpServletResponse


/**
 * The class {@code ProductController} contains actions which manage products.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class ProductController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    LruService lruService
    SalesItemService salesItemService


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (params.letter) {
            int num = Product.countByNameLessThan(params.letter)
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }
        [productInstanceList: Product.list(params), productInstanceTotal: Product.count()]
    }

    def selectorList() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        String searchFilter = params.search ? "%${params.search}%".toString() : ''
        if (params.letter) {
            int num
            if (params.search) {
                num = Product.countByNameLessThanAndNameLike(params.letter, searchFilter)
            } else {
                num = Product.countByNameLessThan(params.letter)
            }
            params.sort = 'name'
            params.offset = Math.floor(num / params.max) * params.max
        }

        def list, count
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
        def productInstance = new Product()
        productInstance.properties = params
        [productInstance: productInstance]
    }

    def copy(Long id) {
        def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), id])
            redirect action: 'list'
            return
        }

        productInstance = new Product(productInstance)
        render view: 'create', model: [productInstance: productInstance]
    }

    def save() {
        def productInstance = new Product()
        if (!salesItemService.saveSalesItemPricing(productInstance, params)) {
            render view: 'create', model: [productInstance: productInstance]
            return
        }

        lruService.recordItem controllerName, productInstance
        productInstance.index()

        flash.message = message(code: 'default.created.message', args: [message(code: 'product.label', default: 'Product'), productInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: productInstance.id
        }
    }

    def show(Long id) {
        def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), id])
            redirect action: 'list'
            return
        }

        [productInstance: productInstance]
    }

    def edit(Long id) {
        def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), id])
            redirect action: 'list'
            return
        }

        [productInstance: productInstance]
    }

    def update(Long id) {
        def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (productInstance.version > version) {
                productInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'product.label', default: 'Product')] as Object[], 'Another user has updated this Product while you were editing')
                render view: 'edit', model: [productInstance: productInstance]
                return
            }
        }

        if (!salesItemService.saveSalesItemPricing(productInstance, params)) {
            render view: 'edit', model: [productInstance: productInstance]
            return
        }

        lruService.recordItem controllerName, productInstance
        productInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'product.label', default: 'Product'), productInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: productInstance.id
        }
    }

    def delete(Long id) {
        def productInstance = Product.get(id)
        if (!productInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
            return
        }

        try {
            productInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'product.label', default: 'Product')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'product.label', default: 'Product')])
            redirect action: 'show', id: id
        }
    }

    def get(Long id) {
        def productInstance = Product.get(id)
        if (!productInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        JSON.use('deep') {
            render(contentType: 'text/json') {
                fullNumber = productInstance.fullNumber
                inventoryItem = productInstance
            }
        }
    }
}
