<div class="html-content"
  ><g:if test="${constraints?.attributes?.nl2br}"
    ><g:nl2br value="${value}"
  /></g:if><g:else
    ><markdown:renderHtml text="${raw(value)}"
  /></g:else
></div>