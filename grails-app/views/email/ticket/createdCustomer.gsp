<g:message code="email.ticket.salutation.customer"
  args="[ticketInstance.fullName]" />

<g:message code="email.ticket.created.customer.text1"
  args="[ticketInstance.helpdesk.name]" />

> [<g:message code="email.ticket.link.show" />](${showLink})
<g:if test="ticketInstance.helpdesk.forEndUsers">> [<g:message code="email.ticket.link.create" />](${overviewLink})</g:if
><g:else>> [<g:message code="email.ticket.link.list" />](${overviewLink})</g:else>

* * *

# <g:fullNumber bean="${ticketInstance}"/> – ${ticketInstance.subject}

<g:applyLayout name="emailTicketData" />
