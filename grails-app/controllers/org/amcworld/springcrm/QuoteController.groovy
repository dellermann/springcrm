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

import javax.servlet.http.HttpServletResponse


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
    LruService lruService


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
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

        [quoteInstanceList: list, quoteInstanceTotal: count]
    }

    def listEmbedded(Long organization, Long person) {
        def l
        def count
        def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            def organizationInstance = Organization.get(organization)
            l = Quote.findAllByOrganization(organizationInstance, params)
            count = Quote.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            def personInstance = Person.get(person)
            l = Quote.findAllByPerson(personInstance, params)
            count = Quote.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        }
        [quoteInstanceList: l, quoteInstanceTotal: count, linkParams: linkParams]
    }

    def create() {
        def quoteInstance = new Quote()
        quoteInstance.properties = params

        quoteInstance.copyAddressesFromOrganization()

        [quoteInstance: quoteInstance]
    }

    def copy(Long id) {
        def quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), id])
            redirect action: 'list'
            return
        }

        quoteInstance = new Quote(quoteInstance)
        render view: 'create', model: [quoteInstance: quoteInstance]
    }

    def save() {
        def quoteInstance = new Quote()
        if (!invoicingTransactionService.saveInvoicingTransaction(quoteInstance, params)) {
            render view: 'create', model: [quoteInstance: quoteInstance]
            return
        }

        lruService.recordItem controllerName, quoteInstance
        quoteInstance.index()

        flash.message = message(code: 'default.created.message', args: [message(code: 'quote.label', default: 'Quote'), quoteInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: quoteInstance.id
        }
    }

    def show(Long id) {
        def quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), id])
            redirect action: 'list'
            return
        }

        [quoteInstance: quoteInstance, printTemplates: fopService.templateNames]
    }

    def edit(Long id) {
        def quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), id])
            redirect action: 'list'
            return
        }

        [quoteInstance: quoteInstance]
    }

    def update(Long id) {
        def quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (quoteInstance.version > version) {
                quoteInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'quote.label', default: 'Quote')] as Object[], 'Another user has updated this Quote while you were editing')
                render view: 'edit', model: [quoteInstance: quoteInstance]
                return
            }
        }

        if (!invoicingTransactionService.saveInvoicingTransaction(quoteInstance, params)) {
            render view: 'edit', model: [quoteInstance: quoteInstance]
            return
        }

        lruService.recordItem controllerName, quoteInstance
        quoteInstance.reindex()

        flash.message = message(code: 'default.updated.message', args: [message(code: 'quote.label', default: 'Quote'), quoteInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: quoteInstance.id
        }
    }

    def delete(Long id) {
        def quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
            return
        }

        try {
            quoteInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'quote.label', default: 'Quote')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'quote.label', default: 'Quote')])
            redirect action: 'show', id: id
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

    def print(Long id, String template) {
        def quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        String xml = fopService.generateXml(quoteInstance, !!params.duplicate)
        GString fileName = "${message(code: 'quote.label')} ${quoteInstance.fullNumber}"
        if (params.duplicate) {
            fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
        }
        fileName += ".pdf"

        fopService.outputPdf xml, 'quote', template, response, fileName
    }
}
