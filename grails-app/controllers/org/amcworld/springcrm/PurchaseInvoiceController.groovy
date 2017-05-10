/*
 * PurchaseInvoiceController.groovy
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
 * The class {@code PurchaseInvoiceController} contains actions which manage
 * purchase invoices.
 *
 * @author  Daniel Ellermann
 * @version 2.2
 */
class PurchaseInvoiceController extends GeneralController<PurchaseInvoice> {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.purchaseInvoice


    //-- Fields ---------------------------------

    DataFileService dataFileService


    //-- Constructors ---------------------------

    PurchaseInvoiceController() {
        super(PurchaseInvoice)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    def create() {
        super.create()
    }

    def delete(Long id) {
        super.delete id
    }

    def edit(Long id) {
        super.edit id
    }

    def index() {
        List<PurchaseInvoice> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = PurchaseInvoice.findAllBySubjectLike(searchFilter, params)
            count = PurchaseInvoice.countBySubjectLike(searchFilter)
        } else {
            list = PurchaseInvoice.list(params)
            count = PurchaseInvoice.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization) {
        Organization organizationInstance = Organization.get(organization)

        getListEmbeddedModel(
            PurchaseInvoice.findAllByVendor(organizationInstance, params),
            PurchaseInvoice.countByVendor(organizationInstance),
            [organization: organizationInstance.id]
        )
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


    //-- Non-public methods ---------------------

    @Override
    protected void lowLevelDelete(PurchaseInvoice invoiceInstance) {
        super.lowLevelDelete invoiceInstance
        if (invoiceInstance.documentFile != null) {
            dataFileService.removeFile FILE_TYPE, invoiceInstance.documentFile
        }
    }

    @Override
    protected PurchaseInvoice lowLevelSave() {
        PurchaseInvoice purchaseInvoiceInstance = new PurchaseInvoice(params)
        if (purchaseInvoiceInstance.items?.find { it.hasErrors() } ||
            !purchaseInvoiceInstance.validate())
        {
            return null
        }

        purchaseInvoiceInstance.documentFile =
            dataFileService.storeFile(FILE_TYPE, params.file as MultipartFile)

        purchaseInvoiceInstance.save failOnError: true, flush: true
    }

    @Override
    protected PurchaseInvoice lowLevelUpdate(PurchaseInvoice invoiceInstance) {
        /*
         * The original implementation which worked in Grails 2.0.0.
         */
        invoiceInstance.properties = params
//        purchaseInvoiceInstance.items?.retainAll { it != null }

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
        invoiceInstance.items?.clear()
        boolean itemErrors = false
        for (int i = 0; params."items[${i}]"; i++) {
            if (params."items[${i}]".id != 'null') {
                PurchaseInvoiceItem item = params."items[${i}]"
                itemErrors |= item.hasErrors()
                invoiceInstance.addToItems item
            }
        }

        if (itemErrors || !invoiceInstance.validate()) {
            invoiceInstance.discard()
            return null
        }

        DataFile df = invoiceInstance.documentFile
        if (params.fileRemove == '1') {
            invoiceInstance.documentFile = null
        } else if (!params.file?.empty) {
            df = dataFileService.updateFile(
                FILE_TYPE, df, params.file as MultipartFile
            )
            invoiceInstance.documentFile = df
        }

        invoiceInstance = invoiceInstance.save(failOnError: true, flush: true)
        if (invoiceInstance != null && params.fileRemove == '1' && df) {
            dataFileService.removeFile FILE_TYPE, df
        }

        invoiceInstance
    }
}
