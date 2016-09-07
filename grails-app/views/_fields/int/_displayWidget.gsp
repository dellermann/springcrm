<g:if test="${constraints?.widget == 'autonumber'}">
  <g:render template="/_fields/int/displayWidget-autonumber"/>
</g:if>
<g:else>
  <g:render template="/_fields/int/displayWidget-default"/>
</g:else>
