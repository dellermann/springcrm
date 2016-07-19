<g:if test="${'textarea' == constraints?.widget}"
  ><div class="html-content"
    ><g:if test="${constraints?.attributes?.nl2br}"
      ><g:nl2br value="${value}"
    /></g:if><g:else
      ><markdown:renderHtml text="${raw(value)}"
    /></g:else
  ></div
></g:if>
<g:elseif test="${('url' == constraints?.widget) && value}"
  ><a href="${value}" target="_blank">${value}</a
></g:elseif>
<g:elseif test="${constraints?.email && value}"
  ><a href="mailto:${value}">${value}</a
></g:elseif>
<g:else>${value}</g:else>
