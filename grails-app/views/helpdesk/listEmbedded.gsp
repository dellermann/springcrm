<g:applyLayout name="listEmbedded"
  model="[list: helpdeskInstanceList, total: helpdeskInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="name" title="${message(code: 'helpdesk.name.label')}" />
        <g:sortableColumn property="accessCode" title="${message(code: 'helpdesk.accessCode.label')}" />
        <th><g:message code="helpdesk.users.label" /></th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <g:each in="${helpdeskInstanceList}" status="i"
        var="helpdeskInstance">
      <tr>
        <td class="col-type-string helpdesk-name"><g:link controller="helpdesk" action="show" id="${helpdeskInstance.id}">${fieldValue(bean: helpdeskInstance, field: "name")}</g:link></td>
        <td class="col-type-string helpdesk-access-code"><g:fieldValue bean="${helpdeskInstance}" field="accessCode" /></td>
        <td class="col-type-string helpdesk-users">${helpdeskInstance.users*.toString().join(', ')}</td>
        <td class="col-actions">
          <g:button controller="helpdesk" action="edit"
            id="${helpdeskInstance.id}" color="success" size="xs"
            icon="pencil-square-o" message="default.button.edit.label" />
        </td>
      </tr>
      </g:each>
    </tbody>
  </table>
</g:applyLayout>
