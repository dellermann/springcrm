<%@ page import="org.amcworld.springcrm.Note" %>
<g:if test="${noteInstanceList}">
<g:letterBar clazz="${Note}" property="title" />
<table class="content-table">
  <thead>
    <tr>
      <th id="content-table-headers-note-row-selector"><input type="checkbox" id="note-row-selector" /></th>
      <g:sortableColumn id="content-table-headers-note-number" property="number" title="${message(code: 'note.number.label', default: 'Number')}" />
      <g:sortableColumn id="content-table-headers-note-title" property="title" title="${message(code: 'note.title.label', default: 'Title')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-note-organization" property="organization.name" title="${message(code: 'note.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-note-person" property="person.lastName" title="${message(code: 'note.person.label', default: 'Person')}" /></g:ifModuleAllowed>
    </tr>
  </thead>
  <tbody>
  <g:each in="${noteInstanceList}" status="i" var="noteInstance">
    <tr data-item-id="${noteInstance.id}">
      <td class="content-table-row-selector" headers="content-table-headers-note-row-selector"><input type="checkbox" id="note-row-selector-${noteInstance.id}" data-id="${noteInstance.id}" /></td>
      <td class="content-table-type-id content-table-column-note-number" headers="content-table-headers-note-number"><a href="#">${fieldValue(bean: noteInstance, field: "fullNumber")}</a></td>
      <td class="content-table-type-string content-table-column-note-title" headers="content-table-headers-note-title"><a href="#">${fieldValue(bean: noteInstance, field: "title")}</a></td>
      <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-note-organization" headers="content-table-headers-note-organization">${fieldValue(bean: noteInstance, field: "organization")}</td></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-note-person" headers="content-table-headers-note-person">${fieldValue(bean: noteInstance, field: "person")}</td></g:ifModuleAllowed>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${noteInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>
