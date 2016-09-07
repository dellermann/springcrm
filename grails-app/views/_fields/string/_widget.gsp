<g:if test="${constraints?.widget == 'textarea'}">
  <g:render template="/_fields/string/widget-textarea"/>
</g:if>
<g:elseif test="${constraints?.password}">
  <g:render template="/_fields/string/widget-password"/>
</g:elseif>
<g:else>
  <g:render template="/_fields/string/widget-default"/>
</g:else>
