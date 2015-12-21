<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="helpdesk" />
  </head>

  <body>
    <g:applyLayout name="list" model="[list: ticketInstanceList]">
      <g:if test="${!mailSystemConfigured}">
      <div class="flash-message form-warning-hint">
        <p
          ><g:message code="ticket.warning.mailNotConfigured.description"
        /></p>
        <div><g:button controller="config" action="show"
          params="[page: 'mail']" color="warning" icon="cog"
          message="ticket.warning.mailNotConfigured.button" /></div>
      </div>
      </g:if>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number" title="${message(code: 'ticket.number.label')}" />
              <g:sortableColumn property="subject" title="${message(code: 'ticket.subject.label')}" />
              <g:sortableColumn property="helpdesk.name" title="${message(code: 'ticket.helpdesk.label')}" />
              <g:sortableColumn property="stage" title="${message(code: 'ticket.stage.label')}" />
              <g:sortableColumn property="customerName" title="${message(code: 'ticket.customerName.label')}" />
              <g:sortableColumn property="dateCreated" title="${message(code: 'ticket.dateCreated.label')}" />
              <th></th>
            </tr>
          </thead>
          <tbody>
          <g:each in="${ticketInstanceList}" status="i" var="ticketInstance">
            <tr>
              <td class="col-type-string ticket-number"><g:link action="show" id="${ticketInstance.id}"><g:fieldValue bean="${ticketInstance}" field="fullNumber" /></g:link></td>
              <td class="col-type-string ticket-subject"><g:link action="show" id="${ticketInstance.id}"><g:fieldValue bean="${ticketInstance}" field="subject" /></g:link></td>
              <td class="col-type-ref ticket-helpdesk">
                <g:if test="${session.user.checkAllowedControllers(['helpdesk'])}">
                <g:link controller="helpdesk" action="show"
                  id="${ticketInstance.helpdesk.id}"><g:fieldValue
                  bean="${ticketInstance}" field="helpdesk.name" /></g:link>
                </g:if>
                <g:else>
                  <g:fieldValue bean="${ticketInstance}" field="helpdesk.name" />
                </g:else>
              </td>
              <td class="col-type-status ticket-stage ticket-stage-${ticketInstance.stage}"><g:message code="ticket.stage.${ticketInstance.stage}" default="${ticketInstance.stage.toString()}" /></td>
              <td class="col-type-string ticket-customer-name"><g:fieldValue bean="${ticketInstance}" field="customerName" /></td>
              <td class="col-type-date ticket-date-created"><g:formatDate date="${ticketInstance.dateCreated}" formatName="default.format.datetime" /></td>
              <td class="col-actions">
                <g:button action="edit" id="${ticketInstance.id}"
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
          <g:paginate total="${ticketInstanceTotal}" maxsteps="3"
            class="pagination-sm" />
        </div>
        <div class="hidden-xs">
          <g:paginate total="${ticketInstanceTotal}" />
        </div>
      </nav>
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="ticket" />
    </content>
  </body>
</html>
