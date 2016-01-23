/*
 * QuoteController.groovy
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


/**
 * The class {@code QuoteController} contains actions which manage quotes.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class QuoteController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<Quote> list
        int count
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
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<Quote> l
        int count
        def linkParams
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

        [
            quoteInstanceList: l, quoteInstanceTotal: count,
            linkParams: linkParams
        ]
    }

    def create() {
        Quote quoteInstance = new Quote(params)
        quoteInstance.copyAddressesFromOrganization()

        [quoteInstance: quoteInstance]
    }

    def copy(Long id) {
        Quote quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'quote.label'), id]
            )
            redirect action: 'index'
            return
        }

        quoteInstance = new Quote(quoteInstance)
        render view: 'create', model: [quoteInstance: quoteInstance]
    }

    def save() {
        Quote quoteInstance = new Quote()
        if (!invoicingTransactionService.save(quoteInstance, params)) {
            render view: 'create', model: [quoteInstance: quoteInstance]
            return
        }

        request.quoteInstance = quoteInstance
        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'quote.label'),
                quoteInstance.toString()
            ]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: quoteInstance.id
        }
    }

    def show(Long id) {
        Quote quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'quote.label'), id]
            )
            redirect action: 'index'
            return
        }

        [quoteInstance: quoteInstance]
    }

    def edit(Long id) {
        Quote quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'quote.label'), id]
            )
            redirect action: 'index'
            return
        }

        [quoteInstance: quoteInstance]
    }

    def update(Long id) {
        Quote quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'quote.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (quoteInstance.version > version) {
                quoteInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'quote.label')] as Object[],
                    'Another user has updated this Quote while you were editing'
                )
                render view: 'edit', model: [quoteInstance: quoteInstance]
                return
            }
        }

        if (!invoicingTransactionService.save(quoteInstance, params)) {
            render view: 'edit', model: [quoteInstance: quoteInstance]
            return
        }

        request.quoteInstance = quoteInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'quote.label'), quoteInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: quoteInstance.id
        }
    }

    def delete(Long id) {
        Quote quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'quote.label'), id]
            )
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        try {
            quoteInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'quote.label')]
            )
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'quote.label')]
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
            ? Organization.get(params.organization) \
            : null

        def c = Quote.createCriteria()
        List<Quote> list = c.list {
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
                for (Quote q in list) {
                    quote id: q.id, number: q.fullNumber, name: q.subject,
                        fullName: q.fullName
                }
            }
        }
    }

    def print(Long id, String template) {
        Quote quoteInstance = Quote.get(id)
        if (!quoteInstance) {
            render status: SC_NOT_FOUND
            return
        }

        String xml = invoicingTransactionService.generateXML(
            quoteInstance,
            quoteInstance.createUser ?: session.credential.loadUser(),
            !!params.duplicate
        )
        GString fileName =
            "${message(code: 'quote.label')} ${quoteInstance.fullNumber}"
        if (params.duplicate) {
            fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
        }
        fileName += ".pdf"

        fopService.outputPdf xml, 'quote', template, response, fileName
    }
}
