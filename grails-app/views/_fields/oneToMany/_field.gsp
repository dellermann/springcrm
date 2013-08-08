<g:applyLayout name="field">
  <g:select name="${property}"
    from="${persistentProperty.referencedPropertyType.list()}" value="${value}"
    optionKey="id" multiple="true" size="${size}" style="${style}" />
  <g:if test="${constraints.minSize}">
  <content tag="fieldMessages">
    <li class="info-msg">
      <g:message code="default.required" default="required" />
    </li>
  </content>
  </g:if>
</g:applyLayout>
