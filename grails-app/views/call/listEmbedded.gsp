<g:if test="${callInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-call-row-selector"><input type="checkbox" id="call-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-call-subject" property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-call-person" property="person.lastName" title="${message(code: 'call.person.label', default: 'Person')}" /></g:ifModuleAllowed>
      <g:sortableColumn id="content-table-headers-call-start" property="start" title="${message(code: 'call.start.label', default: 'Start')}" />
      <g:sortableColumn id="content-table-headers-call-type" property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
      <g:sortableColumn id="content-table-headers-call-status" property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
      <th id="content-table-headers-call-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${callInstanceList}" status="i" var="callInstance">
    <tr>
      <td class="content-table-row-selector" headers="content-table-headers-call-row-selector"><input type="checkbox" id="call-row-selector-${callInstance.id}" data-id="${callInstance.id}" /></td>
      <td class="content-table-type-string content-table-column-call-subject" headers="content-table-headers-call-subject"><g:link controller="call" action="show" id="${callInstance.id}">${fieldValue(bean: callInstance, field: "subject")}</g:link></td>
      <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-call-person" headers="content-table-headers-call-person"><g:link controller="person" action="show" id="${callInstance.person?.id}">${fieldValue(bean: callInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
      <td class="content-table-type-date content-table-column-call-start" headers="content-table-headers-call-subject"><g:formatDate date="${callInstance.start}" /></td>
      <td class="content-table-type-status content-table-column-call-type" headers="content-table-headers-call-subject"><g:message code="call.type.${callInstance?.type}" /></td>
      <td class="content-table-type-status content-table-column-call-status" headers="content-table-headers-call-subject"><g:message code="call.status.${callInstance?.status}" /></td>
      <td class="content-table-buttons" headers="content-table-headers-call-buttons">
        <g:link controller="call" action="edit" id="${callInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="call" action="delete" id="${callInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${callInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
