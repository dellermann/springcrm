<g:applyLayout name="field">
  <g:select name="${property}"
    from="${persistentProperty.referencedPropertyType.list()}" value="${value}"
    optionKey="id" multiple="true" size="${size}" style="${style}" /><br />
  <g:if test="${constraints.minSize}"><span class="info-msg"><g:message code="default.required" default="required" /></span></g:if>
</g:applyLayout>