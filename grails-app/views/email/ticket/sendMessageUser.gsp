<g:message code="email.ticket.salutation.users" />

<g:message code="email.ticket.sendMessage.user.text1"
  args="[ticketInstance.fullNumber, ticketInstance.subject]" />

* * *

# ${ticketInstance.fullNumber} – ${ticketInstance.subject}

## <g:message code="email.ticket.messageData" />

<g:message code="ticket.sender.label" />
:   <g:if test="${sender}">[${sender.fullName}](mailto:${sender.email})</g:if><g:else><g:message code="ticket.customer.label" /></g:else>

## <g:message code="ticket.messageText.label" />

${messageText}

<g:applyLayout name="emailTicketData" />
