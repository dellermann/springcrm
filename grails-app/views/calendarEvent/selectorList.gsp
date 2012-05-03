<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
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
    </tr>
  </thead>
  <tbody>
  <g:each in="${calendarEventInstanceList}" status="i" var="calendarEventInstance">
    <tr data-item-id="${calendarEventInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-calendar-event-row-selector"><input type="checkbox" id="calendar-event-row-selector-${calendarEventInstance.id}" data-id="${calendarEventInstance.id}" /></td>
      <td class="content-table-type-string content-table-column-calendar-event-subject" headers="content-table-headers-calendar-event-subject"><a href="#">${fieldValue(bean: calendarEventInstance, field: "subject")}</a></td>
      <td class="content-table-type-date content-table-column-calendar-event-start" headers="content-table-headers-calendar-event-start"><g:formatDate date="${calendarEventInstance.start}" /></td>
      <td class="content-table-type-date content-table-column-calendar-event-end" headers="content-table-headers-calendar-event-end"><g:formatDate date="${calendarEventInstance.end}" /></td>
      <td class="content-table-type-string content-table-column-calendar-event-recurrence" headers="content-table-headers-calendar-event-recurrence"><g:message message="${calendarEventInstance.recurrence}" /></td>
      <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-calendar-event-organization" headers="content-table-headers-calendar-event-organization">${fieldValue(bean: calendarEventInstance, field: "organization")}</td></g:ifModuleAllowed>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${calendarEventInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
