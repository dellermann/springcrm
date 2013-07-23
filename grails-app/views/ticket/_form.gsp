<fieldset>
  <h4><g:message code="ticket.fieldset.general.label" /></h4>
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
  <h4><g:message code="ticket.fieldset.customerData.label" /></h4>
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
<g:set var="ticketMessageInstance" value="${ticketInstance.messages.first()}" />
<fieldset>
  <h4><g:message code="ticket.fieldset.message.label" /></h4>
  <div class="fieldset-content">
    <f:field bean="${ticketMessageInstance}" property="message" cols="100" rows="10" />
    <f:field bean="${ticketMessageInstance}" property="attachment" />
  </div>
</fieldset>
