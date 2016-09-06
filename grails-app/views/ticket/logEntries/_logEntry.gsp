<%@ page import="org.amcworld.springcrm.TicketLogAction" %>

<g:unless test="${actionName == 'frontendShow' && it.action == TicketLogAction.note}">
  <section class="ticket-log-entry ticket-log-entry-action-${it.action}">
    <h4><g:formatDate date="${it.dateCreated}" /> –
    <g:message message="${it}" /></h4>

    <div class="row">
      <div class="ticket-log-entry-label">
        <g:message code="ticket.creator.label" />
      </div>
      <div class="ticket-log-entry-value">
        <g:if test="${it.creator}">
        <g:fieldValue bean="${it}" field="creator" />
        </g:if>
        <g:else>
        <g:message code="ticket.creator.customer" />
        </g:else>
      </div>
    </div>
    <g:render template="logEntries/${it.action}" model="[logEntry: it]" />
  </section>
</g:unless>
