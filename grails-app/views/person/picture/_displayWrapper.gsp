<g:applyLayout name="fieldDisplay">
  <g:if test="${bean?.picture}">
  <g:link action="getPicture" id="${bean?.id}" data-toggle="lightbox"
    data-title="${bean.fullName}"
    ><img src="${createLink(action: 'getPicture', id: bean?.id)}"
      alt="${bean?.toString()}" title="${bean?.toString()}" height="100"
    />
  </g:link>
  </g:if>
</g:applyLayout>
