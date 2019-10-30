<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<%@ page import="org.amcworld.springcrm.Ticket" %>
<%@ page import="org.amcworld.springcrm.TicketLogAction" %>
<%@ page import="org.amcworld.springcrm.TicketLogEntry" %>
<%@ page import="org.amcworld.springcrm.TicketStage" %>

<html>
  <head>
    <meta name="layout" content="frontend"/>
    <title>${ticketInstance}</title>
    <meta name="stylesheet" content="helpdesk-frontend"/>
    <g:set var="entityName" value="${message(code: 'ticket.label')}"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:unless test="${helpdeskInstance.forEndUsers}">
        <g:button mapping="helpdeskFrontend" params="[
            urlName: helpdeskInstance.urlName,
            accessCode: helpdeskInstance.accessCode
          ]"
          color="default" class="hidden-xs" icon="arrow-left"
          message="default.button.back.label"/>
      </g:unless>
      <button type="button"
        class="btn btn-default hidden-xs hidden-sm send-message-link">
        <i class="fa fa-envelope-o"></i>
        <g:message code="ticket.sendMessage.title"/>
      </button>
    </content>
    <g:if test="${ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess, TicketStage.deferred, TicketStage.closed]}">
    <content tag="actionMenu">
      <li class="visible-xs visible-sm" role="menuitem">
        <a href="#" class="send-message-link">
          <i class="fa fa-envelope-o"></i>
          <g:message code="ticket.sendMessage.title"/>
        </a>
      </li>
      <g:if test="${ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess, TicketStage.deferred]}">
      <li role="menuitem">
        <g:link action="frontendCloseTicket"
          id="${ticketInstance.code ?: ticketInstance.id}"
          params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]"
          class="close-ticket-link">
          <i class="fa fa-check-circle-o"></i>
          <g:message code="ticket.close.label"/>
        </g:link>
      </li>
      </g:if>
      <g:if test="${ticketInstance.stage == TicketStage.closed}">
      <li>
        <g:link action="frontendResubmitTicket"
          id="${ticketInstance.code ?: ticketInstance.id}"
          params="[helpdesk: helpdeskInstance.id, accessCode: helpdeskInstance.accessCode]">
          <i class="fa fa-share-square-o"></i>
          <g:message code="ticket.resubmission.label"/>
        </g:link>
      </li>
      </g:if>
    </content>
    </g:if>

    <div class="form-horizontal data-form detail-view">
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${ticketInstance}" property="code"/>
            <f:display bean="${ticketInstance}" property="subject"/>
            <f:display bean="${ticketInstance}" property="stage"/>
          </div>
          <div class="column">
            <f:display bean="${ticketInstance}" property="priority"/>
            <f:display bean="${ticketInstance}" property="creator"/>
            <f:display bean="${ticketInstance}" property="assignedUser"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.customerData.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${ticketInstance}" property="salutation"/>
            <f:display bean="${ticketInstance}" property="firstName"/>
            <f:display bean="${ticketInstance}" property="lastName"/>
            <f:display bean="${ticketInstance}" property="phone"/>
            <f:display bean="${ticketInstance}" property="phoneHome"/>
            <f:display bean="${ticketInstance}" property="mobile"/>
            <f:display bean="${ticketInstance}" property="fax"/>
            <f:display bean="${ticketInstance}" property="email1"/>
            <f:display bean="${ticketInstance}" property="email2"/>
          </div>
          <f:display bean="${ticketInstance}" property="address"
            suppressHeader="true"/>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.history.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:render template="logEntries/logEntry"
              collection="${ticketInstance.logEntries.reverse()}"/>
          </div>
        </div>
      </section>
    </div>

    <div id="send-message-dialog" class="modal fade" tabindex="-1"
      role="dialog" aria-labelledby="send-message-dialog-title"
      aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"
              aria-label="${message(code: 'default.btn.close')}"
              ><span aria-hidden="true">×</span
            ></button>
            <h4 id="send-message-dialog-title" class="modal-title"
              ><g:message code="ticket.sendMessage.title"
            /></h4>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <g:uploadForm action="frontendSendMessage"
                id="${ticketInstance.code ?: ticketInstance.id}" method="post">
                <g:hiddenField name="helpdesk"
                  value="${helpdeskInstance.id}"/>
                <g:hiddenField name="accessCode"
                  value="${helpdeskInstance.accessCode}"/>
                <g:hiddenField name="returnUrl"
                  value="${createLink(
                    controller: 'ticket', action: 'frontendShow',
                    id: ticketInstance.code ?: ticketInstance.id,
                    params: [
                      helpdesk: helpdeskInstance.id,
                      accessCode: helpdeskInstance.accessCode
                    ]
                  )}"/>
                <f:field bean="${new TicketLogEntry()}" property="message"
                  required="true" orientation="vertical"
                  toolbar="markdown-help"/>
                <div class="form-group">
                  <label for="attachment"
                    ><g:message code="ticket.attachment.label"
                  /></label>
                  <input type="file" id="attachment" name="attachment"/>
                </div>
              </g:uploadForm>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary send-btn"
              ><g:message code="default.button.send.label"
            /></button>
            <button type="button" class="btn btn-default"
              data-dismiss="modal"
              ><g:message code="default.button.cancel.label"
            /></button>
          </div>
        </div>
      </div>
    </div>

    <content tag="scripts">
      <asset:javascript src="helpdesk-frontend"/>
    </content>
  </body>
</html>
