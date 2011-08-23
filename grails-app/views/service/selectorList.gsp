<%@ page import="org.amcworld.springcrm.Service" %>
<g:if test="${serviceInstanceList}">
<g:letterBar clazz="${Service}" property="name" numLetters="3" separator="-"/>
<table class="content-table">
  <thead>
    <tr>
      <!--<th><input type="checkbox" id="service-multop-sel" class="multop-sel" /></th>-->
      <g:sortableColumn property="number" title="${message(code: 'service.number.label', default: 'Number')}" />
      <g:sortableColumn property="name" title="${message(code: 'service.name.label', default: 'Name')}" />
      <th><g:message code="service.category.label" default="Category" /></th>
      <g:sortableColumn property="quantity" title="${message(code: 'service.quantity.label', default: 'Quantity')}" />
      <th><g:message code="service.unit.label" default="Unit" /></th>
      <g:sortableColumn property="unitPrice" title="${message(code: 'service.unitPrice.label', default: 'Unit Price')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${serviceInstanceList}" status="i" var="serviceInstance">
    <tr>
      <!--<td><input type="checkbox" id="service-multop-${serviceInstance.id}" class="multop-sel-item" /></td>-->
      <td style="text-align: center;"><g:link action="get" id="${serviceInstance.id}" class="select-link">${fieldValue(bean: serviceInstance, field: "fullNumber")}</g:link></td>
      <td><g:link action="get" id="${serviceInstance.id}" class="select-link">${fieldValue(bean: serviceInstance, field: "name")}</g:link></td>
      <td>${fieldValue(bean: serviceInstance, field: "category")}</td>
      <td style="text-align: right;">${fieldValue(bean: serviceInstance, field: "quantity")}</td>
      <td>${fieldValue(bean: serviceInstance, field: "unit")}</td>
      <td style="text-align: right;">${formatNumber(number: serviceInstance?.unitPrice, minFractionDigits: 2)} €</td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${serviceInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>