<g:applyLayout name="field">
  <g:select name="${property}"
    from="${persistentProperty.referencedPropertyType.list()}" value="${value}"
    optionKey="id" multiple="true" size="${size}" style="${style}" />
  <content tag="fieldMessages">
  <g:if test="${constraints.minSize}">
    <li class="info-msg">
      <g:message code="default.required" default="required" />
    </li>
  </g:if>
  </content>
</g:applyLayout>
