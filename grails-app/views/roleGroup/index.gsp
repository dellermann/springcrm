<%@ page import="org.amcworld.springcrm.RoleGroup" %>

<html>
  <body>
    <g:applyLayout name="list" model="[list: roleGroupList, type: 'roleGroup']">
      <div class="visible-xs">
        <g:letterBar clazz="${RoleGroup}" property="name" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${RoleGroup}" property="name" numLetters="5"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${RoleGroup}" property="name"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="name"
                title="${message(code: 'roleGroup.name.label')}"/>
              <g:sortableColumn property="authorities"
                title="${message(code: 'roleGroup.authorities.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="roleGroup" in="${roleGroupList}">
              <tr>
                <td class="col-type-string role-group-name">
                  <g:link action="show" id="${roleGroup.id}">
                    <g:fieldValue bean="${roleGroup}" field="name"/>
                  </g:link>
                </td>
                <td class="col-type-list role-group-authorities">
                  <g:if test="${roleGroup.administrators}">
                    <g:message code="role.ROLE_ADMIN"/>
                  </g:if>
                  <g:else>
                    <ul>
                      <g:each var="role" in="${roleGroup.authorities}">
                        <li><g:message code="role.${role.authority}"/></li>
                      </g:each>
                    </ul>
                  </g:else>
                </td>
                <td class="col-actions">
                  <g:button action="edit" id="${roleGroup.id}"
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
            <g:paginate total="${roleGroupCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${roleGroupCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
