<g:applyLayout name="selectorList"
  model="[list: calendarEventInstanceList, total: calendarEventInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="calendar-event-row-selector" /></th>
        <g:sortableColumn property="subject" title="${message(code: 'calendarEvent.subject.label')}" />
        <g:sortableColumn property="start" title="${message(code: 'calendarEvent.start.label')}" />
        <g:sortableColumn property="end" title="${message(code: 'calendarEvent.end.label')}" />
        <g:sortableColumn property="recurrence.type" title="${message(code: 'calendarEvent.recurrence.label')}" />
        <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'calendarEvent.organization.label')}" /></g:ifModuleAllowed>
      </tr>
    </thead>
    <tbody>
    <g:each in="${calendarEventInstanceList}" status="i" var="calendarEventInstance">
      <tr data-item-id="${calendarEventInstance.id}">
        <td class="col-type-row-selector"><input type="checkbox" id="calendar-event-row-selector-${calendarEventInstance.id}" data-id="${calendarEventInstance.id}" /></td>
        <td class="col-type-string calendar-event-subject"><a href="#">${fieldValue(bean: calendarEventInstance, field: "subject")}</a></td>
        <td class="col-type-date calendar-event-start"><g:formatDate date="${calendarEventInstance.start}" /></td>
        <td class="col-type-date calendar-event-end"><g:formatDate date="${calendarEventInstance.end}" /></td>
        <td class="col-type-string calendar-event-recurrence"><g:message message="${calendarEventInstance.recurrence}" /></td>
        <g:ifModuleAllowed modules="contact"><td class="col-type-ref calendar-event-organization">${fieldValue(bean: calendarEventInstance, field: "organization")}</td></g:ifModuleAllowed>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
