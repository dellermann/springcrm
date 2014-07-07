<g:message code="email.ticket.salutation.user"
  args="[ticketInstance.assignedUser.fullName]" />

<g:message code="email.ticket.assignedUser.text1" args="[creator.fullName]" />

* * *

# ${ticketInstance.fullNumber} – ${ticketInstance.subject}

## <g:message code="email.ticket.userData" />

<g:message code="ticket.assignedUser.old" />
:   [${creator.fullName}](mailto:${creator.email})

<g:applyLayout name="emailTicketData" />
