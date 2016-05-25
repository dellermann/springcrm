<g:applyLayout name="field">
  <g:hiddenField name="pictureRemove" value="0" />
  <input type="file" id="picture" name="picture"
    accept="image/jpeg,image/png,image/gif"
    data-picture="${personInstance?.picture ? createLink(action: 'getPicture', id: personInstance?.id) : ''}"
    data-max-file-size="1024"/>
</g:applyLayout>
