<g:message code="email.ticket.salutation.customer"
  args="[ticketInstance.fullName]" />

<g:message code="email.ticket.assigned.text1" />

* * *

# <g:fullNumber bean="${ticketInstance}"/> – ${ticketInstance.subject}

<g:applyLayout name="emailTicketData" />
