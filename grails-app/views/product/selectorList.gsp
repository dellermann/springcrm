<%@ page import="org.amcworld.springcrm.Product" %>
<g:if test="${productInstanceList}">
<g:letterBar clazz="${Product}" property="name" numLetters="3" separator="-"/>
<table class="content-table">
  <thead>
    <tr>
      <!--<th><input type="checkbox" id="product-multop-sel" class="multop-sel" /></th>-->
      <g:sortableColumn property="number" title="${message(code: 'product.number.label', default: 'Number')}" />
      <g:sortableColumn property="name" title="${message(code: 'product.name.label', default: 'Name')}" />
      <th><g:message code="product.category.label" default="Category" /></th>
      <g:sortableColumn property="quantity" title="${message(code: 'product.quantity.label', default: 'Quantity')}" />
      <th><g:message code="product.unit.label" default="Unit" /></th>
      <g:sortableColumn property="unitPrice" title="${message(code: 'product.unitPrice.label', default: 'Unit Price')}" />
    </tr>
  </thead>
  <tbody>
  <g:each in="${productInstanceList}" status="i" var="productInstance">
    <tr>
      <!--<td><input type="checkbox" id="product-multop-${productInstance.id}" class="multop-sel-item" /></td>-->
      <td style="text-align: center;"><g:link action="get" id="${productInstance.id}" class="select-link">${fieldValue(bean: productInstance, field: "fullNumber")}</g:link></td>
      <td><g:link action="get" id="${productInstance.id}" class="select-link">${fieldValue(bean: productInstance, field: "name")}</g:link></td>
      <td>${fieldValue(bean: productInstance, field: "category")}</td>
      <td style="text-align: right;">${fieldValue(bean: productInstance, field: "quantity")}</td>
      <td>${fieldValue(bean: productInstance, field: "unit")}</td>
      <td style="text-align: right;">${formatNumber(number: productInstance?.unitPrice, minFractionDigits: 2)} €</td>
    </tr>
  </g:each>
  </tbody>
</table>
<div class="paginator">
  <g:paginate total="${productInstanceTotal}" />
</div>
</g:if>
<g:else>
  <div class="empty-list"><p><g:message code="default.list.empty" /></p></div>
</g:else>