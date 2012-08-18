<g:if test="${projectInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <th scope="col"><input type="checkbox" id="project-row-selector" /></th>
      <g:sortableColumn scope="col" property="number" title="${message(code: 'project.number.label', default: 'Number')}" />
      <g:sortableColumn scope="col" property="title" title="${message(code: 'project.title.label', default: 'Title')}" />
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
      <td class="id project-number"><g:link controller="project" action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "fullNumber")}</g:link></td>
      <td class="string project-title"><g:link controller="project" action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "title")}</g:link></td>
      <g:ifModuleAllowed modules="contact"><td class="ref project-person"><g:link controller="person" action="show" id="${projectInstance.person?.id}">${fieldValue(bean: projectInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
      <td class="string project-phase"><g:message code="project.phase.${fieldValue(bean: projectInstance, field: "phase")}" default="${projectInstance.phase.toString()}" /></td>
      <td class="string project-status project-status project-status-${projectInstance.status.id}">${fieldValue(bean: projectInstance, field: "status")}</td>
      <td class="action-buttons">
        <g:link controller="project" action="edit" id="${projectInstance.id}" class="button small green"><g:message code="default.button.edit.label" /></g:link>
        <g:link controller="project" action="delete" id="${projectInstance?.id}" class="button small red delete-btn"><g:message code="default.button.delete.label" /></g:link>
      </td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${projectInstanceList}" params="${linkParams}" />
</div>
</g:if>
<g:else>
  <div class="empty-list-inline">
    <p><g:message code="default.list.empty" /></p>
  </div>
</g:else>
