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
      <td style="text-align: center;"><g:link controller="project" action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "fullNumber")}</g:link></td>
      <td><g:link controller="project" action="show" id="${projectInstance.id}">${fieldValue(bean: projectInstance, field: "title")}</g:link></td>
      <g:ifModuleAllowed modules="contact"><td><g:link controller="organization" action="show" id="${projectInstance.organization?.id}">${fieldValue(bean: projectInstance, field: "organization")}</g:link></td></g:ifModuleAllowed>
      <g:ifModuleAllowed modules="contact"><td><g:link controller="person" action="show" id="${projectInstance.person?.id}">${fieldValue(bean: projectInstance, field: "person")}</g:link></td></g:ifModuleAllowed>
      <td><g:message code="project.phase.${fieldValue(bean: projectInstance, field: "phase")}" default="${projectInstance.phase.toString()}" /></td>
      <td style="text-align: center;">${fieldValue(bean: projectInstance, field: "status")}</td>
      <td>
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
