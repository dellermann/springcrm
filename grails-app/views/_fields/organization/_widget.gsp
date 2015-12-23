<%@ page import="grails.util.GrailsNameUtils" %>

<select id="${GrailsNameUtils.getScriptName(property)}-select"
  name="${property}.id"
  data-find-url="${createLink(controller: 'organization', action: 'find')}"
  data-reset-on-change="#person-select, #quote-select, #sales-order-select, #invoice-select, #dunning-select"
  aria-controls="person-select quote-select sales-order-select invoice-select dunning-select billingAddr.street billingAddr.poBox billingAddr.postalCode billingAddr.location billingAddr.state billingAddr.country shippingAddr.street shippingAddr.poBox shippingAddr.postalCode shippingAddr.location shippingAddr.state shippingAddr.country">
  <g:if test="${value}">
  <option value="${value.id}">${value}</option>
  </g:if>
</select>
