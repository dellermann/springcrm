<g:if test="${constraints?.widget == 'currency'}">
  <g:render template="/_fields/number/displayWidget-currency"/>
</g:if>
<g:elseif test="${value != null || !constraints.nullable}">
  <g:render template="/_fields/number/displayWidget-default"/>
</g:elseif>
