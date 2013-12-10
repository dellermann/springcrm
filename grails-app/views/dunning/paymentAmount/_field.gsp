<g:applyLayout name="field">
  <div class="field-text">
    <span class="input">
      <f:input bean="${bean}" property="${property}"
        cssClass="number currency currency-ext" displayZero="true"
        external="true" />
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
