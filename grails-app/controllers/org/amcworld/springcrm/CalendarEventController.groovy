package org.amcworld.springcrm

class CalendarEventController {

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']

    def index = {
        redirect(action: 'list', params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [calendarEventInstanceList: CalendarEvent.list(params), calendarEventInstanceTotal: CalendarEvent.count()]
    }

	def calendar = {}

	def listEmbedded = {
		def l
		def count
		def linkParams
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
			l = CalendarEvent.findAllByOrganization(organizationInstance, params)
			count = CalendarEvent.countByOrganization(organizationInstance)
			linkParams = [organization:organizationInstance.id]
		}
		[calendarEventInstanceList:l, calendarEventInstanceTotal:count, linkParams:linkParams]
	}

	def listRange = {
    	Date start = new Date((params.start as Long) * 1000L)
		Date end = new Date((params.end as Long) * 1000L)
		def c = CalendarEvent.createCriteria()
		def l = c.list {
			or {
				between('start', start, end)
				between('end', start, end)
				and {
					le('start', start)
					ge('end', end)
				}
			}
		}
		render(contentType:"text/json") {
			array {
				for (ce in l) {
					event id:ce.id, title:ce.subject, allDay:ce.allDay, start:ce.start, end:ce.end, url:createLink(controller:'calendarEvent', action:'show', id:ce.id)
				}
			}
		}
	}

    def create = {
        def calendarEventInstance = new CalendarEvent()
        calendarEventInstance.properties = params
		if (calendarEventInstance.organization) {
			calendarEventInstance.location = calendarEventInstance.organization.shippingAddr
		}
		if (params.start) {
			println "Start: ${params.start} / ${calendarEventInstance.start}"
			def c = calendarEventInstance.start.toCalendar()
			c.add(Calendar.HOUR_OF_DAY, 1)
			calendarEventInstance.end = c.time 
		}
        return [calendarEventInstance: calendarEventInstance]
    }
	
	def copy = {
		def calendarEventInstance = CalendarEvent.get(params.id)
		if (calendarEventInstance) {
			calendarEventInstance = new CalendarEvent(calendarEventInstance)
			render(view:'create', model:[calendarEventInstance:calendarEventInstance])
		} else {
			flash.message = "${message(code:'default.not.found.message', args:[message(code:'calenderEvent.label', default:'Calendar event'), params.id])}"
			redirect(action: 'show', id: calendarEventInstance.id)
		}
	}

    def save = {
        def calendarEventInstance = new CalendarEvent(params)
        if (calendarEventInstance.save(flush: true)) {
			calendarEventInstance.index()
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), calendarEventInstance.toString()])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'show', id: calendarEventInstance.id)
			}
        } else {
            render(view: 'create', model: [calendarEventInstance: calendarEventInstance])
        }
    }

    def show = {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (!calendarEventInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])}"
            redirect(action: 'list')
        } else {
            [calendarEventInstance: calendarEventInstance]
        }
    }

    def edit = {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (!calendarEventInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])}"
            redirect(action: 'list')
        } else {
            return [calendarEventInstance: calendarEventInstance]
        }
    }

    def update = {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (calendarEventInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (calendarEventInstance.version > version) {
                    
                    calendarEventInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'calendarEvent.label', default: 'CalendarEvent')] as Object[], "Another user has updated this CalendarEvent while you were editing")
                    render(view: 'edit', model: [calendarEventInstance: calendarEventInstance])
                    return
                }
            }
            calendarEventInstance.properties = params
            if (!calendarEventInstance.hasErrors() && calendarEventInstance.save(flush: true)) {
				calendarEventInstance.reindex()
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), calendarEventInstance.toString()])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'show', id: calendarEventInstance.id)
				}
            } else {
                render(view: 'edit', model: [calendarEventInstance: calendarEventInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])}"
            redirect(action: 'list')
        }
    }

    def delete = {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (calendarEventInstance && params.confirmed) {
            try {
                calendarEventInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent')])}"
				if (params.returnUrl) {
					redirect(url:params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent')])}"
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])}"
			if (params.returnUrl) {
				redirect(url:params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }
}
