<g:applyLayout name="listEmbedded"
  model="[list: documentInstanceList, total: documentInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="name" title="${message(code: 'document.name.label')}" />
        <g:sortableColumn property="size" title="${message(code: 'document.size.label')}" />
        <g:sortableColumn property="lastModified" title="${message(code: 'document.lastModified.label')}" />
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each var="documentInstance" in="${documentInstanceList}">
      <tr>
        <td class="col-type-string document-name">
          <g:link controller="document" action="download"
            params="[path: documentInstance.path]"
            download="${documentInstance.name}"
            >${documentInstance.name}</g:link>
        </td>
        <td class="col-type-number document-size">
          <g:formatSize number="${documentInstance.size}" />
        </td>
        <td class="col-type-date document-last-modified">
          <g:formatDate date="${documentInstance.lastModified}" />
        </td>
        <td class="col-actions">
          <g:button controller="document" action="delete"
            params="[path: documentInstance.path]"
            color="danger" size="xs" class="delete-btn" icon="trash"
            message="default.button.delete.label" />
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
