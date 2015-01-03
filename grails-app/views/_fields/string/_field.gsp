<g:applyLayout name="field">
  <g:if test="${'textarea' == constraints?.widget}">
  <textarea name="${property}" id="${property}" class="form-control"
    rows="${rows ?: 5}"${required ? ' required="required"' : ''}>${value}</textarea>
  </g:if>
  <g:else>
  <f:input type="${constraints?.widget ?: 'text'}" bean="${bean}"
    property="${property}" class="form-control" />
  </g:else>
</g:applyLayout>
