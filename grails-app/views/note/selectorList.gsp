<%@ page import="org.amcworld.springcrm.Note" %>
<g:if test="${noteInstanceList}">
<g:letterBar clazz="${Note}" property="title" />
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="note-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'note.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="title" title="${message(code: 'note.title.label', default: 'Title')}" />
      <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="organization.name" title="${message(code: 'note.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="person.lastName" title="${message(code: 'note.person.label', default: 'Person')}" /></g:ifModuleAllowed>
    </tr>
  </thead>
  <tbody>
  <g:each in="${noteInstanceList}" status="i" var="noteInstance">
    <tr data-item-id="${noteInstance.id}">
      <td class="row-selector"><input type="checkbox" id="note-row-selector-${noteInstance.id}" data-id="${noteInstance.id}" /></td>
      <td class="id note-number"><a href="#"><g:fieldValue bean="${noteInstance}" field="fullNumber" /></a></td>
      <td class="string note-title"><a href="#"><g:fieldValue bean="${noteInstance}" field="title" /></a></td>
      <g:ifModuleAllowed modules="contact"><td class="ref note-organization"><g:fieldValue bean="${noteInstance}" field="organization" /></td></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><td class="ref note-person"><g:fieldValue bean="${noteInstance}" field="person" /></td></g:ifModuleAllowed>
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
