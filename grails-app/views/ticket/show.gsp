<%@ page import="org.amcworld.springcrm.Ticket" %>
<%@ page import="org.amcworld.springcrm.TicketLogAction" %>
<%@ page import="org.amcworld.springcrm.TicketStage" %>
<html>
<head>
  <meta name="layout" content="main" />
  <r:require modules="ticket" />
  <g:set var="entityName" value="${message(code: 'ticket.label', default: 'Ticket')}" />
  <g:set var="entitiesName" value="${message(code: 'ticket.plural', default: 'Tickets')}" />
  <g:set var="user" value="${session.user}" />
  <g:set var="otherUsers" value="${ticketInstance.helpdesk.users - user}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <header>
    <h1><g:message code="${entitiesName}" /></h1>
    <g:render template="/layouts/toolbarShow"
      model="[instance: ticketInstance]" />
  </header>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <g:if test="${(user.admin || user in ticketInstance.helpdesk.users) && ticketInstance.stage in [TicketStage.created, TicketStage.resubmitted]}">
      <li><g:button action="takeOn" id="${ticketInstance?.id}"
        elementId="take-on-btn" color="green" size="medium"
        message="ticket.takeOn.label" /></li>
      </g:if>
      <g:if test="${user.admin || (user == ticketInstance.assignedUser && ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess])}">
      <li>
        <g:button elementId="send-message-to-customer-btn"
          color="white" size="medium" icon="envelope-alt"
          message="ticket.sendMessage.toCustomer.label" 
          data-title="${message(code: 'ticket.sendMessage.toCustomer.title')}"
          data-submit-url="${createLink(action: 'sendMessage', id: ticketInstance.id)}"
          />
      </li>
      </g:if>
      <g:if test="${otherUsers}">
      <li id="send-message-to-user-menu">
        <g:menuButton color="white" size="medium" icon="envelope-alt"
          message="ticket.sendMessage.toUser.label"
          data-title="${message(code: 'ticket.sendMessage.toUser.title')}"
          data-submit-url="${createLink(action: 'sendMessage', id: ticketInstance.id)}">
          <g:each in="${otherUsers}">
          <li><a href="#" data-user-id="${it.id}">${it.toString().encodeAsHTML()}</a></li>
          </g:each>
        </g:menuButton>
      </li>
      </g:if>
      <li>
        <g:button elementId="create-note-btn" color="white" size="medium"
          icon="pencil" message="ticket.createNote.label"
          data-title="${message(code: 'ticket.createNote.title')}"
          data-submit-url="${createLink(action: 'createNote', id: ticketInstance.id)}" />
      </li>
      <g:if test="${(user.admin || user == ticketInstance.assignedUser) && ticketInstance.stage == TicketStage.assigned}">
      <li><g:button action="changeStage" id="${ticketInstance?.id}"
        params="[stage: TicketStage.inProcess]" color="green" size="medium"
        message="ticket.changeStage.inProcess" /></li>
      </g:if>
      <g:if test="${(user.admin || user == ticketInstance.assignedUser) && ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess] && otherUsers}">
      <li>
        <g:menuButton elementId="assign-user-menu" color="blue"
          size="medium" message="ticket.changeStage.assign">
          <g:each in="${otherUsers}">
          <li><g:link action="assignToUser" id="${ticketInstance.id}" params="[user: it.id]">${it.toString().encodeAsHTML()}</g:link></li>
          </g:each>
        </g:menuButton>
      </li>
      </g:if>
      <g:if test="${(user.admin || user == ticketInstance.assignedUser) && ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess]}">
      <li><g:button action="changeStage" id="${ticketInstance?.id}"
        params="[stage: TicketStage.closed]" elementId="close-ticket-btn"
        color="red" size="medium" message="ticket.changeStage.closed" /></li>
      </g:if>
      <g:if test="${ticketInstance.stage == TicketStage.closed}">
      <li><g:button action="changeStage" id="${ticketInstance?.id}"
        params="[stage: TicketStage.resubmitted]" color="orange"
        size="medium" icon="share"
        message="ticket.changeStage.resubmitted" /></li>
      </g:if>
    </ul>
  </aside>
  <div id="content">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h2>${ticketInstance?.toString()}</h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="ticket.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${ticketInstance}" property="number">
              <g:fieldValue bean="${ticketInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${ticketInstance}" property="helpdesk" />
            <f:display bean="${ticketInstance}" property="subject" />
          </div>
          <div class="col col-r">
            <f:display bean="${ticketInstance}" property="stage" />
            <f:display bean="${ticketInstance}" property="priority" />
            <f:display bean="${ticketInstance}" property="creator" />
            <f:display bean="${ticketInstance}" property="assignedUser" />
          </div>
        </div>
      </section>
      <section class="fieldset">
        <header><h3><g:message code="ticket.fieldset.customerData.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${ticketInstance}" property="salutation" />
            <f:display bean="${ticketInstance}" property="firstName" />
            <f:display bean="${ticketInstance}" property="lastName" />
            <f:display bean="${ticketInstance}" property="phone" />
            <f:display bean="${ticketInstance}" property="phoneHome" />
            <f:display bean="${ticketInstance}" property="mobile" />
            <f:display bean="${ticketInstance}" property="fax" />
            <f:display bean="${ticketInstance}" property="email1" />
            <f:display bean="${ticketInstance}" property="email2" />
          </div>
          <div class="col col-r">
            <f:display bean="${ticketInstance}" property="street" />
            <f:display bean="${ticketInstance}" property="postalCode" />
            <f:display bean="${ticketInstance}" property="location" />
            <f:display bean="${ticketInstance}" property="state" />
            <f:display bean="${ticketInstance}" property="country" />
          </div>
        </div>
      </section>

      <section class="fieldset">
        <header><h3><g:message code="ticket.fieldset.history.label" /></h3></header>
        <div>
          <g:render template="logEntries/logEntry"
            collection="${ticketInstance.logEntries.reverse()}" />
        </div>
      </section>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: ticketInstance?.dateCreated, style: 'SHORT'), formatDate(date: ticketInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </div>

  <div id="send-message-dialog" style="display: none;">
    <g:uploadForm action="sendMessage" id="${ticketInstance.id}" method="post">
      <g:hiddenField name="recipient" value="" />
      <div class="form">
        <div class="row">
          <div class="label">
            <label for="messageText"><g:message code="ticket.messageText.label" /></label>
          </div>
          <div class="field">
            <g:textArea name="messageText" cols="40" rows="10" required="required" /><br />
            <ul class="field-msgs">
              <li class="info-msg"><g:message code="default.required" default="required" /></li>
            </ul>
          </div>
        </div>
        <div class="row">
          <div class="label">
            <label for="attachment"><g:message code="ticket.attachment.label" /></label>
          </div>
          <div class="field">
            <input type="file" id="attachment" name="attachment" />
          </div>
        </div>
      </div>
    </g:uploadForm>
  </div>
</body>
</html>
