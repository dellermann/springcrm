<%@ page import="grails.util.GrailsNameUtils" %>

<select id="${GrailsNameUtils.getScriptName(property)}-select"
  name="${property}.id" class="invoicing-transaction-selector"
  data-find-url="${createLink(controller: property, action: 'find')}"
  data-filter-organization="#organization-select"
  data-value="${value ? [
      id: value.id, number: value.fullNumber, name: value.subject,
      fullName: value.fullName
    ].encodeAsJSON() : ''}">
</select>
