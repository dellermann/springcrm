/*
 * InvoiceController.groovy
 *
 * Copyright (c) 2011-2022, Daniel Ellermann
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
 * The class {@code InvoiceController} contains actions which manage invoices.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class InvoiceController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService
    SeqNumberService seqNumberService
    UserService userService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<Invoice> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Invoice.findAllBySubjectLike(searchFilter, params)
            count = Invoice.countBySubjectLike(searchFilter)
        } else if (params.year) {
            int start = (params.int('year') % 1000) * 1000
            int end = start + 999
            list = Invoice.findAllByNumberBetween(start, end, params)
            count = Invoice.countByNumberBetween(start, end)
        } else {
            list = Invoice.list(params)
            count = Invoice.count()
        }

        [
            invoiceInstanceList: list, invoiceInstanceTotal: count,
            yearStart: invoicingTransactionService.findYearStart('I'),
            yearEnd: invoicingTransactionService.findYearEnd('I'),
        ]
    }

    def listEmbedded(Long organization, Long person, Long quote,
                     Long salesOrder)
    {
        List<Invoice> l = null
        int count = 0
        Map<String, Object> linkParams = null

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            def organizationInstance = Organization.get(organization)
            l = Invoice.findAllByOrganization(organizationInstance, params)
            count = Invoice.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            def personInstance = Person.get(person)
            l = Invoice.findAllByPerson(personInstance, params)
            count = Invoice.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        } else if (quote) {
            def quoteInstance = Quote.get(params.long('quote'))
            l = Invoice.findAllByQuote(quoteInstance, params)
            count = Invoice.countByQuote(quoteInstance)
            linkParams = [quote: quoteInstance.id]
        } else if (salesOrder) {
            def salesOrderInstance = SalesOrder.get(salesOrder)
            l = Invoice.findAllBySalesOrder(salesOrderInstance, params)
            count = Invoice.countBySalesOrder(salesOrderInstance)
            linkParams = [salesOrder: salesOrderInstance.id]
        }

        [
            invoiceInstanceList: l, invoiceInstanceTotal: count,
            linkParams: linkParams
        ]
    }

    def create() {
        Invoice invoiceInstance
        if (params.quote) {
            def quoteInstance = Quote.get(params.long('quote'))
            invoiceInstance = new Invoice(quoteInstance)
            invoiceInstance.userService = userService
        } else if (params.salesOrder) {
            def salesOrderInstance = SalesOrder.get(params.long('salesOrder'))
            invoiceInstance = new Invoice(salesOrderInstance)
            invoiceInstance.quote = salesOrderInstance.quote
            invoiceInstance.userService = userService
        } else {
            invoiceInstance = new Invoice(params)
        }

        invoiceInstance.copyAddressesFromOrganization()

        [invoiceInstance: invoiceInstance]
    }

    def copy(Long id) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'invoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        invoiceInstance = new Invoice(invoiceInstance)
        render view: 'create', model: [invoiceInstance: invoiceInstance]
    }

    def save() {
        Invoice invoiceInstance = new Invoice(params)
        if (!invoicingTransactionService.save(invoiceInstance, params)) {
            render view: 'create', model: [invoiceInstance: invoiceInstance]
            return
        }

        Quote quoteInstance = invoiceInstance.quote ?:
            invoiceInstance.salesOrder?.quote
        if (quoteInstance) {
            quoteInstance.stage = QuoteStage.get(603L)
            quoteInstance.save flush: true
        }

        request.invoiceInstance = invoiceInstance
        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'invoice.label'), invoiceInstance.toString()]
        )

        redirect action: 'show', id: invoiceInstance.id
    }

    def show(Long id) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'invoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        [invoiceInstance: invoiceInstance]
    }

    def edit(Long id) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'invoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (!session.credential.admin && invoiceInstance.stage.id >= 902) {
            redirect action: 'index'
            return
        }

        [invoiceInstance: invoiceInstance]
    }

    def editPayment(Long id) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'invoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        [invoiceInstance: invoiceInstance]
    }

    def update(Long id) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'invoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (!session.credential.admin && invoiceInstance.stage.id >= 902) {
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (invoiceInstance.version > version) {
                invoiceInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'invoice.label')] as Object[],
                    'Another user has updated this Invoice while you were editing'
                )
                render view: 'edit', model: [invoiceInstance: invoiceInstance]
                return
            }
        }

        if (!invoicingTransactionService.save(invoiceInstance, params)) {
            render view: 'edit', model: [invoiceInstance: invoiceInstance]
            return
        }

        request.invoiceInstance = invoiceInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'invoice.label'), invoiceInstance.toString()]
        )

        redirect action: 'show', id: invoiceInstance.id
    }

    def updatePayment(Long id) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'invoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (invoiceInstance.version > version) {
                invoiceInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'invoice.label')] as Object[],
                    'Another user has updated this Invoice while you were editing'
                )
                render view: 'edit', model: [invoiceInstance: invoiceInstance]
                return
            }
        }

        invoiceInstance.properties = params.findAll {
            it.key.toString() in [
                'stage', 'paymentDate', 'paymentAmount', 'paymentMethod'
            ]
        }

        if (!invoiceInstance.save(flush: true)) {
            render view: 'edit', model: [invoiceInstance: invoiceInstance]
            return
        }

        request.invoiceInstance = invoiceInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'invoice.label'), invoiceInstance.toString()]
        )

        redirect action: 'show', id: invoiceInstance.id
    }

    def delete(Long id) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'invoice.label'), id]
            )

            redirect action: 'index'
            return
        }

        if (!session.credential.admin && invoiceInstance.stage.id >= 902) {
            redirect action: 'index'
            return
        }

        request.invoiceInstance = invoiceInstance
        try {
            invoiceInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'invoice.label')]
            )

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignored) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'invoice.label')]
            )

            redirect action: 'show', id: id
        }
    }

    def find() {
        Integer number = null
        try {
            number = params.name as Integer
        } catch (NumberFormatException ignored) { /* ignored */ }
        Organization organization = params.organization \
            ? Organization.get(params.long('organization')) \
            : null

        def c = Invoice.createCriteria()
        List<Invoice> list = (List<Invoice>) c.list {
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

        [invoiceInstanceList: list]
    }

    def print(Long id, String template) {
        Invoice invoiceInstance = Invoice.get(id)
        if (!invoiceInstance) {
            render status: SC_NOT_FOUND
            return
        }

        String xml = invoicingTransactionService.generateXML(
            invoiceInstance,
            invoiceInstance.createUser ?: session.credential.loadUser(),
            params.boolean('duplicate') ?: false
        )
        StringBuilder buf = new StringBuilder()
        buf << (message(code: 'invoice.label') as String)
        buf << ' ' << seqNumberService.getFullNumber(invoiceInstance)
        if (params.duplicate) {
            buf << ' ('
            buf << (message(code: 'invoicingTransaction.duplicate') as String)
            buf << ')'
        }
        buf << '.pdf'

        fopService.outputPdf xml, 'invoice', template, response, buf.toString()
    }

    /**
     * Lists all invoices which have unpaid amount.
     *
     * @return  a map containing the model for the view
     */
    def listUnpaidBills() {
        List<Invoice> invoiceInstanceList =
            invoicingTransactionService.findUnpaidBills(
                (Credential) session.credential
            )

        [invoiceInstanceList: invoiceInstanceList]
    }

    def getClosingBalance(Long id) {
        [invoiceInstance: Invoice.get(id)]
    }
}
