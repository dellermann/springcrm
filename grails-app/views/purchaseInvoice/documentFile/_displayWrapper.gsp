<g:applyLayout name="fieldDisplay">
  <g:if test="${bean?.documentFile}">
  <g:link controller="dataFile" action="loadFile" id="${bean.documentFile.id}"
    params="[type: 'purchaseInvoice']" elementId="document" target="_blank"
    download="${bean.documentFile.fileName}"
    >${value}</g:link>
  </g:if>
</g:applyLayout>
