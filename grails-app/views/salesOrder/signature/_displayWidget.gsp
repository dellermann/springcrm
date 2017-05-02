<g:form action="setSignature">
  <g:hiddenField name="id" value="${bean.id}"/>
  <g:hiddenField name="signature" value="${value}"/>
</g:form>
<p id="signature-canvas" style="display: none;">
  <img class="img-responsive"/>
</p>
<p id="no-signature-msg"><g:message code="salesOrder.signature.none"/></p>
<div>
  <button type="button" id="set-signature-btn" class="btn btn-primary">
    <i class="fa fa-pencil"></i>
    <g:message code="salesOrder.signature.btn"/>
  </button>
  <button type="button" id="delete-signature-btn" class="btn btn-danger"
    style="display: none;">
    <i class="fa fa-trash"></i>
    <g:message code="salesOrder.signature.btn.delete"/>
  </button>
</div>
