/*
 * CreditMemoController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code CreditMemoController} contains actions which manage credit
 * memos.
 *
 * @author	Daniel Ellermann
 * @version 1.2
 */
class CreditMemoController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

	def fopService
	def seqNumberService


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def list, count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = CreditMemo.findAllBySubjectLike(searchFilter, params)
            count = CreditMemo.countBySubjectLike(searchFilter)
        } else {
            list = CreditMemo.list(params)
            count = CreditMemo.count()
        }

        return [creditMemoInstanceList: list, creditMemoInstanceTotal: count]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
            if (organizationInstance) {
    			l = CreditMemo.findAllByOrganization(organizationInstance, params)
    			count = CreditMemo.countByOrganization(organizationInstance)
    			linkParams = [organization: organizationInstance.id]
            }
		} else if (params.person) {
			def personInstance = Person.get(params.person)
            if (personInstance) {
    			l = CreditMemo.findAllByPerson(personInstance, params)
    			count = CreditMemo.countByPerson(personInstance)
    			linkParams = [person: personInstance.id]
            }
		} else if (params.invoice) {
			def invoiceInstance = Invoice.get(params.invoice)
            if (invoiceInstance) {
    			l = CreditMemo.findAllByInvoice(invoiceInstance, params)
    			count = CreditMemo.countByInvoice(invoiceInstance)
    			linkParams = [invoice: invoiceInstance.id]
            }
		} else if (params.dunning) {
			def dunningInstance = Dunning.get(params.dunning)
            if (dunningInstance) {
    			l = CreditMemo.findAllByDunning(dunningInstance, params)
    			count = CreditMemo.countByDunning(dunningInstance)
    			linkParams = [dunning: dunningInstance.id]
            }
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
            redirect(action: 'show', id: params.id)
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
        params.id = creditMemoInstance.ident()

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

        /*
         * The original implementation which worked in Grails 2.0.0.
         */
        creditMemoInstance.properties = params
//        creditMemoInstance.items?.retainAll { it != null }

        /*
         * XXX  This code is necessary because the default implementation
         *      in Grails does not work.  The above lines worked in Grails
         *      2.0.0.  Now, either data binding or saving does not work
         *      correctly if items were deleted and gaps in the indices
         *      occurred (e. g. 0, 1, null, null, 4) or the items were
         *      re-ordered.  Then I observed cluttering in saved data
         *      columns.
         *      The following lines do not make me happy but they work.
         *      In future, this problem hopefully will be fixed in Grails
         *      so we can remove these lines.
         */
        creditMemoInstance.items?.clear()
        for (int i = 0; params."items[${i}]"; i++) {
            if (params."items[${i}]".id != 'null') {
                creditMemoInstance.addToItems(params."items[${i}]")
            }
        }

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

        creditMemoInstance.properties['stage.id', 'paymentDate', 'paymentAmount', 'paymentMethod.id'] = params

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
        if (!creditMemoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'creditMemo.label', default: 'Credit memo'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

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
    }

	def print() {
        def creditMemoInstance = CreditMemo.get(params.id)
        if (!creditMemoInstance) {
            render(status: 404)
            return
        }

        String xml = fopService.generateXml(
            creditMemoInstance, !!params.duplicate, [
                invoice: creditMemoInstance.invoice,
                invoiceFullNumber: creditMemoInstance.invoice?.fullNumber,
                dunning: creditMemoInstance.dunning,
                dunningFullNumber: creditMemoInstance.dunning?.fullNumber,
                paymentMethod: creditMemoInstance.paymentMethod?.name
            ]
        )
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
