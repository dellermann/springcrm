<g:applyLayout name="listEmbedded"
  model="[list: personInstanceList, total: personInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="number" title="${message(code: 'person.number.label')}" />
        <g:sortableColumn property="lastName" title="${message(code: 'person.lastName.label')}" />
        <g:sortableColumn property="firstName" title="${message(code: 'person.firstName.label')}" />
        <g:sortableColumn property="phone" title="${message(code: 'person.phone.label')}" />
        <g:sortableColumn property="email1" title="${message(code: 'person.email1.label')}" />
        <th></th>
      </tr>
    </thead>
    <tbody>
      <g:each in="${personInstanceList}" status="i" var="personInstance">
      <tr>
        <td class="col-type-id person-number"><g:link controller="person" action="show" id="${personInstance.id}"><g:fieldValue bean="${personInstance}" field="fullNumber" /></g:link></td>
        <td class="col-type-string person-last-name"><g:link controller="person" action="show" id="${personInstance.id}"><g:fieldValue bean="${personInstance}" field="lastName" /></g:link></td>
        <td class="col-type-string person-first-name"><g:fieldValue bean="${personInstance}" field="firstName" /></td>
        <td class="col-type-string person-phone"><a href="tel:${fieldValue(bean: personInstance, field: "phone")}"><g:fieldValue bean="${personInstance}" field="phone" /></a></td>
        <td class="col-type-string person-email1"><a href="mailto:${fieldValue(bean: personInstance, field: "email1")}"><g:fieldValue bean="${personInstance}" field="email1" /></a></td>
        <td class="col-actions">
          <g:button controller="person" action="edit" id="${personInstance.id}"
            color="success" size="xs" icon="pencil-square-o"
            message="default.button.edit.label" />
        </td>
      </tr>
      </g:each>
    </tbody>
  </table>
</g:applyLayout>
