<html>
  <head>
    <meta name="layout" content="main"/>
    <meta name="stylesheet" content="calendar"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: calendarEventInstanceList, type: 'calendarEvent']">
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="subject"
                title="${message(code: 'calendarEvent.subject.label')}"/>
              <g:sortableColumn property="start"
                title="${message(code: 'calendarEvent.start.label')}"/>
              <g:sortableColumn property="end"
                title="${message(code: 'calendarEvent.end.label')}"/>
              <g:sortableColumn property="recurrence.type"
                title="${message(code: 'calendarEvent.recurrence.label')}"/>
              <g:ifModuleAllowed modules="CONTACT">
                <g:sortableColumn property="organization.name"
                  title="${message(code: 'calendarEvent.organization.label')}"/>
              </g:ifModuleAllowed>
              <g:sortableColumn property="location"
                title="${message(code: 'calendarEvent.location.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${calendarEventInstanceList}" status="i"
            var="calendarEventInstance">
            <tr>
              <td class="col-type-string calendar-event-subject">
                <g:link controller="calendarEvent" action="show"
                  id="${calendarEventInstance.id}">
                  <g:fieldValue bean="${calendarEventInstance}"
                    field="subject"/>
                </g:link>
              </td>
              <td class="col-type-date calendar-event-start">
                <g:formatDate date="${calendarEventInstance.start}"/>
              </td>
              <td class="col-type-date calendar-event-end">
                <g:formatDate date="${calendarEventInstance.end}"/>
              </td>
              <td class="col-type-string calendar-event-recurrence">
                <g:message message="${calendarEventInstance.recurrence}"/>
              </td>
              <g:ifModuleAllowed modules="CONTACT">
                <td class="col-type-ref calendar-event-organization">
                  <g:link controller="organization" action="show"
                    id="${calendarEventInstance.organization?.id}">
                    <g:fieldValue bean="${calendarEventInstance}"
                      field="organization"/>
                  </g:link>
                </td>
              </g:ifModuleAllowed>
              <td class="col-type-string calendar-event-location">
                <g:fieldValue bean="${calendarEventInstance}"
                  field="location"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${calendarEventInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <div class="row">
        <nav class="col-xs-12 col-md-9 pagination-container">
          <div class="pagination-container-xs">
            <g:paginate total="${calendarEventInstanceList}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="pagination-container-sm">
            <g:paginate total="${calendarEventInstanceList}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>


<%--
<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
  <div id="content">
    <div class="fc">
      <div class="fc-toolbar">
        <div class="fc-right">
          <div class="fc-button-group">
            <button type="button"
              class="fc-agendaDay-button ui-button ui-state-default ui-corner-left"
              >day</button
            ><button type="button"
              class="fc-agendaWeek-button ui-button ui-state-default"
              >week</button
            ><button type="button"
              class="fc-month-button ui-button ui-state-default">month</button
            ><button type="button"
              class="fc-list-button ui-button ui-state-default ui-corner-right ui-state-active"
              ><g:message code="calendarEvent.button.text.list" default="list"
              /></button
            >
          </div>
        </div>
        <div class="fc-clear"></div>
      </div>
    </div>
  </div>
  <content tag="scripts">
    <asset:javascript src="calendar"/>
    <asset:javascript src="lang/fullcalendar/${(org.springframework.web.servlet.support.RequestContextUtils.getLocale(org.grails.web.util.WebUtils.retrieveGrailsWebRequest().currentRequest) ?: Locale.default).language}"/>
    <asset:script>//<![CDATA[
      (function (window, $L, $) {

          "use strict";

          var lang = $.fullCalendar.langs["${lang}"].defaultButtonText,
              location = window.location;

          $(".fc-agendaDay-button").click(function () {
                  location.href = "${createLink(controller: 'calendarEvent', action: 'calendar', params: [view: 'agendaDay'])}";
              })
              .text(lang.day);
          $(".fc-agendaWeek-button").click(function () {
                  location.href = "${createLink(controller: 'calendarEvent', action: 'calendar', params: [view: 'agendaWeek'])}";
              })
              .text(lang.week);
          $(".fc-month-button").click(function () {
                  location.href = "${createLink(controller: 'calendarEvent', action: 'calendar', params: [view: 'month'])}";
              })
              .text(lang.month);
          $(".fc-button").hover(function () {
                  $(this).toggleClass("ui-state-hover");
              });
      }(window, $L, jQuery));
    //]]></asset:script>
  </content>
--%>
