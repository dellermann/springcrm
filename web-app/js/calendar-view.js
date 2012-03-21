/*
 * calendar-view.js
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


(function (window, $L, $) {

    "use strict";

    var $calendar = $("#calendar"),
        jQuery = $,
        onClickDay,
        onClickListViewBtn,
        onHoverListViewBtn;

    onClickDay = function (date, allDay) {
        var $ = jQuery,
            fc = $.fullCalendar,
            params;

        params = "start=" + encodeURIComponent(
                fc.formatDate(date, $L("default.format.date")) + " " +
                fc.formatDate(date, $L("default.format.time"))
            ) +
            "&allDay=" + encodeURIComponent(String(allDay));
        window.location.href = $calendar.attr("data-create-event-url") + "?" +
            params;
    };

    onClickListViewBtn = function () {
        window.location.href = $calendar.attr("data-list-view-url");
    };

    onHoverListViewBtn = function () {
        $(this).toggleClass("ui-state-hover");
    };

    $calendar.fullCalendar({
            allDayText: $L("calendarEvent.allDay.label"),
            axisFormat: $L("calendarEvent.axis.format"),
            buttonText: $L("calendarEvent.button.text"),
            columnFormat: $L("calendarEvent.column.format"),
            dayClick: onClickDay,
            dayNames: $L("weekdaysLong"),
            dayNamesShort: $L("weekdaysShort"),
            defaultView: $calendar.attr("data-current-view"),
            eventSources: [{ url: $calendar.attr("data-load-events-url") }],
            firstDay: $L("calendarFirstDay"),
            isRTL: $L("calendarRTL"),
            header: {
                center: "title",
                left: "prev,next today",
                right: "agendaDay,agendaWeek,month"
            },
            monthNames: $L("monthNamesLong"),
            monthNamesShort: $L("monthNamesShort"),
            theme: true,
            timeFormat: $L("calendarEvent.time.format"),
            titleFormat: $L("calendarEvent.title.format")
        });
    $(".fc-header-right").append(
            '<span class="fc-button fc-button-list ui-state-default ' +
            'ui-corner-right">' +
            '<span class="fc-button-inner">' +
            '<span class="fc-button-content">Liste</span>' +
            '<span class="fc-button-effect"><span></span></span></span>'
        );
    $(".fc-button-month").removeClass("ui-corner-right");
    $(".fc-button-list").click(onClickListViewBtn)
        .hover(onHoverListViewBtn);

}(window, $L, jQuery));
