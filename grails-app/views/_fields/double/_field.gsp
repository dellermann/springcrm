<g:applyLayout name="field">
  <g:if test="${'currency' == constraints?.widget}">
    <div class="field-text">
      <span class="input">
        <f:input bean="${bean}" property="${property}" class="number" />
      </span>
      <span class="currency-symbol"><g:currency /></span>
    </div>
  </g:if>
  <g:elseif test="${'percent' == constraints?.widget}">
    <div class="field-text">
      <span class="input">
        <f:input bean="${bean}" property="${property}" class="number" />
      </span>
      <span class="percent-sign">%</span>
    </div>
  </g:elseif>
  <g:else>
    <f:input bean="${bean}" property="${property}" />
  </g:else>
</g:applyLayout>
