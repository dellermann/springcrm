<g:applyLayout name="field">
  <f:input bean="${bean}" property="${property}" value="${formatNumber(number: value, minFractionDigits: 2)}" size="${size ?: 10}" />&nbsp;<g:currency />,
  <a id="still-unpaid" href="#"><g:message code="invoice.stillUnpaid.label" default="still unpaid" />:&nbsp;<span></span>&nbsp;<g:currency /></a><br />
</g:applyLayout>