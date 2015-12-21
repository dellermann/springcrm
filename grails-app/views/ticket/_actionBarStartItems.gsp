<%@ page import="org.amcworld.springcrm.TicketStage" %>

<div class="btn-group">
  <button type="button" class="btn btn-default dropdown-toggle"
    data-toggle="dropdown" aria-haspopup="true" aria-owns="message-menu"
    ><i class="fa fa-envelope-o"></i>
    <g:message code="ticket.sendMessage.label" /> <span class="caret"></span>
  </button>
  <ul id="message-menu" class="dropdown-menu" role="menu"
    aria-expanded="false">
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
    <g:if test="${otherUsers}">
    <li class="dropdown-header" role="presentation">
      <g:message code="ticket.sendMessage.toUser.label" />
    </li>
    <g:each in="${otherUsers}">
    <li role="menuitem">
      <a href="#" class="send-message-to-user-link" data-user-id="${it.id}"
        data-title="${message(code: 'ticket.sendMessage.toUser.title')}"
        data-submit-url="${createLink(action: 'sendMessage', id: ticketInstance.id)}"
        >${it.toString()}</a>
    </li>
    </g:each>
    </g:if>
  </ul>
</div>

<g:if test="${
  (user.admin || user == ticketInstance.assignedUser) &&
  ticketInstance.stage in [TicketStage.assigned, TicketStage.inProcess] &&
  otherUsers
}">
<div class="btn-group visible-lg-inline-block">
  <button type="button" class="btn btn-default dropdown-toggle"
    data-toggle="dropdown" aria-haspopup="true" aria-owns="assign-user-menu"
    ><i class="fa fa-hand-o-right"></i>
    <g:message code="ticket.changeStage.assign" /> <span class="caret"></span>
  </button>
  <ul id="assign-user-menu" class="dropdown-menu" role="menu"
    aria-expanded="false">
    <g:each in="${user.admin ? users : otherUsers}">
    <li>
      <g:link action="assignToUser" id="${ticketInstance.id}"
        params="[user: it.id]" class="assign-user-link"
        >${it.toString()}</g:link>
    </li>
    </g:each>
  </ul>
</div>
</g:if>
