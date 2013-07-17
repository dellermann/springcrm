<g:applyLayout name="field">
  <g:select name="${property}"
    from="${persistentProperty.referencedPropertyType.list()}" value="${value}"
    optionKey="id" multiple="true" /><br />
</g:applyLayout>