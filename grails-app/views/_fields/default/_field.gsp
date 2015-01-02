<g:applyLayout name="field">
  <g:if test="${'textarea' == constraints?.widget}">
  <g:textArea name="${property}" class="form-control" rows="${rows ?: 5}"
    value="${value}" required="${required ? 'required' : ''}" />
  </g:if>
  <g:else>
  <f:input bean="${bean}" property="${property}" class="form-control" />
  </g:else>
</g:applyLayout>
