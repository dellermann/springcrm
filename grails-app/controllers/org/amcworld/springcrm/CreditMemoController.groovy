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
        [creditMemoInstanceList: CreditMemo.list(params), creditMemoInstanceTotal: CreditMemo.count()]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
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
		[creditMemoInstanceList: l, creditMemoInstanceTotal: count, linkParams: linkParams]
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
		if (creditMemoInstance) {
			creditMemoInstance = new CreditMemo(creditMemoInstance)
			render(view: 'create', model: [creditMemoInstance: creditMemoInstance])
		} else {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
			redirect(action: 'show', id: creditMemoInstance.id)
		}
	}

    def save() {
        def creditMemoInstance = new CreditMemo(params)
        if (creditMemoInstance.save(flush: true)) {
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

			flash.message = message(code: 'default.created.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), creditMemoInstance.toString()])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'show', id: creditMemoInstance.id)
			}
        } else {
            render(view: 'create', model: [creditMemoInstance: creditMemoInstance])
        }
    }

    def show() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), params.id])
            redirect(action: 'list')
        } else {
            [creditMemoInstance: creditMemoInstance]
        }
    }

    def edit() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), params.id])
            redirect(action: 'list')
        } else {
			if (session.user.admin || creditMemoInstance.stage.id < 2502) {
				return [creditMemoInstance: creditMemoInstance]
			}
			redirect(action: 'list')
        }
    }

    def update() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (creditMemoInstance.version > version) {

                    creditMemoInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'creditMemo.label', default: 'CreditMemo')] as Object[], "Another user has updated this CreditMemo while you were editing")
                    render(view: 'edit', model: [creditMemoInstance: creditMemoInstance])
                    return
                }
            }
			if (params.autoNumber) {
				params.number = creditMemoInstance.number
			}
            creditMemoInstance.properties = params

			/*
			 * XXX 	This code is necessary because the default implementation
			 * 		in Grails does not work. Either data binding or saving does
			 * 		not work correctly if items were deleted and gaps in the
			 * 		indices occurred (e. g. 0, 1, null, null, 4) or the items
			 * 		were re-ordered. Then I observed cluttering in saved data
			 * 		columns.
			 * 		The following lines do not make me happy but they work. In
			 * 		future, this problem may be fixed in Grails so we can
			 * 		remove these lines.
			 */
			creditMemoInstance.items?.clear()
			for (int i = 0; params."items[${i}]"; i++) {
				if (params."items[${i}]".id != 'null') {
					creditMemoInstance.addToItems(params."items[${i}]")
				}
			}
            if (!creditMemoInstance.hasErrors() && creditMemoInstance.save(flush: true)) {
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

                flash.message = message(code: 'default.updated.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), creditMemoInstance.toString()])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'show', id: creditMemoInstance.id)
				}
            } else {
                render(view: 'edit', model: [creditMemoInstance: creditMemoInstance])
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), params.id])
            redirect(action: 'list')
        }
    }

    def delete() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoInstance && params.confirmed) {
			if (!session.user.admin && creditMemoInstance.stage.id >= 2502) {
				redirect(action: 'list')
			}
            try {
                creditMemoInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo')])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo')])
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'CreditMemo'), params.id])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

	def print() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (creditMemoInstance) {
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
				watermark: params.duplicate ? 'duplicate' : ''
			]
			String xml = (data as XML).toString()
//			println xml

			GString fileName = "${message(code: 'creditMemo.label')} ${creditMemoInstance.fullNumber}"
			if (params.duplicate) {
				fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
			}
			fileName += ".pdf"

			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			fopService.generatePdf(
				new StringReader(xml), '/WEB-INF/data/fo/credit-memo-fo.xsl',
				baos
			)
			response.contentType = 'application/pdf'
			response.addHeader 'Content-Disposition',
				"attachment; filename=\"${fileName}\""
			response.contentLength = baos.size()
			response.outputStream.write(baos.toByteArray())
			response.outputStream.flush()
		} else {
			render(status: 404)
		}
	}
}
