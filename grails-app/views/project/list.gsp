<%@ page import="org.amcworld.springcrm.Project" %>
<html>
<head>
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'project.label', default: 'Project')}" />
  <g:set var="entitiesName" value="${message(code: 'project.plural', default: 'Projects')}" />
  <title>${entitiesName}</title>
  <r:require modules="project" />
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
    <div class="flash-message message" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:if test="${projectInstanceList}">
    <table class="content-table">
      <thead>
        <tr>
          <th scope="col"><input type="checkbox" id="project-row-selector" /></th>
          <g:sortableColumn scope="col" property="number" title="${message(code: 'project.number.label', default: 'Number')}" />
          <g:sortableColumn scope="col" property="title" title="${message(code: 'project.title.label', default: 'Title')}" />
          <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="organization.name" title="${message(code: 'project.organization.label', default: 'Organization')}" /></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><g:sortableColumn scope="col" property="person.lastName" title="${message(code: 'project.person.label', default: 'Person')}" /></g:ifModuleAllowed>
          <g:sortableColumn scope="col" property="phase" title="${message(code: 'project.phase.label', default: 'Phase')}" />
          <g:sortableColumn scope="col" property="status" title="${message(code: 'project.status.label', default: 'Status')}" />
          <th scope="col"></th>
        </tr>
      </thead>
      <tbody>
      <g:each in="${projectInstanceList}" status="i" var="projectInstance">
        <tr>
          <td class="row-selector"><input type="checkbox" id="project-row-selector-${projectInstance.id}" data-id="${projectInstance.id}" /></td>
          <td class="id project-number"><g:link action="show" id="${projectInstance.id}"><g:fieldValue bean="${projectInstance}" field="fullNumber" /></g:link></td>
          <td class="string project-title"><g:link action="show" id="${projectInstance.id}"><g:fieldValue bean="${projectInstance}" field="title" /></g:link></td>
          <g:ifModuleAllowed modules="contact"><td class="ref project-organization"><g:link controller="organization" action="show" id="${projectInstance.organization?.id}"><g:fieldValue bean="${projectInstance}" field="organization" /></g:link></td></g:ifModuleAllowed>
          <g:ifModuleAllowed modules="contact"><td class="ref project-person"><g:link controller="person" action="show" id="${projectInstance.person?.id}"><g:fieldValue bean="${projectInstance}" field="person" /></g:link></td></g:ifModuleAllowed>
          <td class="string project-phase"><g:message code="project.phase.${projectInstance.phase}" default="${projectInstance.phase.toString()}" /></td>
          <td class="status project-status project-status project-status-${projectInstance.status.id}">${fieldValue(bean: projectInstance, field: "status")}</td>
          <td class="action-buttons">
            <g:button action="edit" id="${projectInstance.id}" color="green"
              size="small" message="default.button.edit.label" />
            <g:button action="delete" id="${projectInstance?.id}"
              color="red" size="small" class="delete-btn"
              message="default.button.delete.label" />
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
          <g:button action="create" color="green" icon="plus"
            message="default.new.label" args="[entityName]" />
        </div>
      </div>
    </g:else>
  </div>
</body>
</html>
