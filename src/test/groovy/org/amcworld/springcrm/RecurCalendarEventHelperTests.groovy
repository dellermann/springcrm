package org.amcworld.springcrm;

import static java.util.Calendar.*
import static org.amcworld.springcrm.RecurCalendarEventHelper.*
import static org.junit.Assert.*

import org.junit.Test

class RecurCalendarEventHelperTests {

    @Test
    void testLin() {
        Locale.default = Locale.GERMANY
        assertEquals(7 + SUNDAY, lin(SUNDAY))
        assertEquals(MONDAY, lin(MONDAY))
        assertEquals(SATURDAY, lin(SATURDAY))

        Locale.default = Locale.US
        assertEquals(SUNDAY, lin(SUNDAY))
        assertEquals(MONDAY, lin(MONDAY))
        assertEquals(SATURDAY, lin(SATURDAY))
    }

    @Test
    void testDelin() {
        Locale.default = Locale.GERMANY
        assertEquals(SUNDAY, delin(lin(SUNDAY)))
        assertEquals(MONDAY, delin(lin(MONDAY)))
        assertEquals(SATURDAY, delin(lin(SATURDAY)))
        assertEquals(7 + SUNDAY, lin(delin(7 + SUNDAY)))
        assertEquals(MONDAY, lin(delin(MONDAY)))
        assertEquals(SATURDAY, lin(delin(SATURDAY)))

        Locale.default = Locale.US
        assertEquals(SUNDAY, delin(lin(SUNDAY)))
        assertEquals(MONDAY, delin(lin(MONDAY)))
        assertEquals(SATURDAY, delin(lin(SATURDAY)))
        assertEquals(SUNDAY, lin(delin(SUNDAY)))
        assertEquals(MONDAY, lin(delin(MONDAY)))
        assertEquals(SATURDAY, lin(delin(SATURDAY)))
    }

    @Test
    void testCalibrateDate30() {
        def helper = new RecurCalendarEventHelper(      // We, Fr, Su
            new RecurrenceData(type:30, weekdays:'1,4,6', interval:2)
        )

        Locale.default = Locale.GERMANY
        def d = helper.calibrateStart(getDate(2011, DECEMBER, 7, 10))   // We
        assertEquals(getDate(2011, DECEMBER, 7), d)                     // We
        d = helper.calibrateStart(getDate(2011, DECEMBER, 9, 10))       // Fr
        assertEquals(getDate(2011, DECEMBER, 9), d)                     // Fr
        d = helper.calibrateStart(getDate(2011, DECEMBER, 11, 10))      // Su
        assertEquals(getDate(2011, DECEMBER, 11), d)                    // Su
        d = helper.calibrateStart(getDate(2011, DECEMBER, 6, 10))       // Tu
        assertEquals(getDate(2011, DECEMBER, 7), d)                     // -> We
        d = helper.calibrateStart(getDate(2011, DECEMBER, 8, 10))       // Th
        assertEquals(getDate(2011, DECEMBER, 9), d)                     // -> Fr
        d = helper.calibrateStart(getDate(2011, DECEMBER, 10, 10))      // Sa
        assertEquals(getDate(2011, DECEMBER, 11), d)                    // -> Su

        Locale.default = Locale.US
        d = helper.calibrateStart(getDate(2011, DECEMBER, 7, 10))       // We
        assertEquals(getDate(2011, DECEMBER, 7), d)                     // We
        d = helper.calibrateStart(getDate(2011, DECEMBER, 9, 10))       // Fr
        assertEquals(getDate(2011, DECEMBER, 9), d)                     // Fr
        d = helper.calibrateStart(getDate(2011, DECEMBER, 11, 10))      // Su
        assertEquals(getDate(2011, DECEMBER, 11), d)                    // Su
        d = helper.calibrateStart(getDate(2011, DECEMBER, 6, 10))       // Tu
        assertEquals(getDate(2011, DECEMBER, 7), d)                     // -> We
        d = helper.calibrateStart(getDate(2011, DECEMBER, 8, 10))       // Th
        assertEquals(getDate(2011, DECEMBER, 9), d)                     // -> Fr
        d = helper.calibrateStart(getDate(2011, DECEMBER, 10, 10))      // Sa
        assertEquals(getDate(2011, DECEMBER, 18), d)                    // -> Su

        helper = new RecurCalendarEventHelper(            // We, Fr
            new RecurrenceData(type:30, weekdays:'4,6', interval:2)
        )

        Locale.default = Locale.GERMANY
        d = helper.calibrateStart(getDate(2011, DECEMBER, 10, 10))      // Sa
        assertEquals(getDate(2011, DECEMBER, 21), d)                    // -> We
    }

