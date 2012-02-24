<div class="row">
  <div class="label">
    <label for="vendorName"><g:message code="purchaseInvoice.vendor.label" default="Vendor" /></label>
  </div>
  <div class="field${hasErrors(bean: purchaseInvoiceInstance, field: 'vendor', ' error')}">
    <g:textField name="vendorName" value="${purchaseInvoiceInstance?.vendorName}" size="35" />
    <g:hiddenField name="vendor.id" value="${purchaseInvoiceInstance?.vendor?.id}" /><br />
    <g:hasErrors bean="${purchaseInvoiceInstance}" field="vendorName">
      <span class="error-msg"><g:eachError bean="${purchaseInvoiceInstance}" field="vendorName"><g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
  </div>
</div>