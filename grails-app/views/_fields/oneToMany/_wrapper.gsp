<g:applyLayout name="${orientation == 'vertical' ? 'field-vertical' : 'field'}">
  <f:widget bean="${bean}" property="${property}"/>
  <g:if test="${constraints.minSize}">
  <content tag="fieldMessages">
    <li class="info-msg">
      <g:message code="default.required" default="required" />
    </li>
  </content>
  </g:if>
</g:applyLayout>
