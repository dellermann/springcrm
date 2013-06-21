/*
 * DunningController.groovy
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

import grails.converters.JSON
import javax.servlet.http.HttpServletResponse


/**
 * The class {@code DunningController} contains actions which manage dunnings.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class DunningController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    FopService fopService


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def list, count
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
        def l
        def count
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
        [dunningInstanceList: l, dunningInstanceTotal: count, linkParams: linkParams]
    }

    def create() {
        def dunningInstance
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
            def service = Service.get(serviceId as Long)
            if (service) {
                dunningInstance.addToItems serviceToItem(service)
            }
        }
        serviceId = config['serviceIdDefaultInterest']
        if (serviceId) {
            def service = Service.get(serviceId as Long)
            if (service) {
                dunningInstance.addToItems serviceToItem(service)
            }
        }
        [dunningInstance: dunningInstance]
    }

    def copy(Long id) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), id])
            redirect action: 'show', id: id
            return
        }

        dunningInstance = new Dunning(dunningInstance)
        render view: 'create', model: [dunningInstance: dunningInstance]
    }

    def save() {
        def dunningInstance = new Dunning(params)
        if (!dunningInstance.save(flush: true)) {
            render view: 'create', model: [dunningInstance: dunningInstance]
            return
        }

        request.dunningInstance = dunningInstance

        def invoiceInstance = dunningInstance.invoice
        invoiceInstance.stage = InvoiceStage.get(904)
        invoiceInstance.save flush: true
        invoiceInstance.reindex()

        flash.message = message(code: 'default.created.message', args: [message(code: 'dunning.label', default: 'Dunning'), dunningInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: dunningInstance.id
        }
    }

    def show(Long id) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), id])
            redirect action: 'list'
            return
        }

        [dunningInstance: dunningInstance, printTemplates: fopService.templateNames]
    }

    def edit(Long id) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), id])
            redirect action: 'list'
            return
        }

        if (session.user.admin || dunningInstance.stage.id < 2202) {
            return [dunningInstance: dunningInstance]
        }

        redirect action: 'list'
    }

    def editPayment(Long id) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), id])
            redirect action: 'list'
            return
        }

        [dunningInstance: dunningInstance]
    }

    def update(Long id) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), id])
            redirect action: 'list'
            return
        }

        if (!session.user.admin && dunningInstance.stage.id >= 2202) {
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (dunningInstance.version > version) {
                dunningInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'dunning.label', default: 'Dunning')] as Object[], "Another user has updated this Dunning while you were editing")
                render view: 'edit', model: [dunningInstance: dunningInstance]
                return
            }
        }
        if (params.autoNumber) {
            params.number = dunningInstance.number
        }

        /*
         * The original implementation which worked in Grails 2.0.0.
         */
        dunningInstance.properties = params
//        dunningInstance.items?.retainAll { it != null }

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
        dunningInstance.items?.clear()
        for (int i = 0; params."items[${i}]"; i++) {
            if (params."items[${i}]".id != 'null') {
                dunningInstance.addToItems params."items[${i}]"
            }
        }

        if (!dunningInstance.save(flush: true)) {
            render view: 'edit', model: [dunningInstance: dunningInstance]
            return
        }

        request.dunningInstance = dunningInstance

        def invoiceInstance = dunningInstance.invoice
        invoiceInstance.stage = InvoiceStage.get(904)
        invoiceInstance.save flush: true
        invoiceInstance.reindex()

        flash.message = message(code: 'default.updated.message', args: [message(code: 'dunning.label', default: 'Dunning'), dunningInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: dunningInstance.id
        }
    }

    def updatePayment(Long id) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (dunningInstance.version > version) {
                dunningInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'dunning.label', default: 'Dunning')] as Object[], "Another user has updated this Dunning while you were editing")
                render view: 'edit', model: [dunningInstance: dunningInstance]
                return
            }
        }

        dunningInstance.properties = params.findAll { it.key in ['stage.id', 'paymentDate', 'paymentAmount', 'paymentMethod.id'] }

        if (!dunningInstance.save(flush: true)) {
            render view: 'edit', model: [dunningInstance: dunningInstance]
            return
        }

        request.dunningInstance = dunningInstance
        flash.message = message(code: 'default.updated.message', args: [message(code: 'dunning.label', default: 'Dunning'), dunningInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: dunningInstance.id
        }
    }

    def delete(Long id) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
            return
        }

        if (!session.user.admin && dunningInstance.stage.id >= 2202) {
            redirect action: 'list'
            return
        }

        try {
            dunningInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'dunning.label', default: 'Dunning')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'list'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'dunning.label', default: 'Dunning')])
            redirect action: 'show', id: id
        }
    }

    def find() {
        Integer number = null
        try {
            number = params.name as Integer
        } catch (NumberFormatException ignored) { /* ignored */ }
        def organization = params.organization ? Organization.get(params.organization) : null

        def c = Dunning.createCriteria()
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

        render(contentType: 'text/json') {
            array {
                for (d in list) {
                    dunning id: d.id, name: d.fullName
                }
            }
        }
    }

    def print(Long id, String template) {
        def dunningInstance = Dunning.get(id)
        if (!dunningInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }

        String xml = fopService.generateXml(
            dunningInstance, !!params.duplicate, [
                invoice: dunningInstance.invoice,
                invoiceFullNumber: dunningInstance.invoice.fullNumber,
            ]
        )
        GString fileName = "${message(code: 'dunning.label')} ${dunningInstance.fullNumber}"
        if (params.duplicate) {
            fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
        }
        fileName += ".pdf"

        fopService.outputPdf xml, 'dunning', template, response, fileName
    }

    def getClosingBalance(Long id) {
        def dunningInstance = Dunning.get(id)
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
