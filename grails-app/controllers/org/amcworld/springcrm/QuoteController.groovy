package org.amcworld.springcrm

import grails.converters.XML

class QuoteController {

    static allowedMethods = [save: "POST", update: "POST", delete: "GET"]
	
	def fopService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [quoteInstanceList: Quote.list(params), quoteInstanceTotal: Quote.count()]
    }

    def create = {
		def res = Quote.executeQuery("select max(q.number)+1 from Quote q")
        def quoteInstance = new Quote(number:res[0] ?: 10000)
        quoteInstance.properties = params
        return [quoteInstance: quoteInstance]
    }

    def save = {
        def quoteInstance = new Quote(params)
        if (quoteInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'quote.label', default: 'Quote'), quoteInstance.toString()])}"
            redirect(action: "show", id: quoteInstance.id)
        } else {
            render(view: "create", model: [quoteInstance: quoteInstance])
        }
    }

    def show = {
        def quoteInstance = Quote.get(params.id)
        if (!quoteInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])}"
            redirect(action: "list")
        } else {
            [quoteInstance: quoteInstance]
        }
    }

    def edit = {
        def quoteInstance = Quote.get(params.id)
        if (!quoteInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])}"
            redirect(action: "list")
        } else {
            return [quoteInstance: quoteInstance]
        }
    }

    def update = {
        def quoteInstance = Quote.get(params.id)
        if (quoteInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (quoteInstance.version > version) {
                    
                    quoteInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'quote.label', default: 'Quote')] as Object[], "Another user has updated this Quote while you were editing")
                    render(view: "edit", model: [quoteInstance: quoteInstance])
                    return
                }
            }
            quoteInstance.properties = params
            if (!quoteInstance.hasErrors() && quoteInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'quote.label', default: 'Quote'), quoteInstance.toString()])}"
                redirect(action: "show", id: quoteInstance.id)
            } else {
                render(view: "edit", model: [quoteInstance: quoteInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def quoteInstance = Quote.get(params.id)
        if (quoteInstance) {
            try {
                quoteInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'quote.label', default: 'Quote')])}"
                redirect(action: "list")
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'quote.label', default: 'Quote')])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'quote.label', default: 'Quote'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def print = {
        def quoteInstance = Quote.get(params.id)
        if (quoteInstance) {
			def data = [
				quote:quoteInstance, items:quoteInstance.items,
				organization:quoteInstance.organization,
				person:quoteInstance.person,
				user:session.user,
				fullNumber:quoteInstance.fullNumber,
				taxRates:quoteInstance.taxRateSums,
				values:[
			        subtotalNet:quoteInstance.subTotalNet,
					subtotalGross:quoteInstance.subTotalGross,
					discountPercentAmount:quoteInstance.discountPercentAmount,
					total:quoteInstance.total
				]
			]
			String xml = (data as XML).toString()
//			println xml
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			fopService.generatePdf(
				new StringReader(xml), '/WEB-INF/data/fo/quote-fo.xsl', baos
			)
			response.contentType = 'application/pdf'
			response.addHeader 'Content-Disposition', 
				"attachment; filename=\"Angebot ${quoteInstance.fullNumber}.pdf\""
			response.contentLength = baos.size()
			response.outputStream.write(baos.toByteArray())
			response.outputStream.flush()
		} else {
			render(status: 404)
		}
	}
}
