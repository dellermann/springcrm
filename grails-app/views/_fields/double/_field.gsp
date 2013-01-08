<g:applyLayout name="field">
  <f:input bean="${bean}" property="${property}" />
  <g:if test="${'currency' == constraints?.widget}">&nbsp;<g:currency /></g:if>
  <g:elseif test="${'percent' == constraints?.widget}">&nbsp;%</g:elseif>
  <br />
</g:applyLayout>