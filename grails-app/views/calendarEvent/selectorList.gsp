<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
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
    </tr>
  </thead>
  <tbody>
  <g:each in="${calendarEventInstanceList}" status="i" var="calendarEventInstance">
    <tr data-item-id="${calendarEventInstance.id}">
      <td class="row-selector"><input type="checkbox" id="calendar-event-row-selector-${calendarEventInstance.id}" data-id="${calendarEventInstance.id}" /></td>
      <td class="string calendar-event-subject"><a href="#">${fieldValue(bean: calendarEventInstance, field: "subject")}</a></td>
      <td class="date calendar-event-start"><g:formatDate date="${calendarEventInstance.start}" /></td>
      <td class="date calendar-event-end"><g:formatDate date="${calendarEventInstance.end}" /></td>
      <td class="string calendar-event-recurrence"><g:message message="${calendarEventInstance.recurrence}" /></td>
      <g:ifModuleAllowed modules="contact"><td class="ref calendar-event-organization">${fieldValue(bean: calendarEventInstance, field: "organization")}</td></g:ifModuleAllowed>
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
