<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<%@ page import="org.amcworld.springcrm.Ticket" %>
<%@ page import="org.amcworld.springcrm.TicketLogAction" %>
<%@ page import="org.amcworld.springcrm.TicketStage" %>
<html>
<head>
  <meta name="layout" content="frontend" />
  <g:set var="entityName" value="${message(code: 'ticket.label')}" />
  <g:set var="helpdeskInstance" value="${ticketInstance.helpdesk}" />
  <title><g:fieldValue bean="${helpdeskInstance}" field="name" /> –
  <g:message code="helpdesk.frontend.title" /></title>
  <r:require modules="ticket" />
</head>

<body>
  <content tag="title">
    <h1><g:message code="helpdesk.label" />
    <g:fieldValue bean="${helpdeskInstance}" field="name" /></h1>
  </content>
  <content tag="toolbar">
    <li><g:button mapping="helpdeskFrontend"
      params="[urlName: helpdeskInstance.urlName, accessCode: helpdeskInstance.accessCode]"
      color="white" icon="arrow-left"
      message="default.button.back.label" /></li>
  </content>
  <aside id="action-bar">
    <h3><g:message code="default.actions" /></h3>
    <ul>
      <li>
        <g:button elementId="send-message-btn"
          color="white" size="medium" icon="envelope-alt"
          message="ticket.sendMessage.label" 
          data-title="${message(code: 'ticket.sendMessage.toCustomer.title')}"
          data-submit-url="${createLink(action: 'sendMessage', id: ticketInstance.id)}"
          />
      </li>
      <g:if test="${ticketInstance.stage != TicketStage.closed}">
      <li><g:button controller="ticket" action="frontendCloseTicket"
        id="${ticketInstance.id}"
        params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
        color="red" size="medium" class="close-btn" icon="ok"
        message="ticket.close.label" /></li>
      </g:if>
      <g:if test="${ticketInstance.stage == TicketStage.closed}">
      <li><g:button controller="ticket" action="frontendResubmitTicket"
        id="${ticketInstance.id}"
        params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
        color="orange" size="medium" icon="share"
        message="ticket.resubmission.label" /></li>
      </g:if>
    </ul>
  </aside>
  <div id="content">
    <h2>${entityName}
    <g:fieldValue bean="${ticketInstance}" field="fullNumber" /> –
    <g:fieldValue bean="${ticketInstance}" field="subject" /></h2>
    <div class="data-sheet">
      <section class="fieldset">
        <header><h3><g:message code="ticket.fieldset.general.label" /></h3></header>
        <div class="multicol-content">
          <div class="col col-l">
            <f:display bean="${ticketInstance}" property="number">
              <g:fieldValue bean="${ticketInstance}" field="fullNumber" />
            </f:display>
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
            collection="${ticketInstance.logEntries.reverse().findAll { !it.internal && it.action != TicketLogAction.note } }" />
        </div>
      </section>
    </div>
  </div>

  <div id="send-message-dialog" style="display: none;">
    <g:uploadForm action="frontendSendMessage" id="${ticketInstance.id}">
      <g:hiddenField name="helpdesk" value="${helpdeskInstance.id}" />
      <g:hiddenField name="accessCode"
        value="${helpdeskInstance.accessCode}" />
      <g:hiddenField name="returnUrl" value="${url()}" />
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
