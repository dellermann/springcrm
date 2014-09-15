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
  <g:each var="documentInstance" in="${documentInstanceList}">
    <tr>
      <td class="string document-name">
        <g:link controller="document" action="download"
          params="[path: documentInstance.path]" target="_blank"
          >${documentInstance.name}</g:link>
      </td>
      <td class="number document-size">
        <g:formatSize number="${documentInstance.size}" />
      </td>
      <td class="date document-last-modified">
        <g:formatDate date="${documentInstance.lastModified}" />
      </td>
      <td class="action-buttons">
        <g:link controller="document" action="delete"
          params="[path: documentInstance.path]"
          class="button small red delete-btn"
          ><g:message code="default.button.delete.label"
        /></g:link>
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
