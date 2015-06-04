<section>
  <header>
    <h3><g:message code="ticket.fieldset.general.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${ticketInstance}" property="number" />
      <f:field bean="${ticketInstance}" property="helpdesk" size="1" />
      <f:field bean="${ticketInstance}" property="subject" />
    </div>
    <div class="column">
      <f:field bean="${ticketInstance}" property="priority" />
    </div>
  </div>
</section>
<section>
  <header>
    <h3><g:message code="ticket.fieldset.customerData.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <f:field bean="${ticketInstance}" property="salutation" />
      <f:field bean="${ticketInstance}" property="firstName" />
      <f:field bean="${ticketInstance}" property="lastName" />
      <f:field bean="${ticketInstance}" property="phone" />
      <f:field bean="${ticketInstance}" property="phoneHome" />
      <f:field bean="${ticketInstance}" property="mobile" />
      <f:field bean="${ticketInstance}" property="fax" />
      <f:field bean="${ticketInstance}" property="email1" />
      <f:field bean="${ticketInstance}" property="email2" />
    </div>
    <div class="column">
      <f:field bean="${ticketInstance}" property="address" />
    </div>
  </div>
</section>
<g:unless test="${ticketInstance.id}">
<section>
  <header>
    <h3><g:message code="ticket.fieldset.message.label" /></h3>
  </header>
  <div class="column-group">
    <div class="column">
      <div class="form-group">
        <label for="messageText" class="control-label">
          <g:message code="ticket.messageText.label" />
        </label>
        <div class="control-container">
          <g:textArea name="messageText" rows="7" class="form-control"
            required="required" value="${ticketInstance.messageText}" />
          <ul class="control-messages"
            ><li class="control-message-info"
              ><g:message code="default.required"
            /></li
            ><g:eachError bean="${ticketInstance}" field="messageText"
            ><li class="control-message-error"><g:message error="${it}" /></li
            ></g:eachError
          ></ul>
        </div>
      </div>
      <div class="form-group">
        <label for="attachment" class="control-label">
          <g:message code="ticket.attachment.label" />
        </label>
        <div class="control-container">
          <input type="file" id="attachment" name="attachment" />
        </div>
      </div>
    </div>
  </div>
</section>
</g:unless>
