<fieldset>
  <header><h3><g:message code="ticket.fieldset.general.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
        <f:field bean="${ticketInstance}" property="number" />
        <f:field bean="${ticketInstance}" property="helpdesk" size="1" />
        <f:field bean="${ticketInstance}" property="subject" />
      </div>
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${ticketInstance}" property="priority" />
      </div>
    </div>
  </div>
</fieldset>
<fieldset>
  <header><h3><g:message code="ticket.fieldset.customerData.label" /></h3></header>
  <div class="multicol-content">
    <div class="col col-l">
      <div class="form">
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
    </div>
    <div class="col col-r">
      <div class="form">
        <f:field bean="${ticketInstance}" property="street" cols="35" rows="3" />
        <f:field bean="${ticketInstance}" property="postalCode" />
        <f:field bean="${ticketInstance}" property="location" />
        <f:field bean="${ticketInstance}" property="state" />
        <f:field bean="${ticketInstance}" property="country" />
      </div>
    </div>
  </div>
</fieldset>
<g:unless test="${ticketInstance.id}">
<fieldset>
  <header><h3><g:message code="ticket.fieldset.message.label" /></h3></header>
  <div class="form-fragment">
    <div class="row">
      <div class="label">
        <label for="messageText"><g:message code="ticket.messageText.label" /></label>
      </div>
      <div class="field${hasErrors(bean: ticketInstance, field: 'messageText', ' error')}">
        <g:textArea name="messageText" cols="100" rows="10" required="required"
          value="${ticketInstance.messageText}" />
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
      <div class="field">
        <input type="file" id="attachment" name="attachment" />
      </div>
    </div>
  </div>
</fieldset>
</g:unless>
