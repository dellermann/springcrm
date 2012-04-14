<g:if test="${noteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="note-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'note.number.label', default: 'Number')}" params="${linkParams}" />
      <g:sortableColumn property="title" title="${message(code: 'note.title.label', default: 'Title')}" params="${linkParams}" />
      <g:sortableColumn property="person.lastName" title="${message(code: 'note.person.label', default: 'Person')}" params="${linkParams}" />
      <th></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${noteInstanceList}" status="i" var="noteInstance">
    <tr>
      <td><input type="checkbox" id="note-multop-${noteInstance.id}" class="multop-sel-item" /></td>
      <td class="align-center"><g:link controller="note" action="show" id="${noteInstance.id}">${fieldValue(bean: noteInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="note" action="show" id="${noteInstance.id}">${fieldValue(bean: noteInstance, field: "title")}</g:link></td>
      <td><g:link controller="person" action="show" id="${noteInstance.person?.id}">${fieldValue(bean: noteInstance, field: "person")}</g:link></td>
      <td>
        <g:link controller="note" action="edit" id="${noteInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="note" action="delete" id="${noteInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${noteInstanceTotal}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
