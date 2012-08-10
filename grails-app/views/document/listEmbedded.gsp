<g:if test="${documentInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <g:sortableColumn id="content-table-headers-document-name" property="name" title="${message(code: 'document.name.label', default: 'Name')}" />
      <g:sortableColumn id="content-table-headers-document-size" property="size" title="${message(code: 'document.size.label', default: 'Size')}" />
      <g:sortableColumn id="content-table-headers-document-last-modified" property="lastModified" title="${message(code: 'document.lastModified.label', default: 'Last modified')}" />
      <th id="content-table-headers-document-buttons"></th>
    </tr>
  </thead>
  <tbody>
  <g:each in="${documentInstanceList}" status="i" var="documentInstance">
    <tr>
      <td class="content-table-type-string content-table-column-document-name" headers="content-table-headers-document-name"><g:link controller="document" action="download" id="${documentInstance.id}" target="_blank">${fieldValue(bean: documentInstance, field: "name")}</g:link></td>
      <td class="content-table-type-number content-table-column-document-size" headers="content-table-headers-document-size">${fieldValue(bean: documentInstance, field: "sizeAsString")}</td>
      <td class="content-table-type-date content-table-column-document-last-modified" headers="content-table-headers-document-last-modified">${fieldValue(bean: documentInstance, field: "lastModified")}</td>
      <td class="content-table-buttons" headers="content-table-headers-document-buttons">
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
