<div class="auto-number">
  <div class="input-group">
    <g:if test="${prefix}">
      <span class="input-group-addon">${prefix}-</span>
    </g:if>
    <input type="number" name="number" id="number" class="form-control"
      value="${value}" size="10"${raw(checked ? ' disabled="disabled"' : '')}/>
    <g:if test="${suffix}">
      <span class="input-group-addon">-${suffix}</span>
    </g:if>
  </div>
  <div class="checkbox">
    <label class="checkbox-inline">
      <g:checkBox name="autoNumber" checked="${checked}"
        aria-controls="number"/>
      <g:message code="default.number.auto.label"/>
    </label>
  </div>
</div>
