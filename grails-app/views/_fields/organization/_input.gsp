<select id="${property}-select" name="${property}.id"
  data-find-url="${createLink(controller: 'organization', action: 'find')}">
  <g:if test="${value}">
  <option value="${value.id}">${value}</option>
  </g:if>
</select>
