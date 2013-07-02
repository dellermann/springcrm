<div class="row">
  <div class="label">
    <label for="vendorName"><g:message code="purchaseInvoice.vendor.label" default="Vendor" /></label>
  </div>
  <div class="field${hasErrors(bean: bean, field: 'vendorName', ' error')}">
    <g:textField name="vendorName" value="${bean?.vendorName}" size="35" />
    <g:hiddenField name="vendor.id" value="${bean?.vendor?.id}" /><br />
    <span class="info-msg"><g:message code="default.required" default="required" /></span>
    <g:hasErrors bean="${bean}" field="vendorName">
      <span class="error-msg"><g:eachError bean="${bean}" field="vendorName"><g:message error="${it}" /> </g:eachError></span>
    </g:hasErrors>
  </div>
</div>