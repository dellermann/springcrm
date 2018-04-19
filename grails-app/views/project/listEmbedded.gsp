<g:applyLayout name="listEmbedded"
  model="[list: projectInstanceList, total: projectInstanceTotal]">
  <table class="table data-table">
    <thead>
      <tr>
        <g:sortableColumn property="number" title="${message(code: 'project.number.label')}" />
        <g:sortableColumn property="title" title="${message(code: 'project.title.label')}" />
        <g:ifModuleAllowed modules="CONTACT">
        <g:sortableColumn property="person.lastName" title="${message(code: 'project.person.label')}" />
        </g:ifModuleAllowed>
        <g:sortableColumn property="phase" title="${message(code: 'project.phase.label')}" />
        <g:sortableColumn property="status" title="${message(code: 'project.status.label')}" />
        <th></th>
      </tr>
    </thead>
    <tbody>
    <g:each in="${projectInstanceList}" status="i" var="projectInstance">
      <tr>
        <td class="col-type-id project-number"><g:link controller="project" action="show" id="${projectInstance.id}"><g:fullNumber bean="${projectInstance}"/></g:link></td>
        <td class="col-type-string project-title"><g:link controller="project" action="show" id="${projectInstance.id}"><g:fieldValue bean="${projectInstance}" field="title" /></g:link></td>
        <g:ifModuleAllowed modules="CONTACT">
        <td class="col-type-ref project-person"><g:link controller="person" action="show" id="${projectInstance.person?.id}"><g:fieldValue bean="${projectInstance}" field="person" /></g:link></td>
        </g:ifModuleAllowed>
        <td class="col-type-string project-phase"><g:message code="project.phase.${projectInstance.phase}" default="${projectInstance.phase.toString()}" /></td>
        <td class="col-type-string project-status project-status project-status-${projectInstance.status.id}">${fieldValue(bean: projectInstance, field: "status")}</td>
        <td class="col-actions">
          <g:button controller="project" action="edit"
            id="${projectInstance.id}" color="success" size="xs"
            icon="pencil-square-o" message="default.button.edit.label" />
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:applyLayout>
