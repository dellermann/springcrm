package org.amcworld.springcrm

import grails.converters.JSON

class ProductController {

    static allowedMethods = [save: "POST", update: "POST", delete: "GET"]

	def seqNumberService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.letter) {
			int num = Product.countByNameLessThan(params.letter)
			params.sort = 'name'
			params.offset = Math.floor(num / params.max) * params.max
		}
        [productInstanceList: Product.list(params), productInstanceTotal: Product.count()]
    }
	
	def selectorList = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.letter) {
			int num = Product.countByNameLessThan(params.letter)
			params.sort = 'name'
			params.offset = Math.floor(num / params.max) * params.max
		}
		[productInstanceList: Product.list(params), productInstanceTotal: Product.count()]
	}

    def create = {
        def productInstance = new Product()
        productInstance.properties = params
        return [productInstance: productInstance]
    }

    def save = {
        def productInstance = new Product(params)
        if (productInstance.save(flush:true)) {
			seqNumberService.stepFurther(Product)
			productInstance.index()
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'product.label', default: 'Product'), productInstance.toString()])}"
            redirect(action: "show", id: productInstance.id)
        } else {
            render(view: "create", model: [productInstance: productInstance])
        }
    }

    def show = {
        def productInstance = Product.get(params.id)
        if (!productInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
        } else {
            [productInstance: productInstance]
        }
    }

    def edit = {
        def productInstance = Product.get(params.id)
        if (!productInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
        } else {
            return [productInstance: productInstance]
        }
    }

    def update = {
        def productInstance = Product.get(params.id)
        if (productInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (productInstance.version > version) {
                    productInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'product.label', default: 'Product')] as Object[], "Another user has updated this Product while you were editing")
                    render(view: "edit", model: [productInstance: productInstance])
                    return
                }
            }
            productInstance.properties = params
            if (!productInstance.hasErrors() && productInstance.save(flush: true)) {
				productInstance.reindex()
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'product.label', default: 'Product'), productInstance.toString()])}"
                redirect(action: "show", id: productInstance.id)
            } else {
                render(view: "edit", model: [productInstance: productInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def productInstance = Product.get(params.id)
        if (productInstance) {
            try {
                productInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'product.label', default: 'Product')])}"
                redirect(action: "list")
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'product.label', default: 'Product')])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'product.label', default: 'Product'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def get = {
        def productInstance = Product.get(params.id)
        if (!productInstance) {
			render(status: 404)
        } else {
			JSON.use("deep") {
				render(contentType:"text/json") {
					fullNumber = productInstance.fullNumber
					inventoryItem = productInstance
				}
			}
        }
	}
}
