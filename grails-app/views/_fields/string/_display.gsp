<g:applyLayout name="fieldDisplay">
  <g:if test="${'textarea' == constraints?.widget}">${nl2br(value: value)}</g:if>
  <g:elseif test="${('url' == constraints?.widget) && value}"><a href="${value}" target="_blank">${value}</a></g:elseif>
  <g:elseif test="${constraints?.email && value}"><a href="mailto:${value}">${value}</a></g:elseif>
  <g:else>${value}</g:else>
</g:applyLayout>