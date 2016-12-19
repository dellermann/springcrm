<div class="input-group">
  <input type="text" id="${property}" name="${property}"
    value="${formatCurrency(
      number: value, numberOnly: true, displayZero: true, external: true
    )}"
    class="form-control form-control-number form-control-currency"
    size="${size ?: 10}" ${required ? raw('required="required"') : ''}
    aria-describedby="${property}-currency"
    ${constraints.nullable ? raw('data-allow-null="true"') : ''}/>
  <span id="${property}-currency" class="input-group-addon"
    ><g:currency
  /></span>
</div>