    @Test
    void testCalibrateDate40() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:40, monthDay:14, interval:2)
        )
        def d = helper.calibrateStart(getDate(2011, DECEMBER, 14, 10))
        assertEquals(getDate(2011, DECEMBER, 14), d)
        d = helper.calibrateStart(getDate(2011, DECEMBER, 1, 10))
        assertEquals(getDate(2011, DECEMBER, 14), d)
        d = helper.calibrateStart(getDate(2011, DECEMBER, 10, 10))
        assertEquals(getDate(2011, DECEMBER, 14), d)
        d = helper.calibrateStart(getDate(2011, DECEMBER, 15, 10))
        assertEquals(getDate(2012, FEBRUARY, 14), d)
    }

    @Test
    void testCalibrateDate50() {
        def helper = new RecurCalendarEventHelper(      // Th
            new RecurrenceData(type:50, weekdays:'5', weekdayOrd:3, interval:2)
        )
        def d = helper.calibrateStart(getDate(2011, NOVEMBER, 17, 10))  // Th
        assertEquals(getDate(2011, NOVEMBER, 17), d)                    // Th
        d = helper.calibrateStart(getDate(2011, NOVEMBER, 16, 10))      // We
        assertEquals(getDate(2011, NOVEMBER, 17), d)                    // -> Th
        d = helper.calibrateStart(getDate(2011, NOVEMBER, 1, 10))       // Tu
        assertEquals(getDate(2011, NOVEMBER, 17), d)                    // -> Th
        d = helper.calibrateStart(getDate(2011, NOVEMBER, 18, 10))      // Fr
        assertEquals(getDate(2012, JANUARY, 19), d)                     // -> Th

        helper = new RecurCalendarEventHelper(          // Th
            new RecurrenceData(type:50, weekdays:'5', weekdayOrd:-1, interval:2)
        )
        d = helper.calibrateStart(getDate(2011, NOVEMBER, 24, 10))      // Th
        assertEquals(getDate(2011, NOVEMBER, 24), d)                    // Th
        d = helper.calibrateStart(getDate(2011, NOVEMBER, 23, 10))      // We
        assertEquals(getDate(2011, NOVEMBER, 24), d)                    // -> Th
        d = helper.calibrateStart(getDate(2011, NOVEMBER, 1, 10))       // Tu
        assertEquals(getDate(2011, NOVEMBER, 24), d)                    // -> Th
        d = helper.calibrateStart(getDate(2011, NOVEMBER, 25, 10))      // Fr
        assertEquals(getDate(2012, JANUARY, 26), d)                     // -> Th
    }

    @Test
    void testCalibrateDate60() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:60, monthDay:13, month:MAY)
        )
        def d = helper.calibrateStart(getDate(2011, MAY, 13, 10))
        assertEquals(getDate(2011, MAY, 13), d)
        d = helper.calibrateStart(getDate(2011, MAY, 12, 10))
        assertEquals(getDate(2011, MAY, 13), d)
        d = helper.calibrateStart(getDate(2011, JANUARY, 1, 10))
        assertEquals(getDate(2011, MAY, 13), d)
        d = helper.calibrateStart(getDate(2011, MAY, 14, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.calibrateStart(getDate(2011, DECEMBER, 31, 10))
        assertEquals(getDate(2012, MAY, 13), d)
    }

    @Test
    void testCalibrateDate70() {
        def helper = new RecurCalendarEventHelper(      // Su
            new RecurrenceData(type:70, weekdays:'1', weekdayOrd:2, month:MAY)
        )
        def d = helper.calibrateStart(getDate(2011, MAY, 8, 10))        // Su
        assertEquals(getDate(2011, MAY, 8), d)                          // Su
        d = helper.calibrateStart(getDate(2011, MAY, 7, 10))            // Sa
        assertEquals(getDate(2011, MAY, 8), d)                          // -> Su
        d = helper.calibrateStart(getDate(2011, MARCH, 10, 10))         // Th
        assertEquals(getDate(2011, MAY, 8), d)                          // -> Su
        d = helper.calibrateStart(getDate(2011, JANUARY, 1, 10))        // Sa
        assertEquals(getDate(2011, MAY, 8), d)                          // -> Su
        d = helper.calibrateStart(getDate(2011, MAY, 9, 10))            // Mo
        assertEquals(getDate(2012, MAY, 13), d)                         // -> Su
        d = helper.calibrateStart(getDate(2011, DECEMBER, 31, 10))      // Sa
        assertEquals(getDate(2012, MAY, 13), d)                         // -> Su

        helper = new RecurCalendarEventHelper(          // Su
            new RecurrenceData(type:70, weekdays:'1', weekdayOrd:-1, month:MAY)
        )
        d = helper.calibrateStart(getDate(2011, MAY, 29, 10))           // Su
        assertEquals(getDate(2011, MAY, 29), d)                         // Su
        d = helper.calibrateStart(getDate(2011, MAY, 28, 10))           // Sa
        assertEquals(getDate(2011, MAY, 29), d)                         // -> Su
        d = helper.calibrateStart(getDate(2011, JANUARY, 1, 10))        // Sa
        assertEquals(getDate(2011, MAY, 29), d)                         // -> Su
        d = helper.calibrateStart(getDate(2011, MAY, 30, 10))           // Mo
        assertEquals(getDate(2012, MAY, 27), d)                         // -> Su
        d = helper.calibrateStart(getDate(2011, DECEMBER, 31, 10))      // Sa
        assertEquals(getDate(2012, MAY, 27), d)                         // -> Su
    }

    @Test
    void testComputeNthEvent10() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:10, interval:5)
        )
        def start = getDate(2011, DECEMBER, 5)
        def d = helper.computeNthEvent(start, 1)
        assertEquals(getDate(2011, DECEMBER, 5), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2011, DECEMBER, 10), d)
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2011, DECEMBER, 15), d)
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2011, DECEMBER, 20), d)
        d = helper.computeNthEvent(start, 5)
        assertEquals(getDate(2011, DECEMBER, 25), d)
        d = helper.computeNthEvent(start, 6)
        assertEquals(getDate(2011, DECEMBER, 30), d)
        d = helper.computeNthEvent(start, 7)
        assertEquals(getDate(2012, JANUARY, 4), d)
    }

    @Test
    void testComputeNthEvent30() {
        def helper = new RecurCalendarEventHelper(      // We, Fr, Su
            new RecurrenceData(type:30, weekdays:'1,4,6', interval:2)
        )
        def start = getDate(2011, DECEMBER, 7)
        def d = helper.computeNthEvent(start, 1)                        // We
        assertEquals(getDate(2011, DECEMBER, 7), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2011, DECEMBER, 9), d)                     // Fr
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2011, DECEMBER, 11), d)                    // Su
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2011, DECEMBER, 21), d)                    // We
        d = helper.computeNthEvent(start, 5)
        assertEquals(getDate(2011, DECEMBER, 23), d)                    // Fr
        d = helper.computeNthEvent(start, 6)
        assertEquals(getDate(2011, DECEMBER, 25), d)                    // Su
        d = helper.computeNthEvent(start, 7)
        assertEquals(getDate(2012, JANUARY, 4), d)                      // We
        d = helper.computeNthEvent(start, 8)
        assertEquals(getDate(2012, JANUARY, 6), d)                      // Fr
        d = helper.computeNthEvent(start, 9)
        assertEquals(getDate(2012, JANUARY, 8), d)                      // Su
        d = helper.computeNthEvent(start, 10)
        assertEquals(getDate(2012, JANUARY, 18), d)                     // We
    }

    @Test
    void testComputeNthEvent40() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:40, monthDay:14, interval:2)
        )
        def start = getDate(2011, NOVEMBER, 14)
        def d = helper.computeNthEvent(start, 1)
        assertEquals(getDate(2011, NOVEMBER, 14), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2012, JANUARY, 14), d)
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2012, MARCH, 14), d)
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2012, MAY, 14), d)
        d = helper.computeNthEvent(start, 7)
        assertEquals(getDate(2012, NOVEMBER, 14), d)
    }

    @Test
    void testComputeNthEvent50() {
        def helper = new RecurCalendarEventHelper(      // Th
            new RecurrenceData(type:50, weekdays:'5', weekdayOrd:3, interval:2)
        )
        def start = getDate(2011, NOVEMBER, 17)
        def d = helper.computeNthEvent(start, 1)
        assertEquals(getDate(2011, NOVEMBER, 17), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2012, JANUARY, 19), d)
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2012, MARCH, 15), d)
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2012, MAY, 17), d)
        d = helper.computeNthEvent(start, 7)
        assertEquals(getDate(2012, NOVEMBER, 15), d)

        helper = new RecurCalendarEventHelper(          // Th
            new RecurrenceData(type:50, weekdays:'5', weekdayOrd:-1, interval:2)
        )
        start = getDate(2011, NOVEMBER, 24)
        d = helper.computeNthEvent(start, 1)
        assertEquals(getDate(2011, NOVEMBER, 24), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2012, JANUARY, 26), d)
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2012, MARCH, 29), d)
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2012, MAY, 31), d)
        d = helper.computeNthEvent(start, 7)
        assertEquals(getDate(2012, NOVEMBER, 29), d)
    }

    @Test
    void testComputeNthEvent60() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:60, monthDay:13, month:MAY)
        )
        def start = getDate(2011, MAY, 13)
        def d = helper.computeNthEvent(start, 1)
        assertEquals(getDate(2011, MAY, 13), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2013, MAY, 13), d)
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2014, MAY, 13), d)
        d = helper.computeNthEvent(start, 10)
        assertEquals(getDate(2020, MAY, 13), d)
    }

    @Test
    void testComputeNthEvent70() {
        def helper = new RecurCalendarEventHelper(      // Su
            new RecurrenceData(type:70, weekdays:'1', weekdayOrd:2, month:MAY)
        )
        def start = getDate(2011, MAY, 8)
        def d = helper.computeNthEvent(start, 1)
        assertEquals(getDate(2011, MAY, 8), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2013, MAY, 12), d)
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2014, MAY, 11), d)
        d = helper.computeNthEvent(start, 10)
        assertEquals(getDate(2020, MAY, 10), d)

        helper = new RecurCalendarEventHelper(          // Su
            new RecurrenceData(type:70, weekdays:'1', weekdayOrd:-1, month:MAY)
        )
        start = getDate(2011, MAY, 29)
        d = helper.computeNthEvent(start, 1)
        assertEquals(getDate(2011, MAY, 29), d)
        d = helper.computeNthEvent(start, 2)
        assertEquals(getDate(2012, MAY, 27), d)
        d = helper.computeNthEvent(start, 3)
        assertEquals(getDate(2013, MAY, 26), d)
        d = helper.computeNthEvent(start, 4)
        assertEquals(getDate(2014, MAY, 25), d)
        d = helper.computeNthEvent(start, 10)
        assertEquals(getDate(2020, MAY, 31), d)
    }

    @Test
    void testApproximate10() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:10, interval:5)
        )
        def start = getDate(2011, DECEMBER, 5)
        def d = helper.approximate(start, getDate(2011, DECEMBER, 1, 10))
        assertEquals(getDate(2011, DECEMBER, 5), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 5, 10))
        assertEquals(getDate(2011, DECEMBER, 5), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 6, 10))
        assertEquals(getDate(2011, DECEMBER, 10), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 9, 10))
        assertEquals(getDate(2011, DECEMBER, 10), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 10, 10))
        assertEquals(getDate(2011, DECEMBER, 10), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 11, 10))
        assertEquals(getDate(2011, DECEMBER, 15), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 14, 10))
        assertEquals(getDate(2011, DECEMBER, 15), d)
    }

    @Test
    void testApproximate30() {
        def helper = new RecurCalendarEventHelper(      // We, Fr, Su
            new RecurrenceData(type:30, weekdays:'1,4,6', interval:2)
        )
        def start = getDate(2011, DECEMBER, 7)                          // We
        def d = helper.approximate(start, getDate(2011, DECEMBER, 1, 10))  // Th
        assertEquals(getDate(2011, DECEMBER, 7), d)                     // -> We
        d = helper.approximate(start, getDate(2011, DECEMBER, 7, 10))   // We
        assertEquals(getDate(2011, DECEMBER, 7), d)                     // We
        d = helper.approximate(start, getDate(2011, DECEMBER, 8, 10))   // Th
        assertEquals(getDate(2011, DECEMBER, 9), d)                     // -> Fr
        d = helper.approximate(start, getDate(2011, DECEMBER, 9, 10))   // Fr
        assertEquals(getDate(2011, DECEMBER, 9), d)                     // Fr
        d = helper.approximate(start, getDate(2011, DECEMBER, 10, 10))  // Sa
        assertEquals(getDate(2011, DECEMBER, 11), d)                    // -> Su
        d = helper.approximate(start, getDate(2011, DECEMBER, 11, 10))  // Su
        assertEquals(getDate(2011, DECEMBER, 11), d)                    // Su
        d = helper.approximate(start, getDate(2011, DECEMBER, 12, 10))  // Mo
        assertEquals(getDate(2011, DECEMBER, 21), d)                    // -> We
        d = helper.approximate(start, getDate(2011, DECEMBER, 19, 10))  // Mo
        assertEquals(getDate(2011, DECEMBER, 21), d)                    // -> We
        d = helper.approximate(start, getDate(2011, DECEMBER, 21, 10))  // We
        assertEquals(getDate(2011, DECEMBER, 21), d)                    // We
        d = helper.approximate(start, getDate(2011, DECEMBER, 22, 10))  // Th
        assertEquals(getDate(2011, DECEMBER, 23), d)                    // -> Fr
        d = helper.approximate(start, getDate(2011, DECEMBER, 23))      // Fr
        assertEquals(getDate(2011, DECEMBER, 23), d)                    // Fr
        d = helper.approximate(start, getDate(2011, DECEMBER, 24))      // Sa
        assertEquals(getDate(2011, DECEMBER, 25), d)                    // -> Su
        d = helper.approximate(start, getDate(2011, DECEMBER, 25))      // Su
        assertEquals(getDate(2011, DECEMBER, 25), d)                    // Su
        d = helper.approximate(start, getDate(2011, DECEMBER, 26))      // Mo
        assertEquals(getDate(2012, JANUARY, 4), d)                      // -> We

        helper = new RecurCalendarEventHelper(          // We
            new RecurrenceData(type:30, weekdays:'4', interval:2)
        )
        start = getDate(2011, NOVEMBER, 9)                              // We
        d = helper.approximate(start, getDate(2011, NOVEMBER, 1, 10))   // Tu
        assertEquals(getDate(2011, NOVEMBER, 9), d)                     // -> We
        d = helper.approximate(start, getDate(2011, NOVEMBER, 9, 10))   // We
        assertEquals(getDate(2011, NOVEMBER, 9), d)                     // We
        d = helper.approximate(start, getDate(2011, NOVEMBER, 10, 10))  // Th
        assertEquals(getDate(2011, NOVEMBER, 23), d)                    // -> We
        d = helper.approximate(start, getDate(2011, NOVEMBER, 22, 10))  // Tu
        assertEquals(getDate(2011, NOVEMBER, 23), d)                    // -> We
        d = helper.approximate(start, getDate(2011, NOVEMBER, 23, 10))  // We
        assertEquals(getDate(2011, NOVEMBER, 23), d)                    // We
        d = helper.approximate(start, getDate(2011, NOVEMBER, 24, 10))  // Th
        assertEquals(getDate(2011, DECEMBER, 7), d)                     // -> We
    }

    @Test
    void testApproximate40() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:40, monthDay:14, interval:2)
        )
        def start = getDate(2011, NOVEMBER, 14)
        def d = helper.approximate(start, getDate(2011, NOVEMBER, 1, 10))
        assertEquals(getDate(2011, NOVEMBER, 14), d)
        d = helper.approximate(start, getDate(2011, NOVEMBER, 14, 10))
        assertEquals(getDate(2011, NOVEMBER, 14), d)
        d = helper.approximate(start, getDate(2011, NOVEMBER, 15, 10))
        assertEquals(getDate(2012, JANUARY, 14), d)
        d = helper.approximate(start, getDate(2011, NOVEMBER, 30, 10))
        assertEquals(getDate(2012, JANUARY, 14), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 14, 10))
        assertEquals(getDate(2012, JANUARY, 14), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 1, 10))
        assertEquals(getDate(2012, JANUARY, 14), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 13, 10))
        assertEquals(getDate(2012, JANUARY, 14), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 14, 10))
        assertEquals(getDate(2012, JANUARY, 14), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 15, 10))
        assertEquals(getDate(2012, MARCH, 14), d)
    }

    @Test
    void testApproximate50() {
        def helper = new RecurCalendarEventHelper(      // Th
            new RecurrenceData(type:50, weekdays:'5', weekdayOrd:3, interval:2)
        )
        def start = getDate(2011, NOVEMBER, 17)
        def d = helper.approximate(start, getDate(2011, NOVEMBER, 1, 10))
        assertEquals(getDate(2011, NOVEMBER, 17), d)
        d = helper.approximate(start, getDate(2011, NOVEMBER, 17, 10))
        assertEquals(getDate(2011, NOVEMBER, 17), d)
        d = helper.approximate(start, getDate(2011, NOVEMBER, 18, 10))
        assertEquals(getDate(2012, JANUARY, 19), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 15, 10))
        assertEquals(getDate(2012, JANUARY, 19), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 17, 10))
        assertEquals(getDate(2012, JANUARY, 19), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 1, 10))
        assertEquals(getDate(2012, JANUARY, 19), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 18, 10))
        assertEquals(getDate(2012, JANUARY, 19), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 19, 10))
        assertEquals(getDate(2012, JANUARY, 19), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 20, 10))
        assertEquals(getDate(2012, MARCH, 15), d)
    }

    @Test
    void testApproximate60() {
        def helper = new RecurCalendarEventHelper(
            new RecurrenceData(type:60, monthDay:13, month:MAY)
        )
        def start = getDate(2011, MAY, 13)
        def d = helper.approximate(start, getDate(2011, MAY, 1, 10))
        assertEquals(getDate(2011, MAY, 13), d)
        d = helper.approximate(start, getDate(2011, MAY, 13, 10))
        assertEquals(getDate(2011, MAY, 13), d)
        d = helper.approximate(start, getDate(2011, MAY, 14, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 31, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 1, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, MAY, 12, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, MAY, 13, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, MAY, 14, 10))
        assertEquals(getDate(2013, MAY, 13), d)
        d = helper.approximate(start, getDate(2019, MAY, 14, 10))
        assertEquals(getDate(2020, MAY, 13), d)
    }

    @Test
    void testApproximate70() {
        def helper = new RecurCalendarEventHelper(      // Su
            new RecurrenceData(type:70, weekdays:'1', weekdayOrd:2, month:MAY)
        )
        def start = getDate(2011, MAY, 8)
        def d = helper.approximate(start, getDate(2011, MAY, 1, 10))
        assertEquals(getDate(2011, MAY, 8), d)
        d = helper.approximate(start, getDate(2011, MAY, 8, 10))
        assertEquals(getDate(2011, MAY, 8), d)
        d = helper.approximate(start, getDate(2011, MAY, 9, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2011, DECEMBER, 31, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, JANUARY, 1, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, MAY, 12, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, MAY, 13, 10))
        assertEquals(getDate(2012, MAY, 13), d)
        d = helper.approximate(start, getDate(2012, MAY, 14, 10))
        assertEquals(getDate(2013, MAY, 12), d)
        d = helper.approximate(start, getDate(2019, MAY, 13, 10))
        assertEquals(getDate(2020, MAY, 10), d)
    }


    //-- Non-public methods ---------------------

    private Date getDate(int year, int month, int day, int hour = 0) {
        return new GregorianCalendar(year, month, day, hour, 0).time
    }
}
