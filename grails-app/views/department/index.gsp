<%@ page import="org.amcworld.springcrm.Department" %>

<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: departmentInstanceList, type: 'department']">
      <div class="visible-xs">
        <g:letterBar clazz="${Department}" property="name" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Department}" property="name" numLetters="3"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Department}" property="name"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="name"
                title="${message(code: 'department.name.label')}"/>
              <g:sortableColumn property="costCenter"
                title="${message(code: 'department.costCenter.label')}"/>
              <g:sortableColumn property="manager.fullName"
                title="${message(code: 'department.manager.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${departmentInstanceList}" status="i"
            var="departmentInstance">
            <tr>
              <td class="col-type-string department-name">
                <g:link action="show" id="${departmentInstance.id}">
                  <g:fieldValue bean="${departmentInstance}" field="name"/>
                </g:link>
              </td>
              <td class="col-type-string department-cost-center">
                <g:fieldValue bean="${departmentInstance}" field="costCenter"/>
              </td>
              <td class="col-type-ref department-manager">
                <g:link controller="staff" action="show"
                  id="${departmentInstance.manager?.id}">
                  <g:fieldValue bean="${departmentInstance}" field="manager"/>
                </g:link>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${departmentInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
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
            <g:paginate total="${departmentInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${departmentInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
