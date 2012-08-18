<g:if test="${personInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="person-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'person.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="lastName" title="${message(code: 'person.lastName.label', default: 'Last name')}" />
      <g:sortableColumn scope="col" property="firstName" title="${message(code: 'person.firstName.label', default: 'First name')}" />
      <g:sortableColumn scope="col" property="phone" title="${message(code: 'person.phone.label', default: 'Phone')}" />
      <g:sortableColumn scope="col" property="email1" title="${message(code: 'person.email1.label', default: 'E-mail')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${personInstanceList}" status="i" var="personInstance">
    <tr>
      <td class="row-selector"><input type="checkbox" id="person-row-selector-${personInstance.id}" data-id="${personInstance.id}" /></td>
      <td class="id person-number"><g:link controller="person" action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "fullNumber")}</g:link></td>
      <td class="string person-last-name"><g:link controller="person" action="show" id="${personInstance.id}">${fieldValue(bean: personInstance, field: "lastName")}</g:link></td>
      <td class="string person-first-name">${fieldValue(bean: personInstance, field: "firstName")}</td>
      <td class="string person-phone"><a href="tel:${fieldValue(bean: personInstance, field: "phone")}">${fieldValue(bean: personInstance, field: "phone")}</a></td>
      <td class="string person-email1"><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}">${fieldValue(bean: personInstance, field: "email1")}</a></td>
      <td class="action-buttons">
        <g:link controller="person" action="edit" id="${personInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="person" action="delete" id="${personInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
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
