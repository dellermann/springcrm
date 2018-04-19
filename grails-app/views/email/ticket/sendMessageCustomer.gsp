<g:message code="email.ticket.salutation.customer"
  args="[ticketInstance.fullName]" />

<g:message code="email.ticket.sendMessage.customer.text1"
  args="[fullNumber(bean: ticketInstance), ticketInstance.subject]" />

> [<g:message code="email.ticket.link.show" />](${showLink})
> [<g:message code="email.ticket.link.list" />](${overviewLink})

* * *

# <g:fullNumber bean="${ticketInstance}"/> – ${ticketInstance.subject}

## <g:message code="email.ticket.messageData" />

<g:message code="ticket.sender.label" />
:   ${sender.fullName}

## <g:message code="ticket.messageText.label" />

${messageText}

<g:applyLayout name="emailTicketData" />
