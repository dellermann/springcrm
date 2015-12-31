<g:applyLayout name="field">
  <g:if test="${'textarea' == constraints?.widget}">
  <textarea name="${property}" id="${property}" class="form-control"
    rows="${rows ?: 5}"${required ? ' required="required"' : ''}>${value}</textarea>
  </g:if>
  <g:elseif test="${constraints?.password}">
  <f:widget type="password" bean="${bean}" property="${property}"
    class="form-control" />
  </g:elseif>
  <g:else>
  <f:widget type="${constraints?.widget ?: 'text'}" bean="${bean}"
    property="${property}" class="form-control" />
  </g:else>
</g:applyLayout>
