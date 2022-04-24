/*
 * DunningController.groovy
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
 * The class {@code DunningController} contains actions which manage dunnings.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class DunningController {

    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService
    SeqNumberService seqNumberService


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
        List<Dunning> l = null
        int count = 0
        def linkParams = null

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
            def invoiceInstance = Invoice.get(params.long('invoice'))
            dunningInstance = new Dunning(invoiceInstance)
            dunningInstance.items?.clear()
        } else {
            dunningInstance = new Dunning()
            dunningInstance.setProperties params
        }

        dunningInstance.copyAddressesFromOrganization()

        ConfigHolder config = ConfigHolder.instance
        def workId = config['workIdDunningCharge']
        if (workId) {
            Work work = Work.get(workId.toType(Long))
            if (work) {
                dunningInstance.addToItems workToItem(work)
            }
        }
        workId = config['workIdDefaultInterest']
        if (workId) {
            Work work = Work.get(workId.toType(Long))
            if (work) {
                dunningInstance.addToItems workToItem(work)
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
        if (invoiceInstance) {
            invoiceInstance.stage = InvoiceStage.get(904)
            invoiceInstance.save flush: true
        }

        flash.message = message(
            code: 'default.created.message',
            args: [message(code: 'dunning.label'), dunningInstance.toString()]
        )

        redirect action: 'show', id: dunningInstance.id
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

        if (!session.credential.admin && dunningInstance.stage.id >= 2202) {
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

        if (!session.credential.admin && dunningInstance.stage.id >= 2202) {
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

        request.dunningInstance = dunningInstance

        Invoice invoiceInstance = dunningInstance.invoice
        invoiceInstance.stage = InvoiceStage.get(904)
        invoiceInstance.save flush: true

        flash.message = message(
            code: 'default.updated.message',
            args: [message(code: 'dunning.label'), dunningInstance.toString()]
        )

        redirect action: 'show', id: dunningInstance.id
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

        redirect action: 'show', id: dunningInstance.id
    }

    def delete(Long id) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'dunning.label'), id]
            )

            redirect action: 'index'
            return
        }

        if (!session.credential.admin && dunningInstance.stage.id >= 2202) {
            redirect action: 'index'
            return
        }

        request.dunningInstance = dunningInstance
        try {
            dunningInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'dunning.label')]
            )

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
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
            ? Organization.get(params.long('organization')) \
            : null

        def c = Dunning.createCriteria()
        List<Dunning> list = (List<Dunning>) c.list {
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

        [dunningInstanceList: list]
    }

    def print(Long id, String template) {
        Dunning dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            render status: SC_NOT_FOUND
            return
        }

        String xml = invoicingTransactionService.generateXML(
            dunningInstance,
            dunningInstance.createUser ?:
                ((Credential) session.credential).loadUser(),
            params.boolean('duplicate') ?: false,
            [
                invoice: dunningInstance.invoice,
                invoiceFullNumber:
                    seqNumberService.getFullNumber(dunningInstance.invoice)
            ]
        )
        StringBuilder buf = new StringBuilder()
        buf << (message(code: 'dunning.label') as String)
        buf << ' ' << seqNumberService.getFullNumber(dunningInstance)
        if (params.duplicate) {
            buf << ' ('
            buf << (message(code: 'invoicingTransaction.duplicate') as String)
            buf << ')'
        }
        buf << '.pdf'

        fopService.outputPdf xml, 'dunning', template, response, buf.toString()
    }

    def getClosingBalance(Long id) {
        [dunningInstance: Dunning.get(id)]
    }


    //-- Non-public methods ---------------------

    private InvoicingItem workToItem(Work w) {
        new InvoicingItem(
            number: seqNumberService.getFullNumber(w), quantity: w.quantity,
            unit: w.unit.toString(), name: w.name, description: w.description,
            unitPrice: w.unitPrice, tax: w.taxRate.taxValue * 100
        )
    }
}
