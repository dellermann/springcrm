/*
 * GoogleCalendarSync.groovy
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


package org.amcworld.springcrm.google

import com.google.gdata.client.GoogleService
import com.google.gdata.data.contacts.ContactEntry
import org.amcworld.springcrm.CalendarEvent


/**
 * The class {@code GoogleCalendarSync} synchronizes calendar entries with
 * Google.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 * @since   1.0
 */
class GoogleCalendarSync extends GoogleSync<CalendarEvent, ContactEntry> {

    //-- Instance variables ---------------------

    protected com.google.api.services.calendar.Calendar svc


    //-- Constructors ---------------------------

    /**
     * Creates a new Google synchronization instance for calendar entries.
     */
    GoogleCalendarSync() {
        super(CalendarEvent)
    }


    //-- Non-public methods ---------------------

    @Override
    protected ContactEntry convertToGoogle(CalendarEvent item, ContactEntry entry) {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    protected ContactEntry convertToGoogle(CalendarEvent item) {
        // TODO Auto-generated method stub
        return null
    }

    /**
     * Gets access to the underlying Google API service.  The service is fully
     * authenticated.
     *
     * @return  the Google API service instance
     */
    protected synchronized GoogleService getService() {
        if (!svc) {
            svc = com.google.api.services.calendar.Calendar.builder(
                    HTTP_TRANSPORT, JSON_FACTORY
                )
                .setApplicationName(APPLICATION_NAME)
                .setHttpRequestInitializer(loadCredential())
                .build()
        }
        return svc
    }

    @Override
    protected CalendarEvent convertToLocal(CalendarEvent localEntry,
            ContactEntry googleEntry) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void deleteGoogleEntry(ContactEntry entry) {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean getAllowLocalCreate() {
        return getBooleanSystemConfig('syncCalendarEventsOptionsAllowCreate')
    }

    @Override
    protected boolean getAllowLocalDelete() {
        return getBooleanSystemConfig('syncCalendarEventsOptionsAllowDelete')
    }

    @Override
    protected boolean getAllowLocalModify() {
        return getBooleanSystemConfig('syncCalendarEventsOptionsAllowModify')
    }

    @Override
    protected String getEtag(ContactEntry entry) {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    protected String getUrl(ContactEntry entry) {
        return null
    }

    @Override
    protected ContactEntry insertGoogleEntry(ContactEntry entry) {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    protected Map<String, ContactEntry> loadGoogleEntries() {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    protected void updateGoogleEntry(ContactEntry entry) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String googleEntryToString(ContactEntry entry) {
        // TODO Auto-generated method stub
        return null;
    }
}
