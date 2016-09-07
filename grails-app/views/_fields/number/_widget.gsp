<g:if test="${constraints?.widget == 'currency'}">
  <g:render template="/_fields/number/widget-currency"/>
</g:if>
<g:elseif test="${constraints?.widget == 'percent'}">
  <g:render template="/_fields/number/widget-percentage"/>
</g:elseif>
<g:elseif test="${constraints.range}">
  <g:render template="/_fields/number/widget-range"/>
</g:elseif>
<g:else>
  <g:render template="/_fields/number/widget-default"/>
</g:else>
