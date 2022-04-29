<g:if test="${constraints?.widget == 'autonumber'}">
  <g:render template="/_fields/integer/displayWidget-autonumber"/>
</g:if>
<g:else>
  <g:render template="/_fields/integer/displayWidget-default"/>
</g:else>
