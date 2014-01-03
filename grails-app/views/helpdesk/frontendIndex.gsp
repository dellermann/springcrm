<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<%@ page import="org.amcworld.springcrm.Ticket" %>
<%@ page import="org.amcworld.springcrm.TicketStage" %>
<html>
<head>
  <meta name="layout" content="frontend" />
  <g:set var="entityName" value="${message(code: 'helpdesk.label', default: 'Helpdesk')}" />
  <g:set var="ticketEntityName" value="${message(code: 'ticket.label', default: 'Ticket')}" />
  <title><g:fieldValue bean="${helpdeskInstance}" field="name" /> –
  <g:message code="helpdesk.frontend.title" /></title>
  <r:require modules="ticket" />
</head>

<body>
  <content tag="title">
    <h1><g:message code="${entityName}" />
    <g:fieldValue bean="${helpdeskInstance}" field="name" /></h1>
  </content>
  <aside id="ticket-forms">
    <%--<g:set var="ticketInstance" value="${new Ticket()}" />--%>
    <div id="create-ticket-form">
      <g:uploadForm controller="ticket" action="frontendSave">
        <g:hiddenField name="helpdesk" value="${helpdeskInstance.id}" />
        <g:hiddenField name="accessCode" value="${helpdeskInstance.accessCode}" />
        <fieldset>
          <header>
            <h3><g:message code="ticket.create.title" /></h3>
            <div class="buttons">
              <button type="submit" class="button green small"><i class="fa fa-floppy-o"></i> <g:message code="default.button.save.label" /></button>
            </div>
          </header>
          <div class="form-fragment">
            <f:field bean="${ticketInstance}" property="subject" />
            <f:field bean="${ticketInstance}" property="priority" />
            <f:field bean="${ticketInstance}" property="salutation" />
            <f:field bean="${ticketInstance}" property="firstName" />
            <f:field bean="${ticketInstance}" property="lastName" />
            <f:field bean="${ticketInstance}" property="phone" />
            <f:field bean="${ticketInstance}" property="mobile" />
            <f:field bean="${ticketInstance}" property="email1" />
          </div>
        </fieldset>
        <fieldset>
          <div class="form-fragment">
            <div class="row">
              <div class="label">
                <label for="messageText"><g:message code="ticket.messageText.label" /></label>
              </div>
            </div>
            <div class="row">
              <div class="field${hasErrors(bean: ticketInstance, field: 'messageText', ' error')}">
                <g:textArea name="messageText" cols="20" rows="10"
                  required="required" value="${ticketInstance.messageText}" />
                <ul class="field-msgs">
                  <li class="info-msg"><g:message code="default.required" default="required" /></li>
                  <g:eachError bean="${ticketInstance}" field="messageText">
                  <li class="error-msg"><g:message error="${it}" /></li>
                  </g:eachError>
                </ul>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label for="attachment"><g:message code="ticket.attachment.label" /></label>
              </div>
            </div>
            <div class="row">
              <div class="field">
                <input type="file" id="attachment" name="attachment" />
              </div>
            </div>
          </div>
        </fieldset>
        <div class="buttons">
          <button type="submit" class="button green"><i class="fa fa-floppy-o"></i> <g:message code="default.button.save.label" /></button>
        </div>
      </g:uploadForm>
    </div>
    <div id="send-message-form" style="display: none;">
      <g:uploadForm controller="ticket" action="frontendSendMessage">
        <g:hiddenField name="helpdesk" value="${helpdeskInstance.id}" />
        <g:hiddenField name="accessCode"
          value="${helpdeskInstance.accessCode}" />
        <input type="hidden" name="id" />
        <fieldset>
          <header>
            <h3><g:message code="ticket.sendMessage.title" /></h3>
            <div class="buttons">
              <button type="submit" class="button green small"><i class="fa fa-envelope-o"></i> <g:message code="default.button.send.label" /></button>
            </div>
          </header>
          <div class="form-fragment">
            <div class="row">
              <div class="label">
                <label for="messageText"><g:message code="ticket.messageText.label" /></label>
              </div>
            </div>
            <div class="row">
              <div class="field">
                <g:textArea name="messageText" cols="20" rows="10"
                  required="required" value="${ticketInstance.messageText}" />
                <ul class="field-msgs">
                  <li class="info-msg"><g:message code="default.required" default="required" /></li>
                </ul>
              </div>
            </div>
            <div class="row">
              <div class="label">
                <label for="attachment"><g:message code="ticket.attachment.label" /></label>
              </div>
            </div>
            <div class="row">
              <div class="field">
                <input type="file" id="attachment" name="attachment" />
              </div>
            </div>
          </div>
        </fieldset>
        <div class="buttons">
          <button type="submit" class="button green"><i class="fa fa-envelope-o"></i> <g:message code="default.button.send.label" /></button>
          <g:button color="red" class="cancel-btn" icon="times"
            message="default.button.cancel.label" />
        </div>
      </g:uploadForm>
    </div>
  </aside>
  <div id="content">
    <g:set var="openTickets"
      value="${helpdeskInstance.tickets.findAll { it.stage != TicketStage.closed } }" />
    <g:if test="${openTickets}">
    <section class="fieldset">
      <header>
        <h3><g:message code="ticket.list.open.title" /></h3>
        <div class="buttons">
          <g:button color="green" size="small" class="create-ticket-btn"
            icon="plus" message="default.new.label"
            args="[ticketEntityName]" />
        </div>
      </header>
      <table class="content-table">
        <thead>
          <tr>
            <th scope="col"><input type="checkbox" id="open-ticket-row-selector" /></th>
            <g:sortableColumn scope="col" property="number"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.number.label')}" />
            <g:sortableColumn scope="col" property="subject"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.subject.label')}" />
            <g:sortableColumn scope="col" property="stage"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.stage.label')}" />
            <g:sortableColumn scope="col" property="customerName"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.customerName.label')}" />
            <g:sortableColumn scope="col" property="dateCreated"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.dateCreated.label')}" />
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${openTickets}" var="ticketInstance">
          <tr data-ticket-id="${ticketInstance.id}">
            <td class="row-selector"><input type="checkbox" id="open-ticket-row-selector-${ticketInstance.id}" data-id="${ticketInstance.id}" /></td>
            <td class="id ticket-number">
              <g:link controller="ticket" action="frontendShow"
                id="${ticketInstance.id}"
                params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                ><g:fieldValue bean="${ticketInstance}"
                field="fullNumber" /></g:link>
            </td>
            <td class="string ticket-subject">
              <g:link controller="ticket" action="frontendShow"
                id="${ticketInstance.id}"
                params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                ><g:fieldValue bean="${ticketInstance}" field="subject"
                /></g:link>
            </td>
            <td class="status ticket-stage ticket-stage-${ticketInstance.stage}"><g:message code="ticket.stage.${ticketInstance.stage}" default="${ticketInstance.stage.toString()}" /></td>
            <td class="string ticket-customer-name"><g:fieldValue bean="${ticketInstance}" field="customerName" /></td>
            <td class="date ticket-date-created"><g:formatDate date="${ticketInstance.dateCreated}" formatName="default.format.datetime" /></td>
            <td class="action-buttons">
              <g:button color="white" size="small" class="send-btn"
                icon="envelope-o" message="ticket.sendMessage.label" />
              <g:button controller="ticket" action="frontendCloseTicket"
                id="${ticketInstance.id}"
                params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                color="red" size="small" class="close-btn" icon="check"
                message="ticket.close.label" />
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </section>
    </g:if>
    <g:else>
      <div class="empty-list">
        <p><g:message code="default.list.empty" /></p>
        <div class="buttons">
          <g:button color="green" class="create-ticket-btn" icon="plus"
            message="default.new.label" args="[ticketEntityName]" />
        </div>
      </div>
    </g:else>
    <g:set var="closedTickets"
      value="${helpdeskInstance.tickets.findAll { it.stage == TicketStage.closed } }" />
    <g:if test="${closedTickets}">
    <section class="fieldset">
      <header><h3><g:message code="ticket.list.closed.title" /></h3></header>
      <table class="content-table">
        <thead>
          <tr>
            <th scope="col"><input type="checkbox" id="open-ticket-row-selector" /></th>
            <g:sortableColumn scope="col" property="number"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.number.label')}" />
            <g:sortableColumn scope="col" property="subject"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.subject.label')}" />
            <g:sortableColumn scope="col" property="customerName"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.customerName.label')}" />
            <g:sortableColumn scope="col" property="dateCreated"
              mapping="helpdeskFrontend"
              params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
              title="${message(code: 'ticket.dateCreated.label')}" />
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>
        <g:each in="${closedTickets}" var="ticketInstance">
          <tr>
            <td class="row-selector"><input type="checkbox" id="open-ticket-row-selector-${ticketInstance.id}" data-id="${ticketInstance.id}" /></td>
            <td class="id ticket-number">
              <g:link controller="ticket" action="frontendShow"
                id="${ticketInstance.id}"
                params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                ><g:fieldValue bean="${ticketInstance}" field="fullNumber"
                /></g:link>
            </td>
            <td class="string ticket-subject">
              <g:link controller="ticket" action="frontendShow"
                id="${ticketInstance.id}"
                params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                ><g:fieldValue bean="${ticketInstance}" field="subject"
                /></g:link>
            </td>
            <td class="string ticket-customer-name"><g:fieldValue bean="${ticketInstance}" field="customerName" /></td>
            <td class="date ticket-date-created"><g:formatDate date="${ticketInstance.dateCreated}" formatName="default.format.datetime" /></td>
            <td class="action-buttons">
              <g:button controller="ticket" action="frontendResubmitTicket"
                id="${ticketInstance.id}"
                params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
                color="orange" size="small" icon="share-square-o"
                message="ticket.resubmission.label" />
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </section>
    </g:if>
  </div>
</body>
</html>
