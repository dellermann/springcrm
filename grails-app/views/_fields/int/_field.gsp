<g:applyLayout name="field">
  <g:if test="${constraints?.widget == 'autonumber'}">
  <g:autoNumber prefix="${seqNumberPrefix}" suffix="${seqNumberSuffix}"
    value="${value}" />
  </g:if>
  <g:else>
  <f:input bean="${bean}" property="${property}" size="${size}" />
  </g:else>
</g:applyLayout>
