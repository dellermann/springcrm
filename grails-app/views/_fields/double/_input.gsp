<g:if test="${'currency' == constraints?.widget}">
<g:set var="val" value="${formatCurrency(number: value, numberOnly: true)}" />
</g:if>
<g:else>
<g:set var="val" value="${formatNumber(number: value)}" />
</g:else>
<g:field type="text" name="${property}" value="${val}" size="${size ?: 10}"
  required="${required ? 'required' : ''}" class="${cssClass}" />
