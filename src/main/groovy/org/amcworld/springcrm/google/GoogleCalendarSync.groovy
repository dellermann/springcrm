/*
 * GoogleCalendarSync.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import org.amcworld.springcrm.User;

import com.google.api.client.auth.oauth2.Credential
import com.google.gdata.client.GoogleService
import com.google.gdata.data.contacts.ContactEntry
import groovy.transform.CompileStatic
import org.amcworld.springcrm.CalendarEvent


/**
 * The class {@code GoogleCalendarSync} synchronizes calendar entries with
 * Google.
 *
 * @author	Daniel Ellermann
 * @version 2.0
 * @since   1.0
 */
class GoogleCalendarSync extends AbstractGoogleSync<CalendarEvent, ContactEntry> {

    //-- Constructors ---------------------------

    /**
     * Creates a new Google synchronization instance for calendar entries.
     */
    GoogleCalendarSync() {
        super(CalendarEvent)
    }


    //-- Non-public methods ---------------------

    @Override
    @CompileStatic
    protected ContactEntry convertToGoogle(CalendarEvent item,
                                           ContactEntry entry)
    {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    @CompileStatic
    protected ContactEntry convertToGoogle(CalendarEvent item) {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    @CompileStatic
    protected synchronized GoogleService getService(Credential credential) {
        // TODO implement it the right way
//        new com.google.api.services.calendar.Calendar.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, credential
//            )
//            .setApplicationName(APPLICATION_NAME)
//            .build()
        null
    }

    @Override
    @CompileStatic
    protected CalendarEvent convertToLocal(GoogleService service,
                                           CalendarEvent localEntry,
                                           ContactEntry googleEntry)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @CompileStatic
    protected void deleteGoogleEntry(ContactEntry entry) {
        // TODO Auto-generated method stub
    }

    @Override
    @CompileStatic
    protected boolean getAllowLocalCreate() {
        getBooleanSystemConfig 'syncCalendarEventsOptionsAllowCreate'
    }

    @Override
    @CompileStatic
    protected boolean getAllowLocalDelete() {
        getBooleanSystemConfig 'syncCalendarEventsOptionsAllowDelete'
    }

    @Override
    @CompileStatic
    protected boolean getAllowLocalModify() {
        getBooleanSystemConfig 'syncCalendarEventsOptionsAllowModify'
    }

    @Override
    @CompileStatic
    protected String getEtag(ContactEntry entry) {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    @CompileStatic
    protected String getUrl(ContactEntry entry) {
        return null
    }

    @Override
    @CompileStatic
    protected ContactEntry insertGoogleEntry(GoogleService service,
                                             ContactEntry entry)
    {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    @CompileStatic
    protected boolean isExcluded(CalendarEvent localEntry, User user) {
        false
    }

    @Override
    @CompileStatic
    protected Map<String, ContactEntry> loadGoogleEntries(GoogleService service)
    {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    @CompileStatic
    protected void updateGoogleEntry(GoogleService service, ContactEntry entry)
    {
        // TODO Auto-generated method stub
    }

    @Override
    @CompileStatic
    protected String googleEntryToString(ContactEntry entry) {
        // TODO Auto-generated method stub
        return null
    }
}
