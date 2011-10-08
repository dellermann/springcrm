<g:if test="${callInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="call-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" params="${linkParams}" />
      <th><g:message code="call.person.label" default="Person" /></th>
      <g:sortableColumn property="start" title="${message(code: 'call.start.label', default: 'Start')}" params="${linkParams}" />
      <g:sortableColumn property="type" title="${message(code: 'call.type.label', default: 'Type')}" params="${linkParams}" />
      <g:sortableColumn property="status" title="${message(code: 'call.status.label', default: 'Status')}" params="${linkParams}" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${callInstanceList}" status="i" var="callInstance">
    <tr>
      <td><input type="checkbox" id="call-multop-${callInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="call" action="show" id="${callInstance.id}">${fieldValue(bean: callInstance, field: "subject")}</g:link></td>
      <td><g:link controller="person" action="show" id="${callInstance.person?.id}">${fieldValue(bean: callInstance, field: "person")}</g:link></td>
      <td><g:formatDate date="${callInstance.start}" formatName="default.format.date" /></td>
      <td><g:message code="call.type.${callInstance?.type}" /></td>
      <td><g:message code="call.status.${callInstance?.status}" /></td>
      <td>
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
