<%@ page import="org.amcworld.springcrm.Helpdesk" %>
<%@ page import="org.amcworld.springcrm.Ticket" %>
<%@ page import="org.amcworld.springcrm.TicketLogAction" %>
<%@ page import="org.amcworld.springcrm.TicketLogEntry" %>
<%@ page import="org.amcworld.springcrm.TicketStage" %>

<html>
  <head>
    <meta name="layout" content="frontend"/>
    <title>${ticket}</title>
    <meta name="stylesheet" content="helpdesk-frontend"/>
    <g:set var="entityName" value="${message(code: 'ticket.label')}"/>
  </head>

  <body>
    <content tag="toolbar">
      <g:unless test="${helpdesk.forEndUsers}">
        <g:button mapping="helpdeskFrontend"
          params="[urlName: helpdesk.urlName, accessCode: helpdesk.accessCode]"
          color="default" class="hidden-xs" icon="arrow-left"
          message="default.button.back.label"/>
      </g:unless>
      <button type="button"
        class="btn btn-default hidden-xs hidden-sm send-message-link">
        <i class="fa fa-envelope-o"></i>
        <g:message code="ticket.sendMessage.title"/>
      </button>
    </content>
    <g:if test="${
      ticket.stage in [TicketStage.assigned, TicketStage.inProcess, TicketStage.closed]
    }">
    <content tag="actionMenu">
      <li class="visible-xs visible-sm" role="menuitem">
        <a href="#" class="send-message-link">
          <i class="fa fa-envelope-o"></i>
          <g:message code="ticket.sendMessage.title"/>
        </a>
      </li>
      <g:if test="${
        ticket.stage in [TicketStage.assigned, TicketStage.inProcess]
      }">
      <li role="menuitem">
        <g:link action="frontendCloseTicket" id="${ticket.id}"
          params="[helpdesk: helpdesk.id, accessCode: helpdesk.accessCode]"
          class="close-ticket-link">
          <i class="fa fa-check-circle-o"></i>
          <g:message code="ticket.close.label"/>
        </g:link>
      </li>
      </g:if>
      <g:if test="${ticket.stage == TicketStage.closed}">
      <li>
        <g:link action="frontendResubmitTicket" id="${ticket.id}"
          params="[helpdesk: helpdesk.id, accessCode: helpdesk.accessCode]">
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
            <f:display bean="${ticket}" property="number">
              <g:fullNumber bean="${ticket}"/>
            </f:display>
            <f:display bean="${ticket}" property="subject"/>
            <f:display bean="${ticket}" property="stage"/>
          </div>
          <div class="column">
            <f:display bean="${ticket}" property="priority"/>
            <f:display bean="${ticket}" property="creator"/>
            <f:display bean="${ticket}" property="assignedUser"/>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.customerData.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${ticket}" property="salutation"/>
            <f:display bean="${ticket}" property="firstName"/>
            <f:display bean="${ticket}" property="lastName"/>
            <f:display bean="${ticket}" property="phone"/>
            <f:display bean="${ticket}" property="phoneHome"/>
            <f:display bean="${ticket}" property="mobile"/>
            <f:display bean="${ticket}" property="fax"/>
            <f:display bean="${ticket}" property="email1"/>
            <f:display bean="${ticket}" property="email2"/>
          </div>
          <f:display bean="${ticket}" property="address" suppressHeader="true"/>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.history.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:render template="logEntries/logEntry"
              collection="${ticket.logEntries.reverse()}"/>
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
                id="${ticket.id}" method="post">
                <g:hiddenField name="helpdesk" value="${helpdesk.id}"/>
                <g:hiddenField name="accessCode"
                  value="${helpdesk.accessCode}"/>
                <g:hiddenField name="returnUrl" value="${url()}"/>
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
