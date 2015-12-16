<g:applyLayout name="fieldDisplay">
  <g:if test="${bean?.recType & 1}"><g:message code="organization.recType.customer.label" /></g:if><g:if test="${!(bean?.recType ^ 3)}">, </g:if><g:if test="${bean?.recType & 2}"><g:message code="organization.recType.vendor.label" /></g:if>
</g:applyLayout>