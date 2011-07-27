package org.amcworld.springcrm

import grails.converters.XML

class InvoiceController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']
	
	def fopService
	def seqNumberService

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [invoiceInstanceList: Invoice.list(params), invoiceInstanceTotal: Invoice.count()]
    }

    def create = {
        def invoiceInstance
		if (params.quote) {
			def quoteInstance = Quote.get(params.quote)
			invoiceInstance = new Invoice(quoteInstance)
		} else if (params.salesOrder) {
			def salesOrderInstance = SalesOrder.get(params.salesOrder)
			invoiceInstance = new Invoice(salesOrderInstance)
			invoiceInstance.quote = salesOrderInstance.quote
		} else {
			invoiceInstance = new Invoice()
			invoiceInstance.properties = params
		}
        return [invoiceInstance: invoiceInstance]
    }

    def save = {
        def invoiceInstance = new Invoice(params)
		if (invoiceInstance.save(flush:true)) {
			seqNumberService.stepFurther(Invoice)
			invoiceInstance.index()
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoiceInstance.toString()])}"
            redirect(action: 'show', id: invoiceInstance.id)
        } else {
            render(view: 'create', model: [invoiceInstance: invoiceInstance])
        }
    }

    def show = {
        def invoiceInstance = Invoice.get(params.id)
        if (!invoiceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])}"
            redirect(action: 'list')
        } else {
            [invoiceInstance: invoiceInstance]
        }
    }

    def edit = {
        def invoiceInstance = Invoice.get(params.id)
        if (!invoiceInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])}"
            redirect(action: 'list')
        } else {
            return [invoiceInstance: invoiceInstance]
        }
    }

    def update = {
        def invoiceInstance = Invoice.get(params.id)
        if (invoiceInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (invoiceInstance.version > version) {
                    invoiceInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'invoice.label', default: 'Invoice')] as Object[], "Another user has updated this Invoice while you were editing")
                    render(view: 'edit', model: [invoiceInstance: invoiceInstance])
                    return
                }
            }
            invoiceInstance.properties = params
            if (!invoiceInstance.hasErrors() && invoiceInstance.save(flush: true)) {
				invoiceInstance.reindex()
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'invoice.label', default: 'Invoice'), invoiceInstance.toString()])}"
                redirect(action: 'show', id: invoiceInstance.id)
            } else {
                render(view: 'edit', model: [invoiceInstance: invoiceInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def invoiceInstance = Invoice.get(params.id)
        if (invoiceInstance) {
            try {
                invoiceInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'invoice.label', default: 'Invoice')])}"
                redirect(action: 'list')
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'invoice.label', default: 'Invoice')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'invoice.label', default: 'Invoice'), params.id])}"
            redirect(action: 'list')
        }
    }

	def print = {
        def invoiceInstance = Invoice.get(params.id)
        if (invoiceInstance) {
			def data = [
				transaction:invoiceInstance,
				items:invoiceInstance.items,
				organization:invoiceInstance.organization,
				person:invoiceInstance.person,
				user:session.user,
				fullNumber:invoiceInstance.fullNumber,
				taxRates:invoiceInstance.taxRateSums,
				values:[
			        subtotalNet:invoiceInstance.subtotalNet,
					subtotalGross:invoiceInstance.subtotalGross,
					discountPercentAmount:invoiceInstance.discountPercentAmount,
					total:invoiceInstance.total
				]
			]
			String xml = (data as XML).toString()
//			println xml
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			fopService.generatePdf(
				new StringReader(xml), '/WEB-INF/data/fo/invoice-fo.xsl',
				baos
			)
			response.contentType = 'application/pdf'
			response.addHeader 'Content-Disposition', 
				"attachment; filename=\"${message(code: 'invoice.label')} ${invoiceInstance.fullNumber}.pdf\""
			response.contentLength = baos.size()
			response.outputStream.write(baos.toByteArray())
			response.outputStream.flush()
		} else {
			render(status: 404)
		}
	}
}
