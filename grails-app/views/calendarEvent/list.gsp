<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'calendarEvent.label', default: 'CalendarEvent')}" />
  <g:set var="entitiesName" value="${message(code: 'calendarEvent.plural', default: 'CalendarEvents')}" />
  <title>${entitiesName}</title>
  <r:require modules="calendarView" />
  <r:script>
  //<![CDATA[
  (function (window, $L, $) {

      "use strict";

      var location = window.location;

      $(".fc-button-agendaDay").click(function () {
              location.href = "${createLink(controller: 'calendarEvent', action: 'calendar', params: [view: 'agendaDay'])}";
          })
          .find(".fc-button-content")
              .text($L("calendarEvent.button.text").day);
      $(".fc-button-agendaWeek").click(function () {
              location.href = "${createLink(controller: 'calendarEvent', action: 'calendar', params: [view: 'agendaWeek'])}";
          })
          .find(".fc-button-content")
              .text($L("calendarEvent.button.text").week);
      $(".fc-button-month").click(function () {
              location.href = "${createLink(controller: 'calendarEvent', action: 'calendar', params: [view: 'month'])}";
          })
          .find(".fc-button-content")
              .text($L("calendarEvent.button.text").month);
      $(".fc-button").hover(function () {
              $(this).toggleClass("ui-state-hover");
          });
  }(window, $L, jQuery));
  //]]></r:script>
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
    <div class="fc-header" style="margin-bottom: 1em; text-align: right;">
      <span class="fc-button fc-button-agendaDay ui-state-default ui-corner-left"><span class="fc-button-inner"><span class="fc-button-content">day</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-agendaWeek ui-state-default"><span class="fc-button-inner"><span class="fc-button-content">week</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-month ui-state-default"><span class="fc-button-inner"><span class="fc-button-content">month</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-list ui-state-default ui-corner-right ui-state-active"><span class="fc-button-inner"><span class="fc-button-content"><g:message code="calendarEvent.button.text.list" default="list" /></span><span class="fc-button-effect"><span></span></span></span></span>
    </div>
    <g:if test="${calendarEventInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th id="content-table-headers-calendar-event-row-selector"><input type="checkbox" id="calendar-event-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-calendar-event-subject" property="subject" title="${message(code: 'calendarEvent.subject.label', default: 'Subject')}" />
          <g:sortableColumn id="content-table-headers-calendar-event-start" property="start" title="${message(code: 'calendarEvent.start.label', default: 'Start')}" />
          <g:sortableColumn id="content-table-headers-calendar-event-end" property="end" title="${message(code: 'calendarEvent.end.label', default: 'End')}" />
          <g:sortableColumn id="content-table-headers-calendar-event-recurrence" property="recurrence.type" title="${message(code: 'calendarEvent.recurrence.label')}" />
          <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-calendar-event-organization" property="organization.name" title="${message(code: 'calendarEvent.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
          <g:sortableColumn id="content-table-headers-calendar-event-location" property="location" title="${message(code: 'calendarEvent.location.label', default: 'Location')}" />
          <th id="content-table-headers-calendar-event-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${calendarEventInstanceList}" status="i" var="calendarEventInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-calendar-event-row-selector"><input type="checkbox" id="calendar-event-row-selector-${calendarEventInstance.id}" data-id="${calendarEventInstance.id}" /></td>
          <td class="content-table-type-string content-table-column-calendar-event-subject" headers="content-table-headers-calendar-event-subject"><g:link controller="calendarEvent" action="show" id="${calendarEventInstance.id}">${fieldValue(bean: calendarEventInstance, field: "subject")}</g:link></td>
          <td class="content-table-type-date content-table-column-calendar-event-start" headers="content-table-headers-calendar-event-start"><g:formatDate date="${calendarEventInstance.start}" /></td>
          <td class="content-table-type-date content-table-column-calendar-event-end" headers="content-table-headers-calendar-event-end"><g:formatDate date="${calendarEventInstance.end}" /></td>
          <td class="content-table-type-string content-table-column-calendar-event-recurrence" headers="content-table-headers-calendar-event-recurrence"><g:message message="${calendarEventInstance.recurrence}" /></td>
          <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-calendar-event-organization" headers="content-table-headers-calendar-event-organization"><g:link controller="organization" action="show" id="${calendarEventInstance.organization?.id}">${fieldValue(bean: calendarEventInstance, field: "organization")}</g:link></td></g:ifModuleAllowed>
          <td class="content-table-type-string content-table-column-calendar-event-location" headers="content-table-headers-calendar-event-location">${fieldValue(bean: calendarEventInstance, field: "location")}</td>
          <td class="content-table-buttons" headers="content-table-headers-calendar-event-buttons">
            <g:link action="edit" id="${calendarEventInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${calendarEventInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${calendarEventInstanceTotal}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
</body>
</html>
