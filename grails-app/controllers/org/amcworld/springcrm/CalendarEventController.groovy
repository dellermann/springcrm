/*
 * CalendarEventController.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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

import javax.servlet.http.HttpServletResponse



/**
 * The class {@code CalendarEventController} contains actions which manage
 * calendar events and reminders.
 *
 * @author  Daniel Ellermann
 * @version 1.3
 */
class CalendarEventController {

    //-- Class variables ------------------------

    static allowedMethods = [save: 'POST', update: 'POST', delete: 'GET']


    //-- Instance variables ---------------------

    CalendarEventService calendarEventService


    //-- Public methods -------------------------

    def index() {
        redirect action: 'list', params: params
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)

        def list, count
        if (params.search) {
            String searchFilter = "%${params.search}%".toString()
            list = CalendarEvent.findAllBySubjectLike(searchFilter, params)
            count = CalendarEvent.countBySubjectLike(searchFilter)
        } else {
            list = CalendarEvent.list(params)
            count = CalendarEvent.count()
        }

        calendarEventService.currentCalendarView = 'list'
        [calendarEventInstanceList: list, calendarEventInstanceTotal: count]
    }

    def calendar() {
        calendarEventService.currentCalendarView = 'calendar'
    }

    def listEmbedded(Long organization) {
        def l
        def count
        def linkParams
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        if (organization) {
            def organizationInstance = Organization.get(organization)
            if (organizationInstance) {
                l = CalendarEvent.findAllByOrganization(organizationInstance, params)
                count = CalendarEvent.countByOrganization(organizationInstance)
                linkParams = [organization: organizationInstance.id]
            }
        }
        [calendarEventInstanceList: l, calendarEventInstanceTotal: count, linkParams: linkParams]
    }

    /**
     * Returns a JSON array containing the calendar events between the given
     * start and end time stamp.  The method loads non-recurring events and
     * all occurrences of recurring events.  Usually, the method is called by
     * the <em>fullcalendar</em> JavaScript library via AJAX.
     *
     * @param start the given start time stamp in seconds since the UNIX epoch
     * @param end   the given end time stamp in seconds since the UNIX epoch
     * @return      the rendered JSON response containing the calendar events
     */
    def listRange(Long start, Long end) {
        Date startDate = new Date(start * 1000L)
        Date endDate = new Date(end * 1000L)

        /* load non-recurring events */
        def c = CalendarEvent.createCriteria()
        List<CalendarEvent> list = c.list {
            and {
                eq('recurrence.type', 0)
                or {
                    between('start', startDate, endDate)
                    between('end', startDate, endDate)
                    and {
                        le('start', startDate)
                        ge('end', endDate)
                    }
                }
            }
        }

        /* add occurrences of recurring events to list */
        c = CalendarEvent.createCriteria()
        List<CalendarEvent> l = c.list {
            ne('recurrence.type', 0)
        }
        for (CalendarEvent ce in l) {
            def helper = new RecurCalendarEventHelper(ce.recurrence)
            Date s = ce.start
            Date d = helper.approximate(s, startDate)
            while (d <= endDate) {
                def occurrence = ce.eventAtDate(d)
                occurrence.synthetic = true
                list << occurrence
                Date dOld = d + 1
                d = helper.approximate(s, dOld)
                assert d > dOld
            }
        }

        render(contentType: 'text/json') {
            array {
                for (CalendarEvent ce in list) {
                    event(
                        id: ce.id, title: ce.subject, allDay: ce.allDay,
                        start: ce.start.time / 1000L, end: ce.end.time / 1000L,
                        url: createLink(action: 'show', id: ce.id),
                        editable: !ce.synthetic
                    )
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
            c.add Calendar.HOUR_OF_DAY, 1
            calendarEventInstance.end = c.time
        }
        [calendarEventInstance: calendarEventInstance]
    }

    def copy(Long id) {
        def calendarEventInstance = CalendarEvent.get(id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calenderEvent.label', default: 'Calendar event'), id])
            redirect action: 'show', id: id
            return
        }

        calendarEventInstance = new CalendarEvent(calendarEventInstance)
        render view: 'create', model: [calendarEventInstance: calendarEventInstance]
    }

    def save() {
        def calendarEventInstance = new CalendarEvent(params)
        calendarEventInstance.owner = session.user
        if (!calendarEventInstance.validate()) {
            render view: 'create', model: [calendarEventInstance: calendarEventInstance]
            return
        }

        calendarEventService.refineCalendarEvent calendarEventInstance, params['recurrence.endType'], params['recurrence.cnt'] as Integer
        calendarEventInstance.owner = session.user
        calendarEventInstance.save flush: true
        calendarEventService.saveReminders params.reminders.split(), calendarEventInstance

        request.calendarEventInstance = calendarEventInstance
        flash.message = message(code: 'default.created.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), calendarEventInstance.toString()])
        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: calendarEventInstance.ident()
        }
    }

    def show(Long id) {
        def calendarEventInstance = CalendarEvent.get(id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), id])
            redirect action: 'list'
            return
        }

        List<Reminder> reminderInstanceList = calendarEventService.loadReminders(calendarEventInstance)
        [calendarEventInstance: calendarEventInstance, reminderInstanceList: reminderInstanceList]
    }

    def edit(Long id) {
        def calendarEventInstance = CalendarEvent.get(id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), id])
            redirect action: 'list'
            return
        }

        List<String> reminderRules = calendarEventService.loadReminderRules(calendarEventInstance)
        [calendarEventInstance: calendarEventInstance, reminderRules: reminderRules.join(' ')]
    }

    def update(Long id) {
        def calendarEventInstance = CalendarEvent.get(id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), id])
            redirect action: 'list'
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (calendarEventInstance.version > version) {
                calendarEventInstance.errors.rejectValue('version', 'default.optimistic.locking.failure', [message(code: 'calendarEvent.label', default: 'CalendarEvent')] as Object[], "Another user has updated this CalendarEvent while you were editing")
                render view: 'edit', model: [calendarEventInstance: calendarEventInstance]
                return
            }
        }

        calendarEventInstance.properties = params
        if (!calendarEventInstance.validate()) {
            render view: 'edit', model: [calendarEventInstance: calendarEventInstance]
            return
        }

        calendarEventService.refineCalendarEvent calendarEventInstance, params['recurrence.endType'], params['recurrence.cnt'] as Integer
        calendarEventInstance.save flush: true
        calendarEventService.saveReminders params.reminders.split(), calendarEventInstance, session.user

        request.calendarEventInstance = calendarEventInstance
        flash.message = message(code: 'default.updated.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), calendarEventInstance.toString()])

        if (params.returnUrl) {
            redirect url: params.returnUrl
        } else {
            redirect action: 'show', id: calendarEventInstance.id
        }
    }

    /**
     * Updates the start and end time stamp of the calendar events with the
     * given ID.  The action is called when moving or resizing calendar events
     * in the calendar view.  Currently, changing the start and end time stamp
     * or recurring calendar events is not supported.
     *
     * @param start the given start time stamp in milliseconds since the UNIX
     *              epoch
     * @param end   the given end time stamp in milliseconds since the UNIX
     *              epoch
     * @return      the HTTP status code
     */
    def updateStartEnd(Long id, Long start, Long end) {
        Date startDate = new Date(start)
        Date endDate = new Date(end)

        def calendarEventInstance = CalendarEvent.get(id)
        if (!calendarEventInstance) {
            render status: HttpServletResponse.SC_NOT_FOUND
            return
        }
        if (calendarEventInstance.recurrence.type) {
            render status: HttpServletResponse.SC_NOT_IMPLEMENTED
            return
        }

        calendarEventInstance.start = startDate
        calendarEventInstance.end = endDate
        calendarEventInstance.save()
        calendarEventService.updateReminders calendarEventInstance, session.user

        render status: HttpServletResponse.SC_OK
    }

    def delete(Long id) {
        def calendarEventInstance = CalendarEvent.get(id)
        if (!calendarEventInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent'), id])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: calendarEventService.currentCalendarView ?: 'list'
            }
            return
        }

        try {
            calendarEventInstance.delete flush: true
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent')])
            if (params.returnUrl) {
                redirect url: params.returnUrl
            } else {
                redirect action: calendarEventService.currentCalendarView ?: 'list'
            }
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'calendarEvent.label', default: 'CalendarEvent')])
            redirect action: 'show', id: id
        }
    }

    def reminders() {
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

        render(contentType: 'text/json') {
            array {
                for (Reminder r in list) {
                    reminder title: r.calendarEvent.subject, allDay: r.calendarEvent.allDay, start: r.calendarEvent.start, end: r.calendarEvent.end, url: createLink(controller: 'calendarEvent', action: 'show', id: r.calendarEvent.id)
                }
            }
        }
    }
}
