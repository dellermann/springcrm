package org.amcworld.springcrm

import net.sf.jmimemagic.Magic

class PurchaseInvoiceController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def fileService
	def seqNumberService

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [purchaseInvoiceInstanceList: PurchaseInvoice.list(params), purchaseInvoiceInstanceTotal: PurchaseInvoice.count()]
    }

	def listEmbedded = {
		def organizationInstance = Organization.get(params.organization)
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		[purchaseInvoiceInstanceList:PurchaseInvoice.findAllByVendor(organizationInstance, params), purchaseInvoiceInstanceTotal:PurchaseInvoice.countByVendor(organizationInstance), linkParams:[organization:organizationInstance.id]]
	}

    def create = {
        def purchaseInvoiceInstance = new PurchaseInvoice()
        purchaseInvoiceInstance.properties = params
        return [purchaseInvoiceInstance: purchaseInvoiceInstance]
    }

	def copy = {
		def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
		if (purchaseInvoiceInstance) {
			purchaseInvoiceInstance = new PurchaseInvoice(purchaseInvoiceInstance)
			render(view:'create', model:[purchaseInvoiceInstance:purchaseInvoiceInstance])
		} else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
			redirect(action: 'show', id: purchaseInvoiceInstance.id)
		}
	}

    def save = {
        def purchaseInvoiceInstance = new PurchaseInvoice(params)
		if (!params.file.isEmpty()) {
			purchaseInvoiceInstance.documentFile = fileService.storeFile(params.file)
		}
        if (purchaseInvoiceInstance.save(flush: true)) {
			purchaseInvoiceInstance.index()
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), purchaseInvoiceInstance.toString()])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'show', id: purchaseInvoiceInstance.id)
			}
        } else {
            render(view: 'create', model: [purchaseInvoiceInstance: purchaseInvoiceInstance])
        }
    }

    def show = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (!purchaseInvoiceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
            redirect(action: 'list')
        } else {
            [purchaseInvoiceInstance: purchaseInvoiceInstance]
        }
    }

    def edit = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (!purchaseInvoiceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
            redirect(action: 'list')
        } else {
            return [purchaseInvoiceInstance: purchaseInvoiceInstance]
        }
    }

    def update = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (purchaseInvoiceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (purchaseInvoiceInstance.version > version) {

                    purchaseInvoiceInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')] as Object[], "Another user has updated this PurchaseInvoice while you were editing")
                    render(view: 'edit', model: [purchaseInvoiceInstance: purchaseInvoiceInstance])
                    return
                }
            }
            purchaseInvoiceInstance.properties = params
			if (params.fileRemove == '1') {
				if (purchaseInvoiceInstance.documentFile) {
					fileService.removeFile(purchaseInvoiceInstance.documentFile)
				}
				purchaseInvoiceInstance.documentFile = null;
			} else if (!params.file?.isEmpty()) {
				if (purchaseInvoiceInstance.documentFile) {
					fileService.removeFile(purchaseInvoiceInstance.documentFile)
				}
				purchaseInvoiceInstance.documentFile = fileService.storeFile(params.file)
			}

			/*
			 * XXX 	This code is necessary because the default implementation
			 * 		in Grails does not work. Either data binding or saving does
			 * 		not work correctly if items were deleted and gaps in the
			 * 		indices occurred (e. g. 0, 1, null, null, 4) or the items
			 * 		were re-ordered. Then I observed cluttering in saved data
			 * 		columns.
			 * 		The following lines do not make me happy but they work. In
			 * 		future, this problem may be fixed in Grails so we can
			 * 		remove these lines.
			 */
			purchaseInvoiceInstance.items.clear()
			for (int i = 0; params."items[${i}]"; i++) {
				if (params."items[${i}]".id != 'null') {
					purchaseInvoiceInstance.addToItems(params."items[${i}]")
				}
			}
            if (!purchaseInvoiceInstance.hasErrors() && purchaseInvoiceInstance.save(flush: true)) {
				purchaseInvoiceInstance.reindex()
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), purchaseInvoiceInstance.toString()])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'show', id: purchaseInvoiceInstance.id)
				}
            } else {
                render(view: 'edit', model: [purchaseInvoiceInstance: purchaseInvoiceInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def purchaseInvoiceInstance = PurchaseInvoice.get(params.id)
        if (purchaseInvoiceInstance && params.confirmed) {
			if (purchaseInvoiceInstance.documentFile) {
				fileService.removeFile(purchaseInvoiceInstance.documentFile)
			}
            try {
                purchaseInvoiceInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'purchaseInvoice.label', default: 'PurchaseInvoice'), params.id])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

	def getDocument = {
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
		render(status:404)
	}
}
