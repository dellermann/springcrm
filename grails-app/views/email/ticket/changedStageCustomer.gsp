<g:message code="email.ticket.salutation.customer"
  args="[ticketInstance.fullName]" />

<g:message code="email.ticket.changedStage.customer.text1"
  args='[
    message(code: "ticket.stage.${oldStage}"),
    message(code: "ticket.stage.${ticketInstance.stage}")
  ]'/>

> [<g:message code="email.ticket.link.show" />](${showLink})

* * *

# ${ticketInstance.shortCode} – ${ticketInstance.subject}

<g:applyLayout name="emailTicketData" />
