<g:applyLayout name="field" params="${['propertyId': property + '-date']}">
  <span data-get-organization-url="${createLink(controller: 'organization', action: 'getTermOfPayment')}">
    <g:dateInput name="${property}" precision="${precision ?: 'day'}"
      value="${value}" />
  </span>
  <content tag="fieldMessages">
    <li class="control-message-info">
      <g:message code="default.format.date${((precision ?: 'day') in ['hour', 'minute']) ? 'time' : ''}.label" />
    </li>
  </content>
</g:applyLayout>
