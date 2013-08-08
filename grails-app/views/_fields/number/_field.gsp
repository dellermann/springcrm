<g:applyLayout name="field">
  <g:if test="${constraints.widget == 'currency'}">
    <div class="field-text">
      <span class="input">
        <f:input bean="${bean}" property="${property}"
          value="${formatNumber(number: value, minFractionDigits: 2)}"
          size="${size ?: 10}" class="number" />
      </span>
      <span class="currency-symbol"><g:currency /></span>
    </div>
  </g:if>
  <g:elseif test="${constraints.widget == 'percent'}">
    <div class="field-text">
      <span class="input">
        <f:input bean="${bean}" property="${property}"
          value="${formatNumber(number: value, minFractionDigits: 2)}"
          size="${size ?: 10}" class="number" />
      </span>
      <span class="percent-sign">%</span>
    </div>
  </g:elseif>
  <g:else>
    <f:input bean="${bean}" property="${property}"
      value="${fieldValue(bean: bean, field: property)}"
      size="${size ?: 10}" />
  </g:else>
</g:applyLayout>
