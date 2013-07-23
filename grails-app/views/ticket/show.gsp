<%@ page import="org.amcworld.springcrm.Ticket" %>
<%@ page import="org.amcworld.springcrm.TicketLogAction" %>
<%@ page import="org.amcworld.springcrm.TicketMessage" %>
<%@ page import="org.amcworld.springcrm.TicketStage" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <r:require modules="ticket" />
  <g:set var="entityName" value="${message(code: 'ticket.label', default: 'Ticket')}" />
  <g:set var="entitiesName" value="${message(code: 'ticket.plural', default: 'Tickets')}" />
  <g:set var="user" value="${session.user}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div id="main-container-header">
    <h2><g:message code="${entitiesName}" /></h2>
    <nav id="toolbar-container">
      <ul id="toolbar">
        <li><g:link action="list" class="white"><g:message code="default.button.list.label" /></g:link></li>
        <li><g:link action="create" class="green"><g:message code="default.button.create.label" /></g:link></li>
        <li><g:link action="edit" id="${ticketInstance?.id}" class="green"><g:message code="default.button.edit.label" /></g:link></li>
        <li><g:link action="copy" id="${ticketInstance?.id}" class="blue"><g:message code="default.button.copy.label" /></g:link></li>
        <li><g:link action="delete" id="${ticketInstance?.id}" class="red delete-btn"><g:message code="default.button.delete.label" /></g:link></li>
      </ul>
    </nav>
  </div>
  <aside id="action-bar">
    <h4><g:message code="default.actions" /></h4>
    <ul>
      <g:if test="${(user.admin || user in ticketInstance.helpdesk.users) && ticketInstance.stage in [TicketStage.created, TicketStage.resubmitted]}">
      <li><g:link action="takeOn" id="${ticketInstance?.id}" class="button medium green"><g:message code="ticket.takeOn.label" /></g:link></li>
      </g:if>
      <g:if test="${user.admin || (user == ticketInstance.assignedUser && ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess])}">
      <li><span id="send-message-btn" class="button medium white"><g:message code="ticket.sendMessage.toCustomer.label" /></span></li>
      </g:if>
      <g:if test="${(user.admin || user == ticketInstance.assignedUser) && ticketInstance.stage == TicketStage.assigned}">
      <li><g:link action="changeStage" id="${ticketInstance?.id}" params="[stage: TicketStage.inProcess]" class="button medium green"><g:message code="ticket.changeStage.inProcess" /></g:link></li>
      </g:if>
      <g:if test="${(user.admin || user == ticketInstance.assignedUser) && ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess]}">
      <li class="menu">
        <span class="button menu-button medium blue"><span><g:message code="ticket.assign.label" /></span></span>
        <div>
          <ul>
            <g:each in="${ticketInstance.helpdesk.users - user}">
            <li><g:link action="assignToUser" id="${ticketInstance.id}" params="[user: it.id]">${it.toString().encodeAsHTML()}</g:link></li>
            </g:each>
          </ul>
        </div>
      </li>
      </g:if>
      <g:if test="${(user.admin || user == ticketInstance.assignedUser) && ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess]}">
      <li><g:link action="changeStage" id="${ticketInstance?.id}" params="[stage: TicketStage.closed]" elementId="close-ticket-btn" class="button medium red"><g:message code="ticket.changeStage.closed" /></g:link></li>
      </g:if>
    </ul>
  </aside>
  <section id="content" class="with-action-bar">
    <g:if test="${flash.message}">
    <div class="flash-message message" role="status">${flash.message}</div>
    </g:if>
    <h3>${ticketInstance?.toString()}</h3>
    <div class="data-sheet">
      <div class="fieldset">
        <h4><g:message code="ticket.fieldset.general.label" /></h4>
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
      </div>
      <div class="fieldset">
        <h4><g:message code="ticket.fieldset.customerData.label" /></h4>
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
      </div>

      <div class="fieldset">
        <h4><g:message code="ticket.fieldset.history.label" /></h4>
        <div class="fieldset-content">
          <g:each in="${ticketInstance.logEntries.reverse()}" var="l">
          <section class="ticket-log-entry">
            <h5><g:formatDate date="${l.dateCreated}" /> – <g:message message="${l}" /></h5>
            <div class="row">
              <div class="label"><g:message code="ticket.creator.label" /></div>
              <div class="field">
                <output>
                  <g:if test="${l.creator}"><g:fieldValue bean="${l}" field="creator" /></g:if>
                  <g:else><g:message code="ticket.creator.customer" /></g:else>
                </output>
              </div>
            </div>
            <g:if test="${l.action == TicketLogAction.sendMessage}">
            <div class="row">
              <div class="label"><g:message code="ticket.recipient.label" /></div>
              <div class="field">
                <output>
                  <g:if test="${l.creator}">
                  <g:if test="${l.recipient}"><g:fieldValue bean="${l}" field="recipient" /></g:if>
                  <g:else><g:message code="ticket.recipient.customer" /></g:else>
                  </g:if>
                  <g:else>
                  <g:if test="${l.recipient}"><g:fieldValue bean="${l}" field="recipient" /></g:if>
                  <g:else><g:message code="ticket.recipient.specialist" /></g:else>
                  </g:else>
                </output>
              </div>
            </div>
            <div class="row">
              <div class="label"><g:message code="ticketMessage.message.label" /></div>
              <div class="field">
                <div class="html-content"><markdown:renderHtml text="${l.message.message}" /></div>
              </div>
            </div>
            <g:if test="${l.message.attachment}">
            <div class="row">
              <div class="label"><g:message code="ticketMessage.attachment.label" /></div>
              <div class="field">
                <g:link controller="dataFile" action="loadFile"
                  id="${l.message.attachment.id}"
                  params="[type: 'ticketMessage']" target="_blank">
                  <g:fieldValue bean="${l.message.attachment}" field="fileName"/>
                </g:link>
                (<g:formatSize number="${l.message.attachment.fileSize}" />)
              </div>
            </div>
            </g:if>
            </g:if>
          </section>
          </g:each>
        </div>
      </div>
    </div>

    <p class="record-timestamps">
      <g:message code="default.recordTimestamps" args="[formatDate(date: ticketInstance?.dateCreated, style: 'SHORT'), formatDate(date: ticketInstance?.lastUpdated, style: 'SHORT')]" />
    </p>
  </section>

  <div id="send-message-dialog"
    title="${message(code: 'ticket.sendMessage.toCustomer.title')}"
    style="display: none;">
    <g:set var="ticketMessageInstance" value="${new TicketMessage()}" />
    <g:form action="sendMessage" id="${ticketInstance.id}" method="post">
      <div class="form">
        <f:field bean="${ticketMessageInstance}" property="message"
          cols="40" rows="10" />
        <f:field bean="${ticketMessageInstance}" property="attachment" />
      </div>
    </g:form>
  </div>
</body>
</html>
