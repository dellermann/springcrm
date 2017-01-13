<g:if test="${required}">
  <g:textField type="${constraints?.widget ?: 'text'}" name="${property}"
    value="${value}" class="form-control" required="required"/>
</g:if>
<g:else>
  <g:textField type="${constraints?.widget ?: 'text'}" name="${property}"
    value="${value}" class="form-control"/>
</g:else>
