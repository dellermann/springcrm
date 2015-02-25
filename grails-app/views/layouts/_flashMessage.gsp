<g:if test="${flash.message}">
<div class="alert alert-success alert-dismissible" role="alert">
  <button type="button" class="close" data-dismiss="alert">
    <span aria-hidden="true">×</span>
    <span class="sr-only"><g:message code="default.btn.close" /></span>
  </button>
  ${raw(flash.message)}
</div>
</g:if>
