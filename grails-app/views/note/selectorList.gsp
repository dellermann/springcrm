<g:applyLayout name="selectorList"
  model="[list: noteInstanceList, total: noteInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="note-row-selector" /></th>
        <g:sortableColumn property="number" title="${message(code: 'note.number.label')}" />
        <g:sortableColumn property="title" title="${message(code: 'note.title.label')}" />
        <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'note.organization.label')}" /></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><g:sortableColumn property="person.lastName" title="${message(code: 'note.person.label')}" /></g:ifModuleAllowed>
      </tr>
    </thead>
    <tbody>
    <g:each in="${noteInstanceList}" status="i" var="noteInstance">
      <tr data-item-id="${noteInstance.id}">
        <td class="col-type-row-selector"><input type="checkbox" id="note-row-selector-${noteInstance.id}" data-id="${noteInstance.id}" /></td>
        <td class="col-type-id note-number"><a href="#"><g:fieldValue bean="${noteInstance}" field="fullNumber" /></a></td>
        <td class="col-type-string note-title"><a href="#"><g:fieldValue bean="${noteInstance}" field="title" /></a></td>
        <g:ifModuleAllowed modules="contact"><td class="col-type-ref note-organization"><g:fieldValue bean="${noteInstance}" field="organization" /></td></g:ifModuleAllowed>
        <g:ifModuleAllowed modules="contact"><td class="col-type-ref note-person"><g:fieldValue bean="${noteInstance}" field="person" /></td></g:ifModuleAllowed>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
