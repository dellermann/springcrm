<g:applyLayout name="field">
  <g:hiddenField name="pictureRemove" value="0"/>
  <input type="file" id="picture" name="picture"
    accept="image/jpeg,image/png,image/gif"
    data-picture="${
      bean?.picture ? createLink(action: 'getPicture', id: bean?.id) : ''
    }"
    data-caption="${bean.fullName}" data-size="${bean.picture?.length}"
    data-max-file-size="1024"/>
</g:applyLayout>
