<%@ page import="org.amcworld.springcrm.Staff" %>

<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: staffInstanceList, type: 'staff']">
      <div class="visible-xs">
        <g:letterBar clazz="${Staff}" property="lastName" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Staff}" property="lastName" numLetters="3"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Staff}" property="lastName"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="lastName"
                title="${message(code: 'staff.lastName.label')}"/>
              <g:sortableColumn property="firstName"
                title="${message(code: 'staff.firstName.label')}"/>
              <g:sortableColumn property="department.name"
                title="${message(code: 'staff.department.label')}"/>
              <g:sortableColumn property="email1"
                title="${message(code: 'staff.email1.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${staffInstanceList}" status="i" var="staffInstance">
            <tr>
              <td class="col-type-string staff-last-name">
                <g:link action="show" id="${staffInstance.id}">
                  <g:fieldValue bean="${staffInstance}" field="lastName"/>
                </g:link>
              </td>
              <td class="col-type-string staff-first-name">
                <g:link action="show" id="${staffInstance.id}">
                  <g:fieldValue bean="${staffInstance}" field="firstName"/>
                </g:link>
              </td>
              <td class="col-type-ref staff-department">
                <g:link controller="department" action="show"
                  id="${staffInstance.department?.id}">
                  <g:fieldValue bean="${staffInstance}" field="department"/>
                </g:link>
              </td>
              <td class="col-type-string staff-email1">
                <a href="mailto:${fieldValue(bean: staffInstance, field: "email1")}">
                  <g:fieldValue bean="${staffInstance}" field="email1"/>
                </a>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${staffInstance.id}"
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
            <g:paginate total="${staffInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${staffInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
