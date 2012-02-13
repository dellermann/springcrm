<g:applyLayout name="field">
  <g:if test="${constraints.widget == 'currency'}">
    <f:input bean="${bean}" property="${property}" value="${formatNumber(number: value, minFractionDigits: 2)}" size="8" />&nbsp;<g:currency /><br />
  </g:if>
  <g:elseif test="${constraints.widget == 'percent'}">
    <f:input bean="${bean}" property="${property}" value="${formatNumber(number: value, minFractionDigits: 2)}" size="8" />&nbsp;%<br />
  </g:elseif>
  <g:else>
    <f:input bean="${bean}" property="${property}" />
  </g:else>
</g:applyLayout>