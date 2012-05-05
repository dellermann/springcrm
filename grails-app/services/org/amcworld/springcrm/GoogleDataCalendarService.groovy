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

import com.google.api.client.auth.oauth2.Credential
import com.google.gdata.client.GoogleService
import com.google.gdata.data.contacts.ContactEntry

class GoogleDataCalendarService extends GoogleDataService<CalendarEvent, ContactEntry> {

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

    @Override
    protected GoogleService getServiceInstance() {
        // TODO Auto-generated method stub
        return null
    }
}
