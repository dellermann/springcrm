<g:applyLayout name="field">
  <g:select name="users" from="${org.amcworld.springcrm.User.list()}"
    value="${value}" optionKey="id" multiple="true" size="${size}"
    style="${style}" />
</g:applyLayout>
