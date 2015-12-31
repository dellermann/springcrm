<%@ page import="org.amcworld.springcrm.Call" %>

<html>
  <head>
    <meta name="layout" content="main" />
  </head>

  <body>
    <g:applyLayout name="list" model="[list: callInstanceList, type: 'call']">
      <div class="visible-xs">
        <g:letterBar clazz="${Call}" property="subject" numLetters="5"
          separator="-" />
      </div>
      <div class="visible-sm">
        <g:letterBar clazz="${Call}" property="subject" numLetters="3" />
      </div>
      <div class="hidden-xs hidden-sm">
        <g:letterBar clazz="${Call}" property="subject" />
      </div>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="subject" title="${message(code: 'call.subject.label')}" />
              <g:ifModuleAllowed modules="contact"><g:sortableColumn property="organization.name" title="${message(code: 'call.organization.label')}" style="width: 15em;" /></g:ifModuleAllowed>
              <g:ifModuleAllowed modules="contact"><g:sortableColumn property="person.lastName" title="${message(code: 'call.person.label')}" /></g:ifModuleAllowed>
              <g:sortableColumn property="start" title="${message(code: 'call.start.label')}" />
              <g:sortableColumn property="type" title="${message(code: 'call.type.label')}" />
              <g:sortableColumn property="status" title="${message(code: 'call.status.label')}" />
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${callInstanceList}" status="i" var="callInstance">
            <tr>
              <td class="col-type-string call-subject"><g:link action="show" id="${callInstance.id}"><g:fieldValue bean="${callInstance}" field="subject" /></g:link></td>
              <g:ifModuleAllowed modules="contact"><td class="col-type-ref call-organization"><g:link controller="organization" action="show" id="${callInstance.organization?.id}"><g:fieldValue bean="${callInstance}" field="organization" /></g:link></td></g:ifModuleAllowed>
              <g:ifModuleAllowed modules="contact"><td class="col-type-ref call-person"><g:link controller="person" action="show" id="${callInstance.person?.id}"><g:fieldValue bean="${callInstance}" field="person" /></g:link></td></g:ifModuleAllowed>
              <td class="col-type-date call-start"><g:formatDate date="${callInstance.start}" /></td>
              <td class="col-type-status call-type"><g:message code="call.type.${callInstance?.type}" /></td>
              <td class="col-type-status call-status"><g:message code="call.status.${callInstance?.status}" /></td>
              <td class="col-actions">
                <g:button action="edit" id="${callInstance.id}"
                  color="success" size="xs" icon="pencil-square-o"
                  message="default.button.edit.label" />
              </td>
            </tr>
          </g:each>
          </tbody>
        </table>
      </div>
      <nav class="text-center">
        <div class="visible-xs">
          <g:paginate total="${callInstanceTotal}" maxsteps="3"
            class="pagination-sm" />
        </div>
        <div class="hidden-xs">
          <g:paginate total="${callInstanceTotal}" />
        </div>
      </nav>
    </g:applyLayout>
  </body>
</html>
