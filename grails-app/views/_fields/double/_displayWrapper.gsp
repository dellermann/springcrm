<g:applyLayout name="fieldDisplay">
  <g:if test="${'currency' == constraints?.widget}">
  <g:formatCurrency number="${bean?."${property}"}" />
  </g:if>
  <g:else>
  ${value}
  </g:else>
</g:applyLayout>