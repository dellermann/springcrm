<g:applyLayout name="fieldDisplay">
  <g:if test="${value}">
  <g:link controller="user" action="show" id="${bean.creator.id}"><g:fieldValue bean="${bean}" field="creator" /></g:link>
  </g:if>
  <g:else>
  <g:message code="ticket.creator.customer" />
  </g:else>
</g:applyLayout>