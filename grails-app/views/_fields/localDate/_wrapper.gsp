<g:applyLayout name="${orientation == 'vertical' ? 'field-vertical' : 'field'}"
  params="${['propertyId': property + '-date']}">
  <f:widget bean="${bean}" property="${property}"/>
  <content tag="fieldMessages">
    <li class="control-message-info">
      <g:message code="default.format.date${((precision ?: 'day') in ['hour', 'minute']) ? 'time' : ''}.label"/>
    </li>
  </content>
</g:applyLayout>
