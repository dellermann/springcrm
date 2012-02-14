<g:applyLayout name="field">
  <g:hiddenField name="pictureRemove" value="0" />
  <input type="file" name="picture" /><br />
  <g:if test="${personInstance?.picture}">
  <div class="document-preview">
    <a id="picture" href="${createLink(action: 'getPicture', id: personInstance?.id)}" data-img-dir="${resource(dir: 'img/lightbox')}"><img src="${createLink(action: 'getPicture', id: personInstance?.id)}" alt="${personInstance?.toString()}" title="${personInstance?.toString()}" height="100" /></a>
  </div>
  <ul class="document-preview-links">
    <li class="document-delete"><g:message code="person.picture.delete" /></li>
  </ul>
  </g:if>
</g:applyLayout>