<%@ page import="grails.util.GrailsNameUtils" %>
<%@ page import="org.amcworld.springcrm.InvoicingTransaction" %>

<g:set var="val" value="${bean?."${property}"}"/>
<g:if test="${val}">
  <g:set var="text" value="${
    InvoicingTransaction.isAssignableFrom(type) ? fullName(bean: val) : val
  }"/>
  <sec:access controller="${GrailsNameUtils.getPropertyName(type)}"
    action="show">
  <g:link controller="${GrailsNameUtils.getPropertyName(type)}" action="show"
    id="${val?.id?.toString()}">${text?.encodeAsHTML()}</g:link
  >
  </sec:access>
  <sec:noAccess controller="${GrailsNameUtils.getPropertyName(type)}"
    action="show">
    ${text?.encodeAsHTML()}
  </sec:noAccess>
</g:if>
