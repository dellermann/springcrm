<%@ page import="org.amcworld.springcrm.Project" %>

<html>
  <head>
    <meta name="stylesheet" content="project"/>
  </head>

  <body>
    <g:applyLayout name="list" model="[list: projectList, type: 'project']">
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number"
                title="${message(code: 'project.number.label')}"/>
              <g:sortableColumn property="title"
                title="${message(code: 'project.title.label')}"/>
              <g:ifModuleAllowed modules="CONTACT">
                <g:sortableColumn property="organization.name"
                  title="${message(code: 'project.organization.label')}"/>
              </g:ifModuleAllowed>
              <g:ifModuleAllowed modules="CONTACT">
                <g:sortableColumn property="person.lastName"
                  title="${message(code: 'project.person.label')}"/>
              </g:ifModuleAllowed>
              <g:sortableColumn property="phase"
                title="${message(code: 'project.phase.label')}"/>
              <g:sortableColumn property="status"
                title="${message(code: 'project.status.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="project" in="${projectList}" status="i">
            <tr>
              <td class="col-type-id project-number">
                <g:link action="show" id="${project.id}">
                  <g:fullNumber bean="${project}"/>
                </g:link>
              </td>
              <td class="col-type-string project-title">
                <g:link action="show" id="${project.id}">
                  <g:fieldValue bean="${project}" field="title"/>
                </g:link>
              </td>
              <g:ifModuleAllowed modules="CONTACT">
                <td class="col-type-ref project-organization">
                  <g:link controller="organization" action="show"
                    id="${project.organization?.id}">
                    <g:fieldValue bean="${project}"
                      field="organization"/>
                  </g:link>
                </td>
              </g:ifModuleAllowed>
              <g:ifModuleAllowed modules="CONTACT">
                <td class="col-type-ref project-person">
                  <g:link controller="person" action="show"
                    id="${project.person?.id}">
                    <g:fieldValue bean="${project}" field="person"/>
                  </g:link>
                </td>
              </g:ifModuleAllowed>
              <td class="col-type-string project-phase">
                <g:message code="project.phase.${project.phase}"
                  default="${project.phase.toString()}"/>
              </td>
              <td class="col-type-status project-status project-status
                  project-status-${project.status.id}">
                <g:fieldValue bean="${project}" field="status"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${project.id}" color="success"
                  size="xs" icon="pencil-square-o"
                  message="default.button.edit.label"/>
              </td>
            </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="row">
        <nav class="col-xs-12 col-md-9 pagination-container">
          <div class="visible-xs">
            <g:paginate total="${projectCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${projectCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
