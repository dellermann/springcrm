<g:if test="${documentInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <g:sortableColumn scope="col" property="name" title="${message(code: 'document.name.label', default: 'Name')}" />
      <g:sortableColumn scope="col" property="size" title="${message(code: 'document.size.label', default: 'Size')}" />
      <g:sortableColumn scope="col" property="lastModified" title="${message(code: 'document.lastModified.label', default: 'Last modified')}" />
      <th scope="col"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${documentInstanceList}" status="i" var="documentInstance">
    <tr>
      <td class="content-table-type-string content-table-column-document-name"><g:link controller="document" action="download" id="${documentInstance.id}" target="_blank">${fieldValue(bean: documentInstance, field: "name")}</g:link></td>
      <td class="content-table-type-number content-table-column-document-size">${fieldValue(bean: documentInstance, field: "sizeAsString")}</td>
      <td class="content-table-type-date content-table-column-document-last-modified">${fieldValue(bean: documentInstance, field: "lastModified")}</td>
      <td class="content-table-buttons">
        <g:link controller="document" action="delete" id="${documentInstance.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${documentInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
