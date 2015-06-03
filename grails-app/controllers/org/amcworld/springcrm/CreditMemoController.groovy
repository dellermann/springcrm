/*
 * CreditMemoController.groovy
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


/**
 * The class {@code CreditMemoController} contains actions which manage credit
 * memos.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class CreditMemoController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    FopService fopService
    InvoicingTransactionService invoicingTransactionService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<CreditMemo> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = CreditMemo.findAllBySubjectLike(searchFilter, params)
            count = CreditMemo.countBySubjectLike(searchFilter)
        } else {
            list = CreditMemo.list(params)
            count = CreditMemo.count()
        }

        [creditMemoInstanceList: list, creditMemoInstanceTotal: count]
    }

    def listEmbedded(Long organization, Long person, Long invoice,
                     Long dunning)
    {
        List<CreditMemo> l
        int count
        def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            def organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                l = CreditMemo.findAllByOrganization(organizationInstance, params)
                count = CreditMemo.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        } else if (person) {
            def personInstance = Person.get(person)
            if (personInstance) {
                l = CreditMemo.findAllByPerson(personInstance, params)
                count = CreditMemo.countByPerson(personInstance)
                linkParams = [person: personInstance.id]
            }
        } else if (invoice) {
            def invoiceInstance = Invoice.get(invoice)
            if (invoiceInstance) {
                l = CreditMemo.findAllByInvoice(invoiceInstance, params)
                count = CreditMemo.countByInvoice(invoiceInstance)
                linkParams = [invoice: invoiceInstance.id]
            }
        } else if (dunning) {
            def dunningInstance = Dunning.get(dunning)
            if (dunningInstance) {
                l = CreditMemo.findAllByDunning(dunningInstance, params)
                count = CreditMemo.countByDunning(dunningInstance)
                linkParams = [dunning: dunningInstance.id]
            }
        }

        [
            creditMemoInstanceList: l, creditMemoInstanceTotal: count,
            linkParams: linkParams
        ]
  }

    def create() {
        CreditMemo creditMemoInstance
        if (params.invoice) {
            def invoiceInstance = Invoice.get(params.invoice)
            creditMemoInstance = new CreditMemo(invoiceInstance)
        } else if (params.dunning) {
            def dunningInstance = Dunning.get(params.dunning)
            creditMemoInstance = new CreditMemo(dunningInstance)
        } else {
            creditMemoInstance = new CreditMemo(params)
        }

        creditMemoInstance.copyAddressesFromOrganization()

        [creditMemoInstance: creditMemoInstance]
    }

    def copy(Long id) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'creditMemo.label'), id]
            )
            redirect action: 'index'
            return
        }

        creditMemoInstance = new CreditMemo(creditMemoInstance)
        render view: 'create', model: [creditMemoInstance: creditMemoInstance]
    }

    def save() {
        CreditMemo creditMemoInstance = new CreditMemo(params)
        if (!invoicingTransactionService.save(creditMemoInstance, params)) {
            render view: 'create',
                model: [creditMemoInstance: creditMemoInstance]
            return
        }

        request.creditMemoInstance = creditMemoInstance

        Invoice invoiceInstance = creditMemoInstance.invoice
        if (invoiceInstance) {
            invoiceInstance.stage = InvoiceStage.get(907)
            invoiceInstance.save flush: true
            invoiceInstance.reindex()
        }
        Dunning dunningInstance = creditMemoInstance.dunning
        if (dunningInstance) {
            dunningInstance.stage = DunningStage.get(2206)
            dunningInstance.save flush: true
            dunningInstance.reindex()
        }

        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'creditMemo.label'),
                creditMemoInstance.toString()
            ]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: creditMemoInstance.id
        }
    }

    def show(Long id) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'creditMemo.label'), id]
            )
            redirect action: 'index'
            return
        }

        [creditMemoInstance: creditMemoInstance]
    }

    def edit(Long id) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'creditMemo.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (!session.user.admin && creditMemoInstance.stage.id >= 2502) {
            redirect action: 'index'
            return
        }

        [creditMemoInstance: creditMemoInstance]
    }

    def editPayment(Long id) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'creditMemo.label'), id]
            )
            redirect action: 'index'
            return
        }

        [creditMemoInstance: creditMemoInstance]
    }

    def update(Long id) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'creditMemo.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (!session.user.admin && creditMemoInstance.stage.id >= 2502) {
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (creditMemoInstance.version > version) {
                creditMemoInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'creditMemo.label')] as Object[],
                    'Another user has updated this CreditMemo while you were editing'
                )
                render view: 'edit',
                    model: [creditMemoInstance: creditMemoInstance]
                return
            }
        }

        if (!invoicingTransactionService.save(creditMemoInstance, params)) {
            render view: 'edit',
                model: [creditMemoInstance: creditMemoInstance]
            return
        }

        request.creditMemoInstance = creditMemoInstance

        Invoice invoiceInstance = creditMemoInstance.invoice
        if (invoiceInstance) {
            invoiceInstance.stage = InvoiceStage.get(907)
            invoiceInstance.save flush: true
            invoiceInstance.reindex()
        }
        Dunning dunningInstance = creditMemoInstance.dunning
        if (dunningInstance) {
            dunningInstance.stage = DunningStage.get(2206)
            dunningInstance.save flush: true
            dunningInstance.reindex()
        }

        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'creditMemo.label'),
                creditMemoInstance.toString()
            ]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: creditMemoInstance.id
        }
    }

    def updatePayment(Long id) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'creditMemo.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (creditMemoInstance.version > version) {
                creditMemoInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'creditMemo.label')] as Object[],
                    'Another user has updated this CreditMemo while you were editing'
                )
                render view: 'edit',
                    model: [creditMemoInstance: creditMemoInstance]
                return
            }
        }

        creditMemoInstance.properties[
            'stage', 'paymentDate', 'paymentAmount', 'paymentMethod'
        ] = params

        if (!creditMemoInstance.save(flush: true)) {
            render view: 'edit',
                model: [creditMemoInstance: creditMemoInstance]
            return
        }

        request.creditMemoInstance = creditMemoInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'creditMemo.label'),
                creditMemoInstance.toString()
            ]
        )

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: creditMemoInstance.id
        }
    }

    def delete(Long id) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'creditMemo.label'), id]
            )
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
            return
        }

        if (!session.user.admin && creditMemoInstance.stage.id >= 2502) {
            redirect action: 'index'
            return
        }

        try {
            creditMemoInstance.delete flush: true
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'creditMemo.label')]
            )

            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: 'index'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'creditMemo.label')]
            )
            redirect action: 'show', id: params.id
        }
    }

    def print(Long id, String template) {
        CreditMemo creditMemoInstance = CreditMemo.get(id)
        if (!creditMemoInstance) {
            render status: SC_NOT_FOUND
            return
        }

        String xml = invoicingTransactionService.generateXML(
            creditMemoInstance, session.user, !!params.duplicate, [
                invoice: creditMemoInstance.invoice,
                invoiceFullNumber: creditMemoInstance.invoice?.fullNumber,
                dunning: creditMemoInstance.dunning,
                dunningFullNumber: creditMemoInstance.dunning?.fullNumber,
                paymentMethod: creditMemoInstance.paymentMethod?.name
            ]
        )
        GString fileName =
            "${message(code: 'creditMemo.label')} ${creditMemoInstance.fullNumber}"
        if (params.duplicate) {
            fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
        }
        fileName += '.pdf'

        fopService.outputPdf xml, 'credit-memo', template, response, fileName
    }
}
