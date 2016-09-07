<g:if test="${constraints?.widget == 'textarea'}"
  ><g:render template="/_fields/string/displayWidget-textarea"
/></g:if>
<g:elseif test="${constraints?.widget == 'url' && value}"
  ><g:render template="/_fields/string/displayWidget-url"
/></g:elseif>
<g:elseif test="${constraints?.email && value}"
  ><g:render template="/_fields/string/displayWidget-email"
/></g:elseif>
<g:else
  ><g:render template="/_fields/string/displayWidget-default"
/></g:else>
