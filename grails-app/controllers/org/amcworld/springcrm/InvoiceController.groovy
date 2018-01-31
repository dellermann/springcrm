/*
 * InvoiceController.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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
 * The class {@code InvoiceController} contains actions which manage invoices.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class InvoiceController extends InvoicingController<Invoice> {

    //-- Fields ---------------------------------

    ConfigService configService
    UserService userService


    //-- Constructors ---------------------------

    InvoiceController() {
        super(Invoice)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        Invoice invoiceInstance
        if (params.quote) {
            Quote quoteInstance = Quote.get(params.long('quote'))
            invoiceInstance = new Invoice(quoteInstance)
            invoiceInstance.userService = userService
        } else if (params.salesOrder) {
            SalesOrder salesOrderInstance =
            SalesOrder.get(params.long('salesOrder'))
            invoiceInstance = new Invoice(salesOrderInstance)
            invoiceInstance.quote = salesOrderInstance.quote
            invoiceInstance.userService = userService
        } else {
            invoiceInstance = new Invoice(params)
        }

        getCreateModel invoiceInstance
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def editPayment(Long id) {
        super.editPayment id
    }

    def find() {
        super.find()
    }

    def getClosingBalance(Long id) {
        super.getClosingBalance id
    }

    def index() {
        List<Invoice> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = Invoice.findAllBySubjectLike(searchFilter, params)
            count = Invoice.countBySubjectLike(searchFilter)
        } else {
            list = Invoice.list(params)
            count = Invoice.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person, Long quote,
                     Long salesOrder)
    {
        List<Invoice> list = null
        int count = 0
        Map<String, Object> linkParams = null

        if (organization) {
            def organizationInstance = Organization.get(organization)
            list = Invoice.findAllByOrganization(organizationInstance, params)
            count = Invoice.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            def personInstance = Person.get(person)
            list = Invoice.findAllByPerson(personInstance, params)
            count = Invoice.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        } else if (quote) {
            def quoteInstance = Quote.get(params.long('quote'))
            list = Invoice.findAllByQuote(quoteInstance, params)
            count = Invoice.countByQuote(quoteInstance)
            linkParams = [quote: quoteInstance.id]
        } else if (salesOrder) {
            def salesOrderInstance = SalesOrder.get(salesOrder)
            list = Invoice.findAllBySalesOrder(salesOrderInstance, params)
            count = Invoice.countBySalesOrder(salesOrderInstance)
            linkParams = [salesOrder: salesOrderInstance.id]
        }

        getListEmbeddedModel list, count, linkParams
    }

    /**
     * Lists all invoices which have unpaid amount.
     *
     * @return  a map containing the model for the view
     */
    def listUnpaidBills() {
        List<Invoice> invoiceList =
            invoicingTransactionService.findUnpaidBills()

        respond(
            invoiceList,
            [
                numFractionDigitsExt:
                   configService.getInteger('numFractionDigitsExt')
            ]
        )
    }

    def print(Long id, String template) {
        Invoice invoiceInstance = getDomainInstanceWithStatus(id)
        if (invoiceInstance != null) {
            printDocument invoiceInstance, template
        }
    }

    def save() {
        super.save()
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }

    def updatePayment(Long id) {
        super.updatePayment id
    }


    //-- Non-public methods ---------------------

    @Override
    protected boolean checkAccess(Invoice invoiceInstance) {
        admin || invoiceInstance.stage.id < 902
    }

    @Override
    protected void updateAssociated(Invoice invoiceInstance) {
        Quote quoteInstance =
            invoiceInstance.quote ?: invoiceInstance.salesOrder?.quote
        if (quoteInstance != null) {
            quoteInstance.stage = QuoteStage.get(603L)
            quoteInstance.save flush: true
        }
    }
}
