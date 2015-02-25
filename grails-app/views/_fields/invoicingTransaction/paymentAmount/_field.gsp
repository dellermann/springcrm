<g:applyLayout name="field">
  <div class="row">
    <div class="col-xs-6 col-sm-12 col-md-6">
      <div class="input-group">
        <f:input bean="${bean}" property="${property}"
          cssClass="form-control form-control-number form-control-currency"
          displayZero="true" external="true"
          aria-describedby="payment-amount-currency" />
        <span id="payment-amount-currency" class="input-group-addon"
          ><g:currency
        /></span>
      </div>
    </div>
    <div class="col-xs-6 col-sm-12 col-md-6" style="padding-left: 0;">
      <button type="button" id="still-unpaid" class="btn btn-link still-unpaid"
        data-modified-closing-balance="${bean.modifiedClosingBalance}">
        <g:message code="invoice.stillUnpaid.label" />:
        <output></output>
        <g:currency />
      </button>
    </div>
  </div>
</g:applyLayout>