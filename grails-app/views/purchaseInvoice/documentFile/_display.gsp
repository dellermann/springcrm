<g:applyLayout name="fieldDisplay">
  <g:if test="${bean?.documentFile}">
  <g:link action="getDocument" id="${bean?.id}" elementId="document" target="_blank">${value}</g:link>
  </g:if>
</g:applyLayout>