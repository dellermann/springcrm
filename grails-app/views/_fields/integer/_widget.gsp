<g:if test="${constraints?.widget == 'autonumber'}">
  <g:render template="/_fields/integer/widget-autonumber"/>
</g:if>
<g:else>
  <g:render template="/_fields/integer/widget-default"/>
</g:else>
