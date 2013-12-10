<g:if test="${'currency' == constraints?.widget}">
  <g:set var="val"
    value="${formatCurrency(number: value, numberOnly: true, displayZero: displayZero, external: external)}" />
</g:if>
<g:else>
  <g:set var="val"
    value="${formatNumber(number: value, maxFractionDigits: 10)}" />
</g:else>
<g:field type="text" name="${property}" value="${val}"
  size="${size ?: 10}" required="${required ? 'required' : ''}"
  class="${cssClass}" />
