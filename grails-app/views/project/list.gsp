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
          <th id="content-table-headers-project-row-selector"><input type="checkbox" id="project-row-selector" /></th>
          <g:sortableColumn id="content-table-headers-project-number" property="number" title="${message(code: 'project.number.label', default: 'Number')}" />
          <g:sortableColumn id="content-table-headers-project-title" property="title" title="${message(code: 'project.title.label', default: 'Title')}" />
          <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-project-organization" property="organization.name" title="${message(code: 'project.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><g:sortableColumn id="content-table-headers-project-person" property="person.lastName" title="${message(code: 'project.person.label', default: 'Person')}" /></g:ifModuleAllowed>
          <g:sortableColumn id="content-table-headers-project-phase" property="phase" title="${message(code: 'project.phase.label', default: 'Phase')}" />
          <g:sortableColumn id="content-table-headers-project-status" property="status" title="${message(code: 'project.status.label', default: 'Status')}" />
          <th id="content-table-headers-project-buttons"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${projectInstanceList}" status="i" var="projectInstance">
        <tr>
          <td class="content-table-row-selector" headers="content-table-headers-project-row-selector"><input type="checkbox" id="project-row-selector-${projectInstance.id}" data-id="${projectInstance.id}" /></td>
          <td class="content-table-type-id content-table-column-project-number" headers="content-table-headers-project-number"><g:link action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "fullNumber")}</g:link></td>
          <td class="content-table-type-string content-table-column-project-title" headers="content-table-headers-project-title"><g:link action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "title")}</g:link></td>
          <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-project-organization" headers="content-table-headers-project-organization"><g:link controller="organization" action="show" id="${projectInstance.organization?.id}">${fieldValue(bean: projectInstance, field: "organization")}</g:link></td></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><td class="content-table-type-ref content-table-column-project-person" headers="content-table-headers-project-person"><g:link controller="person" action="show" id="${projectInstance.person?.id}">${fieldValue(bean: projectInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
          <td class="content-table-type-string content-table-column-project-phase" headers="content-table-headers-project-phase"><g:message code="project.phase.${fieldValue(bean: projectInstance, field: "phase")}" default="${projectInstance.phase.toString()}" /></td>
          <td class="content-table-type-status content-table-column-project-status project-status project-status-${projectInstance.status.id}" headers="content-table-headers-project-status">${fieldValue(bean: projectInstance, field: "status")}</td>
          <td class="content-table-buttons" headers="content-table-headers-project-buttons">
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
