<div id="signature-dialog" class="modal fade" tabindex="-1"
  role="dialog" aria-labelledby="signature-dialog-title" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"
          aria-label="${message(code: 'default.btn.close')}"
          ><span aria-hidden="true">×</span
        ></button>
        <h4 id="signature-dialog-title" class="modal-title"
          ><g:message code="salesOrder.signature.title"
        /></h4>
      </div>
      <div class="modal-body">
        <p><g:message code="salesOrder.signature.description"/></p>
        <canvas></canvas>
      </div>
      <div class="modal-footer">
        <div class="row">
          <div class="col-xs-6 text-left">
            <button type="button" class="btn btn-danger clear-btn">
              <i class="fa fa-eraser"></i>
              <g:message code="salesOrder.signature.btn.clear"/>
            </button>
          </div>
          <div class="col-xs-6 text-right">
            <button type="button" class="btn btn-primary ok-btn"
              ><g:message code="default.button.ok.label"
            /></button>
            <button type="button" class="btn btn-default" data-dismiss="modal"
              ><g:message code="default.button.cancel.label"
            /></button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
