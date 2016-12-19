<g:applyLayout name="field">
  <div class="row">
    <div class="col-xs-6 col-sm-12 col-md-6">
      <f:widget bean="${bean}" property="${property}"/>
    </div>
    <div class="col-xs-6 col-sm-12 col-md-6" style="padding-left: 0;">
      <button type="button" id="still-unpaid" class="btn btn-link still-unpaid"
        data-modified-closing-balance="0">
        <g:message code="invoice.stillUnpaid.label" />:
        <output></output>
        <g:currency />
      </button>
    </div>
  </div>
</g:applyLayout>
