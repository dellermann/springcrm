<g:applyLayout name="field">
  <g:hiddenField name="fileRemove" value="0" />
  <input type="file" name="file" />
  <g:if test="${purchaseInvoiceInstance?.documentFile}">
  <div class="document-preview">
    <g:link controller="dataFile" action="loadFile"
      id="${purchaseInvoiceInstance.documentFile.id}"
      params="[type: 'purchaseInvoice']" elementId="document" target="_blank"
      ><g:fieldValue bean="${purchaseInvoiceInstance}" field="documentFile" /></g:link>
  </div>
  <ul class="document-preview-links">
    <li class="document-delete">
      <g:button color="red" size="small" icon="trash-o"
        message="purchaseInvoice.documentFile.delete" />
    </li>
  </ul>
  </g:if>
</g:applyLayout>
