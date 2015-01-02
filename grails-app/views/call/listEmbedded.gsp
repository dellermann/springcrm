<g:if test="${callInstanceList}">
<div class="table-responsive">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="subject" title="${message(code: 'call.subject.label', default: 'Subject')}" />
        <g:ifModuleAllowed modules="contact">
        <g:sortableColumn property="person.lastName" title="${message(code: 'call.person.label', default: 'Person')}" />
        </g:ifModuleAllowed>
        <g:sortableColumn property="start" title="${message(code: 'call.start.label', default: 'Start')}" />
        <g:sortableColumn property="type" title="${message(code: 'call.type.label', default: 'Type')}" />
        <g:sortableColumn property="status" title="${message(code: 'call.status.label', default: 'Status')}" />
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each in="${callInstanceList}" status="i" var="callInstance">
      <tr>
        <td class="col-type-string call-subject"><g:link controller="call" action="show" id="${callInstance.id}"><g:fieldValue bean="${callInstance}" field="subject" /></g:link></td>
        <g:ifModuleAllowed modules="contact">
        <td class="col-type-ref call-person"><g:link controller="person" action="show" id="${callInstance.person?.id}"><g:fieldValue bean="${callInstance}" field="person" /></g:link></td>
        </g:ifModuleAllowed>
        <td class="col-type-date call-start"><g:formatDate date="${callInstance.start}" /></td>
        <td class="col-type-status call-type"><g:message code="call.type.${callInstance?.type}" /></td>
        <td class="col-type-status call-status"><g:message code="call.status.${callInstance?.status}" /></td>
        <td class="col-actions">
          <g:button controller="call" action="edit" id="${callInstance.id}"
            color="success" size="xs" icon="pencil-square-o"
            message="default.button.edit.label" />
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</div>
<g:render template="/layouts/remoteListPaginate"
  model="[total: callInstanceTotal]" />
</g:if>
<g:else>
  <g:render template="/layouts/remoteListEmpty" />
</g:else>
