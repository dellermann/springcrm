/*
 * CalendarEventService.groovy
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

import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder


/**
 * The class {@code CalendarEventService} contains service methods to work with
 * calendar events.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.3
 */
class CalendarEventService {

    //-- Public methods -------------------------

    /**
     * Retrieves the current calendar view from the session.
     *
     * @return  the name of the calendar view to store; must be either
     *          {@code list} or {@code calendar}
     * @since   1.3
     */
    String getCurrentCalendarView() {
        session.currentCalendarView
    }

    /**
     * Loads the rules of all reminders of the given calendar event for the
     * current user.
     *
     * @param calendarEvent the given calendar event
     * @return              the list of reminder rules
     */
    List<String> loadReminderRules(CalendarEvent calendarEvent) {
        loadReminders(calendarEvent)*.rule
    }

    /**
     * Loads all reminders of the given calendar event for the current user.
     *
     * @param calendarEvent the given calendar event
     * @return              the list of reminders
     */
    List<Reminder> loadReminders(CalendarEvent calendarEvent) {
        def query = Reminder.where {
            user == session.user && calendarEvent == calendarEvent
        }
        List<Reminder> reminders = query.list()
        if (!reminders) {
            query = Reminder.where {
                user == null && calendarEvent == calendarEvent
            }
            reminders = query.list()
        }
        reminders
    }

    /**
     * Refines the given calendar event by computing the calibrated start and
     * end time stamp.
     *
     * @param calendarEvent     the given calendar event
     * @param recurrenceEndType the recurrence end type
     * @param recurrenceCount   the number of recurrences; must be set if end
     *                          type is {@code count}
     */
    void refineCalendarEvent(CalendarEvent calendarEvent,
                             String recurrenceEndType,
                             Integer recurrenceCount = null) {
        def helper = new RecurCalendarEventHelper(calendarEvent.recurrence)
        Calendar dayStart = calendarEvent.start.toCalendar()
        dayStart.clearTime()
        int offset = calendarEvent.start.time - dayStart.time.time
        int diff = calendarEvent.end.time - calendarEvent.start.time
        def start = helper.calibrateStart(calendarEvent.start)
        calendarEvent.start = new Date(start.time + offset)
        calendarEvent.end = new Date(start.time + offset + diff)

        def until = null
        switch (recurrenceEndType) {
        case 'until':
            until = helper.approximate(start, calendarEvent.recurrence.until)
            if (until < start) {
                until = start
            }
            break
        case 'count':
            until = helper.computeNthEvent(start, recurrenceCount)
            break
        }
        calendarEvent.recurrence.until = until
    }

    /**
     * Saves the reminders from the given rules for the stated calendar event
     * and user.
     *
     * @param reminderRules the reminder rules to save
     * @param calendarEvent the calendar event to save the reminders for
     * @param user          the user to save the reminders for; if {@code null}
     *                      the owner is used
     */
    void saveReminders(String [] reminderRules, CalendarEvent calendarEvent,
                       User user = null)
    {
        if (calendarEvent.owner.id == user?.id) {
            user = null
        }

        def query = Reminder.where {
            calendarEvent == calendarEvent && (user == null || user == user)
        }
        query.deleteAll()

        for (String rule in reminderRules) {
            Reminder reminder = Reminder.fromRule rule
            reminder.calendarEvent = calendarEvent
            reminder.user = user
            Date d = calendarEvent.start
            if (calendarEvent.recurrence.type > 0) {
                def helper = new RecurCalendarEventHelper(
                    calendarEvent.recurrence
                )
                d = helper.approximate(d)
            }
            reminder.nextReminder = new Date(d.time - reminder.valueAsMilliseconds)
            reminder.save()
        }
    }

    void updateReminders(CalendarEvent calendarEvent, User user = null) {
        if (calendarEvent.owner.id == user?.id) {
            user = null
        }

        List<Reminder> reminders = Reminder.findAllByCalendarEventAndUser(calendarEvent, user)
        for (Reminder reminder in reminders) {
            Date d = calendarEvent.start
            if (calendarEvent.recurrence.type > 0) {
                def helper = new RecurCalendarEventHelper(
                    calendarEvent.recurrence
                )
                d = helper.approximate(d)
            }
            reminder.nextReminder = new Date(d.time - reminder.valueAsMilliseconds)
            reminder.save()
        }
    }

    /**
     * Stores the current calendar view in the session.
     *
     * @param view  the name of the calendar view to store; must be either
     *              {@code list} or {@code calendar}
     * @since       1.3
     */
    void setCurrentCalendarView(String view) {
        session.currentCalendarView = view
    }


    //-- Non-public methods ---------------------

    /**
     * Returns access to the user session.
     *
     * @return the session instance
     */
    protected HttpSession getSession() {
        RequestContextHolder.currentRequestAttributes().session
    }
}
