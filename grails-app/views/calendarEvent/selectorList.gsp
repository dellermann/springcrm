<%@ page import="org.amcworld.springcrm.CalendarEvent" %>
<g:if test="${calendarEventInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="calendarEvent-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="subject" title="${message(code: 'calendarEvent.subject.label', default: 'Subject')}" />
      <g:sortableColumn property="start" title="${message(code: 'calendarEvent.start.label', default: 'Start')}" />
      <g:sortableColumn property="end" title="${message(code: 'calendarEvent.end.label', default: 'End')}" />
      <g:sortableColumn property="recurrence.type" title="${message(code: 'calendarEvent.recurrence.label')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'calendarEvent.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
    </tr>
  </thead>
  <tbody>
  <g:each in="${calendarEventInstanceList}" status="i" var="calendarEventInstance">
    <tr data-item-id="${calendarEventInstance.id}">
      <td><input type="checkbox" id="calendarEvent-multop-${calendarEventInstance.id}" class="multop-sel-item" /></td>
      <td><a href="#">${fieldValue(bean: calendarEventInstance, field: "subject")}</a></td>
      <td class="align-center"><g:formatDate date="${calendarEventInstance.start}" /></td>
      <td class="align-center"><g:formatDate date="${calendarEventInstance.end}" /></td>
      <td><g:message message="${calendarEventInstance.recurrence}" /></td>
      <g:ifModuleAllowed modules="contact"><td>${fieldValue(bean: calendarEventInstance, field: "organization")}</td></g:ifModuleAllowed>
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
