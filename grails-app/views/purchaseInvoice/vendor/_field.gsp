<div class="row">
  <div class="label">
    <label for="vendorName"><g:message code="purchaseInvoice.vendor.label" default="Vendor" /></label>
  </div>
  <div class="field${hasErrors(bean: bean, field: 'vendorName', ' error')}">
    <g:textField name="vendorName" value="${bean?.vendorName}" />
    <g:hiddenField name="vendor.id" value="${bean?.vendor?.id}" />
    <ul class="field-msgs">
      <li class="info-msg">
        <g:message code="default.required" default="required" />
      </li>
      <g:eachError bean="${bean}" field="vendorName">
      <li class="error-msg"><g:message error="${it}" /></li>
      </g:eachError>
    </ul>
  </div>
</div>
