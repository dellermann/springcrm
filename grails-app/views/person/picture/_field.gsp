<g:applyLayout name="field">
  <g:hiddenField name="pictureRemove" value="0" />
  <input type="file" name="picture" />
  <g:if test="${personInstance?.picture}">
  <div class="document-preview">
    <a id="picture" href="${createLink(action: 'getPicture', id: personInstance?.id)}" data-img-dir="${resource(dir: 'images/lightbox')}"><img src="${createLink(action: 'getPicture', id: personInstance?.id)}" alt="${personInstance?.toString()}" title="${personInstance?.toString()}" height="100" /></a>
  </div>
  <ul class="document-preview-links">
    <li class="document-delete"><g:button color="red" size="small" icon="trash-o" message="person.picture.delete" /></li>
  </ul>
  </g:if>
</g:applyLayout>
