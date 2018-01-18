<g:applyLayout name="selectorList" model="[list: noteList, total: noteCount]">
  <table class="table data-table">
    <thead>
      <tr>
        <th><input type="checkbox" id="note-row-selector"/></th>
        <g:sortableColumn property="number"
          title="${message(code: 'note.number.label')}"/>
        <g:sortableColumn property="title"
          title="${message(code: 'note.title.label')}"/>
        <g:ifModuleAllowed modules="CONTACT">
          <g:sortableColumn property="organization.name"
            title="${message(code: 'note.organization.label')}"/>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="CONTACT">
          <g:sortableColumn property="person.lastName"
            title="${message(code: 'note.person.label')}"/>
        </g:ifModuleAllowed>
      </tr>
    </thead>
    <tbody>
    <g:each var="note" in="${noteList}" status="i">
      <tr data-item-id="${note.id}">
        <td class="col-type-row-selector">
          <input type="checkbox" id="note-row-selector-${note.id}"
            data-id="${note.id}"/></td>
        <td class="col-type-id note-number">
          <a href="#"><g:fullNumber bean="${note}"/></a>
        </td>
        <td class="col-type-string note-title">
          <a href="#"><g:fieldValue bean="${note}" field="title"/></a>
        </td>
        <g:ifModuleAllowed modules="CONTACT">
          <td class="col-type-ref note-organization">
            <g:fieldValue bean="${note}" field="organization"/>
          </td>
        </g:ifModuleAllowed>
        <g:ifModuleAllowed modules="CONTACT">
          <td class="col-type-ref note-person">
            <g:fieldValue bean="${note}" field="person"/>
          </td>
        </g:ifModuleAllowed>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
