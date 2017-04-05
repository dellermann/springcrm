/*
 * TimeSliceService.groovy
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

import grails.transaction.Transactional
import java.time.LocalDateTime


/**
 * The class {@code TimeSliceService} contains methods to handle time slices.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Transactional
class TimeSliceService {

    //-- Public methods -------------------------

    /**
     * Creates and persists a new time slice for the given staff and finalizes
     * any previous time slice of that staff.
     *
     * @param staff         the given staff
     * @param type          the type of time slice
     * @param description   an optional description of the time slice
     * @return              the new time slice
     */
    TimeSlice create(Staff staff, TimeSliceType type, String description = null)
    {
        TimeSlice prevTimeSlice = finalizePrevious(staff)
        LocalDateTime now =
            prevTimeSlice ? prevTimeSlice.end : LocalDateTime.now()

        new TimeSlice(
                staff: staff, type: type, start: now, description: description
            )
            .save(failOnError: true, flush: true)
    }

    /**
     * Finalizes a previous time slice of the given staff by setting its end
     * time and save it.
     *
     * @param staff the given staff
     * @return      the finalized time slice; {@code null} if no previous open
     *              time slice exists
     */
    TimeSlice finalizePrevious(Staff staff) {
        TimeSlice timeSlice = findOpen(staff)
        if (timeSlice) {
            timeSlice.end = LocalDateTime.now()
            timeSlice.save failOnError: true, flush: true
        }

        timeSlice
    }

    /**
     * Finds a time slice associated to the given staff which has no end time.
     *
     * @param staff the given staff
     * @return      the time slice; {@code null} if no such time slice could be
     *              found
     */
    TimeSlice findOpen(Staff staff) {
        TimeSlice.findByStaffAndEndIsNull staff
    }
}
