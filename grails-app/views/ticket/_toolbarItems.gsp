<%@ page import="org.amcworld.springcrm.TicketStage" %>

<li class="dropdown-header" role="presentation">
  <i class="fa fa-tag"></i>
  <g:message code="ticket.changeStage.label" />
</li>
<g:if test="${
  (user.admin || user in users) &&
  ticketInstance.stage in [TicketStage.created, TicketStage.resubmitted]
}">
<li role="menuitem">
  <g:link action="takeOn" id="${ticketInstance?.id}" class="take-on-link">
    <i class="fa fa-check"></i>
    <g:message code="ticket.takeOn.label" />
  </g:link>
</li>
</g:if>
<g:if test="${(user.admin || user == ticketInstance.assignedUser)}">
<g:if test="${ticketInstance.stage == TicketStage.assigned}">
<li role="menuitem">
  <g:link action="changeStage" id="${ticketInstance?.id}"
    params="[stage: TicketStage.inProcess]"
    elementId="change-to-in-process-btn">
    <i class="fa fa-thumb-tack"></i>
    <g:message code="ticket.changeStage.inProcess" />
  </g:link>
</li>
</g:if>
<g:if test="${
  ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess]
}">
<li role="menuitem">
  <g:link action="changeStage" id="${ticketInstance?.id}"
    params="[stage: TicketStage.closed]" class="close-ticket-link">
    <i class="fa fa-check-circle-o"></i>
    <g:message code="ticket.changeStage.closed" />
  </g:link>
</li>
</g:if>
</g:if>
<g:if test="${ticketInstance.stage == TicketStage.closed}">
<li>
  <g:link action="changeStage" id="${ticketInstance?.id}"
    params="[stage: TicketStage.resubmitted]" elementId="resubmit-btn">
    <i class="fa fa-share-square-o"></i>
    <g:message code="ticket.changeStage.resubmitted" />
  </g:link>
</li>
</g:if>

<li class="dropdown-header" role="presentation">
  <i class="fa fa-envelope-o"></i>
  <g:message code="ticket.sendMessage.label" />
</li>
<g:if test="${
  user.admin ||
  (user == ticketInstance.assignedUser &&
  ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess])
}">
<li role="menuitem">
  <a href="#" class="send-message-to-customer-link"
    data-title="${message(code: 'ticket.sendMessage.toCustomer.title')}"
    data-submit-url="${createLink(action: 'sendMessage', id: ticketInstance.id)}">
    <g:message code="ticket.sendMessage.toCustomer.label" />
  </a>
</li>
</g:if>
<g:each in="${otherUsers}">
<li role="menuitem">
  <a href="#" class="send-message-to-user-link" data-user-id="${it.id}"
    data-title="${message(code: 'ticket.sendMessage.toUser.title')}"
    data-submit-url="${createLink(action: 'sendMessage', id: ticketInstance.id)}"
    >${it.toString()}</a
  >
</li>
</g:each>

<g:if test="${
  (user.admin || user == ticketInstance.assignedUser) &&
  ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess] &&
  otherUsers
}">
<li class="dropdown-header" role="presentation">
  <i class="fa fa-hand-o-right"></i>
  <g:message code="ticket.changeStage.assign" />
</li>
<g:each in="${user.admin ? users : otherUsers}">
<li>
  <g:link action="assignToUser" id="${ticketInstance.id}"
    params="[user: it.id]" class="assign-user-link"
    >${it.toString()}</g:link>
</li>
</g:each>
</g:if>

<g:if test="${
  (user.admin || user == ticketInstance.assignedUser) &&
  ticketInstance.stage == TicketStage.assigned
}">
<li role="menuitem" class="item-separated">
  <a href="#" class="create-note-link"
    data-title="${message(code: 'ticket.createNote.title')}"
    data-submit-url="${createLink(action: 'createNote', id: ticketInstance.id)}">
    <i class="fa fa-pencil"></i>
    <g:message code="ticket.createNote.label" />
  </a>
</li>
</g:if>
