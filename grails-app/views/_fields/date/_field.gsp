<g:applyLayout name="field" params="${['propertyId': property + '-date']}">
  <g:dateInput name="${property}" precision="${precision ?: 'day'}" value="${value}" /><br />
  <span class="info-msg"><g:message code="default.format.date${((precision ?: 'day') in ['hour', 'minute']) ? 'time' : ''}.label" /></span>
</g:applyLayout>