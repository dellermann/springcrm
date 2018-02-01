<g:applyLayout name="selectorList"
  model="[list: phoneCallList, total: phoneCallCount]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="call-row-selector"/></th>
        <g:sortableColumn property="subject"
          title="${message(code: 'phoneCall.subject.label')}"/>
        <g:ifModuleAllowed modules="CONTACT">
          <g:sortableColumn property="organization.name"
            title="${message(code: 'phoneCall.organization.label')}"
            style="width: 15em;"/>
        </g:ifModuleAllowed>
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
      </tr>
    </thead>
    <tbody>
    <g:each var="phoneCall" in="${phoneCallList}" status="i">
      <tr data-item-id="${phoneCall.id}">
        <td class="col-type-row-selector">
          <input type="checkbox" id="phone-call-row-selector-${phoneCall.id}"
            data-id="${phoneCall.id}"/>
        </td>
        <td class="col-type-string phone-call-subject">
          <a href="#">${fieldValue(bean: phoneCall, field: 'subject')}</a>
        </td>
        <g:ifModuleAllowed modules="CONTACT">
          <td class="col-type-ref phone-call-organization">
            <g:fieldValue bean="${phoneCall}" field="organization"/>
          </td>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="CONTACT">
          <td class="col-type-ref phone-call-person">
            <g:fieldValue bean="${phoneCall}" field="person"/>
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
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
