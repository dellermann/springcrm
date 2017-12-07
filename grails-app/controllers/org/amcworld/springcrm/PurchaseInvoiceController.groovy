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
class PurchaseInvoiceController extends GenericDomainController<PurchaseInvoice> {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.purchaseInvoice


    //-- Fields ---------------------------------

    DataFileService dataFileService
    OrganizationService organizationService
    PurchaseInvoiceService purchaseInvoiceService


    //-- Constructors ---------------------------

    PurchaseInvoiceController() {
        super(PurchaseInvoice)
    }


    //-- Public methods -------------------------

    def copy(Long id) {
        super.copy id
    }

    Map create() {
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
            list = purchaseInvoiceService.findAllBySubjectLike(
                searchFilter, params
            )
            count = purchaseInvoiceService.countBySubjectLike(searchFilter)
        } else {
            list = purchaseInvoiceService.list(params)
            count = purchaseInvoiceService.count()
        }

        getIndexModel list, count
    }

    def listEmbedded(Long organization) {
        Organization organizationInstance =
            organizationService.get(organization)

        getListEmbeddedModel(
            purchaseInvoiceService.findAllByVendor(
                organizationInstance, params
            ),
            purchaseInvoiceService.countByVendor(organizationInstance),
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
    protected void lowLevelDelete(PurchaseInvoice instance) {
        purchaseInvoiceService.delete instance.id
        if (instance.documentFile != null) {
            dataFileService.removeFile FILE_TYPE, instance.documentFile
        }
    }

    @Override
    protected PurchaseInvoice lowLevelGet(Long id) {
        purchaseInvoiceService.get id
    }

    @Override
    protected PurchaseInvoice lowLevelSave(PurchaseInvoice instance) {
        purchaseInvoiceService.save instance
    }

    @Override
    protected PurchaseInvoice saveInstance(PurchaseInvoice instance) {
        if (instance.items?.find { it.hasErrors() } || !instance.validate()) {
            return null
        }

        instance.documentFile =
            dataFileService.storeFile(FILE_TYPE, params.file as MultipartFile)

        lowLevelSave instance
    }

    @Override
    protected PurchaseInvoice updateInstance(PurchaseInvoice instance) {
        /*
         * The original implementation which worked in Grails 2.0.0.
         */
        bindData instance, params
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
        instance.items?.clear()
        boolean itemErrors = false
        for (int i = 0; params."items[${i}]"; i++) {
            if (params."items[${i}]".id != 'null') {
                PurchaseInvoiceItem item = params."items[${i}]"
                itemErrors |= item.hasErrors()
                instance.addToItems item
            }
        }

        if (itemErrors || !instance.validate()) {
            instance.discard()
            return null
        }

        DataFile df = instance.documentFile
        if (params.fileRemove == '1') {
            instance.documentFile = null
        } else if (!params.file?.empty) {
            df = dataFileService.updateFile(
                FILE_TYPE, df, params.file as MultipartFile
            )
            instance.documentFile = df
        }

        instance = lowLevelSave(instance)
        if (instance != null && params.fileRemove == '1' && df) {
            dataFileService.removeFile FILE_TYPE, df
        }

        instance
    }
}
