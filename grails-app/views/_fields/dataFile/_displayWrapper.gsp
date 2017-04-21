<g:applyLayout name="fieldDisplay">
  <g:if test="${value}">
  <g:link controller="dataFile" action="loadFile" id="${value.id}"
    params="[type: bean.getClass().simpleName.uncapitalize()]"
    elementId="${property}" target="_blank" download="${value.fileName}"
    >${value}</g:link>
  </g:if>
</g:applyLayout>
