<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title>${entitiesName}</title>
  <link rel="stylesheet" href="${resource(dir:'css', file:'fullcalendar.css')}" />
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <div id="calendar"></div>
  </section>
  <content tag="additionalJavaScript">
  <script type="text/javascript" src="${resource(dir:'js', file:'fullcalendar.min.js')}"></script>
  <script type="text/javascript">
  //<![CDATA[
  (function (window, SPRINGCRM, $) {

      "use strict";

      var g = SPRINGCRM.getMessage;

      $("#calendar").fullCalendar({
              allDayText: g("calendarEvent_allDay_label"),
              axisFormat: g("calendarEvent_axis_format"),
              buttonText: g("calendarEvent_button_text"),
              columnFormat: g("calendarEvent_column_format"),
              dayClick: function (date, allDay) {
                  var params;

                  params = "start=" + encodeURIComponent(
                          $.fullCalendar.formatDate(date, g("default_format_date")) + 
                          " " +
                          $.fullCalendar.formatDate(date, g("default_format_time"))
                      ) + "&allDay=" + encodeURIComponent(String(allDay));
                  window.location.href = "${createLink(controller:'calendarEvent', action:'create')}?" + 
                      params;
              },
              dayNames: g("weekdaysLong"),
              dayNamesShort: g("weekdaysShort"),
              defaultView: "${params.view ?: 'month'}",
              eventSources: [
                  {
                      url: "${createLink(controller:'calendarEvent', action:'listRange')}"
                  }
              ],
              firstDay: g("calendarFirstDay"),
              isRTL: g("calendarRTL"),
              header: {
                  center: "title",
                  left: "prev,next today",
                  right: "agendaDay,agendaWeek,month"
              },
              monthNames: g("monthNamesLong"),
              monthNamesShort: g("monthNamesShort"),
              theme: true,
              timeFormat: g("calendarEvent_time_format"),
              titleFormat: g("calendarEvent_title_format")
          });
      $(".fc-header-right").append(
              '<span class="fc-button fc-button-list ui-state-default ' +
              'ui-corner-right">' +
              '<span class="fc-button-inner">' +
              '<span class="fc-button-content">Liste</span>' +
              '<span class="fc-button-effect"><span></span></span></span>'
          );
      $(".fc-button-month").removeClass("ui-corner-right");
      $(".fc-button-list").click(function () {
              window.location.href = "${createLink(controller:'calendarEvent', action:'list')}";
          })
          .hover(function () {
              $(this).toggleClass("ui-state-hover");
          });
  }(window, SPRINGCRM, jQuery));
  //]]></script>
  </content>
</body>
</html>
