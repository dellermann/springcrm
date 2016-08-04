<g:if test="${'currency' == constraints?.widget}">
  <g:formatCurrency number="${value}"/>
</g:if>
<g:elseif test="${value != null || !constraints.nullable}">
  <g:formatNumber number="${value}" groupingUsed="true"
    maxFractionDigits="10"/>
</g:elseif>
