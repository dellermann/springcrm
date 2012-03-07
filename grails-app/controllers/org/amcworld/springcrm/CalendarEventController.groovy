/*
 * CalendarEventController.groovy
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

import org.springframework.dao.DataIntegrityViolationException


/**
 * The class {@code CalendarEventController} contains actions which manage
 * calendar events and reminders.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class CalendarEventController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Public methods -------------------------

    def index() {
        redirect(action: 'list', params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        return [calendarEventInstanceList: CalendarEvent.list(params), calendarEventInstanceTotal: CalendarEvent.count()]
    }

	def calendar() {}

	def listEmbedded() {
		def l
		def count
		def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		if (params.organization) {
			def organizationInstance = Organization.get(params.organization)
            if (organizationInstance) {
    			l = CalendarEvent.findAllByOrganization(organizationInstance, params)
    			count = CalendarEvent.countByOrganization(organizationInstance)
    			linkParams = [organization: organizationInstance.id]
            }
		}
		return [calendarEventInstanceList: l, calendarEventInstanceTotal: count, linkParams: linkParams]
	}

	def listRange() {
    	Date start = new Date((params.start as Long) * 1000L)
		Date end = new Date((params.end as Long) * 1000L)
		def c = CalendarEvent.createCriteria()
		def list = c.list {
            and {
                eq('recurrence.type', 0)
                or {
                    between('start', start, end)
                    between('end', start, end)
                    and {
                        le('start', start)
                        ge('end', end)
                    }
                }
            }
		}
        c = CalendarEvent.createCriteria()
        def l = c.list {
            ne('recurrence.type', 0)
        }
        for (CalendarEvent ce in l) {
            def helper = new RecurCalendarEventHelper(ce.recurrence)
            Date s = ce.start
            Date d = helper.approximate(s, start)
            while (d <= end) {
                list << ce.eventAtDate(d)
                Date dOld = d + 1
                d = helper.approximate(s, dOld)
                assert d > dOld
            }
        }
		render(contentType: "text/json") {
			array {
				for (ce in list) {
					event id: ce.id, title: ce.subject, allDay: ce.allDay, start: ce.start, end: ce.end, url: createLink(controller: 'calendarEvent', action: 'show', id: ce.id)
				}
			}
		}
	}

    def create() {
        def calendarEventInstance = new CalendarEvent()
        calendarEventInstance.properties = params
		if (calendarEventInstance.organization) {
			calendarEventInstance.location = calendarEventInstance.organization.shippingAddr
		}
		if (params.start) {
			def c = calendarEventInstance.start.toCalendar()
			c.add(Calendar.HOUR_OF_DAY, 1)
			calendarEventInstance.end = c.time
		}
        return [calendarEventInstance: calendarEventInstance]
    }

	def copy() {
		def calendarEventInstance = CalendarEvent.get(params.id)
		if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calenderEvent.label', default: 'Calendar event'), params.id])
            redirect(action: 'show', id: params.id)
            return
        }

		calendarEventInstance = new CalendarEvent(calendarEventInstance)
		render(view: 'create', model: [calendarEventInstance: calendarEventInstance])
	}

    def save() {
        def calendarEventInstance = new CalendarEvent(params)
        calendarEventInstance.owner = session.user
        if (!calendarEventInstance.validate()) {
            render(view: 'create', model: [calendarEventInstance: calendarEventInstance])
            return
        }
        params.id = calendarEventInstance.ident()

		refineCalendarEvent(calendarEventInstance)
        calendarEventInstance.owner = session.user
		calendarEventInstance.save(flush: true)
        saveReminders(calendarEventInstance)
		calendarEventInstance.index()
        flash.message = message(code: 'default.created.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), calendarEventInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: calendarEventInstance.id)
		}
    }

    def show() {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])
            redirect(action: 'list')
            return
        }

        return [calendarEventInstance: calendarEventInstance]
    }

    def edit() {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])
            redirect(action: 'list')
            return
        }

        def c = Reminder.createCriteria()
        def l = c.list {
            eq('user', session.user)
            eq('calendarEvent', calendarEventInstance)
        }
        if (l.isEmpty()) {
            c = Reminder.createCriteria()
            l = c.list {
                isNull('user')
                eq('calendarEvent', calendarEventInstance)
            }
        }
        def reminderInstanceList = l*.rule
        return [calendarEventInstance: calendarEventInstance, reminderInstanceList: reminderInstanceList.join(' ')]
    }

    def update() {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])
            redirect(action: 'list')
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (calendarEventInstance.version > version) {

                calendarEventInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'calendarEvent.label', default: 'CalendarEvent')] as Object[], "Another user has updated this CalendarEvent while you were editing")
                render(view: 'edit', model: [calendarEventInstance: calendarEventInstance])
                return
            }
        }
        calendarEventInstance.properties = params
        if (!calendarEventInstance.validate()) {
            render(view: 'edit', model: [calendarEventInstance: calendarEventInstance])
            return
        }

		refineCalendarEvent(calendarEventInstance)
		calendarEventInstance.save(flush: true)
        saveReminders(calendarEventInstance, session.user)
		calendarEventInstance.reindex()
        flash.message = message(code: 'default.updated.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), calendarEventInstance.toString()])
		if (params.returnUrl) {
			redirect(url: params.returnUrl)
		} else {
			redirect(action: 'show', id: calendarEventInstance.id)
		}
    }

    def delete() {
        def calendarEventInstance = CalendarEvent.get(params.id)
        if (calendarEventInstance && params.confirmed) {
            try {
                calendarEventInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent')])
				if (params.returnUrl) {
					redirect(url: params.returnUrl)
				} else {
					redirect(action: 'list')
				}
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent')])
                redirect(action: 'show', id: params.id)
            }
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), params.id])
			if (params.returnUrl) {
				redirect(url: params.returnUrl)
			} else {
				redirect(action: 'list')
			}
        }
    }

    def reminders = {
        def c = Reminder.createCriteria()
        def l = c.list {
            and {
                or {
                    isNull('user')
                    eq('user', session.user)
                }
                le('nextReminder', new Date())
            }
        }

        def list = []
        for (Reminder r in l) {
            if (r.user != null) {
                list[r.calendarEvent] = r
            }
        }
        for (Reminder r in l) {
            if (r.user == null && list[r.calendarEvent] == null) {
                list[r.calendarEvent] = r
            }
        }

        render(contentType:"text/json") {
            array {
                for (Reminder r in list) {
                    reminder title:r.calendarEvent.subject, allDay:r.calendarEvent.allDay, start:r.calendarEvent.start, end:r.calendarEvent.end, url:createLink(controller:'calendarEvent', action:'show', id:r.calendarEvent.id)
                }
            }
        }
    }


    //-- Non-public methods ---------------------

	private void refineCalendarEvent(CalendarEvent calendarEventInstance) {
		def helper = new RecurCalendarEventHelper(
			calendarEventInstance.recurrence
		)
        Calendar dayStart = calendarEventInstance.start.toCalendar()
        dayStart.clearTime()
        int offset = calendarEventInstance.start.time - dayStart.time.time
		int diff = calendarEventInstance.end.time - calendarEventInstance.start.time
		def start = helper.calibrateStart(calendarEventInstance.start)
		calendarEventInstance.start = new Date(start.time + offset)
		calendarEventInstance.end = new Date(start.time + offset + diff)

		def until = null
		switch (params['recurrence.endType']) {
		case 'until':
			until = helper.approximate(start, calendarEventInstance.recurrence.until)
			if (until < start) {
				until = start
			}
			break
		case 'count':
			until = helper.computeNthEvent(start, params['recurrence.cnt'] as Integer)
			break
		}
		calendarEventInstance.recurrence.until = until
	}

    private void saveReminders(CalendarEvent calendarEventInstance,
                               User user = null)
    {
        if (calendarEventInstance.owner.id == user?.id) {
            user = null
        }
        def l = Reminder.findAllByCalendarEventAndUser(calendarEventInstance, user)
        for (Reminder r in l) {
            r.delete()
        }

        String [] reminderRules = params.reminders.split()
        for (String rule in reminderRules) {
            Reminder reminder = Reminder.fromRule(rule)
            reminder.calendarEvent = calendarEventInstance
            reminder.user = user
            Date d = calendarEventInstance.start
            if (calendarEventInstance.recurrence.type > 0) {
                def helper = new RecurCalendarEventHelper(
                    calendarEventInstance.recurrence
                )
                d = helper.approximate(d)
            }
            reminder.nextReminder =
                new Date(d.time - reminder.valueAsMilliseconds)
            reminder.save(flush:true)
        }
    }
}
