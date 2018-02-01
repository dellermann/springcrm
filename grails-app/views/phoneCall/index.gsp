<%@ page import="org.amcworld.springcrm.PhoneCall" %>

<html>
  <body>
    <g:applyLayout name="list" model="[list: phoneCallList, type: 'phoneCall']">
      <div class="visible-xs">
        <g:letterBar clazz="${PhoneCall}" property="subject" numLetters="5"
          separator="-"/>
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${PhoneCall}" property="subject" numLetters="3"/>
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${PhoneCall}" property="subject"/>
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="subject"
                title="${message(code: 'phoneCall.subject.label')}"/>
              <g:ifModuleAllowed modules="CONTACT">
                <g:sortableColumn property="organization.name"
                  title="${message(code: 'phoneCall.organization.label')}"
                  style="width: 15em;"/>
              </g:ifModuleAllowed>
              <g:ifModuleAllowed modules="CONTACT">
                <g:sortableColumn property="person.lastName"
                  title="${message(code: 'phoneCall.person.label')}"/>
              </g:ifModuleAllowed>
              <g:sortableColumn property="start"
                title="${message(code: 'phoneCall.start.label')}"/>
              <g:sortableColumn property="type"
                title="${message(code: 'phoneCall.type.label')}"/>
              <g:sortableColumn property="status"
                title="${message(code: 'phoneCall.status.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each var="phoneCall" in="${phoneCallList}" status="i">
            <tr>
              <td class="col-type-string call-subject">
                <g:link action="show" id="${phoneCall.id}">
                  <g:fieldValue bean="${phoneCall}" field="subject"/>
                </g:link>
              </td>
              <g:ifModuleAllowed modules="CONTACT">
                <td class="col-type-ref call-organization">
                  <g:link controller="organization" action="show"
                    id="${phoneCall.organization?.id}">
                    <g:fieldValue bean="${phoneCall}" field="organization"/>
                  </g:link>
                </td>
              </g:ifModuleAllowed>
              <g:ifModuleAllowed modules="CONTACT">
                <td class="col-type-ref call-person">
                  <g:link controller="person" action="show"
                    id="${phoneCall.person?.id}">
                    <g:fieldValue bean="${phoneCall}" field="person"/>
                  </g:link>
                </td>
              </g:ifModuleAllowed>
              <td class="col-type-date call-start">
                <g:formatDate date="${phoneCall.start}"/>
              </td>
              <td class="col-type-status call-type">
                <g:message code="phoneCall.type.${phoneCall?.type}"/>
              </td>
              <td class="col-type-status call-status">
                <g:message code="phoneCall.status.${phoneCall?.status}"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${phoneCall.id}"
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
            <g:paginate total="${phoneCallCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${phoneCallCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
