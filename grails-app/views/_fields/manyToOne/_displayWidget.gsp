<%@ page import="grails.util.GrailsNameUtils" %>
<%@ page import="org.amcworld.springcrm.InvoicingTransaction" %>

<g:set var="val" value="${bean?."${property}"}"/>
<g:if test="${val}">
<g:set var="text" value="${
  InvoicingTransaction.isAssignableFrom(type) ? fullName(bean: val) : val
}"/>
<g:if test="${session.credential?.checkAllowedControllers([GrailsNameUtils.getPropertyName(type)] as Set)}">
<g:link controller="${GrailsNameUtils.getPropertyName(type)}" action="show"
  id="${val?.id}">${text?.encodeAsHTML()}</g:link
>
</g:if>
<g:else>${text?.encodeAsHTML()}</g:else>
</g:if>
