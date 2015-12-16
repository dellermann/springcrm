<g:applyLayout name="field">
  <g:if test="${'currency' == constraints?.widget}">
  <div class="input-group">
    <f:widget bean="${bean}" property="${property}" value="${formatCurrency(
        number: value, numberOnly: true, displayZero: true, external: true
      )}"
      cssClass="form-control form-control-number form-control-currency"
      aria-describedby="${property}-currency" />
    <span id="${property}-currency" class="input-group-addon"
      ><g:currency
    /></span>
  </div>
  </g:if>
  <g:elseif test="${'percent' == constraints?.widget}">
  <div class="input-group">
    <f:widget bean="${bean}" property="${property}" value="${formatNumber(
        type: 'number', number: value, minFractionDigits: 1
      )}"
      cssClass="form-control form-control-number form-control-percentage"
      aria-describedby="${property}-percent" />
    <span id="${property}-percent" class="input-group-addon">%</span>
  </div>
  </g:elseif>
  <g:else>
  <f:widget bean="${bean}" property="${property}" value="${
      formatNumber(type: 'number', number: value, maxFractionDigits: 10)
    }"
    cssClass="form-control form-control-number" />
  </g:else>
</g:applyLayout>
