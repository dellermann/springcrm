<html>
  <head>
    <meta name="stylesheet" content="helpdesk"/>
  </head>

  <body>
    <g:applyLayout name="list" model="[list: ticketList, type: 'ticket']">
      <g:if test="${!mailSystemConfigured}">
      <div class="flash-message form-warning-hint">
        <p
          ><g:message code="ticket.warning.mailNotConfigured.description"/></p>
        <div><g:button controller="config" action="show"
          params="[page: 'mail']" color="warning" icon="cog"
          message="ticket.warning.mailNotConfigured.button"/></div>
      </div>
      </g:if>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number"
                title="${message(code: 'ticket.number.label')}"/>
              <g:sortableColumn property="subject"
                title="${message(code: 'ticket.subject.label')}"/>
              <g:sortableColumn property="helpdesk.name"
                title="${message(code: 'ticket.helpdesk.label')}"/>
              <g:sortableColumn property="stage"
                title="${message(code: 'ticket.stage.label')}"/>
              <g:sortableColumn property="customerName"
                title="${message(code: 'ticket.customerName.label')}"/>
              <g:sortableColumn property="dateCreated"
                title="${message(code: 'ticket.dateCreated.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="ticket" in="${ticketList}" status="i">
            <tr>
              <td class="col-type-string ticket-number">
                <g:link action="show" id="${ticket.id}">
                  <g:fullNumber bean="${ticket}"/>
                </g:link>
              </td>
              <td class="col-type-string ticket-subject">
                <g:link action="show" id="${ticket.id}">
                  <g:fieldValue bean="${ticket}" field="subject"/>
                </g:link>
              </td>
              <td class="col-type-ref ticket-helpdesk">
                <g:if test="${session.credential.checkAllowedControllers(['helpdesk'] as Set)}">
                <g:link controller="helpdesk" action="show"
                  id="${ticket.helpdesk.id}">
                  <g:fieldValue bean="${ticket}" field="helpdesk.name"/>
                </g:link>
                </g:if>
                <g:else>
                  <g:fieldValue bean="${ticket}" field="helpdesk.name"/>
                </g:else>
              </td>
              <td class="col-type-status ticket-stage ticket-stage-${ticket.stage}">
                <g:message code="ticket.stage.${ticket.stage}"
                  default="${ticket.stage.toString()}"/>
              </td>
              <td class="col-type-string ticket-customer-name">
                <g:fieldValue bean="${ticket}" field="customerName"/>
              </td>
              <td class="col-type-date ticket-date-created">
                <g:formatDate date="${ticket.dateCreated}"
                  formatName="default.format.datetime"/>
              </td>
              <td class="col-actions">
                <g:button action="edit" id="${ticket.id}"
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
            <g:paginate total="${ticketCount}" maxsteps="3"
              class="pagination-sm"/>
          </div>
          <div class="hidden-xs">
            <g:paginate total="${ticketCount}"/>
          </div>
        </nav>
        <g:render template="/layouts/numItemsPerPage"/>
      </div>
    </g:applyLayout>
  </body>
</html>
