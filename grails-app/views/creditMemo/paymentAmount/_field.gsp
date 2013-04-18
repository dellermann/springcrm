<g:applyLayout name="field">
  <f:input bean="${bean}" property="${property}" value="${formatCurrency(number: value, numberOnly: true)}" size="${size ?: 10}" class="currency" />&nbsp;<g:currency />,
  <a id="still-unpaid" href="#" data-modified-closing-balance="${bean.modifiedClosingBalance}"><g:message code="invoice.stillUnpaid.label" default="still unpaid" />:&nbsp;<output></output>&nbsp;<g:currency /></a><br />
</g:applyLayout>