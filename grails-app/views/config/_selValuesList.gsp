<div class="form-group">
  <label class="control-label">
    <g:message code="config.selValues.${type}.label" />
  </label>
  <div class="control-container">
    <div class="sel-values-list-container" data-list-type="${type}"
      data-load-url="${createLink(action: 'loadSelValues', params: [type: type])}">
      <div class="sel-values-list"></div>
      <input type="hidden" name="selValues.${type}" />
    </div>
  </div>
</div>
