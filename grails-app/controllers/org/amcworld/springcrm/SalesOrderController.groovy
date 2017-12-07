/*
 * SalesOrderController.groovy
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

import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code SalesOrderController} contains actions which manage sales
 * orders.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class SalesOrderController extends InvoicingController<SalesOrder> {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.salesOrder


    //-- Fields ---------------------------------

    DataFileService dataFileService
    OrganizationService organizationService
    PersonService personService
    QuoteService quoteService
    SalesOrderService salesOrderService


    //-- Constructors ---------------------------

    SalesOrderController() {
        super(SalesOrder)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
        SalesOrder salesOrderInstance
        if (params.quote) {
            Quote quoteInstance = quoteService.get(params.long('quote'))
            salesOrderInstance = new SalesOrder(quoteInstance)
        } else {
            salesOrderInstance = new SalesOrder(params)
        }

        getCreateModel salesOrderInstance
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def find() {
        super.find()
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

        getIndexModel list, count
    }

    def listEmbedded(Long organization, Long person, Long quote) {
        List<SalesOrder> l = null
        int count = 0
        Map<String, Object> linkParams = null
        if (organization) {
            Organization organizationInstance =
                organizationService.get(organization)
            l = salesOrderService.findAllByOrganization(
                organizationInstance, params
            )
            count = salesOrderService.countByOrganization(organizationInstance)
            linkParams = [organization: organizationInstance.id]
        } else if (person) {
            Person personInstance = personService.get(person)
            l = salesOrderService.findAllByPerson(personInstance, params)
            count = salesOrderService.countByPerson(personInstance)
            linkParams = [person: personInstance.id]
        } else if (quote) {
            Quote quoteInstance = quoteService.get(quote)
            l = salesOrderService.findAllByQuote(quoteInstance, params)
            count = salesOrderService.countByQuote(quoteInstance)
            linkParams = [quote: quoteInstance.id]
        }

        getListEmbeddedModel l, count, linkParams
    }

    def print(Long id, String template) {
        SalesOrder salesOrderInstance = getDomainInstanceWithStatus(id)
        if (salesOrderInstance != null) {
            printDocument salesOrderInstance, template
        }
    }

    def save() {
        super.save()
    }

    def setSignature(Long id) {
        SalesOrder salesOrderInstance = lowLevelGet(id)
        if (salesOrderInstance) {
            salesOrderInstance.signature = params.signature
            salesOrderService.save salesOrderInstance
        }

        redirect action: 'show', id: id
    }

    def show(Long id) {
        super.show id
    }

    def update(Long id) {
        super.update id
    }


    //-- Non-public methods ---------------------

    @Override
    protected void lowLevelDelete(SalesOrder instance) {
        salesOrderService.delete instance.id
    }

    @Override
    protected SalesOrder lowLevelGet(Long id) {
        salesOrderService.get id
    }

    @Override
    protected SalesOrder lowLevelSave(SalesOrder instance) {
        salesOrderService.save instance
    }

    @Override
    protected SalesOrder saveInstance(SalesOrder instance = null) {
        SalesOrder salesOrderInstance = newInstance(params)
        salesOrderInstance.orderDocument =
            dataFileService.storeFile(FILE_TYPE, params.file as MultipartFile)

        saveOrUpdateInstance salesOrderInstance
    }

    @Override
    protected SalesOrder updateInstance(SalesOrder salesOrderInstance) {
        DataFile df = salesOrderInstance.orderDocument
        if (params.fileRemove == '1') {
            salesOrderInstance.orderDocument = null
        } else if (!params.file?.empty) {
            df = dataFileService.updateFile(
                FILE_TYPE, df, params.file as MultipartFile
            )
            salesOrderInstance.orderDocument = df
        }

        SalesOrder res = saveOrUpdateInstance(salesOrderInstance)
        if (res != null && params.fileRemove == '1' && df) {
            dataFileService.removeFile FILE_TYPE, df
        }

        res
    }

    @Override
    protected void updateAssociated(SalesOrder salesOrderInstance) {
        Quote quoteInstance = salesOrderInstance.quote
        if (quoteInstance) {
            quoteInstance.stage = QuoteStage.get(603L)
            quoteService.save quoteInstance
        }
    }
}
