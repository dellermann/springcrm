<%@ page import="org.amcworld.springcrm.Product" %>
<div class="selector-toolbar">
  <div class="selector-letter-bar">
    <g:letterBar clazz="${Product}" property="name" numLetters="3" separator="-" where="name like '%${params.search ?: ''}%'" />
  </div>
  <div class="selector-toolbar-search">
    <g:form action="selectorList">
      <g:textField id="selector-search" name="search" value="${params.search}" />
      <button type="submit" class="search-btn"><g:message code="default.search.button.label" default="Search" /></button>
    </g:form>
  </div>
</div>
<g:if test="${productInstanceList}">
<table class="content-table">
  <thead>
    <tr>
      <%--<th><input type="checkbox" id="product-multop-sel" class="multop-sel" /></th>--%>
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
      <%--<td><input type="checkbox" id="product-multop-${productInstance.id}" class="multop-sel-item" /></td>--%>
      <td class="align-center"><g:link action="get" id="${productInstance.id}" class="select-link">${fieldValue(bean: productInstance, field: "fullNumber")}</g:link></td>
      <td><g:link action="get" id="${productInstance.id}" class="select-link">${fieldValue(bean: productInstance, field: "name")}</g:link></td>
      <td>${fieldValue(bean: productInstance, field: "category")}</td>
      <td class="align-right">${fieldValue(bean: productInstance, field: "quantity")}</td>
      <td>${fieldValue(bean: productInstance, field: "unit")}</td>
      <td class="align-right">${formatCurrency(number: productInstance?.unitPrice)}</td>
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