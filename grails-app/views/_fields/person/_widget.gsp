<select id="${property}-select" name="${property}.id"
  data-find-url="${createLink(controller: 'person', action: 'find')}"
  data-filter-organization="#organization-select">
  <g:if test="${value}">
  <option value="${value.id}">${value.fullName}</option>
  </g:if>
</select>
