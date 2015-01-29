<g:applyLayout name="field">
  <g:hiddenField name="pictureRemove" value="0" />
  <input type="file" id="picture" name="picture" accept="image/*"
    data-picture="${personInstance?.picture ? createLink(action: 'getPicture', id: personInstance?.id) : ''}" />
</g:applyLayout>
