<g:applyLayout name="selectorList"
  model="[list: callInstanceList, total: callInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="call-row-selector" /></th>
        <g:sortableColumn property="subject" title="${message(code: 'call.subject.label')}" />
        <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'call.organization.label')}" style="width: 15em;" /></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><g:sortableColumn property="person.lastName" title="${message(code: 'call.person.label')}" /></g:ifModuleAllowed>
        <g:sortableColumn property="start" title="${message(code: 'call.start.label')}" />
        <g:sortableColumn property="type" title="${message(code: 'call.type.label')}" />
        <g:sortableColumn property="status" title="${message(code: 'call.status.label')}" />
      </tr>
    </thead>
    <tbody>
    <g:each in="${callInstanceList}" status="i" var="callInstance">
      <tr data-item-id="${callInstance.id}">
        <td class="col-type-row-selector"><input type="checkbox" id="call-row-selector-${callInstance.id}" data-id="${callInstance.id}" /></td>
        <td class="col-type-string call-subject"><a href="#">${fieldValue(bean: callInstance, field: "subject")}</a></td>
        <g:ifModuleAllowed modules="contact"><td class="col-type-ref call-organization">${fieldValue(bean: callInstance, field: "organization")}</td></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><td class="col-type-ref call-person">${fieldValue(bean: callInstance, field: "person")}</td></g:ifModuleAllowed>
        <td class="col-type-date call-start"><g:formatDate date="${callInstance.start}" /></td>
        <td class="col-type-status call-type"><g:message code="call.type.${callInstance?.type}" /></td>
        <td class="col-type-status call-status"><g:message code="call.status.${callInstance?.status}" /></td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
