<g:if test="${constraints?.widget == 'autonumber'}">
  <g:render template="/_fields/int/widget-autonumber"/>
</g:if>
<g:else>
  <g:render template="/_fields/int/widget-default"/>
</g:else>
