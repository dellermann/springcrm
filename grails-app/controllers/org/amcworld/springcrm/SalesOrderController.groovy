/*
 * SalesOrderController.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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
 * The class {@code SalesOrderController} contains actions which manage sales
 * orders.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class SalesOrderController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<SalesOrder> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = SalesOrder.findAllBySubjectLike(searchFilter, params)
            count = SalesOrder.countBySubjectLike(searchFilter)
        } else {
            list = SalesOrder.list(params)
            count = SalesOrder.count()
        }

        [salesOrderInstanceList: list, salesOrderInstanceTotal: count]
    }

    def listEmbedded(Long organization, Long person, Long quote) {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<SalesOrder> l = null
        int count = 0
        def linkParams = null
        if (organization) {
            def organizationInstance = Organization.get(organization)
            l = SalesOrder.findAllByOrganization(organizationInstance, params)
            count = SalesOrder.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            def personInstance = Person.get(person)
            l = SalesOrder.findAllByPerson(personInstance, params)
            count = SalesOrder.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        } else if (quote) {
            def quoteInstance = Quote.get(quote)
            l = SalesOrder.findAllByQuote(quoteInstance, params)
            count = SalesOrder.countByQuote(quoteInstance)
            linkParams = [quote: quoteInstance.id]
        }

        [
            salesOrderInstanceList: l, salesOrderInstanceTotal: count,
            linkParams: linkParams
        ]
    }

    def create() {
        SalesOrder salesOrderInstance
        if (params.quote) {
            Quote quoteInstance = Quote.get(params.long('quote'))
            salesOrderInstance = new SalesOrder(quoteInstance)
        } else {
            salesOrderInstance = new SalesOrder(params)
        }

        salesOrderInstance.copyAddressesFromOrganization()

        [salesOrderInstance: salesOrderInstance]
    }

    def copy(Long id) {
        SalesOrder salesOrderInstance = SalesOrder.get(id)
        if (!salesOrderInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'salesOrder.label'), id]
            )
            redirect action: 'index'
            return
        }

        salesOrderInstance = new SalesOrder(salesOrderInstance)
        render view: 'create', model: [salesOrderInstance: salesOrderInstance]
    }

    def save() {
        SalesOrder salesOrderInstance = new SalesOrder(params)
        if (!invoicingTransactionService.save(salesOrderInstance, params)) {
            render view: 'create',
                model: [salesOrderInstance: salesOrderInstance]
            return
        }

        Quote quoteInstance = salesOrderInstance.quote
        if (quoteInstance) {
            quoteInstance.stage = QuoteStage.get(603L)
            quoteInstance.save flush: true
        }

        request.salesOrderInstance = salesOrderInstance
        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'salesOrder.label'),
                salesOrderInstance.toString()
            ]
        )

        redirect action: 'show', id: salesOrderInstance.id
    }

    def show(Long id) {
        SalesOrder salesOrderInstance = SalesOrder.get(id)
        if (!salesOrderInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'salesOrder.label'), id]
            )
            redirect action: 'index'
            return
        }

        [salesOrderInstance: salesOrderInstance]
    }

    def edit(Long id) {
        SalesOrder salesOrderInstance = SalesOrder.get(id)
        if (!salesOrderInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'salesOrder.label'), id]
            )
            redirect action: 'index'
            return
        }

        [salesOrderInstance: salesOrderInstance]
    }

    def update(Long id) {
        SalesOrder salesOrderInstance = SalesOrder.get(id)
        if (!salesOrderInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'salesOrder.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (salesOrderInstance.version > version) {
                salesOrderInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'salesOrder.label')] as Object[],
                    'Another user has updated this SalesOrder while you were editing'
                )
                render view: 'edit',
                    model: [salesOrderInstance: salesOrderInstance]
                return
            }
        }

        if (!invoicingTransactionService.save(salesOrderInstance, params)) {
            render view: 'edit',
                model: [salesOrderInstance: salesOrderInstance]
            return
        }

        request.salesOrderInstance = salesOrderInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'salesOrder.label'),
                salesOrderInstance.toString()
            ]
        )

        redirect action: 'show', id: salesOrderInstance.id
    }

    def delete(Long id) {
        SalesOrder salesOrderInstance = SalesOrder.get(id)
        if (!salesOrderInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'salesOrder.label'), id]
            )

            redirect action: 'index'
            return
        }

        request.salesOrderInstance = salesOrderInstance
        try {
            salesOrderInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'salesOrder.label')]
            )

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'salesOrder.label')]
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

        def c = SalesOrder.createCriteria()
        List<SalesOrder> list = (List<SalesOrder>) c.list {
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

        [salesOrderInstanceList: list]
    }

    def print(Long id, String template) {
        SalesOrder salesOrderInstance = SalesOrder.get(id)
        if (!salesOrderInstance) {
            render status: SC_NOT_FOUND
            return
        }

        String xml = invoicingTransactionService.generateXML(
            salesOrderInstance,
            salesOrderInstance.createUser
                ?: ((Credential) session.credential).loadUser(),
            params.boolean('duplicate') ?: false
        )
        GString fileName =
            "${message(code: 'salesOrder.label')} ${salesOrderInstance.fullNumber}"
        if (params.duplicate) {
            fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
        }
        fileName += ".pdf"

        fopService.outputPdf xml, 'sales-order', template, response, fileName
    }
}
