<div class="form-group">
  <label for="termOfPayment" class="control-label">
    <g:message code="organization.termOfPayment.label" />
  </label>
  <div class="control-container">
    <div class="input-group">
      <g:field type="number" name="termOfPayment" value="${value}"
        class="form-control" min="0" aria-describedby="termOfPayment-unit" />
      <span id="termOfPayment-unit" class="input-group-addon">
        <g:message code="organization.termOfPayment.unit" />
      </span>
    </div>
    <ul class="control-messages"
      ><g:if test="${required}"
      ><li class="control-message-info"
        ><g:message code="default.required"
      /></li
      ></g:if
      ><g:pageProperty name="page.fieldMessages"
      /><g:eachError bean="${bean}" field="termOfPayment"
      ><li class="control-message-error"><g:message error="${it}" /></li
      ></g:eachError
    ></ul>
  </div>
</div>
