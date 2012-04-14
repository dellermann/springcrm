/*
 * PurchaseInvoiceController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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

import net.sf.jmimemagic.Magic
import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code PurchaseInvoiceController} contains actions which manage
 * purchase invoices.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class PurchaseInvoiceController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

	def fileService
	def seqNumberService


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def list, count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = PurchaseInvoice.findAllBySubjectLike(searchFilter, params)
            count = PurchaseInvoice.countBySubjectLike(searchFilter)
        } else {
            list = PurchaseInvoice.list(params)
            count = PurchaseInvoice.count()
        }

        return [purchaseInvoiceInstanceList: list, purchaseInvoiceInstanceTotal: count]
    }

	def listEmbedded() {
		def organizationInstance = Organization.get(params.organization)
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		return [purchaseInvoiceInstanceList: PurchaseInvoice.findAllByVendor(organizationInstance, params), purchaseInvoiceInstanceTotal: PurchaseInvoice.countByVendor(organizationInstance), linkParams: [organization: organizationInstance.id]]
	}

    def create() {
        def purchaseInvoiceInstance = new PurchaseInvoice()
        purchaseInvoiceInstance.properties = params
        return [purchaseInvoiceInstance: purchaseInvoiceInstance]
    }

	def copy() {
		def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
		if (!purchaseInvoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])
            redirect(action: 'list')
            return
        }

		purchaseInvoiceInstance = new PurchaseInvoice(purchaseInvoiceInstance)
		render(view: 'create', model: [purchaseInvoiceInstance: purchaseInvoiceInstance])
	}

    def save() {
        def purchaseInvoiceInstance = new PurchaseInvoice(params)
		if (!params.file.isEmpty()) {
			purchaseInvoiceInstance.documentFile = fileService.storeFile(params.file)
		}
        if (!purchaseInvoiceInstance.save(flush: true)) {
            log.debug(purchaseInvoiceInstance.errors)
            render(view: 'create', model: [purchaseInvoiceInstance: purchaseInvoiceInstance])
            return
        }
        params.id = purchaseInvoiceInstance.ident()

		purchaseInvoiceInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), purchaseInvoiceInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: purchaseInvoiceInstance.id)
		}
    }

    def show() {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])
            redirect(action: 'list')
            return
        }

        return [purchaseInvoiceInstance: purchaseInvoiceInstance]
    }

    def edit() {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])
            redirect(action: 'list')
            return
        }

        return [purchaseInvoiceInstance: purchaseInvoiceInstance]
    }

    def update() {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (purchaseInvoiceInstance.version > version) {

                purchaseInvoiceInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')] as Object[], "Another user has updated this PurchaseInvoice while you were editing")
                render(view: 'edit', model: [purchaseInvoiceInstance: purchaseInvoiceInstance])
                return
            }
        }

        /*
         * The original implementation which worked in Grails 2.0.0.
         */
        purchaseInvoiceInstance.properties = params
//        purchaseInvoiceInstance.items?.retainAll { it != null }

        if (params.fileRemove == '1') {
            if (purchaseInvoiceInstance.documentFile) {
                fileService.removeFile(purchaseInvoiceInstance.documentFile)
            }
            purchaseInvoiceInstance.documentFile = null
        } else if (!params.file?.isEmpty()) {
            if (purchaseInvoiceInstance.documentFile) {
                fileService.removeFile(purchaseInvoiceInstance.documentFile)
            }
            purchaseInvoiceInstance.documentFile = fileService.storeFile(params.file)
        }

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
        for (int i = 0; params."items[${i}]"; i++) {
            if (params."items[${i}]".id != 'null') {
                purchaseInvoiceInstance.addToItems(params."items[${i}]")
            }
        }

        if (!purchaseInvoiceInstance.save(flush: true)) {
            log.debug(purchaseInvoiceInstance.errors)
            render(view: 'edit', model: [purchaseInvoiceInstance: purchaseInvoiceInstance])
            return
        }

		purchaseInvoiceInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), purchaseInvoiceInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: purchaseInvoiceInstance.id)
		}
    }

    def delete() {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (!purchaseInvoiceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])
            if (params.returnUrl) {
                redirect(url: params.returnUrl)
            } else {
                redirect(action: 'list')
            }
            return
        }

		if (purchaseInvoiceInstance.documentFile) {
			fileService.removeFile(purchaseInvoiceInstance.documentFile)
		}
        try {
            purchaseInvoiceInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')])
            redirect(action: 'show', id: params.id)
        }
    }

	def getDocument() {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (purchaseInvoiceInstance) {
			String doc = purchaseInvoiceInstance.documentFile
			File f = fileService.retrieveFile(doc)
			if (f.exists()) {
				response.contentType = Magic.getMagicMatch(f, true).mimeType
				response.contentLength = f.length()
				response.addHeader 'Content-Disposition',
					"attachment; filename=\"${doc}\""
				response.outputStream << f.newInputStream()
				return null
			}
		}
		render(status: 404)
	}
}
