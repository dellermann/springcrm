/*
 * GoogleDataCalendarService.groovy
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

import java.util.Map;
import com.google.gdata.client.GoogleService
import com.google.gdata.data.contacts.ContactEntry
import org.amcworld.springcrm.google.GoogleAuthException


class GoogleDataCalendarService extends GoogleDataService<CalendarEvent, ContactEntry> {

    //-- Class variables ------------------------

    static scope = 'session'
    static transactional = false


    //-- Instance variables ---------------------

    com.google.api.services.calendar.Calendar svc


    //-- Public methods -------------------------

    def serviceMethod() {

    }

    @Override
    public ContactEntry convertToGoogle(CalendarEvent item, ContactEntry entry) {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    public ContactEntry convertToGoogle(CalendarEvent item) {
        // TODO Auto-generated method stub
        return null
    }


    //-- Non-public methods ---------------------

    /**
     * Gets access to the underlying Google API service.  The service is fully
     * authenticated.
     *
     * @return  the Google API service instance
     * @since   1.0
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
    protected String getEtag(ContactEntry entry) {
        // TODO Auto-generated method stub
        return null
    }

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
}
