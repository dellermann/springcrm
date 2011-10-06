<g:if test="${personInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="person-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'person.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="lastName" title="${message(code: 'person.lastName.label', default: 'Last name')}" params="${linkParams}" />
      <g:sortableColumn property="firstName" title="${message(code: 'person.firstName.label', default: 'First name')}" params="${linkParams}" />
      <g:sortableColumn property="phone" title="${message(code: 'person.phone.label', default: 'Phone')}" params="${linkParams}" />
      <g:sortableColumn property="email1" title="${message(code: 'person.email1.label', default: 'E-mail')}" params="${linkParams}" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${personInstanceList}" status="i" var="personInstance">
    <tr>
      <td><input type="checkbox" id="person-multop-${personInstance.id}" class="multop-sel-item" /></td>
      <td><g:link controller="person" action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="person" action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "lastName")}</g:link></td>
      <td>${fieldValue(bean: personInstance, field: "firstName")}</td>
      <td>${fieldValue(bean: personInstance, field: "phone")}</td>
      <td><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}">${fieldValue(bean: personInstance, field: "email1")}</a></td>
      <td>
        <g:link controller="person" action="edit" id="${personInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="person" action="delete" id="${personInstance?.id}" class="button small red" onclick="return confirm(springcrm.messages.deleteConfirmMsg);"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${personInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
