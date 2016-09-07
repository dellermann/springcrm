<span data-get-organization-url="${createLink(controller: 'organization', action: 'getTermOfPayment')}">
  <g:dateInput name="${property}" precision="${precision ?: 'day'}"
    value="${value}"/>
</span>
