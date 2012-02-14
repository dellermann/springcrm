<g:applyLayout name="field">
  <g:hiddenField name="fileRemove" value="0" />
  <input type="file" name="file" /><br />
  <g:if test="${purchaseInvoiceInstance?.documentFile}">
  <div class="document-preview">
    <a id="document" href="${createLink(action: 'getDocument', id: purchaseInvoiceInstance?.id)}" target="_blank">${purchaseInvoiceInstance?.documentFile}</a>
  </div>
  <ul class="document-preview-links">
    <li class="document-delete"><g:message code="purchaseInvoice.documentFile.delete" /></li>
  </ul>
  </g:if>
</g:applyLayout>