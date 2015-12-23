<g:applyLayout name="fieldDisplay">
  <g:if test="${'currency' == constraints?.widget}">
  <g:formatCurrency number="${bean?."${property}"}" />
  </g:if>
  <g:else>
  <f:displayWidget bean="${bean}" property="${property}" />
  </g:else>
</g:applyLayout>