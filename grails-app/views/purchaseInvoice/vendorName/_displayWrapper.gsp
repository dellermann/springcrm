<g:applyLayout name="fieldDisplay">
  <g:if test="${bean?.vendor}">
  <g:link controller="organization" action="show" id="${bean?.vendor?.id}"
    >${bean?.vendorName?.encodeAsHTML()}</g:link>
  </g:if>
  <g:else>
  ${bean?.vendorName?.encodeAsHTML()}
  </g:else>
</g:applyLayout>

