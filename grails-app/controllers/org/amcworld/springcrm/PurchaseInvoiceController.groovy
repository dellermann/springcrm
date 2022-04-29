/*
 * PurchaseInvoiceController.groovy
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

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code PurchaseInvoiceController} contains actions which manage
 * purchase invoices.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class PurchaseInvoiceController {

    //-- Constants ------------------------------

    public static final DataFileType FILE_TYPE = DataFileType.purchaseInvoice


    //-- Class fields ---------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Fields ---------------------------------

    DataFileService dataFileService
    PurchaseInvoiceService purchaseInvoiceService


    //-- Public methods -------------------------

    def index() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        List<PurchaseInvoice> list
        int count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = PurchaseInvoice.findAllBySubjectLike(searchFilter, params)
            count = PurchaseInvoice.countBySubjectLike(searchFilter)
        } else if (params.year) {
            int start = (params.int('year') % 1000) * 1000
            int end = start + 999
            list = PurchaseInvoice.findAllByNumberBetween(start, end, params)
            count = PurchaseInvoice.countByNumberBetween(start, end)
        } else {
            list = PurchaseInvoice.list(params)
            count = PurchaseInvoice.count()
        }

        [
            purchaseInvoiceInstanceList: list,
            purchaseInvoiceInstanceTotal: count,
            yearStart: purchaseInvoiceService.findYearStart(),
            yearEnd: purchaseInvoiceService.findYearEnd(),
        ]
    }

    def listEmbedded(Long organization) {
        Organization organizationInstance = Organization.get(organization)
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        [
            purchaseInvoiceInstanceList: PurchaseInvoice.findAllByVendor(
                organizationInstance, params
            ),
            purchaseInvoiceInstanceTotal: PurchaseInvoice.countByVendor(
                organizationInstance
            ),
            linkParams: [organization: organizationInstance.id]
        ]
    }

    def create() {
        [purchaseInvoiceInstance: new PurchaseInvoice(params)]
    }

    def copy(Long id) {
        PurchaseInvoice purchaseInvoiceInstance = PurchaseInvoice.get(id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'purchaseInvoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        purchaseInvoiceInstance = new PurchaseInvoice(purchaseInvoiceInstance)
        render view: 'create',
            model: [purchaseInvoiceInstance: purchaseInvoiceInstance]
    }

    def save() {
        PurchaseInvoice purchaseInvoiceInstance = new PurchaseInvoice(params)
        if (purchaseInvoiceInstance.items?.find { it.hasErrors() } ||
            !purchaseInvoiceInstance.validate())
        {
            render view: 'create',
                model: [purchaseInvoiceInstance: purchaseInvoiceInstance]
            return
        }

        purchaseInvoiceInstance.documentFile =
            dataFileService.storeFile(FILE_TYPE, params.file)
        if (!purchaseInvoiceInstance.save(failOnError: true, flush: true)) {
            render view: 'create',
                model: [purchaseInvoiceInstance: purchaseInvoiceInstance]
            return
        }

        request.purchaseInvoiceInstance = purchaseInvoiceInstance
        flash.message = message(
            code: 'default.created.message',
            args: [
                message(code: 'purchaseInvoice.label'),
                purchaseInvoiceInstance.toString()
            ]
        )

        redirect action: 'show', id: purchaseInvoiceInstance.id
    }

    def show(Long id) {
        PurchaseInvoice purchaseInvoiceInstance = PurchaseInvoice.get(id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'purchaseInvoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        [purchaseInvoiceInstance: purchaseInvoiceInstance]
    }

    def edit(Long id) {
        PurchaseInvoice purchaseInvoiceInstance = PurchaseInvoice.get(id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'purchaseInvoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        [purchaseInvoiceInstance: purchaseInvoiceInstance]
    }

    def update(Long id) {
        PurchaseInvoice purchaseInvoiceInstance = PurchaseInvoice.get(id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'purchaseInvoice.label'), id]
            )
            redirect action: 'index'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (purchaseInvoiceInstance.version > version) {
                purchaseInvoiceInstance.errors.rejectValue(
                    'version', 'default.optimistic.locking.failure',
                    [message(code: 'purchaseInvoice.label')] as Object[],
                    'Another user has updated this PurchaseInvoice while you were editing'
                )
                render view: 'edit',
                    model: [purchaseInvoiceInstance: purchaseInvoiceInstance]
                return
            }
        }

        /*
         * The original implementation which worked in Grails 2.0.0.
         */
        purchaseInvoiceInstance.properties = params
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
        purchaseInvoiceInstance.items?.clear()
        boolean itemErrors = false
        for (int i = 0; params."items[${i}]"; i++) {
            if (params."items[${i}]".id != 'null') {
                PurchaseInvoiceItem item = params."items[${i}]"
                itemErrors |= item.hasErrors()
                purchaseInvoiceInstance.addToItems item
            }
        }

        if (itemErrors || !purchaseInvoiceInstance.validate()) {
            purchaseInvoiceInstance.discard()
            render view: 'edit',
                model: [purchaseInvoiceInstance: purchaseInvoiceInstance]
            return
        }

        DataFile df = purchaseInvoiceInstance.documentFile
        if (params.fileRemove == '1') {
            purchaseInvoiceInstance.documentFile = null
        } else if (!params.file?.empty) {
            df = dataFileService.updateFile(
                FILE_TYPE, df, (MultipartFile) params.file
            )
            purchaseInvoiceInstance.documentFile = df
        }

        if (!purchaseInvoiceInstance.save(failOnError: true, flush: true)) {
            render view: 'edit',
                model: [purchaseInvoiceInstance: purchaseInvoiceInstance]
            return
        }
        if (params.fileRemove == '1' && df) {
            dataFileService.removeFile FILE_TYPE, df
        }

        request.purchaseInvoiceInstance = purchaseInvoiceInstance
        flash.message = message(
            code: 'default.updated.message',
            args: [
                message(code: 'purchaseInvoice.label'),
                purchaseInvoiceInstance.toString()
            ]
        )

        redirect action: 'show', id: purchaseInvoiceInstance.id
    }

    def delete(Long id) {
        PurchaseInvoice purchaseInvoiceInstance = PurchaseInvoice.get(id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(
                code: 'default.not.found.message',
                args: [message(code: 'purchaseInvoice.label'), id]
            )

            redirect action: 'index'
            return
        }

        request.purchaseInvoiceInstance = purchaseInvoiceInstance
        try {
            purchaseInvoiceInstance.delete flush: true
            if (purchaseInvoiceInstance.documentFile) {
                dataFileService.removeFile FILE_TYPE,
                    purchaseInvoiceInstance.documentFile
            }
            flash.message = message(
                code: 'default.deleted.message',
                args: [message(code: 'purchaseInvoice.label')]
            )

            redirect action: 'index'
        } catch (DataIntegrityViolationException ignore) {
            flash.message = message(
                code: 'default.not.deleted.message',
                args: [message(code: 'purchaseInvoice.label')]
            )

            redirect action: 'show', id: id
        }
    }
}

