/*
 * AddressModule.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


package org.amcworld.springcrm.module


class AddressModule extends ShowFieldColModule {

    //-- Class variables ------------------------

    static content = {
        country { row[5].fieldText }
        location { row[3].fieldText }
        mapBtn { module ButtonModule, row[6].field.find('.button') }
        street { row[0].fieldText }
        poBox { row[1].fieldText }
        postalCode { row[2].fieldText }
        state { row[4].fieldText }
    }


    //-- Public methods -------------------------

    void checkMapButton() {
        assert 'Auf der Karte zeigen' == mapBtn.text()
        mapBtn.checkIcon 'map-marker'
        mapBtn.checkColor 'blue'
        mapBtn.checkSize 'medium'
        assert mapBtn.opensNewWindow

        StringBuilder buf = new StringBuilder(street)
        buf << ', ' << postalCode << ' ' << location
        String url = 'http://maps.google.de/maps?hl=&q=' +
            URLEncoder.encode(buf.toString(), 'UTF-8')
        assert url == mapBtn.@href
    }
}
