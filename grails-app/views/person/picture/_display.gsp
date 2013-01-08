<g:applyLayout name="fieldDisplay">
  <g:if test="${value}">
  <g:link action="getPicture" id="${bean?.id}" elementId="picture"><img src="${createLink(action: 'getPicture', id: bean?.id)}" alt="${bean?.toString()}" title="${bean?.toString()}" height="100" /></g:link>
  </g:if>
</g:applyLayout>