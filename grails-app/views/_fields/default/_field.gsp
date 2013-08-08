<g:applyLayout name="field">
  <g:if test="${'textarea' == constraints?.widget}">
  <g:textArea name="${property}" cols="${cols ?: 80}" rows="${rows ?: 5}"
    value="${value}" required="${required ? 'required' : ''}" />
  </g:if>
  <g:else>
  <f:input bean="${bean}" property="${property}" size="${size}" />
  </g:else>
</g:applyLayout>
