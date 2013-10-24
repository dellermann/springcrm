<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<html>
<head>
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
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="create" color="green" icon="plus"
          message="default.new.label" args="[entityName]" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <div class="fc-header" style="margin-bottom: 1em; text-align: right;">
      <span class="fc-button fc-button-agendaDay ui-state-default ui-corner-left"><span class="fc-button-inner"><span class="fc-button-content">day</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-agendaWeek ui-state-default"><span class="fc-button-inner"><span class="fc-button-content">week</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-month ui-state-default"><span class="fc-button-inner"><span class="fc-button-content">month</span><span class="fc-button-effect"><span></span></span></span></span><span class="fc-button fc-button-list ui-state-default ui-corner-right ui-state-active"><span class="fc-button-inner"><span class="fc-button-content"><g:message code="calendarEvent.button.text.list" default="list" /></span><span class="fc-button-effect"><span></span></span></span></span>
    </div>
    <g:if test="${calendarEventInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="calendar-event-row-selector" /></th>
          <g:sortableColumn scope="col" property="subject" title="${message(code: 'calendarEvent.subject.label', default: 'Subject')}" />
          <g:sortableColumn scope="col" property="start" title="${message(code: 'calendarEvent.start.label', default: 'Start')}" />
          <g:sortableColumn scope="col" property="end" title="${message(code: 'calendarEvent.end.label', default: 'End')}" />
          <g:sortableColumn scope="col" property="recurrence.type" title="${message(code: 'calendarEvent.recurrence.label')}" />
          <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="organization.name" title="${message(code: 'calendarEvent.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
          <g:sortableColumn scope="col" property="location" title="${message(code: 'calendarEvent.location.label', default: 'Location')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${calendarEventInstanceList}" status="i" var="calendarEventInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="calendar-event-row-selector-${calendarEventInstance.id}" data-id="${calendarEventInstance.id}" /></td>
          <td class="string calendar-event-subject"><g:link controller="calendarEvent" action="show" id="${calendarEventInstance.id}"><g:fieldValue bean="${calendarEventInstance}" field="subject" /></g:link></td>
          <td class="date calendar-event-start"><g:formatDate date="${calendarEventInstance.start}" /></td>
          <td class="date calendar-event-end"><g:formatDate date="${calendarEventInstance.end}" /></td>
          <td class="string calendar-event-recurrence"><g:message message="${calendarEventInstance.recurrence}" /></td>
          <g:ifModuleAllowed modules="contact"><td class="ref calendar-event-organization"><g:link controller="organization" action="show" id="${calendarEventInstance.organization?.id}"><g:fieldValue bean="${calendarEventInstance}" field="organization" /></g:link></td></g:ifModuleAllowed>
          <td class="string calendar-event-location"><g:fieldValue bean="${calendarEventInstance}" field="location" /></td>
          <td class="action-buttons">
            <g:button action="edit" id="${calendarEventInstance.id}"
              color="green" size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${calendarEventInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
