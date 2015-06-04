<%@ page import="org.amcworld.springcrm.Ticket" %>
<%@ page import="org.amcworld.springcrm.TicketLogAction" %>
<%@ page import="org.amcworld.springcrm.TicketStage" %>

<html>
  <head>
    <meta name="layout" content="main" />
    <meta name="stylesheet" content="helpdesk" />
    <g:set var="user" value="${session.user}" />
    <g:set var="users" value="${ticketInstance.helpdesk.users}" />
    <g:set var="otherUsers" value="${ticketInstance.helpdesk.users - user}" />
  </head>

  <body>
    <g:applyLayout name="show" model="[instance: ticketInstance]">
      <content tag="toolbarItems">
        <g:render template="toolbarItems" />
      </content>
      <content tag="actionBarStart">
        <g:render template="actionBarStartItems" />
      </content>
      <content tag="actionMenu">
        <g:render template="actionMenuItems" />
      </content>

      <section>
        <header>
          <h3><g:message code="ticket.fieldset.general.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:display bean="${ticketInstance}" property="number">
              <g:fieldValue bean="${ticketInstance}" field="fullNumber" />
            </f:display>
            <f:display bean="${ticketInstance}" property="helpdesk" />
            <f:display bean="${ticketInstance}" property="subject" />
          </div>
          <div class="column">
            <f:display bean="${ticketInstance}" property="stage" />
            <f:display bean="${ticketInstance}" property="priority" />
            <f:display bean="${ticketInstance}" property="creator" />
            <f:display bean="${ticketInstance}" property="assignedUser" />
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.customerData.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
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
          <f:display bean="${ticketInstance}" property="address"
            suppressHeader="true" />
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.history.label" /></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <g:render template="logEntries/logEntry"
              collection="${ticketInstance.logEntries.reverse()}" />
          </div>
        </div>
      </section>

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
              <h4 id="send-message-dialog-title" class="modal-title">Modal title</h4>
            </div>
            <div class="modal-body">
              <div class="container-fluid">
                <g:uploadForm action="sendMessage" id="${ticketInstance.id}"
                  method="post">
                  <g:hiddenField name="recipient" value="" />
                  <div class="form-group">
                    <label for="messageText"
                      ><g:message code="ticket.messageText.label"
                    /></label>
                    <g:textArea name="messageText" class="form-control"
                      rows="10" required="required" />
                    <ul class="control-messages"
                      ><li class="control-message-info"
                        ><g:message code="default.required"
                      /></li
                    ></ul>
                  </div>
                  <div class="form-group">
                    <label for="attachment"
                      ><g:message code="ticket.attachment.label"
                    /></label>
                    <input type="file" id="attachment" name="attachment" />
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
    </g:applyLayout>

    <content tag="scripts">
      <asset:javascript src="ticket" />
    </content>
  </body>
</html>
