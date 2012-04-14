<%@ page import="org.amcworld.springcrm.Project" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
  <g:set var="entitiesName" value="${message(code: 'project.plural', default: 'Projects')}" />
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
    <g:if test="${projectInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th><input type="checkbox" id="project-multop-sel" class="multop-sel" /></th>
          <g:sortableColumn property="number" title="${message(code: 'project.number.label', default: 'Number')}" />
          <g:sortableColumn property="title" title="${message(code: 'project.title.label', default: 'Title')}" />
          <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'project.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><g:sortableColumn property="person.lastName" title="${message(code: 'project.person.label', default: 'Person')}" /></g:ifModuleAllowed>
          <g:sortableColumn property="phase" title="${message(code: 'project.phase.label', default: 'Phase')}" />
          <g:sortableColumn property="status" title="${message(code: 'project.status.label', default: 'Status')}" />
          <th></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${projectInstanceList}" status="i" var="projectInstance">
        <tr>
          <td><input type="checkbox" id="project-multop-${projectInstance.id}" class="multop-sel-item" /></td>
          <td class="align-center"><g:link action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "fullNumber")}</g:link></td>
          <td><g:link action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "title")}</g:link></td>
          <g:ifModuleAllowed modules="contact"><td><g:link controller="organization" action="show" id="${projectInstance.organization?.id}">${fieldValue(bean: projectInstance, field: "organization")}</g:link></td></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><td><g:link controller="person" action="show" id="${projectInstance.person?.id}">${fieldValue(bean: projectInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
          <td><g:message code="project.phase.${fieldValue(bean: projectInstance, field: "phase")}" default="${projectInstance.phase.toString()}" /></td>
          <td class="align-center">${fieldValue(bean: projectInstance, field: "status")}</td>
          <td>
            <g:link action="edit" id="${projectInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
            <g:link action="delete" id="${projectInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
          </td>
        </tr>
      </g:each>
      </tbody>
    </table>
    <div class="paginator">
      <g:paginate total="${projectInstanceTotal}" />
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
