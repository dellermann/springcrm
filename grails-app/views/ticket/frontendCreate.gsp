<html>
  <head>
    <meta name="layout" content="frontend"/>
    <title><g:message code="ticket.create.title"/></title>
    <meta name="caption" content="${message(code: 'ticket.create.title')}"/>
    <meta name="stylesheet" content="helpdesk-frontend"/>
  </head>

  <body>
    <content tag="toolbar">
      <button type="submit" form="ticket-form" class="btn btn-success">
        <i class="fa fa-save"></i>
        <g:message code="default.button.save.label"/>
      </button>
      <g:unless test="${helpdeskInstance.forEndUsers}">
        <g:button mapping="helpdeskFrontend" params="[
            urlName: helpdeskInstance.urlName,
            accessCode: helpdeskInstance.accessCode
          ]"
          color="danger" icon="close" class="hidden-xs"
          message="default.button.cancel.label"/>
      </g:unless>
    </content>

    <form id="ticket-form"
      action="${createLink(controller: 'ticket', action: 'frontendSave')}"
      method="post" class="form-horizontal data-form form-view"
      enctype="multipart/form-data">
      <g:hiddenField name="helpdesk" value="${helpdeskInstance.id}"/>
      <g:hiddenField name="accessCode" value="${helpdeskInstance.accessCode}"/>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.general.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <f:field bean="${ticketInstance}" property="subject"/>
            <f:field bean="${ticketInstance}" property="priority"/>
            <f:field bean="${ticketInstance}" property="salutation"/>
            <f:field bean="${ticketInstance}" property="firstName"/>
            <f:field bean="${ticketInstance}" property="lastName"/>
            <f:field bean="${ticketInstance}" property="phone"/>
            <f:field bean="${ticketInstance}" property="mobile"/>
            <f:field bean="${ticketInstance}" property="email1"/>
          </div>
          <div class="column">
            <div class="no-textarea-toolbar">
              <f:field bean="${ticketInstance}" property="address"/>
            </div>
          </div>
        </div>
      </section>
      <section>
        <header>
          <h3><g:message code="ticket.fieldset.message.label"/></h3>
        </header>
        <div class="column-group">
          <div class="column">
            <div class="form-group">
              <label for="messageText" class="control-label">
                <g:message code="ticket.messageText.label"/>
              </label>
              <div class="control-container">
                <g:textArea name="messageText" rows="7" class="form-control"
                  required="required" value="${ticketInstance.messageText}"/>
                <ul class="control-messages"
                  ><li class="control-message-info"
                    ><g:message code="default.required"
                  /></li
                  ><g:eachError bean="${ticketInstance}" field="messageText"
                  ><li class="control-message-error"><g:message error="${it}"/></li
                  ></g:eachError
                ></ul>
              </div>
            </div>
            <div class="form-group">
              <label for="attachment" class="control-label">
                <g:message code="ticket.attachment.label"/>
              </label>
              <div class="control-container">
                <input type="file" id="attachment" name="attachment"/>
              </div>
            </div>
          </div>
        </div>
      </section>
    </form>

    <content tag="scripts">
      <asset:javascript src="helpdesk-frontend"/>
    </content>
  </body>
</html>
