<g:if test="${callInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="call-row-selector" /></th>
      <g:sortableColumn scope="col" property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="person.lastName" title="${message(code: 'call.person.label', default: 'Person')}" /></g:ifModuleAllowed>
      <g:sortableColumn scope="col" property="start" title="${message(code: 'call.start.label', default: 'Start')}" />
      <g:sortableColumn scope="col" property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
      <g:sortableColumn scope="col" property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${callInstanceList}" status="i" var="callInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="call-row-selector-${callInstance.id}" data-id="${callInstance.id}" /></td>
      <td class="string call-subject"><g:link controller="call" action="show" id="${callInstance.id}">${fieldValue(bean: callInstance, field: "subject")}</g:link></td>
      <g:ifModuleAllowed modules="contact"><td class="ref call-person"><g:link controller="person" action="show" id="${callInstance.person?.id}">${fieldValue(bean: callInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
      <td class="date call-start"><g:formatDate date="${callInstance.start}" /></td>
      <td class="status call-type"><g:message code="call.type.${callInstance?.type}" /></td>
      <td class="status call-status"><g:message code="call.status.${callInstance?.status}" /></td>
      <td class="action-buttons">
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
