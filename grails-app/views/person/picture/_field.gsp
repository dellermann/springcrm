<g:applyLayout name="field">
  <g:hiddenField name="pictureRemove" value="0" />
  <input type="file" name="picture" />
  <g:if test="${personInstance?.picture}">
  <div class="document-preview">
    <g:link action="getPicture" id="${personInstance?.id}"
      data-lightbox="person-picture"
      data-title="${personInstance.fullName}"
      ><img src="${createLink(action: 'getPicture', id: personInstance?.id)}"
        alt="${personInstance?.toString()}"
        title="${personInstance?.toString()}" height="100" />
    </g:link>
  </div>
  <ul class="document-preview-links">
    <li class="document-delete"><g:button color="red" size="small" icon="trash-o" message="person.picture.delete" /></li>
  </ul>
  </g:if>
</g:applyLayout>
