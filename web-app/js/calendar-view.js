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
