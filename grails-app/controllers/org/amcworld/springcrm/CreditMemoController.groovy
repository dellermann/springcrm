package org.amcworld.springcrm

import grails.converters.XML
import org.springframework.dao.DataIntegrityViolationException

class CreditMemoController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def fopService
	def seqNumberService

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        return [creditMemoInstanceList: CreditMemo.list(params), creditMemoInstanceTotal: CreditMemo.count()]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = CreditMemo.findAllByOrganization(organizationInstance, params)
			count = CreditMemo.countByOrganization(organizationInstance)
			linkParams = [organization: organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = CreditMemo.findAllByPerson(personInstance, params)
			count = CreditMemo.countByPerson(personInstance)
			linkParams = [person: personInstance.id]
		} else if (params.invoice) {
			def invoiceInstance = Invoice.get(params.invoice)
			l = CreditMemo.findAllByInvoice(invoiceInstance, params)
			count = CreditMemo.countByInvoice(invoiceInstance)
			linkParams = [invoice: invoiceInstance.id]
		} else if (params.dunning) {
			def dunningInstance = Dunning.get(params.dunning)
			l = CreditMemo.findAllByDunning(dunningInstance, params)
			count = CreditMemo.countByDunning(dunningInstance)
			linkParams = [dunning: dunningInstance.id]
		}
		return [creditMemoInstanceList: l, creditMemoInstanceTotal: count, linkParams: linkParams]
	}

    def create() {
        def creditMemoInstance
		if (params.invoice) {
			def invoiceInstance = Invoice.get(params.invoice)
			creditMemoInstance = new CreditMemo(invoiceInstance)
		} else if (params.dunning) {
			def dunningInstance = Dunning.get(params.dunning)
			creditMemoInstance = new CreditMemo(dunningInstance)
		} else {
			creditMemoInstance = new CreditMemo()
			creditMemoInstance.properties = params
		}

		Organization org = creditMemoInstance.organization
		if (org) {
			creditMemoInstance.billingAddrCountry = org.billingAddrCountry
			creditMemoInstance.billingAddrLocation = org.billingAddrLocation
			creditMemoInstance.billingAddrPoBox = org.billingAddrPoBox
			creditMemoInstance.billingAddrPostalCode = org.billingAddrPostalCode
			creditMemoInstance.billingAddrState = org.billingAddrState
			creditMemoInstance.billingAddrStreet = org.billingAddrStreet
			creditMemoInstance.shippingAddrCountry = org.shippingAddrCountry
			creditMemoInstance.shippingAddrLocation = org.shippingAddrLocation
			creditMemoInstance.shippingAddrPoBox = org.shippingAddrPoBox
			creditMemoInstance.shippingAddrPostalCode = org.shippingAddrPostalCode
			creditMemoInstance.shippingAddrState = org.shippingAddrState
			creditMemoInstance.shippingAddrStreet = org.shippingAddrStreet
		}

        return [creditMemoInstance: creditMemoInstance]
    }

	def copy() {
		def creditMemoInstance = CreditMemo.get(params.id)
		if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
            redirect(action: 'list')
            return
        }

        creditMemoInstance = new CreditMemo(creditMemoInstance)
		render(view: 'create', model: [creditMemoInstance: creditMemoInstance])
	}

    def save() {
        def creditMemoInstance = new CreditMemo(params)
        if (!creditMemoInstance.save(flush: true)) {
            render(view: 'create', model: [creditMemoInstance: creditMemoInstance])
            return
        }

		creditMemoInstance.index()

		def invoiceInstance = creditMemoInstance.invoice
		if (invoiceInstance) {
			invoiceInstance.stage = InvoiceStage.get(907)
			invoiceInstance.save(flush: true)
			invoiceInstance.reindex()
		}
		def dunningInstance = creditMemoInstance.dunning
		if (dunningInstance) {
			dunningInstance.stage = DunningStage.get(2206)
			dunningInstance.save(flush: true)
			dunningInstance.reindex()
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), creditMemoInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: creditMemoInstance.id)
		}
    }

    def show() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
            redirect(action: 'list')
            return
        }

        return [creditMemoInstance: creditMemoInstance, printTemplates: fopService.templateNames]
    }

    def edit() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
            redirect(action: 'list')
            return
        }

        if (session.user.admin || creditMemoInstance.stage.id < 2502) {
			return [creditMemoInstance: creditMemoInstance]
		}
		redirect(action: 'list')
    }

    def editPayment() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
            redirect(action: 'list')
            return
        }

        return [creditMemoInstance: creditMemoInstance]
    }

    def update() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
            redirect(action: 'list')
            return
        }

        if (!session.user.admin && creditMemoInstance.stage.id >= 2502) {
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (creditMemoInstance.version > version) {

                creditMemoInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'creditMemo.label', default: 'Credit memo')] as Object[], "Another user has updated this CreditMemo while you were editing")
                render(view: 'edit', model: [creditMemoInstance: creditMemoInstance])
                return
            }
        }
		if (params.autoNumber) {
			params.number = creditMemoInstance.number
		}
        creditMemoInstance.properties = params
        creditMemoInstance.items?.retainAll { it != null }
        if (!creditMemoInstance.save(flush: true)) {
            render(view: 'edit', model: [creditMemoInstance: creditMemoInstance])
            return
        }

		creditMemoInstance.reindex()

		def invoiceInstance = creditMemoInstance.invoice
		if (invoiceInstance) {
			invoiceInstance.stage = InvoiceStage.get(907)
			invoiceInstance.save(flush: true)
			invoiceInstance.reindex()
		}
		def dunningInstance = creditMemoInstance.dunning
		if (dunningInstance) {
			dunningInstance.stage = DunningStage.get(2206)
			dunningInstance.save(flush: true)
			dunningInstance.reindex()
		}

        flash.message = message(code: 'default.updated.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), creditMemoInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: creditMemoInstance.id)
		}
    }

    def updatePayment() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (creditMemoInstance.version > version) {

                creditMemoInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'creditMemo.label', default: 'Credit memo')] as Object[], "Another user has updated this CreditMemo while you were editing")
                render(view: 'edit', model: [creditMemoInstance: creditMemoInstance])
                return
            }
        }

        creditMemoInstance.properties = params.findAll { it.key in ['stage.id', 'paymentDate', 'paymentAmount', 'paymentMethod.id'] }

        if (!creditMemoInstance.save(flush: true)) {
            render(view: 'edit', model: [creditMemoInstance: creditMemoInstance])
            return
        }

        creditMemoInstance.reindex()

        flash.message = message(code: 'default.updated.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), creditMemoInstance.toString()])
        if (params.returnUrl) {
            redirect(url: params.returnUrl)
        } else {
            redirect(action: 'show', id: creditMemoInstance.id)
        }
    }

    def delete() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoInstance && params.confirmed) {
			if (!session.user.admin && creditMemoInstance.stage.id >= 2502) {
				redirect(action: 'list')
                return
			}
            try {
                creditMemoInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'creditMemo.label', default: 'Credit memo')])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'creditMemo.label', default: 'Credit memo')])
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

	def print() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            render(status: 404)
            return
        }

		def data = [
			transaction: creditMemoInstance,
			items: creditMemoInstance.items,
			organization: creditMemoInstance.organization,
			person: creditMemoInstance.person,
			invoice: creditMemoInstance.invoice,
			invoiceFullNumber: creditMemoInstance.invoice?.fullNumber,
			dunning: creditMemoInstance.dunning,
			dunningFullNumber: creditMemoInstance.dunning?.fullNumber,
			user: session.user,
			fullNumber: creditMemoInstance.fullNumber,
			paymentMethod: creditMemoInstance.paymentMethod?.name,
			taxRates: creditMemoInstance.taxRateSums,
			values: [
		        subtotalNet: creditMemoInstance.subtotalNet,
				subtotalGross: creditMemoInstance.subtotalGross,
				discountPercentAmount: creditMemoInstance.discountPercentAmount,
				total: creditMemoInstance.total
			],
			watermark: params.duplicate ? 'duplicate' : '',
            client: Client.loadAsMap()
		]
		String xml = (data as XML).toString()
//		println xml

		GString fileName = "${message(code: 'creditMemo.label')} ${creditMemoInstance.fullNumber}"
		if (params.duplicate) {
			fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
		}
		fileName += ".pdf"

        fopService.outputPdf(
            xml, 'credit-memo', params.template, response, fileName
        )
	}
}
