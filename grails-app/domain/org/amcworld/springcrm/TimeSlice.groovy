/*
 * TimeSlice.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import java.time.LocalDateTime


/**
 * The class {@code TimeSlice} represents a time span for a particular work or
 * pause done by a staff.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class TimeSlice {

    //-- Class fields -------------------------------

    static constraints = {
        end nullable: true
        description nullable: true, widget: 'textarea'
        ticket nullable: true
        project nullable: true
    }
    static mapping = {
        sort 'start'
        description type: 'text'
        start index: 'start'
        end index: 'end'
    }


    //-- Fields -------------------------------------

    /**
     * The start of this time slice.
     */
    LocalDateTime start

    /**
     * The end of this time slice.  May be {@code null} if this time slice is
     * currently active.
     */
    LocalDateTime end

    /**
     * The type of this time slice.
     */
    TimeSliceType type = TimeSliceType.WORK

    /**
     * The staff who did the work or pause.
     */
    Staff staff

    /**
     * An optional description of activities in this time slice.
     */
    String description

    /**
     * An associated ticket which has been treated in this time slice.
     */
    Ticket ticket

    /**
     * An associated project which has been treated in this time slice.
     */
    Project project
}


enum TimeSliceType {

    //-- Values ---------------------------------

    WORK,
    PAUSE
}
