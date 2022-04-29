<html>
  <head>
    <meta name="layout" content="main"/>
  </head>

  <body>
    <g:applyLayout name="list"
      model="[list: helpdeskInstanceList, type: 'helpdesk']">
      <g:if test="${!mailSystemConfigured}">
      <div class="flash-message form-warning-hint">
        <p><g:message code="ticket.warning.mailNotConfigured.description"/></p>
        <div><g:button controller="config" action="show"
          params="[page: 'mail']" color="warning" icon="cog"
          message="ticket.warning.mailNotConfigured.button"/></div>
      </div>
      </g:if>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="name"
                title="${message(code: 'helpdesk.name.label')}"/>
              <g:sortableColumn property="accessCode"
                title="${message(code: 'helpdesk.accessCode.label')}"/>
              <g:sortableColumn property="organization.name"
                title="${message(code: 'helpdesk.organization.label')}"/>
              <th><g:message code="helpdesk.users.label"/></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${helpdeskInstanceList}" status="i"
              var="helpdeskInstance">
            <tr>
              <td class="col-type-string helpdesk-name">
                <g:link action="show" id="${helpdeskInstance.id}">
                  <g:fieldValue bean="${helpdeskInstance}" field="name"/>
                </g:link>
              </td>
              <td class="col-type-string helpdesk-access-code">
                <g:fieldValue bean="${helpdeskInstance}" field="accessCode"/>
              </td>
              <td class="col-type-ref helpdesk-organization">
                <g:if test="${helpdeskInstance.organization}">
                  <g:link controller="organization" action="show"
                    id="${helpdeskInstance.organization.id}">
                    <g:fieldValue bean="${helpdeskInstance}"
                      field="organization"/>
                  </g:link>
                </g:if>
              </td>
              <td class="col-type-string helpdesk-users">
                ${helpdeskInstance.users*.toString().join(', ')}
              </td>
              <td class="col-actions">
                <g:button mapping="helpdeskFrontend"
                  params="${[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]}"
                  color="default" size="xs" icon="eye" target="_blank"
                  message="helpdesk.button.callFrontend"/>
                <g:button action="edit" id="${helpdeskInstance.id}"
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
          <div class="pagination-container-xs">
            <g:paginate total="${helpdeskInstanceTotal}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="pagination-container-sm">
            <g:paginate total="${helpdeskInstanceTotal}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
