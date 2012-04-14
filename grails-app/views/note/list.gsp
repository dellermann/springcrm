<%@ page import="org.amcworld.springcrm.Note" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'note.label', default: 'Note')}" />
  <g:set var="entitiesName" value="${message(code: 'note.plural', default: 'Notes')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </nav>
  </div>
  <section id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
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
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${noteInstanceList}" status="i" var="noteInstance">
        <tr>
          <td><input type="checkbox" id="note-multop-${noteInstance.id}" class="multop-sel-item" /></td>
          <td class="align-center"><g:link action="show" id="${noteInstance.id}">${fieldValue(bean: noteInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${noteInstance.id}">${fieldValue(bean: noteInstance, field: "title")}</g:link></td>
          <g:ifModuleAllowed modules="contact"><td><g:link controller="organization" action="show" id="${noteInstance.organization?.id}">${fieldValue(bean: noteInstance, field: "organization")}</g:link></td></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><td><g:link controller="person" action="show" id="${noteInstance.person?.id}">${fieldValue(bean: noteInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
          <td>
            <g:link action="edit" id="${noteInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${noteInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${noteInstanceTotal}" />
    </div>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:link action="create" class="green"><g:message code="default.new.label" args="[entityName]" /></g:link>
        </div>
      </div>
    </g:else>
  </section>
</body>
</html>
