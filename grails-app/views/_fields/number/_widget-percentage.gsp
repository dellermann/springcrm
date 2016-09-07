<div class="input-group">
  <input type="text" id="${property}" name="${property}"
    value="${formatNumber(type: 'number', number: value, minFractionDigits: 1)}"
    class="form-control form-control-number form-control-percentage"
    size="${size ?: 10}" ${required ? raw('required="required"') : ''}
    aria-describedby="${property}-percent"
    ${constraints.nullable ? raw('data-allow-null="true"') : ''}/>
  <span id="${property}-percent" class="input-group-addon">%</span>
</div>
