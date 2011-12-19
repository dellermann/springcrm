package org.amcworld.springcrm

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class SalesOrderController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def fopService
	def seqNumberService

	def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        return [salesOrderInstanceList: SalesOrder.list(params), salesOrderInstanceTotal: SalesOrder.count()]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = SalesOrder.findAllByOrganization(organizationInstance, params)
			count = SalesOrder.countByOrganization(organizationInstance)
			linkParams = [organization: organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = SalesOrder.findAllByPerson(personInstance, params)
			count = SalesOrder.countByPerson(personInstance)
			linkParams = [person: personInstance.id]
		} else if (params.quote) {
			def quoteInstance = Quote.get(params.quote)
			l = SalesOrder.findAllByQuote(quoteInstance, params)
			count = SalesOrder.countByQuote(quoteInstance)
			linkParams = [quote: quoteInstance.id]
		}
		return [salesOrderInstanceList: l, salesOrderInstanceTotal: count, linkParams: linkParams]
	}

    def create() {
        def salesOrderInstance
		if (params.quote) {
			def quoteInstance = Quote.get(params.quote)
			salesOrderInstance = new SalesOrder(quoteInstance)
		} else {
			salesOrderInstance = new SalesOrder()
			salesOrderInstance.properties = params
		}
		Organization org = salesOrderInstance.organization
		if (org) {
			salesOrderInstance.billingAddrCountry = org.billingAddrCountry
			salesOrderInstance.billingAddrLocation = org.billingAddrLocation
			salesOrderInstance.billingAddrPoBox = org.billingAddrPoBox
			salesOrderInstance.billingAddrPostalCode = org.billingAddrPostalCode
			salesOrderInstance.billingAddrState = org.billingAddrState
			salesOrderInstance.billingAddrStreet = org.billingAddrStreet
			salesOrderInstance.shippingAddrCountry = org.shippingAddrCountry
			salesOrderInstance.shippingAddrLocation = org.shippingAddrLocation
			salesOrderInstance.shippingAddrPoBox = org.shippingAddrPoBox
			salesOrderInstance.shippingAddrPostalCode = org.shippingAddrPostalCode
			salesOrderInstance.shippingAddrState = org.shippingAddrState
			salesOrderInstance.shippingAddrStreet = org.shippingAddrStreet
		}
        return [salesOrderInstance: salesOrderInstance]
    }

	def copy() {
		def salesOrderInstance = SalesOrder.get(params.id)
		if (!salesOrderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])
            redirect(action: 'list')
            return
        }

		salesOrderInstance = new SalesOrder(salesOrderInstance)
		render(view: 'create', model: [salesOrderInstance: salesOrderInstance])
	}

    def save() {
        def salesOrderInstance = new SalesOrder(params)
        if (!salesOrderInstance.save(flush: true)) {
            log.debug(salesOrderInstance.errors)
            render(view: 'create', model: [salesOrderInstance: salesOrderInstance])
            return
        }

		salesOrderInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrderInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: salesOrderInstance.id)
		}
    }

    def show() {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])
            redirect(action: 'list')
            return
        }

        return [salesOrderInstance: salesOrderInstance]
    }

    def edit() {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])
            redirect(action: 'list')
            return
        }

        return [salesOrderInstance: salesOrderInstance]
    }

    def update() {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (salesOrderInstance.version > version) {
                salesOrderInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'salesOrder.label', default: 'SalesOrder')] as Object[], "Another user has updated this SalesOrder while you were editing")
                render(view: 'edit', model: [salesOrderInstance: salesOrderInstance])
                return
            }
        }
		if (params.autoNumber) {
			params.number = salesOrderInstance.number
		}
        salesOrderInstance.properties = params
        salesOrderInstance.items?.retainAll { it != null }
        if (!salesOrderInstance.save(flush: true)) {
            log.debug(salesOrderInstance.errors)
            render(view: 'edit', model: [salesOrderInstance: salesOrderInstance])
            return
        }

		salesOrderInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrderInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: salesOrderInstance.id)
		}
    }

    def delete() {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (salesOrderInstance && params.confirmed) {
            try {
                salesOrderInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder')])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder')])
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

	def find() {
		Integer number = null
		try {
			number = params.name as Integer
		} catch (NumberFormatException) { /* ignored */ }
		def organization = params.organization ? Organization.get(params.organization) : null

		def c = SalesOrder.createCriteria()
		def list = c.list {
			or {
				eq('number', number)
				ilike('subject', "%${params.name}%")
			}
			if (organization) {
				and {
					eq('organization', organization)
				}
			}
			order('number', 'desc')
		}

		render(contentType: "text/json") {
			array {
				for (so in list) {
					salesOrder id: so.id, name: so.fullName
				}
			}
		}
	}

	def print() {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            render(status: 404)
            return
        }

		def data = [
			transaction: salesOrderInstance, items: salesOrderInstance.items,
			organization: salesOrderInstance.organization,
			person: salesOrderInstance.person,
			user: session.user,
			fullNumber: salesOrderInstance.fullNumber,
			taxRates: salesOrderInstance.taxRateSums,
			values: [
		        subtotalNet: salesOrderInstance.subtotalNet,
				subtotalGross: salesOrderInstance.subtotalGross,
				discountPercentAmount: salesOrderInstance.discountPercentAmount,
				total: salesOrderInstance.total
			],
			watermark: params.duplicate ? 'duplicate' : ''
		]
		String xml = (data as XML).toString()
//		println xml

		GString fileName = "${message(code: 'salesOrder.label')} ${salesOrderInstance.fullNumber}"
		if (params.duplicate) {
			fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
		}
		fileName += ".pdf"

		ByteArrayOutputStream baos = new ByteArrayOutputStream()
		fopService.generatePdf(
			new StringReader(xml), '/WEB-INF/data/fo/sales-order-fo.xsl',
			baos
		)
		response.contentType = 'application/pdf'
		response.addHeader 'Content-Disposition',
			"attachment; filename=\"${fileName}\""
		response.contentLength = baos.size()
		response.outputStream.write(baos.toByteArray())
		response.outputStream.flush()
	}
}
