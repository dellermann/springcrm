<g:if test="${noteInstanceList}">
<div class="table-responsive">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="number" title="${message(code: 'note.number.label', default: 'Number')}" />
        <g:sortableColumn property="title" title="${message(code: 'note.title.label', default: 'Title')}" />
        <g:ifModuleAllowed modules="contact">
        <g:sortableColumn property="person.lastName" title="${message(code: 'note.person.label', default: 'Person')}" />
        </g:ifModuleAllowed>
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each in="${noteInstanceList}" status="i" var="noteInstance">
      <tr>
        <td class="col-type-id note-number"><g:link controller="note" action="show" id="${noteInstance.id}"><g:fieldValue bean="${noteInstance}" field="fullNumber" /></g:link></td>
        <td class="col-type-string note-title"><g:link controller="note" action="show" id="${noteInstance.id}"><g:fieldValue bean="${noteInstance}" field="title" /></g:link></td>
        <g:ifModuleAllowed modules="contact">
        <td class="col-type-ref note-person"><g:link controller="person" action="show" id="${noteInstance.person?.id}"><g:fieldValue bean="${noteInstance}" field="person" /></g:link></td>
        </g:ifModuleAllowed>
        <td class="col-actions">
          <g:button controller="note" action="edit" id="${noteInstance.id}"
            color="success" size="xs" icon="pencil-square-o"
            message="default.button.edit.label" />
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</div>
<g:render template="/layouts/remoteListPaginate"
  model="[total: noteInstanceTotal]" />
</g:if>
<g:else>
  <g:render template="/layouts/remoteListEmpty" />
</g:else>
