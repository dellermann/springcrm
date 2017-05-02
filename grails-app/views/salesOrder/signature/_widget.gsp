<g:hiddenField name="signature" value="${value}"/>
<p id="signature-canvas" class="form-control-static" style="display: none;">
  <img class="img-responsive"/>
</p>
<p id="no-signature-msg" class="form-control-static"
  ><g:message code="salesOrder.signature.none"
/></p>
<p>
  <button type="button" id="set-signature-btn" class="btn btn-primary">
    <i class="fa fa-pencil"></i>
    <g:message code="salesOrder.signature.btn"/>
  </button>
  <button type="button" id="delete-signature-btn" class="btn btn-danger"
    style="display: none;">
    <i class="fa fa-trash"></i>
    <g:message code="salesOrder.signature.btn.delete"/>
  </button>
</p>
