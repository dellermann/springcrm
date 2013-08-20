<g:message code="email.ticket.salutation.user"
  args="[ticketInstance.assignedUser.fullName]" />

<g:message code="email.ticket.assignedUser.text1" args="[creator.fullName]" />

* * *

# ${ticketInstance.fullNumber} – ${ticketInstance.subject}

## <g:message code="email.ticket.userData" />

<g:message code="ticket.assignedUser.old" />
:   [${creator.fullName}](mailto:${creator.email})

## <g:message code="email.ticket.ticketData" />

<g:message code="ticket.dateCreated.label" />
:   <g:formatDate date="${ticketInstance.dateCreated}" type="datetime" style="MEDIUM" />
<g:if test="${ticketInstance.priority}">
<g:message code="ticket.priority.label" />
:   ${ticketInstance.priority.name}</g:if>

## <g:message code="email.ticket.customerData" />

<g:message code="ticket.name.label" />
:   ${ticketInstance.fullName}
<g:if test="${ticketInstance.address}">
<g:message code="ticket.address.label" />
:   ${ticketInstance.address}
</g:if><g:if test="${ticketInstance.phone}">
<g:message code="ticket.phone.label" />
:   ${ticketInstance.phone}
</g:if><g:if test="${ticketInstance.phoneHome}">
<g:message code="ticket.phoneHome.label" />
:   ${ticketInstance.phoneHome}
</g:if><g:if test="${ticketInstance.mobile}">
<g:message code="ticket.mobile.label" />
:   ${ticketInstance.mobile}
</g:if><g:if test="${ticketInstance.fax}">
<g:message code="ticket.fax.label" />
:   ${ticketInstance.fax}
</g:if><g:if test="${ticketInstance.email1}">
<g:message code="ticket.email1.label" />
:   ${ticketInstance.email1}
</g:if><g:if test="${ticketInstance.email2}">
<g:message code="ticket.email2.label" />
:   ${ticketInstance.email2}
</g:if>

## <g:message code="ticket.messageText.label" />

${ticket.initialMessage}

* * *

<g:message code="email.ticket.noReplyHint" />

