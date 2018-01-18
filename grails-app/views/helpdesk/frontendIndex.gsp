<%@ page import="org.amcworld.springcrm.TicketStage" %>

<html>
  <head>
    <meta name="layout" content="frontend"/>
    <title><g:message code="default.overview"/></title>
    <meta name="stylesheet" content="helpdesk-frontend"/>
    <g:set var="entityName" value="${message(code: 'ticket.label')}"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:button controller="ticket" action="frontendCreate"
        params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
        color="success" size="sm" icon="plus-circle"
        message="default.new.label" args="[entityName]"/>
    </content>

    <g:set var="openTickets"
      value="${ticketInstanceList.findAll { it.stage != TicketStage.closed } }"/>
    <g:if test="${openTickets}">
    <section>
      <header><h3><g:message code="ticket.list.open.title"/></h3></header>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number" mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.number.label')}"/>
              <g:sortableColumn property="subject" mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.subject.label')}"/>
              <g:sortableColumn property="stage" mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.stage.label')}"/>
              <g:sortableColumn property="lastName" mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.customerName.label')}"/>
              <g:sortableColumn property="dateCreated"
                mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.dateCreated.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="ticket" in="${openTickets}">
            <tr data-ticket-id="${ticket.id}">
              <td class="col-type-id ticket-number">
                <g:link controller="ticket" action="frontendShow"
                  id="${ticket.id}"
                  params="[
                    helpdesk: helpdeskInstance.id,
                    accessCode: helpdeskInstance.accessCode
                  ]">
                  <g:fullNumber bean="${ticket}"/>
                </g:link>
              </td>
              <td class="col-type-string ticket-subject">
                <g:link controller="ticket" action="frontendShow"
                  id="${ticket.id}"
                  params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                  ><g:fieldValue bean="${ticket}" field="subject"
                  /></g:link>
              </td>
              <td class="col-type-status ticket-stage ticket-stage-${ticket.stage}"><g:message code="ticket.stage.${ticket.stage}" default="${ticket.stage.toString()}"/></td>
              <td class="col-type-string ticket-customer-name"><g:fieldValue bean="${ticket}" field="customerName"/></td>
              <td class="col-type-date ticket-date-created"><g:formatDate date="${ticket.dateCreated}" formatName="default.format.datetime"/></td>
              <td class="col-actions">
                <g:button controller="ticket" action="frontendCloseTicket"
                  id="${ticket.id}"
                  params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                  color="danger" size="xs" class="close-ticket-link"
                  icon="check-circle-o" message="ticket.close.label"/>
              </td>
            </tr>
            </g:each>
          </tbody>
        </table>
      </div>
    </section>
    </g:if>

    <g:set var="closedTickets"
      value="${ticketInstanceList.findAll { it.stage == TicketStage.closed } }"/>
    <g:if test="${closedTickets}">
    <section>
      <header><h3><g:message code="ticket.list.closed.title"/></h3></header>
      <div class="table-responsive">
        <table class="table data-table">
          <thead>
            <tr>
              <g:sortableColumn property="number" mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.number.label')}"/>
              <g:sortableColumn property="subject" mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.subject.label')}"/>
              <g:sortableColumn property="lastName" mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.customerName.label')}"/>
              <g:sortableColumn property="dateCreated"
                mapping="helpdeskFrontend"
                params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
                title="${message(code: 'ticket.dateCreated.label')}"/>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <g:each var="ticket" in="${closedTickets}">
            <tr>
              <td class="col-type-id ticket-number">
                <g:link controller="ticket" action="frontendShow"
                  id="${ticket.id}"
                  params="[
                    helpdesk: helpdeskInstance.id,
                    accessCode: helpdeskInstance.accessCode
                  ]">
                  <g:fullNumber bean="${ticket}"/>
                </g:link>
              </td>
              <td class="col-type-string ticket-subject">
                <g:link controller="ticket" action="frontendShow"
                  id="${ticket.id}"
                  params="[
                    helpdesk: helpdeskInstance.id,
                    accessCode: helpdeskInstance.accessCode
                  ]"
                  ><g:fieldValue bean="${ticket}" field="subject"
                  /></g:link>
              </td>
              <td class="col-type-string ticket-customer-name"><g:fieldValue bean="${ticket}" field="customerName"/></td>
              <td class="col-type-date ticket-date-created"><g:formatDate date="${ticket.dateCreated}" formatName="default.format.datetime"/></td>
              <td class="col-actions">
                <g:button controller="ticket" action="frontendResubmitTicket"
                  id="${ticket.id}"
                  params="[
                    helpdesk: helpdeskInstance.id,
                    accessCode: helpdeskInstance.accessCode
                  ]"
                  color="warning" size="xs" icon="share-square-o"
                  message="ticket.resubmission.label"/>
              </td>
            </tr>
            </g:each>
          </tbody>
        </table>
      </div>
    </section>

    </g:if>
    <g:if test="${!ticketInstanceList}">
    <div class="well well-lg empty-list">
      <p><g:message code="default.list.empty"/></p>
      <div class="buttons">
        <g:button controller="ticket" action="frontendCreate"
          params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
          color="success" icon="plus-circle" message="default.new.label"
          args="[entityName]"/>
      </div>
    </div>
    </g:if>

    <content tag="scripts">
      <asset:javascript src="helpdesk-frontend"/>
    </content>
  </body>
</html>
