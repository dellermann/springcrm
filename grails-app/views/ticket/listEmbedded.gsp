<g:applyLayout name="listEmbedded"
  model="[list: ticketList, total: ticketCount]">
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
          <g:link controller="ticket" action="show" id="${ticket.id}">
            <g:fullNumber bean="${ticket}"/>
          </g:link>
        </td>
        <td class="col-type-string ticket-subject">
          <g:link controller="ticket" action="show" id="${ticket.id}">
            <g:fieldValue bean="${ticket}" field="subject"/>
          </g:link>
        </td>
        <td class="col-type-ref ticket-helpdesk">
          <g:if test="${session.credential.checkAllowedControllers(['helpdesk'] as Set)}">
            <g:link controller="helpdesk" action="show"
              id="${ticket.helpdesk.id}"><g:fieldValue
              bean="${ticket}" field="helpdesk.name"/></g:link>
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
          <g:button controller="ticket" action="edit" id="${ticket.id}"
            color="success" size="xs" icon="pencil-square-o"
            message="default.button.edit.label"/>
        </td>
      </tr>
      </g:each>
    </tbody>
  </table>
</g:applyLayout>
