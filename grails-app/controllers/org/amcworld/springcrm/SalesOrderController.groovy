package org.amcworld.springcrm

import grails.converters.XML

class SalesOrderController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

	def fopService
	def seqNumberService

	def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [salesOrderInstanceList: SalesOrder.list(params), salesOrderInstanceTotal: SalesOrder.count()]
    }

    def create = {
        def salesOrderInstance
		if (params.quote) {
			def quoteInstance = Quote.get(params.quote)
			salesOrderInstance = new SalesOrder(quoteInstance)
		} else {
			salesOrderInstance = new SalesOrder()
			salesOrderInstance.properties = params
		}
		def seqNumber = seqNumberService.loadSeqNumber(SalesOrder.class)
		salesOrderInstance.number = seqNumber.nextNumber
        return [salesOrderInstance: salesOrderInstance, seqNumberPrefix: seqNumber.prefix]
    }

    def save = {
        def salesOrderInstance = new SalesOrder(params)
		SalesOrder.withTransaction { status ->
			try {
				seqNumberService.stepFurther(SalesOrder.class)
				salesOrderInstance.save(failOnError:true)
			} catch (Exception) {
				status.setRollbackOnly()
			}
        }
        if (salesOrderInstance.hasErrors()) {
            render(view: 'create', model: [salesOrderInstance: salesOrderInstance])
        } else {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrderInstance.toString()])}"
            redirect(action: 'show', id: salesOrderInstance.id)
        }
    }

    def show = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: 'list')
        } else {
            [salesOrderInstance: salesOrderInstance]
        }
    }

    def edit = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (!salesOrderInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: 'list')
        } else {
			def seqNumber = seqNumberService.loadSeqNumber(SalesOrder.class)
            return [salesOrderInstance: salesOrderInstance, seqNumberPrefix: seqNumber.prefix]
        }
    }

    def update = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (salesOrderInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (salesOrderInstance.version > version) {
                    
                    salesOrderInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'salesOrder.label', default: 'SalesOrder')] as Object[], "Another user has updated this SalesOrder while you were editing")
                    render(view: 'edit', model: [salesOrderInstance: salesOrderInstance])
                    return
                }
            }
            salesOrderInstance.properties = params
            if (!salesOrderInstance.hasErrors() && salesOrderInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), salesOrderInstance.toString()])}"
                redirect(action: 'show', id: salesOrderInstance.id)
            } else {
                render(view: 'edit', model: [salesOrderInstance: salesOrderInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (salesOrderInstance) {
            try {
                salesOrderInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder')])}"
                redirect(action: 'list')
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'salesOrder.label', default: 'SalesOrder'), params.id])}"
            redirect(action: 'list')
        }
    }
	
	def print = {
        def salesOrderInstance = SalesOrder.get(params.id)
        if (salesOrderInstance) {
			def data = [
				salesOrder:salesOrderInstance, items:salesOrderInstance.items,
				organization:salesOrderInstance.organization,
				person:salesOrderInstance.person,
				user:session.user,
				fullNumber:salesOrderInstance.fullNumber,
				taxRates:salesOrderInstance.taxRateSums,
				values:[
			        subtotalNet:salesOrderInstance.subtotalNet,
					subtotalGross:salesOrderInstance.subtotalGross,
					discountPercentAmount:salesOrderInstance.discountPercentAmount,
					total:salesOrderInstance.total
				]
			]
			String xml = (data as XML).toString()
//			println xml
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			fopService.generatePdf(
				new StringReader(xml), '/WEB-INF/data/fo/sales-order-fo.xsl',
				baos
			)
			response.contentType = 'application/pdf'
			response.addHeader 'Content-Disposition', 
				"attachment; filename=\"${message(code: 'salesOrder.label')} ${salesOrderInstance.fullNumber}.pdf\""
			response.contentLength = baos.size()
			response.outputStream.write(baos.toByteArray())
			response.outputStream.flush()
		} else {
			render(status: 404)
		}
	}
}
