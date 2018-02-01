<g:applyLayout name="listEmbedded"
  model="[list: phoneCallList, total: phoneCallCount]">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="subject"
          title="${message(code: 'phoneCall.subject.label')}"/>
        <g:ifModuleAllowed modules="CONTACT">
        <g:sortableColumn property="person.lastName"
          title="${message(code: 'phoneCall.person.label')}"/>
        </g:ifModuleAllowed>
        <g:sortableColumn property="start"
          title="${message(code: 'phoneCall.start.label')}"/>
        <g:sortableColumn property="type"
          title="${message(code: 'phoneCall.type.label')}"/>
        <g:sortableColumn property="status"
          title="${message(code: 'phoneCall.status.label')}"/>
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each var="phoneCall" in="${phoneCallList}" status="i">
      <tr>
        <td class="col-type-string phone-call-subject">
          <g:link controller="phoneCall" action="show" id="${phoneCall.id}">
            <g:fieldValue bean="${phoneCall}" field="subject"/>
          </g:link>
        </td>
        <g:ifModuleAllowed modules="CONTACT">
        <td class="col-type-ref phone-call-person">
          <g:link controller="person" action="show"
            id="${phoneCall.person?.id}">
            <g:fieldValue bean="${phoneCall}" field="person"/>
          </g:link>
        </td>
        </g:ifModuleAllowed>
        <td class="col-type-date phone-call-start">
          <g:formatDate date="${phoneCall.start}"/>
        </td>
        <td class="col-type-status phone-call-type">
          <g:message code="phoneCall.type.${phoneCall?.type}"/>
        </td>
        <td class="col-type-status phone-call-status">
          <g:message code="phoneCall.status.${phoneCall?.status}"/>
        </td>
        <td class="col-actions">
          <g:button controller="phoneCall" action="edit" id="${phoneCall.id}"
            color="success" size="xs" icon="pencil-square-o"
            message="default.button.edit.label"/>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
