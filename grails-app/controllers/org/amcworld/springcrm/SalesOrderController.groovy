/*
 * SalesOrderController.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import grails.web.RequestParameter
import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code SalesOrderController} contains actions which manage sales
 * orders.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@Secured(['ROLE_ADMIN', 'ROLE_SALES_ORDER'])
class SalesOrderController extends GeneralController<SalesOrder> {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.salesOrder


    //-- Fields ---------------------------------

    DataFileService dataFileService
    InvoicingTransactionService invoicingTransactionService
    OrganizationService organizationService
    PersonService personService
    QuoteService quoteService
    SalesOrderService salesOrderService
    SelValueService selValueService
    SeqNumberService seqNumberService


    //-- Constructors ---------------------------

    SalesOrderController() {
        super(SalesOrder)
    }


    //-- Public methods -------------------------

    def copy(SalesOrder salesOrder) {
        respond new SalesOrder(salesOrder), view: 'create'
    }

    def create(@RequestParameter('quote') String quoteId) {
        SalesOrder salesOrder
        if (quoteId) {
            Quote quote = quoteService.get(new ObjectId(quoteId))
            salesOrder = new SalesOrder(quote)
        } else {
            salesOrder = new SalesOrder(params)
        }

        respond salesOrder
    }

    def delete(String id) {
        if (id == null) {
            notFound()
            return
        }

        SalesOrder salesOrder = salesOrderService.delete(new ObjectId(id))
        if (salesOrder?.orderDocument != null) {
            dataFileService.removeFile(FILE_TYPE, salesOrder.orderDocument)
        }

        redirectAfterDelete salesOrder
    }

    def edit(String id) {
        respond id == null ? null : salesOrderService.get(new ObjectId(id))
    }

    def find(String name,
             @RequestParameter('organization') String organizationId)
    {
        Organization organization = organizationId \
            ? organizationService.get(new ObjectId(organizationId))
            : null
        List<SalesOrder> salesOrderList
        if (name) {
            Integer number
            try {
                number = name as Integer
            } catch (NumberFormatException ignored) {
                number = null
            }
            //noinspection GroovyVariableNotAssigned
            salesOrderList = salesOrderService.find(number, name, organization)
        } else {
            salesOrderList = organization == null ? salesOrderService.list([: ])
                : salesOrderService.findAllByOrganization(organization, [: ])
        }

        /*
         * XXX We cannot use respond here because it circumvents interceptors.
         */
        [salesOrderList: salesOrderList]
    }

    def index() {
        List<SalesOrder> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = salesOrderService.findAllBySubjectLike(searchFilter, params)
            count = salesOrderService.countBySubjectLike(searchFilter)
        } else {
            list = salesOrderService.list(params)
            count = salesOrderService.count()
        }

        respond list, model: [salesOrderCount: count]
    }

    def listEmbedded(@RequestParameter('organization') String organizationId,
                     @RequestParameter('person') String personId,
                     @RequestParameter('quote') String quoteId)
    {
        List<SalesOrder> list = null
        Map model = null

        if (organizationId != null) {
            Organization organization =
                organizationService.get(new ObjectId(organizationId))
            if (organization != null) {
                list = salesOrderService.findAllByOrganization(
                    organization, params
                )
                model = [
                    salesOrderCount:
                        salesOrderService.countByOrganization(organization),
                    linkParams: [organization: organization.id.toString()]
                ]
            }
        } else if (personId != null) {
            Person person = personService.get(new ObjectId(personId))
            if (person != null) {
                list = salesOrderService.findAllByPerson(person, params)
                model = [
                    salesOrderCount: salesOrderService.countByPerson(person),
                    linkParams: [person: person.id.toString()]
                ]
            }
        } else if (quoteId != null) {
            Quote quote = quoteService.get(new ObjectId(quoteId))
            list = salesOrderService.findAllByQuote(quote, params)
            model = [
                salesOrderCount: salesOrderService.countByQuote(quote),
                linkParams: [quote: quote.id.toString()]
            ]
        }

        respond list, model: model
    }

    def print(String id, String template) {
        SalesOrder salesOrder =
            id == null ? null : salesOrderService.get(new ObjectId(id))
        if (salesOrder == null) {
            respond salesOrder
            return
        }

        boolean duplicate = params.duplicate
        byte [] pdf =
            invoicingTransactionService.print(salesOrder, template, duplicate)

        StringBuilder buf = new StringBuilder()
        buf << (message(code: 'salesOrder.label') as String)
        buf << ' ' << seqNumberService.getFullNumber(salesOrder)
        if (duplicate) {
            buf << ' '
            buf << (message(code: 'invoicingTransaction.duplicate') as String)
        }
        buf << '.pdf'

        response.contentLength = pdf.length
        render(
            contentType: 'application/pdf',
            file: pdf,
            fileName: buf.toString()
        )
    }

    def save(SalesOrder salesOrder) {
        if (salesOrder == null) {
            notFound()
            return
        }

        salesOrder.orderDocument =
            dataFileService.storeFile(FILE_TYPE, params.file as MultipartFile)

        try {
            salesOrder.beforeInsert()
            updateAssociated salesOrder
            salesOrderService.save salesOrder
            redirectAfterStorage salesOrder
        } catch (ValidationException ignored) {
            respond salesOrder.errors, view: 'create'
        }
    }

    def setSignature(String id) {
        SalesOrder salesOrder = id == null ? null
            : salesOrderService.get(new ObjectId(id))
        if (salesOrder == null) {
            notFound()
            return
        }

        salesOrder.signature = params.signature
        salesOrderService.save salesOrder

        redirect action: 'show', id: id
    }

    def show(String id) {
        respond id == null ? null : salesOrderService.get(new ObjectId(id))
    }

    def update(SalesOrder salesOrder) {
        if (salesOrder == null) {
            notFound()
            return
        }

        DataFile df = salesOrder.orderDocument
        if (params.fileRemove == '1') {
            salesOrder.orderDocument = null
        } else if (params.file != null && !params.file.empty) {
            df = dataFileService.updateFile(
                FILE_TYPE, df, params.file as MultipartFile
            )
            salesOrder.orderDocument = df
        }

        try {
            salesOrder.beforeUpdate()
            salesOrderService.save salesOrder
            updateAssociated salesOrder
            if (params.fileRemove == '1' && df != null) {
                dataFileService.removeFile FILE_TYPE, df
            }
            redirectAfterStorage salesOrder
        } catch (ValidationException ignored) {
            respond salesOrder.errors, view: 'edit'
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Updates the associated quotes.
     *
     * @param salesOrder    the given sales order
     */
    private void updateAssociated(SalesOrder salesOrder) {
        Quote quote = salesOrder.quote
        if (quote != null) {
            quote = quoteService.get(quote.id)

            // XXX selValueService.findByClassAndId() doesn't work here.  It
            // causes an OptimisticLockingException for the SelValue object.
            quote.stage = (QuoteStage) selValueService.get(603L)
            quoteService.save quote
        }
    }
}
