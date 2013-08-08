<g:applyLayout name="field">
  <div class="field-text">
    <span class="input">
      <f:input bean="${bean}" property="${property}"
        value="${formatNumber(number: value, minFractionDigits: 2)}"
        size="${size ?: 10}" />
    </span>
    <span class="currency-symbol"><g:currency />,</span>
    <span id="still-unpaid"
      data-modified-closing-balance="${bean.modifiedClosingBalance}">
      <g:message code="invoice.stillUnpaid.label" default="still unpaid" />:
      <output></output>
      <g:currency />
    </span>
  </div>
</g:applyLayout>
