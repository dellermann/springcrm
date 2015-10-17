/*
 * DunningController.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import grails.converters.JSON


/**
 * The class {@code DunningController} contains actions which manage dunnings.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class DunningController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<Dunning> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Dunning.findAllBySubjectLike(searchFilter, params)
            count = Dunning.countBySubjectLike(searchFilter)
        } else {
            list = Dunning.list(params)
            count = Dunning.count()
        }

        [dunningInstanceList: list, dunningInstanceTotal: count]
    }

    def listEmbedded(Long organization, Long person, Long invoice) {
        List<Dunning> l
        int count
        def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            def organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                l = Dunning.findAllByOrganization(organizationInstance, params)
                count = Dunning.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        } else if (person) {
            def personInstance = Person.get(person)
            if (personInstance) {
                l = Dunning.findAllByPerson(personInstance, params)
                count = Dunning.countByPerson(personInstance)
                linkParams = [person: personInstance.id]
            }
        } else if (invoice) {
            def invoiceInstance = Invoice.get(invoice)
            if (invoiceInstance) {
                l = Dunning.findAllByInvoice(invoiceInstance, params)
                count = Dunning.countByInvoice(invoiceInstance)
                linkParams = [invoice: invoiceInstance.id]
            }
        }

        [
            dunningInstanceList: l, dunningInstanceTotal: count,
            linkParams: linkParams
        ]
    }

    def create() {
        Dunning dunningInstance
        if (params.invoice) {
            def invoiceInstance = Invoice.get(params.invoice)
            invoiceInstance.items?.clear()
            dunningInstance = new Dunning(invoiceInstance)
        } else {
            dunningInstance = new Dunning()
            dunningInstance.properties = params
        }

        dunningInstance.copyAddressesFromOrganization()

        ConfigHolder config = ConfigHolder.instance
        def serviceId = config['serviceIdDunningCharge']
        if (serviceId) {
            Service service = Service.get(serviceId.toType(Long))
            if (service) {
                dunningInstance.addToItems serviceToItem(service)
            }
        }
        serviceId = config['serviceIdDefaultInterest']
        if (serviceId) {
            Service service = Service.get(serviceId.toType(Long))
            if (service) {
                dunningInstance.addToItems serviceToItem(service)
            }
        }

        [dunningInstance: dunningInstance]
    }

    def copy(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )
            redirect action: 'index'
            return
        }

        dunningInstance = new Dunning(dunningInstance)
        render view: 'create', model: [dunningInstance: dunningInstance]
    }

    def save() {
        Dunning dunningInstance = new Dunning(params)
        if (!invoicingTransactionService.save(dunningInstance, params)) {
            render view: 'create', model: [quoteInstance: dunningInstance]
            return
        }

        request.dunningInstance = dunningInstance

        Invoice invoiceInstance = dunningInstance.invoice
        invoiceInstance.stage = InvoiceStage.get(904)
        invoiceInstance.save flush: true
        invoiceInstance.reindex()

        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'dunning.label'), dunningInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: dunningInstance.id
        }
    }

    def show(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )
            redirect action: 'index'
            return
        }

        [dunningInstance: dunningInstance]
    }

    def edit(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (!session.user.admin && dunningInstance.stage.id >= 2202) {
            redirect action: 'index'
            return
        }

        [dunningInstance: dunningInstance]
    }

    def editPayment(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )
            redirect action: 'index'
            return
        }

        [dunningInstance: dunningInstance]
    }

    def update(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (!session.user.admin && dunningInstance.stage.id >= 2202) {
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (dunningInstance.version > version) {
                dunningInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'dunning.label')] as Object[],
                    'Another user has updated this Dunning while you were editing'
                )
                render view: 'edit', model: [dunningInstance: dunningInstance]
                return
            }
        }

        if (!invoicingTransactionService.save(dunningInstance, params)) {
            render view: 'edit', model: [dunningInstance: dunningInstance]
            return
        }

        Invoice invoiceInstance = dunningInstance.invoice
        invoiceInstance.stage = InvoiceStage.get(904)
        invoiceInstance.save flush: true
        invoiceInstance.reindex()

        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'dunning.label'), dunningInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: dunningInstance.id
        }
    }

    def updatePayment(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (dunningInstance.version > version) {
                dunningInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'dunning.label')] as Object[],
                    'Another user has updated this Dunning while you were editing'
                )
                render view: 'edit', model: [dunningInstance: dunningInstance]
                return
            }
        }

        dunningInstance.properties = params.findAll {
            it.key in [
                'stage', 'paymentDate', 'paymentAmount', 'paymentMethod'
            ]
        }

        if (!dunningInstance.save(flush: true)) {
            render view: 'edit', model: [dunningInstance: dunningInstance]
            return
        }

        request.dunningInstance = dunningInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'dunning.label'), dunningInstance.toString()]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: dunningInstance.id
        }
    }

    def delete(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        if (!session.user.admin && dunningInstance.stage.id >= 2202) {
            redirect action: 'index'
            return
        }

        try {
            dunningInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'dunning.label')]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'dunning.label')]
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

        def c = Dunning.createCriteria()
        List<Dunning> list = c.list {
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
                for (Dunning d in list) {
                    dunning id: d.id, number: d.fullNumber, name: d.subject,
                        fullName: d.fullName
                }
            }
        }
    }

    def print(Long id, String template) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            render status: SC_NOT_FOUND
            return
        }

        String xml = invoicingTransactionService.generateXML(
            dunningInstance, session.user, !!params.duplicate, [
                invoice: dunningInstance.invoice,
                invoiceFullNumber: dunningInstance.invoice.fullNumber,
            ]
        )
        GString fileName =
            "${message(code: 'dunning.label')} ${dunningInstance.fullNumber}"
        if (params.duplicate) {
            fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
        }
        fileName += ".pdf"

        fopService.outputPdf xml, 'dunning', template, response, fileName
    }

    def getClosingBalance(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        render([closingBalance: dunningInstance.closingBalance] as JSON)
    }


    //-- Non-public methods ---------------------

    private InvoicingItem serviceToItem(Service s) {
        new InvoicingItem(
            number: s.fullNumber, quantity: s.quantity,
            unit: s.unit.toString(), name: s.name, description: s.description,
            unitPrice: s.unitPrice, tax: s.taxRate.taxValue * 100
        )
    }
}
