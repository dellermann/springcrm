<%@ page import="grails.util.GrailsNameUtils" %>
<g:applyLayout name="fieldDisplay">
  <g:set var="val" value="${bean?."${property}"}" />
  <g:if test="${val}">
  <g:set var="text" value="${org.amcworld.springcrm.InvoicingTransaction.isAssignableFrom(type) ? val?.fullName : val}" />
  <g:link controller="${GrailsNameUtils.getPropertyName(type)}" action="show" id="${val?.id}">${text?.encodeAsHTML()}</g:link>
  </g:if>
</g:applyLayout>