<%@ page import="org.amcworld.springcrm.Note" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'note.label', default: 'Note')}" />
  <g:set var="entitiesName" value="${message(code: 'note.plural', default: 'Notes')}" />
  <title>${entitiesName}</title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:button action="create" color="green" icon="plus"
          message="default.new.label" args="[entityName]" /></li>
      </ul>
    </nav>
  </header>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
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
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${noteInstanceList}" status="i" var="noteInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="note-row-selector-${noteInstance.id}" data-id="${noteInstance.id}" /></td>
          <td class="id note-number"><g:link action="show" id="${noteInstance.id}"><g:fieldValue bean="${noteInstance}" field="fullNumber" /></g:link></td>
          <td class="string note-title"><g:link action="show" id="${noteInstance.id}"><g:fieldValue bean="${noteInstance}" field="title" /></g:link></td>
          <g:ifModuleAllowed modules="contact"><td class="ref note-organization"><g:link controller="organization" action="show" id="${noteInstance.organization?.id}"><g:fieldValue bean="${noteInstance}" field="organization" /></g:link></td></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><td class="ref note-person"><g:link controller="person" action="show" id="${noteInstance.person?.id}"><g:fieldValue bean="${noteInstance}" field="person" /></g:link></td></g:ifModuleAllowed>
          <td class="action-buttons">
            <g:button action="edit" id="${noteInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${noteInstance?.id}" color="red"
              size="small" class="delete-btn"
              message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
