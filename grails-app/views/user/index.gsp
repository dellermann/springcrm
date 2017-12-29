<%@ page import="org.amcworld.springcrm.User" %>

<html>
  <body>
    <g:applyLayout name="list" model="[list: userList, type: 'user']">
      <div class="visible-xs">
        <g:letterBar clazz="${User}" property="username" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${User}" property="username" numLetters="5"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${User}" property="username"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="username"
                title="${message(code: 'user.username.label')}"/>
              <g:sortableColumn property="lastName"
                title="${message(code: 'user.lastName.label')}"/>
              <g:sortableColumn property="firstName"
                title="${message(code: 'user.firstName.label')}"/>
              <g:sortableColumn property="phone"
                title="${message(code: 'user.phone.label')}"/>
              <g:sortableColumn property="mobile"
                title="${message(code: 'user.mobile.label')}"/>
              <g:sortableColumn property="email"
                title="${message(code: 'user.email.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each var="user" in="${userList}">
            <tr>
              <td class="col-type-string user-user-name">
                <g:link action="show" id="${user.id}">
                  <g:fieldValue bean="${user}" field="username"/>
                </g:link>
              </td>
              <td class="col-type-string user-last-name">
                <g:fieldValue bean="${user}" field="lastName"/>
              </td>
              <td class="col-type-string user-first-name">
                <g:fieldValue bean="${user}" field="firstName"/>
              </td>
              <td class="col-type-string user-phone">
                <a href="tel:${fieldValue(bean: user, field: "phone")}">
                  <g:fieldValue bean="${user}" field="phone"/>
                </a>
              </td>
              <td class="col-type-string user-mobile">
                <a href="tel:${fieldValue(bean: user, field: "mobile")}">
                  <g:fieldValue bean="${user}" field="mobile"/>
                </a>
              </td>
              <td class="col-type-string user-email">
                <a href="mailto:${fieldValue(bean: user, field: "email")}">
                  <g:fieldValue bean="${user}" field="email"/>
                </a>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${user.id}"
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
            <g:paginate total="${userCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${userCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>

