<%@ page import="org.amcworld.springcrm.Note" %>
<g:if test="${noteInstanceList}">
<g:letterBar clazz="${Note}" property="title" />
<table class="content-table">
  <thead>
    <tr>
      <th><input type="checkbox" id="note-multop-sel" class="multop-sel" /></th>
      <g:sortableColumn property="number" title="${message(code: 'note.number.label', default: 'Number')}" />
      <g:sortableColumn property="title" title="${message(code: 'note.title.label', default: 'Title')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'note.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><g:sortableColumn property="person.lastName" title="${message(code: 'note.person.label', default: 'Person')}" /></g:ifModuleAllowed>
    </tr>
  </thead>
  <tbody>
  <g:each in="${noteInstanceList}" status="i" var="noteInstance">
    <tr data-item-id="${noteInstance.id}">
      <td><input type="checkbox" id="note-multop-${noteInstance.id}" class="multop-sel-item" /></td>
      <td class="align-center"><a href="#">${fieldValue(bean: noteInstance, field: "fullNumber")}</a></td>
      <td><a href="#">${fieldValue(bean: noteInstance, field: "title")}</a></td>
      <g:ifModuleAllowed modules="contact"><td>${fieldValue(bean: noteInstance, field: "organization")}</td></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><td>${fieldValue(bean: noteInstance, field: "person")}</td></g:ifModuleAllowed>
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
