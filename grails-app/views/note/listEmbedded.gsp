<g:if test="${noteInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-note-row-selector"><input type="checkbox" id="note-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-note-number" property="number" title="${message(code: 'note.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-note-title" property="title" title="${message(code: 'note.title.label', default: 'Title')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-note-person" property="person.lastName" title="${message(code: 'note.person.label', default: 'Person')}" /></g:ifModuleAllowed>
      <th id="content-table-headers-note-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${noteInstanceList}" status="i" var="noteInstance">
    <tr>
      <td class="content-table-row-selector" headers="content-table-headers-note-row-selector"><input type="checkbox" id="note-row-selector-${noteInstance.id}" data-id="${noteInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-note-number" headers="content-table-headers-note-number"><g:link controller="note" action="show" id="${noteInstance.id}">${fieldValue(bean: noteInstance, field: "fullNumber")}</g:link></td>
      <td class="content-table-type-string content-table-column-note-title" headers="content-table-headers-note-title"><g:link controller="note" action="show" id="${noteInstance.id}">${fieldValue(bean: noteInstance, field: "title")}</g:link></td>
      <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-note-person" headers="content-table-headers-note-person"><g:link controller="person" action="show" id="${noteInstance.person?.id}">${fieldValue(bean: noteInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
      <td class="content-table-buttons" headers="content-table-headers-note-buttons">
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
