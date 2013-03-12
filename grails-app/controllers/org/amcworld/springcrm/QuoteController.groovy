/*
 * QuoteController.groovy
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


/**
 * The class {@code QuoteController} contains actions which manage quotes.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class QuoteController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

	FopService fopService
    InvoicingTransactionService invoicingTransactionService
	SeqNumberService seqNumberService


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def list, count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Quote.findAllBySubjectLike(searchFilter, params)
            count = Quote.countBySubjectLike(searchFilter)
        } else {
            list = Quote.list(params)
            count = Quote.count()
        }

        return [quoteInstanceList: list, quoteInstanceTotal: count]
    }

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = Quote.findAllByOrganization(organizationInstance, params)
			count = Quote.countByOrganization(organizationInstance)
			linkParams = [organization: organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = Quote.findAllByPerson(personInstance, params)
			count = Quote.countByPerson(personInstance)
			linkParams = [person: personInstance.id]
		}
		return [quoteInstanceList: l, quoteInstanceTotal: count, linkParams: linkParams]
	}

    def create() {
        def quoteInstance = new Quote()
        quoteInstance.properties = params
		Organization org = quoteInstance.organization
		if (org) {
			quoteInstance.billingAddrCountry = org.billingAddrCountry
			quoteInstance.billingAddrLocation = org.billingAddrLocation
			quoteInstance.billingAddrPoBox = org.billingAddrPoBox
			quoteInstance.billingAddrPostalCode = org.billingAddrPostalCode
			quoteInstance.billingAddrState = org.billingAddrState
			quoteInstance.billingAddrStreet = org.billingAddrStreet
			quoteInstance.shippingAddrCountry = org.shippingAddrCountry
			quoteInstance.shippingAddrLocation = org.shippingAddrLocation
			quoteInstance.shippingAddrPoBox = org.shippingAddrPoBox
			quoteInstance.shippingAddrPostalCode = org.shippingAddrPostalCode
			quoteInstance.shippingAddrState = org.shippingAddrState
			quoteInstance.shippingAddrStreet = org.shippingAddrStreet
		}
        return [quoteInstance: quoteInstance]
    }

	def copy() {
		def quoteInstance = Quote.get(params.id)
		if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])
            redirect(action: 'list')
            return
        }

		quoteInstance = new Quote(quoteInstance)
		render(view: 'create', model: [quoteInstance: quoteInstance])
	}

    def save() {
        def quoteInstance = new Quote()
        if (!invoicingTransactionService.saveInvoicingTransaction(quoteInstance, params)) {
            log.debug quoteInstance.errors
            render(view: 'create', model: [quoteInstance: quoteInstance])
            return
        }
        params.id = quoteInstance.ident()

		quoteInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'quote.label', default: 'Quote'), quoteInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: quoteInstance.id)
		}
    }

    def show() {
        def quoteInstance = Quote.get(params.id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])
            redirect(action: 'list')
            return
        }

        return [quoteInstance: quoteInstance, printTemplates: fopService.templateNames]
    }

    def edit() {
        def quoteInstance = Quote.get(params.id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])
            redirect(action: 'list')
            return
        }

        return [quoteInstance: quoteInstance]
    }

    def update() {
        def quoteInstance = Quote.get(params.id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (quoteInstance.version > version) {
                quoteInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'quote.label', default: 'Quote')] as Object[], 'Another user has updated this Quote while you were editing')
                render(view: 'edit', model: [quoteInstance: quoteInstance])
                return
            }
        }

        if (!invoicingTransactionService.saveInvoicingTransaction(quoteInstance, params)) {
            log.debug quoteInstance.errors
            render(view: 'edit', model: [quoteInstance: quoteInstance])
            return
        }

		quoteInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'quote.label', default: 'Quote'), quoteInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: quoteInstance.id)
		}
    }

    def delete() {
        def quoteInstance = Quote.get(params.id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

        try {
            quoteInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'quote.label', default: 'Quote')])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'quote.label', default: 'Quote')])
            redirect(action: 'show', id: params.id)
        }
    }

	def find() {
		Integer number = null
		try {
			number = params.name as Integer
		} catch (NumberFormatException ignored) { /* ignored */ }
		def organization = params.organization ? Organization.get(params.organization) : null

		def c = Quote.createCriteria()
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
				for (q in list) {
					quote id: q.id, name: q.fullName
				}
			}
		}
	}

	def print() {
        def quoteInstance = Quote.get(params.id)
        if (!quoteInstance) {
            render(status: 404)
            return
        }

        String xml = fopService.generateXml(quoteInstance, !!params.duplicate)
		GString fileName = "${message(code: 'quote.label')} ${quoteInstance.fullNumber}"
		if (params.duplicate) {
			fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
		}
		fileName += ".pdf"

        fopService.outputPdf(xml, 'quote', params.template, response, fileName)
	}
}
