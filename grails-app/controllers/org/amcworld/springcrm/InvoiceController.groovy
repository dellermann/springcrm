package org.amcworld.springcrm

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class InvoiceController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def fopService
	def seqNumberService

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        return [invoiceInstanceList: Invoice.list(params), invoiceInstanceTotal: Invoice.count()]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = Invoice.findAllByOrganization(organizationInstance, params)
			count = Invoice.countByOrganization(organizationInstance)
			linkParams = [organization: organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = Invoice.findAllByPerson(personInstance, params)
			count = Invoice.countByPerson(personInstance)
			linkParams = [person: personInstance.id]
		} else if (params.quote) {
			def quoteInstance = Quote.get(params.quote)
			l = Invoice.findAllByQuote(quoteInstance, params)
			count = Invoice.countByQuote(quoteInstance)
			linkParams = [quote: quoteInstance.id]
		} else if (params.salesOrder) {
			def salesOrderInstance = SalesOrder.get(params.salesOrder)
			l = Invoice.findAllBySalesOrder(salesOrderInstance, params)
			count = Invoice.countBySalesOrder(salesOrderInstance)
			linkParams = [salesOrder: salesOrderInstance.id]
		}
		return [invoiceInstanceList: l, invoiceInstanceTotal: count, linkParams: linkParams]
	}

    def create() {
        def invoiceInstance
		if (params.quote) {
			def quoteInstance = Quote.get(params.quote)
			invoiceInstance = new Invoice(quoteInstance)
		} else if (params.salesOrder) {
			def salesOrderInstance = SalesOrder.get(params.salesOrder)
			invoiceInstance = new Invoice(salesOrderInstance)
			invoiceInstance.quote = salesOrderInstance.quote
		} else {
			invoiceInstance = new Invoice()
			invoiceInstance.properties = params
		}
		Organization org = invoiceInstance.organization
		if (org) {
			invoiceInstance.billingAddrCountry = org.billingAddrCountry
			invoiceInstance.billingAddrLocation = org.billingAddrLocation
			invoiceInstance.billingAddrPoBox = org.billingAddrPoBox
			invoiceInstance.billingAddrPostalCode = org.billingAddrPostalCode
			invoiceInstance.billingAddrState = org.billingAddrState
			invoiceInstance.billingAddrStreet = org.billingAddrStreet
			invoiceInstance.shippingAddrCountry = org.shippingAddrCountry
			invoiceInstance.shippingAddrLocation = org.shippingAddrLocation
			invoiceInstance.shippingAddrPoBox = org.shippingAddrPoBox
			invoiceInstance.shippingAddrPostalCode = org.shippingAddrPostalCode
			invoiceInstance.shippingAddrState = org.shippingAddrState
			invoiceInstance.shippingAddrStreet = org.shippingAddrStreet
		}
        return [invoiceInstance: invoiceInstance]
    }

	def copy() {
		def invoiceInstance = Invoice.get(params.id)
		if (!invoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
            redirect(action: 'list')
            return
        }

        invoiceInstance = new Invoice(invoiceInstance)
		render(view: 'create', model: [invoiceInstance: invoiceInstance])
	}

    def save() {
        def invoiceInstance = new Invoice(params)
		if (!invoiceInstance.save(flush: true)) {
            log.debug(invoiceInstance.errors)
            render(view: 'create', model: [invoiceInstance: invoiceInstance])
            return
        }

		invoiceInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoiceInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: invoiceInstance.id)
		}
    }

    def show() {
        def invoiceInstance = Invoice.get(params.id)
        if (!invoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
            redirect(action: 'list')
            return
        }

        return [invoiceInstance: invoiceInstance]
    }

    def edit() {
        def invoiceInstance = Invoice.get(params.id)
        if (!invoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
            redirect(action: 'list')
            return
        }

		if (session.user.admin || invoiceInstance.stage.id < 902) {
			return [invoiceInstance: invoiceInstance]
		}
		redirect(action: 'list')
    }

    def update() {
        def invoiceInstance = Invoice.get(params.id)
        if (!invoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (invoiceInstance.version > version) {
                invoiceInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'invoice.label', default: 'Invoice')] as Object[], "Another user has updated this Invoice while you were editing")
                render(view: 'edit', model: [invoiceInstance: invoiceInstance])
                return
            }
        }
		if (params.autoNumber) {
			params.number = invoiceInstance.number
		}
        invoiceInstance.properties = params
        invoiceInstance.items?.retainAll { it != null }

        if (!invoiceInstance.save(flush: true)) {
            log.debug(invoiceInstance.errors)
            render(view: 'edit', model: [invoiceInstance: invoiceInstance])
            return
        }

		invoiceInstance.reindex()

        flash.message = message(code: 'default.updated.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoiceInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: invoiceInstance.id)
		}
    }

    def delete() {
        def invoiceInstance = Invoice.get(params.id)
        if (invoiceInstance && params.confirmed) {
			if (!session.user.admin && invoiceInstance.stage.id >= 902) {
				redirect(action: 'list')
			}
            try {
                invoiceInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'invoice.label', default: 'Invoice')])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'invoice.label', default: 'Invoice')])
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])
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

		def c = Invoice.createCriteria()
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

		render(contentType: 'text/json') {
			array {
				for (i in list) {
					invoice id: i.id, name: i.fullName
				}
			}
		}
	}

	def print() {
        def invoiceInstance = Invoice.get(params.id)
        if (!invoiceInstance) {
            render(status: 404)
            return
        }

		def data = [
			transaction: invoiceInstance,
			items: invoiceInstance.items,
			organization: invoiceInstance.organization,
			person: invoiceInstance.person,
			user: session.user,
			fullNumber: invoiceInstance.fullNumber,
			taxRates: invoiceInstance.taxRateSums,
			values: [
		        subtotalNet: invoiceInstance.subtotalNet,
				subtotalGross: invoiceInstance.subtotalGross,
				discountPercentAmount: invoiceInstance.discountPercentAmount,
				total: invoiceInstance.total
			],
			watermark: params.duplicate ? 'duplicate' : ''
		]
		String xml = (data as XML).toString()
//		println xml

		GString fileName = "${message(code: 'invoice.label')} ${invoiceInstance.fullNumber}"
		if (params.duplicate) {
			fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
		}
		fileName += ".pdf"

		ByteArrayOutputStream baos = new ByteArrayOutputStream()
		fopService.generatePdf(
			new StringReader(xml), '/WEB-INF/data/fo/invoice-fo.xsl', baos
		)
		response.contentType = 'application/pdf'
		response.addHeader 'Content-Disposition',
			"attachment; filename=\"${fileName}\""
		response.contentLength = baos.size()
		response.outputStream.write(baos.toByteArray())
		response.outputStream.flush()
	}

	def listUnpaidBills() {
		InvoiceStage stage = InvoiceStage.get(902)
		def c = Invoice.createCriteria()
		def invoiceInstanceList = c.list {
			eq('stage', stage)
			and {
				le('dueDatePayment', new Date())
			}
			order('docDate', 'desc')
		}
		return [invoiceInstanceList: invoiceInstanceList]
	}
}
