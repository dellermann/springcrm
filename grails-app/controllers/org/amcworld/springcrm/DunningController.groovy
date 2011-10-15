package org.amcworld.springcrm

import grails.converters.XML

class DunningController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']
	
	def fopService
	def seqNumberService

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [dunningInstanceList: Dunning.list(params), dunningInstanceTotal: Dunning.count()]
    }

	def listEmbedded = {
		def l
		def count
		def linkParams
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = Dunning.findAllByOrganization(organizationInstance, params)
			count = Dunning.countByOrganization(organizationInstance)
			linkParams = [organization:organizationInstance.id]
		} else if (params.person) {
			def personInstance = Person.get(params.person)
			l = Dunning.findAllByPerson(personInstance, params)
			count = Dunning.countByPerson(personInstance)
			linkParams = [person:personInstance.id]
		} else if (params.invoice) {
			def invoiceInstance = Invoice.get(params.invoice)
			l = Dunning.findAllByInvoice(invoiceInstance, params)
			count = Dunning.countByInvoice(invoiceInstance)
			linkParams = [invoice:invoiceInstance.id]
		}
		[dunningInstanceList:l, dunningInstanceTotal:count, linkParams:linkParams]
	}

    def create = {
        def dunningInstance
		if (params.invoice) {
			def invoiceInstance = Invoice.get(params.invoice)
			dunningInstance = new Dunning(invoiceInstance)
		} else {
			dunningInstance = new Dunning()
			dunningInstance.properties = params
		}

		Organization org = dunningInstance.organization
		if (org) {
			dunningInstance.billingAddrCountry = org.billingAddrCountry
			dunningInstance.billingAddrLocation = org.billingAddrLocation
			dunningInstance.billingAddrPoBox = org.billingAddrPoBox
			dunningInstance.billingAddrPostalCode = org.billingAddrPostalCode
			dunningInstance.billingAddrState = org.billingAddrState
			dunningInstance.billingAddrStreet = org.billingAddrStreet
			dunningInstance.shippingAddrCountry = org.shippingAddrCountry
			dunningInstance.shippingAddrLocation = org.shippingAddrLocation
			dunningInstance.shippingAddrPoBox = org.shippingAddrPoBox
			dunningInstance.shippingAddrPostalCode = org.shippingAddrPostalCode
			dunningInstance.shippingAddrState = org.shippingAddrState
			dunningInstance.shippingAddrStreet = org.shippingAddrStreet
		}

		ConfigHolder config = ConfigHolder.instance
		Integer serviceId = config['serviceIdDunningCharge']
		if (serviceId) {
			def service = Service.get(serviceId)
			if (service) {
				dunningInstance.addToItems(serviceToItem(service))
			}
		}
		serviceId = config['serviceIdDefaultInterest']
		if (serviceId) {
			def service = Service.get(serviceId)
			if (service) {
				dunningInstance.addToItems(serviceToItem(service))
			}
		}
        return [dunningInstance: dunningInstance]
    }
	
	def copy = {
		def dunningInstance = Dunning.get(params.id)
		if (dunningInstance) {
			dunningInstance = new Dunning(dunningInstance)
			render(view:'create', model:[dunningInstance:dunningInstance])
		} else {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), params.id])}"
			redirect(action: 'show', id: dunningInstance.id)
		}
	}

    def save = {
        def dunningInstance = new Dunning(params)
        if (dunningInstance.save(flush:true)) {
			dunningInstance.index()

			def invoiceInstance = dunningInstance.invoice
			invoiceInstance.stage = InvoiceStage.get(904)
			invoiceInstance.save(flush:true)
			invoiceInstance.reindex()

            flash.message = "${message(code: 'default.created.message', args: [message(code: 'dunning.label', default: 'Dunning'), dunningInstance.toString()])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'show', id: dunningInstance.id)
			}
        } else {
        	log.debug(dunningInstance.errors)
            render(view: 'create', model: [dunningInstance: dunningInstance])
        }
    }

    def show = {
        def dunningInstance = Dunning.get(params.id)
        if (!dunningInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), params.id])}"
            redirect(action: 'list')
        } else {
            [dunningInstance: dunningInstance]
        }
    }

    def edit = {
        def dunningInstance = Dunning.get(params.id)
        if (!dunningInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), params.id])}"
            redirect(action: 'list')
        } else {
			if (session.user.admin || dunningInstance.stage.id < 2202) {
				return [dunningInstance: dunningInstance]
			}
			redirect(action:'list')
        }
    }

    def update = {
        def dunningInstance = Dunning.get(params.id)
        if (dunningInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (dunningInstance.version > version) {
                    
                    dunningInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'dunning.label', default: 'Dunning')] as Object[], "Another user has updated this Dunning while you were editing")
                    render(view: 'edit', model: [dunningInstance: dunningInstance])
                    return
                }
            }
			if (params.autoNumber) {
				params.number = dunningInstance.number
			}
            dunningInstance.properties = params

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
			dunningInstance.items?.clear()
			for (int i = 0; params."items[${i}]"; i++) {
				if (params."items[${i}]".id != 'null') {
					dunningInstance.addToItems(params."items[${i}]")
				}
			}
            if (!dunningInstance.hasErrors() && dunningInstance.save(flush: true)) {
				dunningInstance.reindex()

				def invoiceInstance = dunningInstance.invoice
				invoiceInstance.stage = InvoiceStage.get(904)
				invoiceInstance.save(flush:true)
				invoiceInstance.reindex()

				flash.message = "${message(code: 'default.updated.message', args: [message(code: 'dunning.label', default: 'Dunning'), dunningInstance.toString()])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'show', id: dunningInstance.id)
				}
            } else {
                render(view: 'edit', model: [dunningInstance: dunningInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def dunningInstance = Dunning.get(params.id)
        if (dunningInstance && params.confirmed) {
			if (!session.user.admin && dunningInstance.stage.id >= 2202) {
				redirect(action:'list')
			}
            try {
                dunningInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'dunning.label', default: 'Dunning')])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'dunning.label', default: 'Dunning')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dunning.label', default: 'Dunning'), params.id])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

	def print = {
        def dunningInstance = Dunning.get(params.id)
        if (dunningInstance) {
			def data = [
				transaction:dunningInstance,
				items:dunningInstance.items,
				organization:dunningInstance.organization,
				person:dunningInstance.person,
				invoice:dunningInstance.invoice,
				invoiceFullNumber:dunningInstance.invoice.fullNumber,
				user:session.user,
				fullNumber:dunningInstance.fullNumber,
				taxRates:dunningInstance.taxRateSums,
				values:[
			        subtotalNet:dunningInstance.subtotalNet,
					subtotalGross:dunningInstance.subtotalGross,
					discountPercentAmount:dunningInstance.discountPercentAmount,
					total:dunningInstance.total
				],
				watermark:params.duplicate ? 'duplicate' : ''
			]
			String xml = (data as XML).toString()
//			println xml
			
			GString fileName = "${message(code: 'dunning.label')} ${dunningInstance.fullNumber}"
			if (params.duplicate) {
				fileName += " (${message(code: 'invoicingTransaction.duplicate')})"
			}
			fileName += ".pdf"
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream()
			fopService.generatePdf(
				new StringReader(xml), '/WEB-INF/data/fo/dunning-fo.xsl',
				baos
			)
			response.contentType = 'application/pdf'
			response.addHeader 'Content-Disposition', 
				"attachment; filename=\"${fileName}\""
			response.contentLength = baos.size()
			response.outputStream.write(baos.toByteArray())
			response.outputStream.flush()
		} else {
			render(status: 404)
		}
	}

	private InvoicingItem serviceToItem(Service s) {
		return new InvoicingItem(
			number:s.fullNumber, quantity:s.quantity, unit:s.unit.toString(),
			name:s.name, description:s.description, unitPrice:s.unitPrice,
			tax:s.taxClass.taxValue * 100
		)
	}
}
