<g:message code="email.ticket.salutation.customer"
  args="[ticketInstance.fullName]" />

<g:message code="email.ticket.closed.customer.text1" />

> [<g:message code="email.ticket.link.show" />](${showLink})
> [<g:message code="email.ticket.link.list" />](${overviewLink})

* * *

# ${ticketInstance.fullNumber} – ${ticketInstance.subject}

<g:applyLayout name="emailTicketData" />
